import java.util.concurrent.*;

public class barber_shop extends Thread {
    public static int number_of_chairs = 5;
    public static Semaphore customers   = new Semaphore(0);
    public static Semaphore barber      = new Semaphore(0);
    public static Semaphore number_of_available_chairs = new Semaphore(1);
    public static void main(String args[]) {
        barber_shop barberShop = new barber_shop();
        barberShop.start();
    }
    public void run(){
        barber barber_ = new barber();
        barber_.start();
        for (int i=1; i<10; i++) {
            customer customer_ = new customer(i);
            customer_.start();
            try {
                sleep(1000);
            } catch(Exception e) {};
        }
    }
    class barber extends Thread {
        public void cutting_hair(){
            System.out.println("Barber is busy cutting customer's hair");
            try {
                sleep(5000);
            } catch (InterruptedException ex){ }
        }
        public void run() {
            while(true) {
                try {
                    customers.acquire();
                    number_of_available_chairs.release();
                    number_of_chairs++;
                    barber.release();
                    number_of_available_chairs.release();
                    this.cutting_hair();
                } catch (Exception e) {}
            }
        }
    }
    class customer extends Thread {
        int customer_number;
        boolean notCut=true;
        public customer(int j) {
            customer_number = j;
        }
        public void get_haircut(){
            System.out.println("Customer " + this.customer_number + " is getting a hairstyle");
            try {
                sleep(1000);
            } catch (InterruptedException ex) {}
        }
        public void run() {
            while (notCut) {
                try {
                    number_of_available_chairs.acquire();
                    if (number_of_chairs > 0) {
                        System.out.println("Customer " + this.customer_number + " has sat down and waits for the barber.");
                        number_of_chairs--;
                        customers.release();
                        number_of_available_chairs.release();
                        try {
                            barber.acquire();
                            notCut = false;
                            this.get_haircut();
                        } catch (InterruptedException ex) {}
                    }else {
                        System.out.println("There isn't any chair available. So, customer " + this.customer_number + " left the barbershop.");
                        number_of_available_chairs.release();
                        notCut=false;
                    }
                }
                catch (InterruptedException ex) {}
            }
        }
    }
}
