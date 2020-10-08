package ga;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Individual {
    
    public int n; // number of node 
    public int[] pruferCode; // Prufer code
    public double fitness;
    public Random rand;

    public Individual() {}

    public Individual(int n, Random rand) {
        assert n > 2: "Number of nodes must be bigger than 2";
        this.n = n;
        this.pruferCode = new int[n-2];
        this.rand = rand;
        ArrayList<Integer> tmpList = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            tmpList.add(i);
        }
        Collections.shuffle(tmpList, rand);
        for (int i = 0; i < n-2; i++) {
            pruferCode[i] = tmpList.get(i);
        }
    }

    public Individual clone() {
        Individual newInd = new Individual();
        newInd.n = this.n;
        newInd.pruferCode = this.pruferCode.clone();
        return newInd;
    }

    public void print() {
        System.out.print("Individual: ");
        System.out.println(Arrays.toString(pruferCode));
    }
}