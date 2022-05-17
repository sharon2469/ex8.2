package com.example.ex8;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;



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
/** *
 * https://developer.android.com/topic/libraries/architecture/livedata
 */

/***
 * https://developer.android.com/topic/libraries/architecture/viewmodel.html#sharing
 */
        detailsTextView = view.findViewById(R.id.country_details_text_view);
        myViewModel = MainViewModel.getInstance(getActivity().getApplication());

        // OBSERVE
        Observer<Country> userListUpdateObserver = new Observer<Country>() {
            @Override
            public void onChanged(Country country) {
                if(country != null){
                    Log.i("TEST load", "da");
                    detailsTextView.setText(country.getDetails());
                }else{
                    detailsTextView.setText("");

                }

            }
        };

        // OBSERVE
        Observer<Integer> indexObserve = new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                Log.i("TEST load", "da");
                if(index == -1 ){
                    //detailsTextView.setText("");
                }
            }
        };

        myViewModel.getItemSelected().observe(getViewLifecycleOwner(), userListUpdateObserver);
        myViewModel.getPositionSelected().observe(getViewLifecycleOwner(), indexObserve);

        super.onViewCreated(view, savedInstanceState);

    }

}
