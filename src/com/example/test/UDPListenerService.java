package com.example.test;
 
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
 
import org.apache.http.util.ExceptionUtils;
 
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
 
 

public class UDPListenerService extends Service {
  static String UDP_BROADCAST = "UDPBroadcast";
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		shouldRestartSocketListen = true;
		startListenForUDPBroadcast();
		Log.i("UDP", "Service started");
		return START_STICKY;
	}
	
	void startListenForUDPBroadcast() {
		UDPBroadcastThread = new Thread(new Runnable() {
			public void run() {
				try {
					Log.d("UDP", "Inside try");
					InetAddress broadcastIP = InetAddress.getByName("192.168.1.9"); //172.16.238.42 //192.168.1.255
					Integer port = MainActivity2.SERVERPORT;
					while (shouldRestartSocketListen) {
						Log.d("UDP", "Inside while");
						listenAndWaitAndThrowIntent(broadcastIP, port);
					}
					//if (!shouldListenForUDPBroadcast) throw new ThreadDeath();
				} catch (Exception e) {
					Log.i("UDP", "no longer listening for UDP broadcasts cause of error " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
		UDPBroadcastThread.start();
	}
	
	//Boolean shouldListenForUDPBroadcast = false;
	DatagramSocket socket;

	
	private void listenAndWaitAndThrowIntent(InetAddress broadcastIP, Integer port) throws Exception {
		byte[] recvBuf = new byte[15000];
		if (socket == null || socket.isClosed()) {
			Log.d("UDP", "Inside if");
			socket.setReuseAddress(true);
			socket = new DatagramSocket(MainActivity2.SERVERPORT, InetAddress.getByName("192.168.1.9"));
			Log.d("UDP", "Socket port and address set");
			socket.setBroadcast(true);
		}
		//socket.setSoTimeout(1000);
		DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
		Log.e("UDP", "Waiting for UDP broadcast");
		//socket.receive(packet);
		updatetrack("Server: Receiving\n");
        socket.receive(packet);
        updatetrack("Server: Message received: '" + new String(packet.getData()) + "'\n");
        updatetrack("Server: Succeed!\n");
		
		String senderIP = packet.getAddress().getHostAddress();
		socket.close();
		Log.e("UDP", "Got UDB broadcast from " + senderIP + ", message: " + new String(packet.getData()));
 
		//broadcastIntent(senderIP, message);
		
		//updatetrack("\nServer: Start connecting\n");
        
       
        
        
	}
 
	private void broadcastIntent(String senderIP, String message) {
		Intent intent = new Intent(UDPListenerService.UDP_BROADCAST);
		intent.putExtra("sender", senderIP);
		intent.putExtra("message", message);
		sendBroadcast(intent);
	}
	
	Thread UDPBroadcastThread;
	
	
	
	private Boolean shouldRestartSocketListen=true;
	
	void stopListen() {
		shouldRestartSocketListen = false;
		socket.close();
	}
	
	@Override
	public void onCreate() {
		
	};
	
	@Override
	public void onDestroy() {
		stopListen();
	}
 
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	 public void updatetrack(String s){
         Message msg = new Message();
         String textTochange = s;
         msg.obj = textTochange;
         MainActivity2.Handler.sendMessage(msg);
}
	
}
