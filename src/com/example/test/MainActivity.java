package com.example.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("check",getIpAddress().toString());
       // Log.d("check",getBroadcast(getIpAddress()).toString());
       // check_tether();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public InetAddress getIpAddress() {
        try {

            InetAddress inetAddress=null;
            InetAddress myAddr=null;
            Log.d("check","Here1");
            for (Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces(); networkInterface.hasMoreElements();) {

                NetworkInterface singleInterface = networkInterface.nextElement();

                for (Enumeration<InetAddress> IpAddresses = singleInterface.getInetAddresses(); IpAddresses.hasMoreElements();) {
                    inetAddress = IpAddresses.nextElement();

                     if(!inetAddress.isLoopbackAddress() && (singleInterface.getDisplayName().contains("wlan0" ) || 
                                                           singleInterface.getDisplayName().contains("eth0"  ))){
                    	 				
                       myAddr=inetAddress;
                   }       
               } 
            }
             return myAddr;

        } catch (Exception ex) {
            Log.e("check", ex.toString());
        }
        return null;
    }
    
    public InetAddress getBroadcast(InetAddress inetAddr){

        NetworkInterface temp;
        InetAddress iAddr=null;
        String TAG="check";
     try {
         temp = NetworkInterface.getByInetAddress(inetAddr);
         List<InterfaceAddress> addresses = temp.getInterfaceAddresses();

         for(InterfaceAddress inetAddress:addresses)

         iAddr=inetAddress.getBroadcast();
         Log.d(TAG,"iAddr="+iAddr);
         return iAddr;  

     } catch (Exception e) {

         e.printStackTrace();
         Log.d(TAG,"getBroadcast"+e.getMessage());
     }
      return null; 
 }
    public void check_tether()
    {
    	 Log.d("check", "in tether check");
    	 WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
         Method[] wmMethods = wifi.getClass().getDeclaredMethods();
         for(Method method: wmMethods){
         if(method.getName().equals("isWifiApEnabled")) {

         try {
         if(  (Boolean) method.invoke(wifi)){
             Boolean isInetConnOn=true;
             int iNetMode=2;

         }else{
            Log.d("check", "WifiTether off");
         }
         } catch (IllegalArgumentException e) {
           e.printStackTrace();
         } catch (IllegalAccessException e) {
           e.printStackTrace();
         } catch (InvocationTargetException e) {
           e.printStackTrace();
         }

         }
         else
         {
        	 Log.d("check", "WifiAP not enabled ");
         }
         }
    }
}
