//Owen slevin    2873156

package com.example.owen_.assignment2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.view.View.INVISIBLE;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    //added variables
    int card1, card2;
    int firstCard, secondCard;
    int cardNumber = 1;
    int turn = 1;
    int player1 = 1;
    int player2 = 1;

    public CustomLayout Click1, Click2;

    public int numDrawables;
    public CustomLayout[] numButtons;
    public int[] cardId, cards;

    //Used for creating the delay when cards are rotated
    public boolean isProcessing = false;

    TextView tv_p1, tv_p2;



    public final int  FINAL_MATCHES = 8;
    public int correctMatch;
    public int incorrectMatch;
    public long startTime;
    public String gameOverMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_p1 = (TextView) findViewById(R.id.tv_p1);
        tv_p2 = (TextView) findViewById(R.id.tv_p2);

        GridLayout gridLayout_4x4 = (GridLayout) findViewById(R.id.activity_grid4x4_);

        //num of columns and rows
        int totalColumns = gridLayout_4x4.getColumnCount();
        int totalRows = gridLayout_4x4.getRowCount();

        numDrawables = (totalColumns * totalRows);
        numButtons = new CustomLayout[numDrawables];

        //only stores the unique cards
        cards = new int[(numDrawables / 2)];

        cards[0] = R.drawable.img_11;
        cards[1] = R.drawable.img_12;
        cards[2] = R.drawable.img_13;
        cards[3] = R.drawable.img_14;
        cards[4] = R.drawable.img_15;
        cards[5] = R.drawable.img_16;
        cards[6] = R.drawable.img_17;
        cards[7] = R.drawable.img_18;

        cardId = new int[numDrawables];
        //randomly fill ogrid layout
        randomizeCards();

        // for action bar height
        int actionBarHeight = 0;
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        TypedValue typedValue = new TypedValue();

        if (getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data,getResources().getDisplayMetrics());
        }
        if ( resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        int optionBarsHeight = actionBarHeight + statusBarHeight;
        //loop to populate the rows and the columns on the grid
        for (int row = 0; row < totalRows; row++) {
            for (int column = 0; column < totalColumns; column++) {
                CustomLayout tempNum = new CustomLayout(this, row, column, cards[cardId[row * totalColumns + column]], optionBarsHeight);
                tempNum.setId(View.generateViewId());
                tempNum.setOnClickListener(this);
                //Storing the references
                numButtons[row * totalColumns + column] = tempNum;
                gridLayout_4x4.addView(tempNum);
            }
        }
    }

    /**
     * Inflate the menu widgets
     * For the user to reset the game
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Logic for the home and reset button
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.it_refresh4x4: reset4x4Grid();
            default: return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Reset the grid and cards
     */
    public void reset4x4Grid() {
            super.recreate();
        }

    /**
     * Randomize the cards on the grid
     */
    public void randomizeCards() {
        Random random = new Random();
        for (int i = 0; i < numDrawables; i++) {
            cardId[i] = (i % (numDrawables / 2));
        }

        for (int i = 0; i < numDrawables; i++) {
            int temp = cardId[i];
            int swapIndex = random.nextInt(16);
            cardId[i] = cardId[swapIndex];
            cardId[swapIndex] = temp;
        }
    }

    /**
     * If theres a match display a pop up notification to the user
     * keep track of the turns, player 1 = odd and player 2 = even
     * Update the player textviews as necessary
     */
    public void matchToast() {
        Context match = getApplicationContext();
        String matchText = "MATCHED";
        int duration = Toast.LENGTH_SHORT;
        if (turn % 2 == 1) {
            tv_p1.setText("Player 1 = " + player1);
            tv_p2.setTextColor(Color.BLACK);
            tv_p1.setTextColor(Color.GRAY);
            player1++;
        }
        else {
            tv_p2.setText("Player 2 = " + player2);
            tv_p1.setTextColor(Color.BLACK);
            tv_p2.setTextColor(Color.GRAY);
            player2++;
        }

        turn++;

        Toast toast = Toast.makeText(match, matchText, duration);
        //toast.layout_weight=0;
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    /**
     * Display a notification to the user if the cards don't match.
     * track turns and update textview
     */
    public void misMatchToast() {
        Context mismatch = getApplicationContext();
        String mismatchText = "Try Again";
        int duration = Toast.LENGTH_SHORT;

        if (turn % 2 == 1) {
            tv_p2.setTextColor(Color.BLACK);
            tv_p1.setTextColor(Color.GRAY);
        }
        else {
            tv_p1.setTextColor(Color.BLACK);
            tv_p2.setTextColor(Color.GRAY);
        }


        turn++;

        Toast mismatchToast = Toast.makeText(mismatch, mismatchText, duration);
        mismatchToast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
        mismatchToast.show();

        //turn++;
    }

    /**
     * Do nothing when the process is running to prevent the app from crashing
     * Check cards ids to see if theres a match and rotating cards as necessary
     * If theres a match turn the cards invisible if there no match turn them back over
     * When the games over display messages
     *
     * @param view
     *
     */

    @Override
    public void onClick(View view) {
        CustomLayout button = (CustomLayout) view;
        Handler handler = new Handler();

        if (isProcessing) {
            //do nothing when there is a process running - this prevents crashes
            return;
        }

        if (button.isMatched) {
            //if match then do nothing
            return;
        }

        if (Click1 == null) {
            Click1 = button;
            Click1.rotate();
            return;
        }

        if (Click1.getId() == button.getId()) {
            return;
        }
        if (correctMatch == 0 && incorrectMatch == 0){
            startTime = System.currentTimeMillis();
        }
        if (Click1.getFrontDrawableValue() == button.getFrontDrawableValue()) {
            //User matches two cards
            button.rotate();
            button.setMatched(true);
            Click1.setVisibility(View.INVISIBLE);
            button.setVisibility(View.INVISIBLE);
            Click1.setEnabled(false);
            button.setEnabled(false);
            Click1 = null;

            matchToast();
            correctMatch++;

            if (correctMatch == FINAL_MATCHES){
                int matchScore = correctMatch + incorrectMatch;
                long stopTime = System.currentTimeMillis();
                long elapsedTimeMilliSeconds = (stopTime - startTime) / 10;
                long elapsedTimeSeconds = elapsedTimeMilliSeconds / 100;
                long elapsedTimeMinutes = elapsedTimeSeconds/ 60;
                elapsedTimeSeconds = elapsedTimeSeconds % 60;
                elapsedTimeMilliSeconds = elapsedTimeMilliSeconds % 100;

                  if (player1 > player2) {
                     gameOverMessage = ("Player 1 has won the game! The match lasted " + elapsedTimeMinutes + " minutes and " + elapsedTimeSeconds + " seconds");
                 }

                if (player2 > player1) {
                    gameOverMessage = ("Player 2 has won the game! The match lasted " + elapsedTimeMinutes + " minutes and " + elapsedTimeSeconds + " seconds");
                }

                else if (player1 == player2) {
                    gameOverMessage = ("The game was a draw! The match lasted " + elapsedTimeMinutes + " minutes and " + elapsedTimeSeconds + " seconds");
                }


                new AlertDialog.Builder(this)
                        .setTitle("Result")
                        .setMessage(gameOverMessage)
                        .setNegativeButton("Play Again", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reset4x4Grid();
                            }
                        })
                        .create().show();

            }

            return;
        }
        else {
            //if fails to match two cards
            Click2 = button;
            Click2.rotate();
            isProcessing = true;
            incorrectMatch++;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Click2.rotate();
                    Click1.rotate();
                    Click1 = null;
                    Click2 = null;
                    isProcessing = false;
                }
            }, 200);
            misMatchToast();
        }
    }
}

