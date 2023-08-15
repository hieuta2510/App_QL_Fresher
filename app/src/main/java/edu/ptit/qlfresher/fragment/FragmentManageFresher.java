package edu.ptit.qlfresher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ptit.qlfresher.activity.AddActivity;
import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.adapter.AdapterFresher;
import edu.ptit.qlfresher.model.Fresher;

public class FragmentManageFresher extends Fragment {
    private SearchView searchView;
    private Spinner spProgramLang;
    private Button btSearch;
    private RecyclerView recycleView;
    private List<Fresher> mList;
    private AdapterFresher adapter;
    private DatabaseReference myRef;
    private TextView DBFresher;
    private FloatingActionButton add_fresh_button;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recycleView.setLayoutManager(manager);
        myRef = FirebaseDatabase
                .getInstance("https://qlyfresher-default-rtdb.firebaseio.com/")
                .getReference().child("fresherlist");
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fresherName = searchView.getQuery().toString().toLowerCase().trim();
                if(fresherName.isEmpty())
                {
                    getAllFresher();
                }
                else
                {
                    getFresherByName(fresherName);
                }
            }
        });

        spProgramLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String language = spProgramLang.getItemAtPosition(position).toString();
                if(!language.equals("All"))
                {
                    getByLanguage(language);
                    System.out.println(language);
                }
                else
                {
                    getAllFresher();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_fresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("act", 0);
                startActivity(intent);
            }
        });
    }

    private void initView(View view) {
        recycleView = view.findViewById(R.id.recySearch);
        add_fresh_button = view.findViewById(R.id.add_fresh_button);
        searchView= view.findViewById(R.id.search);
        btSearch = view.findViewById(R.id.btSearch);
        spProgramLang = view.findViewById(R.id.spLangSearch);
        DBFresher = view.findViewById(R.id.tvDBFresher);
        String[] arr = getResources().getStringArray(R.array.spProgramLang);
        String[] arr1 = new String[arr.length+1];
        arr1[0] = "All";
        for (int i = 1;i < arr1.length;i++)
        {
            arr1[i] = arr[i-1];
        }
        spProgramLang.setAdapter(new ArrayAdapter<String >(getContext(), R.layout.itemcate, arr1));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fresher, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllFresher();
    }

    private void getAllFresher()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Fresher fresher = dataSnapshot1.getValue(Fresher.class);
                    mList.add(fresher);
                }
                Collections.sort(mList);
                adapter = new AdapterFresher(getContext(), mList);
                recycleView.setAdapter(adapter);
                DBFresher.setText(getResources().getString(R.string.dashboard) + " " + Integer.toString(mList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getFresherByName(String key)
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Fresher fresher = dataSnapshot1.getValue(Fresher.class);
                    if(fresher.getName().toLowerCase().contains(key.toLowerCase()) || fresher.getEmail().contains(key.toLowerCase())) {
                        mList.add(fresher);
                    }
                }
                adapter = new AdapterFresher(getContext(), mList);
                recycleView.setAdapter(adapter);
                DBFresher.setText(getResources().getString(R.string.dashboard) + " " + Integer.toString(mList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getByLanguage(String key)
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Fresher fresher = dataSnapshot1.getValue(Fresher.class);
                    String language = fresher.getLanguage();
                    String[] langList = language.toUpperCase().split(",");
                    for(String i : langList)
                    {
                        if(i.trim().equals(key.toUpperCase())) {
                            mList.add(fresher);
                        }
                    }
                }
                adapter = new AdapterFresher(getContext(), mList);
                recycleView.setAdapter(adapter);
                DBFresher.setText(getResources().getString(R.string.dashboard) + " " + Integer.toString(mList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}