package save_and_load;
import java.io.*;
import java.util.ArrayList;
import util.*;
import game_board.*;
import javafx.scene.layout.BorderPane;
import cards.*;

public class Load {
    private ObjectInputStream fromFile;
    private GameInitiallization gameInitiallization;
    private int n;
    private int gameStoppage;
    int maxDistance;
    private ArrayList<Player> players;
    private ArrayList<SectorData> sectorsData;
    private ArrayList<NodeData> nodesData;
    private ArrayList<EdgeData> edgesData; 
    private Handle_PreGame handle_PreGame;
    private Handle_TurningGame handle_TurningGame;
    private Market market;
    private String statusText;
    private BorderPane currentPane;
    private int num1;
    private int num2;
    private int sectorNumber;
    private String reportsText;
    private boolean moveAuditor;

    public Load(BorderPane currentPane, GameInitiallization gameInitiallization){
        try{
            fromFile  = new ObjectInputStream(new FileInputStream("data.dat"));
            this.currentPane = currentPane;
            this.gameInitiallization = gameInitiallization;
            load();
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }
    public void load() throws IOException, ClassNotFoundException{
        n = fromFile.readInt();
        gameStoppage = fromFile.readInt();
        maxDistance = fromFile.readInt();
        moveAuditor = fromFile.readBoolean();
        players = (ArrayList<Player>)fromFile.readObject();
        nodesData = (ArrayList<NodeData>)fromFile.readObject();
        edgesData = (ArrayList<EdgeData>)fromFile.readObject();
        sectorsData = (ArrayList<SectorData>)fromFile.readObject();
        handle_PreGame = (Handle_PreGame)fromFile.readObject();
        handle_TurningGame = (Handle_TurningGame)fromFile.readObject();
        market = (Market)fromFile.readObject();
        statusText = fromFile.readUTF();
        num1 = fromFile.readInt();
        num2 = fromFile.readInt();
        sectorNumber = fromFile.readInt();
        reportsText = fromFile.readUTF();

        handle_PreGame.setCurrentColor();
        for(Player player: players){
            player.setColor();
            for(ResourceCard resource: player.getMyCards()){
                resource.setColor();
                resource.setImage();
            }
        }

        DrawBoard board = new DrawBoard(currentPane, players, n, this);
        gameInitiallization.setBoard(board);
    }
    public ArrayList<SectorData> getSectorsData(){
        return sectorsData;
    }
    public ArrayList<NodeData> getNodesData(){
        return nodesData;
    }
    public ArrayList<EdgeData> getEdgesData(){
        return edgesData;
    }
    public Handle_PreGame getHandle_PreGame(){
        return handle_PreGame;
    }
    public Handle_TurningGame getHandle_TurningGame(){
        return handle_TurningGame;
    }
    public Market getMarket(){
        return market;
    }
    public String getStatusText(){
        return statusText;
    }
    public int getNum1(){
        return num1;
    }
    public int getNum2(){
        return num2;
    }
    public int getSectorNumber(){
        return sectorNumber;
    }
    public String getReportsText(){
        return reportsText;
    }
    public boolean getMoveAuditor(){
        return moveAuditor;
    }
    public int getGameStoppage(){
        return gameStoppage;
    }
    public int getMaxDistance(){
        return maxDistance;
    }
}
