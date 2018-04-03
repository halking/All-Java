package com.d1m.wechat.controller.file;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.d1m.wechat.model.enums.FileType;
import com.d1m.wechat.util.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.dto.MaterialDto;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.model.Material;
import com.d1m.wechat.service.MaterialService;

@Controller
@RequestMapping("/upload")
public class UploadController extends BaseController {

	private static Logger log = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private MaterialService materialService;

	@RequestMapping(value = "image/new.json", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject uploadImage(
			@RequestParam(required = false) CommonsMultipartFile file,
			HttpServletResponse response, HttpSession session) {
		try {
			Upload upload = upload(file, Constants.IMAGE, Constants.NORMAL);
			Material material = materialService.createImage(
					getWechatId(session), getUser(session), upload);
			MaterialDto materialDto = new MaterialDto();
			materialDto.setId(material.getId());
			materialDto.setTitle(material.getName());
			materialDto.setUrl(material.getPicUrl());
			return representation(Message.FILE_UPLOAD_SUCCESS, materialDto);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}

	@RequestMapping(value = "video/new.json", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject uploadVideo(@RequestParam CommonsMultipartFile file,
			HttpServletResponse response, HttpSession session) {
		try {
			// Upload upload = upload(file, Constants.VIDEO);
			// Material material = materialService.createMaterialVideo(
			// getWechatId(session), getUser(session), upload);
			// return representation(Message.FILE_UPLOAD_SUCCESS,
			// material.getPicUrl());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}

	public static Upload upload(CommonsMultipartFile file, String type,
			String subType) throws Exception {
		if (file.isEmpty()) {
			throw new WechatException(Message.FILE_NOT_BLANK);
		}
		FileItem it = file.getFileItem();
		long size = it.getSize();
		log.info("Upload File Size: {}", size);
		FileUploadConfigUtil config = FileUploadConfigUtil.getInstance();
		if (size > ParamUtil.getLong(config.getValue(type + "_max_size"), 0L)) {
			throw new WechatException(Message.FILE_IS_TOO_BIG);
		}
		String fileName = it.getName();
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
				.toLowerCase();
		String imgExt = config.getValue(type + "_ext");
		if (!imgExt.contains(fileExt)) {
			throw new WechatException(Message.FILE_EXT_NOT_SUPPORT);
		}
		String format = DateUtil.yyyyMMddHHmmss.format(new Date());
		String newFileName = FileUtils.generateNewFileName(
				MD5.MD5Encode(fileName.replace("." + fileExt, "")), fileExt);

		File root = FileUtils
				.getUploadPathRoot(type + File.separator + subType);
		File dir = new File(root, format.substring(0, 6));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String absolutePath = dir.getAbsoluteFile().getAbsolutePath();
		log.info("dir path : " + absolutePath);
		it.write(new File(dir, newFileName));
		log.info("dir name : " + dir.getName());

		String accessPath = config.getValue("upload_url_base") + File.separator
				+ type + File.separator + subType + File.separator
				+ dir.getName() + File.separator + newFileName;
		log.info("accessPath : {}", accessPath);
		Upload upload = new Upload();
		upload.setNewFileName(newFileName);
		upload.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
		upload.setAbsolutePath(absolutePath + File.separator + newFileName);
		upload.setAccessPath(accessPath);
		return upload;
	}
	
	/**
	 * Upload file to WX from file URL.
	 * 
	 * @param filePath String
	 * @param type String
	 * @param subType String
	 * @return uploaded file
	 * @throws Exception
	 */
	public static Upload upload(String filePath, String type,
			String subType) throws Exception {
	
		URL fileUrl = new URL(filePath);
		long size = FileUtils.getFileSize(fileUrl);
		FileUploadConfigUtil config = FileUploadConfigUtil.getInstance();
		if (size > ParamUtil.getLong(config.getValue(type + "_max_size"), 0L)) {
			throw new WechatException(Message.FILE_IS_TOO_BIG);
		}
		String fileName = FilenameUtils.getName(fileUrl.getPath());
		FileType fileType = FileTypeUtil.getType(fileUrl.openStream());
		String fileExt = null;
		if(fileType!=null){
			fileExt = fileType.name().toLowerCase();
		}
		String imgExt = config.getValue(type + "_ext");
		if (!imgExt.contains(fileExt)) {
			throw new WechatException(Message.FILE_EXT_NOT_SUPPORT);
		}
		String format = DateUtil.yyyyMMddHHmmss.format(new Date());
		String newFileName = FileUtils.generateNewFileName(
				MD5.MD5Encode(fileName.replace("." + fileExt, "")), fileExt);

		File root = FileUtils
				.getUploadPathRoot(type + File.separator + subType);
		File dir = new File(root, format.substring(0, 6));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String absolutePath = dir.getAbsoluteFile().getAbsolutePath();
		log.info("dir path : " + absolutePath);
		//org.apache.commons.io.FileUtils.copyURLToFile(fileUrl, new File(dir, newFileName));
		HttpFileUtil.copy(filePath, dir + "/" + newFileName);
		log.info("dir name : " + dir.getName());

		String accessPath = config.getValue("upload_url_base") + "/"
				+ type + "/" + subType + "/"
				+ dir.getName() + "/" + newFileName;
		log.info("accessPath : {}", accessPath);
		Upload upload = new Upload();
		upload.setNewFileName(newFileName);
		if(fileName.indexOf(".")>-1){
			upload.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
		}else{
			upload.setFileName("wechat");
		}

		upload.setAbsolutePath(absolutePath + File.separator + newFileName);
		upload.setAccessPath(accessPath);
		return upload;
	}
	
	public static List<Upload> uploadList(CommonsMultipartFile[] files, String type,
			String subType) throws Exception {
		if (files.length == 0) {
			throw new WechatException(Message.FILE_NOT_BLANK);
		}
		List<Upload> list = new ArrayList<Upload>();
		for(CommonsMultipartFile file : files){
			FileItem it = file.getFileItem();
			long size = it.getSize();
			FileUploadConfigUtil config = FileUploadConfigUtil.getInstance();
			if (size > ParamUtil.getLong(config.getValue(type + "_max_size"), 0L)) {
				throw new WechatException(Message.FILE_IS_TOO_BIG);
			}
			String fileName = it.getName();
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
					.toLowerCase();
			String imgExt = config.getValue(type + "_ext");
			if (!imgExt.contains(fileExt)) {
				throw new WechatException(Message.FILE_EXT_NOT_SUPPORT);
			}
			String format = DateUtil.yyyyMMddHHmmss.format(new Date());
			String newFileName = FileUtils.generateNewFileName(
					MD5.MD5Encode(fileName.replace("." + fileExt, "")), fileExt);

			File root = FileUtils
					.getUploadPathRoot(type + File.separator + subType);
			File dir = new File(root, format.substring(0, 6));
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String absolutePath = dir.getAbsoluteFile().getAbsolutePath();
			log.info("dir path : " + absolutePath);
			it.write(new File(dir, newFileName));
			log.info("dir name : " + dir.getName());

			String accessPath = config.getValue("upload_url_base") + File.separator
					+ type + File.separator + subType + File.separator
					+ dir.getName() + File.separator + newFileName;
			log.info("accessPath : {}", accessPath);
			Upload upload = new Upload();
			upload.setNewFileName(newFileName);
			upload.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
			upload.setAbsolutePath(absolutePath + File.separator + newFileName);
			upload.setAccessPath(accessPath);
			list.add(upload);
		}
		
		return list;
	}
}
