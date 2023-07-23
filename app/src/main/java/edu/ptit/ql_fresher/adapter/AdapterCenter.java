package edu.ptit.ql_fresher.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import edu.ptit.ql_fresher.EditDeleteActivity;
import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.fragment.FragmentEditDeleteCenter;
import edu.ptit.ql_fresher.model.Center;
import edu.ptit.ql_fresher.viewholder.CenterViewHolder;

public class AdapterCenter extends RecyclerView.Adapter<CenterViewHolder> {
    private Context mContext;
    private List<Center> mList;

    public AdapterCenter(Context mContext, List<Center> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }

    public void setList(List<Center> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CenterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_center,parent,false);
        return new CenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterViewHolder holder, int position) {
        Center center=mList.get(position);
        holder.tvAcronym.setText(center.getAcronym());
        holder.tvName.setText(center.getName());
        holder.tvAdd.setText(center.getAddress());
        holder.tvTotal.setText("Total number of center: " + String.valueOf(center.getTotalFresher()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditDeleteActivity.class);
                intent.putExtra("act", 1);
                intent.putExtra("id", center.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}