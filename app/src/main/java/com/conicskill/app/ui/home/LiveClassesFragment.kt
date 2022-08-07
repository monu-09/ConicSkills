package com.conicskill.app.ui.home

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.conicskill.app.BuildConfig
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequest
import com.conicskill.app.data.model.candidateLogin.CandidateLoginRequestData
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequest
import com.conicskill.app.data.model.currentAffairs.CurrentAffairsRequestData
import com.conicskill.app.data.model.home.HomeLayoutRequest
import com.conicskill.app.data.model.home.HomeLayoutResponse
import com.conicskill.app.data.model.home.LayoutItem
import com.conicskill.app.data.model.videoCouses.CourseListItem
import com.conicskill.app.databinding.FragmentLiveClassesBinding
import com.conicskill.app.di.util.ViewModelFactory
import com.conicskill.app.ui.CourseDetailActivity
import com.conicskill.app.ui.SplashActivity
import com.conicskill.app.ui.courseDetail.CourseNameItemAdapter
import com.conicskill.app.ui.courseDetail.interfaces.CourseLayoutClickListener
import com.conicskill.app.ui.login.LoginViewModel
import com.conicskill.app.util.Constants
import com.conicskill.app.util.GlideApp
import com.conicskill.app.util.Utils
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import es.dmoral.toasty.Toasty
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList

class LiveClassesFragment : BaseFragment(), CourseLayoutClickListener {

