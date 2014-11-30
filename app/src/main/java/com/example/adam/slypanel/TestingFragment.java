package com.example.adam.slypanel;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by adam on 22/11/14.
 */
public class TestingFragment extends Fragment {

    public TestingFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View TestingView = inflater.inflate(R.layout.testing_ssh, container, false);

        return (TestingView);
    }

    sshConnection sshConnect = new sshConnection();

    public void onButtonPressed(View v) {
        Boolean connected = false;
        if (v.getId() == R.id.sendSettingsButton) {
            sshConnect.getSettings();
        }

        if (v.getId() == R.id.connectToHostButton) {
            sshConnect.sshSession.run();
            connected = true;
            sshConnect.statusBarText.setText("Connected, Ready to send commands");
        }

        if (v.getId() == R.id.sendCommandButton) {
            if (connected = true) {
                sshConnect.sendCommand();
                sshConnect.statusBarText.setText("Command sent");
            } else {
                sshConnect.statusBarText.setText("You must connect first!");
            }
        }

    }
}