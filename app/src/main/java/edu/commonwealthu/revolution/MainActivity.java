package edu.commonwealthu.revolution;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.commonwealthu.revolution.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private static final int  gridLength= 3;
    private static final int gridWidth = 3;
    private static final int soldepth = 1;
    private Revolution game= new Revolution(gridWidth,gridLength,soldepth);

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

        setButtons();
        drawBoard();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
                //buttons[i].setScale
                gridLayout.addView(buttons[i]);

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

    private void move(View view){}
}