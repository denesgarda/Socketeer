# Socketeer
Java easy-to-use socket API! Just get the latest release [here](https://github.com/DenDen747/Socketeer/raw/main/builds/Java/Socketeer_3.0.jar) and add it as a dependency in your Java project.

## Creating a server
To create a server, you have a dedicated class that extends ``SocketeerServer``.
```java
public class Server extends SocketeerServer {
    protected Server() throws UnknownHostException {
    }

    @Override
    public void onEvent(Event event) {
        
    }
}
```
To start the server, use the ``listen(port)`` method.
```java
public class Server extends SocketeerServer {
    protected Server() throws UnknownHostException {
        this.listen(9000); // Starts the server on port 9000
    }

    @Override
    public void onEvent(Event event) {

    }
}
```

## Creating a client
To create a client, you have to have a dedicated class that extends ``SocketeerClient``.
```java
public class Client extends SocketeerClient {
    protected Client() throws UnknownHostException {
        
    }

    @Override
    public void onEvent(Event event) {
        
    }
}
```
To connect to a server, use the ``connect(address, port)`` method. This method returns a ``Connection`` that you can use to send and receive.
```java
public class Client extends SocketeerClient {
    protected Client() throws IOException {
        Connection connection = this.connect("localhost", 9000); // Connects to the server localhost:9000
    }

    @Override
    public void onEvent(Event event) {

    }
}
```

## Configuring a server
If you run the client and the server in the example above, it'll work, but nothing will happen. On the server, we can listen for events to actually do something. ``SocketeerServer`` and ``SocketeerClient`` both have abstract ``onEvent`` methods. Separate events can be handled by checking the type of event using ``instanceof``.
```java
public class Server extends SocketeerServer {
    protected Server() throws UnknownHostException {
        this.listen(9000);
    }

    @Override
    public void onEvent(Event event) {
        // This is called when a client connects to the server
        if (event instanceof ClientConnectEvent) {
            System.out.println(((ClientConnectEvent) event).getConnection().getOtherEnd().getAddress() + " connected to the server.");
        }
        
        // This is called when a client disconnects from the server
        if (event instanceof ClientDisconnectEvent) {
            System.out.println(((ClientDisconnectEvent) event).getClient().getAddress() + " disconnected from the server.");
        }
        
        // This is called when an unanticipated message is received from a client
        if (event instanceof ReceiveEvent) {
            System.out.println("[" + ((ReceiveEvent) event).getConnection().getOtherEnd().getAddress() + "]: " + ((ReceiveEvent) event).getData());
        }
    }
}
```

## Configuring a client
On the client side, you can also have an event listener set up. Different events are supported by servers and clients.
```java
public class Client extends SocketeerClient {
    protected Client() throws IOException {
        Connection connection = this.connect("localhost", 9000);
    }

    @Override
    public void onEvent(Event event) {
        // This is called when an unanticipated message is received from the server
        if (event instanceof ReceiveEvent) {
            System.out.println("[Server]: " + ((ReceiveEvent) event).getConnection());
        }
        
        // This is called when a server closes or crashes
        if (event instanceof ServerCloseEvent) {
            System.out.println("Server closed.");
        }
    }
}
```
The server can send things to the client and the client can send things to the server using the ``send`` method. I'll have the client send a message to the server.
```java
protected Client() throws IOException {
  	Connection connection = this.connect("localhost", 9000);
  	connection.send("Hey, Server!"); // Sends a message to the server
}
```
We can also have the server reply to the client by sending something back.
```java
if (event instanceof ReceiveEvent) {
    System.out.println("[" + ((ReceiveEvent) event).getConnection().getOtherEnd().getAddress() + "]: " + ((ReceiveEvent) event).getData());
    if (((ReceiveEvent) event).getData().equals("Hey, Server!")) {
        try {
            ((ReceiveEvent) event).getConnection().send("Hi there!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        try {
            ((ReceiveEvent) event).getConnection().send("I don't know how to reply to that.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
To anticipate a response and override ``ReceivedEvent``, we can use the ``nextIn`` method.
```java
protected Client() throws IOException {
    Connection connection = this.connect("localhost", 9000);
    connection.send("Hey, Server!");
    connection.nextIn(new Queueable() {
        @Override
        public void nextIn(String s) throws IOException {
            // This is called when we get a response from the serer
            // It overrides ReceiveEvent so the onEvent method will not be called
            System.out.println("[Server response]: " + s);
        }
    });
}
```
We can close the connection after we got the response.
```java
protected Client() throws IOException {
    Connection connection = this.connect("localhost", 9000);
    connection.send("Hey, Server!");
    connection.nextIn(new Queueable() {
        @Override
        public void nextIn(String s) throws IOException {
            System.out.println("[Server response]: " + s);
            connection.close(); // Closes the connection
        }
    });
}
```

We can also stop the server after we send our response.

```java
if (event instanceof ReceiveEvent) {
    System.out.println("[" + ((ReceiveEvent) event).getConnection().getOtherEnd().getAddress() + "]: " + ((ReceiveEvent) event).getData());
    if (((ReceiveEvent) event).getData().equals("Hey, Server!")) {
        try {
            ((ReceiveEvent) event).getConnection().send("Hi there!");
            this.stopListening(); // Stops the server and closes all connections
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        try {
            ((ReceiveEvent) event).getConnection().send("I don't know how to reply to that.");
            this.stopListening(); // Stops the server and closes all connections
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

This should be the result on the console when both the server and the client are ran.

Server console:

```
127.0.0.1 connected to the server.
[127.0.0.1]: Hey, Server!
127.0.0.1 disconnected from the server.
```

Client console:

```
[Server response]: Hi there!
```

