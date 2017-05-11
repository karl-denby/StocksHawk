package com.example.android.stockshawk.ui;

import android.app.Activity;
import android.os.Bundle;

import com.example.android.stockshawk.R;

import butterknife.ButterKnife;


public class MasterDetail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.master_detail);
        ButterKnife.bind(this);
    }

}
