package util;
import type.PlayerRole;
import card.*;
import java.util.ArrayList;

public class Player {
    private int score;
    private int PlayerNumber;
    private PlayerRole Role;
    private GamePlan gamePlan;
    private ArrayList<ResourceCard> myCards;
    
    public Player(int PlayerNumber, PlayerRole Role){
        this.PlayerNumber = PlayerNumber;
        this.Role = Role;
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

}
