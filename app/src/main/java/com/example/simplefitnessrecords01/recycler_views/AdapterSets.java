package com.example.simplefitnessrecords01.recycler_views;

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
import com.example.simplefitnessrecords01.fitness.OneSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdapterSets extends RecyclerView.Adapter<AdapterSets.HolderSetFitList> {

    Context context;

    //СПИСОК виконаних підходів на тренуванні
    private List<OneSet> setFitList = new ArrayList<>();
    //Задати список підходів
    public void setSetFitList(List<OneSet> setFitList) {
        this.setFitList = setFitList;
    }

    //Добавити в список адаптера ще один сет
    public void addSetFit(OneSet set) {
        setFitList.add(set);
    }

    public List<OneSet> getSetFitList() {
        return setFitList;
    }
    public OneSet getSetFitness(int position) {
        return setFitList.get(position);
    }


    // Конструктори
    public AdapterSets(Context context) {
        this.context = context;
    }

    public AdapterSets(Context context, List<OneSet> oneSetList) {
        this.context = context;
        this.setFitList = oneSetList;
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
        OneSet oneSet = setFitList.get(position);
        //Через метод в холдері запихнути дані в В'ю
        holder.initItemView(oneSet);
    }

    //Метод повертає кількість об'єктів
    @Override
    public int getItemCount() {
        return setFitList.size();
    }




    // *******************  Холдер *****************
    public class HolderSetFitList extends RecyclerView.ViewHolder {

        OneSet oneSet1 = null;

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
                    ((SetActivity)context).getMenuInflater().inflate(R.menu.context_menu_recycler_setfitness, menu);
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


            //Слухачі зміни тексту
            binding.tvExerciceName.addTextChangedListener(new TextWatcher() {
                Timer timer = new Timer();
                final long DELAY = 1000; // time delay msec
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("TextWatcher",s.toString());
                            if( !(s.toString()).equals("") ) oneSet1.getExe().setExeName(s.toString());
                            ((SetActivity)context).saveToDBFromRecycler(AdapterSets.this);
                        }
                    } , DELAY);
                }
            });
            binding.etRepeat.addTextChangedListener(new TextWatcher() {
                Timer timer = new Timer();
                final long DELAY = 1000; // time delay msec
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("TextWatcher",s.toString());
                            if( !(s.toString()).equals("") ) oneSet1.getRecordSet().getRepeats().changeRepeats(s.toString());
                            ((SetActivity)context).saveToDBFromRecycler(AdapterSets.this);
                        }
                    } , DELAY);
                }
            });
            binding.etWeight.addTextChangedListener(new TextWatcher() {
                Timer timer = new Timer();
                final long DELAY = 1000; // time delay msec
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Log.d("TextWatcher",s.toString());
                            if( !(s.toString()).equals("") ) oneSet1.getRecordSet().getWeight().changeWeight(s.toString());
                            ((SetActivity)context).saveToDBFromRecycler(AdapterSets.this);
                        }
                    } , DELAY);
                }
            });
        }



        //Оброблення даних від об'єкту щоб оживити в'ю у цьому холдері
        void initItemView(OneSet oneSet){

            oneSet1 = oneSet;

            String exeName = oneSet.getExe().toString();
            binding.tvExerciceName.setText(exeName);

            int weight = oneSet.getRecordSet().getWeight().getIntWeight();
            binding.etWeight.setText(String.valueOf(weight));
            if (weight == 0) binding.etWeight.setText("");

            int repeats = oneSet.getRecordSet().getRepeats().getIntRepeats();
            binding.etRepeat.setText(String.valueOf(repeats));
            if (repeats == 0) binding.etRepeat.setText("");

        }

    }
}
