package edu.ptit.ql_fresher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.ql_fresher.R;

public class AdapterViewFlip extends BaseAdapter {
    private Context context;
    List<Integer> lst = new ArrayList<>();

    public AdapterViewFlip(Context context, List<Integer> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        ImageView img =v.findViewById(R.id.imgii);
        img.setImageResource(lst.get(position));
        return v;
    }
}