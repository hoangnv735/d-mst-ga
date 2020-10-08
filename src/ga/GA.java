
package ga;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GA {
    int n;
    int d;
    double[][] weightMatrix;
    Random rand;

    public GA(Data data) {
        this.n = data.n;
        rand = new Random();   
        weightMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (i == j) {
                    weightMatrix[i][j] = 0;
                } else {
                    double dis = distance(data.nodes.get(i), data.nodes.get(j));
                    weightMatrix[i][j] = dis;
                    weightMatrix[j][i] = dis;
                }
            }
        }
    }

    public double distance(Node a, Node b) {
        return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
    }

    public int[][] decode(int[] pruferString) {
		int nVertices = pruferString.length + 2;
		int[][] tree = new int[nVertices][nVertices];
		int[] degree = new int[nVertices]; // store the degree of vertices

		for (int i = 0; i < nVertices - 2; i++) {
			int vertex = pruferString[i];
			degree[vertex]++;
		}

		// build the tree from prufer number code
		for (int i = 0; i < nVertices - 2; i++) {
			int curVertex = pruferString[i];
			for (int j = 0; j < nVertices; j++) {
				if (degree[j] == 0) {
					tree[curVertex][j] = tree[j][curVertex] = 1;
					degree[curVertex]--;
					degree[j]--;
					break;
				}
			}
		}
		// if Prufer number code have no element left
		int nLeftVertices = 2;
		int[] twoLeftVertices = new int[nLeftVertices];
		for (int i = 0; i < nVertices; i++) {
			if (degree[i] == 0) {
				twoLeftVertices[nLeftVertices - 1] = i;
				nLeftVertices--;
			}
			if (nLeftVertices < 0) {
				break;
			}
		}
		tree[twoLeftVertices[0]][twoLeftVertices[1]] = tree[twoLeftVertices[1]][twoLeftVertices[0]] = 1;

		return tree;
    }
    
    public double calCost(int[][] tree, double[][] weightMatrix, int n_vertices) {
		double cost = 0;
		boolean[] isVisited = new boolean[n_vertices];
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(0);

		while (!queue.isEmpty()) {
			int parentVertex = queue.poll();
			isVisited[parentVertex] = true;
			for (int i = 0; i < n_vertices; i++) {
				if (tree[parentVertex][i] > 0 && !isVisited[i]) {
					queue.add(i);
					isVisited[i] = true;
					cost += weightMatrix[parentVertex][i]; 
				}
			}
		}
        return cost;
    }

    public void evaluateIndividual(Individual ind) {
        int[][] tree = decode(ind.pruferCode);
        double cost = calCost(tree, weightMatrix, ind.n);
        ind.fitness = -cost;
    }

    public ArrayList<Individual> crossover(Individual parent1, Individual parent2) {
        Individual child1 = parent1.clone();
		Individual child2 = parent2.clone();
        ArrayList<int[]> pathList = Crossover.twoPointCrossover(parent1.pruferCode, parent2.pruferCode, rand);
        child1.pruferCode = pathList.get(0);
        child2.pruferCode = pathList.get(1);
		ArrayList<Individual> list = new ArrayList<Individual>();
		list.add(child1);
		list.add(child2);
		return list;
    }

    public void mutate(Individual ind) {
        int pos = rand.nextInt(ind.n-2);
        int val = rand.nextInt(ind.n);
        ind.pruferCode[pos] = val;
    }

    public void fixDegree(Individual ind, int maxDegree) {
        int[] occurs = new int[ind.n];
        for (int i = 0; i < ind.n-2; i++) {
            occurs[ind.pruferCode[i]] += 1;
        }

        for (int i = 0; i < ind.n-2; i++) {
            if (occurs[ind.pruferCode[i]]+1 > maxDegree) {
                ArrayList<Integer> tmp = new ArrayList<Integer>();
                for (int j = 0; j < ind.n; j++) {
                    if (occurs[j]+1 < maxDegree) {
                        tmp.add(j);
                    }
                }
                int val = tmp.get(rand.nextInt(tmp.size()));
                int oldVal = ind.pruferCode[i];
                ind.pruferCode[i] = val;
                occurs[val] += 1;
                occurs[oldVal] -= 1;
            }
        }
    }

    public ArrayList<Individual> nextPop(Population population) {
        int[] idArray = new int[Parameters.POP_SIZE];
        for (int i = 0; i < Parameters.POP_SIZE; i++) {
            idArray[i] = i;
        }
        Collections.shuffle(Arrays.asList(idArray), rand);        
        ArrayList<Individual> offsprings = new ArrayList<Individual>();
        for (int i = 0; i < Parameters.POP_SIZE; i += 2) {
            ArrayList<Individual> offs = new ArrayList<Individual>();
            offs = crossover(population.pop.get(idArray[i]), population.pop.get(idArray[i+1]));
            double m = rand.nextDouble();
            if (m < Parameters.MUTATION_RATE) {
                mutate(offs.get(0));
            }
            m = rand.nextDouble();
            if (m < Parameters.MUTATION_RATE) {
                mutate(offs.get(1));
            }
            offsprings.addAll(offs);
        }
        return offsprings;
    }

    public void evaluateNewPop(ArrayList<Individual> newPop) {
        for (int i = 0; i < newPop.size(); i++) {
            evaluateIndividual(newPop.get(i));
        }
    }

    // Merge two current population and new population then select the best
    public ArrayList<Individual> select(ArrayList<Individual> currentPop, ArrayList<Individual> offspringsPop) {
        ArrayList<Individual> intermediatePop = new ArrayList<Individual>();
        intermediatePop.addAll(currentPop);
        intermediatePop.addAll(offspringsPop);
        Collections.sort(intermediatePop, (ind1, ind2) -> Double.compare(ind2.fitness, ind1.fitness));
        ArrayList<Individual> newPop = new ArrayList<Individual>(intermediatePop.subList(0, Parameters.POP_SIZE));
		return newPop;
    }

    public void evaluatePopulation(Population population) {
        for (int i = 0; i < Parameters.POP_SIZE; i++) {
            evaluateIndividual(population.pop.get(i));
        }
    }

    public void printBest(Population population) {
        int idBest = -1;
        double bestFitness = -100000000;
        for (int i = 0; i < Parameters.POP_SIZE; i++) {
            if (population.pop.get(i).fitness > bestFitness) {
                idBest = i;
                bestFitness = population.pop.get(i).fitness;
            }
        }
        System.out.println("Best: " + (-population.pop.get(idBest).fitness));
        population.pop.get(idBest).print();

    }

    public void run(int seed) {
        rand.setSeed(seed);
        Population population = new Population(this.n, this.rand);
        // population.print();
        evaluatePopulation(population);
        for (int i = 0; i < Parameters.MAX_GENERATION; i++) {
            ArrayList<Individual> newPop = nextPop(population);
			evaluateNewPop(newPop);
            population.pop = select(population.pop, newPop);
            System.out.println("Generation " + (i+1));
            printBest(population);
        }
        // printBest(population);
    }   
}