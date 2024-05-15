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
    float min_value ;
    public instance(int knap , int obj) {
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
        min_value = random.nextFloat();
        knapsack_arr = new ArrayList<knapsack>(); // array of knapsacks
        obj_arr = new ArrayList<obj>(); // array of objects
        knapsack_num = knap;
        for (int i = 0; i < knapsack_num; i++) {
            int max = random.nextInt(max_weightk - 10 + 1) + min_weightk;
            new_knap = new knapsack(max);
            knapsack_arr.add(new_knap);
        }

        objects_num = obj;
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
        Sref.generat_solution(knapsack_arr, obj_arr, min_value);
        Sref.set_fitness(obj_arr);
    }

    public solution Generate_searchPoints(solution referenced, int flip) {
        solution currentSolution = referenced;
        solution neighbor = new solution();
        Random random = new Random();
        for (int i = 0; i < flip; i++) {
            int random_position = random.nextInt(currentSolution.objects.size());
            int random_knapsack = random.nextInt(knapsack_num);
            neighbor.objects = new ArrayList<>(currentSolution.objects);
            neighbor.objects.set(random_position, random_knapsack);
            neighbor.set_fitness(obj_arr);
        }
        if (neighbor.fitness_val > referenced.fitness_val) {
            referenced = neighbor;
        }
        return referenced;
    }

    public solution Generate_neighbore(solution referenced) {
        solution currentSolution = referenced;
        solution neighbor = new solution();
        Random random = new Random();
        int flip = random.nextInt(10) + 1;
        for (int i = 0; i < flip; i++) {
            int random_position = random.nextInt(currentSolution.objects.size());
            int random_knapsack = random.nextInt(knapsack_num);
            neighbor.objects = new ArrayList<>(currentSolution.objects);
            neighbor.objects.set(random_position, random_knapsack);
            neighbor.set_fitness(obj_arr);
        }
        if (neighbor.fitness_val > referenced.fitness_val) {
            referenced = neighbor;
        }
        referenced.distance = 5 / flip;
        return referenced;
    }

    public solution BSO_solve(int maxIter) {
        taboo = new HashSet<solution>(); // use this set to track old solutions
        SearchPoints = new ArrayList<solution>();
        int counter = 0, BeeNumber = 5; // generate a random number of max iterations and random Bee Number
        boolean optimal = false;
        int chances = 0, maxchances = 4;
        solution generated;
        Dance = new ArrayList<solution>();
        taboo.add(Sref);
        while (counter < maxIter && !optimal) {
            for (int i = 0; i < BeeNumber; i++) { // create a search points
                generated = Generate_searchPoints(Sref, 2);
                taboo.add(generated);
                SearchPoints.add(generated);
            }
            for (int bee = 0; bee < BeeNumber; bee++) { // hna ndirou flip machi ngeneriw new solution my friend
                generated = Generate_neighbore(SearchPoints.get(bee));
                Dance.add(generated);
                taboo.add(generated);
            }

            /*
             * remarque :
             * lazem tmanipuler bach matzitch f la liste taboo li yexistiw deja
             * check sref 3la hssab distance machi only el fitness val
             * test
             */
            Collections.sort(Dance, Comparator.comparing(solution -> solution.fitness_val / solution.distance));

            if (Sref.fitness_val == Dance.get(Dance.size() - 1).fitness_val) {
                chances++;
                if (chances >= maxchances) {
                    optimal = true;
                }
            } else {
                chances = 0;
                counter++;
                Sref = Dance.get(Dance.size() - 1);
                Dance.clear();
                SearchPoints.clear();
            }
        }
        return Sref;
    }

    public solution generate_solGA() {
        solution parent = new solution();
        parent.generat_solution(knapsack_arr, obj_arr,min_value);
        parent.set_fitness(obj_arr);
        return parent;
    }

    public solution crossover(solution sol1, solution sol2) {
        Random random = new Random();
        ArrayList<Integer> child = new ArrayList<>();
        int crossover_point = random.nextInt(sol1.objects.size()) + 1;
        for (int i = 0; i < crossover_point; i++) {
            child.add(sol1.objects.get(i));
        }
        for (int i = crossover_point; i < sol1.objects.size(); i++) {
            child.add(sol2.objects.get(i));
        }
        solution generated = new solution(child, 0);
        generated.set_fitness(obj_arr);
        return generated;
    }

    public solution mutation(solution sol) {
        Random random = new Random();
        int random_position = random.nextInt(sol.objects.size());
        int random_knapsack = random.nextInt(knapsack_num);
        sol.objects.set(random_position, random_knapsack);
        sol.set_fitness(obj_arr);
        return sol;
    }

    public solution GA_solve() {
        /*
         * generer random number of solutions cbn
         * choisire par roulette les parent
         * appliquer des mono corssover depend de proba
         * appliquer mutation depend de proba
         * Test
         */
        Random random = new Random();
        int solution_set = random.nextInt(10);
        solution parent;
        ArrayList<solution> solutions = new ArrayList<solution>();
        // generation de solution
        while (solutions.size()<solution_set) {
            parent = generate_solGA();
            if (!solutions.contains(parent) && parent.isValidSolution(knapsack_arr, obj_arr,min_value)) {
                solutions.add(parent);
            }
        }
        // generation des fils
        solution random_parent1;
        solution random_parent2;
        solution fils = new solution();

        for (int i = 0; i < 100; i++) {
            if (solutions.size() >= 2) {
                random_parent1 = solutions.get(random.nextInt(solutions.size()));
                random_parent2 = solutions.get(random.nextInt(solutions.size()));
                fils = crossover(random_parent1, random_parent2);
                fils = mutation(fils);
                fils.set_fitness(obj_arr);
                Collections.sort(solutions, Comparator.comparing(solution -> solution.fitness_val));
                solutions.remove(0); // the parent have the less fitness value
                if(fils.isValidSolution(knapsack_arr, obj_arr,min_value)){
                    solutions.add(fils);
                }
                
            }
        }
        return solutions.get(solutions.size() - 1);

    }
}
