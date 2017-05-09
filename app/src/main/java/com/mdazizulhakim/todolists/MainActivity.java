package com.mdazizulhakim.todolists;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btn_Completed_task, btn_add_Task, btn_clear_done_task;
    ListView UnDone_Task, Completed_task;
    BottomSheetBehavior behavior;
    NestedScrollView bottomsheet;
    EditText etxt_TaskIn;
    To_do_list_DataBase to_do_list_dataBase;
    String IDlist[], TASKlist[], DoneId[], DoneTask[];
    Long DATElist[];

    ArrayAdapter<String> undoneTaskAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UnDone_Task = (ListView) findViewById(R.id.CList);
        etxt_TaskIn = (EditText) findViewById(R.id.etxt_Task_input);
        Completed_task = (ListView) findViewById(R.id.completed_task);
        btn_Completed_task = (Button) findViewById(R.id.btn_Completed_task);
        btn_add_Task = (Button) findViewById(R.id.btn_add_task);
        btn_clear_done_task = (Button) findViewById(R.id.btn_Clear_done_task);
        bottomsheet = (NestedScrollView) findViewById(R.id.bottomsheeet);
        behavior = BottomSheetBehavior.from(bottomsheet);


        /** here all code are for a Working UI **/

        //Button For Opening BottomSheet
        btn_Completed_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        //Default BottomSheet Height for Hinding it on app start
        behavior.setPeekHeight(0);

        //Making BottomSheet Collasped when it is open
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setPeekHeight(0);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        //Enabling Working ScrollView In our BottomSheet Listview for making ScrollView Work in The listview
        bottomsheet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Completed_task.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        Completed_task.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Completed_task.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


/** a failed step to make a custom listview with Checkbox.. now using default prebuilt listview :( **/


    /*    cadapter = new CustomAdapter(getApplicationContext(), Countrylist);
        Clist.setAdapter(cadapter);

        AdapterView.OnItemClickListener customlistener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                if (lv.isItemChecked(position)) {
                    Toast.makeText(MainActivity.this, "Item Checked " + Countrylist[position], Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Item Unchecked " + Countrylist[position], Toast.LENGTH_SHORT).show();
                }
            }
        };

        Clist.setOnItemClickListener(customlistener);

    } */


        /** Here all code are for Database and loading data in listView **/


        to_do_list_dataBase = new To_do_list_DataBase(this);
        final Long Date = java.lang.System.currentTimeMillis();
//for preventing app crash :(
        to_do_list_dataBase.addToTable_Undone("01", "Demo Task", Date);
        to_do_list_dataBase.addToTable_done("01", "Demo Finished Task", Date);

