package com.mp.douyu.http.exception


class TemplateStatusException(code: Int, message: String = "") : MException(code, message) {
    companion object {
        const val STATE_TEMPLATE_UNLOAD = 0xf1
        const val STATE_TEMPLATE_LOADING = 0xf2
    }
}