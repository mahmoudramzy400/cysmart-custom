package com.cypress.cysmart.CustomApp.ui.mbactivitychart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cypress.cysmart.CustomApp.data.models.SessionG1;
import com.cypress.cysmart.CustomApp.data.models.SessionG2;
import com.cypress.cysmart.CustomApp.data.models.SessionG3;
import com.cypress.cysmart.CustomApp.utils.BroadCastHandler;
import com.cypress.cysmart.CustomApp.utils.DateTime;
import com.cypress.cysmart.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MbActivityChart extends AppCompatActivity {

    private static final String TAG = "MbActivity" ;

    private LineChart mLineChartLayout ;
    private int mActiveChannelNumber ;


    private SessionG1 sessionG1  ;
    private SessionG2 sessionG2  ;
    private SessionG3 sessionG3  ;


    private ArrayList<String> mLables;
    private int indexLabel= -1 ;
    private ArrayList<Entry> mEntries ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mb);

        mLineChartLayout =findViewById(R.id.chart) ;

        checkIntent();

        mEntries = getEntries() ;

        startDrawMa();
    }

    private void checkIntent (){
        Intent intent = getIntent() ;

        if (intent != null  ){

            mActiveChannelNumber =  intent.getIntExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL, 0) ;
        }

        switch (mActiveChannelNumber ){
            case 1 :
                sessionG1 = (SessionG1) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG1);
                break;

            case 2 :
                sessionG2 = (SessionG2) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG2 );
                break;

            case 3 :
                sessionG3 = (SessionG3) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG3 );
                break;
        }
    }

    private ArrayList<Entry> getEntries(){
        ArrayList<Entry> entries = new ArrayList<>( ) ;

        mLables = new ArrayList<>( );

        if (mActiveChannelNumber == 1) {

            if ( sessionG1 != null ) {

                HashMap<Long, Float> hashMap =sessionG1.getM1bValuesAndTime();

                for (Map.Entry<Long,Float> entry : hashMap.entrySet()  ){

                    Long  time = entry.getKey() ;
                    Float m1b    =entry.getValue() ;

                    // get hour in float
                    float floatHour = DateTime.getFloatHour(time );
                    Log.i(TAG ,"time :" + floatHour + " m1b :"+m1b) ;

                    // entry of time and ma for chart
                    Entry entry1 = new Entry(floatHour , m1b) ;
                    entries.add(entry1 );
                    // use human format of time as label for every value
                    String labelForxAxis = DateTime.getTime(time );
                    mLables.add(labelForxAxis ) ;


                }

            }
        }else if (mActiveChannelNumber == 2 ){

            if ( sessionG2 != null ) {

                HashMap<Long, Float> hashMap =sessionG2.getM2bValuesAndTime();

                for (Map.Entry<Long,Float> entry : hashMap.entrySet()  ){

                    Long  time = entry.getKey() ;
                    Float m2b    =entry.getValue() ;

                    // get hour in float
                    float floatHour = DateTime.getFloatHour(time );
                    // entry of time and ma for chart
                    Entry entry1 = new Entry(floatHour , m2b) ;
                    Log.i(TAG ,"time :" + floatHour + " m2b:"+m2b) ;
                    entries.add(entry1 );
                    // use human format of time as label for every value
                    String labelForxAxis = DateTime.getTime(time );
                    mLables.add(labelForxAxis ) ;


                }

            }

        }else if(mActiveChannelNumber == 3 ){

            if ( sessionG3 != null ) {

                HashMap<Long, Float> hashMap =sessionG3.getM3bValuesAndTime();

                for (Map.Entry<Long,Float> entry : hashMap.entrySet()  ){

                    Long  time = entry.getKey() ;
                    Float m3b    =entry.getValue() ;

                    // get hour in float
                    float floatHour = DateTime.getFloatHour(time );
                    Log.i(TAG ,"time :" + floatHour + " m3b :"+m3b) ;
                    // entry of time and ma for chart
                    Entry entry1 = new Entry(floatHour , m3b) ;
                    entries.add(entry1 );
                    // use human format of time as label for every value
                    String labelForxAxis = DateTime.getTime(time );
                    mLables.add(labelForxAxis ) ;


                }

            }
        }

        return  entries ;
    }

    private void startDrawMa (){

        if (mEntries != null  && mEntries.size() > 0 ){

            LineDataSet lineDataSet = new LineDataSet(mEntries , getString(R.string.title_time)) ;
            lineDataSet.setDrawFilled(true );
            lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            XAxis xAxis = mLineChartLayout.getXAxis() ;
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

           /*
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {


                    return  value +"";
                }
            });

            */
            mLineChartLayout.getAxisRight().setEnabled(false );


            LineData lineData  = new LineData( lineDataSet );

            Description description = new Description() ;
            if (mActiveChannelNumber == 1){
                description.setText(getString(R.string.title_m1b_values));

            }else if (mActiveChannelNumber == 2) {
                description.setText(getString(R.string.title_m2b_values));

            }else if (mActiveChannelNumber == 3 ){

                description.setText(getString(R.string.title_m3b_values));
            }

            mLineChartLayout.setDescription(description);
            mLineChartLayout.setData(lineData );
            mLineChartLayout.getAxisLeft().setAxisMinimum(lineDataSet.getYMin());
            mLineChartLayout.getAxisRight().setAxisMinimum(lineDataSet.getXMin() );
            mLineChartLayout.animateX(2000);
            //refresh
            mLineChartLayout.invalidate();
        }

    }
}
