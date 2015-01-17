package com.lonedev.slypanel;

import android.app.Activity;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

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
    static String sshSendCommand = "fakecommand";
    static boolean testing;

    BufferedReader replyFromServer;
    Pattern alphaNumeric = Pattern.compile("([^a-zA-z0-9])");
    public static String terminator = "zDonez";


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.fragment_main);
    }

    static Session sshSession ;
    String statusMessage;
    boolean isConnected;
    JSch jschManager = new JSch();


    Thread sshManager = new Thread(new Runnable() {
        @Override
        public void run() {

            try {
                try {
                    String sshKeyFile = null;
                    sshSession = jschManager.getSession(sshUsername, sshHost, port);
                    sshSession.setPassword(sshPassword);
                    sshSession.setConfig("StrictHostKeyChecking", "no");
                    try {
                        //Store SSH Key either in database or in a local key file on the device
                        sshKeyFile = new File(getFilesDir(), "sshkey.pub").getAbsolutePath();
                    }
                    catch (NullPointerException Exception) {
                        Log.w("Exception", Exception);
                    }

                    if (sshKeyFile != null) {
                        jschManager.addIdentity(sshKeyFile);
                        Log.w("Status", "SSH Public Key loaded");
                    }
                    else {
                        Log.w("Status", "SSH Key Failed to load");
                    }
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
                    replyFromServer = new BufferedReader(new InputStreamReader(cExec.getInputStream()));
//                    sshSendCommand += " && echo \"" + terminator + "\"";
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


    void sendCommand(String sshSendCommand) {
        if (testing = true) {
            sshSendCommand = MainActivity.commandBox.getText().toString();
        }
        else {
            sshSendCommand = MainActivity.commandToSend;
        }
//        MainActivity.statusBarText.setText("Command ready to be sent to: " + sshHost);

        try {
            sshManager.start();
        }
        catch (java.lang.IllegalThreadStateException Exception) {
            sshManager.interrupt();
            sshManager = new sshConnection().sshManager;
            sshManager.start();
            Log.w("Exception", "Output: " + Exception);
        }

        MainActivity.statusBarText.setText("Command: " + sshSendCommand + "was sent to " + sshHost);

    }

    public String getResponse() throws IOException {
        int count = 0;
        String line = "";

        StringBuilder sBuilder = new StringBuilder();

        for (count = 0; true; count++) {
            line = replyFromServer.readLine();
            sBuilder.append(line).append("\n");

            if (line.contains(terminator) && (++count > 1)) {
                break;
            }
        }
            String result = sBuilder.toString();


            int beginIndex = result.indexOf(terminator+"\"") + ((terminator+"\"").length());
            result = result.substring(beginIndex);
            return result.replaceAll(escape(terminator), "").trim();



    }
    private String escape(String subjectString){
        return alphaNumeric.matcher(subjectString).replaceAll("\\\\$1");
    }
}
