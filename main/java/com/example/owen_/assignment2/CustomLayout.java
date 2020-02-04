package com.example.owen_.assignment2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.Gravity;
import android.widget.GridLayout;
import android.view.View;

class CustomLayout extends android.support.v7.widget.AppCompatButton {

    /**
     * reference for row and column, and the id of the faced down card
     */
    public int gridRow,gridColumn, front, actionHeight;

    //Reference to drawable objects
    public Drawable cardFace,cardBack;

    //variables to define the Actions a user can encounter
    public boolean turnCard = false;
    public boolean isMatched = false;

    /**
     * default constructors
     * initialise the front and back of the cards
     * set up the paramaters for the gridlayout
     *
     * @param context
     * @param Row
     * @param Column
     * @param FrontDrawableValue
     * @param abHeight
     */
    public CustomLayout(Context context, int Row, int Column, int FrontDrawableValue, int abHeight) {
        //Constructor class
        super(context);

        this.gridRow = Row;
        this.gridColumn = Column;
        this.front = FrontDrawableValue;
        this.actionHeight = abHeight;

        //initialize the front and back of card
        cardFace = context.getDrawable(front);
        cardBack = context.getDrawable(R.drawable.question_mark);
        setBackground(cardBack);

        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels / 4;
        int screenHeight = (getContext().getResources().getDisplayMetrics().heightPixels - this.actionHeight) / 4;

        GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(Row), GridLayout.spec(Column));
        params.width = screenWidth;
        params.height = screenWidth;

        setLayoutParams(params);
    }

    /**
     * get drawableID to check for match
     * @return
     */
    public int getFrontDrawableValue() {
        return front;
    }

    /**
     * get is matched
     * @return
     */
    public boolean isMatched() {
        return isMatched;
    }

    /**
     * set is matched
     * @param matched
     */
    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    /**
     * Logic to rotate card
     * do nothing if user did not rotate any card
     * if user rotated the card then set is rotated to true and display card
     * do nothing since the user did not match.
     */
    public void rotate() {
        if (turnCard) {
            setBackground(cardBack);
            turnCard = false;
        }
        else {
            setBackground(cardFace);
            turnCard = true;
        }

        if (isMatched) {
            return;
        }
    }
}