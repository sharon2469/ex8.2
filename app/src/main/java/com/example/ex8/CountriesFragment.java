package com.example.ex8;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class CountriesFragment extends Fragment  {

    private RecyclerView recyclerView;
    private CountriesAdapter countriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        // lab 9
        boolean checkBoxFilter =  PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("remember", false);


        countriesAdapter = new CountriesAdapter(getActivity().getApplication(), getContext(), getActivity(), checkBoxFilter); // create an instance of the adapter
        recyclerView.setAdapter(countriesAdapter); // set that adapter for the recycle view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // What is the position of the list vertical or linear

    }

    //the interface of this fragment that include the methods
    public interface CountriesFragmentListener{
        //put here methods you want to utilize to communicate with the hosting activity
    }
}

