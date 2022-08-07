package com.conicskill.app.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import butterknife.BindView
import com.conicskill.app.R
import com.conicskill.app.base.BaseActivity
import com.conicskill.app.data.model.videoCouses.CourseListItem

class DownloadsActivity : BaseActivity() {

    @BindView(R.id.headerText)
    lateinit var headerText: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        headerText.text = "Downloads"

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.screenContainer,
                DownloadCategoryFragment.newInstance(intent.getSerializableExtra("course") as CourseListItem)
            )
            .commit()
    }

    override fun layoutRes(): Int {
        return R.layout.activity_downloads
    }
}