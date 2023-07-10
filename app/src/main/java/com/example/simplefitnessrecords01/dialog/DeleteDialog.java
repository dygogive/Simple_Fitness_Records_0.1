package com.example.simplefitnessrecords01.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.DialogDeleteBinding;

public class DeleteDialog extends Dialog {
    private DialogDeleteBinding mBinding;
    private final Context context;

    private DialogOnClick dialogOnClick;




    //Constructor
    public DeleteDialog(@NonNull Context context, DialogOnClick dialogOnClick) {
        super(context);
        this.context = context;
        this.dialogOnClick = dialogOnClick;
        // We call the method that configures the appearance of the dialog
        setupDialog();
    }


    //set up this dialog
    private void setupDialog() {
        // Setting the appearance of the dialog using view binding
        mBinding = DialogDeleteBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(mBinding.getRoot());

        //set size of text
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedTextSize = preferences.getString("text_size_preference", context.getResources().getString(R.string.default_text_size));
        mBinding.btnDelete.setTextSize(Float.parseFloat(selectedTextSize));
        mBinding.btnCancel.setTextSize(Float.parseFloat(selectedTextSize));
        mBinding.textInfo.setTextSize(Float.parseFloat(selectedTextSize));

        //listener og button in dialog
        mBinding.btnDelete.setOnClickListener(view -> {
            dialogOnClick.OnClick();
            this.cancel();
        });



        //listener og button in dialog
        mBinding.btnCancel.setOnClickListener(view -> {
            this.cancel();
        });

    }
}
