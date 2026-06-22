package util;
import type.PlayerRole;
import card.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Player {
    private int score = -1;
    private int PlayerNumber;
    private PlayerRole Role;
    private Color color;
    private ArrayList<ResourceCard> myCards = new ArrayList<ResourceCard>();
    
    public Player(int PlayerNumber){
        this.PlayerNumber = PlayerNumber;
        setColor();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
    
    public void setMyCards(ArrayList<ResourceCard> myCards) {
        this.myCards = myCards;
    }

    public ArrayList<ResourceCard> getMyCards() {
        return myCards;
    }
    public Color getColor() {
        return color;
    }

    public void setColor() {
        switch (PlayerNumber) {
            case 1:
                color = Color.RED;
                break;
            case 2:
                color = Color.BLUE;
                break;
            case 3:
                color = Color.PURPLE;
                break;
            case 4:
                color = Color.GREEN;
                break;
        }
    }

    public void setRole(PlayerRole role) {
        Role = role;
    }
    public PlayerRole getRole() {
        return Role;
    }
    public int getPlayerNumber(){
        return PlayerNumber;
    }
    
}
