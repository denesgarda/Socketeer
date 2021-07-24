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
  public Client() throws Exception {
    
  }
}
```
To connect to a server, you have to do the following:
```java
public class Client extends End {
  public Client() throws IOException {
    this.connectOneTime("localhost", 6789, new OneTimeAction() {
      @Override
      public void action(Connection connection) throws Exception {
        
      }
    });
  }
}
```

## Configuring a server
If you run the client and the server in the example above, it'll work, but nothing will happen. On the server, we can listen for events to actually do something.
```java
public class Server extends End {
  public Server() throws IOException {
    this.setEventListener(this); //Sets this class as the event litener (make sure you do this before you start the server)
    this.listen(6789); //Start the server and listen on port 6789
  }
  
  @EventHandler
  public void onClientConnected(ClientConnectedEvent event) {
      System.out.println(event.getConnection().getOtherEnd().getAddress() + " connected using " + event.getConnectionType());
  }

  @EventHandler
  public void onReceived(ReceivedEvent event) throws Exception {
      String message = (String) event.read();
      System.out.println("Message received from " + event.getConnection().getOtherEnd().getAddress() + ": " + message);
      event.reply("This is the server's reply");
  }

  @EventHandler
  public void onClientDisconnected(ClientDisconnectedEvent event) {
      System.out.println(event.getConnection().getOtherEnd().getAddress() + " disconnected");
  }
}
```

## Configuring a client
```java
public class Client extends End {
  public Client() throws IOException {
    this.connectOneTime("localhost", 9000, new OneTimeAction() {
      @Override
      public void action(Connection connection) throws Exception {
          String reply = (String) connection.send("Hello There!"); //Returns the server's response
          System.out.println(reply);
      }
    });
  }
}
```
