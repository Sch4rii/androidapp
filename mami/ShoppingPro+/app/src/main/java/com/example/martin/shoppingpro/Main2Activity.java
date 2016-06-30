package com.example.martin.shoppingpro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Main2Activity extends AppCompatActivity {

    ArrayList<String> selectedItems = new ArrayList<String>();
    ArrayList<String> shoppingList = null;
    ArrayAdapter<String> adapter = null;
    ListView artikel = null;
    Button clearAll;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyUserChoice" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        artikel = (ListView) findViewById(R.id.ShoppingList);

        //shoppingList wird mit den vorherigen Werten wieder ersichtlich
        shoppingList = getArrayVal(getApplicationContext());

        //Clear Button setzt den Zustand der CheckBox in false
        clearAll = (Button)findViewById(R.id.clearall);


        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, shoppingList);

        ArrayAdapter<String> adapterItem = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, shoppingList);
        artikel.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        artikel.setAdapter(adapter);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedpreferences.contains(MyPREFERENCES)){
            LoadSelections();
        }





        //----------------------------------Ergänzen 1.1----------------------------------

        //Die Artikel die erfasst wurden brauchen eine Beziehung zu dem jeweiligen ausgewählten Shop


        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View b = findViewById(R.id.del);


                SparseBooleanArray checkedItemPositions = artikel.getCheckedItemPositions();
                String selected = "";
                int itemCount = artikel.getCount();


                for (int i = itemCount; i >= 0; i--) {

                    if (checkedItemPositions.get(i)) {
                        selected += artikel.getItemAtPosition(i).toString() + "\n";
                        System.out.println("Checking list while adding:" + artikel.getItemAtPosition(i).toString());
                        SaveSelections();
                        b.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                b.setVisibility(View.INVISIBLE);

            }

        };

        artikel.setOnItemClickListener(mListClickedHandler);

        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClearSelections();
            }
        });



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

                //ClearSelections();
            }
        });

        artikel.setAdapter(adapter);
    };

    public void remove() {

        SparseBooleanArray checkedItemPositions = artikel.getCheckedItemPositions();
        int itemCount = artikel.getCount();

        for (int i = itemCount - 1; i >= 0; i--) {
            if (checkedItemPositions.get(i)) {
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
        getMenuInflater().inflate(R.menu.menu_main2, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //If click on add
        if(id == R.id.action_add_shop){

            //Dialog zum Hinzufügen erzeugen
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            //Display the Title
            builder.setTitle("Add Shop");

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

    private void SaveSelections() {
// save the selections in the shared preference in private mode for the user

        SharedPreferences.Editor prefEditor = sharedpreferences.edit();
        String savedItems = getSavedItems();
        prefEditor.putString(MyPREFERENCES.toString(), savedItems);
        prefEditor.commit();
    }

    private String getSavedItems() {
        String savedItems = "";
        int count = this.artikel.getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            if (this.artikel.isItemChecked(i)) {
                if (savedItems.length() > 0) {
                    savedItems += "," + this.artikel.getItemAtPosition(i);
                } else {
                    savedItems += this.artikel.getItemAtPosition(i);
                }
            }
        }
        return savedItems;
    }

    //----------------------------------Ergänzen 2.0----------------------------------//

    //Der Checkzustand wird erfolgreich gespeichert und wenn man die App neustartet wird auch die ausgewählte Checkbox
    //mithilfe Toast ausgegeben -> "Current Item: " + currentItem. Nur wird die Checkbox leider nicht
    //angeklickt angezeigt. Die soll aber angeklickt angezeigt werden.

    private void LoadSelections() {
        // if the selections were previously saved load them

        if (sharedpreferences.contains(MyPREFERENCES.toString())) {

            String savedItems = sharedpreferences.getString(MyPREFERENCES.toString(), "");
            selectedItems.addAll(Arrays.asList(savedItems.split(",")));


            int count = this.artikel.getAdapter().getCount();

            for (int i = 0; i < count; i++) {
                String currentItem = (String) artikel.getAdapter()
                        .getItem(i);
                if (selectedItems.contains(currentItem)) {
                    artikel.setItemChecked(i, true);
                    Toast.makeText(getApplicationContext(),
                            "Current Item: " + currentItem,
                            Toast.LENGTH_LONG).show();
                } else {
                    artikel.setItemChecked(i, false);


                }

            }
        }
    }

    //Clear all checkboxes at once
    private void ClearSelections() {
        // user has clicked clear button so uncheck all the items
        int count = this.artikel.getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            this.artikel.setItemChecked(i, false);
        }
        // also clear the saved selections
        SaveSelections();
    }

}
