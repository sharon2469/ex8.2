package com.example.ex8;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder> {

    private static ArrayList<Country> countryList;
    private int selectedRow = -1;
    private MainViewModel viewModel;
    private Application Mycontext;
    private  ICountriesAdapterListener listener;


    public CountriesAdapter(Application application) {
        countryList = CountryXMLParser.parseCountries(application);
        Mycontext = application;
        viewModel=MainViewModel.getInstance(Mycontext);
        countryList = viewModel.getCountriesLiveData().getValue();
    }


    @NonNull
    @Override
    public CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        listener = (ICountriesAdapterListener)parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()); // instance of the inflater
        View countryView = inflater.inflate(R.layout.country_item, parent, false); // get view of the country view object
        return new CountriesViewHolder(countryView); // return county view holder
    }


    // this call by recyclerView for each row in in view
    @Override
    public void onBindViewHolder(@NonNull CountriesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Country country = countryList.get(position);

        if (selectedRow == position){
            holder.row_linearLayout.setBackgroundColor(Color.parseColor("#03dffc"));
        }else{
            holder.row_linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.row_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRow = position;
                notifyItemChanged(selectedRow);
                notifyDataSetChanged();
                viewModel.setItemSelect(country);
                listener.countryClicked();
            }
        });

        //Long Press
        holder.row_linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                System.out.println(getItemCount());
                countryList.remove(position);
                viewModel.setItemSelect(null); // ******* NEED TO FIX THIS -> clear the info to empty
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, countryList.size());
                viewModel.setCountryLiveData(countryList);

                if (position < selectedRow){
                    selectedRow--;
                    notifyItemChanged(selectedRow);
                    notifyDataSetChanged();
                } else if (position == selectedRow){
                    selectedRow = -1;
                    notifyItemChanged(selectedRow);
                    notifyDataSetChanged();
                }

                return true;
            }
        });
        holder.bindData(country.getName(),country.getShorty(),country.getFlag());
    }

    // This function just tell to the recycler view how many items in the data
    @Override
    public int getItemCount() {
        return countryList.size();
    }



    // Each row in RecyclerView will get reference of this CountriesViewHolder
    // *** Include the function that can remove the row
    public class CountriesViewHolder extends RecyclerView.ViewHolder
    {
        private final Context   context;
        private final View      countryItem;
        private final ImageView flagImageView;
        private final TextView  nameTextView;
        private final TextView  populationTextView;
        private LinearLayout row_linearLayout;

        public CountriesViewHolder(@NonNull View itemView) {
            super(itemView);
            context             = itemView.getContext();
            countryItem         = itemView.findViewById(R.id.country_item);
            flagImageView       = itemView.findViewById(R.id.flagImageView);
            nameTextView        = itemView.findViewById(R.id.nameTextView);
            populationTextView  = itemView.findViewById(R.id.populationTextView);
            row_linearLayout    = itemView.findViewById(R.id.country_item);
        }

        //******** This function bind\connect the row widgets with the data
        public void bindData(String name,String shorty, String flag){
            nameTextView.setText(name);
            populationTextView.setText(shorty);
            flagImageView.setImageResource(getDrawableId(context, flag));
        }

    }

    private static int getDrawableId(Context context, String drawableName) {
        Resources resources = context.getResources();
       return resources.getIdentifier(drawableName, "drawable", context.getPackageName());
    }

    public interface ICountriesAdapterListener {
        void countryClicked();
    }
}
