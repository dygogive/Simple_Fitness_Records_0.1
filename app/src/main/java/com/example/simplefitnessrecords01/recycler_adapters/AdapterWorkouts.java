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
import com.example.simplefitnessrecords01.fitness.Workout;

import java.util.List;

public class AdapterWorkouts extends RecyclerView.Adapter<AdapterWorkouts.WorkoutHolder> {

    private final Context context;

    //List of trainings
    private List<Workout> workoutList;



    /* SETTER AND GETTER this exercise list */
    public void    setWorkoutList (List<Workout> trainings1) {
        workoutList = trainings1;
    }
    public List<Workout> getWorkoutList () {
        return workoutList;
    }

    public Workout getWorkout(int position) {
        return workoutList.get(position);
    }
    public void    addWorkout(Workout workout) {
        workoutList.add(workout);
    }
    //returning the number of items displayed by the Recycler
    @Override
    public int getItemCount() {
        //розмір елементів
        return workoutList.size();
    }




    //element click listener
    private OnWorkoutClickListener onItemShortClickListener;

    //method to initialize the click listener
    public void setOnItemRecyclerClickListener(OnWorkoutClickListener listener) {
        this.onItemShortClickListener = listener;
    }





    //Constructor
    public AdapterWorkouts(List<Workout> workoutList, Context context) {
        this.workoutList = workoutList;
        this.context = context;
    }





    //method creates view holders
    @NonNull
    @Override
    public WorkoutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creating a view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_main_activity, parent, false);
        //creating a SetsHolder
        return new WorkoutHolder(view);
    }





    //the method fills the created views in the holders with information
    @Override
    public void onBindViewHolder(@NonNull WorkoutHolder holder, int position) {
        //get a list item
        Workout workout = workoutList.get(position);
        //throw information from it into the holder
        holder.onBindData(workout);
    }




    /*******************   RecyclerView.ViewHolder *********************/
    public class WorkoutHolder extends RecyclerView.ViewHolder {

        //text size
        String selectedTextSize = context.getString(R.string.default_text_size);


        //біндер для елементів лайаута ітема рецикла в'ю
        RecyclerMainActivityBinding binding = RecyclerMainActivityBinding.bind(itemView);

        //конструктор
        public WorkoutHolder(@NonNull View itemView) {
            super(itemView);

            //set size of text
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            selectedTextSize = preferences.getString("text_size_preference", context.getString(R.string.default_text_size));


            //set a short-click listener on the view element
            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                /* you need to check if the OnWorkoutClickListener listener is identified,
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
        void onBindData(Workout workout){
            binding.textView. setText(workout.getDay());
            binding.tvName.   setText(workout.getName());
            binding.tvSubName.setText(workout.getInfo());

            //set size of text
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String selectedTextSize = preferences.getString("text_size_preference", context.getString(R.string.default_text_size));
            binding.textView. setTextSize(Float.parseFloat(selectedTextSize));
            binding.tvName.   setTextSize(Float.parseFloat(selectedTextSize));
            binding.tvSubName.setTextSize(Float.parseFloat(selectedTextSize));
        }

    }


}
