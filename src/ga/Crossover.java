package ga;

import java.util.ArrayList;
import java.util.Random;

public class Crossover {
    public static ArrayList<int[]> twoPointCrossover(int[] father, int[] mother, Random rnd) {
		int genLength = father.length;
		int point = rnd.nextInt(genLength);
		int point2 = rnd.nextInt(genLength);
		while (point == point2) {
			point2 = rnd.nextInt(genLength);
		}
		int[] offspring1 = new int[genLength];
		int[] offspring2 = new int[genLength];
		ArrayList<int[]> offsprings = new ArrayList<int[]>();
		for (int i = 0; i < genLength; i++) {
			if (i < point) {
				offspring1[i] = father[i];
				offspring2[i] = mother[i];
			} else if (i <= point2) {
				offspring1[i] = mother[i];
				offspring2[i] = father[i];
			} else {
				offspring1[i] = father[i];
				offspring2[i] = mother[i];
			}
		}

		offsprings.add(offspring1);
		offsprings.add(offspring2);
		return offsprings;
	}

	public static ArrayList<int[]> onePointCrossover(int[] father, int[] mother, int genLength, Random rnd) {
		int point = rnd.nextInt(genLength);
		while (point == 0 || point == genLength - 1) {
			point = rnd.nextInt(genLength);
		}
		int[] offspring1 = new int[genLength];
		int[] offspring2 = new int[genLength];
		ArrayList<int[]> offsprings = new ArrayList<int[]>();
		for (int i = 0; i < genLength; i++) {
			if (i < point) {
				offspring1[i] = father[i];
				offspring2[i] = mother[i];
			} else {
				offspring1[i] = mother[i];
				offspring2[i] = father[i];
			}
		}

		offsprings.add(offspring1);
		offsprings.add(offspring2);
		return offsprings;
	}
}