package github.hellocsl.cursorwheellayout.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import github.hellocsl.cursorwheel.CursorWheelLayout
import github.hellocsl.cursorwheellayout.R

/**
 *
 * Created by chensuilun on 16-4-19.
 */
class SimpleTextCursorWheelLayout(context: Context, attrs: AttributeSet?) : CursorWheelLayout(context, attrs) {
    override fun onInnerItemSelected(v: View?) {
        super.onInnerItemSelected(v)
        val tv = v?.findViewById<View>(R.id.wheel_menu_item_tv)
        tv?.animate()?.scaleX(2f)?.scaleY(2f)
    }

    override fun onInnerItemUnselected(v: View?) {
        super.onInnerItemUnselected(v)
        val tv = v?.findViewById<View>(R.id.wheel_menu_item_tv)
        tv?.animate()?.scaleX(1f)?.scaleY(1f)
    }

    companion object {
        const val MENU_COUNT = 10
        const val INDEX_SPEC = 9
    }
}