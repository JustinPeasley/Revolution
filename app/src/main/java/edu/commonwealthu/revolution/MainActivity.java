package edu.commonwealthu.revolution;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.commonwealthu.revolution.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    //private Menu optionsMenu;
    private static final int  gridLength= 3;
    private static final int gridWidth = 3;
    private static final int soldepth = 1;
    private Revolution game= new Revolution(gridWidth,gridLength,soldepth);

    //references for the move button for rotate buttons

    private GridLayout gridLayout;  //contains Buttons
    private Button[] buttons;   //displays numbers

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.showOverflowMenu();
        toolbar.setTitleTextAppearance(this, R.style.Theme_Revolution);
        setSupportActionBar(toolbar);

        gridLayout = findViewById(R.id.mainGridLayout);
        buttons = new Button[gridWidth*gridLength];

        gridLayout.setRowCount(gridWidth);
        gridLayout.setColumnCount(gridLength);

        setButtons();
        for(Button button : buttons)
            button.setOnClickListener(this::move);
        drawBoard();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //optionsMenu = menu;
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();

        if(id==R.id.menu_about){
            showCustomDialog(R.layout.dialog_about);
        }
        if(id==R.id.menu_exit){
            //create new game
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
            window.setBackgroundDrawableResource(android.R.color.holo_green_dark);
        }
    }


    /**
     * Initalizes the Buttons for the Gridlayout
     */
    private void setButtons(){
        buttons = new Button[gridWidth*gridLength];

        //in case starting new game
        gridLayout.removeAllViews();

        //calc size of buttons so grid fits in screen
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        int numCells = gridWidth*gridLength;
        int buttonSize = (9*displayWidth/10)/gridWidth;
        
        //create each button and add it to the gridlayout
        for (int i = 0; i < buttons.length; i++) {
                //new params for each button
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = buttonSize;
                params.height = buttonSize;
                params.setMargins(2,2,2,2);//left, top, right, bottom

                buttons[i]= new Button(this);
                buttons[i].setLayoutParams(params);
                buttons[i].setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                buttons[i].setTag(i);
                gridLayout.addView(buttons[i]);

            // Set an OnClickListener for each button
            //final View view = ;
            //buttons[i].setOnClickListener(v -> move(this)); // Pass the index to the move method

        }
    }

    private void drawBoard()
    {
        int buttonCount=0;
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridWidth; j++) {
                System.out.println("butt count = " + buttonCount);
                buttons[buttonCount].setText(game.getOccupant(i,j));
                buttonCount++;
            }
        }
    }

    /**
     * gets button that was clicked and waits for input on rotation of that button
     * @param view
     */
    private void move(View view){
        int moveIndex = (Integer) view.getTag();


        //test if valid move
        if(game.validAnchor(moveIndex/3, moveIndex%3))
        {

            for (Button button:buttons) //Reset button colors
                    button.setBackgroundColor(-1);

            colorChanger(view);
            // Set listener for the left rotation button
            findViewById(R.id.mainLeftButton).setOnClickListener(v -> {
                // Call method to rotate left
                game.rotateLeft(moveIndex / 3, moveIndex % 3);
                drawBoard(); // Update the board
            });

            // Set listener for the right rotation button
            findViewById(R.id.mainRightButton).setOnClickListener(v -> {
                // Call method to rotate right
                game.rotateRight(moveIndex / 3, moveIndex % 3);
                drawBoard(); // Update the board
            });

            drawBoard();
        }
    }

    /**
     * highlights selected 2x2 grid that is set to be rotated
     * @param view
     */
    private void colorChanger(View view){
        buttons[(Integer)view.getTag()].setBackgroundColor(Color.argb(100,199,248,124));
        buttons[(Integer)view.getTag()+1].setBackgroundColor(Color.argb(100,199,248,124));
        buttons[(Integer)view.getTag()+gridWidth].setBackgroundColor(Color.argb(100,199,248,124));
        buttons[(Integer)view.getTag()+gridWidth+1].setBackgroundColor(Color.argb(100,199,248,124));
    }
}