package com.example.simplefitnessrecords01.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.example.simplefitnessrecords01.databinding.DialogNewTrainingBinding;

//Dialog for construct one training
public class StartDialog extends Dialog {
    private DialogNewTrainingBinding mBinding;
    private final Context context;
    private UniqueNameProcessor uniqueNameProcessor;


    //Constructor
    public StartDialog(@NonNull Context context, UniqueNameProcessor uniqueNameProcessor) {
        super(context);
        this.context = context;
        this.uniqueNameProcessor = uniqueNameProcessor;

        // We call the method that configures the appearance of the dialog
        setupDialog();
    }


    //set up this dialog
    private void setupDialog() {
        // Setting the appearance of the dialog using view binding
        mBinding = DialogNewTrainingBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(mBinding.getRoot());

        //listener og button in dialog
        mBinding.buttonCreate.setOnClickListener(view -> {

            // get texts from the fields in the dialog
            String day = mBinding.editTextDay.getText().toString();
            String name = mBinding.editTextName.getText().toString();
            String subname = mBinding.editTextSubname.getText().toString();

            //check if at least one field is empty, then fill it in red
            if (day.equals("") | name.equals("") | subname.equals("")) {
                //Red color
                int redColor = Color.RED;
                //set the hidden text in red
                mBinding.editTextDay.setHintTextColor(redColor);
                mBinding.editTextName.setHintTextColor(redColor);
                mBinding.editTextSubname.setHintTextColor(redColor);
            } else {
                //create a unique workout name
                String[] texts = new String[]{day, name, subname};
                //process unique name
                uniqueNameProcessor.processUniqueName(texts);
                //закрити діалог
                dismiss();
            }
        });

    }
}
