package com.swbg.mlivestreaming.bean

import android.text.InputFilter

class InputEditBean(var shapeType: Int = 0, var inputContent: String? = "", var inputHint: String? = "", var inputTitle: String? = "", var lengthFilter: InputFilter.LengthFilter? = null, var inputType: Array<Int>? = null, var countdown: Long? = 0) {
}
