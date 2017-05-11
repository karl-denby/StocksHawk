package com.example.android.stockshawk.ui;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.android.stockshawk.R;

public class SymbolFragment extends Fragment {

    OnClickListener mCallback;
    FrameLayout frameLayout;

    public interface OnClickListener {
        public void OnClick(View view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.symbol_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameLayout = (FrameLayout) getView().findViewById(R.id.symbol_frame);
        if (frameLayout!=null) {
            frameLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mCallback.OnClick(view);
                }
            });
        } else {
            Log.v("DEBUG", "Didn't find framelayout reference");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnClickListener");
        }
    }

}
