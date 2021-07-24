# Socketeer
Java easy-to-use socket API! Just get the latest release [here](./builds/Socketeer_1.0.jar) and add it as a dependency in your Java project.

## Creating a server
To create a server, you have to do the following:
```java
public class Server extends SocketeerServer {
  public Server() throws UnknownHostException {

  }
}
```
To start the server, do the following:
```java
public class Server extends SocketeerServer {
  public Server() throws IOException {
    this.listen(9000); //Start the server and listen on port 9000
  }
}
```

## Creating a client
To create a client, you have to do the following:
```java
public class Client extends SocketeerClient {
  public Client() throws UnknownHostException {

  }
}
```
To connect to a server, you have to do the following:
```java
public class Client extends SocketeerClient {
  public Client() throws Exception {
    this.connectOneTime("localhost", 9000, new OneTimeAction() {
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
public class Server extends SocketeerServer implements Listener {
  public Server() throws IOException {
    this.setEventListener(this); //Sets this class as the event listener
    this.listen(9000);
  }

  @EventHandler
  public void onClientConnected(ClientConnectedEvent event) { //Fires when a client connects
    System.out.println(event.getConnection().getOtherEnd().getAddress() + " connected to the server.");
  }

  @EventHandler
  public void onReceived(ReceivedEvent event) throws Exception { //Fires when a client sends a message
    String messageFromClient = (String) event.read(); //Reads what the client sent
    System.out.println(event.getConnection().getOtherEnd().getAddress() + ": " + messageFromClient);
    event.reply("This is the server's reply!"); //Replies to the client
  }

  @EventHandler
  public void onClientDisconnected(ClientDisconnectedEvent event) { //Fires when a client disconnects
    System.out.println(event.getConnection().getOtherEnd().getAddress() + " disconnected from the server.");
  }
}
```

## Configuring a client
```java
public class Client extends SocketeerClient {
  public Client() throws Exception {
    this.connectOneTime("localhost", 9000, new OneTimeAction() {
      @Override
      public void action(Connection connection) throws Exception {
        String replyFromServer = (String) connection.sendToServer("Hello there, server!");
        System.out.println(connection.getOtherEnd().getAddress() + ": " + replyFromServer);
      }
    });
  }
}
```
## Running the server and client
If you run the server and client demonstrated above, these are the outputs you will get:
### Server's output
```
127.0.0.1 connected to the server.
127.0.0.1 disconnected from the server.
127.0.0.1: Hello there, server!
```
The reason you see ``127.0.0.1: Hello there, server!`` after ``127.0.0.1 disconnected from the server.`` is because the system takes longer to fire ``ReceivedEvent``, but they virtually happen at the same time (only 3 milliseconds apart).

### Client's output
```
localhost: This is the server's reply!
```
