package com.techtweakz.cowsandbulls;

import android.util.Log;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public final class StompUtil
{
    public static StompClient mStompClient;
    public static Disposable mRestPingDisposable;
    public static CompositeDisposable compositeDisposable;

    public static final String TAG = "techtweakz";

    public static StompClient createStompClient(String username) {
        HashMap<String,String> map = new HashMap<>();
        map.put("username",username);
        StompClient  mStompClient =
                Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.1.11:8080/techtweakz-stomp-end-point/websocket",map);
       // Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.0.106:8080/techtweakz-stomp-end-point/websocket",map);

     //  mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://192.168.43.99:8080/techtweakz-stomp-end-point/websocket");
        //+ ANDROID_EMULATOR_LOCALHOST
        //+ ":" + RestClient.SERVER_PORT + "/example-endpoint/websocket");
        //    Toast.makeText(this, "Start connecting to server", Toast.LENGTH_SHORT).show();

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);

        resetSubscriptions();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            //    toast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                        //    toast("Stomp connection error");
                            break;
                        case CLOSED:
                            Log.i(TAG, "Stomp connection Closed");
                        //    toast("Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.i(TAG, "Stomp connection failed heartbeat");
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);
       mStompClient.connect();

        return mStompClient;

    }
 /*   private void toast(String message) {

        Toast.makeText(getContext() ,message,Toast.LENGTH_SHORT).show();
    }*/

    private static void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }
}
