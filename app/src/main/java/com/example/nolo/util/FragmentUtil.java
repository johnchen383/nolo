package com.example.nolo.util;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtil {
    /**
     * Add fragment on top of the current fragment
     *
     * @param activity
     * @param oldFragmentContainer
     * @param newFragment
     * @param tag
     */
    public static void addFragment(FragmentActivity activity, int oldFragmentContainer, Class<? extends androidx.fragment.app.Fragment> newFragment, String tag) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.add(oldFragmentContainer, newFragment, null, tag)
                .addToBackStack(newFragment.getName());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    /**
     * Undo the addFragment(),
     * so basically pop/remove the top fragment, and goes back to previous fragment
     *
     * @param activity
     * @param fragmentName
     */
    public static void popFragment(FragmentActivity activity, String fragmentName) {
        FragmentManager fm = activity.getSupportFragmentManager();
        fm.popBackStack(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
