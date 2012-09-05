package com.tencent.eclipse.nior.util;

public class StringUtil {
	public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
	
	  public static boolean isBlank(String str) {
	        int strLen;
	        if (str == null || (strLen = str.length()) == 0) {
	            return true;
	        }
	        for (int i = 0; i < strLen; i++) {
	            if ((Character.isWhitespace(str.charAt(i)) == false)) {
	                return false;
	            }
	        }
	        return true;
	    }
}
