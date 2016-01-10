package it.jaschke.alexandria.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.barcode.BarcodeCaptureActivity;
import it.jaschke.alexandria.data.AlexandriaContract;
import it.jaschke.alexandria.logger.Debug;
import it.jaschke.alexandria.services.BookService;
import it.jaschke.alexandria.services.DownloadImage;


public class AddBook extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "INTENT_TO_SCAN_ACTIVITY";
    public static final int REQUEST_SCAN_BARCODE = 101;
    public static final String SCANNED_BARCODE = "SCANNED_BARCODE";
    private final int LOADER_ID = 1;
    private final String EAN_CONTENT = "eanContent";
    private static final String SCAN_FORMAT = "scanFormat";
    private static final String SCAN_CONTENTS = "scanContents";

    private String mScanFormat = "Format:";
    private String mScanContents = "Contents:";

    @Bind(R.id.save_button)
    Button saveButton;

    @Bind(R.id.scan_button)
    Button scanButton;

    @Bind(R.id.delete_button)
    Button deleteButton;

    @Bind(R.id.ean)
    EditText eanEditText;

    @Bind(R.id.bookTitle)
    TextView bookTitleTextView;

    @Bind(R.id.bookSubTitle)
    TextView bookSubTitleTextView;

    @Bind(R.id.authors)
    TextView authorsTextView;

    @Bind(R.id.categories)
    TextView categoriesTextView;

    @Bind(R.id.bookCover)
    ImageView bookCoverImageView;


    public AddBook() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (eanEditText != null) {
            outState.putString(EAN_CONTENT, eanEditText.getText().toString());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        getActivity().setTitle(getString(R.string.scan));
        ButterKnife.bind(this, rootView);
        Bundle bundle = getArguments();
        Debug.bundle(bundle);
        if(bundle!=null){
            String barcode = bundle.getString(SCANNED_BARCODE, "");
            eanEditText.setText(barcode);
            loadBookFromISBN(barcode);
        }
        eanEditText.addTextChangedListener(new TextWatcher() {
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
                String ean = s.toString();
                //catch isbn10 numbers
                if (ean.length() == 10 && !ean.startsWith("978")) {
                    ean = "978" + ean;
                }
                if (ean.length() < 13) {
                    clearFields();
                    return;
                }

                loadBookFromISBN(ean);

            }
        });

        if (savedInstanceState != null) {
            eanEditText.setText(savedInstanceState.getString(EAN_CONTENT));
            eanEditText.setHint("");
        }

        return rootView;
    }

    private void loadBookFromISBN(String barcode){
        //Once we have an ISBN, start a book intent
        Intent bookIntent = new Intent(getActivity(), BookService.class);
        bookIntent.putExtra(BookService.EAN, barcode);
        bookIntent.setAction(BookService.FETCH_BOOK);
        getActivity().startService(bookIntent);
        AddBook.this.restartLoader();
    }

    @OnClick(R.id.delete_button)
    void deleteButtonClicked(View view) {
        Debug.c();
        Intent bookIntent = new Intent(getActivity(), BookService.class);
        bookIntent.putExtra(BookService.EAN, eanEditText.getText().toString());
        bookIntent.setAction(BookService.DELETE_BOOK);
        getActivity().startService(bookIntent);
        eanEditText.setText("");
    }

    @OnClick(R.id.scan_button)
    void scanButtonClicked(View view) {
        Debug.c();
        Debug.i("scan to perform", false);

        Intent intent = new Intent(getActivity(), BarcodeCaptureActivity.class);

        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        getActivity().startActivityForResult(intent, REQUEST_SCAN_BARCODE);
    }

    @OnClick(R.id.save_button)
    void saveButtonClicked(View view) {
        Debug.c();
        eanEditText.setText("");
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (eanEditText.getText().length() == 0) {
            return null;
        }
        String eanStr = eanEditText.getText().toString();
        if (eanStr.length() == 10 && !eanStr.startsWith("978")) {
            eanStr = "978" + eanStr;
        }
        return new CursorLoader(
                getActivity(),
                AlexandriaContract.BookEntry.buildFullBookUri(Long.parseLong(eanStr)),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (!data.moveToFirst()) {
            return;
        }

        String bookTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.TITLE));
        bookTitleTextView.setText(bookTitle);

        String bookSubTitle = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.SUBTITLE));
        bookSubTitleTextView.setText(bookSubTitle);

        String authors = data.getString(data.getColumnIndex(AlexandriaContract.AuthorEntry.AUTHOR));
        String[] authorsArr = authors.split(",");
        authorsTextView.setLines(authorsArr.length);
        authorsTextView.setText(authors.replace(",", "\n"));
        String imgUrl = data.getString(data.getColumnIndex(AlexandriaContract.BookEntry.IMAGE_URL));
        if (Patterns.WEB_URL.matcher(imgUrl).matches()) {
            new DownloadImage(bookCoverImageView).execute(imgUrl);
            bookCoverImageView.setVisibility(View.VISIBLE);
        }

        String categories = data.getString(data.getColumnIndex(AlexandriaContract.CategoryEntry.CATEGORY));
        categoriesTextView.setText(categories);

        saveButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    private void clearFields() {
        Debug.c();
        bookTitleTextView.setText("");
        bookSubTitleTextView.setText("");
        authorsTextView.setText("");
        categoriesTextView.setText("");
        bookCoverImageView.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
    }




}
