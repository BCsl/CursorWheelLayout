# CursorWheelLayout

CursorWheelLayout is an Android library that allows view to be placed on a rotatable wheel. It behaves like a Circular ListView where items rotate rather than scroll vertically.
CursorWheelLayout consists of two components , the center item with id `id_wheel_menu_center_item` and the menu items that provided by CycleWheelAdapter.

The CursorWheelLayout can be used as a way to select one item from a list. The `wheelSelectedAngle` attribute determines what position on the wheel is selected.
You can also receive a callback for when an item is clicked, and whether it is selected. Have a look at the sample for a working example!

## Screenshot
![1]
![2]
## Apk

[Download Demo here](https://github.com/BCsl/CursorWheelLayout/tree/master/demo/wheel-v1.01.apk)

## Setup

### Gradle

`compile 'github.hellocsl:CursorWheelLayout:1.0.2'`

### Maven

```xml
<dependency>
  <groupId>github.hellocsl</groupId>
  <artifactId>CursorWheelLayout</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```

## Usage
1) Add a custom view in Xml
```xml
  <github.hellocsl.cursorwheel.CursorWheelLayout
        android:id="@+id/test_circle_menu_right"
        android:layout_width="306dip"
        android:layout_height="306dip"
        android:layout_gravity="center_vertical|right|bottom"
        android:layout_marginBottom="-153dp"
        android:layout_marginRight="-153dip"
        app:wheelBackgroundColor="@color/colorAccent_Translucent"
        app:wheelCursorColor="@color/red"
        app:wheelCursorHeight="20dip"
        app:wheelFlingValue="460"
        app:wheelSelectedAngle="225">

        <github.hellocsl.cursorwheellayout.widget.SwitchButton
            android:id="@id/id_wheel_menu_center_item"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:boardWidth="0dip"
            app:uncheckRevealColor="#ff2a2f36"
        />
    </github.hellocsl.cursorwheel.CursorWheelLayout>

```
2) Define your `WheelCycleAdapter`
```java
public class SimpleTextAdapter implements CursorWheelLayout.CycleWheelAdapter{

        public int getCount(){
        //...
        }

        public View getView(View parent, int position){
        //...
        }

        public Object getItem(int position){
        //...
        }

}
```
3) Set your `WheelCycleAdapter` similar to how you would set an adapter with a ListView
```java
        SimpleTextAdapter simpleTextAdapter = new SimpleTextAdapter(this, menuItemDatas);
        mTestCircleMenuLeft.setAdapter(simpleTextAdapter);
```

## Listener
1) A listener for when the closest item to the `SelectionAngle` changes.
```java
      mTestCircleMenuTop.setOnMenuSelectedListener(new CursorWheelLayout.OnMenuSelectedListener() {
            @Override
            public void onItemSelected(CursorWheelLayout parent, View view, int pos) {
                Toast.makeText(MainActivity.this, "Top Menu selected position:" + pos, Toast.LENGTH_SHORT).show();
            }
        });
```
2) A listener for when an item is clicked.
```java
        mTestCircleMenuTop.setOnMenuItemClickListener(new CursorWheelLayout.OnMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Toast.makeText(MainActivity.this, "Top Menu click position:" + pos, Toast.LENGTH_SHORT).show();

            }
        });
```

## Useful attributes
 Here are the custom attributes that can be declared in xml:
  * wheelSelectedAngle
  * wheelPaddingRadio
  * wheelCenterRadio
  * wheelItemRadio
  * wheelFlingValue
  * wheelCursorHeight
  * wheelCursorColor
  * wheelBackgroundColor
  * wheelRotateItem


## Refer to
http://blog.csdn.net/lmj623565791/article/details/43131133

## License
Apache License Version 2.0
http://apache.org/licenses/LICENSE-2.0.txt

[1]: ./screenshot/gif2.gif
[2]: ./screenshot/gif3.gif
