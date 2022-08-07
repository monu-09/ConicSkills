package com.conicskill.app.ui.courseDetail;

import android.content.Context;
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.conicskill.app.R
import com.conicskill.app.data.model.home.LayoutItem
import com.conicskill.app.ui.courseDetail.interfaces.CourseLayoutClickListener

class CourseNameItemAdapter(
    private val context:Context,
    private var layoutItems:List<LayoutItem>,
    private val courseLayoutClickListener: CourseLayoutClickListener
) : RecyclerView.Adapter<CourseNameItemAdapter.CourseNameViewHolder>() {

    class CourseNameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCourseType: AppCompatTextView = itemView.findViewById(R.id.textViewCourseType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseNameViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_course_name_header, parent,
            false)
        return CourseNameViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseNameViewHolder, position: Int) {
        holder.textViewCourseType.text = layoutItems[position].title
        holder.itemView.setOnClickListener {
            for(i in layoutItems.indices) {
                when (i) {
                    position -> {
                        layoutItems[i].isSelected = true
                        holder.textViewCourseType.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        holder.textViewCourseType.background = ContextCompat.getDrawable(context, R.drawable.border_view_questions)
                    }
                    else -> {
                        layoutItems[i].isSelected = false
                        holder.textViewCourseType.setTextColor(ContextCompat.getColor(context, R.color.md_grey_500))
                        holder.textViewCourseType.background = ContextCompat.getDrawable(context, R.drawable.border_grey)
                    }
                }
            }
            courseLayoutClickListener.layoutClick(layoutItems[position], position)
        }
        if(layoutItems[position].isSelected) {
            holder.textViewCourseType.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
            holder.textViewCourseType.background = ContextCompat.getDrawable(context, R.drawable.border_view_questions)
        } else {
            holder.textViewCourseType.setTextColor(ContextCompat.getColor(context, R.color.md_grey_500))
            holder.textViewCourseType.background = ContextCompat.getDrawable(context, R.drawable.border_grey)
        }
        holder.setIsRecyclable(false);
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return layoutItems.size
    }
}