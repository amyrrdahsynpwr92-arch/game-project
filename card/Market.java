package card;
import java.util.List;

import cards.Capital;
import cards.Cloud;
import cards.Data;
import cards.Patent;
import cards.ResourceCard;
import cards.Talent;

import java.util.Arrays;
import java.util.ArrayList;
import type.*;


public class Market {
    private List<ResourceCard> cards = Arrays.asList(new Capital(), new Cloud(), new Data(), new Patent(), new Talent());
    
    public void IncreasePrice(ProductionResources resourceType){
        switch(resourceType){
            case Capital:
                (cards.get(0).price)++;
                cards.get(0).unused_turns = 0;
                break;
            case Cloud:
                (cards.get(1).price)++;
                cards.get(1).unused_turns = 0;
                break;
            case Data:
                (cards.get(2).price)++;
                cards.get(2).unused_turns = 0;
                break;
            case Patent:
                (cards.get(3).price)++;
                cards.get(3).unused_turns = 0;
                break;
            case Talent:
                (cards.get(4).price)++;
                cards.get(4).unused_turns = 0;
                break;
        }
    }
    public void setPrices(){
        for(ResourceCard resourceCard: cards){
            resourceCard.unused_turns++;
            if((resourceCard.unused_turns % 3) == 0){
                resourceCard.price = (resourceCard.price > 0 ? resourceCard.price - 1 : 0);
            }
        }
    }
    public List<ResourceCard> getCards(){
        return cards;
    }
}
