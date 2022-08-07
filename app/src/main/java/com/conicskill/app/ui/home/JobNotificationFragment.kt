package com.conicskill.app.ui.home

import android.animation.Animator
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.conicskill.app.R
import com.conicskill.app.base.BaseFragment
import com.conicskill.app.data.model.jobNotification.JobNotificationsItem
import com.conicskill.app.data.model.jobNotification.JobRequest
import com.conicskill.app.data.model.jobNotification.PageParams
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

class JobNotificationFragment : BaseFragment() {

    @BindView(R.id.recyclerViewJobNotification)
    lateinit var recyclerViewJobNotification: RecyclerView

    private var oneAdapter: OneAdapter? = null


    companion object {
        fun newInstance() = JobNotificationFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: JobNotificationViewModel
    private var progressOverlay: ProgressDialog? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(JobNotificationViewModel::class.java)
        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewJobNotification.layoutManager = linearLayoutManager

        recyclerViewJobNotification.itemAnimator = null
        oneAdapter = OneAdapter(recyclerViewJobNotification)
                .attachItemModule(featureItems().addEventHook(clickEventHook()))

        observeViewModel()
        val pageParams = PageParams(0, 21)
        val token = viewModel.tinyDB.getString(Constants.AUTH_TOKEN)
        viewModel.callFetchJobList(jobRequest = JobRequest(pageParams, token))
    }

    override fun layoutRes(): Int {
        return R.layout.job_notification_fragment
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
        viewModel.getJobNotificationList()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.body()?.code == HttpsURLConnection.HTTP_OK) {
                    oneAdapter?.setItems(it.body()?.data?.jobNotifications!!)
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
                requireActivity().supportFragmentManager.beginTransaction()
                        .addToBackStack("videoListingFragment")
                        .replace(R.id.screenContainer, JobNotificationDetailFragment.newInstance(model.jobId!!))
                        .commit()
            }
        }
    }

}