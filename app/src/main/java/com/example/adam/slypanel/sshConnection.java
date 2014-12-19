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
    static String sshUsername = "nullUsername";
    static String sshPassword = "nullPassword";
    static String sshHost = "127.0.0.1";
    static int port = 22;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.fragment_main);
    }

    Session sshSession;
    String statusMessage;
    boolean isConnected;
    String sshSendCommand = "fakecommand";


    Thread sshManager = new Thread(new Runnable() {
        JSch sshManager = new JSch();

        @Override
        public void run() {

            try {
                if (sshUsername == "nullUsername") {
                    Log.w("Minor", "No host has been set");
                }
                sshSession = sshManager.getSession(sshUsername, sshHost, port);
                sshSession.setPassword(sshPassword);

                Log.w("Warn", "Host key checking is DISABLED");
                sshSession.setConfig("StrictHostKeyChecking", "no");

                sshSession.connect(600);

                Log.w("Status", "I should be connected now!");
                isConnected = true;
                statusMessage = "Trying to connect";
                Log.w("ssh", statusMessage);
                Log.w("ssh", "Hey server! I wanna connect!");
                sendCommand();

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
                 if (sshSession == null) {
                        sshManager.run();
                        Log.w("Error", "sshSession returned null - Retrying config");

                    } else {
                        Channel sshChannel = sshSession.openChannel("exec");
//                    ((ChannelExec) sshChannel).setCommand("echo lel | wall;");
                        if (sshSendCommand == "fakecommand") {

                            sshSendCommand = "echo There was no command | wall";
                            statusMessage = "There was no command!";
                        }
                        ChannelExec cExec = (ChannelExec) sshChannel;
                        cExec.setCommand(sshSendCommand);
                        cExec.connect();
                        Log.w("Status", "Command was sent... Hopefully");
                        isConnected = true;


                        // Disconnect from session after sending the command?
//                        cExec.disconnect();
//                        sshSession.disconnect();
                    }

            } catch (JSchException jschX) {
                Log.w("Error", "Couldn't create the channel");
                statusMessage = "Channel creation failed";
            }
        }
    });


    void sendCommand() {

        sshSendCommand = MainActivity.commandBox.toString();
        MainActivity.statusBarText.setText("Command ready to be sent to: " + sshHost);
        sshCommand.start();
        MainActivity.statusBarText.setText("\"" + sshSendCommand + "\"" + "was sent to " + sshHost);

    }

}
