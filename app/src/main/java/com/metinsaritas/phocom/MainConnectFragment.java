package com.metinsaritas.phocom;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metinsaritas.phocom.Types.FragmentWithTitle;

public class MainConnectFragment extends FragmentWithTitle {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_connect, container, false);
    }

}
