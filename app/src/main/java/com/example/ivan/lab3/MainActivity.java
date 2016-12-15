package com.example.ivan.lab3;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    AlertDialog actions;
    EditText taskInput;
    String input,item;
    int itemPos,currentPos;
    List<String> values;
    String[] todos = new String[] {"idiaz3(G00711429)","groceries","wash car"};
    String[] options = {"Delete"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (android.widget.ListView)findViewById(R.id.toDoList);
        listView.setBackgroundColor(Color.YELLOW);
        assert listView !=null;
        values = new ArrayList<String>(Arrays.asList(todos));//Converts array to ArrayList type

         //Now with values being an arraylist, we can display pre-populated ToDos
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,values);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this); //relates to Alert Box

        listView.setAdapter(adapter);

        /**
         * When an Item is selected do the done prefix or remove
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemPos = adapter.getPosition(((TextView)view).getText().toString());
                item = adapter.getItem(itemPos);
                assert item!=null;
                if(item.contains("DONE:")){
                    Toast.makeText(getApplicationContext(),"Unmarking Item as done "
                            ,Toast.LENGTH_SHORT).show();
                    adapter.remove(item);
                    item = item.replace("DONE:",""); //First param is the string to replace with 2nd
                    adapter.insert(item,0); //Add to top(Or beginning) of the list
                } else {
                    item = "DONE: " + ((TextView)view).getText();
                    adapter.remove(((TextView)view).getText().toString());
                    adapter.insert(item, adapter.getCount());
                    Toast.makeText(getApplicationContext(),"Marking item as done "
                        +((TextView )view).getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        /**
         * Grabs the item to be deleted if the user wishes to do so, along with item's position in
         * the list. It triggers only if the item is highlighted for more than a second
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("olcERROR", "onLongClick: LONG CLICK TRIGGERED ");
                itemPos = adapter.getPosition(((TextView)view).getText().toString());
                currentPos = itemPos;
                actions.show();
                return true;
            }
        });

        /*
        listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemPos = adapter.getPosition(((TextView)v).getText().toString());
                currentPos = itemPos;
                actions.show();
                return true;
            }
        });*/

        Button addItemBtn = (Button)findViewById(R.id.addItemBtn);
        assert addItemBtn!=null;
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addElem(v);
            }
        });

        Button deleteDoneBtn = (Button)findViewById(R.id.deleteDoneBtn);
        assert deleteDoneBtn!=null;
        deleteDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDone(v);
            }
        });


        /**
         * This is invoked when the user taps Delete, and it will delete the item that triggered
         * the long click
         */
        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener(){
         @Override
         public void onClick(DialogInterface dialog, int which){
              switch(which){
                  case 0: //Delete
                     adapter.remove(adapter.getItem(currentPos));
                      break;
                 default:
                     break;
              }
         }
        };

        //Builder attributes for alert dialog
        alertBuilder.setTitle("Confirm Delete");
        alertBuilder.setItems(options, actionListener);
        alertBuilder.setNegativeButton("cancel",null);
        actions = alertBuilder.create();

    }

    /**
     * Add items typed in and make a notification using toaster, it will check for 3 cases
     *  1) Is the new task empty, 2) does it already contain DONE in the tring 3) if not,accept input
     * @param v
     */
    public void addElem(View v){
        taskInput = (EditText)findViewById(R.id.newTaskText);
        assert  taskInput!=null;
        input = taskInput.getText().toString();
        if(input.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please type something in as a task",
                    Toast.LENGTH_LONG).show();
        }
        else if(input.contains("DONE")){
             Toast.makeText(getApplicationContext(),"You're funny, type in a non-completed task",
                    Toast.LENGTH_LONG).show();
        }
        else{
            adapter.add(input);
            Toast.makeText(getApplicationContext(), "Adding " + input, Toast.LENGTH_SHORT).show();
            taskInput.setText(""); //clear the taskInput bar
        }
    }

    /**
     * Delete all the items with prefix "DONE", it will call itself recursively until it reaches
     * the end of the list where it does not find any more DONE words
     * @param v
     */
    public void deleteDone(View v){
        for (int i=0;i<adapter.getCount();i++){
            item = adapter.getItem(i);
            assert item !=null;
            if(item.contains("DONE:")){
                adapter.remove(item);
                adapter.notifyDataSetChanged();
                deleteDone(v);
            }
        }
    }




}
