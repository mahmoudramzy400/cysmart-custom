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

import com.cypress.btion.CustomApp.data.models.CycleChannelG3;
import com.cypress.btion.CustomApp.data.models.SessionG3;
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
public class Channel3Fragment extends Fragment implements View.OnClickListener {


    public static final String TITLE  = "Channel G3";
    private static final String TAG  = "Channel3Fragment" ;


    private SessionG3 sessionG3;
    private CycleChannelG3 cycleChannelG3;
    private int mCurrentActiveChannel = 0;

    private Button mM3aButton ;
    private Button mM3bButton ;
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
    public static Fragment newInstance(){
        return  new Channel3Fragment () ;
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
        View rootView =  inflater.inflate(R.layout.fragment_channel3, container, false);

        mM3aButton  = rootView.findViewById(R.id.btn_m3a) ;
        mM3bButton  = rootView.findViewById(R.id.btn_m3b) ;
        mCycle1Chart= rootView.findViewById(R.id.chart_cycle3) ;
        mActiveTextView = rootView.findViewById(R.id.text_active_channel ) ;
        mMaxMaValueEditText = rootView.findViewById(R.id.et_max_ma_value);
        mMaxMBVaueEditText =rootView.findViewById(R.id.et_max_mb_value) ;
        mMADone  =rootView.findViewById(R.id.btn_ma_done);
        mMbDone  =rootView.findViewById(R.id.btn_mb_done ) ;
        mProgressTankImageView = rootView.findViewById(R.id.image_tank_progress) ;
        mTankLayout =            rootView.findViewById(R.id.layout_tank);

        mMADone.setOnClickListener(this );
        mMbDone.setOnClickListener(this );

        mM3aButton.setOnClickListener(this );
        mM3bButton.setOnClickListener(this );

        checkCustomService();

        return rootView ;
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
        if (mActivity != null && mActivity.get3ChannelsCustomService() != null ){

            mCustomService3Channels = mActivity.get3ChannelsCustomService() ;

            if (mCustomService3Channels.getmLastCycleG3() != null ){
                cycleChannelG3 = mCustomService3Channels.getmLastCycleG3() ;
            }

            if (mCustomService3Channels.getSessionG3() != null){
                sessionG3 = mCustomService3Channels.getSessionG3() ;
                setupTank();
            }

            if (mCustomService3Channels.getmCurrentChannelVoltageOn() == 3)
                mActiveTextView.setVisibility(View.VISIBLE);
            else
                mActiveTextView.setVisibility(View.INVISIBLE);


        }
    }

    private void setupTank (){

        float tankProgress = 0 ;
        if (sessionG3 != null  && sessionG3.getMaValuesAndSeconds()!= null && sessionG3.getMaValuesAndSeconds().size() >0){


            LinkedHashMap<Integer,Float> hashMap = sessionG3.getMaValuesAndSeconds() ;

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
            Log.i(TITLE , " onReceive :"+ action) ;

            if (action.equals(BroadCastHandler.ACTION_ACTIVE_CHANNEL_CHANGED)) {

                mCurrentActiveChannel = intent.getIntExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL, 0);

                if (mCurrentActiveChannel ==3) {
                    mActiveTextView.setVisibility(View.VISIBLE );
                }else{
                    mActiveTextView.setVisibility(View.INVISIBLE );
                }

            }

            if (action.equals(BroadCastHandler.ACTION_CYCLECHANG3)) {

                cycleChannelG3 = (CycleChannelG3) intent.getSerializableExtra(BroadCastHandler.EXTRA_CYCLE_CHANG3);
                drawCycle1Chart() ;

                Log.i(TAG , "cycle 3 added") ;
                Log.i(TAG  , "cycle 3 lds :"+cycleChannelG3.getLds()) ;
                Log.i(TAG , "cycle 3 lds0 :"+ cycleChannelG3.getLds0()) ;
                Log.i(TAG , "cycle 3 lgs :"+cycleChannelG3.getLgs() );
                Log.i(TAG , "cycle 3 lgs0 : "+ cycleChannelG3.getLgs0() ) ;
                Log.i(TAG , "cycle 3 ma : "+ cycleChannelG3.getM3a() ) ;
                Log.i(TAG , "cycle 3 mb : "+ cycleChannelG3.getM3b() ) ;


            }

            if (action.equals(BroadCastHandler.ACTION_SESSION3_CHANGED) && intent.hasExtra(BroadCastHandler.EXTRA_SESSIONG3)) {
                Log.i(TAG ,"onReceive : Session 3 Changed ") ;
                sessionG3 = (SessionG3) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG3);
                setupTank();


            }
        }
    };


    private void drawCycle1Chart (){

        Log.i(TITLE , "drawCycle1Chart") ;
        ArrayList<Entry> entries = new ArrayList<>();

        if (cycleChannelG3 != null && cycleChannelG3.getTimeAndCurrentDList() != null){

            for (ArrayList<Integer> timeAndCurrentList : cycleChannelG3.getTimeAndCurrentDList()){

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

        switch (id ){

            case R.id.btn_ma_done :
                onMaDoneClicked () ;
                break;

            case R.id.btn_mb_done :
                onMbDoneClicked () ;
                break ;

            case R.id.btn_m3a :
                onMaClicked ();
                break;

            case R.id.btn_m3b :
                onMbClicked ();
                break;

        }
    }



    private void onMbDoneClicked() {

        if (mMaxMBVaueEditText != null && mMaxMBVaueEditText.getText() != null && !mMaxMBVaueEditText.getText().toString().isEmpty()){
            float mbMaxValue = Float.valueOf(mMaxMBVaueEditText.getText().toString() )  ;

            if ( mCustomService3Channels != null && mCustomService3Channels.getSessionG3() != null  ){
                mCustomService3Channels.getSessionG3().setMaxMbValue(mbMaxValue );
                mMaxMBVaueEditText.setText("");
                mMaxMBVaueEditText.setHint(""+mbMaxValue);
            }
        }
    }

    private void onMaDoneClicked() {
        if( mMaxMaValueEditText != null  && mMaxMaValueEditText.getText()!= null && !mMaxMaValueEditText.getText().toString().isEmpty()) {
            float maMaxValue = Float.valueOf(mMaxMaValueEditText.getText().toString()) ;

            if ( mCustomService3Channels != null && mCustomService3Channels.getSessionG3() != null  ){
                mCustomService3Channels.getSessionG3().setMaxMavalue(maMaxValue );
                mMaxMaValueEditText.setText("");
                mMaxMaValueEditText.setHint(""+maMaxValue);
            }
        }
    }

    private void onMbClicked() {

    }

    private void onMaClicked() {
        if (sessionG3 != null){

            Intent intent = new Intent(getActivity() , MaActivityChart.class);
            intent.putExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL , 3 ) ;
            intent.putExtra(BroadCastHandler.EXTRA_SESSIONG3, sessionG3 ) ;
            startActivity(intent);
        }
    }
}
