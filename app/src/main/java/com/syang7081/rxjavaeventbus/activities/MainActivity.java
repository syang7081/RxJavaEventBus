package com.syang7081.rxjavaeventbus.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.syang7081.rxjavaeventbus.R;
import com.syang7081.rxjavaeventbus.util.BaseEvent;
import com.syang7081.rxjavaeventbus.util.RxJavaEventBus;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private Consumer listener = new MyConsumer();
    private Thread testingThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    public void onResume() {
        super.onResume();
        RxJavaEventBus.register(listener);
        // start testing thread
        testingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    MyEvent myEvent = new MyEvent();
                    myEvent.message = "Hello World! Message sequence number: " + (i + 1);
                    RxJavaEventBus.post(myEvent);
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {}
                }
            }
        });
        testingThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        RxJavaEventBus.unregister(listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyEvent implements BaseEvent {
        public String message;
    }

    class MyConsumer implements Consumer<MyEvent> {
        public void accept(MyEvent event) throws Exception {
            // Update UI
            TextView textView = (TextView) findViewById(R.id.greeting);
            textView.setText(event.message);
        }
    }
}
