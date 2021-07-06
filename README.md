# Bluetooth Handler
Library with basic Bluetooth handling 

To use this Library add this to Your root `build.gradle`:  
```Kotlin
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
```
And add this dependency in Yours app module `build.gradle`:  
```Kotlin
  dependencies {
    implementation 'com.github.DawidKrok:BluetoothHandler:0.0.4'
  }
```

# `BthHandler`
Main class with static methods for handling Bluetooth Connection!  
It's methods also sends proper Broadcasts, which are handy for reacting to connection process from anywhere in your project

<details>
  <summary>METHODS</summary>

### **void** `checkBluetoothEnable(Activity activity)`  
  Checks if Bluetooth is enabled in a device. If not requests enabling it  
  ***activity***: activity needed for opening dialog requesting enabling Bluetooth  

### **void** `checkPermission(AppCompatActivity activity)`  
  Checks for user's permissions on dangerous features, in this case it will be [ACCESS_COARSE_LOCATION](https://developer.android.com/reference/android/Manifest.permission#ACCESS_COARSE_LOCATION)  
  If permission is not granted requests granting it.  
  Use before trying to find new devices in range.  
  ***activity***: activity needed for context on which permissions will be checked and showing dialog box asking for permission  
  
### **boolean** `findPairedDevice(Context context, String device_address)`  
  Finds paired device with given address and saves it in static variable as current device for later use  
  Can send broadcasts: `UNABLE_TO_CLOSE_SOCKET` `PAIRED_DEVICE_FOUND`  
  ***context***: needed for sending broadcasts  
  ***device_address***: MAC address of paired bluetooth device which we want to find  
  ***return***: whether device was found or not  

### **boolean** `connectWithDevice(Context context)`  
  Establishes connection with founded device. Should be used on separate Thread as it would block the one it'll be called on during connection  
  Can send broadcasts: `UNABLE_TO_GET_SOCKET` `UNABLE_TO_CLOSE_SOCKET` `UNABLE_TO_SET_IO_STREAM` `CONNECTING` `CONNECTED`  
  ***context***: needed for sending broadcasts  
  ***return***: whether device was found or not  
  
### **void** `sendData(Context context, String message)`  
  Sends data to currently connected device  
  Can send broadcasts: `UNABLE_TO_SEND_DATA`  
  ***context***: needed for sending broadcasts  
  ***message***: message to send to a device  
  
### **String** `readData(Context context)`  
  Reads data from curerntly connected device  
  Can send broadcasts: `UNABLE_TO_READ_DATA`  
  ***context***: needed for sending broadcasts  
  ***return***: read data  
</details>  

<details>
  <summary>BROADCASTS</summary>
  
  `UNABLE_TO_GET_SOCKET` - Failed to obtain BluetoothSocket from device  
  `UNABLE_TO_CLOSE_SOCKET` - Failed to close BluetoothSocket  
  `UNABLE_TO_SET_IO_STREAM` - Failed to obtain InputStream and OutputStream from device  
  `UNABLE_TO_SEND_DATA` - Failed to send data to device  
  `UNABLE_TO_READ_DATA` - Failed to read data from device  
  `PAIRED_DEVICE_FOUND` - Founded paired BluetoothDevice  
  `CONNECTING` - Started connecting with device  
  `CONNECTED` - Successfully connected with device  
</details>
  
  

