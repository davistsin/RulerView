package com.qindachang.rulerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.qindachang.widget.RulerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView mTvHeight = (TextView) findViewById(R.id.tv_height);
        RulerView mRulerViewHeight = (RulerView) findViewById(R.id.rulerView_height);
        mRulerViewHeight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTvHeight.setText(value + "cm");
            }
        });

        final TextView mTvWeight = (TextView) findViewById(R.id.tv_weight);
        RulerView mRulerViewWeight = (RulerView) findViewById(R.id.rulerView_weight);
        //mRulerViewWeight.setValue(60.0f, 0.0f, 100.0f, 1.0f);
        mRulerViewWeight.setOnValueChangeListener(new RulerView.OnValueChangeListener() {
            @Override
            public void onValueChange(float value) {
                mTvWeight.setText(value + "kg");
            }
        });
    }
}
