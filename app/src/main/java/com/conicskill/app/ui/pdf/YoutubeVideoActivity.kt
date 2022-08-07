package com.conicskill.app.ui.pdf

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.AbsoluteLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import butterknife.BindView
import com.conicskill.app.BuildConfig
import com.conicskill.app.R
import com.conicskill.app.base.BaseActivity
import com.conicskill.app.data.model.carousel.DisplayData
import com.conicskill.app.data.model.playlist.CategoryListItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.data.model.videoCouses.SubCategoryItem
import com.conicskill.app.data.model.videoCouses.VideoListItem
import com.conicskill.app.ui.home.EmptyFragment
import com.conicskill.app.ui.home.HomeFragment
import com.conicskill.app.ui.home.LiveClassesFragment
import com.conicskill.app.ui.home.TestOptionsFragment
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Exception

class YoutubeVideoActivity: BaseActivity() {
    private val RECOVERY_DIALOG_REQUEST = 1

    @BindView(R.id.webViewVideo)
    lateinit var webViewVideo: WebView

    @BindView(R.id.linearLayoutMain)
    lateinit var linearLayoutMain: LinearLayout

    @BindView(R.id.tab_layout)
    lateinit var tabLayout: TabLayout

    @BindView(R.id.pager)
    lateinit var viewPager2: ViewPager2

    @BindView(R.id.view)
    lateinit var overlayView: View

    @BindView(R.id.relativeLayoutWebView)
    lateinit var relativeLayoutWebView: RelativeLayout

    private var VIDEO_ID: String? = ""
    private var videoListItem: VideoListItem? = null
    var titles = arrayListOf<String>()
    var isLive: Boolean = false
    var courseListItem: CourseListItem? = null
    var categoryListItem: CategoryListItem? = null
    var subCategoryItem: SubCategoryItem? = null


    override fun layoutRes(): Int {
        return R.layout.activity_video
    }

