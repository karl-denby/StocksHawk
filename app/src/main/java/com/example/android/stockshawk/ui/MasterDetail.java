package com.example.android.stockshawk.ui;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.stockshawk.R;

import butterknife.ButterKnife;


public class MasterDetail extends Activity implements SymbolFragment.OnClickListener {

    Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.master_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void OnClick(View view) {
        // The user selected the headline of an article from the HeadlinesFragment
        // Do something here to display that article

        DetailsFragment detailsFragment = (DetailsFragment)
                getFragmentManager().findFragmentById(R.id.details_frag);

        if (mToast!=null) {
            mToast.cancel();
        }

        if (detailsFragment!= null) {
            // If detail frag is available, we're in two-pane/tablet layout...
            mToast = Toast.makeText(this, "Tablet Layout", Toast.LENGTH_SHORT);

        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...
            mToast = Toast.makeText(this, "Phone Layout", Toast.LENGTH_SHORT);

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

        if (mToast!=null) {
            mToast.show();
        }

    }
}
