package com.example.simplefitnessrecords01.recycler_views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.SetFitRecyclerItemBinding;
import com.example.simplefitnessrecords01.fitness.SetFit;

import java.util.ArrayList;
import java.util.List;

public class AdapterSets extends RecyclerView.Adapter<AdapterSets.HolderSetFitList> {

    Context context;

    //СПИСОК виконаних підходів на тренуванні
    private List<SetFit> setFitList = new ArrayList<>();
    //Задати список підходів
    public void setSetFitList(List<SetFit> setFitList) {
        this.setFitList = setFitList;
    }



    // Конструктори
    public AdapterSets(Context context) {
        this.context = context;
    }

    public AdapterSets(Context context, List<SetFit> setFitList) {
        this.context = context;
        this.setFitList = setFitList;
    }



    // **************Створення заготовки В'ю ТА Холдера ********
    @NonNull
    @Override
    public HolderSetFitList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Створення В'ю
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_fit_recycler_item, parent, false);

        //Створення холдера
        return new HolderSetFitList(view);
    }

    //****** Ініціалізувати (Оживити) В'ю та холдер даними об'єкту з списку
    @Override
    public void onBindViewHolder(@NonNull HolderSetFitList holder, int position) {
        //Витягти об'єкт зі списку
        SetFit setFit = setFitList.get(position);
        //Через метод в холдері запихнути дані в В'ю
        holder.initItemView(setFit);
    }

    //Метод повертає кількість об'єктів
    @Override
    public int getItemCount() {
        return setFitList.size();
    }


    // *******************  Холдер *****************
    public class HolderSetFitList extends RecyclerView.ViewHolder {

        //біндер
        SetFitRecyclerItemBinding binding = SetFitRecyclerItemBinding.bind(itemView);

        //Конструктор
        public HolderSetFitList(@NonNull View itemView) {
            super(itemView);
        }

        //Оброблення даних від об'єкту щоб оживити в'ю у цьому холдері
        void initItemView(SetFit setFit){
            String exeName = setFit.getExe().toString();
            binding.tvExerciceName.setText(exeName);

            String weight = setFit.getRecordSet().getWeight().toString();
            binding.etWeight.setText(weight);

            String repeats = setFit.getRecordSet().getRepeats().toString();
            binding.etRepeat.setText(repeats);
        }


    }
}
