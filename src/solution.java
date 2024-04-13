import java.util.ArrayList;
import java.util.Random;
public class solution {
    ArrayList<Integer> objects = new ArrayList<Integer>();
    float fitness_val;

    public solution(){
        objects = new ArrayList<Integer>();
        fitness_val = 0;
    }
    public solution(ArrayList<Integer> objs ,float fitness_value){
        objects = objs;
        fitness_val = fitness_value;
    }
    public void generat_solution(ArrayList<knapsack> knapsacks, ArrayList<obj> objects_arr){
        Random random = new Random();
        int value;
        boolean state = false;
        while(!state){
            for(int i=0 ; i < objects_arr.size();i++){
                value = random.nextInt(knapsacks.size()+1) -1;
                objects.add(value);
            }
            if(!isValidSolution(knapsacks,objects_arr)){
                objects.clear();
            }
            else {
                
                state=true;
            }
        }  
    }
    public boolean isValidSolution(ArrayList<knapsack> knapsacks, ArrayList<obj> objects_arr) {
        int[] knapsackWeights = new int[knapsacks.size()];
        for (int i = 0; i < objects_arr.size(); i++) {
            int knapsackIndex = objects.get(i);
            if (knapsackIndex != -1) {
                knapsackWeights[knapsackIndex] += objects_arr.get(i).weight;
            }
        }

        for (int i = 0; i < knapsacks.size(); i++) {
            if (knapsackWeights[i] > knapsacks.get(i).max_weight) {
                return false; // Solution is invalid
            }
        }
        for (int i = 0; i < knapsacks.size(); i++) {
            System.out.print(knapsackWeights[i]+" ");
        }
        System.out.println();
        return true; // Solution is valid
    }
    public void set_fitness(ArrayList<obj> objects_arr){
        float value =0;
        float weight =0;

        for(int i =0;i<objects.size();i++){
            if(objects.get(i)!=-1){
                value+=objects_arr.get(i).value;
                weight+=objects_arr.get(i).weight;
            }
        }
        fitness_val =  value/weight;
    }
    public void display(){
        System.out.println("==============Solution===============");
        for(int i=0;i<objects.size();i++){
            System.out.print(objects.get(i)+" ");
        }
        System.out.println("fitness value : "+fitness_val);
    }
}
