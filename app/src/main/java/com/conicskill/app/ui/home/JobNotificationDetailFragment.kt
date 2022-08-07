package com.conicskill.app.ui.home

import android.animation.Animator
import android.app.ProgressDialog
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.jobNotification.JobDetailRequest
import com.conicskill.app.data.model.jobNotification.JobFetch
import com.conicskill.app.data.model.jobNotification.JobNotificationsItem
import com.conicskill.app.di.util.ViewModelFactory
import com.conicskill.app.util.Constants
import com.conicskill.app.util.GlideApp
import com.conicskill.app.util.ProgressOverlay
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import es.dmoral.toasty.Toasty
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class JobNotificationDetailFragment : BaseFragment() {

    @BindView(R.id.webViewDescription)
    lateinit var webViewDescription: WebView

    @BindView(R.id.imageViewCourseBanner)
    lateinit var imageViewCourseBanner: AppCompatImageView

    @BindView(R.id.textViewCourseName)
    lateinit var textViewCourseName: AppCompatTextView

    private var oneAdapter: OneAdapter? = null
    private lateinit var jobId: String

    companion object {
        fun newInstance(userIdFromIntent: String) = JobNotificationDetailFragment()
                .apply {
                    jobId = userIdFromIntent
                }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: JobNotificationViewModel
    private var progressOverlay: ProgressDialog? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(JobNotificationViewModel::class.java)

        observeViewModel()
        val token = viewModel.tinyDB.getString(Constants.AUTH_TOKEN)
        var jobFetch = JobFetch(jobId)
        viewModel.callFetchJobDetails(jobDetailRequest = JobDetailRequest(jobFetch, token))
    }

    override fun layoutRes(): Int {
        return R.layout.job_details_fragment
    }

    private fun observeViewModel() {
        viewModel.getLoading()?.observe(viewLifecycleOwner, Observer { loading: Boolean? ->
            if (loading!!) {
                progressOverlay = ProgressOverlay.show(activity, "Fetching List")
            } else {
                progressOverlay?.dismiss()
            }
        })

        viewModel.getError()?.observe(viewLifecycleOwner, Observer { error: Boolean? ->
            if (error != null) {
                if (error) {
                    progressOverlay?.dismiss()
                    Toasty.error(requireContext(), "Failed to register. Please try again.",
                            Toasty.LENGTH_LONG, false).show()
                }
            }
        })
        viewModel.getJobDetails()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.body()?.code == HttpsURLConnection.HTTP_OK) {
                    webViewDescription.settings.javaScriptEnabled = true
                    webViewDescription.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.windowBackground))
                    if (it.body()?.jobDetail?.description != null && !it.body()?.jobDetail?.description?.isEmpty()!!) {
                        val head1 = "<head><style>@font-face {font-family: 'arial';src: url('file:///android_asset/fonts/DIN-Medium.otf');}body {font-family: 'verdana';}</style></head>"
                        val text = ("<html>" + head1
                                + "<body >" + it.body()?.jobDetail?.description
                                + "</body></html>")
                        webViewDescription.loadDataWithBaseURL("http://127.0.0.1/", text,
                                "text/html", "UTF-8", null)
                    } else {
                        webViewDescription.loadDataWithBaseURL("http://127.0.0.1/", "No description found",
                                "text/html", "UTF-8", null)
                    }

                    textViewCourseName.text = it.body()?.jobDetail?.title

                    GlideApp.with(requireContext())
                            .load(it.body()?.jobDetail?.thumbnail)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(imageViewCourseBanner)
                }
            }
        })
    }

    private fun featureItems(): ItemModule<JobNotificationsItem> {
        return object : ItemModule<JobNotificationsItem>() {
            override fun provideModuleConfig(): ItemModuleConfig {
                return object : ItemModuleConfig() {
                    override fun withLayoutResource(): Int {
                        return R.layout.item_job_notification
                    }

                    override fun withFirstBindAnimation(): Animator? {
                        // can be implemented by inflating Animator Xml
                        return null
                    }
                }
            }

            override fun onBind(item: Item<JobNotificationsItem>, viewBinder: ViewBinder) {
                val textViewNewsHeading = viewBinder.findViewById<AppCompatTextView>(R.id.textViewNewsHeading)
                val imageViewNews = viewBinder.findViewById<AppCompatImageView>(R.id.imageViewNews)
                GlideApp.with(context!!).load(item.model.thumbnail)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(imageViewNews)
                textViewNewsHeading.text = item.model.title
            }


        }
    }

    private fun clickEventHook(): ClickEventHook<JobNotificationsItem> {
        return object : ClickEventHook<JobNotificationsItem>() {
            override fun onClick(model: JobNotificationsItem, viewBinder: ViewBinder) {

            }
        }
    }

}