package com.example.simplefitnessrecords01.recycler_views;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.GetterDB;
import com.example.simplefitnessrecords01.UI_activities.MainActivity;
import com.example.simplefitnessrecords01.fitness.Fitness;
import com.example.simplefitnessrecords01.databinding.TextForRecyclerBinding;
import com.example.simplefitnessrecords01.sql.SQLSetFits;
import com.example.simplefitnessrecords01.sql.SQLfitness;

import java.util.List;

public class FitnessListAdapter extends RecyclerView.Adapter<FitnessListAdapter.SetTrainingHolder> {


    //контекст
    private Context context;




    //Список ітемів - тренувань
    private List<Fitness> setTrainingList;
    public void setFitnessList(List<Fitness> setTrainingList) {
        this.setTrainingList = setTrainingList;
    }
    public Fitness getItem(int position) {
        return setTrainingList.get(position);
    }






    //слухач натискань на елемент
    private OnItemRecyclerClickListener listenerShort;

    //метод для ініціалізації слухача натискань
    public void setOnItemRecyclerClickListener(OnItemRecyclerClickListener listener) {
        this.listenerShort = listener;
    }



    //конструктор
    public FitnessListAdapter(List<Fitness> setTrainingList, Context context) {
        this.setTrainingList = setTrainingList;
        this.context = context;
    }



    //метод створює в'ю холдери
    @NonNull
    @Override
    public SetTrainingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //створення в'ю
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_for_recycler, parent, false);
        //створення SetsHolder
        return new SetTrainingHolder(view);
    }



    //метод наповнює інформацією створені в'ю у холдерах
    @Override
    public void onBindViewHolder(@NonNull FitnessListAdapter.SetTrainingHolder holder, int position) {
        //отримати елемент списку
        Fitness setTraining = setTrainingList.get(position);
        //закинути з нього інфу у холдер
        holder.onBindData(setTraining);
    }



    //повернення кількості елементів, які відображає Рециклер
    @Override
    public int getItemCount() {
        //розмір елементів
        return setTrainingList.size();
    }


    //Видалити один з обёэктыв ы з бази, ы з рециклера
    public void deleteItem(int position) {
        GetterDB getterDB = (GetterDB) context;
        //код для видалення
        String table_name = SQLfitness.DATABASE_TABLE;
        String where_clause = SQLfitness.COLUMN_DAY + "=?";
        String[] where_args = new String[] { setTrainingList.get(position).getDay() };
        String sql = "DELETE FROM " + table_name + " WHERE " + where_clause;
        //видалити з бази
        getterDB.getDB().execSQL(sql, where_args);



        /*видалити з бази записів тренувань
          код для видалення */
        Log.d("whereErr", "test 1");
        table_name   = SQLSetFits.DATABASE_TABLE;
        Log.d("whereErr", "test 2");
        where_clause = SQLfitness.COLUMN_FITNAME + "=?";
        Log.d("whereErr", "test 3");
        where_args = new String[] { setTrainingList.get(position).getFitnessName() };
        Log.d("whereErr", "test 4");
        sql          = "DELETE FROM " + table_name + " WHERE " + where_clause;
        Log.d("whereErr", "test 5");
        //видалити з бази
        SQLSetFits sqlSetFits = new SQLSetFits(context);
        sqlSetFits.getWritableDatabase().execSQL(sql, where_args);
        sqlSetFits.close();
        Log.d("whereErr", "test 6");


        //видалити з адаптера
        setTrainingList.remove(position);
        //повідомити адаптер про видалення
        notifyItemRemoved(position);
    }


    //*******Холдер елементыв *************
    public class SetTrainingHolder extends RecyclerView.ViewHolder {

        //біндер для елементів лайаута ітема рецикла в'ю
        TextForRecyclerBinding binding = TextForRecyclerBinding.bind(itemView);

        //конструктор
        public SetTrainingHolder(@NonNull View itemView) {
            super(itemView);



            //встановити слухач натискань на елемент в'ю
            itemView.setOnClickListener(v -> {
                /*
                 * метод getAbsoluteAdapterPosition() повертає позицію елемента
                 * в адаптері, з урахуванням переміщення елементів у списку,
                 *  тоді як getBindingAdapterPosition() повертає позицію елемента в
                 * адаптері, не залежно від того, чи був виконаний переміщення
                 * елементів у списку. Тому, якщо ви використовуєте функціональність
                 * переміщення елементів у списку, використовуйте метод getAbsoluteAdapterPosition(), а якщо не
                 * використовуєте, використовуйте getBindingAdapterPosition().
                 * */
                int position = getAbsoluteAdapterPosition();
                //потрібно перевірити чи ідентифіковано OnItemRecyclerClickListener listener;
                //а також чи є позиція натисненого елементу
                if (listenerShort != null & RecyclerView.NO_POSITION != position) {
                    // тепер я викликаю метод натиснення на своєму слухачеві і передаю елемент SetTraining
                    listenerShort.onItemClick(position);
                }
            });

            //встановити слухач Long натискань на елемент в'ю
            itemView.setOnLongClickListener(v -> {
                //позиція
                int position = getAbsoluteAdapterPosition();
                //реєстр контекстного меню
                v.setOnCreateContextMenuListener((menu, v1, menuInfo) -> {
                    // Встановлюємо заголовок контекстного меню
                    menu.setHeaderTitle("Опції");
                    // Заповнюємо контекстне меню пунктами
                    ((Activity)context).getMenuInflater().inflate(R.menu.menu_for_recycler_viev, menu);
                });

                //Якщо контекст це МейнАктівіті (краще використати інтерфейс)
                if (context instanceof MainActivity) {
                    MainActivity child = (MainActivity) context;
                    //Задаєш позицію
                    child.setPositioContextMenu(position);
                } else Toast.makeText(context, "Error! MainActivity renamed!", Toast.LENGTH_SHORT).show();

                //показати контекстне меню
                v.showContextMenu();

                return true;
            });
        }

        void onBindData(Fitness fitness){
            binding.textView.setText(fitness.getDay());
        }

    }

}
