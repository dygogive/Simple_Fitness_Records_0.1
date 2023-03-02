package com.example.simplefitnessrecords01.activityResultContracts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.simplefitnessrecords01.UI_activities.SetActivity;

public class MyActivityResultContract extends ActivityResultContract<String[], Boolean> {

    //створити намір і відправити його з інфою
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String[] txts) {
        // Створіть намір для запуску вашої активності
        Intent intent = new Intent(context, SetActivity.class);
        //положити дані в намір
        intent.putExtra("start fitness", txts);
        //повернути методом намір
        return intent;
    }

    //получити намір і обробити інфу з ним
    @Override
    public Boolean parseResult(int resultCode, @Nullable Intent intent) {
        // Розібрати результат з активності
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getBooleanExtra("result fitness", false);
        }
        return false;
    }
}
