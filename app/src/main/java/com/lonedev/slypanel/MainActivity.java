package com.lonedev.slypanel;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int selectedOption;

    //Instances of things
    public sshConnection sshConnect = new sshConnection();
    FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getBaseContext());
    public ConnectionFragment connFrag = new ConnectionFragment();
    public sshKeyInsert sshKeyInsert = new sshKeyInsert();
    public ServerStatusFragment serverStatus = new ServerStatusFragment();


    public static EditText commandBox;
    public static EditText usernameBox;
    public static EditText passwordBox;
    public static EditText ipAddressBox;
    public static TextView statusBarText;
    public static String username = "";
    public static String password = "";
    public static String host = "127.0.0.1";
    public static boolean connected = false;
    public static boolean disconnect = false;
    public static boolean allowRefresh = true;
    private Handler handler = new Handler();

    public static String commandToSend;

    public static String storedUsername;
    public static String storedPassword;
    public static String storedHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    private Runnable refreshTimer = new Runnable() {
        @Override
        public void run() {

            if (allowRefresh) {
                if (ServerStatusFragment.cpuPercentage != null) {
                    ServerStatusFragment.refresh();
                    Log.w("Status", "Refreshed");
                }
                else {
                    Log.w("Error", "It exploded");
                }
            }

            handler.postDelayed(this, 3000);
        }
    };

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();


    }

    public void onSectionAttached(int number) {

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                selectedOption = 1;
                Log.w("Status", "Y'arr you be openin the server status");
                allowRefresh = true;
                sshConnection.testing = false;
                refreshTimer.run();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                selectedOption = 2;
                Log.w("Status", "SSH be vewwy vewwy quiet");
                allowRefresh = false;
                sshConnection.testing = false;
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                selectedOption = 3;
                Log.w("Status", "TOOLS? Who wants to edit this stuff? It's perfect... right?");
                allowRefresh = false;
                sshConnection.testing = false;
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                selectedOption = 4;
                Log.w("Status", "Someone is tinkering...");
                allowRefresh = false;
                sshConnection.testing = true;
                break;
        }

        FragmentManager fragmentManager = getFragmentManager();

        switch (selectedOption) {
            case 1:
                fragmentManager.beginTransaction().replace(R.id.container, new ServerStatusFragment())
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container, new SSHFragment())
                        .commit();
                break;
            case 3:
                //Add a settings page here
                break;
            case 4:
                fragmentManager.beginTransaction().replace(R.id.container, new TestingFragment())
                        .commit();
                break;
        }


    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);

