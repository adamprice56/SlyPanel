package com.lonedev.slypanel;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import java.util.Random;


/**
 * Created by root on 02/10/14.
 */
public class ServerStatusFragment extends Fragment {

    public static ProgressBar cpuPercentage;
    public static ProgressBar ramPercentage;
    public static ProgressBar tempValue;
    public static XYPlot cpuGraph;
    public static SimpleXYSeries cpuGraphSeries;
    public static XYPlot ramGraph;
    public static SimpleXYSeries ramGraphSeries;
    public static XYPlot tempGraph;
    public static SimpleXYSeries tempGraphSeries;

    public static GridLayout cpuGraphLayout;
    public static GridLayout ramGraphLayout;
    public static GridLayout tempGraphLayout;

    public ServerStatusFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View ServerStatusView = inflater.inflate(R.layout.fragment_server_status, container, false);

        return (ServerStatusView);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        cpuPercentage = (ProgressBar) view.findViewById(R.id.cpuUsageBar);
        cpuGraphLayout = (GridLayout) view.findViewById(R.id.gridLayoutCpu);
        ramPercentage = (ProgressBar) view.findViewById(R.id.ramUsageBar);
        ramGraphLayout = (GridLayout) view.findViewById(R.id.gridLayoutRam);
        tempValue = (ProgressBar) view.findViewById(R.id.tempBar);
        tempGraphLayout = (GridLayout) view.findViewById(R.id.gridLayoutTemp);


        cpuGraph = (XYPlot) view.findViewById(R.id.cpuGraph);
        cpuGraphSeries = new SimpleXYSeries("CPU Usage");
        setupCpuGraph();

        ramGraph = (XYPlot) view.findViewById(R.id.ramGraph);
        ramGraphSeries = new SimpleXYSeries("RAM Usage");
        setupRamGraph();

        tempGraph = (XYPlot) view.findViewById(R.id.tempGraph);
        tempGraphSeries = new SimpleXYSeries("Temperature");
        setupTempGraph();

    }

    //For testing -- Replace when needed
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static void refresh() {
        int cpuint = randInt(0, 100);
        int ramint = randInt(0, 100);
        int tempint = randInt(20, 100);

        cpuPercentage.setProgress(cpuint);
        ramPercentage.setProgress(ramint);
        tempValue.setProgress(tempint);

        cpuGraphSeries.addLast(null, cpuint);
        ramGraphSeries.addLast(null, ramint);
        tempGraphSeries.addLast(null, tempint);

        if (cpuGraphSeries.size() > 31) {
            cpuGraphSeries.removeFirst();
        }
        if (ramGraphSeries.size() > 31) {
            ramGraphSeries.removeFirst();
        }
        if (tempGraphSeries.size() > 31) {
            tempGraphSeries.removeFirst();
        }

        cpuGraph.redraw();
        ramGraph.redraw();
        tempGraph.redraw();

    }

    void setupCpuGraph() {
        cpuGraphSeries.useImplicitXVals();
        cpuGraph.addSeries(cpuGraphSeries, new LineAndPointFormatter(Color.GREEN, Color.RED, Color.TRANSPARENT, null));

        cpuGraph.setDomainLabel("Time (Seconds)");
        cpuGraph.setDomainStepValue(3);
        cpuGraph.setDomainBoundaries(0, 30, BoundaryMode.FIXED);
        cpuGraph.getDomainLabelWidget().pack();

        cpuGraph.setRangeLabel("Usage (%)");
        cpuGraph.setRangeStepValue(10);
        cpuGraph.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        cpuGraph.getRangeLabelWidget().pack();

        cpuGraph.setGridPadding(1, 1, 1, 1);
    }

    void setupRamGraph() {
        ramGraphSeries.useImplicitXVals();
        ramGraph.addSeries(ramGraphSeries, new LineAndPointFormatter(Color.GREEN, Color.RED, Color.TRANSPARENT, null));

        ramGraph.setDomainLabel("Time (Seconds)");
        ramGraph.setDomainStepValue(3);
        ramGraph.setDomainBoundaries(0, 30, BoundaryMode.FIXED);
        ramGraph.getDomainLabelWidget().pack();

        ramGraph.setRangeLabel("Usage (%)");
        ramGraph.setRangeStepValue(10);
        ramGraph.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        ramGraph.getRangeLabelWidget().pack();

        ramGraph.setGridPadding(1, 1, 1, 1);
    }

    void setupTempGraph() {
        tempGraphSeries.useImplicitXVals();
        tempGraph.addSeries(tempGraphSeries, new LineAndPointFormatter(Color.GREEN, Color.RED, Color.TRANSPARENT, null));

        tempGraph.setDomainLabel("Time (Seconds)");
        tempGraph.setDomainStepValue(3);
        tempGraph.setDomainBoundaries(0, 30, BoundaryMode.FIXED);
        tempGraph.getDomainLabelWidget().pack();

        tempGraph.setRangeLabel("Temperature (Degrees)");
        tempGraph.setRangeStepValue(10);
        tempGraph.setRangeBoundaries(0, 100, BoundaryMode.FIXED);
        tempGraph.getRangeLabelWidget().pack();

        tempGraph.setGridPadding(1, 1, 1, 1);
    }

    void setVisibility(String graph, boolean visible) {
        switch (graph) {
            case "cpu":
                if (visible) {
                    cpuGraphLayout.setVisibility(View.VISIBLE);
                } else {
                    cpuGraphLayout.setVisibility(View.GONE);
                }
            case "ram":
                if (visible) {
                    ramGraphLayout.setVisibility(View.VISIBLE);
                } else {
                    ramGraphLayout.setVisibility(View.GONE);
                }
            case "temp":
                if (visible) {
                    tempGraphLayout.setVisibility(View.VISIBLE);
                } else {
                    tempGraphLayout.setVisibility(View.GONE);
                }
        }
    }
}
