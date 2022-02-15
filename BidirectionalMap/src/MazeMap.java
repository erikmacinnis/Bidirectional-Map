import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * A Bidirectional map is a map where every gate/portal connects both ways
 * e.g. Room A east <-> Room B south
 * In a one directional map portals are one way only
 * e.g. Room A east -> Room B south
 * Finding a way out in an one directional map is more difficult because you can't backtrack
 **/
public abstract class MazeMap {
    // Dummy entrance and exit nodes
    protected MazeRoom entrance;

    /**
     * Prints a report indicating the rooms and keys that are accessible from the entrance
     **/
    public void mazeReport() {
        ArrayList<String> keys = new ArrayList<>();//keeps track of all room keys
        ArrayList<String> visited = new ArrayList<>();//name of rooms that were visited

        Stack<MazeRoom> toVisit = new Stack<>();//
        toVisit.add(entrance);//starts with the entrance room

        while (!toVisit.empty()) {//while toVisit not empty
            MazeRoom current = toVisit.pop();//current room we are in

            if (!visited.contains(current.name())) {//test if the current room was already visited
                visited.add(current.name());//if not then the current room name is added to the visited room names list
                if (!current.roomKey().equals(""))
                    keys.add(current.roomKey());//adds the key from the current room to the list of key names
                for (Action action : current) toVisit.add(action.room);//this will iterate through all the
            }
        }

        System.out.println("The following rooms are accessible from the entrance:");
        for (String room : visited) System.out.println(room);

        System.out.println("The following keys are accessible from the entrance:");
        for (String key : keys) System.out.println(key);
    }

    /**
     * This method will parse through all the possible actions in each room of the maze and will list each action in a file
     * @param filename this will be the name of the file that is created which holds all the actions
     * @return will return true if the file is saved properly
     * @throws IOException if the maze is empty an exception will be thrown
     */
    public boolean saveToFile(String filename) throws IOException {
        HashMap<String, ArrayList<Action>> actionMap = new HashMap<>();//map that holds all the information to print the actions
        ArrayList<String> visited = new ArrayList<>();//keeps track of the rooms that have already been visited

        Stack<MazeRoom> toVisit = new Stack<>();//holds the next room that is being planned to visit

        if (entrance instanceof MazeRoom && entrance != null) toVisit.add(entrance);//checks if the maze is empty
        else throw new IOException("The maze is empty");//if the maze is empty it will throw this exception



        while (!toVisit.empty()) {//while toVisit not empty
            MazeRoom current = toVisit.pop();//current room we are in
            ArrayList<Action> actionList = new ArrayList<>();//list of all the possible actions out of a room

            if (!visited.contains(current.name())) {//test if the current room was already visited
                visited.add(current.name());//if not then the current room name is added to the visited room names list
                for (Action action : current) {//this will iterate through all the actions in the current room
                    toVisit.add(action.room);//adds the room that the current room's action leads to the toVisit stack
                    actionList.add(action);//adds the action to the list
                }
            }
            else continue;
            actionMap.put(current.name(), actionList);//adds the room name as the key and a list of all the actions out of the room as the value
        }

        PrintWriter writer;
        writer = new PrintWriter(new FileWriter("./" + filename + ".txt"));//initializes the writer variable to a new PrintWriter object
        ArrayList<String> finishedRooms = new ArrayList<>();//list for rooms that have already had their action written to the file

        for (String room : actionMap.keySet()) {//iterates through all the rooms
            if (room.equals("Entrance")) continue;//skips over the entrance room
            for (Action a1 : actionMap.get(room)) {//iterates through a rooms possible actions
                if (finishedRooms.contains(a1.room.name())) continue; //checks if the specific action has already been done by another room
                if (a1.room.name().equals("Exit") || a1.room.name().equals("Entrance")) {//checks if the action leads to the entrance or exit
                    if (a1.key.equals(""))//checks if there is a key
                        writer.println(room + " " + a1.gate + " <-> " + a1.room.name());
                    else
                        writer.println(room + " " + a1.gate + " <" + a1.key + "> " + a1.room.name());
                } else {
                    for (Action a2 : actionMap.get(a1.room.name())) {//iterates through all the actions in the room that the a1 action leads to
                        if (a2.room.name().equals(room)) {//checks if a2 actions leads back to the original room variable
                            if (a1.key.equals(""))//checks if there is a key
                                writer.println(room + " " + a1.gate + " <-> " + a1.room.name() + " " + a2.gate);
                            else
                                writer.println(room + " " + a1.gate + " <" + a1.key + "> " + a1.room.name() + " " + a2.gate);
                            break;
                        }
                    }
                    finishedRooms.add(room);//adds the room to the finishedRooms list once it's done
                }
            }
        }
        writer.close();
        return true;
    }

