package edu.commonwealthu.revolution;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.util.Stack;


public class Utility {
        private static Toast toast;
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
     * Writes the state of game to bundle.
     */
    public static void saveGame(Revolution game, Bundle bundle) {
       bundle.putSerializable("current", game);
    }

    public static Revolution getState(Bundle bundle)
    {
        return (Revolution) bundle.getSerializable("current");
    }

    /**
     * Displays a given string in a custom toast.
     */
    public static void showCustomToast(Activity activity, String message) {
        if (toast != null) toast.cancel(); // cancels any current toast to prevent queuing toast


        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                activity.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        toast = new Toast(activity.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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
            for (int j = 0; j < game.getRow(); j++) {
                System.out.println("butt count = " + buttonCount);
                buttons[buttonCount].setText(game.getOccupant(i,j));
                buttonCount++;
            }
        }
    }
}
