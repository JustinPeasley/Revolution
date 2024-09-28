package edu.commonwealthu.revolution;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.Stack;


public class Utility {
        private static Toast toast;

    /**
     * highlights selected 2x2 grid that is set to be rotated
     * @param view for grabbing the right buttons
     */
    public static void colorChanger(View view,Button[] buttons, Revolution game, Context context){
        buttons[(Integer)view.getTag()].setBackgroundColor(context.getResources().getColor(R.color.light_orange));
        buttons[(Integer)view.getTag()+1].setBackgroundColor(context.getResources().getColor(R.color.light_orange));
        buttons[(Integer)view.getTag()+game.getCol()].setBackgroundColor(context.getResources().getColor(R.color.light_orange));
        buttons[(Integer)view.getTag()+game.getRow()+1].setBackgroundColor(context.getResources().getColor(R.color.light_orange));
    }


    /**
     * Displays a custom dialog using a specified layout.
     */
    public static void showCustomDialog(Activity activity, int layoutId) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(layoutId, null);

        //create Alert dialog window
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.light_orange);
        }
    }

    /**
     * Writes the state of game to bundle.
     */
    public static void saveGame(Revolution game, Bundle bundle) {
       bundle.putSerializable("current", game);
    }

    /**
     * get the saved state
     * @param bundle takes bundle from mainActivity with activity data
     * @return Revolution object of saved game
     */
    public static Revolution getState(Bundle bundle)
    {
        return (Revolution) bundle.getSerializable("current");
    }

    /**
     * Displays a given string in a custom toast.
     */
    public static void showCustomToast(Activity activity, String message) {
        //cancels any current toast to prevent queuing toast
        if (toast != null) toast.cancel();

        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                activity.findViewById(R.id.toast_layout_root));

        //get text for toast
        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        //create the new toast
        toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0,
                -activity.getResources().getDisplayMetrics().heightPixels/3);
        toast.show();
    }

    /**
     * Shows an exit dialog asking if the user wants to exit (if so, terminates).
     */
    public static void showExitDialog(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_exit, null);

        //build alert dialog window
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView)
                .setNegativeButton(android.R.string.no, null) //cancel
                //suspends app
                .setPositiveButton(android.R.string.yes, (v, n) -> activity.finish());
        AlertDialog dialog = builder.create();
        dialog.show();

        //sets base background color
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.light_orange);
        }
    }

    /**
     * undoes the moves the user makes all the way to the base
     * @param view
     */
    public static void undo(View view, Revolution game, SoundManager soundManager,
                      Button[] buttons, Activity activity) {
        // If the undo operation succeeds, display the new move
        // count. Otherwise,display an error toast.
        if (game.undo()) {
            Utility.drawBoard(game,buttons);
            soundManager.playUndoSound();
        } else {
            showCustomToast(activity, activity.getString(R.string.undo_fail));
            soundManager.playFailSound();
        }
    }

    /**
     * draws the game board
     * @param game obj of revolution class that runs the backend
     * @param buttons buttons within the grid that are rotatable
     */
    public static void drawBoard(Revolution game, Button[] buttons)
    {
        int buttonCount=0;
        for (int i = 0; i < game.getCol(); i++) {
            for (int j = 0; j < game.getRow(); j++) { //set text of each button
                buttons[buttonCount].setText(game.getOccupant(i,j));
                buttonCount++;
            }
        }
    }
}
