package edu.ptit.ql_fresher.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.ptit.ql_fresher.EditDeleteActivity;
import edu.ptit.ql_fresher.MainActivity;
import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.fragment.FragmentAddFresher;
import edu.ptit.ql_fresher.fragment.FragmentEditDeleteFresher;
import edu.ptit.ql_fresher.model.Fresher;
import edu.ptit.ql_fresher.viewholder.FresherViewHolder;

public class AdapterFresher extends RecyclerView.Adapter<FresherViewHolder>{
    private Context mContext;
    private List<Fresher> mList;

    public AdapterFresher(Context mContext, List<Fresher> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }

    public void setmList(List<Fresher> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void filterList(List<Fresher> filterlist)
    {
        mList = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FresherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_fresher,parent,false);
        return new FresherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FresherViewHolder holder, int position) {
        Fresher fresher=mList.get(position);
        holder.tvName.setText(fresher.getName());
        holder.tvEmail.setText(fresher.getEmail());
        holder.tvLang.setText(fresher.getLanguage());
        holder.tvCenter.setText(fresher.getCenter());
        Picasso.get().load(fresher.getImage()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditDeleteActivity.class);
                intent.putExtra("act", 0);
                intent.putExtra("key", fresher.getKey());
                intent.putExtra("oldCenterName", fresher.getCenter());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}