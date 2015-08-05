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
		double[] numbers = new double[1001];  //we will use 1001 since we will be starting at 1
		double[] tempSend = new double[10];   //this array we use for sending out the 10's of the array
		int servPort = 443;	//port number the server will run on
		int count = 1;		//start at 1
		int numOfClients = 5;	//we will be running with 5 clients
		boolean isLoopDone = false;		//dont change
		int frames = 0;		//frames will keep track of which frame we're on
		int low = 1;		//we start at 1
		int high = 10;		//we send at 10
		
		loadArray(numbers);	//loads an array with 1000 random integers
		ServerSocket servSock = new ServerSocket(servPort);		//create the serversocket
		
		while (true) {		
			//accept sockets until we reach designated amount of clients
			if (sockets.add(servSock.accept())); {	
				count++;
			}
			if (count > numOfClients) {
				for (int i = 1; i <= 100; i++) {	//this will run 100 times so 100 x 10 elements = 1000
					grabPartnSet(low,high,numbers,tempSend); //
					sendStream(frames,tempSend,sockets);	//this will send the stream
					receiveStream(frames, sockets, doubleList);	//now we will receive 1 integer and store in doubleList
					frames++;	//increment by 1
					low = low + 10;	//set the new low
					high = high + 10;	//set the new high
					if (frames > 4)		//when we reach 5 frames we reset back to 0
						frames = 0;
				}
				//do it again we now have 100 to go through
				frames = 0;		//reset back to 0
				low = 0;		//reset low to 0
				high = 9;		//reset high to 9
				for (int i = 1; i <= 10; i++) {		//we repeat to do the 100 elements
					grab2(low,high,doubleList,tempSend);	
					sendStream(frames,tempSend,sockets);
					receiveStream(frames,sockets,doubleList2); //100 elements stored into doubleList2
					frames++;
					low = low + 10;
					high = high + 10;
					if (frames > 4)
						frames = 0;
				}
				//we now have 10, do it once more
				doubleList.clear(); //clear doubleList to reuse
				grab2(0,9,doubleList2,tempSend);	
				sendStream(0,tempSend,sockets);
				receiveStream(0,sockets,doubleList);
				isLoopDone = true;		//once we're done we flip to true so we can go to the next part
			}
			if (isLoopDone == true) { //answer is printed and we send stop to all the clients
				System.out.println("The sum of the list is: " + doubleList.get(0));
				sendStop(sockets);
				break;  //break out and the program ends
			}
		}
	}
	
	//source is the source is the socket which it comes from (this is stored in the arraylist)
	//socks is the arraylist of sockets
	//dList is arraylist we store the new double we received
	public static void receiveStream(int source,ArrayList<Socket> socks,ArrayList<Double> dList) throws IOException, ClassNotFoundException {
		DataInputStream dis = new DataInputStream(socks.get(source).getInputStream());
		dList.add(dis.readDouble());
	}
	
	//grabs low and high bounds from an input array, then stores then into an output array
	public static void grabPartnSet(int lowBound, int highBound, double[] in, double[] out) {
		int count = 0;
		for (int i = lowBound; i <= highBound; i++) {
			out[count] = in[i];
			count++;
		}	
	}
	
	//writes out the double array to the specified socket
	public static void sendStream(int destination,double[] doubleArray,ArrayList<Socket> sockets) throws IOException {
		DataOutputStream dos = new DataOutputStream(sockets.get(destination).getOutputStream());
		for (int i = 0; i < doubleArray.length; i++) {
			dos.writeDouble(doubleArray[i]);
		}
			
	}
	
	//creates array of 1000 with 1000 random doubles
	public static void loadArray(double[] array) {
		Random random = new Random();
		for (int i = 1; i <= 1000; i++) {
			array[i] =  random.nextDouble();
		}
	}
	
	//similar to grabPartnSet except the input array is in an arraylist
	public static void grab2(int lowBound, int highBound, ArrayList<Double> doubles, double[] out) {
		int count = 0;
		for (int i = lowBound; i <= highBound; i++) {
			out[count] = doubles.get(i);
			count++;
		}
	}	
	
	//kills all the sockets in the ArrayList of sockets by sending -10
	public static void sendStop(ArrayList<Socket> sockets) throws IOException {
		double[] stopArray = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
		
		for (int i = 0; i < sockets.size(); i++) {
			sendStream(i,stopArray,sockets);	
		}
	}
}