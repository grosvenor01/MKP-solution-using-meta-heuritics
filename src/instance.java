import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class instance {
    ArrayList<knapsack> knapsack_arr;
	ArrayList<obj> obj_arr;
	int objects_num;
	int knapsack_num;

    public instance() {
		int max_weightk = 2000;// use it to generate a max weight for all knapsacks
		int min_weightk = 20;// use it to generate a min weight for all knapsacks
		int max_weighto = 150;// use it to generate a max weight for all objects
		int min_weighto = 20;// use it to generate a min weight for the objects
		int max_valo = 100;// use it to generate a max value for the objects
		int min_valo = 1;// use it to generate a min value for all objects
		knapsack new_knap;
		obj new_obj;
		Scanner scanner = new Scanner(System.in);
		Random random = new Random();

		knapsack_arr = new ArrayList<knapsack>(); // array of knapsakcs
		obj_arr = new ArrayList<obj>(); // array of objects
        System.out.println("enter the number of knaposack");
		knapsack_num = scanner.nextInt();
		for (int i = 0; i < knapsack_num; i++) {
			int max = random.nextInt(max_weightk - 10 + 1) + min_weightk;
			new_knap = new knapsack(max);
			knapsack_arr.add(new_knap);
		}
        System.out.println("enter the number of objects");
		objects_num = scanner.nextInt();
		for (int i = 0; i < objects_num; i++) {
			int max = random.nextInt(max_weighto - 10 + 1) + min_weighto;
			int max_val = random.nextInt(max_valo - 10 + 1) + min_valo;
			new_obj = new obj(max, max_val);
			obj_arr.add(new_obj);
		}
		scanner.close();
		System.out.println("Instance created successfully!");
        for(int i=0 ; i<obj_arr.size();i++){
            obj_arr.get(i).display();
        }
        for(int i=0 ; i<knapsack_arr.size();i++){
            knapsack_arr.get(i).display();
        }
	}
    public solution generate_sol(){
        solution sol = new solution();
        sol.generat_solution(knapsack_arr, obj_arr);
        sol.set_fitness(obj_arr);
        sol.display();
        System.out.println("dady");
        return sol;
    }
}
