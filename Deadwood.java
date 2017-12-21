import java.util.*;
import java.io.Console;

public class Deadwood {

//    public static ArrayList<Player> players;
    public static HashMap<String,ArrayList> setNeighbors;
    public static ArrayList<Card> cards;
    public static int scenesLeft = 10;
    public static Player globalPlayer;
    public static int turnsComplete = 0;
    public static int numDays;
    public static BoardLayersListener board;
    
    public static void main(String[] args) {
        // parse the file
        ParseFile.parseCards();
        ParseFile.parseBoard();
        // set days
        numDays = 0;
        // set up game
        GameSetup game = new GameSetup();
        ArrayList<Player> players = game.GameSetup();
        numDays = game.getNumDays();
        // set the players 
        ParseFile.setPlayers(players);
        cards = ParseFile.cards;
        // set up the visuals
        board = new BoardLayersListener();
        board.setVisible(true);
        // set first global player
        globalPlayer = players.get(0);
        // start the first turn
        BoardLayersListener.displayGenericMessage("\nIt is now " + globalPlayer.getPlayer() + "'s turn.");
        board.playerInformation(globalPlayer,numDays);
    }

    // What a player want to do with their turn
    public static void startTurn(Player currentPlayer, String input) {
       boolean val = false;
       // user input for number of players
       do {
        String[] inputArray;
        inputArray = input.split(" ");
        input = inputArray[0];
        switch(input) {
         case "move":
             Move move = new Move();
             move.move(currentPlayer, inputArray);
           break;
         case "work":
           work(currentPlayer, inputArray);
           endTurn(currentPlayer);
           val = true;
           break;
         case "rank up":

             if( (inputArray[1] != "$") && (inputArray[1] == "cr") ) {
                 System.out.println("Not a valid input, try again.");
                 System.out.println("Your options are: who, where, move (room), work (part), upgrade $ level, upgrade cr level, rehearse, act, and end");
             } else {
                 try {

                   int level = Integer.parseInt(inputArray[2]);
                   System.out.println("do we hit here? 1");
                   playerUpgrade(currentPlayer,inputArray, level);
                   playerUpgrade(currentPlayer,inputArray, level);

                 } catch (NumberFormatException e) {
                     System.out.println("Invalid level. Try again.");
                     System.out.println("Your options are: who, where, move (room), work (part), upgrade $ level, upgrade cr level, rehearse, act, and end");
                 }
             }

           break;
         case "rehearse":
           playerRehearse(currentPlayer);
           val=true;
           endTurn(currentPlayer);
           break;
         case "act":
           Act act = new Act();
           act.playerAct(currentPlayer);
           if (currentPlayer.getTurn() == false){
               val = true;
           }
           endTurn(currentPlayer);
           break;
         case "end":
            endTurn(currentPlayer);
            val = true;
           break;
         default:
           System.out.println("Not a valid input, try again.");
           break;
        }
       } while (val == false);
     }
    
    // if a player wants to upgrade
    public static void playerUpgrade(Player currentPlayer, String[] inputArray, int level) {

        /* Current player is in casting office */
        if(currentPlayer.getPlayerPosition().equals("Casting Office")) {

                if(inputArray[1].equals("cr")) {
                    CastingOffice.upgradeRankWithFame(currentPlayer,level);
                } else if(inputArray[1].equals("$")) {
                    CastingOffice.upgradeRankWithMoney(currentPlayer,level);
                }


        /* Current player is in scene room */
    } else {
         BoardLayersListener.displayGenericMessage("You are not in the casting office");
    }

    }
    
    // If a player wants to end turn
    public static void endTurn(Player currentPlayer){
        ParseFile pf = new ParseFile();
        ArrayList<Player> players = pf.players;
        currentPlayer.setTurn(false);
        currentPlayer.setMoved(false);
        if (turnsComplete == (players.size() - 1)) {
            turnsComplete = 0;
        } else {
            turnsComplete++;
        }
        System.out.println(turnsComplete);
        globalPlayer = players.get(turnsComplete);
        BoardLayersListener.displayGenericMessage("\nIt is now " + globalPlayer.getPlayer() + "'s turn.");
        board.playerInformation(globalPlayer,numDays);
    }
    
    // if a player wants to rehearse
    public static void playerRehearse(Player currentPlayer) {
      String playerRole = currentPlayer.getRole();
      if (playerRole.equals("")) {
        BoardLayersListener.displayGenericMessage("You are not on a role");
      } else {
        ParseFile pf = new ParseFile();
        ArrayList<Card> cards = pf.cards;
        HashMap<String,Room> rooms = pf.rooms;
        Act act = new Act();
        Card card = act.findCard(cards, playerRole);
        BoardLayersListener.displayGenericMessage("You succeeded in rehearsing. You have added one to role value.\n");
        currentPlayer.setAddedDiceValue();
      }
    }

    // if a player wants to work or take a role
    public static boolean work(Player currentPlayer, String[] role) {
        Act act = new Act();
        return act.takeUpRole(currentPlayer, role);
    }
    
    // set the number of scenes left
    public void setScenesLeft(int scenesLeft) {
      this.scenesLeft = scenesLeft;
    }
}
