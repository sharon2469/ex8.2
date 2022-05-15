package com.example.ex8;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class DetailsFragment extends Fragment {

    private TextView detailsTextView;
    private MainViewModel myViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details_fragment,container,false);
    }



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        detailsTextView = view.findViewById(R.id.country_details_text_view);
        myViewModel= MainViewModel.getInstance(getActivity().getApplication());
        Observer<Country> userListUpdateObserver = new Observer<Country>() {
            @Override
            public void onChanged(Country country) {
                detailsTextView.setText(country.getDetails());
            }
        };

        myViewModel.getItemSelected().observe(getViewLifecycleOwner(),userListUpdateObserver);

        super.onViewCreated(view, savedInstanceState);



    }

}
