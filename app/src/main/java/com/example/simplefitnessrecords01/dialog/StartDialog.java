package com.example.simplefitnessrecords01.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.example.simplefitnessrecords01.databinding.DialogNewTrainingBinding;

import java.util.UUID;

public class StartDialog extends Dialog {
    private DialogNewTrainingBinding mBinding;
    private final Context context;

    ButtonOK buttonOK;

    public StartDialog(@NonNull Context context, ButtonOK buttonOK) {
        super(context);
        this.context = context;
        this.buttonOK = buttonOK;
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
                String[] texts = new String[]{day, name, subname};
                buttonOK.pressBtnOK(texts);
                //закрити діалог
                dismiss();
            }
        });

    }
}
