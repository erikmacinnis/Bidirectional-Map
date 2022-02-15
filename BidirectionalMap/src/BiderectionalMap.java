import java.util.HashMap;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * This class creates a map of linked rooms for a given file with the same format as the maze.txt file
 */
public class BiderectionalMap extends MazeMap {

    File fileName;
    Scanner file1;//used for the first iteration
    Scanner file2;//used for the second iteration
    HashMap<String, Room> roomMap;//dictionary of all the rooms where the key is the room name and the value is the room

    public BiderectionalMap(File fileName) {

        this.fileName = fileName;
        this.roomMap = new HashMap<String, Room>();//initialise the dictionary for the object

        try {
            this.file1 = new Scanner(fileName);
            this.file2 = new Scanner(fileName);
        }
        catch (FileNotFoundException e) {//makes sure there isn't an issue if the given file name is wrong
            System.out.println("File not found ):");
        }

        int firstLine = 0;//this counter is used to indicate the first line of the file
        while (file1.hasNextLine()) {//this loop will iterate over each line in the file, the goal is to create a dictionary of all the rooms

            String line = file1.nextLine();//line is current line that the loop is parsing over

            if (firstLine == 0){
                Room enter = new Room(getNextRoom(line), "");//creates the entrance room
                roomMap.put(enter.name(), enter);//puts the room into the dictionary
                entrance = enter;//this initialises the dummy node of the linked rooms
                firstLine++;
            }

            if (!roomMap.containsKey(getRoom(line))) roomMap.put(getRoom(line), new Room(getRoom(line), ""));//if adds the room to the list if it isn't there yet

            if (!roomMap.containsKey(getNextRoom(line))) roomMap.put(getNextRoom(line), new Room(getNextRoom(line), ""));//if adds the room to the list if it isn't there yet
        }

        while (file2.hasNextLine()){//the goal of this loop is to parse over each line and create the necessary links between the rooms and add the keys to the rooms

            String line = file2.nextLine();//current line that is being parsed over

            if (firstLine == 1){//since we know that the entrance is on the first line we set the link from the entrance to roomA
                roomMap.get(getNextRoom(line)).setnGate(roomMap.get(getRoom(line)));//sets the link from the Entrance to room A
                firstLine++;
            }

            if (hasRoomKey(line)) roomMap.get(getRoom(line)).setRoomKey(getRoomKey(line));//sets key into the room if it has one

            else {
                setRightGate(roomMap.get(getRoom(line)), roomMap.get(getNextRoom(line)), line);//sets the link from the first room to the second room
                setNextRightGate(roomMap.get(getRoom(line)), roomMap.get(getNextRoom(line)), line);//sets the link from the second room to the first room

                if (hasRequiredKey(line)) setRightLockedGate(roomMap.get(getRoom(line)), line);//sets the necessary key to create that link if it is necessary
            }
        }



    }

    /**
     * this checks if a key is required to enter a room from another room
     * @param line
     * @return true if there is a required key, false otherwise
     */
    public boolean hasRequiredKey(String line) {
        if (Character.isLetter(line.charAt(line.indexOf('<') + 1))) return true;
        else return false;
    }

    /**
     * this checks if a line is just setting a key into a room
     * @param line
     * @return true if it is, false otherwise
     */
    public boolean hasRoomKey(String line) {
        if (Character.isUpperCase(line.charAt(2))) return true;
        else return false;
    }

    /**
     * this gets the name of the first room in the line
     * @param line
     * @return returns the name of the room as a String
     */
    public String getRoom(String line) {
        return Character.toString(line.charAt(0));
    }

    /**
     * this gets the room key from a line that is setting a room key
     * @param line
     * @return the colour of the room key as a String
     */
    public String getRoomKey(String line) {
        char[] chars = line.substring(2, line.length()).toCharArray();
        String colour = "";
        for (char c : chars) {
            if (Character.isUpperCase(c)) colour += c;
            else break;
        }
        return colour;
    }

    /**
     * this gets the key when a gate requires a key to make a link
     * @param line
     * @return the colour of the key required to make the link
     */
    public String getRequiredKey(String line) {
        char[] chars = line.substring(line.indexOf('<') + 1, line.length()).toCharArray();
        String colour = "";
        for (char c : chars) {
            if (Character.isLetter(c)) colour += c;
            else break;
        }
        return colour;
    }

    /**
     * this gets the name of the second room in the line
     * @param line
     * @return the second room in the line as a String
     */
    public String getNextRoom(String line) {
        char[] chars = line.substring(line.indexOf('>') + 2, line.length()).toCharArray();
        String room = "";
        for (char c : chars) {
            if (Character.isLetter(c)) room += c;
            else break;
        }
        return room;
    }

    /**
     * gets the direction that the second room in the line receives a portal
     * @param line
     * @return the first letter of the direction as a char
     */
    public char getNextDir(String line) {
        char c = line.charAt(line.indexOf('>') + 4);
        return c;
    }

    /**
     * gets the direction that the first room in the lines portal is going from
     * @param line
     * @return the first letter of the direction as a char
     */
    public char getDir(String line) {
        return line.charAt(2);
    }

    /**
     * sets the gate from a first room to the second room in the right direction
     * @param room first room in the line
     * @param nextRoom second room in the line
     * @param line
     */
    public void setRightGate(Room room, Room nextRoom, String line) {
        switch (getDir(line)) {
            case 'n':
                room.setnGate(nextRoom);
                break;
            case 's':
                room.setsGate(nextRoom);
                break;
            case 'e':
                room.seteGate(nextRoom);
                break;
            case 'w':
                room.setwGate(nextRoom);
                break;
        }
    }

    /**
     * sets the gate from the second room to the first room in the right direction
     * @param room first room in the line
     * @param nextRoom second room in the line
     * @param line
     */
    public void setNextRightGate(Room room, Room nextRoom, String line) {
        switch (getNextDir(line)) {
            case 'n':
                nextRoom.setnGate(room);
                break;
            case 's':
                nextRoom.setsGate(room);
                break;
            case 'e':
                nextRoom.seteGate(room);
                break;
            case 'w':
                nextRoom.setwGate(room);
                break;
        }
    }

    /**
     * sets the necessary key to enter a gate to the right direction
     * @param room first room in the line
     * @param line
     */
    public void setRightLockedGate(Room room, String line) {
        switch (getDir(line)) {
            case 'n':
                room.setnLock(getRequiredKey(line));
                break;
            case 'w':
                room.setwLock(getRequiredKey(line));
                break;
            case 'e':
                room.seteLock(getRequiredKey(line));
                break;
            case 's':
                room.setsLock(getRequiredKey(line));
                break;
        }
    }
    @Override
    /**
     * calls the mazeReport constructor
     */
    public void mazeReport() {
        super.mazeReport();
    }
}

