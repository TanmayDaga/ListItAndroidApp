package com.example.listit.MainActivity;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.ListItEntry;
import com.example.listit.ListItDiffUtils;
import com.example.listit.R;
import com.example.listit.Utilities;

import java.util.Date;
import java.util.List;


public class ListItFragmentRecyclerAdapter extends RecyclerView.Adapter<ListItFragmentRecyclerAdapter.ListItViewHolder> {


    private List<ListItEntry> mListItEntries;
    private static final String TAG = ListItFragmentRecyclerAdapter.class.getSimpleName();
    private final Context mContext;


    public ListItFragmentRecyclerAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public ListItViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_it_recyclerview_item, parent, false);
        return new ListItViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ListItViewHolder holder, int position) {


        holder.listItemConstraintLayout.setBackground(Utilities.getListItemLayoutBg(mContext,
                mListItEntries.get(position).getPriority()));

        holder.descriptionView.setText(mListItEntries.get(position).getDescription());
        holder.listItTitleTextView.setText(mListItEntries.get(position).getTitle());
        holder.doneCheckBox.setChecked(false);
        Date date = mListItEntries.get(position).getDueDate();
        holder.dueDateView.setText(Utilities.dateFormatter(date));

    }

    @Override
    public int getItemCount() {
        if (mListItEntries == null) {

            return 0;
        }
        return mListItEntries.size();

    }


    class ListItViewHolder extends RecyclerView.ViewHolder {

        public TextView listItTitleTextView;
        public CheckBox doneCheckBox;
        public ConstraintLayout listItemConstraintLayout;
        public Button dropDownButton;
        public TextView descriptionView;
        public TextView dueDateView;
        private Boolean descriptionIsVisible = false;

        public ListItViewHolder(@NonNull View itemView) {
            super(itemView);
            listItTitleTextView = itemView.findViewById(R.id.todoTitleTextView);
            doneCheckBox = itemView.findViewById(R.id.todoDoneCheckBox);
            listItemConstraintLayout = itemView.findViewById(R.id.listItemConstraintLayout);
            dropDownButton = itemView.findViewById(R.id.button);
            descriptionView = itemView.findViewById(R.id.descriptionView);
            dueDateView = itemView.findViewById(R.id.dateView);


//            Visibility of drop down on drop down arrow button
            dropDownButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleDescriptionView();
                }
            });

            doneCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    AppExecutors.getsInstance().diskIo().execute(new Runnable() {
                        @Override
                        public void run() {
                            AppDataBase.getInstance().listItDao().setCompletedById(
                                    b, mListItEntries.get(getAdapterPosition()).getId()
                            );
                        }


                    });

                    if (b) {
                        mListItEntries.remove(getAdapterPosition());

                    }
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
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ListItDiffUtils(mListItEntries, listItEntries));
        mListItEntries = listItEntries;
        result.dispatchUpdatesTo(this);

    }

    public List<ListItEntry> getTasks() {
        return mListItEntries;
    }
}
