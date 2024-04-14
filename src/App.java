public class App {
    public static void main(String[] args) throws Exception {
        instance cas_0 = new instance();
        cas_0.generate_sol();
        long startTime = System.currentTimeMillis();
        solution BSO_solve = cas_0.BSO_solve(10);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("BSO avec 1/2");
        BSO_solve.display();
        System.out.println(executionTime+"ms");


        startTime = System.currentTimeMillis();
        BSO_solve = cas_0.BSO_solve(50);
        endTime = System.currentTimeMillis();
        executionTime = endTime - startTime;
        System.out.println("BSO avec 1/2");
        BSO_solve.display();
        System.out.println(executionTime+"ms");

        startTime = System.currentTimeMillis();
        BSO_solve = cas_0.BSO_solve(100);
        endTime = System.currentTimeMillis();
        executionTime = endTime - startTime;
        System.out.println("BSO avec 1/2");
        BSO_solve.display();
        System.out.println(executionTime+"ms");

        startTime = System.currentTimeMillis();
        BSO_solve = cas_0.BSO_solve(150);
        endTime = System.currentTimeMillis();
        executionTime = endTime - startTime;
        System.out.println("BSO avec 1/2");
        BSO_solve.display();
        System.out.println(executionTime+"ms");

        startTime = System.currentTimeMillis();
        BSO_solve = cas_0.BSO_solve(200);
        endTime = System.currentTimeMillis();
        executionTime = endTime - startTime;
        System.out.println("BSO avec 1/2");
        BSO_solve.display();
        System.out.println(executionTime+"ms");
    }
}
