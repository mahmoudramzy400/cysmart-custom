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

import com.cypress.btion.CustomApp.data.models.CycleChannelG2;
import com.cypress.btion.CustomApp.data.models.SessionG2;
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
public class Channel2Fragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Channel2Fragment" ;
    public static final String TITLE = "Channel G2" ;


    private SessionG2 sessionG2;
    private CycleChannelG2 cycleChannelG2;
    private int mCurrentActiveChannel = 0;

    private Button mM2aButton ;
    private Button mM2bButton ;
    private LineChart mCycle1Chart ;
    private TextView mActiveTextView ;
    private EditText mMaxMaValueEditText;
    private EditText mMaxMBVaueEditText;
    private ImageButton mMbDone ;
    private ImageButton mMADone ;
    private ChannelsDataActivity mActivity;
    private CustomService3Channels mCustomService3Channels;
    private CustomService2Channels mCustomService2Channels;


    private ImageView mProgressTankImageView ;
    private RelativeLayout mTankLayout ;

    public static Fragment newInstance (){
        return  new Channel2Fragment() ;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof  ChannelsDataActivity ){
            mActivity = (ChannelsDataActivity) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_channel2, container, false ) ;

        mM2aButton  = rootView.findViewById(R.id.btn_m2a) ;
        mM2bButton  = rootView.findViewById(R.id.btn_m2b) ;
        mCycle1Chart= rootView.findViewById(R.id.chart_cycle2) ;
        mActiveTextView = rootView.findViewById(R.id.text_active_channel ) ;
        mMaxMaValueEditText = rootView.findViewById(R.id.et_max_ma_value);
        mMaxMBVaueEditText =rootView.findViewById(R.id.et_max_mb_value) ;
        mMADone  =rootView.findViewById(R.id.btn_ma_done);
        mMbDone  =rootView.findViewById(R.id.btn_mb_done ) ;

        mProgressTankImageView = rootView.findViewById(R.id.image_tank_progress) ;
        mTankLayout =            rootView.findViewById(R.id.layout_tank);

        mMADone.setOnClickListener(this );
        mMbDone.setOnClickListener(this );



        mM2aButton.setOnClickListener(this ) ;
        mM2bButton.setOnClickListener(this);


        checkCustomService();

        return  rootView  ;
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

    private void checkCustomService (){

        if (mActivity != null &&
                mActivity.get3ChannelsCustomService() != null &&
               mActivity.getChannelNumber() ==3 ){

            mCustomService3Channels = mActivity.get3ChannelsCustomService() ;

            if (mCustomService3Channels.getmLastCycleG2() != null ){
                cycleChannelG2 = mCustomService3Channels.getmLastCycleG2() ;
            }

            if (mCustomService3Channels.getSessionG2() != null){
                sessionG2 = mCustomService3Channels.getSessionG2() ;
                setupTank();
            }

            if (mCustomService3Channels.getmCurrentChannelVoltageOn() == 2)
                mActiveTextView.setVisibility(View.VISIBLE);
            else
                mActiveTextView.setVisibility(View.INVISIBLE);

            setupTank();
        }else if (mActivity != null &&
                mActivity.get2ChannelsCustomService() != null &&
                mActivity.getChannelNumber() ==2 ){

            mCustomService2Channels = mActivity.get2ChannelsCustomService() ;

            if (mCustomService2Channels.getmLastCycleG2() != null ){
                cycleChannelG2 = mCustomService2Channels.getmLastCycleG2() ;
            }

            if (mCustomService2Channels.getSessionG2() != null){
                sessionG2 = mCustomService2Channels.getSessionG2() ;
                setupTank();
            }

            if (mCustomService2Channels.getmCurrentChannelVoltageOn() == 2)
                mActiveTextView.setVisibility(View.VISIBLE);
            else
                mActiveTextView.setVisibility(View.INVISIBLE);


        }
    }

    private void setupTank (){

        float tankProgress = 0 ;
        if (sessionG2 != null  && sessionG2.getMaValuesAndSeconds()!= null && sessionG2.getMaValuesAndSeconds().size() >0){


            LinkedHashMap<Integer,Float> hashMap = sessionG2.getMaValuesAndSeconds() ;

            Iterator iterator = hashMap.entrySet().iterator() ;

            while (iterator.hasNext() ){

                Map.Entry<Integer,Float> entry = (Map.Entry<Integer, Float>) iterator.next();

                int time =entry.getKey() ;
                float ma = entry.getValue() ;

                Log.i(TAG ,"ma is : "+ma) ;
                Log.i(TAG , "time is : "+time ) ;
                float x = time *ma ;

                tankProgress += x ;
            }
            float tankProgressPercent = tankProgress/2000 ;


            Log.i(TAG ,"tankProgess" + tankProgress ) ;



            tankProgressPercent = Math.abs(tankProgressPercent) ;
            ViewGroup.LayoutParams layoutParams = mProgressTankImageView.getLayoutParams() ;
            layoutParams.height =(int)( mTankLayout.getHeight()*  tankProgressPercent) ;
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
                Log.i(TITLE , " onReceive :"+ action) ;
                mCurrentActiveChannel = intent.getIntExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL, 0);

                if (mCurrentActiveChannel ==2) {
                    mActiveTextView.setVisibility(View.VISIBLE );
                }else{
                    mActiveTextView.setVisibility(View.INVISIBLE );
                }

            }

            if (action.equals(BroadCastHandler.ACTION_CYCLECHANG2)) {
                Log.i(TITLE , " onReceive :"+ action) ;
                cycleChannelG2 = (CycleChannelG2) intent.getSerializableExtra(BroadCastHandler.EXTRA_CYCLE_CHANG2);
                drawCycle1Chart() ;

                Log.i(TAG , "cycle 2 added") ;
                Log.i(TAG  , "cycle 2 lds :"+cycleChannelG2.getLds()) ;
                Log.i(TAG , "cycle 2 lds0 :"+ cycleChannelG2.getLds0()) ;
                Log.i(TAG , "cycle 2 lgs :"+cycleChannelG2.getLgs() );
                Log.i(TAG , "cycle 2 lgs0 : "+ cycleChannelG2.getLgs0() ) ;
                Log.i(TAG , "cycle 2 ma : "+ cycleChannelG2.getM2a() ) ;
                Log.i(TAG , "cycle 2 mb : "+ cycleChannelG2.getM2b() ) ;

            }

            if (action.equals(BroadCastHandler.ACTION_SESSION2_CHANGED) && intent.hasExtra(BroadCastHandler.EXTRA_SESSIONG2)) {
                Log.i(TAG  , " onReceive : Session 2 Changed ") ;
                sessionG2 = (SessionG2) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG2);
                setupTank();
            }
        }
    };


    private void drawCycle1Chart (){

        Log.i(TITLE , "drawCycle1Chart") ;
        ArrayList<Entry> entries = new ArrayList<>();

        if (cycleChannelG2 != null && cycleChannelG2.getTimeAndCurrentDList() != null){

            for (ArrayList<Integer> timeAndCurrentList : cycleChannelG2.getTimeAndCurrentDList()){

                int time       = timeAndCurrentList.get(0) ;
                int currentOfD = timeAndCurrentList.get(1 );
                Log.i(TITLE , "time:" + time + " currentOfD :"+currentOfD);

                entries.add(new Entry(time,currentOfD ));
            }
        }else{
            Log.i(TITLE ,"Time and current is null ") ;
        }

        LineDataSet lineDataSet = new LineDataSet(entries , TITLE) ;
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        XAxis xAxis = mCycle1Chart.getXAxis() ;
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM );
        xAxis.setGranularity(1);

        LineData lineData = new LineData(lineDataSet ) ;
        // disable right y axis
        mCycle1Chart.getAxisRight().setEnabled(false);
        mCycle1Chart.getAxisLeft().setAxisMinimum(lineDataSet.getYMin());
        mCycle1Chart.getAxisRight().setAxisMinimum(lineDataSet.getXMin() );
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
        int id = v.getId() ;
        switch (id){
            case R.id.btn_ma_done :
                onMaDoneClicked () ;
                break;

            case R.id.btn_mb_done :
                onMbDoneClicked () ;
                break;

            case R.id.btn_m2a :
                onMaClicked ();
                break;


            case R.id.btn_m2b :
                onMbClicked () ;
                break;
        }
    }




    private void onMaDoneClicked() {
        if( mMaxMaValueEditText != null  &&
                mMaxMaValueEditText.getText()!= null &&
                !mMaxMaValueEditText.getText().toString().isEmpty()) {

            float maMaxValue = Float.valueOf(mMaxMaValueEditText.getText().toString()) ;

            // 3 channel mode
            if ( mCustomService3Channels != null
                    && mCustomService3Channels.getSessionG2() != null  &&
                  mActivity.getChannelNumber() ==3 ){
                mCustomService3Channels.getSessionG2().setMaxMavalue(maMaxValue );
                mMaxMaValueEditText.setText("");
                mMaxMaValueEditText.setHint(""+maMaxValue);

                // 2 Channel mode
            }else if ( mCustomService2Channels != null
                    && mCustomService2Channels.getSessionG2() != null  &&
                    mActivity.getChannelNumber() ==2 ){

                mCustomService2Channels.getSessionG2().setMaxMavalue(maMaxValue );
                mMaxMaValueEditText.setText("");
                mMaxMaValueEditText.setHint(""+maMaxValue);
            }
        }

    }

    private void onMbDoneClicked() {

        if (mMaxMBVaueEditText != null && mMaxMBVaueEditText.getText() != null &&! mMaxMBVaueEditText.getText().toString().isEmpty()){
            float mbMaxValue = Float.valueOf(mMaxMBVaueEditText.getText().toString() )  ;

            // 3 Channel mode
            if ( mCustomService3Channels != null &&
                    mCustomService3Channels.getSessionG2() != null &&
            mActivity.getChannelNumber() == 3 ){
                mCustomService3Channels.getSessionG2().setMaxMbValue(mbMaxValue );
                mMaxMBVaueEditText.setText("");
                mMaxMBVaueEditText.setHint(""+mbMaxValue);

                //2  Channel mode
            }else if ( mCustomService2Channels != null &&
                    mCustomService2Channels.getSessionG2() != null  &&
            mActivity.getChannelNumber()  == 2){


                mCustomService2Channels.getSessionG2().setMaxMbValue(mbMaxValue );
                mMaxMBVaueEditText.setText("");
                mMaxMBVaueEditText.setHint(""+mbMaxValue);
            }
        }
    }


    private void onMaClicked() {

        if (sessionG2 != null){

            Intent intent = new Intent(getActivity() , MaActivityChart.class);
            intent.putExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL , 2 ) ;
            intent.putExtra(BroadCastHandler.EXTRA_SESSIONG2, sessionG2 ) ;
            startActivity(intent);
        }
    }
    private void onMbClicked() {

        if (sessionG2 != null){

            Intent intent = new Intent(getActivity() , MbActivityChart.class);
            intent.putExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL , 2 ) ;
            intent.putExtra(BroadCastHandler.EXTRA_SESSIONG2, sessionG2 ) ;
            startActivity(intent);
        }
    }

}
