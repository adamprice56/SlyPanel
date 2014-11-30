package com.example.adam.slypanel;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 02/10/14.
 */
public class CreateConnection extends Fragment {

    public CreateConnection() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View connectionView = inflater.inflate(R.layout.fragment_create_connection, container, false);

        return (connectionView);
    }
}
