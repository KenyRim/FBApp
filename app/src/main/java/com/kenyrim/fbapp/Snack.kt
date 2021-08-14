package com.kenyrim.fbapp

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.Snackbar


object Snack {
    fun snackBar(msg: String, view: View) {
        val snack = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
        val params = snack.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params
        snack.show()
    }
}