package server.services;

public interface ServerService {
    public static final LoggerService logger = new LoggerService();
    public static final Thread loggerThread = new Thread(logger);
}
