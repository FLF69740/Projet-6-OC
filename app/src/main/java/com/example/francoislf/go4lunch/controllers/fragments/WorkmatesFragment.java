package com.example.francoislf.go4lunch.controllers.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.Utils.ItemClickSupport;
import com.example.francoislf.go4lunch.Views.WorkmatesAdapter;
import com.example.francoislf.go4lunch.api.UserHelper;
import com.example.francoislf.go4lunch.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkmatesFragment extends Fragment implements WorkmatesAdapter.Listener {

    @BindView(R.id.workmates_recyclerview)RecyclerView mRecyclerView;
    @BindView(R.id.workmates_list_empty)TextView mListEmptyTextView;

    private WorkmatesAdapter mWorkmatesAdapter;
    private View mView;

    public static WorkmatesFragment newInstanceWorkmates(){
        WorkmatesFragment workmatesFragment = new WorkmatesFragment();
        return workmatesFragment;
    }

    private OnClickedAvatarItem mCallback;

    public interface OnClickedAvatarItem{
        void onResultAvatarTransmission(View view, String title);
    }

    public WorkmatesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, mView);
        this.configureRecyclerView();
        this.configureOnClickRecyclerView();
        return mView;
    }

    private void configureRecyclerView(){
        this.mWorkmatesAdapter = new WorkmatesAdapter(generateOptionsForAdapter(UserHelper.getAllUserForRecyclerViewWorkmates()), this);
        mWorkmatesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mWorkmatesAdapter.getItemCount());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
        mRecyclerView.setAdapter(this.mWorkmatesAdapter);
    }

    private FirestoreRecyclerOptions<User> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
    }

    @Override
    public void onDataChanged() {
        mListEmptyTextView.setVisibility(this.mWorkmatesAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    // Configure item click on RecyclerView
    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_list_view_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        User user = mWorkmatesAdapter.getItem(position);
                        mCallback.onResultAvatarTransmission(mView, user.getRestaurantChoice());
                    }
                });
    }

    //Parent activity will automatically subscribe to callback
    private void createCallbackToParentActivity(){
        try {
            mCallback = (WorkmatesFragment.OnClickedAvatarItem) getActivity();
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
