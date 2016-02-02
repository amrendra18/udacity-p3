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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import barqsoft.footballscores.R;
import barqsoft.footballscores.adapters.FootballScoreAdapter;
import barqsoft.footballscores.adapters.ViewHolder;
import barqsoft.footballscores.app.activity.MainActivity;
import barqsoft.footballscores.db.DatabaseContract;
import barqsoft.footballscores.logger.Debug;
import barqsoft.footballscores.utils.AppConstants;
import barqsoft.footballscores.utils.AppUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment {
    public FootballScoreAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private String fragmentDate;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.error_message)
    TextView errorTv;

    @Bind(R.id.view_error)
    RelativeLayout errorLayout;

    @Bind(R.id.view_loading)
    RelativeLayout loadingLayout;

    @Bind(R.id.scores_list)
    ListView score_list;

    public MainScreenFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bug fix : on device rotation date was set to null, hence no data was loaded for null date
        Bundle bundle = getArguments();
        if (bundle != null) {
            fragmentDate = bundle.getString(AppConstants.FRAGMENT_DATE);
        } else {
            Debug.e("Should not happen. Fragmentdate not supllied", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        mAdapter = new FootballScoreAdapter(getActivity(), null, 0);
        score_list.setAdapter(mAdapter);
        restartLoader();
        mAdapter.detail_match_id = MainActivity.selected_match_id;
        score_list.setOnItemClickListener((parent, view, position, id) -> {
            ViewHolder selected = (ViewHolder) view.getTag();
            mAdapter.detail_match_id = selected.match_id;
            MainActivity.selected_match_id = (int) selected.match_id;
            mAdapter.notifyDataSetChanged();
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void restartLoader() {
        progressBar.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.VISIBLE);
        errorTv.setVisibility(View.INVISIBLE);
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
                    DatabaseContract.FixtureEntry.TIME_COL + " ASC"
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
            mAdapter.swapCursor(cursor);

            progressBar.setVisibility(View.INVISIBLE);


            if (cursor != null && cursor.getCount() > 0) {
                errorTv.setVisibility(View.INVISIBLE);
                errorLayout.setVisibility(View.INVISIBLE);
            } else {
                if (!AppUtils.isNetworkConnected(getActivity())) {
                    Debug.showToastShort(getActivity().getString(R.string
                            .internet), getActivity(), true);
                }
                errorTv.setText(getActivity().getString(R.string.nomatch));
                errorTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.egg_empty, 0, 0);
                errorTv.setContentDescription(getActivity().getString(R.string.nomatch));
                errorTv.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {
            mAdapter.swapCursor(null);
        }
    };

}
