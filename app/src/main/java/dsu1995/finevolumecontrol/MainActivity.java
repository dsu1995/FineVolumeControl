package dsu1995.finevolumecontrol;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

//    private AudioManager audioManager;
//    private TextView prevVolumeView,
//                     nextVolumeView;

//    private Equalizer equalizer;
//    private short bandNum;
//    private short[] bandRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, VolumeControlService.class));
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

//        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//        prevVolumeView = (TextView) findViewById(R.id.textView3);
//        nextVolumeView = (TextView) findViewById(R.id.textView4);

//        equalizer = new Equalizer(0, 0);
//        bandNum = equalizer.getNumberOfBands();
//        bandRange = equalizer.getBandLevelRange();
//        equalizer.setEnabled(true);

//        for (short i = 0; i < bandNum; i++) {
//            equalizer.setBandLevel(i, (short) 0);
//        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // TODO: begin on startup, step value, on/off


        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void volumeUp(View view) {
//        prevVolumeView.setText(Integer.toString(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
//        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
//        nextVolumeView.setText(Integer.toString(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));

//        String str = "";
//        for (short i = 0; i < bandNum; i++) {
//            str += equalizer.getBandLevel(i) + " ";
//        }
//        prevVolumeView.setText(str);
//
//        for (short i = 0; i < bandNum; i++) {
//            equalizer.setBandLevel(i, (short)(equalizer.getBandLevel(i) + 150));
//        }
//
//        str = "";
//        for (short i = 0; i < bandNum; i++) {
//            str += equalizer.getBandLevel(i) + " ";
//        }
//        nextVolumeView.setText(str);
    }

    public void volumeDown(View view) {
//        prevVolumeView.setText(Integer.toString(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));
//        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
//        nextVolumeView.setText(Integer.toString(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)));

//        String str = "";
//        for (short i = 0; i < bandNum; i++) {
//            str += equalizer.getBandLevel(i) + " ";
//        }
//        prevVolumeView.setText(str);
//
//        for (short i = 0; i < bandNum; i++) {
//            equalizer.setBandLevel(i, (short)(equalizer.getBandLevel(i) - 150));
//        }
//
//        str = "";
//        for (short i = 0; i < bandNum; i++) {
//            str += equalizer.getBandLevel(i) + " ";
//        }
//        nextVolumeView.setText(str);
    }

    public void showMaxVolume(View view) {
//        ((Button)view).setText(Integer.toString(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));

//        ((Button)view).setText(bandRange[0] + "-" + bandRange[1]);
    }
}
