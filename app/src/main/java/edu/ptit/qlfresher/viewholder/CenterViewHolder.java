package edu.ptit.qlfresher.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.ptit.qlfresher.R;
import edu.ptit.qlfresher.modelinterface.ItemClickListener;

public class CenterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvAcronym, tvName, tvAdd, tvTotal;
        public ItemClickListener listener;
        public CenterViewHolder(@NonNull View view) {
            super(view);
            tvAcronym =view.findViewById(R.id.tvAcronymit);
            tvName =view.findViewById(R.id.tvNameCenterit);
            tvAdd =view.findViewById(R.id.tvAddressCenterit);
            tvTotal =view.findViewById(R.id.tvTotalCenterit);
        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition(),false);
        }
}