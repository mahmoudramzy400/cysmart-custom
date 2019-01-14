package com.cypress.btion.CustomApp.ui.mbactivitychart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cypress.btion.CustomApp.data.models.SessionG1;
import com.cypress.btion.CustomApp.data.models.SessionG2;
import com.cypress.btion.CustomApp.data.models.SessionG3;
import com.cypress.btion.CustomApp.utils.BroadCastHandler;
import com.cypress.btion.CustomApp.utils.DateTime;
import com.cypress.btion.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MbActivityChart extends AppCompatActivity {

    private static final String TAG = "MbActivity";

    private LineChart mLineChartLayout;
    private int mActiveChannelNumber;


    private SessionG1 sessionG1;
    private SessionG2 sessionG2;
    private SessionG3 sessionG3;


    private ArrayList<String> mLables;
    private int indexLabel = -1;
    private ArrayList<Entry> mEntries;
    private Entry maxEntry;
    private Entry minEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mb);

        mLineChartLayout = findViewById(R.id.chart);

        checkIntent();

        mEntries = getEntries();

        startDrawMb();
    }

    private void checkIntent() {
        Intent intent = getIntent();

        if (intent != null) {

            mActiveChannelNumber = intent.getIntExtra(BroadCastHandler.EXTRA_ACTIVATE_CHANNEL, 0);
        }

        switch (mActiveChannelNumber) {
            case 1:
                sessionG1 = (SessionG1) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG1);
                break;

            case 2:
                sessionG2 = (SessionG2) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG2);
                break;

            case 3:
                sessionG3 = (SessionG3) intent.getSerializableExtra(BroadCastHandler.EXTRA_SESSIONG3);
                break;
        }
    }

    private ArrayList<Entry> getEntries() {
        ArrayList<Entry> entries = new ArrayList<>();

        mLables = new ArrayList<>();
        float maxMB = 0;
        float minMB = 0;
        maxEntry =null;
        minEntry = null;
        if (mActiveChannelNumber == 1) {

            if (sessionG1 != null) {

                LinkedHashMap<Long, Float> hashMap = sessionG1.getM1bValuesAndTime();

                Iterator it = hashMap.entrySet().iterator();
                while (it.hasNext()) {

                    Map.Entry<Long, Float> entry = (Map.Entry<Long, Float>) it.next();
                    Long time = entry.getKey();
                    Float m1b = entry.getValue();

                    // get hour in float
                    float floatHour = DateTime.getFloatHour(time);
                    Log.i(TAG, "time :" + floatHour + " m1b :" + m1b);

                    // entry of time and ma for chart
                    Entry entry1 = new Entry(floatHour, m1b);

                    if (maxMB == 0 &&  minMB == 0){
                        maxMB =m1b ;
                        minMB = m1b ;
                    }
                    if (m1b > maxMB) {
                        maxEntry = entry1;
                        maxMB = m1b ;
                    }

                    if (m1b < minMB) {
                        minEntry = entry1;
                        minMB =  m1b;
                    }
                    entries.add(entry1);
                    // use human format of time as label for every value
                    String labelForxAxis = DateTime.getTime(time);
                    mLables.add(labelForxAxis);

                }


            }
        } else if (mActiveChannelNumber == 2) {

            if (sessionG2 != null) {

                LinkedHashMap<Long, Float> hashMap = sessionG2.getM2bValuesAndTime();

                Iterator it = hashMap.entrySet().iterator();
                while (it.hasNext()) {

                    Map.Entry<Long, Float> entry = (Map.Entry<Long, Float>) it.next();
                    Long time = entry.getKey();
                    Float m2b = entry.getValue();

                    // get hour in float
                    float floatHour = DateTime.getFloatHour(time);
                    // entry of time and ma for chart
                    Entry entry1 = new Entry(floatHour, m2b);
                    if (maxMB == 0 &&  minMB == 0){
                        maxMB =m2b ;
                        minMB = m2b ;
                    }
                    if (m2b > maxMB) {

                        maxEntry = entry1;
                        maxMB = m2b ;
                    }

                    if (m2b < minMB) {
                        minEntry = entry1;
                        minMB = m2b ;
                    }
                    Log.i(TAG, "time :" + floatHour + " m2b:" + m2b);
                    entries.add(entry1);
                    // use human format of time as label for every value
                    String labelForxAxis = DateTime.getTime(time);
                    mLables.add(labelForxAxis);
                }


            }

        } else if (mActiveChannelNumber == 3) {

            if (sessionG3 != null) {

                LinkedHashMap<Long, Float> hashMap = sessionG3.getM3bValuesAndTime();

                Iterator it = hashMap.entrySet().iterator();
                while (it.hasNext()) {

                    Map.Entry<Long, Float> entry = (Map.Entry<Long, Float>) it.next();

                    Long time = entry.getKey();
                    Float m3b = entry.getValue();

                    // get hour in float
                    float floatHour = DateTime.getFloatHour(time);
                    Log.i(TAG, "time :" + floatHour + " m3b :" + m3b);
                    // entry of time and ma for chart
                    Entry entry1 = new Entry(floatHour, m3b);
                    if (maxMB == 0 &&  minMB == 0){
                        maxMB =m3b ;
                        minMB = m3b ;
                    }
                    if (m3b > maxMB) {

                        maxMB =m3b ;
                        maxEntry = entry1;
                    }

                    if (m3b < minMB) {
                        minEntry = entry1;
                        minMB = m3b ;
                    }
                    entries.add(entry1);
                    // use human format of time as label for every value
                    String labelForxAxis = DateTime.getTime(time);
                    mLables.add(labelForxAxis);
                }


            }
        }

        return entries;
    }

    private void startDrawMb() {

        if (mEntries != null && mEntries.size() > 0) {

            if (maxEntry != null ){
                mEntries.get(mEntries.indexOf(maxEntry)) .setIcon(getResources().getDrawable(R.drawable.ic_plus));
            }

            if (minEntry != null ){
                mEntries.get(mEntries.indexOf(minEntry)).setIcon(getResources().getDrawable(R.drawable.ic_negative));

            }

            LineDataSet lineDataSet = new LineDataSet(mEntries, getString(R.string.title_time));
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            XAxis xAxis = mLineChartLayout.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);


           /*
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {


                    return  value +"";
                }
            });

            */
            mLineChartLayout.getAxisRight().setEnabled(false);


            LineData lineData = new LineData(lineDataSet);

            Description description = new Description();
            if (mActiveChannelNumber == 1) {
                description.setText(getString(R.string.title_m1b_values));

            } else if (mActiveChannelNumber == 2) {
                description.setText(getString(R.string.title_m2b_values));

            } else if (mActiveChannelNumber == 3) {

                description.setText(getString(R.string.title_m3b_values));
            }

            mLineChartLayout.setDescription(description);

            mLineChartLayout.getAxisLeft().setAxisMinimum(lineDataSet.getYMin());
            mLineChartLayout.getAxisRight().setAxisMinimum(lineDataSet.getXMin());
            mLineChartLayout.setData(lineData);
            mLineChartLayout.animateX(2000);
            //refresh
            mLineChartLayout.invalidate();
        }

    }
}
