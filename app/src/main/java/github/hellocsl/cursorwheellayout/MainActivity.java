package github.hellocsl.cursorwheellayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.hellocsl.cursorwheel.CursorWheelLayout;
import github.hellocsl.cursorwheellayout.adapter.SimpleImageAdapter;
import github.hellocsl.cursorwheellayout.adapter.SimpleTextAdapter;
import github.hellocsl.cursorwheellayout.data.ImageData;
import github.hellocsl.cursorwheellayout.data.MenuItemData;
import github.hellocsl.cursorwheellayout.widget.SimpleTextCursorWheelLayout;

public class MainActivity extends AppCompatActivity implements CursorWheelLayout.OnMenuSelectedListener {

    @Bind(R.id.test_circle_menu_left)
    SimpleTextCursorWheelLayout mTestCircleMenuLeft;
    @Bind(R.id.test_circle_menu_right)
    SimpleTextCursorWheelLayout mTestCircleMenuRight;
    @Bind(R.id.main_button_random_selected)
    Button mMainButtonRadonSelected;
    Random mRandom = new Random();
    @Bind(R.id.test_circle_menu_top)
    CursorWheelLayout mTestCircleMenuTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        String[] res = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "OFF"};
        List<MenuItemData> menuItemDatas = new ArrayList<MenuItemData>();
        for (int i = 0; i < res.length; i++) {
            menuItemDatas.add(new MenuItemData(res[i]));
        }
        SimpleTextAdapter simpleTextAdapter = new SimpleTextAdapter(this, menuItemDatas);
        mTestCircleMenuLeft.setAdapter(simpleTextAdapter);
        mTestCircleMenuLeft.setOnMenuSelectedListener(this);
        mTestCircleMenuRight.setAdapter(simpleTextAdapter);
        mTestCircleMenuRight.setOnMenuSelectedListener(this);
        List<ImageData> imageDatas = new ArrayList<ImageData>();
        imageDatas.add(new ImageData(R.drawable.ic_bank_bc, "0"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_china, "1"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_guangda, "2"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_guangfa, "3"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_jianshe, "4"));
        imageDatas.add(new ImageData(R.drawable.ic_bank_jiaotong, "5"));
        SimpleImageAdapter simpleImageAdapter = new SimpleImageAdapter(this, imageDatas);
        mTestCircleMenuTop.setAdapter(simpleImageAdapter);
        mTestCircleMenuTop.setOnMenuSelectedListener(new CursorWheelLayout.OnMenuSelectedListener() {
            @Override
            public void onItemSelected(CursorWheelLayout parent, View view, int pos) {
                Toast.makeText(MainActivity.this, "Top Menu selected position:" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        mTestCircleMenuTop.setOnMenuItemClickListener(new CursorWheelLayout.OnMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Toast.makeText(MainActivity.this, "Top Menu click position:" + pos, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemSelected(CursorWheelLayout p, View view, int pos) {
    }

    @OnClick(R.id.main_button_random_selected)
    void onRandomClick() {
        int index = mRandom.nextInt(10);
        mTestCircleMenuLeft.setSelection(index);
        mTestCircleMenuRight.setSelection(index, false);
        mMainButtonRadonSelected.setText("Random Selected:" + index);
    }
}
