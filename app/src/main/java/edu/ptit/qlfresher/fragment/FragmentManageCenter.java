package edu.ptit.qlfresher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.ptit.qlfresher.activity.AddActivity;
import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.adapter.AdapterCenter;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.Center;

public class FragmentManageCenter extends Fragment {
    private RecyclerView recycleView;
    private AdapterCenter adapter;
    private List<Center> mList;
    private TextView DBCenter;
    private FloatingActionButton add_center_button;
    private SQLiteHelper db;
    private View mView;

    public FragmentManageCenter()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_center, container, false);
        initView(mView);
        db = new SQLiteHelper(getActivity());
        mList = db.getAllCenter();
        adapter = new AdapterCenter(getActivity(), mList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recycleView.setLayoutManager(manager);
        recycleView.setAdapter(adapter);
        DBCenter.setText(getResources().getString(R.string.dbCenter) + " " + Integer.toString(mList.size()));
        add_center_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("act", 1);
                startActivity(intent);
            }
        });
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Center> list = db.getAllCenter();
        adapter.setList(list);
    }

    private void initView(View view) {
        recycleView = view.findViewById(R.id.recycleCenter);
        add_center_button = view.findViewById(R.id.add_center_button);
        DBCenter = view.findViewById(R.id.tvDBCenter);
    }
}