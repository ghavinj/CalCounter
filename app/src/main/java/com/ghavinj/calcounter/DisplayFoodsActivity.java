package com.ghavinj.calcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.CustomListViewAdapter;
import data.DatabaseHandler;
import model.Food;
import util.Util;

public class DisplayFoodsActivity extends AppCompatActivity {

    private DatabaseHandler dba;
    private ArrayList<Food> dbFoods = new ArrayList<>();
    private CustomListViewAdapter foodAdapter;
    private ListView listView;

    private Food myFood;
    private TextView totalCals, totalFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_foods);

        listView = (ListView)findViewById(R.id.list);
        totalCals = (TextView)findViewById(R.id.totalAmountTextView);
        totalFoods = (TextView)findViewById(R.id.totalItemsTextView);

        refreshData();
    }

    private void refreshData() {
        dbFoods.clear();

        dba = new DatabaseHandler(getApplicationContext());

        ArrayList<Food> foodsFromDB = dba.getFoods();

        int calsValue  = dba.totalCalories();
        int totalItems = dba.getTotalItems();

        String formattedValue = Util.formatNumber(calsValue);
        String formattedItems = Util.formatNumber(totalItems);

        totalCals.setText("Total Calories: " + formattedValue);
        totalFoods.setText("Total Foods: " + formattedItems);

        for (int i = 0; i < foodsFromDB.size(); i++){

            String name = foodsFromDB.get(i).getFoodName();
            String dateText = foodsFromDB.get(i).getRecordDate();
            int cals = foodsFromDB.get(i).getCalories();
            int foodId = foodsFromDB.get(i).getFoodId();

            Log.v("FOOD IDS: ", String.valueOf(foodId));

            myFood = new Food();
            myFood.setFoodName(name);
            myFood.setRecordDate(dateText);
            myFood.setCalories(cals);
            myFood.setFoodId(foodId);

            dbFoods.add(myFood);
        }
        dba.close();

        //Set up adapter
        foodAdapter = new CustomListViewAdapter(DisplayFoodsActivity.this, R.layout.list_row, dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();


    }
}
