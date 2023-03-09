package com.example.simplefitnessrecords01.recycler_views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.SetFitRecyclerItemBinding;
import com.example.simplefitnessrecords01.fitness.SetFitness;

import java.util.ArrayList;
import java.util.List;

public class AdapterSets extends RecyclerView.Adapter<AdapterSets.HolderSetFitList> {

    Context context;

    //СПИСОК виконаних підходів на тренуванні
    private List<SetFitness> setFitList = new ArrayList<>();
    //Задати список підходів
    public void setSetFitList(List<SetFitness> setFitList) {
        this.setFitList = setFitList;
    }

    //Добавити в список адаптера ще один сет
    public void addSet(SetFitness set) {
        setFitList.add(set);
    }





    // Конструктори
    public AdapterSets(Context context) {
        this.context = context;
    }

    public AdapterSets(Context context, List<SetFitness> setFitnessList) {
        this.context = context;
        this.setFitList = setFitnessList;
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
        SetFitness setFitness = setFitList.get(position);
        //Через метод в холдері запихнути дані в В'ю
        holder.initItemView(setFitness);
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
        void initItemView(SetFitness setFitness){
            String exeName = setFitness.getExe().toString();
            binding.tvExerciceName.setText(exeName);

            String weight = setFitness.getRecordSet().getWeight().toString();
            binding.etWeight.setText(weight);

            String repeats = setFitness.getRecordSet().getRepeats().toString();
            binding.etRepeat.setText(repeats);
        }


    }
}
