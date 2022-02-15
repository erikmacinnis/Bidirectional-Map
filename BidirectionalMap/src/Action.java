public class Action {
    public String gate;
    public String key;
    public MazeRoom room;

    public Action(String gate, String key, MazeRoom room){
        this.gate = gate;
        this.room = room;
        this.key = key;
    }
}
