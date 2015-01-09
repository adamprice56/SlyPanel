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
    static String sshHost = "not.an.ip.address";
    static int port = 22;


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.fragment_main);
    }

    static Session sshSession ;
    String statusMessage;
    boolean isConnected;
    String sshSendCommand = "fakecommand";
    JSch jschManager = new JSch();


    Thread sshManager = new Thread(new Runnable() {
        @Override
        public void run() {

                try {
                    try {
                        sshSession = jschManager.getSession(sshUsername, sshHost, port);
                        sshSession.setPassword(sshPassword);
                        sshSession.setConfig("StrictHostKeyChecking", "no");
                        sshSession.connect(600);
                        Channel sshChannel = sshSession.openChannel("exec");
                        if (sshSendCommand == "fakecommand") {
                            try {
                                // Try to set the command
                                sshSendCommand = MainActivity.commandBox.getText().toString();
                                if (sshSendCommand == "fakecommand") ;
                                {
                                    // If the command fails to set, Throw any exception
                                    throw new NoSuchMethodError();
                                }
                            } catch (NoSuchMethodError Exception) {
                                // Send a pre set command instead
                                Log.w("Error", "Unable to set a command");
                                sshSendCommand = "echo There was no command | wall";
                                statusMessage = "There was no command!";
                            }

                        } else {
                            sshSendCommand = MainActivity.commandBox.getText().toString();
                            Log.w("Status", "Command " + sshSendCommand + " Is ready to be sent");
                        }
                        ChannelExec cExec = (ChannelExec) sshChannel;
                        cExec.setCommand(sshSendCommand);
                        cExec.connect();
                        Log.w("Status", "Command was sent... Hopefully");
                        MainActivity.connected = true;

                        if (MainActivity.disconnect == true) {
                            // Disconnect from session after sending the command?
                            cExec.disconnect();
                            sshSession.disconnect();
                            MainActivity.connected = false;
                        }

                    } catch (JSchException jschX) {
                        Log.w("Error", "Couldn't create the channel");
                        statusMessage = "Channel creation failed";
                        Log.w("JschException", jschX);
                    }
                }
                 catch (Exception Exception) {

                    Log.w("Exception", Exception);
                }
            }
    });



    void sendCommand() {

        sshSendCommand = MainActivity.commandBox.getText().toString();
//        MainActivity.statusBarText.setText("Command ready to be sent to: " + sshHost);

        try {
            sshManager.start();
        }
        catch (java.lang.IllegalThreadStateException Exception) {
            sshManager.interrupt();
            Log.w("Exception", "Output: " + Exception);
        }

        MainActivity.statusBarText.setText("Command: " + sshSendCommand + "was sent to " + sshHost);

    }

}
