package com.mp.douyu.utils;

import android.text.TextUtils;

public class HideDataUtil {
    /**
     *
     * 方法描述 隐藏银行卡号中间的字符串（使用*号），显示前四后四
     *
     */
    public static String hideCardNo(String cardNo) {
        if(TextUtils.isEmpty(cardNo)) {
            return cardNo;
        }

        int length = cardNo.length();
        int beforeLength = 4;
        int afterLength = 4;
        //替换字符串，当前使用“*”
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++) {
            if(i < beforeLength || i >= (length - afterLength)) {
                sb.append(cardNo.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }

        return sb.toString();
    }
    /**
     *
     * 方法描述 隐藏手机号中间位置字符，显示前三后三个字符
     */
    public static String hidePhoneNo(String phoneNo) {
        if(TextUtils.isEmpty(phoneNo)) {
            return phoneNo;
        }

        int length = phoneNo.length();
        int beforeLength = 3;
        int afterLength = 4;
        //替换字符串，当前使用“*”
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++) {
            if(i < beforeLength || i >= (length - afterLength)) {
                sb.append(phoneNo.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }

        return sb.toString();
    }

}
