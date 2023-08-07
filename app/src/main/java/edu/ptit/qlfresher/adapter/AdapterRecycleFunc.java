package edu.ptit.qlfresher.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.model.Func;

public class AdapterRecycleFunc extends RecyclerView.Adapter<AdapterRecycleFunc.HomeViewHolder>{
    private List <Func> list;
    private ItemListener itemListener;
    public AdapterRecycleFunc() {
        list = new ArrayList<>();
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<Func> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Func getItem(int position)
    {
        return list.get(position);
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_func, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Func func = list.get(position);
        holder.image_func.setImageResource(func.getImage());
        holder.name_func.setText(func.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView image_func;
        private TextView name_func;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            image_func = view.findViewById(R.id.item_func_img);
            name_func = view.findViewById(R.id.item_func_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemListener != null)
            {
                itemListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public interface ItemListener{
        void onItemClick(View view, int position);
    }
}