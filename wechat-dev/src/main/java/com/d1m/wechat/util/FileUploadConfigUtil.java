package com.d1m.wechat.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUploadConfigUtil {

	private static Logger logger = LoggerFactory
			.getLogger(FileUploadConfigUtil.class);

	private static final Object LOCK = new Object();
	private static FileUploadConfigUtil CONFIG = null;
	private static ResourceBundle RB = null;
	private static final String CONFIG_FILE = "fileupload";

	public static FileUploadConfigUtil getInstance() {
        if (CONFIG == null) {
            synchronized (LOCK) {
                if (CONFIG == null) {
                    CONFIG = new FileUploadConfigUtil();
                }
            }
        }
		return (CONFIG);
	}

	private FileUploadConfigUtil() {
		RB = ResourceBundle.getBundle(CONFIG_FILE);
	}

	public String getValue(String key) {
		return (RB.getString(key));
	}

}
