package com.castwave.castwave.base

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.InflateException
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.R
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.utils.DisposeBag
import com.castwave.castwave.utils.extension.disposedBy
import com.castwave.castwave.utils.extension.setDrawableStart
import com.castwave.castwave.utils.widget.Boast
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject


abstract class BaseFragment<T : ViewBinding> : Fragment(),
    ViewTreeObserver.OnGlobalLayoutListener {
    private var rootView: View? = null

    lateinit var binding: T

    lateinit var rxBus: RxBus

    protected val bag by lazy { DisposeBag.create() }

    protected abstract fun getLayoutBinding(): T

    protected abstract fun updateUI(savedInstanceState: Bundle?)

    override fun onGlobalLayout() {
        rootView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            if (it is BaseActivity<*>) {
                this.rxBus = it.rxBus
            }
        }
        setHasOptionsMenu(false)
    }

    fun addDispose(vararg disposables: Disposable) {
        bag.add(*disposables)
    }

    fun Disposable.addToBag() {
        bag.add(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView != null) {
            val parent = rootView!!.parent as ViewGroup?
            parent?.removeView(rootView)
        } else {
            try {
                binding = getLayoutBinding()
                rootView = binding.root
                rootView!!.viewTreeObserver.addOnGlobalLayoutListener(this)
            } catch (e: InflateException) {
                e.printStackTrace()
            }
        }
        return rootView
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setHideKeyboardFocus(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _: View?, _: MotionEvent? ->
                if (activity != null) {
                    hideKeyboard()
                }
                false
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setHideKeyboardFocus(innerView)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI(savedInstanceState)
    }

    override fun onDestroy() {
        bag.dispose()
        super.onDestroy()
    }

    fun openFragments(
        resId: Int,
        fragmentClazz: Class<*>,
        args: Bundle?,
        addBackStack: Boolean
    ) {
        (activity as BaseActivity<*>).openFragment(resId, fragmentClazz, args, addBackStack)
    }

    fun openFragmentSlideUp(
        resId: Int,
        fragmentClazz: Class<*>,
        args: Bundle?,
        addBackStack: Boolean
    ) {
        (activity as BaseActivity<*>).openFragmentWithActivity(resId, fragmentClazz, args, addBackStack)
    }

    @Throws
    open fun openFragment(
        resId: Int,
        fragmentClazz: Class<*>,
        args: Bundle?,
        addBackStack: Boolean
    ) {
        val tag = fragmentClazz.simpleName
        try {
            val isExisted =
                childFragmentManager.popBackStackImmediate(tag, 0)    // IllegalStateException
            if (!isExisted) {
                val fragment: Fragment
                try {
                    fragment = (fragmentClazz.asSubclass(Fragment::class.java)).newInstance()
                        .apply { arguments = args }
                    val transaction = childFragmentManager.beginTransaction()
                    transaction.setCustomAnimations(
                        R.anim.slide_in_bottom_fragment,
                        R.anim.slide_out_bottom_fragment,
                        R.anim.slide_in_bottom_fragment,
                        R.anim.slide_out_bottom_fragment
                    )
                    transaction.add(resId, fragment, tag)
                    if (addBackStack) {
                        transaction.addToBackStack(tag)
                    }
                    transaction.commit()
                } catch (e: java.lang.InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun removeFragment(frag: Class<*>) {
        parentFragmentManager.fragments.findLast { it::class.java.simpleName == frag.simpleName }
            ?.let { fragment ->
                hideKeyboard()
                parentFragmentManager.beginTransaction().remove(fragment).commit()
                parentFragmentManager.popBackStack()
            }
    }

    fun popBackTo(clazz: Class<*>) {
        parentFragmentManager.popBackStack(clazz.simpleName, 0)
    }

    fun toast(msg: String?) {
        context?.let {
            if (msg.isNullOrEmpty()) return
            Boast.makeText(it, msg).show()
        }
    }

    fun toast(msg: String, duration: Int, cancelCurrent: Boolean) {
        context?.let {
            Boast.makeText(it, msg, duration).show(cancelCurrent)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }


    fun showDialog() {
        activity?.let {
            if (it is BaseActivity<*>) {
                it.showDialog()
            }
        }
    }

    fun hideDialog() {
        activity?.let {
            if (it is BaseActivity<*>) {
                it.hideDialog()
            }
        }
    }

    fun hideKeyboard() {
        activity?.let {
            if (it is BaseActivity<*>) {
                it.hideKeyboard()
            }
        }
    }

    fun hideKeyboardOutSide(view: View) {
        activity?.let {
            if (it is BaseActivity<*>) {
                it.hideKeyboardOutSide(view)
            }
        }
    }

    fun hideKeyboardOutSideText(view: View) {
        activity?.let {
            if (it is BaseActivity<*>) {
                it.hideKeyboardOutSideText(view)
            }
        }
    }

    fun onBackPressed() {
        hideKeyboard()
        activity?.onBackPressed()
    }

    open fun clearAllBackStack() {
        activity?.let {
            if (it is BaseActivity<*>) {
                it.clearAllBackStack()
            }
        }
    }

    fun finish() {
        activity?.finish()
    }

    fun runOnUiThread(action: () -> Unit) {
        activity?.runOnUiThread { action() } ?: action()
    }

}