import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class vector_multiplication extends Thread {
    int thread_number, number_of_threads;
    int first_vector[], second_vector[], result_vector[];
    public vector_multiplication(int first_vector_[], int second_vector_[], int result_vector_[], int thread_number_, int number_of_threads_) {
        this.first_vector  = first_vector_;
        this.second_vector = second_vector_;
        this.result_vector = result_vector_;
        this.thread_number = thread_number_;
        this.number_of_threads = number_of_threads_;
    }
    public void run() {
        for (int i = thread_number * (first_vector.length / number_of_threads); i < (thread_number + 1) * (first_vector.length / number_of_threads); i++) {
            result_vector[i] = first_vector[i] + second_vector[i];
            try {
                Thread.sleep(1);
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
}
public class main {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        try {
            FileWriter outputfile = new FileWriter("result.csv");
//            List<String> data = new ArrayList<String>();

//            outputfile.write("1st_column, 2nd_column,\n");
//            PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
//            writer.print("number_of_threads:\t\t\t\t1\t\t\t\t2\t\t\t\t5\t\t\t\t10\t\t\t\t20\t\t\t\t50\t\t\t\t100\t\t\t\t");
            System.out.print("\t\tnumber_of_threads:\t\t\t\t\t1\t\t\t\t2\t\t\t\t5\t\t\t\t10\t\t\t\t20\t\t\t\t50\t\t\t\t100\t\t\t\t");
            int[] number_of_threads = {1, 2, 5, 10, 20, 50, 100};
            int[] power = {1, 2, 3};
            for (int a = 0; a < power.length; a++) {
                double size_of_vector_ = Math.pow(10, power[a]);
                int size_of_vector = (int) size_of_vector_;
                int[] first_vector = new int[size_of_vector];
                int[] second_vector = new int[size_of_vector];
                int[] result_vector = new int[size_of_vector];

                for (int i = 0; i < size_of_vector; i++) {
                    first_vector[i] = (ThreadLocalRandom.current().nextInt(10));
                    second_vector[i] = (ThreadLocalRandom.current().nextInt(10));
                    result_vector[i] = 0;
                }
                System.out.print("\n\t" + "size_of_vector: " + "10^" + (a + 1) + "\t");
//                writer.println();
//                writer.print("size_of_vector: " + "10^" + (a + 1) + "\t");
                for (int i = 0; i < number_of_threads.length; i++) {
                    vector_multiplication[] threads = new vector_multiplication[i];
                    long start_time = System.nanoTime();
                    for (int j = 0; j < i; j++) {
                        vector_multiplication thread = new vector_multiplication(first_vector, second_vector, result_vector, j, i);
                        thread.run();
                        thread.join();
                    }
                    long end_time = System.nanoTime();
                    long total_timing = (end_time - start_time) / 1000000;
                    System.out.print("\t\t\t\t" + total_timing);
//                    writer.print("\t\t\t" + total_timing);
                    outputfile.write(""+ total_timing +", "+total_timing  +",");
                }
                outputfile.write("\n");
            }
//            writer.close();
            outputfile.close();

        }catch(IOException e) {
             e.printStackTrace();
        }
    }
}
