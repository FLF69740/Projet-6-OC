package com.example.francoislf.go4lunch.controllers.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.Utils.ItemClickSupport;
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
    private View mView;

    public static ListViewFragment newInstanceWithList(List<RestaurantProfile> restaurantProfileList){
        ListViewFragment listViewFragment = new ListViewFragment();
        Bundle args = new Bundle();
        args.putSerializable("list", (Serializable) restaurantProfileList);
        listViewFragment.setArguments(args);
        return listViewFragment;
    }

    private OnClickedResultItem mCallback;

    public interface OnClickedResultItem{
        void onResultItemTransmission(View view, String title);
    }

    public ListViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, mView);
        this.mRestaurantProfileList = new ArrayList<>();
        mRestaurantProfileList = (List<RestaurantProfile>) getArguments().getSerializable("list");
        if (mRestaurantProfileList.isEmpty()) this.setRestaurantProfileList(mRestaurantProfileList);
        this.configureRecyclerView();
        this.configureOnClickRecyclerView();
        return mView;
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

    // Configure item click on RecyclerView
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_list_view_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        RestaurantProfile restaurantProfile = mAdapter.getRestaurantProfile(position);
                        mCallback.onResultItemTransmission(mView, restaurantProfile.getName());
                    }
                });
    }

    //Parent activity will automatically subscribe to callback
    private void createCallbackToParentActivity(){
        try {
            mCallback = (OnClickedResultItem) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnClickedResultMarker");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }

}
