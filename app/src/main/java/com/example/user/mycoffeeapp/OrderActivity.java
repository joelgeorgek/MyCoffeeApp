package com.example.user.mycoffeeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    Spinner itemInput;
    EditText itemCountInput;
    List<String> coffeeList = new ArrayList<>();
    List<String> coffeePriceList = new ArrayList<>();
    List<String> coffeeListWithPrices = new ArrayList<>();
    DBHelper myDB;

    Map<String,Integer> ItemsAndPrices = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportActionBar().setTitle("Order Here");

        itemInput = findViewById(R.id.ItemInput);
        itemCountInput = findViewById(R.id.ItemCountInput);

        coffeeList = Arrays.asList(getResources().getStringArray(R.array.coffees_array));
        coffeePriceList = Arrays.asList(getResources().getStringArray(R.array.coffees_price_array));

        int i=0;
        for(String item : coffeeList){
            coffeeListWithPrices.add(item +" - $"+Integer.parseInt(coffeePriceList.get(i)));
            ItemsAndPrices.put(item, Integer.parseInt(coffeePriceList.get(i)));
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_dropdown_item,coffeeListWithPrices);

        itemInput.setAdapter(adapter);

        myDB = new DBHelper(this);
    }
    public void onClickAddOrder(View view){
        String item = coffeeList.get(itemInput.getSelectedItemPosition());
        String itemCountString = itemCountInput.getText().toString();
        //if(!coffeeList.contains(item)){
        //    itemInput.setError("Item does not exist");
        //    itemInput.setText("");
        //    return;
        //}
        //if(item.equals("")){
        //    itemInput.setError("Enter an item");
        //    itemInput.setText("");
        //    return;
        //}
        if(itemCountString.equals("")){
            itemCountInput.setError("Enter a count");
            itemCountInput.setText("");
            return;
        }
        int itemCount = Integer.parseInt(itemCountString);
        if(itemCount<1){
            itemCountInput.setError("It should be a positive number");
            itemCountInput.setText("");
            return;
        }
        int totalCost = ItemsAndPrices.get(item)*itemCount;
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Ordering...");
        dialog.show();
        //Log.e("HAHA","Item :"+item);
        //Log.e("HAHA","Cost :"+Integer.toString(totalCost));
        //Log.e("HAHA","Count :"+itemCountString);
        myDB.insertOrder(item,Integer.toString(totalCost),itemCountString);
        dialog.dismiss();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}
