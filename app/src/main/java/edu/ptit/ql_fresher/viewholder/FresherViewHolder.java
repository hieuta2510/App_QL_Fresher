package edu.ptit.ql_fresher.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ptit.ql_fresher.R;
import edu.ptit.ql_fresher.model.ItemClickListener;

public class FresherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView img;
        public TextView tvName, tvEmail, tvLang;
        public ItemClickListener listener;
        public FresherViewHolder(@NonNull View view) {
            super(view);
            tvName =view.findViewById(R.id.tvNameit);
            tvEmail =view.findViewById(R.id.tvEmailit);
            tvLang =view.findViewById(R.id.tvLangit);
            img=view.findViewById(R.id.imgit);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition(),false);
        }
}