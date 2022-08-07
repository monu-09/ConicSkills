package com.conicskill.app.util;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.widget.ProgressBar;


import com.conicskill.app.R;

import androidx.core.content.ContextCompat;

/**
 * Simulates a progress overlay with a progress dialog with no borders, frame nor text:
 * {@code overlay = ProgressOverlay.show(this); ... overlay.dismiss()}
 */
@SuppressWarnings("unused")
public final class ProgressOverlay {

    /**
     * The color of the circular spinner on API 21+ (null to use the default one)
     */
    public static Integer MATERIAL_COLOR = Color.WHITE;

    /**
     * Creates and shows a progress dialog with no borders, frame nor text over the default semi-transparent dialogs' background.
     *
     * @return ProgressDialog The new dialog instance
     */
    public static ProgressDialog show(final Context context, final String title) {
        return show(context, title, false);
    }

    /**
     * Creates and shows a progress dialog with no borders, frame nor text over the default semi-transparent dialogs' background.
     *
     * @return ProgressDialog The new dialog instance
     */
    public static ProgressDialog show(final Context context, final String title, final boolean cancelable) {
        return show(context, cancelable, title, null);
    }

    /**
     * Creates and shows a progress dialog with no borders, frame nor text over the default semi-transparent dialogs' background.
     *
     * @return ProgressDialog The new dialog instance
     */
    @SuppressLint("NewApi")
    public static ProgressDialog show(final Context context, final boolean cancelable, String title, final DialogInterface.OnCancelListener cancelListener) {
        final ProgressDialog dialog = ProgressDialog.show(context, title, title, true, cancelable, cancelListener);
        final ProgressBar view = new ProgressBar(context);
        MATERIAL_COLOR = ContextCompat.getColor(context, R.color.colorAccent);
        if (Build.VERSION.SDK_INT >= 21)
            view.setIndeterminateTintList(ColorStateList.valueOf(MATERIAL_COLOR));
        dialog.setContentView(view);
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

}