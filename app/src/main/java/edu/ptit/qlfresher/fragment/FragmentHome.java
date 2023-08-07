package edu.ptit.qlfresher.fragment;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.adapter.AdapterRecycleFunc;
import edu.ptit.qlfresher.adapter.AdapterViewFlip;
import edu.ptit.qlfresher.model.Func;


public class FragmentHome extends Fragment{
    private RecyclerView recyclerView;
    private AdapterRecycleFunc adapter;
    private ViewFlipper viewFlipper;
    private AdapterViewFlip adapterViewFlip;

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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        viewFlipper = view.findViewById(R.id.vfFragHome);
        actionViewFlip();
    }

    private void actionViewFlip() {
        List <Integer> lst = new ArrayList<>();
        lst.add(R.drawable.cay_alone);
        lst.add(R.drawable.mom_dophin);
        adapterViewFlip = new AdapterViewFlip(getContext(), lst);
        for(int i = 0; i < adapterViewFlip.getCount(); i++)
        {
            viewFlipper.addView(adapterViewFlip.getView(i,null,null));
        }

        viewFlipper.setFlipInterval(3000); // Chuyển đổi sau mỗi 2 giây
        viewFlipper.setAutoStart(true); // Tự động chạy
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() > e2.getX()) {
                    // Kéo sang phải
                    viewFlipper.showNext();
                } else if (e1.getX() < e2.getX()) {
                    // Kéo sang trái
                    viewFlipper.showPrevious();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }
}
