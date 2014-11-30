package com.example.adam.slypanel;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by adam on 26/11/14.
 */
public class getSettingsFromUser extends Activity {

        String username;
        String password;
        String host;
        String command;


        EditText commandBox = (EditText) findViewById(R.id.commandBox);

        EditText usernameBox = (EditText) findViewById(R.id.usernameBox);
        EditText passwordBox = (EditText) findViewById(R.id.passwordBox);
        EditText ipAddressBox = (EditText) findViewById(R.id.ipAddressBox);

        TextView statusBarText = (TextView) findViewById(R.id.statusBarText);


    void getSettings() {

            username = usernameBox.toString();
            password = passwordBox.toString();
            host = ipAddressBox.toString();
            statusBarText.setText("Ready to connect to: " + host);

        }

        void sendCommand() {
            sshConnection sshConnect = new sshConnection();

            sshConnect.command = commandBox.toString();
            statusBarText.setText("Command ready to be sent to: " + host);
            sshConnect.sshCommand.start();
            statusBarText.setText("\"" + command + "\"" + "was sent to " + host);

        }
}
