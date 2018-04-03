package com.d1m.wechat.exception;


/**
 * xml util class exception
 * @author Huang Jiaming
 */
@SuppressWarnings("serial")
public class XmlConvertException extends Exception {
    public final static int XML_CONVERT_TO_OBJECT_ERROR = -40301;
    public final static int OBJECT_CONVERT_TO_XML_ERROR = -40302;

    private int code;

    private static String getMessage(int code) {
        switch (code) {
        case XML_CONVERT_TO_OBJECT_ERROR:
            return "xml convert to object failed.";
        case OBJECT_CONVERT_TO_XML_ERROR:
            return "object convert to xml failed.";
        default:
            return null;
        }
    }

    public int getCode() {
        return code;
    }

    public XmlConvertException(int code) {
        super(getMessage(code));
        this.code = code;
    }
}
