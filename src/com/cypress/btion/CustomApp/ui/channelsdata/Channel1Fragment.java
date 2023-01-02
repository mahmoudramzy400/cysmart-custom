package com.cypress.btion.CustomApp.ui.channelsdata;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cypress.btion.CustomApp.data.models.CycleChannelG1;
import com.cypress.btion.CustomApp.data.models.SessionG1;
import com.cypress.btion.CustomApp.services.CustomService2Channels;
import com.cypress.btion.CustomApp.services.CustomService3Channels;
import com.cypress.btion.CustomApp.ui.maactivitychart.MaActivityChart;
import com.cypress.btion.CustomApp.ui.mbactivitychart.MbActivityChart;
import com.cypress.btion.CustomApp.utils.BroadCastHandler;
import com.cypress.btion.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Channel1Fragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Channel1Fragment";
    public static final String TITLE = "Channel G1";

    private SessionG1 sessionG1;
    private CycleChannelG1 cycleChannelG1;
    private int mCurrentActiveChannel = 0;

    private Button mM1aButton;
    private Button mM1bButton;
    private LineChart mCycle1Chart;
    private TextView mActiveTextView;
    private EditText mMaxMaValueEditText;
    private EditText mMaxMBVaueEditText;
    private ImageButton mMbDone;
    private ImageButton mMADone;
    private ChannelsDataActivity mActivity;

    private CustomService3Channels mCustomService3Channels;
    private CustomService2Channels mCustomService2Channels;

    private ImageView mProgressTankImageView;
    private RelativeLayout mTankLayout;


    public static Fragment newInstance() {
        return new Channel1Fragment();
    }


    @Override
    public void onAttach(Context context) {
        if (context instanceof ChannelsDataActivity) {
            mActivity = (ChannelsDataActivity) context;
        }
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_channel1, container, false);

        mM1aButton = rootView.findViewById(R.id.btn_m1a);
        mM1bButton = rootView.findViewById(R.id.btn_m1b);
        mCycle1Chart = rootView.findViewById(R.id.chart_cycle1);
        mActiveTextView = rootView.findViewById(R.id.text_active_channel);
        mMaxMaValueEditText = rootView.findViewById(R.id.et_max_ma_value);
        mMaxMBVaueEditText = rootView.findViewById(R.id.et_max_mb_value);
        mMADone = rootView.findViewById(R.id.btn_ma_done);
        mMbDone = rootView.findViewById(R.id.btn_mb_done);
        mProgressTankImageView = rootView.findViewById(R.id.image_tank_progress);
        mTankLayout = rootView.findViewById(R.id.layout_tank);


        mMADone.setOnClickListener(this);
        mMbDone.setOnClickListener(this);


        mM1aButton.setOnClickListener(this);
        mM1bButton.setOnClickListener(this);


        checkCustomService();

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mCustomServiceReceiver, BroadCastHandler.makeCustomIntentFilter());
        checkCustomService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mCustomServiceReceiver);
    }

    private void checkCustomService() {

        if (mActivity != null &&
                mActivity.get3ChannelsCustomService() != null &&
                mActivity.getChannelNumber() == 3) {

            mCustomService3Channels = mActivity.get3ChannelsCustomService();

            if (mCustomService3Channels.getmLastCycleG1() != null) {
                cycleChannelG1 = mCustomService3Channels.getmLastCycleG1();
                drawCycle1Chart();
            }

            if (mCustomService3Channels.getSessionG1() != null) {
                sessionG1 = mCustomService3Channels.getSessionG1();
                setupTank();
            }

            if (mCustomService3Channels.getmCurrentChannelVoltageOn() == 1)
                mActiveTextView.setVisibility(View.VISIBLE);
            else
                mActiveTextView.setVisibility(View.INVISIBLE);

            setupTank();


            //2 channel mode
        }else if (mActivity != null &&
                mActivity.get2ChannelsCustomService() != null &&
                mActivity.getChannelNumber() == 2){

            mCustomService2Channels = mActivity.get2ChannelsCustomService();

            if (mCustomService2Channels.getmLastCycleG1() != null) {
                cycleChannelG1 = mCustomService2Channels.getmLastCycleG1();
                drawCycle1Chart();
            }

            if (mCustomService2Channels.getSessionG1() != null) {
                sessionG1 = mCustomService2Channels.getSessionG1();
                setupTank();
            }

            if (mCustomService2Channels.getmCurrentChannelVoltageOn() == 1)
                mActiveTextView.setVisibility(View.VISIBLE);
            else
                mActiveTextView.setVisibility(View.INVISIBLE);


        }
    }

    private void setupTank() {

        float tankProgress = 0;
        if (sessionG1 != null && sessionG1.getMaValuesAndSeconds() != null && sessionG1.getMaValuesAndSeconds().size() > 0) {


            LinkedHashMap<Integer, Float> hashMap = sessionG1.getMaValuesAndSeconds();

            Iterator iterator = hashMap.entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry<Integer, Float> entry = (Map.Entry<Integer, Float>) iterator.next();

                int time = entry.getKey();
                float ma = entry.getValue();
                Log.i(TAG, "ma is : " + ma);
                Log.i(TAG, "time is : " + time);
                float x = time * ma;

                tankProgress += x;
            }
            float tankProgressPercent = tankProgress / 2000;


            Log.i(TAG, "tankProgess" + tankProgress);


            tankProgressPercent = Math.abs(tankProgressPercent);
            ViewGroup.LayoutParams layoutParams = mProgressTankImageView.getLayoutParams();
            layoutParams.height = (int) (mTankLayout.getHeight() * tankProgressPercent);
            mProgressTankImageView.setLayoutParams(layoutParams);
        }

    }

    /**
     * BroadcastReceiver to receive broadcast from CustomService3Channels
     */
    private BroadcastReceiver mCustomServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();


            if (action.equals(BroadCastHandler.ACTION_ACTIVE_CHANNEL_CHANGED)) {
                Log.i(TITLE, " onReceive :" + action);
                mCurrentActiveChannel = intent.getIntExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL, 0);

                if (mCurrentActiveChannel == 1) {
                    mActiveTextView.setVisibility(View.VISIBLE);
                } else {
                    mActiveTextView.setVisibility(View.INVISIBLE);
                }

            }

            if (action.equals(BroadCastHandler.ACTION_CYCLECHANG1)) {
                Log.i(TITLE, " onReceive :" + action);
                cycleChannelG1 = (CycleChannelG1) intent.getSerializableExtra(BroadCastHandler.EXTRA_CYCLE_CHANG1);
                drawCycle1Chart();

                Log.i(TAG, "cycle 1 added");
                Log.i(TAG, "cycle 1 lds :" + cycleChannelG1.getLds());
                Log.i(TAG, "cycle 1 lds0 :" + cycleChannelG1.getLds0());
                Log.i(TAG, "cycle 1 lgs :" + cycleChannelG1.getLgs());
                Log.i(TAG, "cycle 1 lgs0 : " + cycleChannelG1.getLgs0());
                Log.i(TAG, "cycle 1 ma : " + cycleChannelG1.getM1a());
                Log.i(TAG, "cycle 1 mb : " + cycleChannelG1.getM1b());


            }

            if (action.equals(BroadCastHandler.ACTION_SESSION1_CHANGED) && intent.hasExtra(BroadCastHandler.EXTRA_SESSIONG1)) {
                Log.i(TAG, " onReceive : Session 1 Changed");
                sessionG1 = (SessionG1) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG1);
                setupTank();
            }
        }
    };


    private void drawCycle1Chart() {

        Log.i(TITLE, "drawCycle1Chart");
        ArrayList<Entry> entries = new ArrayList<>();

        if (cycleChannelG1 != null && cycleChannelG1.getTimeAndCurrentDList() != null) {

            for (ArrayList<Integer> timeAndCurrentList : cycleChannelG1.getTimeAndCurrentDList()) {

                int time = timeAndCurrentList.get(0);
                int currentOfD = timeAndCurrentList.get(1);
                Log.i(TITLE, "time:" + time + " currentOfD :" + currentOfD);

                entries.add(new Entry(time, currentOfD));
            }
        } else {
            Log.i(TITLE, "Time and current is null ");
        }

        LineDataSet lineDataSet = new LineDataSet(entries, getString(R.string.title_last_cycle1));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        XAxis xAxis = mCycle1Chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);

        LineData lineData = new LineData(lineDataSet);
        // disable right y axis
        mCycle1Chart.getAxisRight().setEnabled(false);
        mCycle1Chart.getAxisLeft().setAxisMinimum(lineDataSet.getYMin());
        mCycle1Chart.getAxisRight().setAxisMinimum(lineDataSet.getXMin());
        mCycle1Chart.getAxisLeft().setGranularity(1);
        // set data to chart
        mCycle1Chart.setData(lineData);
        // animate the chart
        mCycle1Chart.animateX(2000);
        // refresh
        mCycle1Chart.invalidate();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_ma_done:
                btnMaDoneClicked();
                break;

            case R.id.btn_mb_done:
                btnMbDoneClicked();
                break;

            case R.id.btn_m1a:
                btnMaClicked();
                break;

            case R.id.btn_m1b:
                btnMbClicked();
                break;
        }
    }

    private void btnMaDoneClicked() {

        if (mMaxMaValueEditText != null &&
                mMaxMaValueEditText.getText() != null &&
                !mMaxMaValueEditText.getText().toString().isEmpty()) {

            float maMaxValue = Float.valueOf(mMaxMaValueEditText.getText().toString());

            // 3 Channel mode
            if (mCustomService3Channels != null && mCustomService3Channels.getSessionG1() != null &&
            mActivity.getChannelNumber() == 3 ) {
                mCustomService3Channels.getSessionG1().setMaxMavalue(maMaxValue);
                mMaxMaValueEditText.setText("");
                mMaxMaValueEditText.setHint("" + maMaxValue);


                // 2 channel mode
            }else if (mCustomService2Channels != null && mCustomService2Channels.getSessionG1() != null &&
                    mActivity.getChannelNumber() == 2 ) {

                mCustomService2Channels.getSessionG1().setMaxMavalue(maMaxValue);
                mMaxMaValueEditText.setText("");
                mMaxMaValueEditText.setHint("" + maMaxValue);
            }
        }
    }

    private void btnMbDoneClicked() {

        if (mMaxMBVaueEditText != null &&
                mMaxMBVaueEditText.getText() != null &&
                !mMaxMBVaueEditText.getText().toString().isEmpty()) {


            float mbMaxValue = Float.valueOf(mMaxMBVaueEditText.getText().toString());

            if (mCustomService3Channels != null && mCustomService3Channels.getSessionG1() != null) {
                mCustomService3Channels.getSessionG1().setMaxMbValue(mbMaxValue);
                mMaxMBVaueEditText.setText("");
                mMaxMBVaueEditText.setHint("" + mbMaxValue);

                //2 Channel mode
            }else if (mCustomService2Channels != null && mCustomService2Channels.getSessionG1() != null) {
                mCustomService2Channels.getSessionG1().setMaxMbValue(mbMaxValue);
                mMaxMBVaueEditText.setText("");
                mMaxMBVaueEditText.setHint("" + mbMaxValue);
            }
        }
    }


    private void btnMaClicked() {
        if (sessionG1 != null) {

            Intent intent = new Intent(getActivity(), MaActivityChart.class);
            intent.putExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL, 1);
            intent.putExtra(BroadCastHandler.EXTRA_SESSIONG1, sessionG1);
            startActivity(intent);
        }

    }


    private void btnMbClicked() {
        if (sessionG1 != null) {

            Intent intent = new Intent(getActivity(), MbActivityChart.class);
            intent.putExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL, 1);
            intent.putExtra(BroadCastHandler.EXTRA_SESSIONG1, sessionG1);
            startActivity(intent);
        }
    }
}
