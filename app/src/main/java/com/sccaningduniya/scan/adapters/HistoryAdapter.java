package com.sccaningduniya.scan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.database.HistoryEntity;


public class HistoryAdapter extends ListAdapter<HistoryEntity, HistoryAdapter.HistoryHolder> {
    private static final DiffUtil.ItemCallback<HistoryEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<HistoryEntity>() { 
        @Override 
        public boolean areItemsTheSame(HistoryEntity oldItem, HistoryEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override 
        public boolean areContentsTheSame(HistoryEntity oldItem, HistoryEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }
    };
    private OnItemClickListener listener;

    public HistoryAdapter() {
        super(DIFF_CALLBACK);
    }

    @Override 
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryHolder(itemView);
    }

    @Override 
    public void onBindViewHolder(HistoryHolder holder, int position) {
        HistoryEntity currentEntry = getItem(position);
        holder.tvHistoryDate.setText(currentEntry.getDate());
        holder.tvHistoryFormat.setText(currentEntry.getFormat());
        holder.tvHistoryCode.setText(currentEntry.getInformation());
    }

    public HistoryEntity getHistoryEntryAt(int position) {
        return getItem(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    
    public interface OnItemClickListener {
        void onItemClick(HistoryEntity historyEntity);
    }

    
    class HistoryHolder extends RecyclerView.ViewHolder {
        private TextView tvHistoryCode;
        private TextView tvHistoryDate;
        private TextView tvHistoryFormat;

        public HistoryHolder(View itemView) {
            super(itemView);
            this.tvHistoryCode = (TextView) itemView.findViewById(R.id.tvHistoryCode);
            this.tvHistoryDate = (TextView) itemView.findViewById(R.id.tvHistoryDate);
            this.tvHistoryFormat = (TextView) itemView.findViewById(R.id.tvHistoryFormat);
            itemView.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View v) {
                    int position = HistoryHolder.this.getAdapterPosition();
                    if (HistoryAdapter.this.listener != null && position != -1) {
                        HistoryAdapter.this.listener.onItemClick((HistoryEntity) HistoryAdapter.this.getItem(position));
                    }
                }
            });
        }
    }
}
