import java.util.*;

public class Board {
  public int numRooms = 10;
  public ArrayList<Card> cards = new ArrayList<Card>();
  public ArrayList<Card> cardStack = new ArrayList<Card>();
  public HashMap<String,Room> rooms = new HashMap<String,Room>();
  
  //  creating the board
  public void setBoard(){
    ParseFile pf = new ParseFile();
    this.cards = pf.cards;
    this.cardStack = pf.cards;
    this.rooms = pf.rooms;
  }
  
  // placing cards on the board using a stack for cards
  public void placeCards(){
    int i = 0;
    for (String key: rooms.keySet()){
      Room currRoom = rooms.get(key);
      if (key != "trailer"){
        Card currCard = cardStack.get(i);
        currRoom.setCard(currCard);
        cardStack.remove(i);
        i++;
      }
    }

  }

}
