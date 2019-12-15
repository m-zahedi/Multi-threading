import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;
public class main {
    //    public static ArrayList<Integer> list = new ArrayList();
    public static File resourceFile = new File("result.txt");
    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore empty = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        int[] number_of_thread = new int[] {3, 5, 10, 20};
        int[] productionTimes  = new int[] {200,300, 400};
        for (int threadNum : number_of_thread) {
            for (int p : productionTimes) {
                int productionSlice = p / threadNum;
                consumer[] consumers = new consumer[threadNum];
                producer[] producers = new producer[threadNum];
                for (int i = 0; i < threadNum; i++) {
                    producers[i] = new producer(i, resourceFile, mutex, empty, productionSlice);
                }
                for (int i = 0; i < threadNum; i++) {
                    consumers[i] = new consumer(i, resourceFile, mutex, empty, productionSlice);
                }
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < threadNum; i++) {
                    producers[i].start();
                }
                for (int i = 0; i < threadNum; i++) {
                    consumers[i].start();
                }
                for (producer producer : producers) {
                    producer.join();
                }
                for (consumer consumer : consumers) {
                    consumer.join();
                }
                long endTime = System.currentTimeMillis();
                System.out.print("\t" + (endTime - startTime));
            }
            System.out.println("");
        }
    }
}
class producer extends Thread {
    private final int id;
    private final Semaphore mutex;
    private final File resourceFile;
    private final Semaphore empty;
    private final int productionSlice;

    public producer(int id, File resourceFile, Semaphore mutex, Semaphore prodSem, int productionSlice) {
        this.id = id;
        this.resourceFile = resourceFile;
        this.mutex = mutex;
        this.empty = prodSem;
        this.productionSlice = productionSlice;
    }
    @Override
    public void run() {
        produce();
    }
    private void produce() {

        for (int i = 0; i < productionSlice; i++) {
            try {
                mutex.acquire();
                Thread.sleep(1);
                try (FileWriter fileWriter = new FileWriter(resourceFile, true)) {
                    fileWriter.append('1');
                    fileWriter.flush();
                }
                mutex.release();
                empty.release();
            } catch (InterruptedException | FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        }
    }
}
public class consumer extends Thread {
    private final int id;
    private final Semaphore mutex;
    private final File resourceFile;
    private final Semaphore empty;
    private final int productionSlice;
    public consumer(int id, File resourceFile, Semaphore mutex, Semaphore empty, int productionSlice) {
        this.id = id;
        this.resourceFile = resourceFile;
        this.mutex = mutex;
        this.empty = empty;
        this.productionSlice = productionSlice;
    }
    @Override
    public void run() {
        consume();
    }
    private void consume() {
        for (int i = 0; i < productionSlice; i++) {
            try {
                empty.acquire();
                mutex.acquire();
                Thread.sleep(1);
                String line = null;
                try (FileReader fr = new FileReader(resourceFile);
                     BufferedReader bufferedReader = new BufferedReader(fr)) {
                     line = bufferedReader.readLine();
                }
                try (FileOutputStream fileOutputStream = new FileOutputStream(resourceFile)) {
                    fileOutputStream.write(line.substring(1).getBytes());
                }
                mutex.release();
            } catch (InterruptedException | IOException ex) {
            }
        }
    }
}
