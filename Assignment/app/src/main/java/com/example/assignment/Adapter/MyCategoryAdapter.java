package com.example.assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.assignment.Model.CategoryModel;
import com.example.assignment.R;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;

public class MyCategoryAdapter extends RecyclerView.Adapter<MyCategoryAdapter.MyViewHolder> {

    private Context context;
    private List<CategoryModel> categoryModelList;

    public MyCategoryAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    public MyCategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(categoryModelList.get(position).getImage()).into(holder.category_image);
        holder.txt_category_name.setText(new StringBuilder(categoryModelList.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        KenBurnsView category_image;
        TextView txt_category_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            category_image = itemView.findViewById(R.id.category_image);
            txt_category_name = itemView.findViewById(R.id.txt_category_name);
        }
    }
}
