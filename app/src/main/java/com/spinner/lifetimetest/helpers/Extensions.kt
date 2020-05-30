package com.spinner.lifetimetest.helpers

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData


fun <T> LiveData<T>.nonNullObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, androidx.lifecycle.Observer {
        it?.let(observer)
    })
}

fun View.hide() {
    visibility = View.GONE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun FragmentActivity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun EditText.showKeyboard(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    runDelayed({
        requestFocus()
        imm!!.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }, 500)
}

fun View.animateAndHide(anim: Animation) {
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(p0: Animation?) {
            hide()
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }
        override fun onAnimationStart(p0: Animation?) {

        }

    })
    startAnimation(anim)
}