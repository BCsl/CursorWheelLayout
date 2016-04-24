package github.hellocsl.cursorwheellayout.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import github.hellocsl.cursorwheel.CursorWheelLayout;
import github.hellocsl.cursorwheellayout.R;

/**
 *
 * Created by chensuilun on 16-4-19.
 */
public class SimpleTextCursorWheelLayout extends CursorWheelLayout {
    public static final int MENU_COUNT = 10;
    public static final int INDEX_SPEC = 9;

    public SimpleTextCursorWheelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onInnerItemSelected(View v) {
        super.onInnerItemSelected(v);
        if (v == null) {
            return;
        }
        View tv = v.findViewById(R.id.wheel_menu_item_tv);
        tv.animate().scaleX(2).scaleY(2);
    }


    @Override
    protected void onInnerItemUnselected(View v) {
        super.onInnerItemUnselected(v);
        if (v == null) {
            return;
        }
        View tv = v.findViewById(R.id.wheel_menu_item_tv);
        tv.animate().scaleX(1).scaleY(1);
    }


}
