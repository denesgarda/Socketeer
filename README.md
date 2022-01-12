# Socketeer
Java easy-to-use socket API! Just get the latest release [here](https://github.com/DenDen747/Socketeer/raw/main/builds/Java/Socketeer_2.1.jar) and add it as a dependency in your Java project.

## Creating a server
To create a server, you have a dedicated class that extends ``SocketeerServer``.
```java
public class Server extends SocketeerServer {
    public Server() throws UnknownHostException {
        
    }
}
```
To start the server, use the ``listen(port)`` method.
```java
public class Server extends SocketeerServer {
    public Server() throws UnknownHostException {
        this.listen(9000); //Start the server and listen on port 9000
    }
}
```

## Creating a client
To create a client, you have to have a dedicated class that extends ``SocketeerClient``.
```java
public class Client extends SocketeerClient {
    public Client() throws UnknownHostException {
        
    }
}
```
To connect to a server, use the ``connect(address, port)`` method. This method returns a ``Connection`` that you can use to send and receive.
```java
public class Client extends SocketeerClient {
    public Client() throws IOException {
        Connection connection = this.connect("localhost", 9000);
    }
}
```

## Configuring a server
If you run the client and the server in the example above, it'll work, but nothing will happen. On the server, we can listen for events to actually do something. ``SocketeerServer`` and ``SocketeerClient`` both have ``EventListener`` interfaces. By default, they are assigned to an empty class. To have the event listener of the server be the  ``Server`` class, make the class implement ``EventListener`` and add the line ``this.setEventListener(this);`` before you start the server. Each event listener is its own method. You have to annotate each event-listening method with ``EventHandler``. You can name the method anything and have it throw anything, but it needs to have the event you are listening for in the parameters.
```java
public class Server extends SocketeerServer implements EventListener {
    public Server() throws UnknownHostException {
        this.setEventListener(this);
        this.listen(9000);
    }
    
    @EventHandler
    public void onClientConnected(ClientConnectedEvent event) { // Fires when a client successfully connects
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " connected to the server");
    }
    
    @EventHandler
    public void onClientRejected(ClientRejectedEvent event) { // Fires when a client tried to connect but gets rejected
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " tried to connect to the server but got rejected due to " + event.getReason());
    }
    
    @EventHandler
    public void onClientDisconnected(ClientDisconnectedEvent event) { // Fires when a client disconnects
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " disconnected from the server");
    }
    
    @EventHandler
    public void onReceived(ReceivedEvent event) { // Fires when a message is received from a client
        System.out.println("[" + event.getConnection().getOtherEnd().getAddress() + "]: " + event.getData());
    }
}
```

## Configuring a client
On the client side, you can also have an event listener set up. The only event supported by the client, however, is ``ReceivedEvent``.
```java
public class Client extends SocketeerClient implements EventListener {
    public Client() throws IOException {
        this.setEventListener(this);
        Connection connection = this.connect("localhost", 9000);
    }
    
    @EventHandler
    public void onReceived(ReceivedEvent event) {
        System.out.println("[Server]: " + event.getData());
    }
}
```
The server can send things to the client and the client can send things to the server using the ``send(data)`` method. I'll have the client send a message to the server.
```java
public class Client extends SocketeerClient implements EventListener {
    public Client() throws IOException {
        this.setEventListener(this);
        Connection connection = this.connect("localhost", 9000);
        connection.send("Hey, server!"); //Sends "Hey, server!" to the server
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) {
        System.out.println("[Server]: " + event.getData());
    }
}
```
We can also have the server reply to the client by sending something back.
```java
public class Server extends SocketeerServer implements EventListener {
    public Server() throws UnknownHostException {
        this.setEventListener(this);
        this.listen(9000);
    }

    @EventHandler
    public void onClientConnected(ClientConnectedEvent event) {
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " connected to the server");
    }

    @EventHandler
    public void onClientRejected(ClientRejectedEvent event) {
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " tried to connect to the server but got rejected due to " + event.getReason());
    }

    @EventHandler
    public void onClientDisconnected(ClientDisconnectedEvent event) {
        System.out.println(event.getConnection().getOtherEnd().getAddress() + " disconnected from the server");
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) throws IOException {
        System.out.println("[" + event.getConnection().getOtherEnd().getAddress() + "]: " + event.getData());
        if(event.getData().equals("Hey, server!")) {
            event.getConnection().send("Hey there!"); //Replies with "Hey there!" if we receive "Hey, server!"
        }
        else {
            event.getConnection().send("I don't know how to reply to that"); //Replies with "I don't know how to reply to that" if we get anything else
        }
    }
}
```
To wait for a response and override ``ReceivedEvent``, we can use the ``awaitResponse()`` method.
```java
public class Client extends SocketeerClient implements EventListener {
    public Client() throws IOException {
        this.setEventListener(this);
        Connection connection = this.connect("localhost", 9000);
        connection.send("Hey, server!");
        String response = connection.awaitResponse(); //Waits for a server response
        System.out.println("[Response]: " + response);
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) {
        System.out.println("[Server]: " + event.getData());
    }
}
```
We can also close the connection after we got the response.
```java
public class Client extends SocketeerClient implements EventListener {
    public Client() throws IOException {
        this.setEventListener(this);
        Connection connection = this.connect("localhost", 9000);
        connection.send("Hey, server!");
        String response = connection.awaitResponse();
        System.out.println("[Response]: " + response);
        connection.close();
    }

    @EventHandler
    public void onReceived(ReceivedEvent event) {
        System.out.println("[Server]: " + event.getData());
    }
}
```
