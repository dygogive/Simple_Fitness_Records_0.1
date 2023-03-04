package com.example.simplefitnessrecords01.recycler_views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.SetFitRecyclerItemBinding;
import com.example.simplefitnessrecords01.fitness.SetFit;

import java.util.ArrayList;
import java.util.List;

public class AdapterSets extends RecyclerView.Adapter<AdapterSets.HolderSetFitList> {

    Context context;
    private List<SetFit> setFitList = new ArrayList<>();

    public void setSetFitList(List<SetFit> setFitList) {
        this.setFitList = setFitList;
    }

    public AdapterSets(Context context) {
        this.context = context;
    }

    public AdapterSets(Context context, List<SetFit> setFitList) {
        this.context = context;
        this.setFitList = setFitList;
    }

    @NonNull
    @Override
    public HolderSetFitList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_fit_recycler_item, parent, false);

        return new HolderSetFitList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderSetFitList holder, int position) {
        SetFit setFit = setFitList.get(position);
        holder.initItemView(setFit);
    }

    @Override
    public int getItemCount() {
        return setFitList.size();
    }

    public class HolderSetFitList extends RecyclerView.ViewHolder {

        SetFitRecyclerItemBinding binding = SetFitRecyclerItemBinding.bind(itemView);

        public HolderSetFitList(@NonNull View itemView) {
            super(itemView);
        }

        void initItemView(SetFit setFit){
            String exeName = setFit.getExe().toString();
            binding.tvExerciceName.setText(exeName);

            String weight = setFit.getRecordSet().getWeight().toString();
            binding.etWeight.setText(weight);

            String repeats = setFit.getRecordSet().getRepeats().toString();
            binding.etRepeat.setText(repeats);
        }


    }
}
