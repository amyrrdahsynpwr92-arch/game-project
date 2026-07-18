package save_and_load;
import game_board.*;
import util.*;
import java.util.*;
import java.io.*;
import graph.*;
import cards.*;

public class Save {
    private ObjectOutputStream toFile;
    public Save(DrawBoard board){
        try{
            toFile = new ObjectOutputStream(new FileOutputStream("data.dat"));
            save(board);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public void save(DrawBoard board) throws IOException{
        int n = board.getN();
        int gameStoppage = board.getLeftSide().getPlayerWon();
        int maxDistance = board.getMap().getLongestPath().getMaxDistance();
        ArrayList<Player> players = board.getPlayers();
        ArrayList<SectorData> sectorsData = new ArrayList<>();
        ArrayList<NodeData> nodesData = new ArrayList<>();
        ArrayList<EdgeData> edgesData = new ArrayList<>();
        Handle_PreGame handle_PreGame = board.getMap().getHandle_PreGame();
        Handle_TurningGame handle_TurningGame = board.getMap().getHandle_TurningGame();
        String statusText = board.getTopSide().getStatusBox().getText();
        int num1 = board.getRightSide().getNum1();
        int num2 = board.getRightSide().getNum2();
        int sectorNumber = board.getDownSide().getSectorNumber();
        String reportsText = board.getDownSide().getTextArea().getText();
        boolean moveAuditor = board.getDownSide().getMoveAuditor();

        for(int i=0; i<board.getN()+1; i++){
            for(int j=0; j<board.getN()+1; j++){
                nodesData.add(new NodeData(board.getMap().getNodes()[i][j]));
            }
        }
        for(Sector sector: board.getMap().getSectors()){
            sectorsData.add(new SectorData(sector));
        }
        for(Edge edge: board.getMap().getEdges()){
            edgesData.add(new EdgeData(edge));
        }

        toFile.writeInt(n);
        toFile.flush();
        toFile.writeInt(gameStoppage);
        toFile.flush();
        toFile.writeInt(maxDistance);
        toFile.flush();
        toFile.writeBoolean(moveAuditor);
        toFile.flush();
        toFile.writeObject(players);
        toFile.flush();
        toFile.writeObject(nodesData);
        toFile.flush();
        toFile.writeObject(edgesData);
        toFile.flush();
        toFile.writeObject(sectorsData);
        toFile.flush();
        toFile.writeObject(handle_PreGame);
        toFile.flush();
        toFile.writeObject(handle_TurningGame);
        toFile.flush();
        toFile.writeObject(board.getTopSide().getMarket());
        toFile.flush();
        toFile.writeUTF(statusText);
        toFile.flush();
        toFile.writeInt(num1);
        toFile.flush();
        toFile.writeInt(num2);
        toFile.flush();
        toFile.writeInt(sectorNumber);
        toFile.flush();
        toFile.writeUTF(reportsText);
        toFile.flush();
    }
}
