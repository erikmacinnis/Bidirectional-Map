import java.io.File;

public class DriverP2 {
    public static void main(String[] args) throws Exception{
        File mazeFile = new File("maze_test3.txt");

        BiderectionalMap newMap = new BiderectionalMap(mazeFile);

        try {
            newMap.noKeySolution("maze_solution");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
