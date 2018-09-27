package com.example.francoislf.go4lunch.controllers.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.example.francoislf.go4lunch.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_NAME;
import static com.example.francoislf.go4lunch.controllers.activities.BaseActivity.USER_UID;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    View mView;
    @BindView(R.id.profile_information)TextView mTextViewProfilInformation;
    @BindView(R.id.switch_notification)Switch mSwitchNotification;
    private static final String SWITCH_STATE = "SWITCH_STATE";


    private OnClickObjectFragmentSettings mCallback;

    public interface OnClickObjectFragmentSettings{
        void OnClickButtonDeleteUserAccount(View view);
        void OnClickButtonSwitchNotifications(View view, boolean bool);
    }

    //Parent activity will automatically subscribe to callback
    private void createCallbackToParentActivity(){
        try {mCallback = (SettingsFragment.OnClickObjectFragmentSettings) getActivity();}
        catch (ClassCastException e) {throw new ClassCastException(e.toString()+ " must implement OnClickedResultMarker");}
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }

    public SettingsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, mView);
        this.loadUserInformations(getArguments().getString(USER_NAME), getArguments().getString(USER_UID));
        this.switchSysteme();
        return mView;
    }

    @OnClick (R.id.delete_account) public void deleteUserAccount(){
        mCallback.OnClickButtonDeleteUserAccount(mView);
    }

    // Load User Informations account
    private void loadUserInformations(String username, String uid){
        String informations = mView.getContext().getString(R.string.settings_name) + " " + username + "\nID : " + uid;
        mTextViewProfilInformation.setText(informations);
    }

    // create listener for switchButton Notification
    private void switchSysteme(){
        if (getArguments().getString(SWITCH_STATE) != null) mSwitchNotification.setChecked(Boolean.valueOf(getArguments().getString(SWITCH_STATE)));
        mSwitchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCallback.OnClickButtonSwitchNotifications(mView, mSwitchNotification.isChecked());
            }
        });
    }

}
