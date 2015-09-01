import java.net.Socket;
import java.net.UnknownHostException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
	
public class Client {
	
	static Socket socket;
	static final int BUFSIZE = 1000;
	static boolean lock = false;
	static double arraySum = 0;
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		
		String server = "127.0.0.1";			//input server ip you will be connecting to
		int servPort = 444;						//port we connect through
		socket = new Socket(server,servPort);	// create socket for read/write 
		
		Thread send = new Thread() {	//here we create the send thread to handle sending 
			public void run() {
				DataOutputStream dos = null;
				try {dos = new DataOutputStream(socket.getOutputStream());} catch (IOException e) {}
				while (true) {
					try {sleep(15);} catch (InterruptedException e1) {}
					if (lock == true) {
						try {dos.writeDouble(arraySum);} catch (IOException e) {}
						arraySum = 0;
						lock = false;
					}
				}
			}			
		};
		
		Thread receive = new Thread() {	//here is the receive thread, it handles receiving messages
			public void run() {
				DataInputStream dis = null;
				try {dis = new DataInputStream(socket.getInputStream());} catch (IOException e) {}
				while (true) {
					try {sleep(15);} catch (InterruptedException e1) {}
					if (lock == false) {
						for (int i = 0; i < 10; i++) {
							try {double temp = dis.readDouble();
							arraySum = temp + arraySum;
							} catch (IOException e1) {}
						}
						if (arraySum == -10)
							System.exit(0);
						lock = true;
					}
				}
			}		
		};
		send.start();		//start both threads
		receive.start();
	}
}
