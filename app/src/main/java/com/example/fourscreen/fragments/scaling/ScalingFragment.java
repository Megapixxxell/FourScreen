package com.example.fourscreen.fragments.scaling;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourscreen.R;

public class ScalingFragment extends Fragment {

    public ScalingFragment() {
    }

    public static ScalingFragment newInstance() {
        ScalingFragment fragment = new ScalingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scaling, container, false);
    }

}