    private lateinit var liveClassBinding: FragmentLiveClassesBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var mHomeViewModel: HomeViewModel
    lateinit var mLoginViewModel: LoginViewModel
    lateinit var oneAdapter: OneAdapter
    lateinit var courseNameItemAdapter: CourseNameItemAdapter
    var layoutItems: ArrayList<LayoutItem> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        liveClassBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_live_classes, container,
            false
        )
        return liveClassBinding.root
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_live_classes
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHomeViewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        mLoginViewModel = ViewModelProvider(this, viewModelFactory).get(
            LoginViewModel::class.java
        )
        layoutItems.clear()
        courseNameItemAdapter = CourseNameItemAdapter(requireContext(), layoutItems, this)
        liveClassBinding.recyclerViewTop.adapter = courseNameItemAdapter
        observeViewModel()
        val currentAffairsRequestData = CurrentAffairsRequestData()
        currentAffairsRequestData.companyId = BuildConfig.COMPANY_ID

        val currentAffairsRequest = CurrentAffairsRequest()
        currentAffairsRequest.token = mHomeViewModel.tinyDB.getString(Constants.AUTH_TOKEN)
        currentAffairsRequest.currentAffairsRequestData = currentAffairsRequestData
        val homeLayoutRequest =
            HomeLayoutRequest(ArrayList(), mHomeViewModel.tinyDB.getString(Constants.AUTH_TOKEN))
        mHomeViewModel.callHomeCoursesApi(homeLayoutRequest)

        liveClassBinding.recyclerViewCourses.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
        oneAdapter = OneAdapter(liveClassBinding.recyclerViewCourses)
            .attachItemModule(courseListItemModule().addEventHook(clickCourseItemHook()))

        liveClassBinding.cardViewTop.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .addToBackStack("videoListingFragment")
                .replace(R.id.screenContainer, VideoCourseListingFragment.newInstance())
                .commit()
        }

        liveClassBinding.buttonClickView.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .addToBackStack("videoListingFragment")
                .replace(R.id.screenContainer, VideoCourseListingFragment.newInstance())
                .commit()
        }
    }

    fun observeViewModel() {
        mHomeViewModel.homeLayoutResponse.observe(
            viewLifecycleOwner,
            { response: Response<HomeLayoutResponse?>? ->
                if (response?.body() != null) {
                    if (response.body()!!.code == HttpsURLConnection.HTTP_OK) {
                        for (i in response.body()?.data?.layout?.indices!!) {
                            if (response.body()?.data?.layout?.get(i)?.content != null &&
                                response.body()?.data?.layout?.get(i)?.content!!.isNotEmpty()
                            ) {
                                layoutItems.add(response.body()?.data?.layout?.get(i)!!)
                                if(layoutItems.size == 1) {
                                    layoutItems[0].isSelected = true
                                }
                            }
                        }
                        liveClassBinding.recyclerViewTop.layoutManager = LinearLayoutManager(
                            context,
                            RecyclerView.HORIZONTAL, false
                        )
                        liveClassBinding.recyclerViewTop.post {
                            courseNameItemAdapter.notifyDataSetChanged()
                            oneAdapter.setItems(layoutItems[0].content as ArrayList<CourseListItem>)
                        }
                        liveClassBinding.recyclerViewTop.visibility = View.VISIBLE
                    } else if (response.body()!!.code == 5005 || response.body()!!.code == 510) {
                        val data = CandidateLoginRequestData()
                        data.password = mHomeViewModel.tinyDB.getString(Constants.USER_PASSWORD)
                        data.mobile = mHomeViewModel.tinyDB.getString(Constants.USER_NAME)
                        data.notificationToken =
                            mHomeViewModel.tinyDB.getString(Constants.FCM_TOKEN)
                        data.appVersion = BuildConfig.VERSION_CODE
                        data.additionalInfo = Utils.getPhoneInfo()
                        data.loginVia = 1
                        data.companyId = BuildConfig.COMPANY_ID
                        val request = CandidateLoginRequest()
                        request.candidateLoginRequestData = data
                        mLoginViewModel.callLoginApi(request)
                    } else if (response.body()!!.code == 5030) {
                        Toasty.error(
                            requireContext(),
                            response.body()!!.message!!,
                            Toast.LENGTH_SHORT,
                            false
                        ).show()
                        //                    Utils.deleteDatabaseFile(getContext(), DatabaseConstant.DATABASE_NAME);
                        mLoginViewModel.tinyDB.putString(Constants.NAME, "")
                        mLoginViewModel.tinyDB.putString(
                            Constants.EMAIL,
                            ""
                        )
                        mLoginViewModel.tinyDB.putBoolean(
                            Constants.IS_LOGGED_IN,
                            false
                        )
                        val intent = Intent(context, SplashActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            LiveClassesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun layoutClick(layoutItem: LayoutItem, position: Int) {
        for(i in layoutItems.indices) {
            when (i) {
                position -> {
                    layoutItems[i].isSelected = true
                }
                else -> {
                    layoutItems[i].isSelected = false
                }
            }
        }
        liveClassBinding.recyclerViewTop.post {
            courseNameItemAdapter.notifyDataSetChanged()
        }
        oneAdapter.setItems(layoutItem.content as ArrayList<CourseListItem>)
    }

    private fun courseListItemModule(): ItemModule<CourseListItem> {
        return object : ItemModule<CourseListItem>() {
            override fun provideModuleConfig(): ItemModuleConfig {
                return object : ItemModuleConfig() {
                    override fun withLayoutResource(): Int {
                        return R.layout.item_video_course_home
                    }

                    override fun withFirstBindAnimation(): Animator? {
                        // can be implemented by inflating Animator Xml
                        return null
                    }
                }
            }

            override fun onBind(item: Item<CourseListItem>, viewBinder: ViewBinder) {
                val textViewNewsHeading =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewNewsHeading)
                val textViewNew = viewBinder.findViewById<AppCompatTextView>(R.id.textViewNew)
                val textViewSubDescription =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewSubDescription)
                val imageViewNews = viewBinder.findViewById<AppCompatImageView>(R.id.imageViewNews)
                val textViewTotalLecture =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewTotalLecture)
                val textViewTotalDuration =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewTotalDuration)
                val linearLayoutTotalLecture =
                    viewBinder.findViewById<LinearLayout>(R.id.linearLayoutVideoLecture)
                val textViewPrice = viewBinder.findViewById<AppCompatTextView>(R.id.textViewPrice)
                val textViewMRP = viewBinder.findViewById<AppCompatTextView>(R.id.textViewMRP)
                val textViewDiscount =
                    viewBinder.findViewById<AppCompatTextView>(R.id.textViewDiscount)
                linearLayoutTotalLecture.visibility = View.VISIBLE
                textViewNewsHeading.text = item.model.courseName
                if (item.model.description != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textViewSubDescription.text =
                            Html.fromHtml(item.model.description, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        textViewSubDescription.text = Html.fromHtml(item.model.description)
                    }
                }
                GlideApp.with(context!!).load(item.model.thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageViewNews)
                textViewTotalDuration.text = item.model.duration
                textViewTotalLecture.text = item.model.lectureCount
                if (item.model.isLive != null) {
                    if (item.model.isLive.equals("1", ignoreCase = true)) {
                        textViewNew.text = "LIVE"
                        textViewNew.visibility = View.VISIBLE
                        val anim = ObjectAnimator.ofInt(
                            textViewNew,
                            "backgroundColor",
                            Color.RED,
                            Color.BLUE,
                            Color.RED
                        )
                        anim.duration = 3000
                        anim.setEvaluator(ArgbEvaluator())
                        anim.repeatCount = Animation.INFINITE
                        anim.start()
                    } else {
                        if (item.model.isNew != null) {
                            if (item.model.isNew.equals("1", ignoreCase = true)) {
                                textViewNew.text = "NEW"
                                textViewNew.visibility = View.VISIBLE
                                val anim = ObjectAnimator.ofInt(
                                    textViewNew,
                                    "backgroundColor",
                                    Color.GREEN,
                                    Color.RED,
                                    Color.GREEN
                                )
                                anim.duration = 3000
                                anim.setEvaluator(ArgbEvaluator())
                                anim.repeatCount = Animation.INFINITE
                                anim.start()
                            } else {
                                textViewNew.visibility = View.GONE
                            }
                        } else {
                            textViewNew.visibility = View.GONE
                        }
                    }
                } else {
                    if (item.model.isNew != null) {
                        if (item.model.isNew.equals("1", ignoreCase = true)) {
                            textViewNew.text = "NEW"
                            textViewNew.visibility = View.VISIBLE
                            val anim = ObjectAnimator.ofInt(
                                textViewNew,
                                "backgroundColor",
                                Color.GREEN,
                                Color.RED,
                                Color.GREEN
                            )
                            anim.duration = 3000
                            anim.setEvaluator(ArgbEvaluator())
                            anim.repeatCount = Animation.INFINITE
                            anim.start()
                        } else {
                            textViewNew.visibility = View.GONE
                        }
                    } else {
                        textViewNew.visibility = View.GONE
                    }
                }
                if (item.model.mrp != null) {
                    textViewMRP.text = getString(R.string.txt_rs_symbol) + " " + item.model.mrp
                    if (!textViewMRP.paint.isStrikeThruText) {
                        textViewMRP.paintFlags =
                            textViewMRP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    } else {
                        textViewMRP.paintFlags =
                            textViewMRP.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    }
                }
                if (item.model.price != null) {
                    textViewPrice.text = getString(R.string.txt_rs_symbol) + " " + item.model.price
                }
                if (item.model.discountPercent != null) {
                    textViewDiscount.text = item.model.discountPercent + " %"
                }
                linearLayoutTotalLecture.visibility = View.GONE
            }
        }
    }

    private fun computeCardWidth(context: Context): Int {
        return (context.resources.displayMetrics.widthPixels
                - context.resources.getDimensionPixelSize(R.dimen.peek_width_large)
                - context.resources.getDimensionPixelSize(R.dimen.item_spacing_large))
    }

    private fun clickCourseItemHook(): ClickEventHook<CourseListItem> {
        return object : ClickEventHook<CourseListItem>() {
            override fun onClick(model: CourseListItem, viewBinder: ViewBinder) {
                if (model.purchased != null && !model.purchased.isEmpty()) {
                    if (model.purchased.equals("0", ignoreCase = true)) {
                        val intent = Intent(activity, CourseDetailActivity::class.java)
                        intent.putExtra("course", model)
                        startActivity(intent)
                    } else if (model.purchased.equals("1", ignoreCase = true)) {
                        val intent = Intent(activity, CourseDetailActivity::class.java)
                        intent.putExtra("course", model)
                        startActivity(intent)
                    }
                } else {
                    val intent = Intent(activity, CourseDetailActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}