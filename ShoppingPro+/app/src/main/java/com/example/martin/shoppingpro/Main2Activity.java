package com.example.martin.shoppingpro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Main2Activity extends AppCompatActivity {

    ArrayList<String> shoppingList = null;
    //Hilft den Wert in de
    ArrayAdapter<String> adapter = null;
    // Create ListView
    ListView artikel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //shoppingList wird mit den vorherigen Werten wieder ersichtlich
        shoppingList = getArrayVal(getApplicationContext());

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingList);

        //Create
        artikel = (ListView) findViewById(R.id.ShoppingList);

        //Zeigt die Artikel an
        artikel.setAdapter(adapter);



        //----------------------------------Ergänzen----------------------------------



        //Meine Versuche
        /*Intent i = getIntent();

        String shopTitle = i.getStringExtra("name");
        String shopItem = i.getStringExtra("Location");

        setTitle(shopTitle);

        adapter.add(shopItem);*/


        //Listener, falls geklickt wird, wird ein Event ausgeführt
        artikel.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {

                //Setzt den geklickten Wert ins selectedItem
                String selectedItem = ((TextView) view).getText().toString();

                //Falls selectedItem wert aufweist, und es zum ShoppinglistArray gehört
                if (selectedItem.trim().equals(shoppingList.get(position).trim())) {

                    removeElement(selectedItem, position);

                } else {

                    Toast.makeText(getApplicationContext(),"Error Removing Element", Toast.LENGTH_LONG).show();

                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
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
            artikel.setAdapter(adapter);

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
                    artikel.setAdapter(adapter);

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

                    artikel.setAdapter(adapter);
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
    public static void storeArrayVal(ArrayList<String> inArrayList, Context context)
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

    //Pick string of selectedItem
    public void removeElement(String selectedItem, final int position){

        //Dialog zum Loeschen erzeugen
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                shoppingList.remove(position);

                //Werte werden mithilfe storeArrayVal in getApplicationContext gespeichert
                storeArrayVal(shoppingList, getApplicationContext());

                artikel.setAdapter(adapter);
            }

        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        builder.show();
    }

}
