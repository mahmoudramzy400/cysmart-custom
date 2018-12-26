package com.cypress.cysmart.CustomApp.ui.maactivitychart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

public class MaActivityChart extends AppCompatActivity {


    private LineChart mLineChartLayout ;
    private int mActiveChannelNumber ;


    private SessionG1 sessionG1  ;
    private SessionG2 sessionG2  ;
    private SessionG3 sessionG3  ;


    private ArrayList<String> mLables;
    private ArrayList<Entry> mEntries ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_chart);
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

               HashMap<Long, Float> hashMap =sessionG1.getM1aValuesAndTime();

               for (Map.Entry<Long,Float> entry : hashMap.entrySet()  ){

                   Long  time = entry.getKey() ;
                   Float m1a    =entry.getValue() ;

                   // get hour in float
                   float floatHour = DateTime.getFloatHour(time );
                   // entry of time and ma for chart
                   Entry entry1 = new Entry(floatHour , m1a) ;
                   entries.add(entry1 );
                   // use human format of time as label for every value
                   String labelForxAxis = DateTime.getTime(time );
                   mLables.add(labelForxAxis ) ;


               }

           }
        }else if (mActiveChannelNumber == 2 ){

            if ( sessionG2 != null ) {

                HashMap<Long, Float> hashMap =sessionG2.getM2aValuesAndTime();

                for (Map.Entry<Long,Float> entry : hashMap.entrySet()  ){

                    Long  time = entry.getKey() ;
                    Float m2a    =entry.getValue() ;

                    // get hour in float
                    float floatHour = DateTime.getFloatHour(time );
                    // entry of time and ma for chart
                    Entry entry1 = new Entry(floatHour , m2a) ;
                    entries.add(entry1 );
                    // use human format of time as label for every value
                    String labelForxAxis = DateTime.getTime(time );
                    mLables.add(labelForxAxis ) ;


                }

            }

        }else if(mActiveChannelNumber == 3 ){

            if ( sessionG3 != null ) {

                HashMap<Long, Float> hashMap =sessionG3.getM3aValuesAndTime();

                for (Map.Entry<Long,Float> entry : hashMap.entrySet()  ){

                    Long  time = entry.getKey() ;
                    Float m3a    =entry.getValue() ;

                    // get hour in float
                    float floatHour = DateTime.getFloatHour(time );
                    // entry of time and ma for chart
                    Entry entry1 = new Entry(floatHour , m3a) ;
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

            mLineChartLayout.getAxisRight().setEnabled(false );


            LineData lineData  = new LineData( lineDataSet );

            Description description = new Description() ;
            if (mActiveChannelNumber == 1){
                description.setText(getString(R.string.title_m1a_values));

            }else if (mActiveChannelNumber == 2) {
                description.setText(getString(R.string.title_m2a_values));

            }else if (mActiveChannelNumber == 3 ){

                description.setText(getString(R.string.title_m3a_values));
            }

            mLineChartLayout.setDescription(description);
            mLineChartLayout.setData(lineData );
            mLineChartLayout.getAxisLeft().setAxisMinimum(lineDataSet.getYMin());
            mLineChartLayout.getAxisRight().setAxisMinimum(lineDataSet.getXMin() );
            mLineChartLayout.animateX(2000);
            mLineChartLayout.invalidate();
        }

    }
}


/*
 ArrayList<Entry> entries = new ArrayList<>() ;
        entries.add(new Entry(0,1000))  ;
        entries.add(new Entry(1,1500 ));
        entries.add(new Entry(2,3000))  ;
        entries.add(new Entry(3,2000))  ;
        entries.add(new Entry(3,1000))  ;
        entries.add(new Entry(4, 4000)) ;
        entries.add(new Entry(5, 6000)) ;
        entries.add(new Entry(6, 7000)) ;
        entries.add(new Entry(7, 8000)) ;

        LineDataSet lineDataSet = new LineDataSet(entries , "label dataset ") ;
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        XAxis xAxis = mLineChartLayout.getXAxis()  ;

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

     //   xAxis.setGranularity(1f);
       // xAxis.setValueFormatter(formatter);


        // Controlling right side of y axis
        YAxis yAxisRight = mLineChartLayout.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = mLineChartLayout.getAxisLeft();
        yAxisLeft.setGranularity(1f);


        // Setting Data
        LineData data = new LineData(lineDataSet);

        mLineChartLayout.setData(data);
        mLineChartLayout.animateX(2500);
        //refresh
        mLineChartLayout.invalidate();
 */