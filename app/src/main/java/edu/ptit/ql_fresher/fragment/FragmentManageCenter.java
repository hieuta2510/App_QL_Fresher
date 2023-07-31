package edu.ptit.ql_fresher.fragment;

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

import edu.ptit.ql_fresher.AddActivity;
import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.adapter.AdapterCenter;
import edu.ptit.ql_fresher.database.SQLiteHelper;
import edu.ptit.ql_fresher.model.Center;

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