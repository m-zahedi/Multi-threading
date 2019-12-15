import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

class multiplication extends Thread {
    int thread_number,  number_of_threads;
    int  first_array[][], second_array[][], result_array[][];
    public multiplication(int [][]first_array_, int [][]second_array_, int result_array_[][], int thread_number_, int number_of_threads_) {
        first_array = first_array_;
        second_array = second_array_;
        result_array = result_array_;
        thread_number = thread_number_;
        number_of_threads = number_of_threads_;
    }
    public void run() {
        for(int i = thread_number*(first_array.length/number_of_threads); i< (thread_number+1)*(first_array.length/number_of_threads); i++){
            for (int j = 0; j<result_array.length; j++){
                int summation = 0;
                for(int k=0; k<result_array.length; k++){
                    summation += Math.sinh(first_array[i][k]) * Math.sinh(second_array[j][k]);
                }
                result_array[i][j] = summation;
            }
        }
    }
}
public class main {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        PrintWriter writer = new PrintWriter("result.txt", "UTF-8");
        writer.print("number_of_threads:\t\t\t\t\t1\t\t\t\t2\t\t\t\t5\t\t\t\t10\t\t\t\t20\t\t\t\t50\t\t\t\t100\t\t\t");
        System.out.print("\t\tnumber_of_threads:\t\t\t\t\t1\t\t\t2\t\t\t5\t\t\t10\t\t\t20\t\t\t50\t\t\t100\t\t\t");
        int []number_of_threads={1,2,5,10,20,50,100};
        int []power = {1,2,3};

        for (int a=0; a<power.length; a++) {

            double size_of_array_ = Math.pow(10,power[a]);
            int size_of_array = (int) size_of_array_;

            int[][] first_array = new int [size_of_array][size_of_array];
            int[][] second_array = new int[size_of_array][size_of_array];
            int[][] result_array = new int[size_of_array][size_of_array];

            for (int i=0; i<size_of_array;i++){
                for (int j=0; j<size_of_array;j++){
                    first_array [i][j] = (int)(Math.random()*1000);
                    second_array[j][i] = (int)(Math.random()*1000);
                    result_array[i][j] = 0;
                }
            }
            System.out.print("\n\t\t"+"size_of_array: "+"10^"+(a+1)+"\t\t");
            writer.println();
            writer.print("size_of_array: "+"10^"+(a+1)+"\t");
            for (int i=0; i<number_of_threads.length;i++) {
                multiplication[] threads=new multiplication[i];
                long start_time = System.nanoTime();
                for (int j=0; j<i; j++){
                    multiplication thread_ = new multiplication(first_array, second_array, result_array, j, i);
                    thread_.run();
                    thread_.join();
                }
                long end_time = System.nanoTime();
                long total_timing = (end_time - start_time)/1000000;
                System.out.print("\t\t\t"+ total_timing);
                writer.print("\t\t\t"+ total_timing);
            }
        }
        writer.close();
    }
}
