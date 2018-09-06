package com.example.francoislf.go4lunch.controllers.fragments;


import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.Views.RestaurantAdapter;
import com.example.francoislf.go4lunch.models.RestaurantProfile;

import java.io.Serializable;
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

    public static ListViewFragment newInstanceWithList(List<RestaurantProfile> restaurantProfileList){
        ListViewFragment listViewFragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putSerializable("list", (Serializable) restaurantProfileList);
        listViewFragment.setArguments(args);
        return listViewFragment;
    }

    public ListViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);
        this.mRestaurantProfileList = new ArrayList<>();
        mRestaurantProfileList = (List<RestaurantProfile>) getArguments().getSerializable("list");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toast.makeText(getContext(), mRestaurantProfileList.get(0).getName() + " : " +
            mRestaurantProfileList.get(0).getWeekHour().get(0), Toast.LENGTH_LONG).show();
        }
        if (mRestaurantProfileList.isEmpty()) this.setRestaurantProfileList(mRestaurantProfileList);
        this.configureRecyclerView();
        return view;
    }


    public void setRestaurantProfileList(List<RestaurantProfile> restaurantProfileList){
        this.mRestaurantProfileList.addAll(restaurantProfileList);
    }

    private void configureRecyclerView(){

        this.mAdapter = new RestaurantAdapter(this.mRestaurantProfileList);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.notifyDataSetChanged();
    }

}
