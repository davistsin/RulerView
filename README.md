# RulerView


<img src="https://github.com/qindachang/RulerView/blob/master/imgs/Screenshot_1479577403.png" width = "320" height = "568" alt="截屏" align=center />

## 引入

    compile 'com.qindachang:ruler-view:1.0.0'

## XML

    <com.qindachang.widget.RulerView
        android:id="@+id/rulerView_height"
        android:layout_width="match_parent"
        android:layout_height="80dp"
        android:background="#ffd900" //设置背景为黄色
        app:alphaEnable="true" //设置两边透明度
        app:lineColor="#ffffff" //设置竖线的颜色
        app:lineMaxHeight="35dp" //设置竖线最大高度
        app:lineMidHeight="30dp" //设置竖线中间高度
        app:lineMinHeight="20dp" //设置竖线最小高度
        app:lineSpaceWidth="7dp" //设置竖线之间的距离
        app:lineWidth="2dp" //设置竖线的宽度
        app:textColor="#ffffff" //设置文本的颜色
        app:textMarginTop="5dp" //设置文本距离竖线的距离
        app:textSize="18sp" //设置文字大小
        app:minValue="70.0"    // 设置最小值
        app:maxValue="230.0"  //设置最大值
        app:perValue="0.1" //设置刻度尺的单位值
        app:selectorValue="170.0"/>  //设置选中的值

## Java

    final TextView mTvWeight = (TextView) findViewById(R.id.tv_weight);
    RulerView mRulerViewWeight = (RulerView) findViewById(R.id.rulerView_weight);
    mRulerViewWeight.setValue(60.0f, 0.0f, 100.0f, 0.1f);//设置选中值、最小值、最大值、单位值
    mRulerViewWeight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
        @Override
        public void onValueChange(float value) {
            mTvWeight.setText(value + "kg");
        }
    });
