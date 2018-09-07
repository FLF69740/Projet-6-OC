package com.example.francoislf.go4lunch.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<RestaurantProfile> mRestaurantProfiles;

    public RestaurantAdapter(List<RestaurantProfile> restaurantProfileList){
        this.mRestaurantProfiles = restaurantProfileList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_view_item, parent, false);

        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        holder.updateWithRestaurantProfile(this.mRestaurantProfiles.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mRestaurantProfiles.size();
    }

    public RestaurantProfile getRestaurantProfile(int position){
        return this.mRestaurantProfiles.get(position);
    }
}
