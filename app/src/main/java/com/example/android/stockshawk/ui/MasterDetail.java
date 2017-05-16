package com.example.android.stockshawk.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.stockshawk.R;

import butterknife.ButterKnife;


public class MasterDetail extends Activity implements SymbolFragment.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.master_detail);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Log.v("DEBUG", "MasterDetail.onCreate()");
    }

    @Override
    public void OnClick(View view) {
        Log.v("DEBUG", "MasterDetail.onClick()");
        // The user selected the Symbol of a stock from the SymbolFragment
        // Do something here to display that stocks details

        DetailsFragment detailsFragment = (DetailsFragment)
                getFragmentManager().findFragmentById(R.id.details_frag);

        if (detailsFragment!= null) {
            // If detail frag is available, we're in two-pane/tablet layout...
            Toast toast = Toast.makeText(this, "Tablet Layout", Toast.LENGTH_SHORT);
            toast.show();

        } else {
            // Create fragment and give it an argument for the selected article
            DetailsFragment newFragment = new DetailsFragment();
            Bundle args = new Bundle();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.list_frag, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }

    }
}
