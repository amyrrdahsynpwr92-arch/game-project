package util;
import type.PlayerRole;

public class Player {
    private int score;
    private int PlayerNumber;
    private PlayerRole Role;
    private GamePlan gamePlan;
    
    public Player(int PlayerNumber, PlayerRole Role){
        this.PlayerNumber = PlayerNumber;
        this.Role = Role;
    }
}
