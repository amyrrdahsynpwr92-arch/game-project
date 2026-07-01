package card;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import type.*;


public class Market {
    private List<ResourceCard> cards = Arrays.asList(new Capital(), new Cloud(), new Data(), new Patent(), new Talent());
    
    public void IncreasePrice(ProductionResources card){
        switch(card){
            case Capital:
                (cards.get(0).price)++;
                break;
            case Cloud:
                (cards.get(1).price)++;
                break;
            case Data:
                (cards.get(2).price)++;
                break;
            case Patent:
                (cards.get(3).price)++;
                break;
            case Talent:
                (cards.get(4).price)++;
                break;
        }
    }
    public void setPrices(){
        for(ResourceCard resourceCard: cards){
            if((resourceCard.unused_turns % 3) == 0){
                resourceCard.price = (resourceCard.price > 0 ? resourceCard.price - 1 : 0);
            }
        }
    }
    public List<ResourceCard> getCards(){
        return cards;
    }
}
