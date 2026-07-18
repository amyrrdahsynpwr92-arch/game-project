package game_board;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import cards.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import set_undo_and_redo.UndoAction;
import util.Handle_PreGame;
import util.Handle_TurningGame;
import util.Player;
import util.Sector;
import graph.*;
import save_and_load.*;

public class Map extends Pane{
    private Node[][] nodes;
    private ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/images/backgrounds/bg.png")));
    private DoubleProperty hGap = new SimpleDoubleProperty(50);
    private DoubleProperty vGap = new SimpleDoubleProperty(50);
    private Handle_PreGame handle_preGame;
    private Handle_TurningGame handle_TurningGame;
    private ArrayList<Sector> sectors = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private DrawBoard board;
    private UndoAction undoAction;
    private int n;
    private LongestPath longestPath;

    public Map(int numberOfPlayers, ArrayList<Player> players, DrawBoard board, int n){
        undoAction = board.getTopSide().getUndoAction();
        this.n = n;
        this.players = players;
        this.handle_TurningGame = new Handle_TurningGame(players);
        handle_preGame = new Handle_PreGame(players, handle_TurningGame);
        nodes = new Node[n+1][n+1];
        this.board = board;
        switch(n){
            case 5: 
                hGap.bind(widthProperty().divide(6.7));
                vGap.bind(heightProperty().divide(6.7));
                break;
            case 6: 
                hGap.bind(widthProperty().divide(7.8));
                vGap.bind(heightProperty().divide(7.8));
                break;
            case 7: 
                hGap.bind(widthProperty().divide(8.9));
                vGap.bind(heightProperty().divide(8.9));
                break;
            case 8: 
                hGap.bind(widthProperty().divide(10.0));
                vGap.bind(heightProperty().divide(10.0));
                break;
            case 9: 
                hGap.bind(widthProperty().divide(11.1));
                vGap.bind(heightProperty().divide(11.1));
                break;
            case 10: 
                hGap.bind(widthProperty().divide(12.2));
                vGap.bind(heightProperty().divide(12.4));
                break;    
        }
        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        background.setSmooth(true);
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,CornerRadii.EMPTY,Insets.EMPTY)));
        this.getChildren().add(background);
        if(board.getLoadClass() == null)
            drawGraph();
        else 
            loadGraph();
    }
    public void drawGraph(){
        List<ResourceCard> cards = Arrays.asList(new Capital(), new Cloud(), new Data(), new Null(), new Patent(), new Talent());
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                Sector sector = new Sector(cards.get(new Random().nextInt(6)),j, i, -1);
                sector.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                sector.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                sector.getSectorShape().widthProperty().bind(hGap);
                sector.getSectorShape().heightProperty().bind(vGap);
                sector.prefWidthProperty().bind(hGap);
                sector.prefHeightProperty().bind(vGap);
                sectors.add(sector);
                this.getChildren().add(sector);
            }
        }
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n+1; j++){
                Node node = new Node(j, i, handle_preGame, handle_TurningGame, sectors, board, undoAction, false, false, null);
                nodes[i][j] = node;
                node.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                node.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                node.getNodeShape().radiusProperty().bind(getWidth() > getHeight() ? widthProperty().divide(70) : heightProperty().divide(70));
            }
        }
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n; j++){
                Edge e = new Edge(nodes[i][j], nodes[i][j+1], handle_preGame, handle_TurningGame, i*n+j, board, undoAction, false, null);
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j+1)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
            }
        }
        for(int j=0; j<n+1; j++){
            for(int i=0; i<n; i++){
                Edge e = new Edge(nodes[i][j], nodes[i+1][j], handle_preGame, handle_TurningGame, n*(n+1) + j*n+i, board, undoAction, false, null);
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i+1)));
            }   
        }   
        longestPath = new LongestPath(edges);
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n+1; j++){
                nodes[i][j].setNodes(nodes);
                this.getChildren().add(nodes[i][j]);
            }
        }
    }
    public void loadGraph(){
        int index = 0;
        ArrayList<SectorData> sectorsData = board.getLoadClass().getSectorsData();
        ArrayList<NodeData> nodesData = board.getLoadClass().getNodesData();
        ArrayList<EdgeData> edgesData = board.getLoadClass().getEdgesData();
        handle_preGame = board.getLoadClass().getHandle_PreGame();
        handle_TurningGame = board.getLoadClass().getHandle_TurningGame();
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                Sector sector = new Sector(sectorsData.get(index).getResource() ,j, i, sectorsData.get(index).getNumber());
                sector.setMvpPlayers(sectorsData.get(index).getMvpPlayers());
                sector.setUnicornPlayers(sectorsData.get(index).getUnicornPlayers());
                sector.setHasAuditor(sectorsData.get(index).HasAuditor());
                index++;
                sector.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                sector.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                sector.getSectorShape().widthProperty().bind(hGap);
                sector.getSectorShape().heightProperty().bind(vGap);
                sector.prefWidthProperty().bind(hGap);
                sector.prefHeightProperty().bind(vGap);
                sectors.add(sector);
                this.getChildren().add(sector);
            }
        }
        index = 0;
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n+1; j++){
                Node node = new Node(j, i, handle_preGame, handle_TurningGame, sectors, board, undoAction, nodesData.get(index).HasMVP(), nodesData.get(index).HasUnicorn(), nodesData.get(index).getColor());
                index++;
                nodes[i][j] = node;
                node.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                node.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                node.getNodeShape().radiusProperty().bind(getWidth() > getHeight() ? widthProperty().divide(70) : heightProperty().divide(70));
            }
        }
        index = 0;
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n; j++){
                Edge e = new Edge(nodes[i][j], nodes[i][j+1], handle_preGame, handle_TurningGame, i*n+j, board, undoAction, edgesData.get(index).HasPartnership(), edgesData.get(index).getColor());
                index++;
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j+1)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
            }
        }
        for(int j=0; j<n+1; j++){
            for(int i=0; i<n; i++){
                Edge e = new Edge(nodes[i][j], nodes[i+1][j], handle_preGame, handle_TurningGame, n*(n+1) + j*n+i, board, undoAction, edgesData.get(index).HasPartnership(), edgesData.get(index).getColor());
                index++;
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i+1)));
            }   
        }   
        index = 0;
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n+1; j++){
                Node node = nodes[i][j];
                for(Integer ind: nodesData.get(index).getLinkedPartnerships()){
                    node.getLinkedPartnerships().add(edges.get(ind));
                }
                index++;
            }
        }
        longestPath = new LongestPath(edges);
        longestPath.setMaxDistance(board.getLoadClass().getMaxDistance());
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n+1; j++){
                nodes[i][j].setNodes(nodes);
                this.getChildren().add(nodes[i][j]);
            }
        }
    }

    public ArrayList<Sector> getSectors() {
        return sectors;
    }
    public Node[][] getNodes() {
        return nodes;
    }
    public ArrayList<Edge> getEdges() {
        return edges;
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public Handle_PreGame getHandle_PreGame() {
        return handle_preGame;
    }
    public Handle_TurningGame getHandle_TurningGame() {
        return handle_TurningGame;
    }
    public LongestPath getLongestPath(){
        return longestPath;
    }
}
