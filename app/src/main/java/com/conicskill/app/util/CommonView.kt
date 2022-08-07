package com.conicskill.app.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.conicskill.app.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object CommonView {

    private var alertDialog: AlertDialog? = null
    fun showRoundedAlertDialog(
        context: Context?,
        title: String?,
        textToBeShown: String?,
        posText: String?,
        negText: String?,
        response: OnResponse
    ) {
        val customView =  LayoutInflater.from(context)
            .inflate(R.layout.custom_popup, null, false)

        val builder = MaterialAlertDialogBuilder(
            context!!,
            R.style.CustomDialogDesignTheme
        )
            .setTitle(title)
            .setMessage(textToBeShown)
            .setCancelable(false)
            .setView(customView)

        val textViewPositive: TextView = customView.findViewById(R.id.textViewPositive)
        val textViewNegative: TextView = customView.findViewById(R.id.textViewNegative)

        textViewPositive.text = posText
        textViewNegative.text = negText

        textViewPositive.setOnClickListener {
            alertDialog?.dismiss()
            response.onResponse(true)
        }

        textViewNegative.setOnClickListener {
            alertDialog?.dismiss()
            response.onResponse(false)
        }

        alertDialog = builder.create()
        alertDialog!!.show()
    }

    fun showSingleButtonAlertDialog(
        context: Context?,
        title: String?,
        textToBeShown: String?,
        posText: String?,
        response: OnResponse
    ) {
        val customView =  LayoutInflater.from(context)
            .inflate(R.layout.custom_popup, null, false)

        val builder = MaterialAlertDialogBuilder(
            context!!,
            R.style.CustomDialogDesignTheme
        )
            .setTitle(title)
            .setMessage(textToBeShown)
            .setCancelable(false)
            .setView(customView)

        val textViewPositive: TextView = customView.findViewById(R.id.textViewPositive)
        val textViewNegative: TextView = customView.findViewById(R.id.textViewNegative)
        val view2: View = customView.findViewById(R.id.view2)
        view2.visibility = View.GONE
        textViewNegative.visibility = View.GONE

        textViewPositive.text = posText

        textViewPositive.setOnClickListener {
            alertDialog?.dismiss()
            response.onResponse(true)
        }

        textViewNegative.setOnClickListener {
            alertDialog?.dismiss()
            response.onResponse(false)
        }

        alertDialog = builder.create()
        alertDialog!!.show()
    }

    fun showNoInternetDialog(activity: Activity, restart: Boolean) {
        val builder = AlertDialog.Builder(activity)
        @SuppressLint("RestrictedApi")
        val ctx: ContextThemeWrapper = ContextThemeWrapper(
            activity, R.style.AppTheme
        )
        val layoutInflater = LayoutInflater.from(ctx)
        val view: View = layoutInflater.inflate(R.layout.layout_no_internet_dialog, null)
        builder.setView(view)
        // Set other dialog properties
        builder.setCancelable(false)

        // Create the AlertDialog
        val dialog = builder.create()
        val button = view.findViewById<View>(R.id.buttonRetry) as Button
        button.setOnClickListener {
            if (restart) {
                val intent = activity.intent
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                activity.finish()
                activity.startActivity(intent)
            } else {
                dialog.dismiss()
            }
        }
        if (!activity.isFinishing) {
            dialog.show()
        }
    }
}