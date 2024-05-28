package server;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    
    ServerSocket serverSocket;
    AtomicBoolean running;
    PlayerManager manager;
    Thread managerThread;
    Thread serverThread;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.running = new AtomicBoolean(false);
            this.manager = new PlayerManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.running.set(true);

        this.managerThread = new Thread(this.manager);
        this.managerThread.start();

        this.serverThread = new Thread(() -> {
            System.out.println("Server started on port " + this.serverSocket.getLocalPort());
            
            while(this.running.get()) {
                try {
                    this.manager.addPlayer(this.serverSocket.accept());
                } catch(Exception e) {
                    if(running.get()) e.printStackTrace();
                    else System.out.println("Server stopped.");
                }
            }

            this.shutdown();
        });

        this.serverThread.start();
    }

    public void shutdown() {
        this.running.set(false);
        this.manager.shutdown();

        try {
            if(!this.serverSocket.isClosed()) this.serverSocket.close();
            this.serverThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Server stopped.");
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }
}
