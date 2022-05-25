package com.example.ex8;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder> {

    private static ArrayList<Country> countryList;
    private int selectedRow = -1;
    private MainViewModel myViewModel;
    private Application Mycontext;
    private  ICountriesAdapterListener listener;
    private CountriesViewHolder viewHolder;
    private Context context;


    public CountriesAdapter(Application application, Context context, Activity activity, boolean checkBoxFilter) {
        //countryList = CountryXMLParser.parseCountries(application);
        Mycontext = application;
        myViewModel = MainViewModel.getInstance(application, Mycontext, activity, checkBoxFilter);
        countryList = myViewModel.getCountriesLiveData().getValue();
        this.context = context;
    }


    @NonNull
    @Override
    public CountriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        listener = (ICountriesAdapterListener)parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()); // instance of the inflater
        View countryView = inflater.inflate(R.layout.country_item, parent, false); // get view of the country view object
        viewHolder = new CountriesViewHolder(countryView);

        return viewHolder; // return county view holder
    }


    // this call by recyclerView for each row in in view
    @Override
    public void onBindViewHolder(@NonNull CountriesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Country country = countryList.get(position);

        // OBSERVE
        // Here we will observe and update the selected row
        Observer<Integer> observeSelectedIndex = new Observer<Integer>() {
            @Override
            public void onChanged(Integer index) {
                selectedRow = index;
            }
        };

        myViewModel.getPositionSelected().observe((LifecycleOwner)context, observeSelectedIndex);


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
                myViewModel.setPositionSelected(selectedRow); // Lab 8
                listener.countryClicked(); // This what will open the frag from the MainActivity listener
            }
        });

        //Long Press
        holder.row_linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                System.out.println(getItemCount());

                boolean removeList =  PreferenceManager.getDefaultSharedPreferences(Mycontext)
                        .getBoolean("remember", false);
                if(removeList)
                {
                    myViewModel.setRemoveListBySP(String.valueOf(position));
                      //myViewModel.setRemoveListByFile(String.valueOf(position));

                }

                // ************ lab 8
                countryList.remove(position);

                myViewModel.setCountryLiveData(countryList);

                // Here we do some logic
                // if the position equals to the current selected row so we need to unselected completely the selected row
                if(position == myViewModel.getPositionSelected().getValue()){
                    myViewModel.setPositionSelected(-1);
                }

                if(position < myViewModel.getPositionSelected().getValue()){
                    myViewModel.setPositionSelected(myViewModel.getPositionSelected().getValue()-1);
                }


                notifyDataSetChanged();
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

        public LinearLayout getRow(){
            return row_linearLayout;
        }
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
