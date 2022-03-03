package com.example.listit.ActivityUncompleted;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listit.Database.ListItEntry;
import com.example.listit.ListItDiffUtils;
import com.example.listit.R;
import com.example.listit.Utilities;

import java.util.Date;
import java.util.List;

public class UncompletedRecyclerAdapter extends RecyclerView.Adapter<UncompletedRecyclerAdapter.ViewHolder> {


    private final Context mContext;
    private List<ListItEntry> mData;

    public UncompletedRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_uncompleted_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.constraintLayout.setBackground(Utilities.getListItemLayoutBg(mContext,
                mData.get(position).getPriority()));
        holder.descriptionView.setText(mData.get(position).getDescription());
        holder.titleTextView.setText(mData.get(position).getTitle());
        Date date = mData.get(position).getDueDate();
        holder.dueDateView.setText(Utilities.dateFormatter(date));
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public ConstraintLayout constraintLayout;
        public Button dropDownButton;
        public TextView descriptionView;
        public TextView dueDateView;
        public Boolean descriptionIsVisible = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.uncompletedTitleTextView);
            constraintLayout = itemView.findViewById(R.id.uncompletedConstraintLayout);
            dropDownButton = itemView.findViewById(R.id.uncompletedDropDownButton);
            descriptionView = itemView.findViewById(R.id.uncompletedDescriptionView);
            dueDateView = itemView.findViewById(R.id.uncompletedDateView);

            dropDownButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleDescriptionView();
                }
            });
        }

        private void toggleDescriptionView() {
            if (descriptionIsVisible) {
                descriptionView.setVisibility(View.GONE);
                descriptionIsVisible = false;
            } else {
                descriptionView.setVisibility(View.VISIBLE);
                descriptionIsVisible = true;
            }
        }
    }

    public void setData(List<ListItEntry> listItEntries) {

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ListItDiffUtils(mData, listItEntries));
        mData = listItEntries;
        result.dispatchUpdatesTo(this);
    }
}
