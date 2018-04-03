package com.hal.utils;

/**
 * Basic methods about a String type.
 * 
 * @author Ling-Ling
 *
 */
public final class StringUtils {
    
    private StringUtils() {
    }
    
    /**
     * Tests if the string value is null or empty.
     * 
     * @param value The value to be tested.
     * @return True if the string is null or empty.
     */
    public static boolean isNullOrEmpty(String value) {
        boolean res = false;
        
        if (value == null) {
            res = true;
        }
        
        if (value != null && value.length() == 0) {
            res = true;
        }
        
        return res;
    }
    
    /**
     * Tests if the object value is null.
     * 
     * @param value The value to be tested.
     * @return True if the object is null.
     */
    public static boolean isNull(Object value) {
        boolean res = false;
        
        if (value == null) {
            res = true;
        }
        
        return res;
    }
    
    /***
     * Xml Escape
     * @param value
     * @return
     */
//    public static String xmlEscape(String value){
//       return HtmlUtils.htmlEscape(value);
//    }
    
    
}
