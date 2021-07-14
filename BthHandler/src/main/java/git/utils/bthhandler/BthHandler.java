package git.utils.bthhandler;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

/** Class for handling Bluetooth */
public class BthHandler {
    private static final int REQUEST_ENABLE_BT = 1,
    REQUEST_PERMISSION_BLUETOOTH = 2;

    // UUID for connection with device
    public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");;

    // Strings used for broadcasts messages
    public static final String TAG = "BthHandler",
            /** Failed to obtain BluetoothSocket from device */
            UNABLE_TO_GET_SOCKET = "git.utils.BthHandler.unable_to_get_socket",

            /** Failed to close BluetoothSocket */
            UNABLE_TO_CLOSE_SOCKET = "git.utils.BthHandler.unable_to_close_socket",

            /** Failed to obtain InputStream and OutputStream from device */
            UNABLE_TO_SET_IO_STREAM = "git.utils.BthHandler.unable_to_set_io_stream",

            /** Failed to connect with device */
            UNABLE_TO_CONNECT = "git.utils.BthHandler.unable_to_connect",

            /** Failed to send data to device */
            UNABLE_TO_SEND_DATA = "git.utils.BthHandler.unable_to_send_data",

            /** Failed to read data from device */
            UNABLE_TO_READ_DATA = "git.utils.BthHandler.unable_to_read_data",

            /** Failed to find paired BluetoothDevice */
            PAIRED_DEVICE_NOT_FOUND = "git.utils.BthHandler.unable_to_find_paired_device",

            /** Founded paired BluetoothDevice */
            PAIRED_DEVICE_FOUND = "git.utils.BthHandler.paired_device_found",

            /** Started connecting with device */
            CONNECTING = "git.utils.BthHandler.connecting",

            /** Successfully connected with device */
            CONNECTED = "git.utils.BthHandler.connected";


    private static final BluetoothAdapter BthAdapter = BluetoothAdapter.getDefaultAdapter();
    private static BluetoothDevice my_device = null;
    private static BluetoothSocket socket;
    private static OutputStream output;
    private static InputStream input;




    /** Checks if Bluetooth is enabled in a device. If not requests enabling it
     * @param activity: activity needed for opening dialog requesting enabling Bluetooth */
    public static void checkBluetoothEnable(Activity activity) {
        if (!BthAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /** Checks for permissions on dangerous features.
     * In Bluetooth case it will be ACCESS_COARSE_LOCATION - needed for finding new devices in range.
     * If permission is not granted requests granting it.
     * It's best to check permissions only when the feature is needed.
     * Required in API 23 or higher
     * @param activity: needed for context on which permissions will be checked and showing dialog box asking for permission. */
    public static void checkPermissions(AppCompatActivity activity) {
        // checks if permission is not granted
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            // Check if user demands showing excuses for using dangerous features before asking for permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION))
                // Notify why user must obey your laws
                Toast.makeText(activity, "Permission must be granted to find Bluetooth devices.", Toast.LENGTH_SHORT).show();

            // Request permission.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_BLUETOOTH);
        }
    }

    /** Finds paired device with given address
     * @param context: context of application - for sending broadcasts
     * @param device_address: MAC address of paired bluetooth device which we want to find
     * @return whether device was found or not*/
    public static boolean findPairedDevice(Context context, String device_address) throws IOException {
        //Close eventual connection
        if(socket != null)
            try {
                socket.close();
            } catch (IOException e) {
                context.sendBroadcast(new Intent(UNABLE_TO_CLOSE_SOCKET));
                throw e;
            }

        // get set of paired devices
        Set<BluetoothDevice> pairedDevices = BthAdapter.getBondedDevices();

        // to check if device was founded in set of paired Devices
        boolean foundedDevice = false;

        if (pairedDevices.size() > 0) {
            // Seek for device with given address in paired devices
            for (BluetoothDevice device : pairedDevices) {
                if(device.getAddress().equals(device_address)) {
                    context.sendBroadcast(new Intent(PAIRED_DEVICE_FOUND));
                    my_device = device;
                    foundedDevice = true;
                    break;
                }
            }
        }
        // return whether device was found or not
        if(!foundedDevice)
            context.sendBroadcast(new Intent(PAIRED_DEVICE_NOT_FOUND));

        return foundedDevice;
    }

    /** Establish connection with founded device (Must use {@link #findPairedDevice(Context, String)} first)
     * Should be used on separate Thread
     * @param context: context of application - for sending broadcasts
     * @return whether this operation was successful or not */
    public static boolean connectWithDevice(Context context) throws IOException {
        if(my_device == null)   return false;

        // try obtaining socket for connections
        try {
            socket = my_device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            context.sendBroadcast(new Intent(UNABLE_TO_GET_SOCKET));
            throw e;
        }

        context.sendBroadcast(new Intent(CONNECTING));

        // try connecting with device
        try { socket.connect(); }
        catch (IOException connectException) {
            // Unable to connect; close the socket and throw Exception.
            context.sendBroadcast(new Intent(UNABLE_TO_CONNECT));

            try { socket.close(); }
            catch (IOException closeException) {
                context.sendBroadcast(new Intent(UNABLE_TO_CLOSE_SOCKET));
                throw closeException;
            }
            throw connectException;
        }


        // establish I/O stream
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
        } catch(IOException e) {
            context.sendBroadcast(new Intent(UNABLE_TO_SET_IO_STREAM));
            throw e;
        }

        context.sendBroadcast(new Intent(CONNECTED));
        return true;
    }

    /** Sends data to device
     * Must be connected with device first (use {@link #connectWithDevice(Context)})
     * @param context: context of application - for sending broadcasts
     * @param message: message to send */
    public void sendData(Context context, String message) throws IOException {
        // try sending data
        try {
            output.write(message.getBytes());
        } catch(IOException e) {
            context.sendBroadcast(new Intent(UNABLE_TO_SEND_DATA));
            throw e;
        }
    }

    /** Reads data from device
     * Must be connected with device first (use {@link #connectWithDevice(Context)})
     * @param context: context of application
     * @return read data */
    public String readData(Context context) throws IOException {
        DataInputStream mmInStream = new DataInputStream(input);
        byte[] buffer = new byte[256];
        // try returning read data
        try {
            return new String(buffer, 0, mmInStream.read(buffer));
        } catch(Exception e) {
            context.sendBroadcast(new Intent(UNABLE_TO_READ_DATA));
            throw e;
        }
    }
}