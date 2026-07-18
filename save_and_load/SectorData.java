package save_and_load;
import util.*;
import java.io.Serializable;
import java.util.ArrayList;
import cards.*;

public class SectorData implements Serializable {
    ResourceCard resource;
    int number;
    ArrayList<Player> MvpPlayers;
    ArrayList<Player> UnicornPlayers;
    boolean hasAuditor;

    public SectorData(Sector sector){
        this.resource = sector.getResource();
        this.number = sector.getNumber();
        this.MvpPlayers = sector.getMvpPlayers();
        this.UnicornPlayers = sector.getUnicornPlayers();
        this.hasAuditor = sector.HasAuditor();
    }
    public ResourceCard getResource(){
        return resource;
    }
    public ArrayList<Player> getMvpPlayers() {
        return MvpPlayers;
    }
    public ArrayList<Player> getUnicornPlayers() {
        return UnicornPlayers;
    }
    public int getNumber() {
        return number;
    }
    public boolean HasAuditor(){
        return hasAuditor;
    }
}
