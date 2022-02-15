import java.io.File;

public class DriverP1 {
    public static void main(String[] args) throws Exception{
        File mazeFile = new File("maze_test3.txt");

        BiderectionalMap newMap = new BiderectionalMap(mazeFile);

        try {
            newMap.saveToFile("resaved");
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
