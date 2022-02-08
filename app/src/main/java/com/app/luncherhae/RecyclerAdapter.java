package com.app.luncherhae;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<Article> list;

    public RecyclerAdapter(Context context, List<Article> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customprefrence, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article article = list.get(position);
        holder.descriptionTextView.setText("Description: " + article.getCityDescription());
        holder.temperatureTextView.setText("Temperature: " + article.getCityTemperature());
        holder.countryTextView.setText("Country: " + article.getCityCountry());
        holder.cityTextView.setText("City: " + article.getCityName());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cityTextView,countryTextView, temperatureTextView, descriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            cityTextView = itemView.findViewById(R.id.cityID);
            countryTextView = itemView.findViewById(R.id.countryID);
            temperatureTextView = itemView.findViewById(R.id.temperatureID);
            descriptionTextView = itemView.findViewById(R.id.descriptionID);
        }
    }
}