//for undone table

      Cursor cursor = to_do_list_dataBase.Display_Table_Undone();
        if (cursor.getCount() > 0) {
            IDlist = new String[cursor.getCount()];
            TASKlist = new String[cursor.getCount()];
            DATElist = new Long[cursor.getCount()];

            cursor.moveToFirst();

            int count = 0;

            do {
                IDlist[count] = cursor.getString(0);
                TASKlist[count] = cursor.getString(1);
                DATElist[count] = cursor.getLong(2);

                count++;

            } while (cursor.moveToNext());
        } else {
        }


        //for done table
        Cursor display_table_done_cursor = to_do_list_dataBase.Display_Table_done();
        if (display_table_done_cursor.getCount() > 0) {
            DoneId = new String[display_table_done_cursor.getCount()];
            DoneTask = new String[display_table_done_cursor.getCount()];
            DATElist = new Long[display_table_done_cursor.getCount()];

            display_table_done_cursor.moveToFirst();

            int count = 0;

            do {
                DoneId[count] = display_table_done_cursor.getString(0);
                DoneTask[count] = display_table_done_cursor.getString(1);
                DATElist[count] = display_table_done_cursor.getLong(2);

                count++;

            } while (display_table_done_cursor.moveToNext());
        } else {
        }


        //inserting data into database.

        btn_add_Task.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String Task = etxt_TaskIn.getText().toString();
                                                Long Date = java.lang.System.currentTimeMillis();


                                                boolean check = to_do_list_dataBase.addToTable_Undone(null, Task, Date);
                                                if (check == true) {
                                                    Toast.makeText(MainActivity.this, "New Task Added!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Faild to Add new Task! ", Toast.LENGTH_SHORT).show();
                                                }

                                                //refreshing intent for loading Database
                                                Intent intent = getIntent();
                                                // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                finish();
                                                overridePendingTransition(0, 0);
                                                startActivity(intent);
                                                overridePendingTransition(0, 0);
                                            }
                                        }

        );

        final String List[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        //Showing Data in Listview With CheckBox
        undoneTaskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, TASKlist);
        UnDone_Task.setAdapter(undoneTaskAdapter);

        //Creating a Custom OnclickListener for Creating Checkbox Working in ListView
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                if (lv.isItemChecked(position)) {
                    Toast.makeText(MainActivity.this, "Task Completed " + TASKlist[position], Toast.LENGTH_SHORT).show();
                    to_do_list_dataBase.addToTable_done(IDlist[position], TASKlist[position], DATElist[position]);
                    to_do_list_dataBase.Delete_Table_undone(IDlist[position]);

                    //refreshing activity for loading database
                    Intent intent = getIntent();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(MainActivity.this, "Item Unchecked " + TASKlist[position], Toast.LENGTH_SHORT).show();
                }
            }
        };
        UnDone_Task.setOnItemClickListener(onItemClickListener);

        ArrayAdapter<String> newadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DoneTask);
        Completed_task.setAdapter(newadapter);

        btn_clear_done_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                to_do_list_dataBase.Delete_Table_done();
                Toast.makeText(MainActivity.this, "Task Cleared!", Toast.LENGTH_SHORT).show();
                //refreshing activity for loading database
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });




    }

    public void addUndoneTask(View v) {

        String Task = etxt_TaskIn.getText().toString();
        Long Date = java.lang.System.currentTimeMillis();


        boolean check = to_do_list_dataBase.addToTable_Undone(null, Task, Date);
        if (check == true) {
            Toast.makeText(MainActivity.this, "New Task Added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Faild to Add new Task! ", Toast.LENGTH_SHORT).show();
        }
        //for undone table
        Cursor cursor = to_do_list_dataBase.Display_Table_Undone();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Error! no Data Found!", Toast.LENGTH_SHORT).show();
        }

        IDlist = new String[cursor.getCount()];
        TASKlist = new String[cursor.getCount()];
        DATElist = new Long[cursor.getCount()];

        cursor.moveToFirst();

        int count = 0;

        do {
            IDlist[count] = cursor.getString(0);
            TASKlist[count] = cursor.getString(1);
            DATElist[count] = cursor.getLong(2);

            count++;

        } while (cursor.moveToNext());

        undoneTaskAdapter.notifyDatasetChanged();



    }


    public void viewDoneTask(View v) {

        //for done table
        Cursor display_table_done_cursor = to_do_list_dataBase.Display_Table_done();
        if (display_table_done_cursor.getCount() == 0) {
            Toast.makeText(this, "Error! No Data Found!", Toast.LENGTH_SHORT).show();

        }

        DoneId = new String[display_table_done_cursor.getCount()];
        DoneTask = new String[display_table_done_cursor.getCount()];
        DATElist = new Long[display_table_done_cursor.getCount()];


        display_table_done_cursor.moveToFirst();

        int count = 0;

        do {
            DoneId[count] = display_table_done_cursor.getString(0);
            DoneTask[count] = display_table_done_cursor.getString(1);
            DATElist[count] = display_table_done_cursor.getLong(2);

            count++;

        } while (display_table_done_cursor.moveToNext());
        ArrayAdapter<String> newadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DoneTask);
        Completed_task.setAdapter(newadapter);

    }

    public void clearDoneTask(View v) {
        to_do_list_dataBase.Delete_Table_done();
        Toast.makeText(MainActivity.this, "Task Cleared!", Toast.LENGTH_SHORT).show();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}





