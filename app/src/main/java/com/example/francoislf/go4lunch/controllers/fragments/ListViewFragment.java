package com.example.francoislf.go4lunch.controllers.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.Utils.ItemClickSupport;
import com.example.francoislf.go4lunch.Views.RestaurantAdapter;
import com.example.francoislf.go4lunch.api.LikedHelper;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.models.ChoiceRestaurantCountdown;
import com.example.francoislf.go4lunch.models.RestaurantProfile;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.BLANK_ANSWER;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_DATE_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_HOUR_CHOICE;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_RESTAURANT_CHOICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment implements EventListener<QuerySnapshot> {

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

    // refresh the list of restaurant for the recyclerView creation
    public void setRestaurantProfileList(List<RestaurantProfile> restaurantProfileList){
        this.mRestaurantProfileList.addAll(restaurantProfileList);
    }

    // update recyclerView with autocomplete process
    public  void updateRecycler(){
        mAdapter.notifyDataSetChanged();
    }

    private void configureRecyclerView(){
        this.mAdapter = new RestaurantAdapter(this.mRestaurantProfileList);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.updateRecycler();
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

    @Override
    public void onPause() {
        super.onPause();
        UserHelper.getUsersCollection().addSnapshotListener(this); // Add a listener when database FireStore change
        LikedHelper.getLikedCollection().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                configureRecyclerView();
            }
        });
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (queryDocumentSnapshots != null) {
            for (int i = 0; i < mRestaurantProfileList.size(); i++) mRestaurantProfileList.get(i).setNumberOfParticipant(0);
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                if (!document.getString(USER_DATE_CHOICE).equals(BLANK_ANSWER)) {
                    if (!new ChoiceRestaurantCountdown(document.getString(USER_HOUR_CHOICE), document.getString(USER_DATE_CHOICE)).getCountdownResult()) {
                        for (int i = 0; i < mRestaurantProfileList.size(); i++) {
                            if (document.getString(USER_RESTAURANT_CHOICE).equals(mRestaurantProfileList.get(i).getName())) {
                                int newNumber = mRestaurantProfileList.get(i).getNumberOfParticipant() + 1;
                                mRestaurantProfileList.get(i).setNumberOfParticipant(newNumber);
                            }}}}}
            this.configureRecyclerView();
        }
    }


}
