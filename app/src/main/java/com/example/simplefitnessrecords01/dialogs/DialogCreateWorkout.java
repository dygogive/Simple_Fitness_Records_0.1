package com.example.simplefitnessrecords01.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;


import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.DialogCreateOnegymBinding;

//Dialog for construct one training
public class DialogCreateWorkout extends Dialog {
    private DialogCreateOnegymBinding mBinding;
    private final Context context;
    private DialogUniqueNameProcessor dialogUniqueNameProcessor;
    private String[] dataToDialogue = null;


    //Constructor
    public DialogCreateWorkout(@NonNull Context context, DialogUniqueNameProcessor dialogUniqueNameProcessor) {
        super(context);
        this.context = context;
        this.dialogUniqueNameProcessor = dialogUniqueNameProcessor;

        // We call the method that configures the appearance of the dialog
        setupDialog();
    }


    public DialogCreateWorkout(@NonNull Context context, DialogUniqueNameProcessor dialogUniqueNameProcessor, String[] dataToDialogue1) {
        super(context);
        this.context = context;
        this.dialogUniqueNameProcessor = dialogUniqueNameProcessor;
        dataToDialogue = dataToDialogue1;

        // We call the method that configures the appearance of the dialog
        setupDialog();
    }


    //set up this dialog
    private void setupDialog() {
        mBinding = DialogCreateOnegymBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(mBinding.getRoot());

        //get text size
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedTextSize       = preferences.getString("text_size_preference", context.getResources().getString(R.string.default_text_size));

        //set textSize
        mBinding.editTextDay    .setTextSize(Float.parseFloat(selectedTextSize));
        mBinding.editTextName   .setTextSize(Float.parseFloat(selectedTextSize));
        mBinding.editTextSubname.setTextSize(Float.parseFloat(selectedTextSize));
        mBinding.buttonCreate   .setTextSize(Float.parseFloat(selectedTextSize));

        //setup text in dialog
        if(dataToDialogue != null) {
            mBinding.editTextDay.     setText(dataToDialogue[0]);
            mBinding.editTextName.    setText(dataToDialogue[1]);
            mBinding.editTextSubname. setText(dataToDialogue[2]);
            mBinding.buttonCreate.    setText(R.string.edit);
        }

        //listener og button in dialog
        mBinding.buttonCreate.setOnClickListener(view -> {

            // get texts from the fields in the dialog
            String day  =  mBinding.editTextDay    .getText().toString();
            String name =  mBinding.editTextName   .getText().toString();
            String info =  mBinding.editTextSubname.getText().toString();

            //check if at least one field is empty, then fill it in red
            if ( day.equals("") | name.equals("") ) {
                //Red color
                int redColor = Color.RED;
                //set the hidden text in red
                mBinding.editTextDay  .setHintTextColor(redColor);
                mBinding.editTextName .setHintTextColor(redColor);
            } else {
                //create a unique workout name
                String[] uniqueName = new String[]{ day, name, info };
                //process unique name
                dialogUniqueNameProcessor.processUniqueName(uniqueName);
                //закрити діалог
                dismiss();
            }
        });
    }
}
