package util;
import type.*;
import card.*;
import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Player {
    private int score = -1;
    private int PlayerNumber;
    private PlayerRole Role;
    private Color color;
    private State state;
    private ArrayList<ResourceCard> myCards = new ArrayList<ResourceCard>();
    private boolean onTradeRequest = false;
    private boolean onTax = false;
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
    public int getCapitals(){
        int count = 0;
        for(ResourceCard card: myCards){
            if(card.getType() == ProductionResources.Capital)
                count++;
        }
        return count;
    }
    public void deleteCapitals(int count){
        while(count>0){
            for(ResourceCard card: myCards){
                if(card.getType() == ProductionResources.Capital){
                    myCards.remove(card);
                    count--;
                    break;
                }
            } 
        }
    }
    public long getCapitalCount(){
        long count = myCards.stream().filter(r -> r instanceof Capital).count();
        return count;
    }
    public long getCloudCount(){
        long count = myCards.stream().filter(r -> r instanceof Cloud).count();
        return count;
    }
    public long getDataCount(){
        long count = myCards.stream().filter(r -> r instanceof Data).count();
        return count;
    }
    public long getPatentCount(){
        long count = myCards.stream().filter(r -> r instanceof Patent).count();
        return count;
    }
    public long getTalentCount(){
        long count = myCards.stream().filter(r -> r instanceof Talent).count();
        return count;
    }
    public void setOnTradeRequest(boolean onTradeRequest){
        this.onTradeRequest = onTradeRequest;
    }
    public boolean getOnTradeRequest(){
        return onTradeRequest;
    }
    public void setOnTax(boolean onTax){
        this.onTax = onTax;
    }
    public boolean getOnTax(){
        return onTax;
    }
}
