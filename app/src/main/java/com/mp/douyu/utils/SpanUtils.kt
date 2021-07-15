package com.mp.douyu.utils

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.widget.TextView
import androidx.annotation.ColorInt
import com.mp.douyu.MApplication
import com.mp.douyu.MApplication.Companion.getApplicationInstances
import com.mp.douyu.R
import com.mp.douyu.view.LiveImageSpan
import java.util.regex.Matcher
import java.util.regex.Pattern


object SpanUtils {
    val TAG = SpanUtils::class.java.simpleName
    fun makeWinResultContent(view: TextView, name: String, gameType: String, money: String) {
        val richText = String.format("恭喜\"%1$1s\"在%2$1s中了%3$1s元", name, gameType, money)
        val spanString = SpannableString(richText)
        val nameStart = richText.indexOf("\"$name\"")
        val nameEnd = richText.indexOf("\"$name\"") + "\"$name\"".length
        spanString.setSpan(ForegroundColorSpan(MApplication.getApplicationInstances.getResources().getColor (R.color.color_4688FF)),
        nameStart,
        nameEnd,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val winStart = richText.indexOf("中了")
        val winEnd = richText.indexOf("中了") + "中了".length
        spanString.setSpan(ForegroundColorSpan(MApplication.getApplicationInstances.getResources()
            .getColor(R.color.color_4688FF)), winStart, winEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val moneyStart = richText.indexOf(money)
        val moneyEnd = richText.indexOf(money) + money.length
        spanString.setSpan(ForegroundColorSpan(MApplication.getApplicationInstances.getResources()
            .getColor(R.color.color_CC481D)),
            moneyStart,
            moneyEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text = spanString
    }
    fun getLiveContent(memberManage: Int, userLevel: String, fansLevel: String, nickname: String, content: String?): SpannableStringBuilder? {
        var nickname = nickname
        val manageTxt: String = getApplicationInstances.getResources().getString(R.string.manager)
        nickname = "$nickname:"
        var richText = ""
        //        memberManage = 1;
        richText = if (memberManage == 1) {
            //            richText = String.format("%1$s %2$s %3$s %4$s %5$s", manageTxt, userLevel, fansLevel, nickname, content);
            String.format("%1\$s %2\$s %3\$s %4\$s %5\$s", " ", " ", " ", nickname, content)
        } else {
            //            richText = String.format("%1$s %2$s %3$s %4$s", userLevel, fansLevel, nickname, content);
            String.format("%1\$s %2\$s %3\$s %4\$s", " ", " ", nickname, content)
        }
        val builder = SpannableStringBuilder(richText)
        var manageEnd = 0
        if (memberManage == 1) {
            val manageStart = 0
            manageEnd = manageTxt.length
            builder.setSpan(LiveImageSpan(getApplicationInstances,
                R.drawable.ic_manager,
                manageTxt).setTextCenter(),
                manageStart,
                manageEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val userLevelStart = manageEnd + 1
        val userLevelEnd = userLevelStart + userLevel.length
        builder.setSpan(LiveImageSpan(getApplicationInstances, R.drawable.ic_user_level, userLevel),
            userLevelStart,
            userLevelEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val fansLevelStart = userLevelEnd + 1
        val fansLevelEnd = fansLevelStart + fansLevel.length
        builder.setSpan(LiveImageSpan(getApplicationInstances, R.drawable.ic_fans_level, fansLevel),
            fansLevelStart,
            fansLevelEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val nicknameStart = fansLevelStart + 1
        val nicknameEnd = nicknameStart + nickname.length
        builder.setSpan(TextAppearanceSpan(getApplicationInstances, R.style.style_16sp_yellow),
            nicknameStart,
            nicknameEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }

    /**
     * 关键字标红
     */
    fun matcherTitle(title: CharSequence?, keyword: String?, @ColorInt color: Int,size:Float?=14f): SpannableString? {
        val content = SpannableString(title)
        if (!TextUtils.isEmpty(keyword)) {
            try {
                val p: Pattern = Pattern.compile(keyword)
                val m: Matcher = p.matcher(content)
                while (m.find()) {
                    val start: Int = m.start()
                    val end: Int = m.end()

//                    content.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    val textAppearanceSpan = TextAppearanceSpan("sans-serif",
                        Typeface.NORMAL,
                        ScreenUtils.dp2px(size!!),
                        ColorStateList.valueOf(color),
                        ColorStateList.valueOf(color))
                    content.setSpan(textAppearanceSpan,
                        start,
                        end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            } catch (e: Exception) {
                Log.e(TAG, "matcherTitle Exception: $e")
            }
        }
        return content
    }
    /**
     * 关键字标红
     */
    fun matcherTitle2(title: CharSequence?, keyword: String?, @ColorInt color: Int): SpannableString? {
        val content = SpannableString(title)
        if (!TextUtils.isEmpty(keyword)) {
            try {
                val p: Pattern = Pattern.compile(keyword)
                val m: Matcher = p.matcher(content)
                while (m.find()) {
                    val start: Int = m.start()
                    val end: Int = m.end()

                    content.setSpan( ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                }
            } catch (e: Exception) {
                Log.e(TAG, "matcherTitle Exception: $e")
            }
        }
        return content
    }
}