package com.lonedev.slypanel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by adam on 10/01/15.
 */
public class ConnectionFragment extends DialogFragment {

    public static RadioButton passwordOnly;
    public static RadioButton sshKeyOnly;
    public static RadioButton sshKeyAndPassword;
    public static EditText host;
    public static EditText username;
    public static EditText password;
    public static EditText sshKey;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                //View XML
                .setView(R.layout.connection_editor)
                //Title
                .setTitle("Connection Editor")
                //Right button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Send details
                        sshConnection.sshHost = host.getText().toString();
                        sshConnection.sshUsername = username.getText().toString();
                        sshConnection.sshPassword = password.getText().toString();
                    }
                })
                //Left button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Negative action
                        ConnectionFragment.this.getDialog().cancel();
                    }
                });

        return  builder.create();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        host = (EditText) view.findViewById(R.id.hostDialog);
        username = (EditText) view.findViewById(R.id.usernameDialog);
        password = (EditText) view.findViewById(R.id.passwordDialog);
        sshKey = (EditText) view.findViewById(R.id.sshKeyDialog);

        passwordOnly = (RadioButton) view.findViewById(R.id.passwordOnlyRadio);
        sshKeyOnly = (RadioButton) view.findViewById(R.id.sshKeyOnlyRadio);
        sshKeyAndPassword = (RadioButton) view.findViewById(R.id.sshKeyPasswordRadio);

    }

}
