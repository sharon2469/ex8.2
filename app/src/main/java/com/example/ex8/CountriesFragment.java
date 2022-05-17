package com.example.ex8;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CountriesFragment extends Fragment  {

    private RecyclerView recyclerView;
    CountriesFragmentListener listener; // hold the mainactivity referance
    private CountriesAdapter countriesAdapter;
    private Application application;
    private MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        application = getActivity().getApplication();
        return inflater.inflate(R.layout.countriesfrag, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)  {
        recyclerView = view.findViewById(R.id.recycle_view);
                super.onViewCreated(view, savedInstanceState);
    }

    //*******************************************************************
    // After activity created we can set the ADAPTER for the recycle view
    //*******************************************************************
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countriesAdapter = new CountriesAdapter(getActivity().getApplication(), getContext()); // create an instance of the adapter
        recyclerView.setAdapter(countriesAdapter); // set that adapter for the recycle view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // What is the position of the list vertical or linear

    }

    //the interface of this fragment that include the methods
    public interface CountriesFragmentListener{
        //put here methods you want to utilize to communicate with the hosting activity
    }
}

