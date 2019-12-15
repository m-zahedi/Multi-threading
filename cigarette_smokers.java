import java.util.Random;
public class main {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        new agent(monitor).start();
        new cigar_smoker("cigar smoker 1", "tabacco", monitor).start();
        new cigar_smoker("cigar smoker 2", "paper",   monitor).start();
        new cigar_smoker("cigar smoker 3", "glue",    monitor).start();
    }
}
class cigar_smoker extends Thread {
    private String smoker_material;
    private Monitor monitor;
    public cigar_smoker(String cigar_smoker_number_, String material, Monitor monitor) {
        this.smoker_material = material;
        this.monitor = monitor;
        setName(cigar_smoker_number_);
    }
    public void run() {
        while (true) {
            this.monitor.smoke(getName(), this.smoker_material);
        }
    }
}
class agent extends Thread {
    private int random_material;
    private Random random = new Random();
    private String first_material  = new String();
    private String second_material = new String();
    public Monitor monitor;
    public agent(Monitor monitor) {
        this.monitor = monitor;
    }
    public void run() {
        while (true) {
            this.random_material = this.random.nextInt(3);
            switch (this.random_material) {
                case 0:
                    this.first_material = "tabacco";
                    this.second_material = "paper";
                    break;
                case 1:
                    this.first_material = "tabacco";
                    this.second_material = "glue";
                    break;
                case 2:
                    this.first_material = "glue";
                    this.second_material = "paper";
                    break;
                default:
                    break;
            }
            this.monitor.number_of_materials(this.first_material, this.second_material);
        }
    }
}
class Monitor {
    private int number_of_materials = 0, n = 2;
    private String first_material, second_material  = new String();
    public synchronized void number_of_materials(String first_material_, String second_material_) {
        try {
            while (number_of_materials == n) //if both materials have been put on the table
                this.wait();
                this.first_material = first_material_;
            this.second_material = second_material_;
            System.out.println("the agent put " + this.first_material + " and " + this.second_material + " on the table.");
            this.number_of_materials = 2;
            this.notify();
        } catch (Exception e) {}
    }
    public synchronized void smoke(String cigar_smoker_number, String material) {
        try {
            while (number_of_materials == 0) { //if nothing is on the table
                this.wait();
            }
            if (!this.haveThisIngredient(material)) {
                System.out.println( cigar_smoker_number + " have " + material
                        + ". So he makes a cigar with " + this.first_material + " and " + this.second_material + " which are on the table.");
                System.out.println("smoking...");
                this.number_of_materials = 0;
                this.notifyAll();
            } else
                this.notifyAll();

        } catch (Exception e) {}
    }
    public boolean haveThisIngredient(String ing) {
        return this.first_material.equals(ing) || this.second_material.equals(ing);
    }
}
