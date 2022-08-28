package com.example.nolo.util;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtil {

    public static void addFragment(FragmentActivity activity, int oldFragmentContainer, Class<? extends androidx.fragment.app.Fragment> newFragment, String tag) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(oldFragmentContainer, newFragment, null, tag)
                .addToBackStack(newFragment.getName());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public static void popFragment(FragmentActivity activity, String fragmentName) {
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.popBackStack(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
