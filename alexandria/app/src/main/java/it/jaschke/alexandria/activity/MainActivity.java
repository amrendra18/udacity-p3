package it.jaschke.alexandria.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import it.jaschke.alexandria.receiver.MessageReceiver;

import it.jaschke.alexandria.fragments.NavigationDrawerFragment;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.api.Callback;
import it.jaschke.alexandria.barcode.BarcodeCaptureActivity;
import it.jaschke.alexandria.fragments.About;
import it.jaschke.alexandria.fragments.AddBook;
import it.jaschke.alexandria.fragments.BookDetail;
import it.jaschke.alexandria.fragments.ListOfBooks;
import it.jaschke.alexandria.logger.Debug;
import it.jaschke.alexandria.utils.AppConstants;
import it.jaschke.alexandria.utils.Utils;


public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, Callback {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence title;
    public static boolean IS_TABLET = false;
    private BroadcastReceiver messageReceiver;

    public static final String MESSAGE_EVENT = "MESSAGE_EVENT";
    public static final String MESSAGE_KEY = "MESSAGE_EXTRA";

    private String mEan = null;

    // slide menu items
    private String[] navMenuTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        IS_TABLET = isTablet();
        if (IS_TABLET) {
            setContentView(R.layout.activity_main_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setLogo(R.drawable.ic_launcher);
            actionBar.setDisplayUseLogoEnabled(true);
        }


        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter(MESSAGE_EVENT);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, filter);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        navigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        Utils.hideKeyboard(this);

        if (savedInstanceState != null) {
            mEan = savedInstanceState.getString(AppConstants.EAN, null);
            if (IS_TABLET && findViewById(R.id.right_container) != null && mEan != null) {
                onItemSelected(mEan);
                mEan = null;
            }
        }

    }

    public String[] getNavMenuStrings() {
        return navMenuTitles;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Debug.c();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment nextFragment;

        switch (position) {
            default:
            case 0:
                nextFragment = new ListOfBooks();
                break;
            case 1:
                nextFragment = new AddBook();
                break;
            case 2:
                nextFragment = new About();
                break;
        }

        title = navMenuTitles[position];

        // if its tablet, clear the right fragment in case of detail
        if (IS_TABLET && findViewById(R.id.right_container) != null) {
            fragmentManager.popBackStack(getString(R.string.detail), FragmentManager
                    .POP_BACK_STACK_INCLUSIVE);
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, nextFragment)
                .addToBackStack((String) title)
                .commit();

        setUpTitle((String) title);
        Utils.hideKeyboard(this);
    }


    public void setUpTitle(String title) {
        Debug.e(title, false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            //restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == android.R.id.home) {
            Utils.hideKeyboard(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.EAN, mEan);
    }

    @Override
    public void onItemSelected(String ean) {
        Bundle args = new Bundle();
        args.putString(BookDetail.EAN_KEY, ean);
        mEan = ean;

        Utils.hideKeyboard(this);

        BookDetail fragment = new BookDetail();
        fragment.setArguments(args);

        int id = R.id.container;
        if (findViewById(R.id.right_container) != null) {
            id = R.id.right_container;
        }
        setUpTitle(getResources().getString(R.string.detail));
        getSupportFragmentManager().beginTransaction()
                .replace(id, fragment)
                .addToBackStack(getResources().getString(R.string.detail))
                .commit();


    }

    public void goBack(View view) {
        getSupportFragmentManager().popBackStack();
    }

    private boolean isTablet() {
        return (getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public void onBackPressed() {
        // Bug fixed :
        // Check for correct title to be set, when previous fragment is popped.
        int pos = getSupportFragmentManager().getBackStackEntryCount();
        if (pos < 2) {
            finish();
        } else {
            String tr = getSupportFragmentManager().getBackStackEntryAt(pos - 2).getName();
            setUpTitle(tr);
        }
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddBook.REQUEST_SCAN_BARCODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Debug.bundle(bundle);
                    String barcode = bundle.getString(BarcodeCaptureActivity.BarcodeObject, "");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment addBook = new AddBook();
                    Bundle args = new Bundle();
                    args.putString(AddBook.SCANNED_BARCODE, barcode);
                    addBook.setArguments(args);
                    //remove the previous addBook fragment
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, addBook)
                            .addToBackStack((String) title)
                            .commit();

                }
            }
        }
    }

}