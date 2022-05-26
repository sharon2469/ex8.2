package com.example.ex8;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private static MainViewModel instance;

    public Context context;
    public Activity activity;

    // ******* The observable vars *********************

    //  The MutableLiveData class has the setValue(T) and postValue(T)
    //  methods publicly and you must use these if you need to edit the value stored in a LiveData object.
    //  Usually, MutableLiveData is used in the ViewModel and then the ViewModel only exposes

    private MutableLiveData<ArrayList<Country>>  countryLiveData ;
    private MutableLiveData<Integer> positionSelected;
    // *****************************
    // lab 9
    private MutableLiveData<Boolean> saveRemoved;
    private MutableLiveData<ArrayList<String>> removedCountries;
    private int[] intArr;
    private ArrayList<String> removedCountryList = new ArrayList<>();




    public MainViewModel(@NonNull Application application, Context context, Activity activity, boolean checkBoxFilter) {
        super(application);
        //super(application);
        // call your Rest API in init method
        this.activity = activity;
        this.context = context;

        // Lab 8 + Lab 9
        init(application, checkBoxFilter);


    }

    public MutableLiveData<ArrayList<Country>>  getCountryLiveData() {
        return countryLiveData;
    }

    public LiveData<ArrayList<Country>> getCountriesLiveData() {
        return countryLiveData;
    }


    public void setPositionSelected(Integer index){
        positionSelected.setValue(index);
    }

    public MutableLiveData<Integer> getPositionSelected(){
        return positionSelected;
    }

    public void setCountryLiveData(ArrayList<Country> list){
        countryLiveData.setValue(list);
    }

    public boolean getSaveRemove(){
        return saveRemoved.getValue();
    }

    // Pay attention that MainViewModel is singleton it helps
    public static MainViewModel getInstance(Application application, Context context, Activity activity, boolean checkBoxFilter){
        if(instance ==null){
            instance = new MainViewModel(application, context, activity, checkBoxFilter);
        }
        return instance;
    }
    // This use the setValue
    public void init(Application application, boolean checkBoxFilter){
        countryLiveData = new MutableLiveData<>();
        positionSelected = new MutableLiveData<>();
        positionSelected.setValue(-1);



        // lab 9
        saveRemoved = new MutableLiveData<>();
        saveRemoved.setValue(checkBoxFilter);
        removedCountries = new MutableLiveData<>();
        checkRemoveList(application); // this is also connect to lab 8 and 9
    }



    // Lab 8 (only set the country list) + Lab 9 ( remove from original list the remove country)
    public void checkRemoveList(Application application){
        ArrayList<Country> countryList = CountryXMLParser.parseCountries(application);


        if(saveRemoved.getValue()) {

            String s = getRemoveListByFile();
            //String s = getRemoveListBySP();

            String[] removeArray = s.split(",",countryList.size());
                for(int i = 0; i < removeArray.length; i++) {
                    int finalI = i;
                    countryList.removeIf(obj -> obj.getName().equals(removeArray[finalI]));
                }
        }else{// clear files
            clearListByFile();
            //clearListBySP();
        }
        countryLiveData.setValue(countryList);


    }

    // ******************** file ************
    public String getRemoveListByFile() {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("remove.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int size = inputStream.available();
                char[] buffer = new char[size];

                inputStreamReader.read(buffer);

                inputStream.close();
                ret = new String(buffer);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public void setRemoveListByFile(String name)
    {
        if(!removedCountryList.contains(name)){
            String removelist = getRemoveListByFile();
            if(removelist.length() == 0)
                removelist = name;
            else{
                removelist += "," + name;
            }
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("remove.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(removelist);
                outputStreamWriter.flush();
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }

    }


    private void clearListByFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("remove.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }


    // ******************* SP **********************
    public void setRemoveListBySP(String name) {

        if (!removedCountryList.contains(name)) {
            String removelist = getRemoveListBySP();
            if (removelist.length() == 0)
                removelist = name;
            else
                removelist += "," + name;

            SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("removelist", removelist);
            editor.apply();

        }
    }

    public String getRemoveListBySP() {

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString("removelist", "");
    }

    private void clearListBySP() {

        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("removelist", "");
        editor.apply();
    }

}
