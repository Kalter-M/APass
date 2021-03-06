package com.apass;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.apass.entity.RecordList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    RecordList recordList = new RecordList();
    final int REQUEST_CODE_ADD = 1;
    ListView lvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        //TEST
        //recordList.add(new Record("name1", "log1", "pass1", "desc1"));
        //recordList.add(new Record("name2", "log2", "pass2", "desc2"));
        //recordList.save(this);
        //recordList.clear();
        recordList.load(this);

        ShowRecords(this);
    }

    void ShowRecords(final ContextWrapper contextWrapper)
    {
        lvMain = (ListView) findViewById(R.id.lvMain);
        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, recordList.getNames());

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

        //показ записи
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intentCreate = new Intent(ListActivity.this, ShowRecordActivity.class);
                intentCreate.putExtra("record", recordList.get(position));
                startActivity(intentCreate);
                ShowRecords(contextWrapper);
            }
        });

        //удаление записи
        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                recordList.remove(recordList.get(position));
                recordList.save(contextWrapper);
                ShowRecords(contextWrapper);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intentCreate = new Intent(this, AddChangeActivity.class);
                intentCreate.putExtra("recordList", recordList);
                startActivityForResult(intentCreate, REQUEST_CODE_ADD);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // если пришло ОК
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ADD:
                    recordList = (RecordList)data.getSerializableExtra("recordList");
                    recordList.save(this);
                    ShowRecords(this);
                    break;
            }
            // если вернулось не ОК
        } else {
            Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }
}
