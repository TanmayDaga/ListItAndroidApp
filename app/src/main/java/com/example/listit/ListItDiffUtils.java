package com.example.listit;

import androidx.recyclerview.widget.DiffUtil;

import com.example.listit.Database.ListItEntry;

import java.util.List;

public class ListItDiffUtils extends DiffUtil.Callback {

    private List<ListItEntry> mOldList;
    private List<ListItEntry> mNewList;
    public ListItDiffUtils(List<ListItEntry> oldList,List<ListItEntry> newList){
        this.mNewList = newList;
        this.mOldList = oldList;
    }

    @Override
    public int getOldListSize() {
        if(mOldList == null){
            return 0;
        }
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        if(mNewList == null){
            return 0;
        }
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return
                mOldList.get(oldItemPosition).getId()
                        == mNewList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ListItEntry oldItem = mOldList.get(oldItemPosition);
        ListItEntry newItem = mNewList.get(newItemPosition);
        return oldItem.getId() == newItem.getId() &&
                oldItem.getTitle().equals(newItem.getTitle()) &&
                oldItem.getCategory().equals(newItem.getCategory()) &&
                oldItem.getDescription().equals(newItem.getDescription()) &&
                oldItem.getDueDate() == newItem.getDueDate()&&
                oldItem.getCompleted() == newItem.getCompleted();
    }
}
