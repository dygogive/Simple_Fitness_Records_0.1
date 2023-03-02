package com.example.simplefitnessrecords01.recycler_views;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.MainActivity;
import com.example.simplefitnessrecords01.fitness.SetTraining;
import com.example.simplefitnessrecords01.databinding.TextForRecyclerBinding;
import com.example.simplefitnessrecords01.sql.MyDatabaseHelper;

import java.lang.reflect.Method;
import java.util.List;

public class SetTrainingAdapter extends RecyclerView.Adapter<SetTrainingAdapter.SetTrainingHolder> {

    public void setSetTrainingList(List<SetTraining> setTrainingList) {
        this.setTrainingList = setTrainingList;
    }

    //Список ітемів
    private List<SetTraining> setTrainingList;

    //контекст
    private Context context;


    //слухач натискань на елементи, якого я створив для того, щоб оброблювати натискання на елементи
    private OnItemRecyclerClickListener listenerShort;



    //конструктор
    public SetTrainingAdapter(List<SetTraining> setTrainingList, Context context) {
        this.setTrainingList = setTrainingList;
        this.context = context;
    }


    //метод для встановлення слухача натискань на елементи Рециклера
    public void setOnItemRecyclerClickListener(OnItemRecyclerClickListener listener) {
        this.listenerShort = listener;
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
        SetTraining setTraining = setTrainingList.get(position);
        //закинути з нього інфу у холдер
        holder.binding.textView.setText(setTraining.getDay());
    }

    @Override
    public int getItemCount() {
        //розмір елементів
        return setTrainingList.size();
    }


    public void deleteItem(int position) {
        //взяти базу
        MyDatabaseHelper helper = new MyDatabaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        //код для видалення
        String table_name = MyDatabaseHelper.DATABASE_TABLE;
        String where_clause = MyDatabaseHelper.COLUMN_DAY + "=?";
        String[] where_args = new String[] { setTrainingList.get(position).getDay() };
        String sql = "DELETE FROM " + table_name + " WHERE " + where_clause;

        //видалити з бази
        db.execSQL(sql, where_args);
        db.close();
        //видалити з адаптера
        setTrainingList.remove(position);

        //повідомити адаптер про видалення
        notifyItemRemoved(position);


        Toast.makeText(context, "Видалити позицію: " + position, Toast.LENGTH_SHORT).show();
    }


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

                if (context instanceof MainActivity) {
                    MainActivity child = (MainActivity) context;
                    child.setPosition(position);
                } else Toast.makeText(context, "Error! MainActivity renamed!", Toast.LENGTH_SHORT).show();

                //показати контекстне меню
                v.showContextMenu();

                return true;
            });


        }



    }

}
