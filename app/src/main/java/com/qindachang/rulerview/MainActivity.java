package com.qindachang.rulerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.qindachang.library.RulerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView mTvHeight = (TextView) findViewById(R.id.tv_height);
        RulerView mRulerViewHeight = (RulerView) findViewById(R.id.rulerView_height);
        mRulerViewHeight.initViewParam(170.0f, 70.0f, 230.0f, 1);
        mRulerViewHeight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTvHeight.setText(value + "cm");
            }
        });

        final TextView mTvWeight = (TextView) findViewById(R.id.tv_weight);
        RulerView mRulerViewWeight = (RulerView) findViewById(R.id.rulerView_weight);
        mRulerViewWeight.initViewParam(60.0f, 0.0f, 100.0f, 1);
        mRulerViewWeight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTvWeight.setText(value + "kg");
            }
        });
    }
}
