package com.example.android.stockshawk.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.stockshawk.R;

public class DetailsFragment extends Fragment {

    String mSymbol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Bundle arguments = getArguments();
        if (arguments!=null) {
            mSymbol = arguments.getString("symbol");
        }

        return inflater.inflate(R.layout.detail_panel, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        TextView textView = (TextView) getView().findViewById(R.id.symbol_text);

        if (mSymbol!=null) {
            textView.setText(mSymbol);
        }
    }

}
