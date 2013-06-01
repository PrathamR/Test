package com.example.test;
 
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.DhcpInfo;
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
 
public class MainActivity2 extends Activity {
 
	public static String SERVERIP = getIpAddress().toString().substring(1);
    public static final int SERVERPORT = 4444;
      public TextView text1;
      public EditText input;
      public boolean start;
      public static Handler Handler;
      public View promptView;
      public ArrayList<String> hosts;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        text1=(TextView)findViewById(R.id.textView1);
        input=(EditText)findViewById(R.id.editText1);
        promptView=(View)findViewById(R.layout.promptview);
        startService(new Intent(this, UDPListenerService.class));
//        btn = (Button)findViewById(R.id.button1);
//        btn.setOnClickListener(this);
//        btn2 = (Button)findViewById(R.id.button2);
//        btn2.setOnClickListener(this);
//        btn3 = (Button)findViewById(R.id.button3);
//        btn3.setOnClickListener(this);
        
        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptView);
		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("Server",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								
								 new Thread(new Server()).start();
							        try {
							                        Thread.sleep(500);
							                } catch (InterruptedException e) { }
							        
							}
						})
				.setNegativeButton("Client",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								new Thread(new Client()).start();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();*/
        new Thread(new Client()).start();
        start=false;
      
        
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
                //InetAddress serverAddr = InetAddress.getByName(SERVERIP);
                updatetrack("Client: Start connecting\n");
                DatagramSocket socket = new DatagramSocket();
                byte[] buf;
                if(!input.getText().toString().equals(""))
                {
                  buf=input.getText().toString().getBytes();
                }
                else
                {
                  buf = ("Default message").getBytes();
                }
                DatagramPacket packet = new DatagramPacket(buf, buf.length, getBroadcastAddress(), SERVERPORT);
                updatetrack("Client: Sending '" + new String(buf) + "'\n");
                socket.send(packet);
                updatetrack("Client: Message sent\n");
                updatetrack("Client: Succeed!\n");
                start=false;
        } catch (Exception e) {
                  updatetrack("Client: Error!\n"+e.getMessage());
        }
        }
}
    
    InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
          quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
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
                    
                        DatagramSocket socket;
                        
                        socket = new DatagramSocket(SERVERPORT, serverAddr);
                        socket.setReuseAddress(true);
                        byte[] buf = new byte[17];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        updatetrack("Server: Receiving\n");
                        socket.receive(packet);
                        updatetrack("Server: Message received: '" + new String(packet.getData()) + "'\n");
                        updatetrack("Server: Succeed!\n");
                        socket.close();
                } catch (Exception e) {
                  updatetrack("Server: Error!\n"+e.getMessage());
                }
        }
        
        
}
      
      public void MyClick(View v) {
            // TODO Auto-generated method stub
    	  switch(v.getId())
    	  {
    	  case R.id.button1:
    		  //new Thread(new Server()).start();
    		  start=true;
    		  new Thread(new Client()).start();
    		  start=false;
    		  break;
    		  
    	  case R.id.button2:
    		  Intent myIntent = new Intent(MainActivity2.this, White_Board.class);
              MainActivity2.this.startActivity(myIntent);
              break;
              
    	  case R.id.button3:
    		  
    		  Toast.makeText(this, getIpAddress().toString(), Toast.LENGTH_LONG).show();
              break;
              
    	  case R.id.button4:
    		 
    		  getHosts();
    		  /*try {
				hostnames();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
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
                         //myAddr=InetAddress.getByName("127.0.0.1");
                     }       
                 } 
              }
               return myAddr;

          }catch (Exception ex) {
              Log.e("check", ex.toString());
          }
          return null;
      }
      
      public ArrayList<String> scanSubNet(String subnet){
    	    ArrayList<String> hosts = new ArrayList<String>();

    	    InetAddress inetAddress = null;
    	    
    	    for(int i=1; i<10; i++){
    	        Log.d("subnet scan", "Trying: " + subnet + String.valueOf(i));
    	        try {
    	        	inetAddress = InetAddress.getByName(subnet + String.valueOf(i));
    	            if(inetAddress.isReachable(1000)){
    	                hosts.add(inetAddress.getCanonicalHostName());
    	                Log.d("host name", inetAddress.getCanonicalHostName());
    	            }
    	        } catch (UnknownHostException e) {
    	            e.printStackTrace();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	    }

    	    return hosts;
    	}
      
      private ArrayList<String> getHosts(){
      Thread thread = new Thread(new Runnable(){
	        @Override
	        public void run() {
	            try {
	            	hosts = scanSubNet("192.168.1.");
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	       
	    });

	    thread.start(); 
	    return hosts;
      }
      
     
    	  /*public void hostnames() throws IOException{
    	  
    		  Thread thread = new Thread(new Runnable(){
    		        @Override
    		        public void run() {
    		            try {
    		            	//hosts = scanSubNet("192.168.1.");
    		           

    	  InetAddress localhost;
		try {
			localhost = InetAddress.getLocalHost();
		

    	  // this code assumes IPv4 is used

    	  byte[] ip = localhost.getAddress();

    	  for (int i = 1; i <= 10; i++) 
    	  {

    	  ip[3] = (byte)i;
    	  InetAddress address = InetAddress.getByAddress(ip);
    	  System.out.print(address);

    	  if (address.isReachable(1000))
    	  {
    	  	Log.d("hostname", address.getHostName().toString());
    	      Log.d("hostname","machine is turned on and can be pinged"); //Accept this
    	  }
    	  else if (!address.getHostAddress().equals(address.getHostName()))
    	  {
    		  Log.d("hostname", address.getHostName().toString());
    		  Log.d("hostname","machine is known in a DNS lookup"); //Accept this
    	  }
    	  else
    	  {
    		  Log.d("hostname","the host address and host name are equal, meaning the host name could not be resolved");
    	  }
    	  }
    	  } catch (UnknownHostException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} } catch (Exception e) {
            e.printStackTrace();
        }
    		        }
    		        
    		  });

    		  thread.start(); 
    	  
      }*/
}
