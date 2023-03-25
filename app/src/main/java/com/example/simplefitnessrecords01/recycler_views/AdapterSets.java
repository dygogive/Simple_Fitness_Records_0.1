package com.example.simplefitnessrecords01.recycler_views;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.SetActivity;
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
    public void addSetFit(SetFitness set) {
        setFitList.add(set);
    }

    public List<SetFitness> getSetFitList() {
        return setFitList;
    }
    public SetFitness getSetFitness(int position) {
        return setFitList.get(position);
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

        SetFitness setFitness1 = null;

        //біндер
        SetFitRecyclerItemBinding binding = SetFitRecyclerItemBinding.bind(itemView);

        //Конструктор
        public HolderSetFitList(@NonNull View itemView) {
            super(itemView);

            //встановити слухач Long натискань на елемент в'ю
            itemView.setOnLongClickListener(v -> {
                //позиція
                int position = getAbsoluteAdapterPosition();



                //реєстр контекстного меню
                v.setOnCreateContextMenuListener((menu, v1, menuInfo) -> {
                    // Встановлюємо заголовок контекстного меню
                    menu.setHeaderTitle("Опції");
                    Log.d("isErrorOr", "v.setOnCreateContextMenuListener((menu, v1, menuInfo) -> {");
                    // Заповнюємо контекстне меню пунктами
                    ((Activity)context).getMenuInflater().inflate(R.menu.context_menu_recycler_setfitness, menu);
                });




                //Якщо контекст це МейнАктівіті (краще використати інтерфейс)
                if (context instanceof SetActivity) {
                    SetActivity child = (SetActivity) context;
                    //Задаєш позицію
                    child.setPosition(position);
                } else Toast.makeText(context, "Error! SetActivity renamed!", Toast.LENGTH_SHORT).show();




                //показати контекстне меню
                v.showContextMenu();
                Log.d("findErr2" , "test0");



                return true;
            });


            //Слухач зміни тексту
            binding.tvExerciceName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setFitness1.getExe().setExeName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.etRepeat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setFitness1.getRecordSet().getRepeats().changeRepeats(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.etWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    setFitness1.getRecordSet().getWeight().changeWeight(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }



        //Оброблення даних від об'єкту щоб оживити в'ю у цьому холдері
        void initItemView(SetFitness setFitness){

            setFitness1 = setFitness;

            String exeName = setFitness.getExe().toString();
            binding.tvExerciceName.setText(exeName);

            int weight = setFitness.getRecordSet().getWeight().getIntWeight();
            if(weight != 0) binding.etWeight.setText(String.valueOf(weight));

            int repeats = setFitness.getRecordSet().getRepeats().getIntRepeats();
            if(repeats != 0) binding.etRepeat.setText(String.valueOf(repeats));

        }

    }
}
