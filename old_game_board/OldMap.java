package old_game_board;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import board_graph.*;
import cards.Capital;
import cards.Cloud;
import cards.Null;
import cards.Patent;
import cards.ResourceCard;
import cards.Talent;
import game_board.DrawBoard;
import graph.Edge;
import graph.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import set_undo_and_redo.UndoAction;
import undo_and_redo.*;
import util.*;

public class OldMap {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private ArrayList<Sector> sectors;
    private ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/images/backgrounds/bg.png")));
    private DoubleProperty hGap = new SimpleDoubleProperty(50);
    private DoubleProperty vGap = new SimpleDoubleProperty(50);
    private DrawOldBoard board;
    private UndoAction undoButton; 
    private SaveStages stages;
    private int n;
    public OldMap(DrawOldBoard board, int n, ArrayList<Node> nodes, ArrayList<Edge> edges, ArrayList<Sector> sectors){
        this.n = n;
        this.nodes = nodes;
        this.edges = edges;
        this.sectors = sectors;
        this.board = board;
        stages = new SaveStages();
        //undoButton = new DrawUndoButton(this, stages, board.getCurrentPane());
        drawGraph();
    }
    public void drawGraph(){
        hGap.bind(widthProperty().divide(6.7 + (n-5)*0.8));
        vGap.bind(heightProperty().divide(6.7 + (n-5)*0.9));
        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        background.setSmooth(true);
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,CornerRadii.EMPTY,Insets.EMPTY)));
        this.getChildren().add(background);
        int indexOfSector = 0;
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                Sector sector = sectors.get(indexOfSector++);
                sector.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                sector.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                sector.getSectorShape().widthProperty().bind(hGap);
                sector.getSectorShape().heightProperty().bind(vGap);
                sector.prefWidthProperty().bind(hGap);
                sector.prefHeightProperty().bind(vGap);
                this.getChildren().add(sector);
            }
        }
        int indexOfEdges = 0;
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n; j++){
                Edge e = edges.get(indexOfEdges++);
                this.getChildren().add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j+1)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
            }
        }
        for(int j=0; j<n+1; j++){
            for(int i=0; i<n; i++){
                Edge e = edges.get(indexOfEdges++);
                this.getChildren().add(e);
                e.startXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.startYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                e.endXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                e.endYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i+1)));
            }   
        }
        int indexOfNodes = 0;
        for(int i=0; i<n+1; i++){
            for(int j=0; j<n+1; j++){
                Node node = nodes.get(indexOfNodes++);
                node.layoutXProperty().bind(widthProperty().subtract(hGap.multiply(n)).divide(2).add(hGap.multiply(j)));
                node.layoutYProperty().bind(heightProperty().subtract(vGap.multiply(n)).divide(2).add(vGap.multiply(i)));
                node.getNodeShape().radiusProperty().bind(getWidth() > getHeight() ? widthProperty().divide(70) : heightProperty().divide(70));
            }
        }
    }
}
