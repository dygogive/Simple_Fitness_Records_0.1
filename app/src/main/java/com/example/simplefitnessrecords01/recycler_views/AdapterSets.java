package com.example.simplefitnessrecords01.recycler_views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.GetterDB;
import com.example.simplefitnessrecords01.UI_activities.GetterSQLhelper;
import com.example.simplefitnessrecords01.UI_activities.MainActivity;
import com.example.simplefitnessrecords01.UI_activities.SetActivity;
import com.example.simplefitnessrecords01.databinding.SetFitRecyclerItemBinding;
import com.example.simplefitnessrecords01.fitness.SetFitness;
import com.example.simplefitnessrecords01.sql.SQLfitness;
import com.example.simplefitnessrecords01.sql.SQLsets;

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

    public void resetSetFitList(){
        setFitList = new ArrayList<>();
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




    public void deleteItem(int position){

        GetterSQLhelper getterSQLhelper = (GetterSQLhelper) context;

        //код для видалення
        String table_name = ((SQLsets)getterSQLhelper.getHelper()).getTableName();
        String where_clause = SQLsets.COLUMN_ID + "=?";
        String[] where_args = new String[] {String.valueOf(setFitList.get(position).getId())};
        String sql = "DELETE FROM " + table_name + " WHERE " + where_clause;

        //видалити з бази
        getterSQLhelper.getHelper().getWritableDatabase().execSQL(sql, where_args);

        //видалити з адаптера
        setFitList.remove(position);

        //повідомити адаптер про видалення
        notifyItemRemoved(position);
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


            //встановити слухач Long натискань на елемент в'ю
            itemView.setOnLongClickListener(v -> {
                //позиція
                int position = getAbsoluteAdapterPosition();
                //реєстр контекстного меню
                v.setOnCreateContextMenuListener((menu, v1, menuInfo) -> {
                    // Встановлюємо заголовок контекстного меню
                    menu.setHeaderTitle("Опції");
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

                return true;
            });
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
