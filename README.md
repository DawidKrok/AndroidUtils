# BluetoothHandler
Module with basic Bluetooth connection handling

## BthHandler
Main class with static methods for handling Bluetooth Connection!  
It's methods also sends proper Broadcasts, which are handy for reacting to connection process from anywhere in your project

### **void** `checkBluetoothEnable(Activity activity)`  
  Checks if Bluetooth is enabled in a device. If not requests enabling it  
  ***activity***: activity needed for opening dialog requesting enabling Bluetooth  

### **void** `checkPermission(AppCompatActivity activity)`  
  Checks for user's permissions on dangerous features, in this case it will be ACCESS_COARSE_LOCATION  
  If permission is not granted requests granting it.  
  Use before trying to find new devices in range.  
  ***activity***: activity needed for context on which permissions will be checked and showing dialog box asking for permission  
  
### **boolean** `findPairedDevice(Context context, String device_address)`  
  Finds paired device with given address and saves it in static variable as current device for later use  
  ***context***: needed for sending broadcasts  
  ***device_address***: MAC address of paired bluetooth device which we want to find  
  ***return***: whether device was found or not  

### **boolean** `connectWithDevice(Context context)`  
  Establishes connection with founded device. Should be used on separate Thread as it would block the one it'll be called on during connection  
  ***context***: needed for sending broadcasts  
  ***return***: whether device was found or not  
  
### **void** `sendData(Context context, String message)`  
  Sends data to currently connected device  
  ***context***: needed for sending broadcasts  
  ***message***: message to send to a device  
  
### **String** `readData(Context context)`  
  Reads data from curerntly connected device  
  ***context***: needed for sending broadcasts  
  ***return***: read data  
  