    @SuppressLint(
        "SourceLockedOrientationActivity",
        "ClickableViewAccessibility",
        "WrongConstant",
        "SetJavaScriptEnabled"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        overlayView.setOnClickListener { view: View? -> }
        webViewVideo.isLongClickable = false
        webViewVideo.setOnLongClickListener { v: View? ->
            webViewVideo.performClick()
            webViewVideo.invalidate()
            val param = RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            overlayView.layoutParams = param
            false
        }
        webViewVideo.setOnClickListener { view: View? -> }
        webViewVideo.isHapticFeedbackEnabled = true
        webViewVideo.settings.javaScriptEnabled = true
        val newUA =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Safari/602.1.50"
        webViewVideo.webViewClient = WebViewClientImpl(this)
        webViewVideo.settings.userAgentString = newUA
        webViewVideo.settings.javaScriptEnabled = true
        webViewVideo.settings.loadWithOverviewMode = true
        webViewVideo.settings.useWideViewPort = true
        webViewVideo.settings.mediaPlaybackRequiresUserGesture = false
        webViewVideo.scrollBarStyle = 33554432
        webViewVideo.isScrollbarFadingEnabled = false
        webViewVideo.settings.allowFileAccess = true
        webViewVideo.settings.allowUniversalAccessFromFileURLs = true
        webViewVideo.settings.databaseEnabled = true
        webViewVideo.settings.setSupportZoom(true)
        webViewVideo.settings.domStorageEnabled = true
        webViewVideo.settings.builtInZoomControls = true
        webViewVideo.settings.displayZoomControls = false
        webViewVideo.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(
                webView: WebView,
                str: String,
                str2: String,
                jsResult: JsResult
            ): Boolean {
                jsResult.cancel()
                return true
            }

            override fun onJsConfirm(
                webView: WebView,
                str: String,
                str2: String,
                jsResult: JsResult
            ): Boolean {
                jsResult.cancel()
                return true
            }

            override fun onJsPrompt(
                webView: WebView,
                str: String,
                str2: String,
                str3: String,
                jsPromptResult: JsPromptResult
            ): Boolean {
                jsPromptResult.cancel()
                return true
            }

            override fun getDefaultVideoPoster(): Bitmap? {
                return if (super.getDefaultVideoPoster() == null) {
                    BitmapFactory.decodeResource(
                        this@YoutubeVideoActivity.resources,
                        R.mipmap.ic_launcher_foreground
                    )
                } else super.getDefaultVideoPoster()
            }
        }
        if (savedInstanceState == null) {
            if (intent.getStringExtra("videoId") != null) {
                VIDEO_ID = intent.getStringExtra("videoId")
                isLive = intent.getBooleanExtra("isLive", false)
                courseListItem = intent.getSerializableExtra("course") as CourseListItem?
                categoryListItem = intent.getSerializableExtra("category") as CategoryListItem?
                subCategoryItem = intent.getSerializableExtra("subCategory") as SubCategoryItem?
                try {
                    videoListItem = intent.getSerializableExtra("video") as VideoListItem
                } catch (e: Exception) {
                    val displayData = intent.getSerializableExtra("video") as DisplayData
                    videoListItem = VideoListItem()
                    videoListItem?.chatEnabled = displayData.chatEnabled
                    videoListItem?.chatId = displayData.chatId
                    videoListItem?.pdfUrl = displayData.pdfUrl
                }
                if (intent.getIntExtra("playerType", 1) == 1) {
                    val text = """<html>
                            <head><meta name="viewport" content="width=device-width, user-scalable=yes" /></head><style>
                            .resp-container {
                                position: relative;
                                overflow: hidden;
                                width: 100%;
                                height: 100%;
                            }
                            .resp-iframe {
                                position: absolute;
                                top: 0;
                                left: 0;
                                width: 100%;
                                height: 100%;
                                border: 0;
                            }
                            .rep-iframe-tophide{
                                position: absolute;
                                top: 0;
                                left: 0;
                                width: 100%;
                                height: 15%;
                                z-index:200;
                                background-color:transparent;
                            
                            }
                            </style>
                            <body style="margin: 0;>
                            <div class="resp-container">
                            <div class="rep-iframe-tophide">
                                      </div>
                            <iframe class="resp-iframe" src="https://www.youtube.com/embed/$VIDEO_ID?controls=1&modestbranding=1&rel=0&autoplay=1" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                            </div>
                            </body>
                            </html>
                            """
                    webViewVideo.loadDataWithBaseURL(null, text, "text/html", "utf-8", null)
                } else {
                    webViewVideo.loadData(getHTML(VIDEO_ID)!!, "text/html", "utf-8")
                }
            } else {
                finish()
            }
        } else {
            webViewVideo.restoreState(savedInstanceState)
        }
        if (videoListItem != null) {
            if (videoListItem?.chatEnabled.equals(
                    "1",
                    ignoreCase = true
                ) || videoListItem?.pdfUrl != null
            ) {
                if(isLive) {
                    titles = arrayListOf("PDF for Class", "Live Chat")
                } else {
                    titles = arrayListOf("PDF for Class", "Live Chat", "Download")
                }
                val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
                viewPager2.adapter = adapter
                viewPager2.isUserInputEnabled = false
                viewPager2.offscreenPageLimit = 5
                TabLayoutMediator(
                    tabLayout,
                    viewPager2
                ) { tab, position ->
                    if (position == 0) {
                        tab.icon = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_baseline_picture_as_pdf_24
                        )
                    } else if (position == 1) {
                        tab.icon = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_medical_chart
                        )
                    } else if (position == 2) {
                        tab.icon = ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_baseline_download_24
                        )
                    }
                }.attach()

                tabLayout.addOnTabSelectedListener(object :
                    TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        /*val tintColor =
                            ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark)
                        tab.icon?.setTint(tintColor)*/
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {
                        /*val tintColor =
                            ContextCompat.getColor(applicationContext, R.color.md_grey_500)
                        tab.icon?.setTint(tintColor)*/
                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {

                    }
                })

            } else {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                setFullscreen()
            }
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            setFullscreen()
        }

    }
    
    private fun setFullscreen() {
        window
            .decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    private fun setNormalScreen() {
        window
            .decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The [OnBackPressedDispatcher][.getOnBackPressedDispatcher] will be given a
     * chance to handle the back button before the default behavior of
     * [Activity.onBackPressed] is invoked.
     *
     * @see .getOnBackPressedDispatcher
     */
    override fun onBackPressed() {
        finish()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFullscreen()
            tabLayout.visibility = View.GONE
            viewPager2.visibility = View.GONE
            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            relativeLayoutWebView.layoutParams = param

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setNormalScreen()
            tabLayout.visibility = View.VISIBLE
            viewPager2.visibility = View.VISIBLE
            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                2.0f
            )
            relativeLayoutWebView.layoutParams = param
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webViewVideo.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        webViewVideo.restoreState(savedInstanceState)
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return titles.size
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return PdfFragment.newInstance(
                    videoListItem?.pdfUrl,
                    "youtube",
                    courseListItem,
                    categoryListItem,
                    subCategoryItem
                )
                1 -> return ChatFragment.newInstance(
                    videoListItem?.chatId,
                    videoListItem?.chatEnabled
                )
                2 -> return DownloadVideoFragment.newInstance(
                    courseListItem!!, categoryListItem!!, subCategoryItem!!, videoListItem!!
                )
            }
            return EmptyFragment.newInstance()
        }
    }


    fun getHTML(str: String?): String? {
        return "<style>.video {\n     position: absolute;\n     top: 0;\n     left: 0;\n     width: 100%;\n     height: 100%;\n }</style><iframe class=\"video\" scrolling=\"no\" style=\"border: 0; width: 100%; height: 100%;padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" src=\"http://www.youtube.com/embed/$str?&theme=dark&autohide=2&modestbranding=1&showinfo=0&autoplay=1\\fs=0\" frameborder=\"0\" allowfullscreen autobuffer controls onclick=\"this.play()\">\n</iframe>\n"
    }

    override fun onKeyDown(i: Int, keyEvent: KeyEvent?): Boolean {
        if (i != 4 || !webViewVideo.canGoBack()) {
            return super.onKeyDown(i, keyEvent)
        }
        webViewVideo.goBack()
        return true
    }

    class WebViewClientImpl(activity2: Activity?) : WebViewClient() {
        private var activity: Activity? = null
        override fun shouldOverrideUrlLoading(webView: WebView, str: String): Boolean {
            return true
        }

        override fun onPageFinished(webView: WebView, str: String) {
            super.onPageFinished(webView, str)
        }

        override fun onReceivedError(
            webView: WebView,
            webResourceRequest: WebResourceRequest,
            webResourceError: WebResourceError
        ) {
            super.onReceivedError(webView, webResourceRequest, webResourceError)
        }

        override fun onReceivedError(webView: WebView, i: Int, str: String, str2: String) {
            super.onReceivedError(webView, i, str, str2)
            webView.loadUrl("about:blank")
        }

        init {
            activity = activity2
        }
    }

    override fun onPause() {
        super.onPause()
        (getSystemService(AUDIO_SERVICE) as AudioManager).requestAudioFocus(
            { i: Int -> }, 3, 2
        )
    }
}