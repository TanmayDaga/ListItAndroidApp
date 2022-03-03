package com.example.listit.MainActivity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.listit.AppExecutors;
import com.example.listit.R;
import com.example.listit.Utilities;

public class AddCategoryDialog extends DialogFragment {

    public AddCategoryDialog(){
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
       View view = inflater.inflate(R.layout.add_category_dialog,null);

       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       return builder.setView(view)
               .setTitle("Add Category?")
               .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
               })
               .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       AppExecutors.getsInstance().diskIo().execute(new Runnable() {
                           @Override
                           public void run() {

                               String name = ((TextView) view.findViewById(R.id.addCategoryEditText)).getText().toString();
                               if(TextUtils.isEmpty(name)){
                                   getActivity().runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           Toast.makeText(getContext(),"Empty category cannot be added",Toast.LENGTH_LONG).show();
                                       }
                                   });

                                   return;
                               }

                               Utilities.addCategory(getContext(),
                                        name);

                           }
                       });
                   }
               }).create();


    }

    public static AddCategoryDialog newInstance(){
        return new AddCategoryDialog();
    }


}
