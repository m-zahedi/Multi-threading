import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class philosopher extends Thread {
    private String philosopher_number;
    private final Lock leftFork, rightFork;
    public philosopher(String number, Lock left, Lock right) {
        this.philosopher_number = number;
        this.leftFork = left;
        this.rightFork = right;
    }

    public void waiting() {
        log("waiting");
    }

    public void eat() {
        try {
            log("eating");
            int eatingTime = getRandomEatingTime();
            TimeUnit.NANOSECONDS.sleep(eatingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    @Override
    public void run() {
        while (true) {
            waiting();
            if (leftFork.tryLock()) {
                try {
                    log("picked up left fork");
                    if (rightFork.tryLock()) {
                        try {
                            log("picked up the right fork");
                            eat();
                        } finally {
                            log("put down right fork");
                            rightFork.unlock();
                        }
                    }
                } finally {
                    log("put down left fork");
                    leftFork.unlock();
                }
            }
        }
    }

    private void log(String msg) {
        System.out.printf("%s: %s%n", philosopher_number, msg);
        System.out.flush();
    }

    private int getRandomEatingTime() {
        Random random = new Random();
        return random.nextInt(500) + 50;
    }
}
public class main {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        PrintWriter writer = new PrintWriter("thread Test.txt", "UTF-8");
        Lock[] forks = new Lock[5];
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new ReentrantLock();
        }
        philosopher[] philosophers = new philosopher[5];
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < philosophers.length; i++) {
            Lock leftFork   = forks[i];
            Lock rightFork  = forks[(i + 1) % forks.length];
            philosophers[i] = new philosopher("Philosopher " + (i + 1), leftFork, rightFork);
            executorService.execute(philosophers[i]);
        }
        executorService.shutdown();
    }
}
