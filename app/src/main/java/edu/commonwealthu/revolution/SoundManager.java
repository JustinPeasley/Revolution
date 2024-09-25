package edu.commonwealthu.revolution;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

/**
 * Provides game-related sound effects to an activity.
 *
 * @author Drue Coles
 */
public class SoundManager {
    private SoundPool soundPool;
    private final int rotateSound;  // start a new game
    private final int buttonSound; // valid move
    private final int failSound;   // invalid move
    private final int undoSound;   // take back a move
    private final int winSound;    // puzzle solved

    /**
     * Initializes a new sound manager for a given context.
     */
    public SoundManager(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder().setMaxStreams(1)
                .setAudioAttributes(audioAttributes).build();

        rotateSound = soundPool.load(context, R.raw.swipe03, 1);
        buttonSound = soundPool.load(context, R.raw.snap_sound, 1);
        failSound = soundPool.load(context, R.raw.cartoon_bing, 1);
        winSound = soundPool.load(context, R.raw.win, 1);
        undoSound = soundPool.load(context, R.raw.undo_sound, 1);
    }

    /**
     * Releases all memory and resources used by the SoundPool.
     */
    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    public void playRotateSound() {
        play(rotateSound);
    }

    public void playButtonSound() {
        play(buttonSound);
    }

    public void playWinSound() {
        play(winSound);
    }

    public void playFailSound() {
        play(failSound);
    }

    public void playUndoSound() {
        play(undoSound);
    }

    /**
     * Plays a sound specified by its resource ID.
     */
    private void play(int id) {
        if (soundPool != null) {
            soundPool.play(id, 1, 1, 0, 0, 1);
        }
    }
}