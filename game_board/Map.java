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
import set_undo_and_redo.drawUndoButton;
import set_undo_and_redo.saveStages;
import javafx.scene.text.Font;
import util.Handle_PreGame;
import util.Handle_TurningGame;
import util.Player;
import util.Sector;
import graph.*; 
import card.*; 
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
    private drawBoard board;
    private drawUndoButton undoButton; 
    private saveStages stages;
    public Map(int numberOfPlayers, ArrayList<Player> players, drawBoard board){
        this.numberOfPlayers = numberOfPlayers;
        this.handle_TurningGame = new Handle_TurningGame(players);
        handle_preGame = new Handle_PreGame(players, handle_TurningGame);
        nodes = new Node[6][6];
        this.board = board;
        stages = new saveStages();
        undoButton = new drawUndoButton(this, stages, board.getCurrentPane());
        drawGraph();
    }
    public void drawGraph(){
        hGap.bind(widthProperty().divide(6.7));
        vGap.bind(heightProperty().divide(6.7));
        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        background.setSmooth(true);
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,CornerRadii.EMPTY,Insets.EMPTY)));
        this.getChildren().add(background);
        List<ResourceCard> cards = Arrays.asList(new Capital(), new Cloud(), new Data(), new Null(), new Patent(), new Talent());
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                Sector sector = new Sector(cards.get(new Random().nextInt(6)), j * 50 + 50, i * 50 + 50, j, i);
                sector.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                sector.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                sector.getSectorShape().widthProperty().bind(hGap);
                sector.getSectorShape().heightProperty().bind(vGap);
                sector.prefWidthProperty().bind(hGap);
                sector.prefHeightProperty().bind(vGap);
                sectors.add(sector);
                this.getChildren().add(sector);
            }
        }
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                nodes[i][j] = new Node(j, i, handle_preGame, handle_TurningGame, sectors, board, stages, undoButton);
                nodes[i][j].layoutXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                nodes[i][j].layoutYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                nodes[i][j].getNodeShape().radiusProperty().bind(getWidth() > getHeight() ? widthProperty().divide(70) : heightProperty().divide(70));
            }
        }
        ArrayList<Edge> edges = new ArrayList<>();
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                Edge e = new Edge(nodes[i][j], nodes[i][j+1], handle_preGame, handle_TurningGame, i*5+j, board, stages, undoButton);
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j+1)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
            }
        }
        for(int j=0; j<6; j++){
            for(int i=0; i<5; i++){
                Edge e = new Edge(nodes[i][j], nodes[i+1][j], handle_preGame, handle_TurningGame, 30 + j*5+i, board, stages, undoButton);
                this.getChildren().add(e);
                edges.add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(5)).divide(2).add(hGap.multiply(j)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(5)).divide(2).add(vGap.multiply(i+1)));
            }   
        }    
        for(Edge e: edges){
            e.setEdges(edges);
        }
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                Node n = nodes[i][j];
                n.setNodes(nodes);
                this.getChildren().add(n);
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
}
