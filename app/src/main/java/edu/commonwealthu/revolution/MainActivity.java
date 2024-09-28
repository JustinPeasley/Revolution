package edu.commonwealthu.revolution;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.NumberPicker;

/**
 * Plays a game of revolution
 *
 * @author Justin Peasley
 */
public class MainActivity extends AppCompatActivity {

    //variables to pass to backend revolution file
    private static int gridHeight = 4;  //row count
    private static int gridWidth = 4;  //col count
    private static int solDepth = 1;   //solution depth
    private Revolution game= new Revolution(gridWidth, gridHeight,solDepth);

    private SoundManager soundManager; //for sound effects
    private GridLayout gridLayout;     //contains Buttons
    private Button[] buttons;          //displays numbers

    /**
     * Creates the base view of the application which consists of
     *  a toolbar,
     *  a grid of 3x3, (user input for grid size in future)
     *  and horizontal LinearLayout of ImageButtons
     * @param savedInstanceState if saved state of game then reload to this Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar creation
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.showOverflowMenu();
        toolbar.setTitleTextAppearance(this, R.style.Theme_Revolution);
        setSupportActionBar(toolbar);

        //initializing elements of the ui
        gridLayout = findViewById(R.id.mainGridLayout);
        buttons = new Button[gridWidth * gridHeight];
        soundManager = new SoundManager(this);

        if(savedInstanceState != null) //checks if there is a saved instance to load
            game = Utility.getState(savedInstanceState);

        gridLayout.setRowCount(gridHeight);    //set row size
        gridLayout.setColumnCount(gridWidth);  //set col size
        setButtons();                          //set buttons in the gridLayout

        //tie all buttons to actions
        for(Button button : buttons)
            button.setOnClickListener(this::move);
        findViewById(R.id.mainNewGame).setOnClickListener(this::newGame);
        findViewById(R.id.mainUndoButton).setOnClickListener(this::undo);

        Utility.drawBoard(game, buttons);      //draw game
    }

    /**
     * Saves game instance of Revolution
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Utility.saveGame(game, outState);
    }

    /**
     * @param menu The options menu in which you place your items.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    /**
     * takes input and decides what to do in the toolbar menu
     * @param item The menu item that was selected.
     * @return item selected specified action
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();

        if(id==R.id.menu_about){
            showCustomDialog(R.layout.dialog_about);
        }
        if(id==R.id.menu_exit){
            showExitDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Displays a custom alert dialog with a given layout.
     */
    private void showCustomDialog(int layoutId) {
        Utility.showCustomDialog(this, layoutId);
    }


    /**
     * Displays a custom dialog using a specified layout.
     */
    public static void showCustomDialog(Activity activity, int layoutId) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(layoutId, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.darker_gray);
        }
    }


    /**
     * Initializes the Buttons for the Gridlayout
     */
    private void setButtons(){
        buttons = new Button[gridWidth* gridHeight];

        //in case starting new game
        gridLayout.removeAllViews();

        //calc size of buttons so grid fits in screen
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int displayHeight = getResources().getDisplayMetrics().heightPixels;
        int numCells = buttons.length;
        int buttonSize = (9*displayWidth/10)/gridWidth;

        //if more rows then col
        if(displayHeight>displayWidth) buttonSize = (9*displayWidth/10)/ gridWidth;

        //create each button and add it to the gridlayout
        for (int i = 0; i < buttons.length; i++) {
                //new params for each button
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonSize;
                params.height = buttonSize;
                params.setMargins(2,2,2,2);//left, top, right, bottom

                buttons[i]= new Button(this);
                buttons[i].setLayoutParams(params);
                buttons[i].setBackgroundColor(ContextCompat.getColor(this,
                        R.color.white));
                buttons[i].setTag(i);
                buttons[i].setTextSize(buttonSize*.2f);
                gridLayout.addView(buttons[i]);

        }
    }

    /**
     * gets button that was clicked and waits for input on rotation of that button
     * @param view
     */
    private void move(View view){
        int moveIndex = (Integer) view.getTag();

        //test if valid move
        //note to get row & colum positions can be achieved by dividing the index by
        // colum(gridWidth)
        if(game.validAnchor(moveIndex/gridWidth, moveIndex%gridWidth))
        {
            for (Button button:buttons) //Reset button colors
                    button.setBackgroundColor(-1);
            colorChanger(view);

            // Set listener for the left rotation button
            findViewById(R.id.mainLeftButton).setOnClickListener(v -> {
                // Call method to rotate left
                game.rotateLeft(moveIndex / gridWidth, moveIndex % gridHeight);
                Utility.drawBoard(game, buttons); // Update the board
                soundManager.playRotateSound();
                if(game.isOver()){ showCustomToast(getString(R.string.win));
                    soundManager.playWinSound();
                    newGame(findViewById(R.id.mainNewGame));
                }
            });

            // Set listener for the right rotation button
            findViewById(R.id.mainRightButton).setOnClickListener(v -> {
                // Call method to rotate right
                game.rotateRight(moveIndex / gridWidth, moveIndex % gridWidth);
                Utility.drawBoard(game, buttons); // Update the board
                soundManager.playRotateSound();
                if(game.isOver()){ showCustomToast(getString(R.string.win));
                    soundManager.playWinSound();
                    newGame(findViewById(R.id.mainNewGame));
                }
            });
            Utility.drawBoard(game, buttons);
        } else{
            showCustomToast(getString(R.string.invalid));
            soundManager.playFailSound();
        }
    }

    /**
     * used to display a custom toast
     * @param msg what is to be displayed
     */
    private void showCustomToast(String msg) {
        Utility.showCustomToast(this, msg);
    }

    /**
     * undoes the moves the user makes all the way to the base
     * @param view
     */
    private void undo(View view) {
        Utility.undo(view,game,soundManager,buttons,this);
    }

    /**
     * Event handler for restart button (starts a new game).
     */
    private void newGame(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_new_game, null);

        //create numberPicker for user inputted solution depth
        NumberPicker solDepthPicker = dialogView.findViewById(R.id.solDepthPicker);
        solDepthPicker.setMinValue(1); //min solDepth
        solDepthPicker.setMaxValue(9); //max solDepth

        //build the alert Dialog window
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setNegativeButton(android.R.string.no, null) //cancel button to get out
                .setPositiveButton(android.R.string.yes, (v, n) ->   //yes create new game button
                {
                    solDepth = solDepthPicker.getValue(); //set new solution depth from numberPicker
                    game = new Revolution(gridWidth, gridHeight,solDepth); //make new game
                    Utility.drawBoard(game,buttons); //redraw grid

                    for (Button button:buttons) //Reset button colors
                        button.setBackgroundColor(-1);
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        //sets base background color
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.light_orange);
        }
    }

    /**
     * Shows an exit dialog asking if the user wants to exit (if so, terminates).
     */
    protected void showExitDialog() {
        Utility.showExitDialog(this);
    }

    /**
     * calls method that highlights selected 2x2 grid that is set to be rotated
     * @param view for button tags
     */
    private void colorChanger(View view){
        Utility.colorChanger(view, buttons, game, this);
    }
}