package com.example.adam.slypanel;

import android.app.Activity;
import android.widget.ProgressBar;

/**
 * Created by adam on 30/11/14.
 */
public class refreshFragments extends Activity{

    sshConnection sshConnect = new sshConnection();

    Thread refreshView = new Thread(new Runnable() {
        @Override
        public void run() {
            ProgressBar cpuPercentage = (ProgressBar) findViewById(R.id.cpuUsageBar);
            ProgressBar ramPercentage = (ProgressBar) findViewById(R.id.ramUsageBar);
            ProgressBar tempValue = (ProgressBar) findViewById(R.id.tempBar);

            cpuPercentage.setProgress(ServerStatusFragment.randInt(0, 100));
            ramPercentage.setProgress(ServerStatusFragment.randInt(0, 100));
            tempValue.setProgress(ServerStatusFragment.randInt(20, 100));

            sshConnect.sshManager.start();
        }
    });


}
