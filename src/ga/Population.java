
package ga;

import java.util.ArrayList;
import java.util.Random;

public class Population {
    int n;
    ArrayList<Individual> pop;
    Random rand;

    public Population(int n, Random rand) {
        this.n = n;
        this.rand = rand;
        pop = new ArrayList<Individual>();
        for (int i = 0; i < Parameters.POP_SIZE; i++) {
            Individual ind = new Individual(this.n, this.rand);
            pop.add(ind);
        }
    }

    public void print() {
        for (int i = 0; i < Parameters.POP_SIZE; i++) {
            pop.get(i).print();
        }
    }
}