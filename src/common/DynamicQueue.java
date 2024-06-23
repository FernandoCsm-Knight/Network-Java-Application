package common;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class DynamicQueue<T> {

    private Queue<T> queue;
    private Semaphore semaphore;
    private final Object lock = new Object();

    public DynamicQueue() {
        this.queue = new LinkedList<T>();
        this.semaphore = new Semaphore(0);
    }

    public int size() {
        int count = -1;
        
        synchronized (lock) {
            count = queue.size();
        }

        return count;
    }

    public boolean remove(T element) {
        boolean removed;

        synchronized (lock) {
            removed = queue.remove(element);
        }

        if (removed) {
            semaphore.acquireUninterruptibly();
        }

        return removed;
    }

    public void enqueue(T element) {
        synchronized (lock) {
            queue.add(element);
        }

        semaphore.release();
    }

    public T dequeue() throws InterruptedException {
        semaphore.acquire();

        T element = null;
        synchronized (lock) {
            element = queue.poll();
        }

        return element;
    }

    public T peek() {
        T element = null;
        synchronized (lock) {
            element = queue.peek();
        }

        return element;
    }

    public boolean isEmpty() {
        boolean empty = false;
        synchronized (lock) {
            empty = queue.isEmpty();
        }

        return empty;
    }

    public void clear() {
        synchronized (lock) {
            queue.clear();
        }
    }
}
