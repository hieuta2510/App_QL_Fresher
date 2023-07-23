package edu.ptit.ql_fresher.fragment;

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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.adapter.AdapterFresher;
import edu.ptit.ql_fresher.model.Center;
import edu.ptit.ql_fresher.model.Fresher;

public class FragmentDetailsCenter extends Fragment {
    private View mView;
    private TextView tvAcronym, tvAdd, tvName, tvDB;
    private Button btSearch;
    private RecyclerView recycleFresher;
    private AdapterFresher adapter;
    private List<Fresher> mList;
    private DatabaseReference myRef;
    private String acronym = "";
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
        tvDB.setText(getResources().getString(R.string.dashboard) + String.valueOf(center.getTotalFresher()));
        //recycle view
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recycleFresher.setLayoutManager(manager);
        myRef = FirebaseDatabase
                .getInstance("https://qlyfresher-default-rtdb.firebaseio.com/")
                .getReference().child("fresherlist");
    }

    private void init(View view)
    {
        tvAcronym = view.findViewById(R.id.tvAcronymDetails);
        tvAdd = view.findViewById(R.id.tvAddDetails);
        tvName = view.findViewById(R.id.tvNameDetails);
        tvDB = view.findViewById(R.id.tvDBDetails);
        btSearch = view.findViewById(R.id.btAddDetails);
        recycleFresher = view.findViewById(R.id.recycleFresherDetails);
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
}