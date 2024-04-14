import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class instance {
    ArrayList<knapsack> knapsack_arr;
    ArrayList<obj> obj_arr;
    int objects_num;
    int knapsack_num;
    /* BSO VARIABLES NEEDED */
    solution Sref; // the initial bee solution
    HashSet<solution> taboo;
    ArrayList<solution> SearchPoints;
    ArrayList<solution> Dance;

    public instance() {
        int max_weightk = 2000; // use it to generate a max weight for all knapsacks
        int min_weightk = 20; // use it to generate a min weight for all knapsacks
        int max_weighto = 150; // use it to generate a max weight for all objects
        int min_weighto = 20; // use it to generate a min weight for the objects
        int max_valo = 100; // use it to generate a max value for the objects
        int min_valo = 1; // use it to generate a min value for all objects
        knapsack new_knap;
        obj new_obj;
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        knapsack_arr = new ArrayList<knapsack>(); // array of knapsacks
        obj_arr = new ArrayList<obj>(); // array of objects
        System.out.println("Enter the number of knapsacks:");
        knapsack_num = scanner.nextInt();
        for (int i = 0; i < knapsack_num; i++) {
            int max = random.nextInt(max_weightk - 10 + 1) + min_weightk;
            new_knap = new knapsack(max);
            knapsack_arr.add(new_knap);
        }
        System.out.println("Enter the number of objects:");
        objects_num = scanner.nextInt();
        for (int i = 0; i < objects_num; i++) {
            int max = random.nextInt(max_weighto - 10 + 1) + min_weighto;
            int max_val = random.nextInt(max_valo - 10 + 1) + min_valo;
            new_obj = new obj(max, max_val);
            obj_arr.add(new_obj);
        }
        scanner.close();
        System.out.println("Instance created successfully!");
        for (int i = 0; i < obj_arr.size(); i++) {
            obj_arr.get(i).display();
        }
        for (int i = 0; i < knapsack_arr.size(); i++) {
            knapsack_arr.get(i).display();
        }
    }

    public void generate_sol() {
        Sref = new solution();
        Sref.generat_solution(knapsack_arr, obj_arr);
        Sref.set_fitness(obj_arr);
        Sref.display();
    }

	public solution Generate_neighbore(solution referenced) {
		solution currentSolution = referenced;
		solution bestNeighbor = null;
	
		// Iterate over all objects in the current solution
		for (int i = 0; i < currentSolution.objects.size(); i++) {
			// Try moving the object to a different knapsack
			for (int j = 0; j < knapsack_arr.size(); j++) {
				// Skip if moving the object to the same knapsack
				if (currentSolution.objects.get(i) == j) {
					continue;
				}
				solution neighbor = new solution();
				neighbor.objects = new ArrayList<>(currentSolution.objects);
				neighbor.objects.set(i, j);
				neighbor.set_fitness(obj_arr);
				if (bestNeighbor == null || neighbor.fitness_val > bestNeighbor.fitness_val) {
					bestNeighbor = neighbor;
				}
			}
		}
		// Return the best neighbor solution found
		return bestNeighbor;
	}

    public solution BSO_solve(int maxIter) {
        taboo = new HashSet<solution>(); // use this set to track old solutions
        SearchPoints = new ArrayList<solution>();
        int /*maxIter = 10,*/ counter = 0 ,BeeNumber = 5; // generate a random number of max iterations and random Bee Number
        boolean optimal = false;
        solution generated;
        Dance = new ArrayList<solution>();
        taboo.add(Sref);
        while (counter < maxIter && !optimal) {
            for (int i = 0; i < BeeNumber; i++) { // create a search points
                generated = Generate_neighbore(Sref);
                taboo.add(generated);
                SearchPoints.add(generated);
            }
            for (int bee = 0; bee < BeeNumber; bee++) {
                generated = Generate_neighbore(SearchPoints.get(bee));
                Dance.add(generated);
                taboo.add(generated);
            }
			Collections.sort(Dance, Comparator.comparing(solution -> solution.fitness_val));
			if(Sref.fitness_val == Dance.get(Dance.size()-1).fitness_val){
				optimal =true;
			}
			else {
				counter ++;
				Sref = Dance.get(Dance.size()-1);
				Dance.clear();
				SearchPoints.clear();
			}
		}
		return Sref;
	}
}
