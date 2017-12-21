import java.util.ArrayList;
import java.util.HashMap;

public class EndGame {

  int numPlayers = 0;
  int numDays = 0;
  ArrayList<Player> players = new ArrayList<Player>();
  
  // check how many days left
  private boolean checkNumberDays() {
      if(this.numPlayers == 2 || this.numPlayers == 3){
          if(numDays == 3){
              return true;
          }
      } else if(this.numPlayers >= 4){
          if(numDays == 4){
              return true;
          }
      }
      return false;
  }
/* Determine the winner, but function doesn't work
  private static void determineWinner(HashMap<Player, Integer> playersPoint) {
      Set set = playersPoint.entrySet();
      Iterator iterator = set.iterator;
      int temp = 0;
      while(iterator.hasNext()) {
          Map.Entry mentry = (Map.Entry)iterator.next();
          if(mentry.getValue() >= temp) {
              temp = mentry.getValue();
          }
      }
      return;
  }
  */
  
  // calculate the players points
  private HashMap<Player, Integer> calculatePlayerPoints() {
      HashMap<Player, Integer> playersPoint = new HashMap<Player, Integer>();
      //hash table;
      for(int i = 0; i < this.players.size(); i++){
          //this.players.get(i).
          int playerPoint = this.players.get(i).money + this.players.get(i).fame + (int)(this.players.get(i).rank*6);
          playersPoint.put(this.players.get(i),playerPoint);
      }
      return playersPoint;
  }

}
