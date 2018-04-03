package com.d1m.wechat.common;

public enum MessageTypeEnum {

    TEXT("text", 1),
    IMAGE("image", 2),
    VOICE("voice", 3),
    VIDEO("video", 4),
    SHORTVIDEO("shortvideo", 4),
    NEWS("news",5),
    LOCATION("location");

    private final int intValue;
    private final String value;
    private MessageTypeEnum(String value, int intValue) {
        this.value = value;
        this.intValue = intValue;
    }
    private MessageTypeEnum(String value) {
        this.value = value;
        intValue = 0;
    }

    public static MessageTypeEnum getByValue(String value) {
        for(MessageTypeEnum e : MessageTypeEnum.values()) {
            if(e.value.equals(value)) {
                return e;
            }
        }

        return null;
    }

    public static MessageTypeEnum getByIntValue(int intValue) {
        for(MessageTypeEnum e : MessageTypeEnum.values()) {
            if(e.intValue == intValue) {
                return e;
            }
        }

        return null;
    }

    public String getValue() {
        return value;
    }

    public int getIntValue() {
        return intValue;
    }
}