//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        setContentView(R.layout.activity_main);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
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
        if (id == R.id.action_example) {
            connFrag.show(getFragmentManager(), "ConnectionEditor");
            return true;
        } else if (id == R.id.action_settings) {
            //Settings menu goes here
            return super.onOptionsItemSelected(item);

        }

            switch (item.getItemId()) {
                case R.id.cpuGraphEnable:
                    if (item.isChecked()) {
                        serverStatus.setVisibility("cpu", false);
                    }
                    else {
                        serverStatus.setVisibility("cpu", true);
                        return true;
                    }
                case R.id.ramGraphEnable:
                    if (item.isChecked()) {
                        serverStatus.setVisibility("ram", false);
                    }
                    else {
                        serverStatus.setVisibility("ram", true);
                        return true;
                    }
                case R.id.tempGraphEnable:
                    if (item.isChecked()) {
                        serverStatus.setVisibility("temp", false);
                    }
                    else {
                        serverStatus.setVisibility("temp", true);
                        return true;
                    }
                default:
                    return super.onOptionsItemSelected(item);
            }
        }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.settings, popup.getMenu());
        popup.show();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public void onButtonPress(View v) {
        connected = false;
        if (v.getId() == R.id.sendSettingsButton) {
            getSettings();
            Log.w("Status", "Send settings Button pressed");
        }

        if (v.getId() == R.id.connectToHostButton) {
            if (usernameBox.getText().toString().isEmpty()) {
                Log.w("Error", "No SSH details supplied");
                statusBarText.append("Please set the connection details");
            } else {
                final Thread startConnection = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w("Status", "Connect button pressed");
                        if (sshConnect.sshManager.isAlive()) {
                            sshConnect.sshManager.start();
                            Log.w("Status", "sshManager is already running");
                        }
                    }

                });

                Log.w("Status", "Host: " + sshConnection.sshHost +
                                " Username: " + sshConnection.sshUsername +
                                " Command: " + sshConnect.sshSendCommand
                );

                try {
                    startConnection.start();
                } catch (IllegalThreadStateException Exception) {
                    Log.w("Exception", "Output: " + Exception);
                    statusBarText.clearComposingText();
                    statusBarText.append("You may only connect once!");
                }
            }

        }

        if (v.getId() == R.id.sendCommandButton) {
            Log.w("Status", "I heard a send command button was pressed");
            if (connected = true) {
                sshConnect.sendCommand(sshConnection.sshSendCommand);
                statusBarText.append("Command sent");
            } else {
                statusBarText.append("You must connect first!");
            }
        }

        if (v.getId() == R.id.saveButton) {
            //Save connection details
            storedUsername = usernameBox.getText().toString();
            storedPassword = passwordBox.getText().toString();
            storedHost = ipAddressBox.getText().toString();

            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseManager.FeedEntry.USERNAME, storedUsername);
            values.put(DatabaseManager.FeedEntry.PASSWORD, storedPassword);
            values.put(DatabaseManager.FeedEntry.HOST, storedHost);

            long newRowId;
            newRowId = db.insert(
                    DatabaseManager.FeedEntry.CONNECTION_DETAILS,
                    DatabaseManager.FeedEntry.NULLABLE,
                    values);

            statusBarText.setText("Saved to database");
        }

        if (v.getId() == R.id.loadButton) {
            //Load connection details

            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    DatabaseManager.FeedEntry._ID,
                    DatabaseManager.FeedEntry.HOST,
                    DatabaseManager.FeedEntry.USERNAME,
                    DatabaseManager.FeedEntry.PASSWORD,
            };


            Cursor c = db.query(
                    DatabaseManager.FeedEntry.CONNECTION_DETAILS,  // The table to query
                    projection,                                    // The columns to return
                    null,                                          // The columns for the WHERE clause
                    null,                                          // The values for the WHERE clause
                    null,                                          // don't group the rows
                    null,                                          // don't filter by row groups
                    null                                           // The sort order
            );

            c.moveToFirst();
            long itemId = c.getLong(
                    c.getColumnIndexOrThrow(DatabaseManager.FeedEntry._ID)
            );
        }

        if (v.getId() == R.id.sshKeyButton) {
            sshKeyInsert.show(getFragmentManager(), "sshKeyInsert");
        }
        if (v.getId() == R.id.passwordOnlyRadio) {
            Log.w("Button", "Password only selected");
            findViewById(R.id.sshKeyDialog).setFocusableInTouchMode(false);
        }

        if (v.getId() == R.id.sshKeyOnlyRadio) {
            Log.w("Button", "SSH Key only selected");
            findViewById(R.id.sshKeyDialog).setFocusableInTouchMode(true);
            findViewById(R.id.passwordDialog).setFocusableInTouchMode(false);
        }

        if (v.getId() == R.id.sshKeyPasswordRadio) {
            Log.w("Button", "SSH Key & Password");
            findViewById(R.id.sshKeyDialog).setFocusableInTouchMode(true);
            findViewById(R.id.passwordDialog).setFocusableInTouchMode(true);
        }
    }


    void getSettings() {

        sshConnection.sshUsername = usernameBox.getText().toString();
        sshConnection.sshPassword = passwordBox.getText().toString();
        sshConnection.sshHost = ipAddressBox.getText().toString();
        sshConnection.sshSendCommand = commandBox.getText().toString();

        statusBarText.setText("Ready to connect to: " + sshConnection.sshHost);

    }

}

