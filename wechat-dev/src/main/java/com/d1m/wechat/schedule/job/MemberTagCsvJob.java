package com.d1m.wechat.schedule.job;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.dto.MemberDto;
import com.d1m.wechat.dto.MemberTagDto;
import com.d1m.wechat.dto.TaskDto;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.model.MemberMemberTag;
import com.d1m.wechat.model.MemberTag;
import com.d1m.wechat.model.MemberTagCsv;
import com.d1m.wechat.model.MemberTagType;
import com.d1m.wechat.model.Task;
import com.d1m.wechat.model.enums.MemberTagStatus;
import com.d1m.wechat.pamametermodel.MemberTagModel;
import com.d1m.wechat.schedule.ExecResult;
import com.d1m.wechat.schedule.IJob;
import com.d1m.wechat.service.MemberMemberTagService;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.service.MemberTagCsvService;
import com.d1m.wechat.service.MemberTagService;
import com.d1m.wechat.service.MemberTagTypeService;
import com.d1m.wechat.service.TaskService;
import com.d1m.wechat.util.AppContextUtils;

@Component
public class MemberTagCsvJob implements IJob{
	
	private Logger log = LoggerFactory.getLogger(MassConversationJob.class);
	
	@Autowired
	private MemberTagCsvService memberTagCsvService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberTagService memberTagService;
	
	@Autowired
	private MemberTagTypeService memberTagTypeService;
	
	@Autowired
	private MemberMemberTagService memberMemberTagService;
	
	@Override
	public void execute(JobExecutionContext context, TaskDto task) {
		ExecResult ret = new ExecResult();
		TaskService taskService = AppContextUtils.getBean(TaskService.class);
		String taskName = task.getTaskName();
		MemberTagCsv dto = memberTagCsvService.selectByTaskName(taskName);
		String data = dto.getData();
		Integer wechatId = dto.getWechatId();
		Integer creatorId = dto.getCreatorId();
		dto.setStatus((byte)1);
		memberTagCsvService.updateNotNull(dto);
		try {
			JSONObject json = JSON.parseObject(data);
			for(String openId : json.keySet()){
				JSONObject tagsJson = JSON.parseObject(json.getString(openId));
				List<MemberTagModel> memberTagModels = new ArrayList<MemberTagModel>();
				for(String tagType : tagsJson.keySet()){
					MemberTagType memberTagType = new MemberTagType();
					memberTagType.setName(tagType);
					memberTagType.setWechatId(wechatId);
					memberTagType = memberTagTypeService.selectOne(memberTagType);
					String[] tags = tagsJson.getString(tagType).split("\\|");
					for(String tag : tags){
						if(!tag.equals("")){
							MemberTagModel memberTagModel = new MemberTagModel();
							memberTagModel.setMemberTagTypeId(memberTagType.getId());
							memberTagModel.setName(tag);
							MemberTag memberTag = new MemberTag();
							memberTag.setName(tag);
							memberTag.setWechatId(wechatId);
							memberTag = memberTagService.selectOne(memberTag);
							if(memberTag!=null){
								memberTagModel.setId(memberTag.getId());
							}
							memberTagModels.add(memberTagModel);
						}
					}
				}
				
				List<MemberTag> memberTags = getMemberTags(wechatId, creatorId,
						memberTagModels);
				
				Date current = new Date();
				MemberDto memberDto = memberService.selectByOpenId(openId, wechatId);
				List<MemberMemberTag> memberMemberAddTags = new ArrayList<MemberMemberTag>();
				List<MemberTagDto> existMemberTags = memberDto.getMemberTags();
				MemberMemberTag memberMemberTag = null;
				for (MemberTag memberTag : memberTags) {
					if (!contains(existMemberTags, memberTag)) {
						memberMemberTag = new MemberMemberTag();
						memberMemberTag.setMemberId(memberDto.getId());
						memberMemberTag.setMemberTagId(memberTag.getId());
						memberMemberTag.setWechatId(wechatId);
						memberMemberTag.setCreatedAt(current);
						memberMemberAddTags.add(memberMemberTag);
					}
				}
				
				if (!memberMemberAddTags.isEmpty()) {
					memberMemberTagService.insertList(memberMemberAddTags);
				}
				
			}
			dto.setStatus((byte)2);
			memberTagCsvService.updateNotNull(dto);
			ret.setStatus(true);
		} catch (Exception e) {
			ret.setStatus(false);
			ret.setMsg(e.getMessage());
			log.error("会员导入批量加标签失败：" + e.getMessage());
		}
		
		Task entity = new Task();
		entity.setId(task.getId());
		entity.setLastExecTime(new Date());
		entity.setNextExecTime(context.getNextFireTime());
		if(ret.getStatus()){
			entity.setLastExecStatus((byte) 1);
		}else{
			entity.setLastExecStatus((byte) 0);
		}
		entity.setLastExecError(ret.getMsg());
		taskService.updateNotNull(entity);
		if(context.getNextFireTime()==null){
			try {
				taskService.changeStatus(task.getId(), "shutdown");
			} catch (SchedulerException e) {
				log.error("shutdown task-"+task.getTaskName()+" failed,msg: "+e.getMessage());
			} 
		}
	}
	
	private void writeException(String fileName, String[] content){
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(new File(fileName), true);
			for(String str : content){
				fileWriter.write(str + ",");
			}
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private List<MemberTag> getMemberTags(Integer wechatId, Integer userId,
			List<MemberTagModel> memberTagModels) throws WechatException {
		MemberTag memberTag = null;
		List<MemberTag> memberTags = new ArrayList<MemberTag>();
		Date current = new Date();
		for (MemberTagModel memberTagModel : memberTagModels) {
			memberTag = new MemberTag();
			if (memberTagModel.getId() != null) {
				memberTag.setWechatId(wechatId);
				memberTag.setId(memberTagModel.getId());
				memberTag = memberTagService.selectOne(memberTag);
				memberTags.add(memberTag);
			} else {
				memberTag.setWechatId(wechatId);
				memberTag.setName(memberTagModel.getName());
				memberTag.setCreatedAt(current);
				memberTag.setCreatorId(userId);
				memberTag.setStatus(MemberTagStatus.INUSED.getValue());
				memberTag.setWechatId(wechatId);
				memberTag.setMemberTagTypeId(memberTagModel
						.getMemberTagTypeId());
				memberTagService.save(memberTag);
				memberTags.add(memberTag);
			}

		}
		return memberTags;
	}
	
	private boolean contains(List<MemberTagDto> list, MemberTag memberTag) {
		if (list == null || list.isEmpty()) {
			return false;
		}
		for (MemberTagDto memberTagDto : list) {
			if (memberTagDto.getId().equals(memberTag.getId())) {
				return true;
			}
		}
		return false;
	}

}
