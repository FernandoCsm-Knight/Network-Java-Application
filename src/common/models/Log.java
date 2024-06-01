package common.models;

public class Log {

    public String message;
    public Level level;

    public static enum Level {
        INFO, WARNING, ERROR, DEBUG;    
    }

    public Log(String message, Level level) {
        this.message = message;
        this.level = level;
    }

    @Override
    public String toString() {
        return ((this.level != Level.INFO) ? this.level.name() + ": " : "") + this.message;
    }
}
