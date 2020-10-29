package com.example.mpandroidchart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
//Tutorial followed on https://www.youtube.com/watch?v=C0O9u0jd6nQ&list=PLFh8wpMiEi89LcBupeftmAcgDKCeC24bJ&index=17
public class MainActivity extends AppCompatActivity {
    private static final String TAG ="Main Activity";

    EditText xValue, yValue;
    Button btnAdd;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    LineChart chart;

    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;
    private ArrayList<Entry> dataVals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xValue = findViewById(R.id.addValueX);
        yValue = findViewById(R.id.addValueY);
        btnAdd = findViewById(R.id.btnAdd);
        chart =findViewById(R.id.chart);


        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Chart Values");
        insertData();

    }

    //Inserts data to database
    private void insertData() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = myRef.push().getKey();
                int x = Integer.parseInt(xValue.getText().toString());
                int y = Integer.parseInt(yValue.getText().toString());

                DataPoint dataPoint = new DataPoint(x, y);
                //Adds data to database
                myRef.child(id).setValue(dataPoint);
                retriveData();
            }
        });
    }

    private void retriveData() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Entry> dataVals = new ArrayList<Entry>();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()) {
                        //Gets data from database
                        String id = myDataSnapshot.getKey();
                        Log.d(TAG, "Id inside getData: "+id);
                        Log.d(TAG, "Value inside getData: "+myDataSnapshot.getValue().toString());
                        Log.d(TAG, "DS inside getData: "+myDataSnapshot.child(id));

                        DataPoint dataPoint = myDataSnapshot.getValue(DataPoint.class);
                        dataVals.add(new Entry(dataPoint.getxValue(), dataPoint.getyValue()));
                    }
                    showChart(dataVals);
                } else {
                    chart.clear();
                    chart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showChart(ArrayList<Entry> dataVals) {
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("Heart measurements");

        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
/*
        //Formating
        chart.setBackgroundColor(Color.DKGRAY);
        chart.setNoDataText("No Data");

        //chart.setDrawGridBackground(true);
        chart.setDrawBorders(true);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setTextSize(10);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisLeft().setTextSize(10);
        chart.getAxisRight().setEnabled(false);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setValueTextColor(Color.WHITE);
        lineDataSet.setValueTextSize(5);
        lineDataSet.setDrawCircles(false);
        //Disabled legend (Line discriptors)
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        //Chart Description

        Description description = new Description();
        description.setText("Heart Measurements");
        description.setTextColor(Color.WHITE);
        chart.setDescription(description);

        //Set pitch zoom
        chart.setPinchZoom(true);
        //Set animations
        chart.animateX(500);
*/
        chart.clear();
        chart.setData(lineData);
        chart.invalidate();
    }
}