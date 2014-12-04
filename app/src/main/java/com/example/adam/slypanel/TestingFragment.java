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

}