package beta.project.quiz;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class audio_player extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    audio_player(){}

    void init_audio() {
        try {
            //SharedPreferences sp = getSharedPreferences("myOptions", Context.MODE_PRIVATE);
            mediaPlayer = MediaPlayer.create(this, R.raw.button_click);
            if(MainActivity.sp.getBoolean("soundOn", false)) {
                // Initialize MediaPlayer with the sound file
                playSound();
                Log.d("sdsfd","sound played");
            }
        }
        catch (Exception e){
            Log.d("sdsfd","sound not played"+e.getMessage());
        }
    }

    private void playSound() {
        if (mediaPlayer != null) {
            // Reset the MediaPlayer to the beginning
            mediaPlayer.seekTo(0);
            // Play the sound
            mediaPlayer.start();
        }
    }

    /*void end_audio() {
        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }*/
}
