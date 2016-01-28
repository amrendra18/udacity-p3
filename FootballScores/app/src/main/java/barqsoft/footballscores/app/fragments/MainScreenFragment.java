package barqsoft.footballscores.app.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import barqsoft.footballscores.R;
import barqsoft.footballscores.adapters.ScoresAdapter;
import barqsoft.footballscores.adapters.ViewHolder;
import barqsoft.footballscores.app.activity.MainActivity;
import barqsoft.footballscores.db.DatabaseContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment {
    public ScoresAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String fragmentDate;

    public MainScreenFragment() {
    }

    public void setFragmentDate(String date) {
        fragmentDate = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView score_list = (ListView) rootView.findViewById(R.id.scores_list);
        mAdapter = new ScoresAdapter(getActivity(), null, 0);
        score_list.setAdapter(mAdapter);
        restartLoader();
        mAdapter.detail_match_id = MainActivity.selected_match_id;
        score_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder selected = (ViewHolder) view.getTag();
                mAdapter.detail_match_id = selected.match_id;
                MainActivity.selected_match_id = (int) selected.match_id;
                mAdapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    private void restartLoader() {
        if (getLoaderManager().getLoader(SCORES_LOADER) == null) {
            getLoaderManager().initLoader(SCORES_LOADER, null, matchLoaderCallbacks);
        } else {
            getLoaderManager().restartLoader(SCORES_LOADER, null, matchLoaderCallbacks);
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> matchLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new CursorLoader(
                    getActivity(),
                    DatabaseContract.FixtureEntry.buildScoreWithDate(fragmentDate),
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mAdapter.swapCursor(cursor);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mAdapter.swapCursor(null);
        }
    };

}
