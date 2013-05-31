package com.example.test;
 
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
 
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity2 extends Activity implements OnClickListener{
    public static final String SERVERIP = getIpAddress().toString().substring(1);
    public static final int SERVERPORT = 4444;
      public TextView text1;
      public EditText input;
      public Button btn,btn2,btn3;
      public boolean start;
      public Handler Handler;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        text1=(TextView)findViewById(R.id.textView1);
        input=(EditText)findViewById(R.id.editText1);
        btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(this);
        btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        btn3 = (Button)findViewById(R.id.button3);
        btn3.setOnClickListener(this);
        start=false;
        new Thread(new Server()).start();
        try {
                        Thread.sleep(500);
                } catch (InterruptedException e) { }
        /*new Thread(new Client()).start();*/
        
        
        
        Handler = new Handler() {
      @Override public void handleMessage(Message msg) {
            String text = (String)msg.obj;
            text1.append(text);
      }
      };
    }
 
    public class Client implements Runnable {
        @Override
        public void run() {
            while(start==false)
            {
            }
            try {
            	
                
                        Thread.sleep(500);
                  } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                  }
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                updatetrack("Client: Start connecting\n");
                DatagramSocket socket = new DatagramSocket();
                byte[] buf;
                if(!input.getText().toString().isEmpty())
                {
                  buf=input.getText().toString().getBytes();
                }
                else
                {
                  buf = ("Default message").getBytes();
                }
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, SERVERPORT);
                updatetrack("Client: Sending '" + new String(buf) + "'\n");
                socket.send(packet);
                updatetrack("Client: Message sent\n");
                updatetrack("Client: Succeed!\n");
        } catch (Exception e) {
                  updatetrack("Client: Error!\n");
        }
        }
}
public class Server implements Runnable {
        
        @Override
        public void run() {
            while(start==false)
            {
            }
                try {
                	
                        InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                    
                        updatetrack("\nServer: Start connecting\n");
                    
                        DatagramSocket socket = new DatagramSocket(SERVERPORT, serverAddr);
                        byte[] buf = new byte[17];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        updatetrack("Server: Receiving\n");
                        socket.receive(packet);
                        updatetrack("Server: Message received: '" + new String(packet.getData()) + "'\n");
                        updatetrack("Server: Succeed!\n");
                } catch (Exception e) {
                  updatetrack("Server: Error!\n");
                }
        }
}
      @Override
      public void onClick(View v) {
            // TODO Auto-generated method stub
    	  switch(v.getId())
    	  {
    	  case R.id.button1:
    		  start=true;
    		  break;
    	  case R.id.button2:
    		  Intent myIntent = new Intent(MainActivity2.this, White_Board.class);
              MainActivity2.this.startActivity(myIntent);
              break;
              
    	  case R.id.button3:
    		  
    		  Toast.makeText(this, getIpAddress().toString(), Toast.LENGTH_LONG).show();
              break;
    	  }
            
            
            
      }
      public void updatetrack(String s){
                        Message msg = new Message();
                        String textTochange = s;
                        msg.obj = textTochange;
                        Handler.sendMessage(msg);
      }
      public static InetAddress getIpAddress() {
          try {

              InetAddress inetAddress=null;
              InetAddress myAddr=null;
              Log.d("check","Here1");
              for (Enumeration<NetworkInterface> networkInterface = NetworkInterface.getNetworkInterfaces(); networkInterface.hasMoreElements();) {

                  NetworkInterface singleInterface = networkInterface.nextElement();

                  for (Enumeration<InetAddress> IpAddresses = singleInterface.getInetAddresses(); IpAddresses.hasMoreElements();) {
                      inetAddress = IpAddresses.nextElement();

                      Log.d("check","" + inetAddress);
                       if(!inetAddress.isLoopbackAddress() && (singleInterface.getDisplayName().contains("wlan0" ) || 
                                                             singleInterface.getDisplayName().contains("eth0"  ))){
                      	 				
                         myAddr=inetAddress;
                     }       
                 } 
              }
               return myAddr;

          }catch (Exception ex) {
              Log.e("check", ex.toString());
          }
          return null;
      }
     
}