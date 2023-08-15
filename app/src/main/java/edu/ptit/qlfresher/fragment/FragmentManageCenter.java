package edu.ptit.qlfresher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.ptit.qlfresher.activity.AddActivity;
import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.adapter.AdapterCenter;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.Center;
import edu.ptit.qlfresher.model.Fresher;

public class FragmentManageCenter extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener{
    private RecyclerView recycleView;
    private AdapterCenter adapter;
    private List<Center> mList;
    private TextView DBCenter;
    private FloatingActionButton add_center_button;
    private SQLiteHelper db;
    private View mView;
    private Button btSearchCenter;
    private SearchView searchCenter;

    public FragmentManageCenter()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_center, container, false);
        initView(mView);
        db = new SQLiteHelper(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recycleView.setLayoutManager(manager);
        getAllCenter();
        add_center_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("act", 1);
                startActivity(intent);
            }
        });
        searchCenter.setOnQueryTextListener(FragmentManageCenter.this);
        btSearchCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String centerName = searchCenter.getQuery().toString().toLowerCase().trim();
                if(centerName.isEmpty())
                {
                    getAllCenter();
                }
                else
                {
                    getCenterByName(centerName);
                }
            }
        });
        return mView;
    }

    private void getAllCenter()
    {
        mList = new ArrayList<>();
        mList = db.getAllCenter();
        adapter = new AdapterCenter(getActivity(), mList);
        recycleView.setAdapter(adapter);
        DBResultList(mList.size());
    }

    private void getCenterByName(String name)
    {
        mList = new ArrayList<>();
        List<Center> list = new ArrayList<>();
        mList = db.getAllCenter();
        for (Center i : mList)
        {
            if (i.getName().toLowerCase().contains(name) || i.getAcronym().toLowerCase().contains(name))
            {
                list.add(i);
            }
        }
        adapter = new AdapterCenter(getActivity(), list);
        recycleView.setAdapter(adapter);
        DBResultList(list.size());
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
        btSearchCenter = view.findViewById(R.id.btSearchCenter);
        searchCenter = view.findViewById(R.id.searchCenter);
    }

    private void filterCenter(String email) {
        getAllCenter();
        List<Center> centerList = new ArrayList<>();
        for (Center i : mList)
        {
            if (i.getName().toLowerCase().contains(email.toLowerCase()) || i.getAcronym().toLowerCase().contains(email.toLowerCase()))
            {
                centerList.add(i);
            }
        }

        if (centerList.isEmpty())
        {
            Toast.makeText(getActivity(), "Khong co ket qua", Toast.LENGTH_SHORT).show();
        }
        else
        {
            adapter.filterList(centerList);
            DBResultList(centerList.size());
        }
    }

    private void DBResultList(int listSize)
    {
        DBCenter.setText(getResources().getString(R.string.dbCenter) + " " + Integer.toString(listSize));
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String email) {
        if(!email.trim().isEmpty())
        {
            filterCenter(email.toLowerCase());
        }
        return false;
    }
}