package com.mp.douyu.utils

import java.util.*
import java.util.regex.Pattern


object StringUtils {


    /***
     * 判断国外手机号码位数是否为7~11位
     */
    fun checkForeignPhone(foreignPhoneStr: String): Boolean {
        val regExp = "\\d{7,11}"
        val p = Pattern.compile(regExp)
        val m = p.matcher(foreignPhoneStr)
        return m.matches()
    }


    /**
     * 手机号判断
     */
    fun checkMobilePhone(phone: String?): Boolean {
       // val regExp = "^(13[0-9]|14[5-9]|15[0-3,5-9]|16[6]|17[0-8]|18[0-9]|19[89])\\d{8}\$"
        val regExp = "^(1[0-9])\\d{9}\$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(phone)
        return m.matches()
    }


    /**
     * 匹配电话号码，11到12位数字。
     * @param phone 待匹配的电话号码
     * @return true 匹配成功， false 匹配失败
     * @link https://wenku.baidu.com/view/d4108d57b84ae45c3b358c3a.html?mark_pay_doc=0&mark_rec_page=1&mark_rec_position=5&clear_uda_param=1
     */
    fun checkAllPhone(phone: String?): Boolean {
        return checkMobilePhone(phone) || checkTelephone(phone)
    }

    private fun checkTelephone(phone: String?): Boolean {
        val reg = "^0\\d{2,3}\\d{7,8}"
        val p = Pattern.compile(reg)
        val m = p.matcher(phone)
        return m.matches()
    }


    /**
     * 匹配邮箱
     * @param email 待匹配的邮箱
     * @return true 合法邮箱， false 不合法邮箱
     */
    fun checkEmail(email: String?): Boolean {
        return email != null && email.matches(
                ("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\" + ".[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?").toRegex())
    }


    fun checkPassword(password: String?): Boolean {
        val regex = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=\\-_|{}\\[\\]\\\\':;\",.<>/?]){6,20}$"
        return password != null && password.matches(regex.toRegex())
    }


    private fun getPhoneLineNumStr(s: String, vararg indexs: Int): String {
        val lineSb = StringBuilder(s)
        Arrays.sort(indexs)
        for (i in indexs.indices.reversed()) {
            if (indexs[i] > lineSb.length) continue
            lineSb.insert(indexs[i], "-")
        }
        return lineSb.toString()
    }

    fun formatPhoneNum(s: String?): String {
        if (s == null) return ""

        return if (s.length < 4) {
            s
        } else if (s.length < 8) {
            getPhoneLineNumStr(s, 3)
        } else {
            getPhoneLineNumStr(s, 3, 7)
        }
    }

    fun checkFloat(source: String?): Boolean {
        return source != null && source.matches("[\\d]{0,9}?(\\.[\\d]?)?".toRegex())
    }

    fun checkFloat2(source: String?): Boolean {
        return source != null && source.matches("[\\d]{0,13}?(\\.[\\d]{0,2}?)?".toRegex())
    }

    fun checkCode(code: String?): Boolean {
        return code != null && code.matches("[\\d]{4}".toRegex())
    }
}
