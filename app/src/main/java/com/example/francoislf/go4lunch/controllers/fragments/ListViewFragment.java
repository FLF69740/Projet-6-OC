package com.example.francoislf.go4lunch.controllers.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.Views.RestaurantAdapter;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment {

    @BindView(R.id.fragment_list_view_recycler_view)RecyclerView mRecyclerView;

    private List<RestaurantProfile> mRestaurantProfileList;
    private RestaurantAdapter mAdapter;

    public ListViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView();
        return view;
    }


    public void setRestaurantProfileList(List<RestaurantProfile> restaurantProfileList){
        this.mRestaurantProfileList.addAll(restaurantProfileList);
        mAdapter.notifyDataSetChanged();
    }

    private void configureRecyclerView(){
        this.mRestaurantProfileList = new ArrayList<>();
        this.mAdapter = new RestaurantAdapter(this.mRestaurantProfileList);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}
