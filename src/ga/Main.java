package ga;

import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        String filename = "att48.tsp";
        Data data = Util.readFile(filename);
        Util.printData(data);
        
        GA ga = new GA(data);
        ga.run(42);
    }
}