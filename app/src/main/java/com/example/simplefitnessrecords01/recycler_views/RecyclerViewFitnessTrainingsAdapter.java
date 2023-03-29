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
import com.example.simplefitnessrecords01.UI_activities.MainActivity;
import com.example.simplefitnessrecords01.fitness.OneFitnessTraining;
import com.example.simplefitnessrecords01.databinding.TextForRecyclerBinding;

import java.util.List;

public class RecyclerViewFitnessTrainingsAdapter extends RecyclerView.Adapter<RecyclerViewFitnessTrainingsAdapter.SetTrainingHolder> {

    private final Context context;




    //List of exercises
    private List<OneFitnessTraining> setTrainingList;

    /* SETTER AND GETTER this exercise list */
    public void setFitnessList(List<OneFitnessTraining> setTrainingList) {
        this.setTrainingList = setTrainingList;
    }
    public List<OneFitnessTraining> getSetTrainingList() {
        return setTrainingList;
    }

    public OneFitnessTraining getItem(int position) {
        return setTrainingList.get(position);
    }
    public void setItem(OneFitnessTraining oneFitnessTraining) {
        setTrainingList.add(oneFitnessTraining);
    }
    //returning the number of items displayed by the Recycler
    @Override
    public int getItemCount() {
        //розмір елементів
        return setTrainingList.size();
    }




    //element click listener
    private OnItemRecyclerClickListener onItemShortClickListener;

    //method to initialize the click listener
    public void setOnItemRecyclerClickListener(OnItemRecyclerClickListener listener) {
        this.onItemShortClickListener = listener;
    }





    //Constructor
    public RecyclerViewFitnessTrainingsAdapter(List<OneFitnessTraining> setTrainingList, Context context) {
        this.setTrainingList = setTrainingList;
        this.context = context;
    }





    //method creates view holders
    @NonNull
    @Override
    public SetTrainingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //creating a view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_for_recycler, parent, false);
        //creating a SetsHolder
        return new SetTrainingHolder(view);
    }





    //the method fills the created views in the holders with information
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewFitnessTrainingsAdapter.SetTrainingHolder holder, int position) {
        //get a list item
        OneFitnessTraining setTraining = setTrainingList.get(position);
        //throw information from it into the holder
        holder.onBindData(setTraining);
    }




    //*******   RecyclerView.    ViewHolder    *************
    public class SetTrainingHolder extends RecyclerView.ViewHolder {

        //біндер для елементів лайаута ітема рецикла в'ю
        TextForRecyclerBinding binding = TextForRecyclerBinding.bind(itemView);

        //конструктор
        public SetTrainingHolder(@NonNull View itemView) {
            super(itemView);




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
                    ((Activity)context).getMenuInflater().inflate(R.menu.menu_for_recycler_viev, menu);
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
        void onBindData(OneFitnessTraining oneFitnessTraining){
            binding.textView.setText(oneFitnessTraining.getDay());
        }

    }

}
