package edu.ptit.qlfresher.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.Center;
import edu.ptit.qlfresher.model.Fresher;
import edu.ptit.qlfresher.model.StatisticsCallback;

public class FragmentDashboard extends Fragment {
    private BarChart barChart;
    private PieChart pieChart;
    private SQLiteHelper db;
    private List<Center> mListCenter;
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        displayBarChart();
        displayPieChart();
    }

    private void init(View view) {
        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        myRef = FirebaseDatabase
                .getInstance("https://qlyfresher-default-rtdb.firebaseio.com/")
                .getReference().child("fresherlist");
    }

    //dem fresher theo score
    private void statisticsFresherByScore(StatisticsCallback callback) {
        int[] aTotalFoS = new int[]{0, 0, 0, 0, 0};
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Fresher> mListFresher = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Fresher fresher = dataSnapshot1.getValue(Fresher.class);
                    mListFresher.add(fresher);
                }
                for (Fresher fresher : mListFresher) {
                    float score = Float.parseFloat(fresher.getScore());
                    if (score >= 8.5) {
                        aTotalFoS[0]++;
                    } else if (score >= 7.0) {
                        aTotalFoS[1]++;
                    } else if (score >= 5.5) {
                        aTotalFoS[2]++;
                    } else if (score >= 4.0) {
                        aTotalFoS[3]++;
                    } else {
                        aTotalFoS[4]++;
                    }
                }
                callback.onStatisticsUpdated(aTotalFoS); // Gọi callback khi dữ liệu đã sẵn sàng
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getListCenter()
    {
        db = new SQLiteHelper(getActivity());
        mListCenter = db.getAllCenter();
        Collections.sort(mListCenter);
    }

    // hien thi thong ke fresher theo diem
    private void displayBarChart()
    {
        statisticsFresherByScore(new StatisticsCallback() {
            @Override
            public void onStatisticsUpdated(int[] aTotalFoS) {
                barChart.getAxisRight().setDrawLabels(false);
                List<String> labels = new ArrayList<>();
                int[] listStatisticFresher = aTotalFoS;
                ArrayList<BarEntry> listBarEntry = new ArrayList<>();
                listBarEntry.clear();
                listBarEntry.add(new BarEntry(0f, listStatisticFresher[0]));
                listBarEntry.add(new BarEntry(1f, listStatisticFresher[1]));
                listBarEntry.add(new BarEntry(2f, listStatisticFresher[2]));
                listBarEntry.add(new BarEntry(3f, listStatisticFresher[3]));
                listBarEntry.add(new BarEntry(4f, listStatisticFresher[4]));
                labels.clear();
                labels.add("A, A+");
                labels.add("B, B+");
                labels.add("C, C+");
                labels.add("D, D+");
                labels.add("F");

                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setAxisMaximum(10f);
                yAxis.setAxisMinimum(0f);
                yAxis.setAxisLineWidth(2f);
                yAxis.setAxisLineColor(Color.BLACK);
                yAxis.setLabelCount(10);

                BarDataSet barDataSet = new BarDataSet(listBarEntry, "");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueFormatter(new DefaultValueFormatter(0));
                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);
                Description description = new Description();
                description.setText("Statistics of students by centers");
                barChart.setDescription(description);
                barChart.invalidate();
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                barChart.getXAxis().setGranularity(1f);
                barChart.getXAxis().setGranularityEnabled(true);
            }
        });
    }

    // Thong ke fresher theo center
    private void displayPieChart()
    {
        mListCenter = new ArrayList<>();
        getListCenter();
        List<PieEntry> listPieEntry = new ArrayList<>();
        for(Center center : mListCenter)
        {
            listPieEntry.add(new PieEntry(center.getTotalFresher(), center.getAcronym()));
        }
        PieDataSet dataSet = new PieDataSet(listPieEntry, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueFormatter(new DefaultValueFormatter(0));
        dataSet.setValueTextSize(12f);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setCenterText("Fresher");
//        pieChart.getDescription().setEnabled(false);
        Description description = new Description();
        description.setText("Statistics of students by scores");
        pieChart.setDescription(description);
        pieChart.animate();
    }
}
