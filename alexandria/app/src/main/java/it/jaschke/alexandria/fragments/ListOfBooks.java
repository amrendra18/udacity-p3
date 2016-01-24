package it.jaschke.alexandria.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.api.BookListAdapter;
import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.logger.Debug;
import it.jaschke.alexandria.utils.Utils;


public class ListOfBooks extends Fragment {

    private BookListAdapter bookListAdapter;
    private int position = ListView.INVALID_POSITION;
    private final int LOADER_ID = 10;

    @Bind(R.id.listOfBooks)
    ListView bookList;

    @Bind(R.id.searchText)
    EditText searchText;

    @OnClick(R.id.searchButton)
    public void searchButtonClicked(View v) {
        ListOfBooks.this.restartLoader();
    }

    public ListOfBooks() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debug.c();
        getActivity().setTitle(getString(R.string.books));
        Cursor cursor = getActivity().getContentResolver().query(
                AlexandriaContract.BookEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        bookListAdapter = new BookListAdapter(getActivity(), cursor, 0);
        View rootView = inflater.inflate(R.layout.fragment_list_of_books, container, false);
        ButterKnife.bind(this, rootView);

        bookList.setAdapter(bookListAdapter);
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Cursor cursor = bookListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(pos)) {
                    position = pos;
                    ((Callback) getActivity())
                            .onItemSelected(cursor.getString(cursor.getColumnIndex(AlexandriaContract.BookEntry._ID)));
                }
            }
        });

        restartLoader();
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //no need
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //no need
            }

            @Override
            public void afterTextChanged(Editable s) {
                restartLoader();
            }
        });
        Utils.hideKeyboard(getActivity());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void restartLoader() {
        if (getLoaderManager().getLoader(LOADER_ID) == null) {
            getLoaderManager().initLoader(LOADER_ID, null, bookListLoaderCallbacks);
        } else {
            getLoaderManager().restartLoader(LOADER_ID, null, bookListLoaderCallbacks);
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> bookListLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            final String selection = AlexandriaContract.BookEntry.TITLE + " LIKE ? OR " + AlexandriaContract.BookEntry.SUBTITLE + " LIKE ? ";
            String searchString = searchText.getText().toString();

            if (searchString.length() > 0) {
                searchString = "%" + searchString + "%";
                return new CursorLoader(
                        getActivity(),
                        AlexandriaContract.BookEntry.CONTENT_URI,
                        null,
                        selection,
                        new String[]{searchString, searchString},
                        null
                );
            }

            return new CursorLoader(
                    getActivity(),
                    AlexandriaContract.BookEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            bookListAdapter.swapCursor(data);
            if (position != ListView.INVALID_POSITION) {
                bookList.smoothScrollToPosition(position);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            bookListAdapter.swapCursor(null);
        }
    };
}
