package com.example.martin.shoppinglist20;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.app.ListActivity;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> shoppingList = null;
    //Hilft den Wert in de
    ArrayAdapter<String> adapter = null;
    // Create ListView
    ListView lv = null;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //shoppingList wird mit den vorherigen Werten wieder ersichtlich
        shoppingList = getArrayVal(getApplicationContext());

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, shoppingList);

        //Create
        lv = (ListView) findViewById(R.id.ShoppingList);

        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (prefs.getBoolean("Checked", true)) {

                }*/

                View b = findViewById(R.id.del);


                SparseBooleanArray checkedItemPositions = lv.getCheckedItemPositions();
                int itemCount = lv.getCount();


                for (int i = itemCount; i >= 0; i--) {

                    if (checkedItemPositions.get(i)) {
                        b.setVisibility(View.VISIBLE);

                    } else {
                        b.setVisibility(View.INVISIBLE);
                    }

                    if (checkedItemPositions.get(i)){

                        adapter.notifyDataSetChanged();
                        //Werte werden mithilfe storeArrayVal in getApplicationContext gespeichert
                        storeArrayVal(shoppingList, getApplicationContext());
                    }
                }




            }
        };

            lv.setOnItemClickListener(mListClickedHandler);






        FloatingActionButton del = (FloatingActionButton) findViewById(R.id.del);
        del.setImageResource(R.drawable.ic_delete_white_48dp);

        del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                View b = findViewById(R.id.del);
                remove();
                Snackbar.make(view, "The Item('s) is deleted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                b.setVisibility(View.INVISIBLE);
            }
        });

        /** Setting the event listener for the delete button */

        lv.setAdapter(adapter);
    };
    public void remove(){

        SparseBooleanArray checkedItemPositions = lv.getCheckedItemPositions();
        int itemCount = lv.getCount();

        for(int i=itemCount-1; i >= 0; i--){
            if(checkedItemPositions.get(i)){
                adapter.remove((String) shoppingList.get(i));
            }
        }
        checkedItemPositions.clear();
        adapter.notifyDataSetChanged();


        //Werte werden mithilfe storeArrayVal in getApplicationContext gespeichert
        storeArrayVal(shoppingList, getApplicationContext());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_sort){

            //Sortiert aus der Klasse Collections die shoppingList automatisch
            Collections.sort(shoppingList);

            //Wird angepasst
            lv.setAdapter(adapter);

            return true;
        }
        //If click on add
        if(id == R.id.action_add){

            //Dialog zum Hinzufügen erzeugen
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //Display the Title
            builder.setTitle("Add Item");

            final EditText input = new EditText(this);

            builder.setView(input);

            //Shows button "OK"
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //add Methode wird von shoppingList aufgerufen
                    shoppingList.add(preferredCase(input.getText().toString()));

                    //Werte werden mithilfe storeArrayVal in getApplicationContext gespeichert
                    storeArrayVal(shoppingList, getApplicationContext());

                    //listView updaten
                    lv.setAdapter(adapter);

                }
            });
            //Shows button "Cancel"
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });

            builder.show();

            return true;

        }

        if(id == R.id.action_clear){

            //Dialog zum Loeschen erzeugen
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Clear Entire List");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    shoppingList.clear();

                    //Werte werden mithilfe storeArrayVal in getApplicationContext gespeichert
                    storeArrayVal(shoppingList, getApplicationContext());

                    lv.setAdapter(adapter);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();

                }
            });

            builder.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Zeichen Ausgabe (Erster grosser Buchstabe)
    public static String preferredCase(String original)
    {

        if (original.isEmpty())
            return original;

        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();

    }

    //Jedes mal wenn jemand etwas erfasst wird das im Store gespeichert
    public static void storeArrayVal( ArrayList<String> inArrayList, Context context)
    {

        //HashSet ist Subklasse von Set
        Set<String> WhatToWrite = new HashSet<String>(inArrayList);

        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();

        prefEditor.putStringSet("myArray", WhatToWrite);
        prefEditor.commit();
    }

    public static ArrayList getArrayVal( Context dan)
    {

        SharedPreferences WordSearchGetPrefs = dan.getSharedPreferences("dbArrayValues",Activity.MODE_PRIVATE);

        Set<String> tempSet = new HashSet<String>();
        tempSet = WordSearchGetPrefs.getStringSet("myArray", tempSet);

        return new ArrayList<String>(tempSet);
    }
}
