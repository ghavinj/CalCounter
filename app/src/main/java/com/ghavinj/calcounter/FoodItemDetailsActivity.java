package com.ghavinj.calcounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;

public class FoodItemDetailsActivity extends AppCompatActivity {

    private TextView foodName, calories, dateTaken;
    private Button shareButton;
    private int foodId;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item_details);



        foodName = (TextView)findViewById(R.id.detsFoodName);
        calories = (TextView)findViewById(R.id.detsCaloriesValue);
        dateTaken = (TextView)findViewById(R.id.detsDateText);

        shareButton = (Button)findViewById(R.id.detsShareButton);
        deleteButton = (Button)findViewById(R.id.detsDeleteButton);

        Food food =  (Food) getIntent().getSerializableExtra("userObj");

        foodName.setText(food.getFoodName());
        calories.setText(String.valueOf(food.getCalories()));
        dateTaken.setText("Consumed On: " +food.getRecordDate());

        foodId = food.getFoodId();
        calories.setTextSize(34.9f);
        calories.setTextColor(Color.RED);



        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareCals();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(FoodItemDetailsActivity.this);
                alert.setTitle("Delete?");
                alert.setMessage("Are you sure you want to delete this item");
                alert.setNegativeButton("No", null);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler dba = new DatabaseHandler(getApplicationContext());
                        dba.deleteFood(foodId);

                        Toast.makeText(getApplicationContext(), "Food Item Deleted!", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(FoodItemDetailsActivity.this, DisplayFoodsActivity.class));

                        //remove activity from activity stack
                        FoodItemDetailsActivity.this.finish();
                    }
                });
                alert.show();
            }
        });
    }

    public void shareCals(){

        StringBuilder dataString = new StringBuilder();

        String name = foodName.getText().toString();
        String cals = calories.getText().toString();
        String date = dateTaken.getText().toString();

        dataString.append(" Food: " + name + "\n");
        dataString.append(" Calories: " + cals + "\n");
        dataString.append(date);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "My Caloric Intake");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"ghavinj@gmail.com"});
        i.putExtra(Intent.EXTRA_TEXT, dataString.toString());

        try{
            startActivity(Intent.createChooser(i, "Send mail..."));

        }catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),"Please install email client before sending", Toast.LENGTH_LONG).show();
        }
    }


}
