package com.metinsaritas.phocom;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metinsaritas.phocom.Types.FragmentWithTitle;

public class MainCreateFragment extends FragmentWithTitle {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_create, container, false);
    }

}
