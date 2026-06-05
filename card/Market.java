package card;
import java.util.ArrayList;

public class Market {
    private ArrayList<ResourceCard> cards;
    public Market(ArrayList<ResourceCard> cards){
        this.cards = cards;
    }
    public void setPrices(ResourceCard card){
        card.price = (card.price > 0 ? card.price + 1 : 0);
        card.unused_turns = 0;
        for(ResourceCard resourceCard: cards){
            if(resourceCard.unused_turns == 3){
                resourceCard.price = (resourceCard.price > 0 ? resourceCard.price - 1 : 0);
                resourceCard.unused_turns = 0;
            }
        }
    }
}
