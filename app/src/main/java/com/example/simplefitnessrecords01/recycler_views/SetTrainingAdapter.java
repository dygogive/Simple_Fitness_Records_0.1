package com.example.simplefitnessrecords01.recycler_views;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.GetterDB;
import com.example.simplefitnessrecords01.UI_activities.MainActivity;
import com.example.simplefitnessrecords01.fitness.SetFit;
import com.example.simplefitnessrecords01.fitness.TrainingFitness;
import com.example.simplefitnessrecords01.databinding.TextForRecyclerBinding;
import com.example.simplefitnessrecords01.sql.MyDatabaseHelper;

import java.util.List;

public class SetTrainingAdapter extends RecyclerView.Adapter<SetTrainingAdapter.SetTrainingHolder> {


    //контекст
    private Context context;

    //Список ітемів - тренувань
    private List<TrainingFitness> setTrainingList;
    public void setSetTrainingList(List<TrainingFitness> setTrainingList) {
        this.setTrainingList = setTrainingList;
    }



    //слухач натискань на елементи, якого я створив для того, щоб оброблювати натискання на елементи
    private OnItemRecyclerClickListener listenerShort;

    //метод для встановлення слухача натискань на елементи Рециклера
    public void setOnItemRecyclerClickListener(OnItemRecyclerClickListener listener) {
        this.listenerShort = listener;
    }



    //конструктори
    public SetTrainingAdapter(List<TrainingFitness> setTrainingList, Context context) {
        this.setTrainingList = setTrainingList;
        this.context = context;
    }
    public SetTrainingAdapter(Context context) {
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
    public void onBindViewHolder(@NonNull SetTrainingAdapter.SetTrainingHolder holder, int position) {
        //отримати елемент списку
        TrainingFitness setTraining = setTrainingList.get(position);
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
        String table_name = MyDatabaseHelper.DATABASE_TABLE;
        String where_clause = MyDatabaseHelper.COLUMN_DAY + "=?";
        String[] where_args = new String[] { setTrainingList.get(position).getDay() };
        String sql = "DELETE FROM " + table_name + " WHERE " + where_clause;

        //видалити з бази
        getterDB.getDB().execSQL(sql, where_args);
        getterDB.getDB().close();

        //видалити з адаптера
        setTrainingList.remove(position);

        //повідомити адаптер про видалення
        notifyItemRemoved(position);
    }


    //*******Холдер елементыв *************
    public class SetTrainingHolder extends RecyclerView.ViewHolder {

        //біндер для елементів лайаута ітема рецикла в'ю
        TextForRecyclerBinding binding = TextForRecyclerBinding.bind(itemView);

        //Унікальний ідентифікатор
        String unicID;

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
                    listenerShort.onItemClick(position, unicID);
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
                    child.setPosition(position);
                } else Toast.makeText(context, "Error! MainActivity renamed!", Toast.LENGTH_SHORT).show();

                //показати контекстне меню
                v.showContextMenu();

                return true;
            });
        }

        void onBindData(TrainingFitness trainingFitness){
            binding.textView.setText(trainingFitness.getDay());
            unicID = trainingFitness.getUnicID();
        }

    }

}
