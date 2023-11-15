package com.mobdeve.tighee.farminggameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* In this exercise, you're to implement the FarmingGameApp, but allow for multiple production tasks
* to run. One get difference here is that there is no "current" progress UI -- all updates are
* pushed to the message log. Allow up to 3 tasks to run at the same time. If more than 3 tasks are
* present, the first in is the first to be inserted into executed (i.e. queue like fashion)
*
* Study what code you're given and implement the problem described above.
* */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityLog";

    // Views for the activity
    private TextView moneyTv;
    private Button buyBtn;
    private LinearLayout gameLogVll;
    private ScrollView gameLogSv;

    // Keeps track of our money.
    private int money = 0;
    // Keeps track of the number of jobs sent off. Doubles as the job ID.
    private int jobCount = 1;

    private ExecutorService executorService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.executorService = Executors.newFixedThreadPool(3);
        this.handler = new Handler();

        // View initialization
        this.moneyTv = findViewById(R.id.moneyTv);
        this.buyBtn = findViewById(R.id.buyBtn);
        this.gameLogVll = findViewById(R.id.gameLogVll);
        this.gameLogSv = findViewById(R.id.gameLogSv);

        // Initialization of the money and its view; money is initially set to 0.
        updateMoney(30);

        this.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(money >= 10) {
                    updateMoney(-10);

                    /*
                    * TODO:
                    *  1. Add your own logic for processing/queueing the task
                    * */
                    executorService.execute(new ProductionRunnable(jobCount));

                    addToGameLog(jobCount, "Production task queued.");
                    jobCount++;
                } else {
                    Toast.makeText(
                            MainActivity.this,
                            "Not enough money... kono Dio da!.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addToGameLog(int jobCount, String message) {
        String log = "Task #" + jobCount + ": " + message;

        TextView temp_tv = new TextView(MainActivity.this);
        temp_tv.setText(log);

        gameLogVll.addView(temp_tv);

        // This is a UI thing to scroll to the bottom since we're adding views at the bottom.
        gameLogSv.fullScroll(ScrollView.FOCUS_DOWN);
    }

    private void updateMoney(int m) {
        this.money = this.money + m;
        this.moneyTv.setText("Money: " + this.money);
    }

    /*
     * TODO:
     *  1. Modify the object to properly update the game log according to the specifications
     *      described in Canvas.
     *      1.1. Inform the log that the task is being worked on with the task's name (this is a one
     *          time log once the tasks starts)
     *      1.2. Inform the log with how much the product made and update the money's view
     *  2. Don't forget that UI updates must not run on a worker thread.
     *
     * NOTE:
     *  1. Creating a ProductionRunnable takes in a taskId int so that the task can inform the log
     *      what task it is.
     *  2. Utilize that this class is within the MainActivity in order to access UI elements.
     * */
    private class ProductionRunnable implements Runnable {
        private int taskId;

        public ProductionRunnable(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            try {
                Log.d(TAG, "thread: id is" + this.taskId);

                // Randomly create a new product
                Product product = Product.generateProduct();
                Log.d(TAG, "thread: created product: " + product.getName());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        addToGameLog(taskId, "Task has started. Working on " + product.getName() + ".");
                    }
                });

                // Work on the actual product (i.e. sleep...)
                for(int i = 0; i < product.getProductionTime() && !Thread.currentThread().isInterrupted(); i++) {
                    Log.d(TAG, "thread: working on product... " + i);

                    Thread.sleep(1000);
                }

                // Generation... of... da pera
                int generatedMoney = product.generateMoney();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        addToGameLog(taskId, "Task has finished and generated " + generatedMoney + " money.");
                        updateMoney(generatedMoney);
                    }
                });

                Log.d(TAG, "thread: finished.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}