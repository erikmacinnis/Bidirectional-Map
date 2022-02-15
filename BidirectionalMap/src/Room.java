import com.sun.source.tree.LiteralTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class Room implements MazeRoom{
    /**
     * this class creates the rooms for the mazeMap
     */

    private String eLock = "";//necessary key to enter the east gate
    private Room eGate;//the room that east gate leads to
    private String nLock = "";//necessary key to enter the north gate
    private Room nGate;//the room that north gate leads to
    private String wLock = "";//necessary key to enter the west gate
    private Room wGate;//the room that west gate leads to
    private String sLock = "";//necessary key to enter the south gate
    private Room sGate;//the room that south gate leads to
    private String name;//name of the room
    private String key;//the key that is in the room

    @Override
    public Iterator<Action> iterator() {
        /**
         * this creates the MazeRoomIterator that tells the compiler what methods to use for the iteration
         returns the MazeRoomIterator of this Room
         */
        return new MazeRoomIterator(this);
    }

    public class MazeRoomIterator implements Iterator<Action>{
        /**
         * this makes the Room object an object that can be iterated over
         */
        Room currentRoom;
        int i;//counter for the hasNext and next methods

        public MazeRoomIterator(Room room){
            this.currentRoom = room;
            this.i = 0;
        }

        @Override
        public boolean hasNext() {
            /**
             * this checks whether the room has another room linked to it
             returns true if there is, false otherwise
             */
            while (this.i < 4){
                switch (this.i){
                    case 0: //checks if there's a room through the east gate
                        if (currentRoom.geteGate() != null) {
                            i++;
                            return true;
                        }
                        i++;
                        break;
                    case 1://checks if there's a room through the west gate
                        if (currentRoom.getwGate() != null) {
                            i++;
                            return true;
                        }
                        i++;
                        break;
                    case 2://checks if there's a room through the south gate
                        i++;
                        if (currentRoom.getsGate() != null) {
                            return true;
                        }
                        break;
                    case 3://checks if there's a room through the north gate
                        i++;
                        if (currentRoom.getnGate() != null) {
                            return true;
                        }
                        break;
                }
            }return false;
        }

        public Action next(){
            /**
             * this will create an action for the direction that a gate is present
             */
            switch (i) {
                case 1://if there's a gate to the east this creates the action to the east
                    return new Action("east", currentRoom.geteLock(), currentRoom.geteGate());

                case 2://if there's a gate to the west this creates the action to the west
                    return new Action("west", currentRoom.getwLock(), currentRoom.getwGate());

                case 3://if there's a gate to the south this creates the action to the south
                    return new Action("south", currentRoom.getsLock(), currentRoom.getsGate());

                case 4://if there's a gate to the north this creates the action to the north
                    return new Action("north", currentRoom.getnLock(), currentRoom.getnGate());

            }return null;
        }
    }

    public Room(String name, String key){
        this.name = name;
        this.key = key;
    }
    public Room geteGate(){
        return this.eGate;
    }

    public void seteGate(Room room){
        this.eGate = room;
    }

    public Room getnGate(){
        return this.nGate;
    }

    public void setnGate(Room room){
        this.nGate = room;
    }

    public Room getsGate(){
        return this.sGate;
    }

    public void setsGate(Room room){
        this.sGate = room;
    }

    public Room getwGate(){
        return this.wGate;
    }

    public void setwGate(Room room){
        this.wGate = room;
    }

    public String geteLock() {
        return this.eLock;
    }

    public void seteLock(String colour){
        this.eLock = colour;
    }

    public String getwLock(){
        return this.wLock;
    }

    public void setwLock(String colour){
        this.wLock = colour;
    }

    public String getnLock(){
        return this.nLock;
    }

    public void setnLock(String colour){
        this.nLock = colour;
    }

    public String getsLock(){
        return this.sLock;
    }

    public void setsLock(String colour){
        this.sLock = colour;
    }

    public String name(){
        return this.name;
    }

    public String roomKey(){
        return this.key;
    }

    public void setRoomKey(String key) {this.key = key;}
}

