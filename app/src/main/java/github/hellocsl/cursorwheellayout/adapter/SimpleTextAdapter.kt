package github.hellocsl.cursorwheellayout.adapter

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import github.hellocsl.cursorwheel.CursorWheelLayout.CycleWheelAdapter
import github.hellocsl.cursorwheellayout.R
import github.hellocsl.cursorwheellayout.data.MenuItemData

/**
 * Created by chensuilun on 16/4/24.
 */
class SimpleTextAdapter constructor(
    private val mContext: Context,
    val mMenuItemDatas: List<MenuItemData>,
    val mGravity: Int = Gravity.CENTER
) : CycleWheelAdapter() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun getCount(): Int {
        return mMenuItemDatas.size
    }

    override fun getView(parent: View?, position: Int): View {
        val (mTitle) = getItem(position)
        val root = mLayoutInflater.inflate(R.layout.wheel_menu_item, null, false)
        val textView = root.findViewById<View>(R.id.wheel_menu_item_tv) as TextView
        textView.visibility = View.VISIBLE
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.text = mTitle
        if (textView.layoutParams is FrameLayout.LayoutParams) {
            (textView.layoutParams as FrameLayout.LayoutParams).gravity = mGravity
        }
        if (position == INDEX_SPEC) {
            textView.setTextColor(ActivityCompat.getColor(mContext, R.color.red))
        }
        return root
    }

    override fun getItem(position: Int): MenuItemData {
        return mMenuItemDatas[position]
    }

    companion object {
        const val INDEX_SPEC = 9
    }
}