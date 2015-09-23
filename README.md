# nonadding-server
A java sockets program with a server that cannot add. The server must send work to the clients which do the addition.

Non-Adding Server and client implemented in java sockets. This is a server server with a randomized array of 1000 doubles and it cannot perform any type of math. The objective is to send parts of the array to different clients, and those clients will perform all the math. The server will eventually end up with 1 doubles which will be the sum of the 1000 doubles.

The client should be fairly straight forward, it is two threads, one for reading and one writing. The receiving thread will receive 10 doubles, add those 10 doubles and then the sending thread will send back 1 double back to the server.

The server is a bit more complicated as it has to do all the managing. The way this server works is it will wait until it has the designated amount of clients. Once all the clients have connected it will run three different functions 100 times, then 10 times, then 1 more time for a total of 111 adds. The first function will grab low through high of an array and set it to another temporary array. The second function will send the temporary array to a client, and the third array will read from the client it just sent to. This is then repeated multiple times. This program was written when I was working on the page replacement program so there is some influence from there.

To run just change the numOfClients to however many you want, start the server and then start how many every clients you chose.
