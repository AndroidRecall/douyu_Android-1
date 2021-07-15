package com.swbg.mlivestreaming.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AbsoluteLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lzf.easyfloat.EasyFloat
import com.swbg.mlivestreaming.GlobeStatusViewHolder
import com.swbg.mlivestreaming.R
import com.swbg.mlivestreaming.bean.WebViewBtnBean
import com.swbg.mlivestreaming.jumpIsToGamePageDialog
import com.swbg.mlivestreaming.provider.StoredUserSources
import com.swbg.mlivestreaming.singleClick
import com.swbg.mlivestreaming.ui.home.HomeViewModel
import com.swbg.mlivestreaming.ui.mine.walnut.ChargeCenterActivity
import com.swbg.mlivestreaming.utils.LogUtils
import com.swbg.mlivestreaming.utils.Utils
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_base_web_view.*
import kotlinx.android.synthetic.main.title_bar_simple.*


class BaseWebViewActivity : MBaseActivity() {
    var listBtn = listOf<WebViewBtnBean>()
    var currentMainUrl : String? = ""
    override val contentViewLayoutId: Int
        get() = R.layout.activity_base_web_view

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun initView() {
        ibReturn.apply {
            singleClick {
                finishView()
            }
        }
        iftTitle.text = intent.getStringExtra(EXTRA_TITLE)
        if (intent.getStringExtra(EXTRA_TITLE).isNullOrEmpty()) {
            titleBar.visibility = View.GONE
        }
        extraTitle?.let {
            when (it) {
                getString(R.string.wallet) -> {
                    iftTitle.text = ""
                }
            }
        }
//        webView.settings.apply {
//            setDomStorageEnabled(true)
//            domStorageEnabled = true
//            javaScriptEnabled = true
//            useWideViewPort = true
//            loadWithOverviewMode = true
//            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
//        }

        val webSettings = webView.getSettings();
        webSettings.setDomStorageEnabled(true);//主要是这句
        webSettings.setJavaScriptEnabled(true);//启用js
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webView.setWebChromeClient(WebChromeClient());//这行最好不要丢掉
        //该方法解决的问题是打开浏览器不调用系统浏览器，直接用webview打开

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(p0: WebView?, p1: Int) {
                super.onProgressChanged(p0, p1)
                progressBar.setProgress(p1)
                progressBar.visibility = if (p1 in 0 until 100) View.VISIBLE else View.GONE
            }


        }
        LogUtils.e("link==", "$link")
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                extraTitle?.let {
                    when (it) {
                        getString(R.string.wallet) -> {
                            iftTitle.text = p0?.title
                        }
                    }
                }
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                return try {
                    view?.post {
                        val interruputUrl = view.url
                        LogUtils.i("==url==", "==$interruputUrl")
                    }
                    super.shouldInterceptRequest(view, request)
                } catch (e: Exception) {
                    LogUtils.i("==url==", "錯誤")
                    null
                }
            }

        }

        webView.loadUrl(link)

        if (isMainGamePage || isGamePage) {
            if (GlobeStatusViewHolder.isNotNeedLogin(this)) homeViewModel.getGameLink(hashMapOf("game_id" to "0"))
        }

        if (isGamePage) {
            EasyFloat.with(this).setLayout(R.layout.float_rv).registerCallback {
                createResult { b, s, view ->
                    view?.findViewById<RecyclerView>(R.id.rv_btn_float)?.apply {
                        adapter = mAdapter
                        layoutManager = LinearLayoutManager(this@BaseWebViewActivity).apply {
                            addItemDecoration(object : RecyclerView.ItemDecoration() {
                                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                                    outRect.set(0, Utils().dp2px(context, 10).toInt(), 0, 0)
                                }
                            })
                        }
                    }
                    listBtn = listOf(WebViewBtnBean(R.mipmap.game_one, true),
                        WebViewBtnBean(R.mipmap.game_two),
                        WebViewBtnBean(R.mipmap.game_three),
                        WebViewBtnBean(R.mipmap.game_four),
                        WebViewBtnBean(R.mipmap.game_five))
                    mAdapter.refresh(listBtn, null)
                }

            }.show()

        }

    }

    private val homeViewModel by getViewModel(HomeViewModel::class.java) {
        _getGameLinkData.observe(it, Observer {
            it?.let {
                currentMainUrl = it.url
                //首页过来需要判断弹框
                if (isMainGamePage) jumpIsToGamePageDialog(it, this@BaseWebViewActivity)
            }
        })
    }
    private val mAdapter by lazy {
        WebViewBtnAdapter(this) {
            when (listBtn[it].imageUrl) {
                R.mipmap.game_one -> {
                    initOne(listBtn[listBtn.lastIndex].isShow)
                }
                R.mipmap.game_two -> {
//                    webView.clearHistory()
                    webView.loadUrl(currentMainUrl)
                }
                R.mipmap.game_three -> {
                    webView.reload()
                }
                R.mipmap.game_four -> {
                    finishView()
                }
                R.mipmap.game_five -> {
                    StoredUserSources.getUserInfo()?.user?.apply {

                        startActivityWithTransition(ChargeCenterActivity.open(this@BaseWebViewActivity))

//                        jumpIsToGamePageDialog(GetGameLinkBean(alert = Alert(balance,
//                            "",
//                            "$game_balance")), this@BaseWebViewActivity)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack()
        else super.onBackPressed()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initOne(show: Boolean) {
        for (lb in listBtn) {
            if (lb != listBtn.first()) {
                lb.isShow = !show
            }
        }
        mAdapter.refresh(listBtn, null)
    }


    private val link by lazy {
        intent.getStringExtra(EXTRA_LINK) ?: ""
    }

    private val isGamePage by lazy {
        intent.getBooleanExtra(IS_GAME_PAGE, false)
    }

    private val isMainGamePage by lazy {
        intent.getBooleanExtra(IS_MAIN_GAME_PAGE, false)
    }

    private val extraTitle by lazy {
        intent.getStringExtra(EXTRA_TITLE)
    }


    companion object {
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_LINK = "EXTRA_LINK"
        private const val IS_GAME_PAGE = "IS_GAME_PAGE"
        private const val IS_MAIN_GAME_PAGE = "IS_MAIN_GAME_PAGE"

        @SuppressLint("SetJavaScriptEnabled")
        fun open(context: Context, title: String, link: String?, isGamePage: Boolean = false, isMainGamePage: Boolean = false): Intent {
            return Intent(context, BaseWebViewActivity::class.java).putExtra(EXTRA_TITLE, title)
                .putExtra(EXTRA_LINK, link).putExtra(IS_MAIN_GAME_PAGE, isMainGamePage)
                .putExtra(IS_GAME_PAGE, isGamePage)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }


}
