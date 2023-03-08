package com.example.simplefitnessrecords01.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.example.simplefitnessrecords01.UI_activities.MainActivity;
import com.example.simplefitnessrecords01.databinding.DialogNewTrainingBinding;

import java.util.UUID;

public class CustomDialog extends Dialog {
    private DialogNewTrainingBinding mBinding;
    private final Context context;

    DialogOK dialogOK;

    public CustomDialog(@NonNull Context context, DialogOK dialogOK1) {
        super(context);
        this.context = context;
        dialogOK = dialogOK1;
        // Викликаємо метод, який налаштує зовнішній вигляд діалогу
        setupDialog();
    }


    private void setupDialog() {
        // Налаштування зовнішнього вигляду діалогу з використанням view binding
        mBinding = DialogNewTrainingBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(mBinding.getRoot());
        setTitle("Custom Dialog");

        mBinding.buttonStart.setOnClickListener(view -> {

            //отримати тексти з полів в діалозі
            String day = mBinding.editTextDay.getText().toString();
            String name = mBinding.editTextName.getText().toString();
            String subname = mBinding.editTextSubname.getText().toString();
            // Генерує унікальну строку для імені
            String uniqueID = UUID.randomUUID().toString();


            //перевіряємо якщо хоча б одне поле пусте
            if (day.equals("") | name.equals("") | subname.equals("")) {
                //червоний колір
                int redColor = Color.RED;
                //задати прихований текст червоним кольором
                mBinding.editTextDay.setHintTextColor(redColor);
                mBinding.editTextName.setHintTextColor(redColor);
                mBinding.editTextSubname.setHintTextColor(redColor);
            } else {
                //створити тестові дані
                String[] texts = new String[]{day, name, subname, uniqueID};

                dialogOK.pressBtnOK(texts);

                //закрити діалог
                dismiss();
            }
        });

    }
}
