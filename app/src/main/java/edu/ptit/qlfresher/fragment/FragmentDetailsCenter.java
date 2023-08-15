package edu.ptit.qlfresher.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.adapter.AdapterFresher;
import edu.ptit.qlfresher.database.SQLiteHelper;
import edu.ptit.qlfresher.model.Center;
import edu.ptit.qlfresher.model.Fresher;

public class FragmentDetailsCenter extends Fragment implements androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private View mView;
    private TextView tvAcronym, tvAdd, tvName, tvDB;
    private RecyclerView recycleFresher;
    private AdapterFresher adapter;
    private List<Fresher> mList;
    private DatabaseReference myRef;
    private String acronym = "";
    private androidx.appcompat.widget.SearchView searchView;
    private Button btAddFresher;
    private Fresher fresher = new Fresher();
    public FragmentDetailsCenter() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_details_center, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        Bundle bundle = getArguments();
        Center center = (Center) bundle.getSerializable("center");
        acronym = center.getAcronym();
        tvAcronym.setText("Acronym: "+center.getAcronym());
        tvAdd.setText("Add: " + center.getAddress());
        tvName.setText(center.getName());
        tvDB.setText(getResources().getString(R.string.dashboard) + " " + String.valueOf(center.getTotalFresher()));
        //recycle view
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recycleFresher.setLayoutManager(manager);
        myRef = FirebaseDatabase
                .getInstance("https://qlyfresher-default-rtdb.firebaseio.com/")
                .getReference().child("fresherlist");
        searchView.setOnQueryTextListener(FragmentDetailsCenter.this);

        btAddFresher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFresherToCenter(searchView.getQuery().toString().trim().toLowerCase());
            }
        });
    }

    private void init(View view)
    {
        tvAcronym = view.findViewById(R.id.tvAcronymDetails);
        tvAdd = view.findViewById(R.id.tvAddDetails);
        tvName = view.findViewById(R.id.tvNameDetails);
        tvDB = view.findViewById(R.id.tvDBDetails);
        recycleFresher = view.findViewById(R.id.recycleFresherDetails);
        searchView = view.findViewById(R.id.searchFresherDetails);
        btAddFresher = view.findViewById(R.id.btAddDetails);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFresherOfCenter();
    }

    private void getFresherOfCenter() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList = new ArrayList<>();
                // set code to retrive data and replace layout
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Fresher fresher = dataSnapshot1.getValue(Fresher.class);
                    if (fresher.getCenter().toUpperCase().equals(acronym))
                    {
                        mList.add(fresher);
                    }
                }
                Collections.sort(mList);
                adapter = new AdapterFresher(getContext(), mList);
                recycleFresher.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterFresher(String email) {
        getAllFresher();
        List<Fresher> filterlist = new ArrayList<>();
        for (Fresher fresher2 : mList)
        {
            if (fresher2.getEmail().toLowerCase().contains(email.toLowerCase()))
            {
                filterlist.add(fresher2);
            }
        }

        if (filterlist.isEmpty())
        {
            Toast.makeText(getActivity(), "Khong co ket qua", Toast.LENGTH_SHORT).show();
        }
        else
        {
            adapter.filterList(filterlist);
            if (filterlist.size() == 1)
            {
                fresher = filterlist.get(0);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String email) {
        if(!email.trim().isEmpty()) {
            filterFresher(email.toLowerCase());
        } else
        {
            getFresherOfCenter();
        }
        return false;
    }


    private void addFresherToCenter(String email) {
        getAllFresher();
        for(Fresher fresher : mList)
        {
            if(fresher.getEmail().equals(email))
            {
                HashMap<String, Object> newFresher = new HashMap<>();
                newFresher.put("key", fresher.getKey());
                newFresher.put("name", fresher.getName());
                newFresher.put("email", fresher.getEmail());
                newFresher.put("language", fresher.getLanguage());
                newFresher.put("center", acronym);
                newFresher.put("dateOfBirth", fresher.getDateOfBirth());
                newFresher.put("score", fresher.getScore());
                newFresher.put("image", fresher.getImage());
                String oldCenterName = fresher.getCenter();
                myRef.child(fresher.getKey()).updateChildren(newFresher).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.toastUpdateSuccess), Toast.LENGTH_SHORT).show();
                            updateCenterTotalFresherEdit(acronym, oldCenterName);
                        }
                    }
                });
            }
        }
    }

    private void updateCenterTotalFresherEdit(String centerName, String oldCenterName)
    {
        if (!centerName.equals(oldCenterName)){
            SQLiteHelper db = new SQLiteHelper(getActivity());
            // thay doi so luong fresher center moi
            Center center = db.getCenterByAcronym(centerName);
            center.setTotalFresher(center.getTotalFresher()+1);
            db.updateCenter(center);

            //thay doi so luong fresher center cu
            center = db.getCenterByAcronym(oldCenterName);
            center.setTotalFresher(center.getTotalFresher()-1);
            db.updateCenter(center);
        }
    }
}