package adam.price.slypanel;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ProgressBar;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.nio.channels.Channel;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


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


    ScheduledExecutorService scheduleExecutor = Executors.newScheduledThreadPool(1);


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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();


    }

    public void onSectionAttached(int number) {

        scheduleExecutor.scheduleAtFixedRate(refreshFragments, 5 , 5, TimeUnit.SECONDS);
        Log.w("Status", "Timer initiated");

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                selectedOption = 1;
                Log.w("Status", "Y'arr you be openin the server status");
                scheduleExecutor.execute(refreshFragments);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                selectedOption = 2;
                Log.w("Status", "SSH be vewwy vewwy quiet");
                scheduleExecutor.shutdown();
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                selectedOption = 3;
                Log.w("Status", "TOOLS? Who wants to edit this stuff? It's perfect... right?");
                scheduleExecutor.shutdown();
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
        }




    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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
            refreshFragments.run();
            Log.w("Status", "*Sprays Fabreeze* - Good as new!");
            return true;
        }
        else if (id == R.id.action_settings){
            //Settings menu goes here
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
           fragment.
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
Runnable refreshFragments = new Runnable() {

    ProgressBar cpuPercentage = (ProgressBar) findViewById(R.id.cpuUsageBar);
    ProgressBar ramPercentage = (ProgressBar) findViewById(R.id.ramUsageBar);
    ProgressBar tempValue = (ProgressBar) findViewById(R.id.tempBar);

    public void run() {

        cpuPercentage.setProgress(ServerStatusFragment.randInt(0, 100));
        ramPercentage.setProgress(ServerStatusFragment.randInt(0, 100));
        tempValue.setProgress(ServerStatusFragment.randInt(20, 100));

//        sshConnection sshConnect = new sshConnection();
//
//        sshConnect.sshManager("testuser", "10.10.67.195", 22, "P@ssword");
    }
};
    public class sshConnection {

        //SSH Command for cpu percentage top -b -n2 | grep "Cpu(s)" | awk '{print $2 + $4}'
        //Get the second line of this for true cpu percentage
        //Takes about 3-4 seconds to get it so refresh at ~5 seconds


        private Session sshSession;
        private String statusMessage;
        private boolean isConnected;

        public String sshManager(String username, String ipAddr, int port, String password) {
            JSch sshManager = new JSch();


            try {

                sshSession = sshManager.getSession(username, ipAddr, port);
                sshSession.setPassword(password);

                sshSession.setConfig("StrictHostKeyChecking", "no");

                sshSession.connect(600);
               Log.w("Status", "I should be connected now!");
                isConnected = true;
                statusMessage = "Connected";

            } catch (JSchException jschX) {
                Log.w("Error", "It borked :(");
                statusMessage = "Connection setup failed";
            }

            return (statusMessage);
        }

        public String sshCommand() {

            try {

                com.jcraft.jsch.Channel sshChannel = sshSession.openChannel("exec");
                ((ChannelExec)sshChannel).setCommand("echo It works | wall");
                sshChannel.connect();
                isConnected = true;

            } catch (JSchException jschX) {
                Log.w("Error", "Couldn't create the channel");
                statusMessage = "Channel creation failed";
            }
                return (statusMessage);
        }
    }


}
