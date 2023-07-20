package com.example.simplefitnessrecords01.recycler_adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.MainActivity;
import com.example.simplefitnessrecords01.databinding.RecyclerMainActivityBinding;
import com.example.simplefitnessrecords01.fitness.OneGym;

import java.util.List;

public class AdapterRecyclerFitnessTrainings extends RecyclerView.Adapter<AdapterRecyclerFitnessTrainings.SetTrainingHolder> {

    private final Context context;





    //List of exercises
    private List<OneGym> setOneGymList;

    /* SETTER AND GETTER this exercise list */
    public void setFitnessList(List<OneGym> setOneGymList) {
        this.setOneGymList = setOneGymList;
    }
    public List<OneGym> getSetTrainingList() {
        return setOneGymList;
    }

    public OneGym getItem(int position) {
        return setOneGymList.get(position);
    }
    public void setItem(OneGym oneGym) {
        setOneGymList.add(oneGym);
    }
    //returning the number of items displayed by the Recycler
    @Override
    public int getItemCount() {
        //розмір елементів
        return setOneGymList.size();
    }




    //element click listener
    private OnItemRecyclerClickListener onItemShortClickListener;

    //method to initialize the click listener
    public void setOnItemRecyclerClickListener(OnItemRecyclerClickListener listener) {
        this.onItemShortClickListener = listener;
    }





    //Constructor
    public AdapterRecyclerFitnessTrainings(List<OneGym> setOneGymList, Context context) {
        this.setOneGymList = setOneGymList;
        this.context = context;
    }





    //method creates view holders
    @NonNull
    @Override
    public SetTrainingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creating a view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_main_activity, parent, false);
        //creating a SetsHolder
        return new SetTrainingHolder(view);
    }





    //the method fills the created views in the holders with information
    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerFitnessTrainings.SetTrainingHolder holder, int position) {
        //get a list item
        OneGym setOneGym = setOneGymList.get(position);
        //throw information from it into the holder
        holder.onBindData(setOneGym);
    }




    /*******************   RecyclerView.ViewHolder *********************/
    public class SetTrainingHolder extends RecyclerView.ViewHolder {

        //text size
        String selectedTextSize = context.getString(R.string.default_text_size);


        //біндер для елементів лайаута ітема рецикла в'ю
        RecyclerMainActivityBinding binding = RecyclerMainActivityBinding.bind(itemView);

        //конструктор
        public SetTrainingHolder(@NonNull View itemView) {
            super(itemView);

            //set size of text
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            selectedTextSize = preferences.getString("text_size_preference", context.getString(R.string.default_text_size));


            //set a short-click listener on the view element
            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                /* you need to check if the OnItemRecyclerClickListener listener is identified,
                 and if the position of the clicked item is */
                if (onItemShortClickListener != null & RecyclerView.NO_POSITION != position) {
                    // now I call the click method on my listener and pass the SetTraining element
                    onItemShortClickListener.onItemClick(position);
                }
            });



            //set the listener for Long clicks on the view element
            itemView.setOnLongClickListener(v -> {
                //position
                int position = getAbsoluteAdapterPosition();
                //context menu registry
                v.setOnCreateContextMenuListener((menu, v1, menuInfo) -> {
                    // We fill the context menu with items
                    ((Activity)context).getMenuInflater().inflate(R.menu.context_main_activity, menu);
                });




                //If the context is MainActivity
                if (context instanceof MainActivity) {
                    MainActivity child = (MainActivity) context;
                    //You set a position
                    child.setPositioContextMenu(position);
                } else Toast.makeText(context, "Error! MainActivity renamed!", Toast.LENGTH_SHORT).show();



                //show the context menu
                v.showContextMenu();

                return true;
            });
        }


        // fill the item holder item with information
        void onBindData(OneGym oneGym){
            binding.textView. setText(oneGym.getDay());
            binding.tvName.   setText(oneGym.getName());
            binding.tvSubName.setText(oneGym.getInfo());

            //set size of text
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String selectedTextSize = preferences.getString("text_size_preference", context.getString(R.string.default_text_size));
            binding.textView. setTextSize(Float.parseFloat(selectedTextSize));
            binding.tvName.   setTextSize(Float.parseFloat(selectedTextSize));
            binding.tvSubName.setTextSize(Float.parseFloat(selectedTextSize));
        }

    }


}
