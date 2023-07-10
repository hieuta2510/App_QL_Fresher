package edu.ptit.ql_fresher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import edu.ptit.ql_fresher.MainActivity;
import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.adapter.AdapterRecycleFunc;
import edu.ptit.ql_fresher.adapter.AdapterViewFlip;
import edu.ptit.ql_fresher.model.Fresher;
import edu.ptit.ql_fresher.model.Func;


public class FragmentHome extends Fragment implements AdapterRecycleFunc.ItemListener{
    private FragmentManager fragmentManager;
    private RecyclerView recyclerView;
    private AdapterRecycleFunc adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Func> list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rcv_home);
        adapter = new AdapterRecycleFunc();
        Func func1 = new Func(R.drawable.manage_fresh, getResources().getString(R.string.home_manageFresher));
        Func func2 = new Func(R.drawable.manage_center, getResources().getString(R.string.home_manageCenter));
        Func func3 = new Func(R.drawable.dashboard, getResources().getString(R.string.home_dashboard));
        list.add(func1); list.add(func2); list.add(func3);
        adapter.setList(list);
        adapter.setItemListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        fragmentManager = getActivity().getSupportFragmentManager();
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
