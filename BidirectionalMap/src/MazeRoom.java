public interface MazeRoom extends Iterable<Action> {
    /**
     * The name of the room is a String
     * @return the name of the room
     */
    public String name();

    /**
     * The description will be a color if there is a key or an empty string otherwise ("")
     * @return String indicating the color of the key (empty string if no key is present)
     */
    public String roomKey();

}



