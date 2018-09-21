package com.example.francoislf.go4lunch.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.francoislf.go4lunch.R;
import com.example.francoislf.go4lunch.models.User;

import java.util.List;

public class IsJoiningAdapter extends RecyclerView.Adapter<IsJoiningHolder> {

    private List<User> mUserList;

    public IsJoiningAdapter(List<User> userList) {
        mUserList = userList;
    }

    @NonNull
    @Override
    public IsJoiningHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_workmates_item, parent, false);

        return new IsJoiningHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IsJoiningHolder holder, int position) {
        holder.updateAvatarIsJoining(this.mUserList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mUserList.size();
    }
}