    private Stack<String> alreadyVisited = new Stack<>();//all the rooms that have already been visited
    private HashMap<String, Action> route = new HashMap<>();//a list of all the actions to take in each room

    /**
     * This method will write the solution to the biderectionalMap.
     * It will do this by writing in a text file all the necessary actions to take in each room starting from the entrance all the way to the exit.
     * @param filename the name of the file of the solution
     * @return returns true if the file properly created
     * @throws Exception throws an exception if the exit is missing or if the map has no rooms
     */
    public boolean noKeySolution(String filename) throws Exception{

        if (entrance instanceof MazeRoom && entrance != null);//checks if there is a room
        else throw new IOException("The maze is empty");

        findNextRoom(entrance);//calls the recursive method that adds the solution path to the route map

        PrintWriter writer;
        writer = new PrintWriter(new FileWriter("./" + filename + ".txt"));//creates the PrintWriter object

        for (String name : alreadyVisited){//iterates through the visited rooms
            Action action = route.get(name);//will get the actions associated from the room
            if (name.equals("Entrance")) writer.println(name + " -> " + action.room.name());//checks if the room is the entrance
            else writer.println(name + " -" + action.gate + "> " + action.room.name());//writes it like this for the rest of the rooms
        }
        writer.close();
        return true;
    }

    private boolean done = false;//helps the recursive stop when it reaches the exit
    private int counter = 0;//will count the number of times the loop returns to room A

    /**
     * This method will recursively find the next room on the path to the exit
     * @param room the room we will start in
     * @return true if the method appropriately finds the solution
     * @throws Exception if there is no solution to the map this will get thrown
     */
    public boolean findNextRoom(MazeRoom room) throws Exception {
        if (done == true) return true;//checks if it's found the exit

        for (Action a1 : room) {//iterates through each action in a room
            if (done == true) return true;//checks if it's found the exit
            if (a1.room.name().equals("Exit")) {//checks if the action leads to the exit
                alreadyVisited.add(room.name());//adds the exit room name to the list
                done = true;//solution is found so we are done
                route.put(room.name(), a1);//ads the last action to the route map
                return true;
            }
            if (!alreadyVisited.contains(a1.room.name()) && !done) {//checks if the action leads to is not in the list and checks if the exit has not been reached
                alreadyVisited.add(room.name());//adds the room to the already visited list
                route.put(room.name(), a1);//adds the room to the route map
                findNextRoom(a1.room);//calls the function recursively to find the next room of the room that the action leads to
            } else {//if action leads to a room that has already been visited
                if (alreadyVisited.size() == 2) {//checks if only the entrance and the first room are in the alreadyVisited list
                    counter++;//adds 1 to the counter variable
                    if (counter >= 3)
                        throw new IOException("The maze has no solution");//checks if all the option from the first room don't work
                }
            }
        }
        if (!done) {//checks if the solution was found and this happens when and we get here if all the actions out of the current room don't work
            alreadyVisited.pop();//if so then it removes the current rooms
            route.remove(room.name());//removes the current room and action from the map
        }
        return false;
    }
}



