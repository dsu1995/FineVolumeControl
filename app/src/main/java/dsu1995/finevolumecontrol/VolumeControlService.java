package dsu1995.finevolumecontrol;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class VolumeControlService extends Service {

    private VolumeObserver volumeObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Volume Control Service", "Created");

        volumeObserver = new VolumeObserver(new Handler(), this);

        registerVolumeObserver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO intent can tell service to register/unregister volumeObserver
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterVolumeObserver();
    }




    private void registerVolumeObserver() {
        getApplicationContext().
                getContentResolver().
                registerContentObserver(android.provider.Settings.System.CONTENT_URI, true,
                        volumeObserver);
    }

    private void unregisterVolumeObserver() {
        try {
            getApplicationContext().
                    getContentResolver().
                    unregisterContentObserver(volumeObserver);
        }
        catch (IllegalStateException e) {}
        volumeObserver.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // binding not supported
    }
}


