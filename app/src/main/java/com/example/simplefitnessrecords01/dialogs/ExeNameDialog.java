package com.example.simplefitnessrecords01.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.DialogNewExeBinding;

//Dialog for construct one training
public class ExeNameDialog extends Dialog {
    private DialogNewExeBinding mBinding;
    private final Context context;
    private DialogUniqueNameProcessor dialogUniqueNameProcessor;

    private String exeName = "null";




    //Constructor
    public ExeNameDialog(@NonNull Context context, DialogUniqueNameProcessor dialogUniqueNameProcessor) {
        super(context);
        this.context = context;
        this.dialogUniqueNameProcessor = dialogUniqueNameProcessor;

        // We call the method that configures the appearance of the dialog
        setupDialog();
    }

    public ExeNameDialog(@NonNull Context context, DialogUniqueNameProcessor dialogUniqueNameProcessor, String exeName) {
        super(context);
        this.context = context;
        this.dialogUniqueNameProcessor = dialogUniqueNameProcessor;
        this.exeName = exeName;

        // We call the method that configures the appearance of the dialog
        setupDialog();
    }




    //set up this dialog
    private void setupDialog() {
        // Setting the appearance of the dialog using view binding
        mBinding = DialogNewExeBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(mBinding.getRoot());

        //set size of text
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedTextSize = preferences.getString("text_size_preference", context.getResources().getString(R.string.default_text_size));
        mBinding.etExeNameNew.setTextSize(Float.parseFloat(selectedTextSize));

        if(!exeName.equals("null")) mBinding.etExeNameNew.setText(exeName);



        //listener og button in dialog
        mBinding.imageOK.setOnClickListener(view -> {

            // get texts from the fields in the dialog
            String name = mBinding.etExeNameNew.getText().toString();


            //check if at least one field is empty, then fill it in red
            if (name.equals("")) {
                //Red color
                int redColor = Color.RED;
                //set the hidden text in red
                mBinding.etExeNameNew.setHintTextColor(redColor);
            } else {
                //create a unique workout name
                String[] texts = new String[]{name};
                //process unique name
                dialogUniqueNameProcessor.processUniqueName(texts);
                //закрити діалог
                dismiss();
            }
        });

    }
}
