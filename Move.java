import java.util.*;

/*Move is an object that is responsible for player to move to his desired scene*/
public class Move {

  /*The boolean function,move takes currentplayer and his desired place and lets him to move to the destination from his current location*/
  public static boolean move(Player currentPlayer, String[] destination) {
    // check to see if player already moved
    if (currentPlayer.getMoved() == false) {
      String currentPosition = currentPlayer.getPlayerPosition();
      ParseFile parse = new ParseFile();
      HashMap<String,Room> rooms = parse.rooms;
      Room playerRoom = rooms.get(currentPosition);
      ArrayList<String> neighbors = playerRoom.getNeighbors();
      if (destination.length == 2) {
        String dest = destination[1].trim();
        if (neighbors.contains(dest)){
          currentPlayer.setPlayerPosition(dest);
          Room newRoom = rooms.get(dest);
          if (!(dest.equals("trailer"))){
              Card card = newRoom.getCard();
              card.setFlipped(true);
          }
          BoardLayersListener.removePlayer(currentPlayer);
          BoardLayersListener.movePlayer(currentPlayer, newRoom.getCardArea(), newRoom);
          if (!(destination[1].equals("Casting Office")) && !(destination[1].equals("trailer"))) {
              BoardLayersListener.flipCard(newRoom);
          }
          BoardLayersListener.displayGenericMessage("\nPlayer " + currentPlayer.getPlayer() + " is now in " + destination[1] + ". \n");
          newRoom.setPlayersOnCard((newRoom.getPlayersOnCard()+1));
          playerRoom.setPlayersOnCard((playerRoom.getPlayersOnCard()-1));
          currentPlayer.setMoved(true);
          return true;
        } else {
          BoardLayersListener.displayGenericMessage("The destination you wish to move is not a neighbor");
          System.out.println("\nYour neighbors are: ");
          for (int i = 0; i < neighbors.size(); i++) {
              System.out.println(neighbors.get(i));
          }
        }
      } else {
        String newDestination = "";
        for (int l = 1; l < destination.length; l++) {
          newDestination = newDestination + " " + destination[l];
        }
        newDestination = newDestination.trim();
        if (neighbors.contains(newDestination)){
          currentPlayer.setPlayerPosition(newDestination);
          Room newRoom = rooms.get(newDestination);
          Card card = newRoom.getCard();
          card.setFlipped(true);
          BoardLayersListener.removePlayer(currentPlayer);
          BoardLayersListener.movePlayer(currentPlayer, newRoom.getCardArea(), newRoom);
          if (!(newDestination.equals("Casting Office")) && !(newDestination.equals("trailer"))) {
              BoardLayersListener.flipCard(newRoom);
          }
          BoardLayersListener.displayGenericMessage("\nPlayer " + currentPlayer.getPlayer() + " is now in " + newDestination + ". \n");
          newRoom.setPlayersOnCard((newRoom.getPlayersOnCard()+1));
          playerRoom.setPlayersOnCard((playerRoom.getPlayersOnCard()-1));
          currentPlayer.setMoved(true);
          return true;
        } else {
          BoardLayersListener.displayGenericMessage("The destination you wish to move is not a neighbor");
          System.out.println("\nYour neighbors are: ");
          for (int i = 0; i < neighbors.size(); i++) {
              System.out.println(neighbors.get(i));
          }
        }
      }
    } else {
      System.out.printf("player %s has already moved.\n", currentPlayer.getPlayer());
    }
    return false;
  }

  /*The function checks if the current player has already moved or not.*/
   public static boolean askIfMoving() {
        boolean getOutOfLoop = false;
        String answer = "";
        System.out.println("If you want to move, type in y. Otherwise, type in n\n");
        while(!getOutOfLoop){
            Scanner input = new Scanner(System.in);
            answer = input.next();
            if(!answer.toLowerCase().equals("y") || !answer.toLowerCase().equals("n")){
                System.out.println("Type in y or n. I ain't got no time to mess with you");
            } else {
                getOutOfLoop = true;
            }
        }

        if(answer.toLowerCase().equals("y")){
            return true;
        } else if(answer.toLowerCase().equals("n")){
            return false;
        }
        return false;

   }
  /*The function adjacentSets displays all adjacent sets from current set and takes which scene the user wishes to move to.*/
    public static String adjacentSets(HashMap<String,ArrayList> setNeighbors, String currentSet) {
        boolean getOutOfLoop = false;
        String answer = "";
        String destination = "";
        Set<String> keys = setNeighbors.keySet();
        while(!getOutOfLoop) {
            System.out.println("Which set would you move to? Please type in a single digit such as 1\n");
            for(String key: keys) {
                if(currentSet.equals(key)) {
                    //return values
                    for(int i = 0; i < setNeighbors.get(key).size(); i++) {
                        System.out.println(i+1 + ") " + setNeighbors.get(key).get(i));
                    }
                    Scanner input = new Scanner(System.in);
                    answer = input.next();
                    if(!answer.equals("1") || !answer.equals("2") || !answer.equals("3") || !answer.equals("4")){
                        System.out.println("You may not put anything but a single digit");
                    } else {

                        destination = setNeighbors.get(key).get(Integer.parseInt(answer)-1).toString();
                        getOutOfLoop = true;
                    }
                }
            }
        }
        return destination;
    }

}
