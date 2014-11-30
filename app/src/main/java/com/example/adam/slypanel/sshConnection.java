package com.example.adam.slypanel;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Created by adam on 26/11/14.
 */
public class sshConnection extends Activity {

    //SSH Command for cpu percentage top -b -n2 | grep "Cpu(s)" | awk '{print $2 + $4}'
    //Get the second line of this for true cpu percentage
    //Takes about 3-4 seconds to get it so refresh at ~5 seconds


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_main);
    }

    EditText commandBox = (EditText) findViewById(R.id.commandBox);
    EditText usernameBox = (EditText) findViewById(R.id.usernameBox);
    EditText passwordBox = (EditText) findViewById(R.id.passwordBox);
    EditText ipAddressBox = (EditText) findViewById(R.id.ipAddressBox);
    TextView statusBarText = (TextView) findViewById(R.id.statusBarText);

    String username;
    String password;
    String host;
    int port = 22;

    Session sshSession;
    String statusMessage;
    boolean isConnected;
    String command = " ";

    Thread sshManager = new Thread(new Runnable() {
        JSch sshManager = new JSch();

        @Override
        public void run() {

            try {

                sshSession = sshManager.getSession(username, host, port);
                sshSession.setPassword(password);

                sshSession.setConfig("StrictHostKeyChecking", "no");

                sshSession.connect(600);

                Log.w("Status", "I should be connected now!");
                isConnected = true;
                statusMessage = "Connected";
                Log.w("ssh", statusMessage);
                Log.w("ssh", "Attempting to connect");
//                    sshCommand.start();

            } catch (JSchException jschX) {
                Log.w("Error", "It borked :(");
                statusMessage = "Connection setup failed";
                Log.w("ssh", statusMessage);
            }
        }
    }) {


    };

    Thread sshCommand = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Channel sshChannel = sshSession.openChannel("exec");
//                    ((ChannelExec) sshChannel).setCommand("echo lel | wall;");
                if (command == " ") {

                    command = "echo There was no command | wall";
                    statusMessage = "There was no command!";
                }
                ChannelExec cExec = (ChannelExec) sshChannel;
                cExec.setCommand(command);
                cExec.connect();
                Log.w("Status", "Command was sent... Hopefully");
                isConnected = true;



                // Disconnect from session after sending the command?
                cExec.disconnect();
                sshSession.disconnect();
            } catch (JSchException jschX) {
                Log.w("Error", "Couldn't create the channel");
                statusMessage = "Channel creation failed";
            }
        }
    });




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
