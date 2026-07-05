package game_board;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import game_board.DrawBoard;
import set_undo_and_redo.DrawUndoButton;
import javafx.scene.text.Font;
import util.Handle_PreGame;
import util.Handle_TurningGame;
import util.Player;
import util.Sector;
import card.*;
import graph.Edge;
import graph.Node;
import javafx.scene.shape.Rectangle;

public class Map extends Pane{
    private Node[][] nodes;
    private ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/images/backgrounds/bg.png")));
    private DoubleProperty hGap = new SimpleDoubleProperty(50);
    private DoubleProperty vGap = new SimpleDoubleProperty(50);
    private int numberOfPlayers;
    private Handle_PreGame handle_preGame;
    private Handle_TurningGame handle_TurningGame;
    private ArrayList<Sector> sectors = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private DrawBoard board;
    private DrawUndoButton undoButton;
    private int n;
    public Map(int numberOfPlayers, ArrayList<Player> players, DrawBoard board, int n){
        undoButton = board.getTopSide().getUndoButton();
        this.n = n;
        this.numberOfPlayers = numberOfPlayers;
        this.players = players;
        this.handle_TurningGame = new Handle_TurningGame(players);
        handle_preGame = new Handle_PreGame(players, handle_TurningGame);
        nodes = new Node[n+1][n+1];
        this.board = board;
        undoButton = board.getTopSide().getUndoButton();
        drawGraph();
    }
    public void drawGraph(){
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
            case 15: 
                hGap.bind(widthProperty().divide(17.9));
                vGap.bind(heightProperty().divide(17.9));
                break;    
        }
        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        background.setSmooth(true);
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,CornerRadii.EMPTY,Insets.EMPTY)));
        this.getChildren().add(background);
        List<ResourceCard> cards = Arrays.asList(new Capital(), new Cloud(), new Data(), new Null(), new Patent(), new Talent());
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                Sector sector = new Sector(cards.get(new Random().nextInt(6)),j, i);
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
                Node node = new Node(j, i, handle_preGame, handle_TurningGame, sectors, board, undoButton);
                nodes[i][j] = node;
                node.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                node.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                node.getNodeShape().radiusProperty().bind(getWidth() > getHeight() ? widthProperty().divide(70) : heightProperty().divide(70));
            }
        }
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n; j++){
                Edge e = new Edge(nodes[i][j], nodes[i][j+1], handle_preGame, handle_TurningGame, i*n+j, board, undoButton);
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
                Edge e = new Edge(nodes[i][j], nodes[i+1][j], handle_preGame, handle_TurningGame, 30 + j*n+i, board, undoButton);
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i+1)));
            }   
        }    
        for(Edge e: edges){
            e.setEdges(edges);
        }
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n+1; j++){
                nodes[i][j].setNodes(nodes);
                this.getChildren().add(nodes[i][j]);
            }
        }
        //new drawSourcesInformation(this);
    }
    // class drawSourcesInformation extends Pane{
    //     private DoubleProperty gap = new SimpleDoubleProperty(50);
    //     Pane pane;
    //     public drawSourcesInformation(Pane pane){
    //         this.pane = pane;
    //         gap.bind(pane.widthProperty().divide(6.7));
    //         //this.layoutXProperty().bind(widthProperty().subtract(gap.multiply(5)).divide(2).add(gap.multiply(5)));
    //        // this.layoutYProperty().bind(heightProperty().subtract(gap.multiply(5)).divide(2));
    //         arrangeComponents();
    //     }
    //     private void arrangeComponents(){
    //         List<Color> colors = Arrays.asList(Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED, Color.PURPLE);
    //         List<String> titles = Arrays.asList("Capital", "Cloud", "Data", "Patent", "Talent");
    //         for(int i=0; i<5; i++){
    //             HBox paneForComponent = new HBox(5);
    //             Rectangle rec = new Rectangle();
    //             rec.setWidth(10);
    //             rec.setHeight(10);
    //             rec.setFill(colors.get(i));
    //             Text text = new Text(titles.get(i));
    //             text.setFill(Color.WHITE);
    //             text.setFont(Font.font(10));
    //             paneForComponent.getChildren().addAll(rec, text);
  
    //             paneForComponent.layoutXProperty().bind(pane.widthProperty().subtract(gap.multiply(5)).divide(2).add(gap.multiply(i)));
    //             paneForComponent.layoutYProperty().bind(pane.heightProperty().subtract(gap.multiply(5)).divide(2).add(gap.multiply(5)));  
    //             pane.getChildren().add(paneForComponent);
    //         }
    //     }
    // }

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

    public Handle_TurningGame getHandle_TurningGame() {
        return handle_TurningGame;
    }
}
