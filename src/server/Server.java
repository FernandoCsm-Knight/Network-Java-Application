package server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import common.types.IPVersion;
import server.managers.PlayerManager;
import server.services.ServerService;

public class Server implements ServerService {
    
    private ServerSocket serverSocket;
    private PlayerManager manager;
    private Thread managerThread;
    private Thread serverThread;
    
    private final AtomicBoolean running;
    private final IPVersion currentIpVersion;

    public Server(int port) throws UnknownHostException {
        this(IPVersion.IPv4, "localhost", port);
    }

    public Server(IPVersion ipVersion, String address, int port) {
        this.running = new AtomicBoolean(false);
        this.currentIpVersion = ipVersion;
        
        try {
            InetAddress inetAddress = InetAddress.getByName(address);

            if(ipVersion == IPVersion.IPv4) {
                this.serverSocket = new ServerSocket(port, 50, inetAddress);
            } else {
                this.serverSocket = new ServerSocket();
                this.serverSocket.bind(new InetSocketAddress(inetAddress, port));
            }

            this.manager = new PlayerManager();
            loggerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.running.set(true);

        this.managerThread = new Thread(this.manager);
        this.managerThread.start();

        this.serverThread = new Thread(() -> {
            logger.info(
                "Server started on " + 
                currentIpVersion.name() + " socket " +
                this.serverSocket.getInetAddress().getHostAddress() +
                ":" + this.serverSocket.getLocalPort()
            );

            while(this.running.get()) {
                try {
                    Socket c = this.serverSocket.accept();
                    this.manager.addClient(c);
                    logger.info("Client connected: " + c.getInetAddress().getHostAddress());
                } catch(Exception e) {
                    if(running.get()) {
                        e.printStackTrace();
                    } else {
                        logger.warn("Server stopped.");
                    }
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

        logger.warn("Server stopped.");
    }
}
