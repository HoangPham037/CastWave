package com.castwave.castwave.base

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.castwave.castwave.R
import com.castwave.castwave.base.rx.RxBus
import com.castwave.castwave.utils.DisposeBag
import com.castwave.castwave.utils.KeyboardUtils
import com.castwave.castwave.utils.widget.Boast
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    var currentFragment: Fragment? = null

    lateinit var loading: AlertDialog

    private var isCancelable = false

    lateinit var binding: T

    @Inject
    lateinit var rxBus: RxBus

    protected abstract fun getLayoutBinding(): T

    protected val bag by lazy { DisposeBag.create() }

    @LayoutRes
    open fun getLayoutIdLoading(): Int = -1

    open fun getThemResId(): Int = -1

    protected abstract fun updateUI(savedInstanceState: Bundle?)

    override fun onDestroy() {
        bag.dispose()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        binding = getLayoutBinding()
        setContentView(binding.root)
        initDialog()
        updateUI(savedInstanceState)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(
                        v.windowToken,
                        0
                    )
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun addDispose(vararg disposables: Disposable) {
        bag.add(*disposables)
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
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.anim_zoom_fragment_open_enter,
                R.anim.anim_zoom_fragment_open_exit,
                R.anim.anim_zoom_fragment_close_enter,
                R.anim.anim_zoom_fragment_close_exit
            )
            val existingFragment = manager.findFragmentByTag(tag)
            if (existingFragment == null) {
                val fragment = fragmentClazz.asSubclass(Fragment::class.java).newInstance()
                    .apply { arguments = args }
                if (addBackStack) {
                    transaction.addToBackStack(tag)
                }
                currentFragment = fragment
                transaction.add(resId, fragment, tag).commit()
            } else {
                manager.fragments.forEach { frag ->
                    if (frag != existingFragment && !frag.isHidden) {
                        transaction.hide(frag)
                    }
                }

                if (existingFragment.isHidden || existingFragment.isDetached || existingFragment.isAdded) {
                    transaction.show(existingFragment)
                }
                currentFragment = existingFragment
                transaction.commit()
            }
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws
    open fun openFragmentWithActivity(
        resId: Int,
        fragmentClazz: Class<*>,
        args: Bundle?,
        addBackStack: Boolean
    ) {
        val tag = fragmentClazz.simpleName
        try {
            val fragment: Fragment
            try {
                fragment = (fragmentClazz.asSubclass(Fragment::class.java)).newInstance()
                    .apply { arguments = args }

                val transaction = supportFragmentManager.beginTransaction()
                transaction.setCustomAnimations(
                    R.anim.slide_in_bottom_fragment,
                    R.anim.slide_out_bottom_fragment,
                    R.anim.slide_in_bottom_fragment,
                    R.anim.slide_out_bottom_fragment
                )
                if (addBackStack) {
                    transaction.addToBackStack(tag)
                }
                if (fragment.isAdded) transaction.show(fragment)
                else transaction.add(resId, fragment, tag)
                transaction.commit()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Show toast
     * @param msg
     */
    fun toast(msg: String) = Boast.makeText(this, msg).show()

    /**
     * Init dialog loading
     */
    private fun initDialog() {
        val builder: AlertDialog.Builder = if (getThemResId() != -1)
            AlertDialog.Builder(this, getThemResId()) else AlertDialog.Builder(
            this,
            R.style.CustomDialog
        )

        builder.setCancelable(isCancelable)
        builder.setView(if (getLayoutIdLoading() == -1) R.layout.layout_loading_dialog_default else getLayoutIdLoading())
        loading = builder.create()
    }

    /**
     * Show dialog loading
     */
    open fun showDialog() {
        runOnUiThread {
            if (!loading.isShowing) {
                loading.show()
            }
        }
    }

    fun popBackTo(clazz: Class<*>) {
        supportFragmentManager.popBackStack(clazz.simpleName, 0)
    }

    /**
     * Hide dialog loading
     */
    open fun hideDialog() {
        runOnUiThread {
            if (loading.isShowing) {
                loading.dismiss()
            }
        }
    }

    /**
     * Set cancelable dialog
     */
    fun setCancelableDialog(isCancelable: Boolean) {
        this.isCancelable = isCancelable
    }

    fun hideKeyboard() {
        KeyboardUtils.hideKeyboard(this)
    }

    fun hideKeyboardOutSide(view: View) {
        KeyboardUtils.hideKeyBoardWhenClickOutSide(view, this)
    }

    fun hideKeyboardOutSideText(view: View) {
        KeyboardUtils.hideKeyBoardWhenClickOutSideText(view, this)
    }

    open fun clearAllBackStack() {
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }
}