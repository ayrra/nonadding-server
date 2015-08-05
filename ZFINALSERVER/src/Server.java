import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ArrayList<Socket> sockets = new ArrayList<Socket>();
		ArrayList<Double> doubleList = new ArrayList<Double>();
		ArrayList<Double> doubleList2 = new ArrayList<Double>();
		double[] numbers = new double[1001];
		double[] tempSend = new double[10];
		int servPort = 443;
		int count = 1;
		int numOfClients = 5;
		boolean isLoopDone = false;
		int frames = 0;
		int low = 1;
		int high = 10;
		
		loadArray(numbers);	
		ServerSocket servSock = new ServerSocket(servPort);	
		
		while (true) {
			if (sockets.add(servSock.accept())); {	
				count++;
			}
			if (count > numOfClients) {
				for (int i = 1; i <= 100; i++) {
					grabPartnSet(low,high,numbers,tempSend);
					sendStream(frames,tempSend,sockets);
					receiveStream(frames, sockets, doubleList);
					frames++;
					low = low + 10;
					high = high + 10;
					if (frames > 4)
						frames = 0;
				}
				//do it again we now have 100 to go through
				frames = 0;
				low = 0;
				high = 9;
				for (int i = 1; i <= 10; i++) {
					grab2(low,high,doubleList,tempSend);
					sendStream(frames,tempSend,sockets);
					receiveStream(frames,sockets,doubleList2);
					frames++;
					low = low + 10;
					high = high + 10;
					if (frames > 4)
						frames = 0;
				}
				//we now have 10, do it once more
				doubleList.clear();
				grab2(0,9,doubleList2,tempSend);
				sendStream(0,tempSend,sockets);
				receiveStream(0,sockets,doubleList);
				isLoopDone = true;
			}
			if (isLoopDone == true) {
				System.out.println("The sum of the list is: " + doubleList.get(0));
				sendStop(sockets);
				break;
			}
		}
	}
	public static void receiveStream(int source,ArrayList<Socket> socks,ArrayList<Double> dList) throws IOException, ClassNotFoundException {
		DataInputStream dis = new DataInputStream(socks.get(source).getInputStream());
		dList.add(dis.readDouble());
	}
	public static void grabPartnSet(int lowBound, int highBound, double[] in, double[] out) {
		int count = 0;
		for (int i = lowBound; i <= highBound; i++) {
			out[count] = in[i];
			count++;
		}	
	}
	public static void sendStream(int destination,double[] doubleArray,ArrayList<Socket> sockets) throws IOException {
		DataOutputStream dos = new DataOutputStream(sockets.get(destination).getOutputStream());
		for (int i = 0; i < doubleArray.length; i++) {
			dos.writeDouble(doubleArray[i]);
		}
			
	}
	public static void loadArray(double[] array) {	
		Random random = new Random();
		for (int i = 1; i <= 1000; i++) {
			array[i] =  random.nextDouble();
		}
	}
	public static void grab2(int lowBound, int highBound, ArrayList<Double> doubles, double[] out) {
		int count = 0;
		for (int i = lowBound; i <= highBound; i++) {
			out[count] = doubles.get(i);
			count++;
		}
	}	
	public static void sendStop(ArrayList<Socket> sockets) throws IOException {
		double[] stopArray = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		
		for (int i = 0; i < sockets.size(); i++) {
			sendStream(i,stopArray,sockets);	
		}
	}
}