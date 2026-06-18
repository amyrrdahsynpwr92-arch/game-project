package set_undo_and_redo;
import java.util.ArrayList;
import game_board.Map;

public class saveStages {
    private ArrayList<Map> stages = new ArrayList<>();
    private int currentStage;
    public void addStage(Map map){
        stages.add(map);
        currentStage = stages.size()-1;
    }
    public Map getCurrentStage(){
        if(currentStage > 0){
            return stages.get(--currentStage); 
        }else{
            return stages.get(0);
        }
    }
}
