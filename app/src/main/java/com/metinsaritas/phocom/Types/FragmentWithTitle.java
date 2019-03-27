package com.metinsaritas.phocom.Types;

import android.support.v4.app.Fragment;

public class FragmentWithTitle extends Fragment {
    protected String title;

    public FragmentWithTitle setTitle (String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }
}
