package dsu1995.finevolumecontrol;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class VolumeObserver extends ContentObserver {
    private enum EqualizerVolumeOverflow {UNDERFLOW, IN_RANGE, OVERFLOW}

//    private Context context;

    private AudioManager audioManager;
    private int streamMaxVolume;

    private static final int STREAM = AudioManager.STREAM_MUSIC;

    private Equalizer equalizer;
    private short numOfEqualizerBands;
    private short[] equalizerVolumeRange;

    private int prevStreamVolume;
    private static final short STEP_VALUE = 300;

    private int changedStreamVolumeNumTimes = 0;

    public VolumeObserver(Handler handler, Context context) {
        super(handler);
        Log.d("Volume Observer", "Created");
//        this.context = context;

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        streamMaxVolume = audioManager.getStreamMaxVolume(STREAM);

        equalizer = new Equalizer(0, 0);
        numOfEqualizerBands = equalizer.getNumberOfBands();
        equalizerVolumeRange = equalizer.getBandLevelRange();
//        STEP_VALUE = (short) ((equalizerVolumeRange[1] - equalizerVolumeRange[0]) / 10);
        resetEqualizerVolume();
        equalizer.setEnabled(true);

        updatePrevStreamVolume();
    }

    public void close() {
        equalizer.setEnabled(false);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        onChange(selfChange);
    }

    // called if any change is made to the system settings
    @Override
    public void onChange(boolean selfChange) {
        debug("top");

        if (changedStreamVolumeNumTimes < 0) {
            Log.e("VolumeObserver", "changedStreamVolumeNumTimes < 0");
            assert false;
        }

        if (changedStreamVolumeNumTimes > 0) {
            Log.d("VolumeObserver/!=0", "" + changedStreamVolumeNumTimes);
            changedStreamVolumeNumTimes--;





            updatePrevStreamVolume();






            return;
        }

        if (isVolumeChanged()) {
            onVolumeChange();
        }

        updatePrevStreamVolume();

        debug("bot");
    }

    private boolean isVolumeChanged() {
//        if (Math.abs(prevStreamVolume - getStreamVolume()) > 1) {
//            Log.e("VolumeObserver", "Volume Change > 1");
//            assert false;
//        }

        return prevStreamVolume != getStreamVolume();
    }

    private void updatePrevStreamVolume() {
        prevStreamVolume = getStreamVolume();
    }

    // called only for volume changes
    private void onVolumeChange() {


        int curStreamVolume = getStreamVolume();
        onPressVolumeButton(prevStreamVolume, (int) Math.signum(curStreamVolume - prevStreamVolume));
    }

    private void debug(String str) {
        Log.d("VolumeObserver/" + str, getStreamVolume() + " " + getEqualizerVolume() + " " + changedStreamVolumeNumTimes + " "  + Thread.currentThread().getId());
    }

    private void onPressVolumeButton(int streamVolume, int direction) {
        // edge cases
        if (streamVolume <= 0) { // muted
            if (direction > 0) { // volume up button pressed
                minimizeEqualizerVolume();
                setStreamVolume(1);
                return;
            }
            else if (direction <= 0) { // volume down button pressed
                // do nothing
                minimizeEqualizerVolume();
                setStreamVolume(0);
                return;
            }
        }
        else if (streamVolume >= streamMaxVolume &&
                getEqualizerVolume() >= equalizerVolumeRange[1] &&
                direction >= 0) { // absolute max volume and increasing
            // do nothing
            maximizeEqualizerVolume();
            setStreamVolume(streamMaxVolume);
            return;
        }
        else {
            EqualizerVolumeOverflow overflow = adjustEqualizerVolume(direction);

            switch (overflow) {
                case UNDERFLOW:
                    setStreamVolume(streamVolume - 1);
                    maximizeEqualizerVolume();
                    return;
                case OVERFLOW:
                    setStreamVolume(streamVolume + 1);
                    minimizeEqualizerVolume();
                    return;
                case IN_RANGE:
                    setStreamVolume(streamVolume);
                    return;
            }
        }
    }

    private void setStreamVolume(int volume) {
        if (volume < 0) {
            volume = 0;
        }
        else if (volume > streamMaxVolume) {
            volume = streamMaxVolume;
        }

        if (volume != getStreamVolume()) {
            changedStreamVolumeNumTimes++;

            audioManager.setStreamVolume(STREAM, volume, 0);
        }
    }

    private int getStreamVolume() {
        return audioManager.getStreamVolume(STREAM);
    }

    private EqualizerVolumeOverflow adjustEqualizerVolume(int amount) {
        short equalizerVolume = getEqualizerVolume();
        // if volume already at upper or lower limit, return OVERFLOW/UNDERFLOW,
        // more handling will be done by caller
        if (equalizerVolume == equalizerVolumeRange[0] && amount < 0) {
            return EqualizerVolumeOverflow.UNDERFLOW;
        }
        else if (equalizerVolume == equalizerVolumeRange[1] && amount > 0) {
            return EqualizerVolumeOverflow.OVERFLOW;
        }

        // adjust volume
        setEqualizerVolume((short) (equalizerVolume + amount * STEP_VALUE));
        return EqualizerVolumeOverflow.IN_RANGE;
    }

    private void setEqualizerVolume(short equalizerVolume) {
        if (equalizerVolume < equalizerVolumeRange[0]) {
            equalizerVolume = equalizerVolumeRange[0];
        }
        else if (equalizerVolume > equalizerVolumeRange[1]){
            equalizerVolume = equalizerVolumeRange[1];
        }

        for (short i = 0; i < numOfEqualizerBands; i++) {
            equalizer.setBandLevel(i, equalizerVolume);
        }
    }

    private short getEqualizerVolume() {
        return equalizer.getBandLevel((short) 0);
    }

    private void resetEqualizerVolume() {
        setEqualizerVolume((short) 0);
    }

    private void minimizeEqualizerVolume() {
        setEqualizerVolume(equalizerVolumeRange[0]);
    }

    private void maximizeEqualizerVolume() {
        setEqualizerVolume(equalizerVolumeRange[1]);
    }


//    private void adjustStreamVolume(int direction) {
//        if (direction > 0) {
//            changedStreamVolumeNumTimes++;
//
//            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
//                    AudioManager.ADJUST_RAISE, 0);
//        }
//        else if (direction < 0) {
//            changedStreamVolumeNumTimes++;
//
//            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
//                    AudioManager.ADJUST_LOWER, 0);
//        }
//    }

    //    @Override
//    public void onChange(boolean selfChange) {
//        super.onChange(selfChange);
//
//        debug("top");
//
//        int curStreamVolume = getStreamVolume();
//        if (curStreamVolume > prevStreamVolume) {
//            if (changedStreamVolumeNumTimes > 0) {
//                changedStreamVolumeNumTimes--;
//
//                debug("mid");
//                return;
//            }
//
//            // revert volume change
//            setStreamVolume(prevStreamVolume);
//
//            Log.d("VolumeObserver", "Volume Increased");
//            Toast.makeText(context, "Volume Increased", Toast.LENGTH_SHORT).show();
//
//            onPressVolumeButton(1);
//
//            prevStreamVolume = curStreamVolume;
//
//        }
//        else if (curStreamVolume < prevStreamVolume) {
//            if (changedStreamVolumeNumTimes > 0) {
//                changedStreamVolumeNumTimes--;
//
//                debug("mid");
//                return;
//            }
//
//            // revert volume change
//            setStreamVolume(prevStreamVolume);
//
//            Log.d("VolumeObserver", "Volume Decreased");
//            Toast.makeText(context, "Volume Decreased", Toast.LENGTH_SHORT).show();
//
//            onPressVolumeButton(-1);
//
//            prevStreamVolume = curStreamVolume;
//
//        }
//        debug("bottom");
//    }
}
