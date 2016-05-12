package com.droid.manasshrestha.rxandroid.splashscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droid.manasshrestha.rxandroid.R;

/**
 * Created by Manas on 5/12/2016.
 */
public class PermissionRequestFragment extends Fragment implements PageChangeListener {

    private static final String KEY_POSITION = "position";
    private int position;

    public static PermissionRequestFragment getInstance(int position) {
        PermissionRequestFragment firstScreenFragment = new PermissionRequestFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_POSITION, position);
        firstScreenFragment.setArguments(bundle);

        return firstScreenFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.third_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onPageChangeListener(int position) {

    }
}
