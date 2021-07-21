# Socketeer
Java easy-to-use socket API! Just get the latest release [here] (not yet available) and add it as a dependency in your Java project.

## Creating a server
To create a server, you have to do the following:
```java
public class Server extends End {
  public Server() throws IOException {
    
  }
}
```
To start the server, do the following:
```java
public class Server extends End {
  public Server() throws IOException {
    this.listen(6789); //Start the server and listen on port 6789
  }
}
```

## Creating a client
To create a client, you have to do the following:
```java
public class Client extends End {
  public Client() throws IOException {
    
  }
}
```
To connect to a server, you have to do the following:
```java
public class Client extends End {
  public Client() throws IOException {
    this.connect("localhost", 6789); //Connects to an address and port
  }
}
```

## Configuring a server
If you run the client and the server in the example above, it'll work, but nothing will happen. On the server, we can listen for events to actually do something.
```java
public class Server extends End {
  public Server() throws IOException {
    this.addEventListener(this); //Sets this class as the event litener (make sure you do this before you start the server)
    this.listen(6789); //Start the server and listen on port 6789
  }
  
  //This method listens for a client connection
  @EventHandler
  public void connectionListener(ConnectionSuccessfulEvent event) {
    System.out.println("Client connected: " + event.getConnection.getOtherEnd.getAddress()); //This will happen every time a client connects successfully
  }
  
  //This method listens for messages from the client
  @EventHandler
  public void messageListener(Received event) {
    System.out.println("Message received from client " + event.getConnection.getOtherEnd().getAddress() + ": " + event..getObject()); //This wil happen every time a client sends a message to the server
    event.getConnection.send("Reply to client"); //This sends a message to the client [WORK IN PROGRESS]
  }
}
```

## Configuring a client
```java
public class Client extends End {
  public Client() throws IOException {
    Connection connection = this.connect("localhost", 6789); //Connects to an address and port
    connection.send("Message to server"); //This sends a message to the server
    connection.close(); //Closes the connection
  }
}
```
What if you want a client to listen for a server response, then you have to do the following:
```java
public class Client extends End {
  public Client() throws IOException {
    this.addEventListener(this); //Adds the class as an event listener
    Connection connection = this.connect("localhost", 6789); //Connects to an address and port
    connection.send("Message to server"); //This sends a message to the server
  }
  
  //Listens for a message from the server then closes the connection [WORK IN PROGRESS]
  @EventHandler
  public void messageListener(ReceivedEvent event) {
    System.out.println("Message received from server " + event.getConnection().getOtherEnd().getAddress() + ": " + event.getObject();
    event.getConnection.close();
  }
}
```
