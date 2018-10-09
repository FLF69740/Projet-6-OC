package com.example.francoislf.go4lunch.Views;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class WorkmatesAdapter extends FirestoreRecyclerAdapter<User, WorkmatesViewHolder> {

    public interface Listener{
        void onDataChanged();
    }

    private Listener mCallback;

    public WorkmatesAdapter(@NonNull FirestoreRecyclerOptions<User> options, Listener callback) {
        super(options);
        mCallback = callback;
        startListening();
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position, @NonNull User model) {
        holder.updateWithWorkmatesProfile(model);
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkmatesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_workmates_item, parent, false));
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.mCallback.onDataChanged();
    }

    @NonNull
    @Override
    public User getItem(int position) {
        return super.getItem(position);
    }
}
