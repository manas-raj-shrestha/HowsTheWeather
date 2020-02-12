package com.howstheweather.splashscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.howstheweather.R;

/**
 * shows the permission required for app to function
 */
public class PermissionRequestFragment extends Fragment implements PageChangeListener {

    private static final String KEY_POSITION = "position";

    public static PermissionRequestFragment getInstance(int position) {
        PermissionRequestFragment firstScreenFragment = new PermissionRequestFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        firstScreenFragment.setArguments(bundle);

        return firstScreenFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.permission_request_fragment, container, false);
    }

    @Override
    public void onPageChangeListener(int position) {
        //there is no animation right now
    }
}
