package git.utils.bthhandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/** super class for Broadcast Receiver to handle Broadcasts send by BluetoothHandler library */
public abstract class BluetoothBroadcastReceiver extends BroadcastReceiver {

    /** onReceive method will call functions corresponding to Broadcasts send by BthHandler.
     * Those functions have to be implemented in subclass */
    @Override
    public void onReceive(Context context, Intent intent) {
        switch(intent.getAction()) {
            case BthHandler.UNABLE_TO_GET_SOCKET:       unableToGetSocket();
                break;
            case BthHandler.UNABLE_TO_CLOSE_SOCKET:     unableToCloseSocket();
                break;
            case BthHandler.UNABLE_TO_SET_IO_STREAM:    unableToSetIOStream();
                break;
            case BthHandler.UNABLE_TO_CONNECT:          unableToConnect();
                break;
            case BthHandler.UNABLE_TO_SEND_DATA:        unableToSendData();
                break;
            case BthHandler.UNABLE_TO_READ_DATA:        unableToReadData();
                break;
            case BthHandler.PAIRED_DEVICE_NOT_FOUND:    pairedDeviceNotFound();
                break;
            case BthHandler.PAIRED_DEVICE_FOUND:        pairedDeviceFound();
                break;
            case BthHandler.CONNECTING:                 connecting();
                break;
            case BthHandler.CONNECTED:                  connected();
                break;
        }
    }



    /** Registers Receiver to listen for all Broadcasts send by BthHandler
     * (Also, friendly reminder to unregister your Receiver when you don't need it)
     * @param context: Context on which Receiver will be registered */
    public void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(BthHandler.UNABLE_TO_GET_SOCKET);
        intentFilter.addAction(BthHandler.UNABLE_TO_CLOSE_SOCKET);
        intentFilter.addAction(BthHandler.UNABLE_TO_SET_IO_STREAM);
        intentFilter.addAction(BthHandler.UNABLE_TO_CONNECT);
        intentFilter.addAction(BthHandler.UNABLE_TO_SEND_DATA);
        intentFilter.addAction(BthHandler.UNABLE_TO_READ_DATA);
        intentFilter.addAction(BthHandler.PAIRED_DEVICE_NOT_FOUND);
        intentFilter.addAction(BthHandler.PAIRED_DEVICE_FOUND);
        intentFilter.addAction(BthHandler.CONNECTING);
        intentFilter.addAction(BthHandler.CONNECTED);
        context.registerReceiver(this, intentFilter);
    }

    /** function that'll be called in response to {@link BthHandler#UNABLE_TO_GET_SOCKET} Broadcast */
    public void unableToGetSocket() {}
    /** function that'll be called in response to {@link BthHandler#UNABLE_TO_CLOSE_SOCKET} Broadcast */
    public void unableToCloseSocket() {}
    /** function that'll be called in response to {@link BthHandler#UNABLE_TO_CONNECT} Broadcast */
    public void unableToConnect() {}
    /** function that'll be called in response to {@link BthHandler#UNABLE_TO_SET_IO_STREAM} Broadcast */
    public void unableToSetIOStream() {}
    /** function that'll be called in response to {@link BthHandler#UNABLE_TO_SEND_DATA} Broadcast */
    public void unableToSendData() {}
    /** function that'll be called in response to {@link BthHandler#UNABLE_TO_READ_DATA} Broadcast */
    public void unableToReadData() {}
    /** function that'll be called in response to {@link BthHandler#PAIRED_DEVICE_NOT_FOUND} Broadcast */
    public void pairedDeviceNotFound() {}
    /** function that'll be called in response to {@link BthHandler#PAIRED_DEVICE_FOUND} Broadcast */
    public void pairedDeviceFound() {}
    /** function that'll be called in response to {@link BthHandler#CONNECTING} Broadcast */
    public void connecting() {}
    /** function that'll be called in response to {@link BthHandler#CONNECTED} Broadcast */
    public void connected() {}
}
