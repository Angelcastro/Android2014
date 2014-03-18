package com.castro.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RutasDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link RutasListFragment} and the item details
 * (if present) is a {@link RutasDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link RutasListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class RutasListActivity extends FragmentActivity
        implements RutasListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;

            ((RutasListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.item_list))
                    .setActivateOnItemClick(true);
        }

    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(RutasDetailFragment.ARG_ITEM_ID, id);
            RutasDetailFragment fragment = new RutasDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, RutasDetailActivity.class);
            detailIntent.putExtra(RutasDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
