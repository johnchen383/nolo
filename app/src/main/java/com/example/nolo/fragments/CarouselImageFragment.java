package com.example.nolo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nolo.R;

public class CarouselImageFragment extends Fragment {
    private String uri;

    public CarouselImageFragment(String uri) {
        super(R.layout.carousel_img);
        this.uri = uri;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView img = getView().findViewById(R.id.img);

        int i = getActivity().getResources().getIdentifier(
                uri, "drawable",
                getActivity().getPackageName());

        img.setImageResource(i);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        ViewGroup rootView = (ViewGroup) inflater.inflate(
//                R.layout.carousel_img, container, false);
//
////
////
//
//
//        return rootView;
//    }
}
