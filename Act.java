import java.util.*;

public class Act{
    int sceneRank = 0;
    int sceneNumber = 0;
    int playerRank = 0;
    int playerNumber = 0;
    int fame = 0;
    int money = 0;
    boolean actOnorOff = false;

    public void createAct(int sceneRank, int sceneNumber, int playerRank, int playerNumber, int fame, int money, boolean actOnorOff) {
        this.sceneRank = sceneRank;
        this.sceneNumber = sceneNumber;
        this.playerRank = playerRank;
        this.playerNumber = playerNumber;
        this.fame = fame;
        this.money = money;
        this.actOnorOff = actOnorOff;
    }

    public void playerAct(Player currentPlayer) {
        /*
            player is not in a role or casting office: case 1
                -> player is in  a scene room, then ask if they want to take a role
            or currently in a role: case 2
            or in the casting office: case 3
        */
        String playerRole = currentPlayer.getRole();
        if (playerRole.equals("")) {
          BoardLayersListener.displayGenericMessage("Player not on a role");
        } else {
          ParseFile pf = new ParseFile();
          ArrayList<Card> cards = pf.cards;
          HashMap<String,Room> rooms = pf.rooms;
          // find out if a player is working on a card
          // if yes - return the card
          // if no - return card with name fail
          Card card = findCard(cards, playerRole);
          if (!(card.getCardName().equals("fail"))) {
              int movieBudget = card.getBudget();
              Dice dice = new Dice();
              int diceVal = dice.actRollDice();
              BoardLayersListener.displayGenericMessage("You rolled " +  diceVal + ".\n");
              actOnCard(currentPlayer, rooms, cards, currentPlayer.getPlayerPosition(), diceVal, movieBudget);
          } else {
              int movieBudget = rooms.get(currentPlayer.getPlayerPosition()).getCard().getBudget();
              Dice dice = new Dice();
              int diceVal = dice.actRollDice();
              BoardLayersListener.displayGenericMessage("You rolled " +  diceVal + ".\n");
              actOffCard(currentPlayer, rooms, cards, currentPlayer.getPlayerPosition(), diceVal, movieBudget);
          }
        }
        /* Current player is in casting office */
        if(currentPlayer.getPlayerPosition().equals("CastingOffice")) {
            //ask first if player wants to upgrade
            CastingOffice castingOffice = new CastingOffice();
            boolean askUpgrade = castingOffice.askIfUpgrade();
            if(askUpgrade) {
                String fameOrDollar = castingOffice.howToUpgrade();
                if(fameOrDollar.equals("f")) {
                    castingOffice.upgradeRankWithFame(currentPlayer,currentPlayer.getRank()+1);
                } else if(fameOrDollar.equals("d")) {
                    castingOffice.upgradeRankWithMoney(currentPlayer,currentPlayer.getRank()+1);
                }
                //user wants to move => find a adjacent room
            }


        /* Current player is in scene room */
        }
    }
    
    // Find card within card arraylist
    public Card findCard(ArrayList<Card> cards, String cardName){
      for (int i = 0; i < cards.size(); i++) {
        Card currCard = cards.get(i);
        if (currCard.getCardName().equals(cardName)){
          return currCard;
        }
      }
      Card cardFailure = new Card();
      cardFailure.setCardName("fail");
      return cardFailure;
    }
    
    // acting on the card
    public void actOnCard(Player currentPlayer, HashMap<String,Room> rooms, ArrayList<Card> cards, String room, int dieValue, int budgetMovie) {
        if(actOnorOff){
            if(dieValue >= budgetMovie){
                BoardLayersListener.displayGenericMessage("You succeeded in acting off card! You will get 2 fame");
                int currFame = currentPlayer.getFame();
                currFame += 2;
                currentPlayer.setFame(currFame);
                Room currRoom = rooms.get(room);
                int take = currRoom.getNumofTakes();
                take--;
                currRoom.setNumofTakes(take);
                ArrayList<take> takes = currRoom.getTakes();
                take currTake = takes.get(takes.size()-1);
                BoardLayersListener.removeTake(currTake);
                takes.remove(currTake);
                currRoom.setTakesList(takes);
                if (take == 0){
                  currentPlayer.setRole("");
                  endScene(currRoom, cards, budgetMovie);
                }
                currentPlayer.setTurn(false);
            } else {
                BoardLayersListener.displayGenericMessage("You failed in acting on card! You earn nothing");
                currentPlayer.setTurn(false);
            }
        }
    }
    
    // acting off the card
    public void actOffCard(Player currentPlayer, HashMap<String,Room> rooms, ArrayList<Card> cards, String room, int dieValue, int budgetMovie){
        if(!actOnorOff){
            if(dieValue >= budgetMovie){
                BoardLayersListener.displayGenericMessage("You succeeded in acting off card!\nYou recieved $1 and 1 fame.");
                Room currRoom = rooms.get(room);
                int take = currRoom.getNumofTakes();
                take--;
                currRoom.setNumofTakes(take);
                if (take >= 0) {
                    ArrayList<take> takes = currRoom.getTakes();
                    take currTake = takes.get(takes.size()-1);
                    BoardLayersListener.removeTake(currTake);
                    takes.remove(currTake);
                    currRoom.setTakesList(takes);
                }
                if (take == 0){
                    currentPlayer.setRole("");
                    endScene(currRoom, cards, budgetMovie);
                }
                int currFame = currentPlayer.getFame();
                currFame += 2;
                currentPlayer.setFame(currFame);
                BoardLayersListener.playerInformation(currentPlayer,Deadwood.numDays);
                int currMoney = currentPlayer.getMoney();
                currMoney++;
                currentPlayer.setMoney(currMoney);
                BoardLayersListener.playerInformation(currentPlayer,Deadwood.numDays);
                currentPlayer.setTurn(false);
            } else {
                BoardLayersListener.displayGenericMessage("You failed in acting off card! You still earn 1 money");
                int currMoney = currentPlayer.getMoney();
                currMoney++;
                currentPlayer.setMoney(currMoney);
                BoardLayersListener.playerInformation(currentPlayer,Deadwood.numDays);
                currentPlayer.setTurn(false);
            }
        }
    }
    
    // have a player take a role or work
    public boolean takeUpRole(Player currentPlayer, String[] destination){
      String currentPosition = currentPlayer.getPlayerPosition();
      ParseFile pf = new ParseFile();
      ArrayList<Card> cards = pf.cards;
      HashMap<String,Room> rooms = pf.rooms;
      if (Objects.equals(currentPlayer.getRole(), "")) {
        // one word
        if (destination.length == 2) {
          // check room
          Room currRoom = rooms.get(currentPosition);
          Card card = currRoom.getCard();
          ArrayList<partExtra> parts = currRoom.getParts();
          ArrayList<part> cardParts = card.getCardParts();
          if (checkRole(currentPlayer, parts, cardParts, destination[1], currRoom)){
            return true;
          } else {
            return false;
          }
          // multiple words
        } else {
          String newDestination = "";
          for (int l = 1; l < destination.length; l++) {
            newDestination = newDestination + " " + destination[l];
          }
          newDestination = newDestination.trim();
          Room currRoom = rooms.get(currentPosition);
          Card card = currRoom.getCard();
          ArrayList<partExtra> parts = currRoom.getParts();
          ArrayList<part> cardParts = card.getCardParts();
          if (checkRole(currentPlayer, parts, cardParts, newDestination, currRoom)){
            return true;
          } else {
            return false;
          }
        }
      } else {
        BoardLayersListener.displayGenericMessage("Player already in a role.\n");
        return false;
      }
    }
    
    // check to see if the role they want to take is valid
    public boolean checkRole(Player currentPlayer, ArrayList<partExtra> parts, ArrayList<part> cardParts, String partName, Room room){
      boolean check = false;
      for (int i = 0; i < parts.size(); i++) {
        partExtra currPart = parts.get(i);
        String name = currPart.getPartName();
        boolean taken = currPart.getTaken();
        if (taken != true) {
              if (name.equals(partName)) {
                  if (currentPlayer.getRank() >= currPart.getLevel()) {
                    currentPlayer.setRole(partName);
                    currentPlayer.setRoleLevel(currPart.getLevel());
                    currentPlayer.setRoleValue("off");
                    currPart.setTaken(true);
                    room.setPlayersOnCard((room.getPlayersOnCard()-1));
                    BoardLayersListener.removePlayer(currentPlayer);
                    BoardLayersListener.movePlayer(currentPlayer, currPart.getArea(), room);
                    currentPlayer.setTurn(false);
                    return true;
                }
                BoardLayersListener.displayGenericMessage("You do not have a high enough rank for this role.");
              }
            } else {
              BoardLayersListener.displayGenericMessage("This role is already taken. \n");
              check = true;
            }
      }
      // make sure it hasn't already taken a part off the card
      if (check == false) {
        for (int j = 0; j < cardParts.size(); j++) {
          part currPart = cardParts.get(j);
          String currName = currPart.getName();
          boolean taken = currPart.getTaken();
          if (taken != true) {
            if (currName.equals(partName)) {
                 if (currentPlayer.getRank() >= currPart.getLevel()) {
                  currentPlayer.setRole(partName);
                  currentPlayer.setRoleLevel(currPart.getLevel());
                  currentPlayer.setRoleValue("on");
                  currPart.setTaken(true);
                  room.setPlayersOnCard((room.getPlayersOnCard()-1));
                  BoardLayersListener.removePlayer(currentPlayer);
                  BoardLayersListener.onCardMove(currentPlayer, currPart.getArea(), room.getCardArea());
                  currentPlayer.setTurn(false);
                  return true;
              }
              BoardLayersListener.displayGenericMessage("You do not have a high enough rank for this role.");
              return false;
            }
          } else {
             BoardLayersListener.displayGenericMessage("This role is already taken. \n");
          }
        }
      }
      if (check == false) {
         BoardLayersListener.displayGenericMessage("The role you want to act in is not on the card or board.\n");
     }
      return false;
    }
    
    // end the scene logic
    public void endScene(Room room, ArrayList<Card> cards, int budgetMovie){
      BoardLayersListener.displayGenericMessage("End of Scene!");
      BoardLayersListener.removeCard(room.getCard());
      ArrayList<Player> onTheCardPlayers = new ArrayList<Player>();
      Deadwood d = new Deadwood();
      int scenesLeft = d.scenesLeft;
      scenesLeft--;
      d.setScenesLeft(scenesLeft);
      Card card = room.getCard();
      String cardName = card.getCardName();
      ArrayList<part> parts = card.getCardParts();
      GameSetup GS = new GameSetup();
      ArrayList<Player> players = GS.players;
      for (int i=0; i< players.size() ;i++) {
        Player currPlayer = players.get(i);
        if (parts.contains(currPlayer.getRole())){
            BoardLayersListener.removePlayer(currPlayer);
            BoardLayersListener.movePlayer(currPlayer, room.getCardArea(), room);
            onTheCardPlayers.add(currPlayer);
        }
      }
      if (onTheCardPlayers.size() > 0){
        if (onTheCardPlayers.size() == 1) {
          Dice die = new Dice();
          ArrayList<Integer> sortedDice = die.rollDice(budgetMovie);
        } else {
          onTheCardPlayers = sortPlayers(onTheCardPlayers, parts);
          Dice die = new Dice();
          ArrayList<Integer> sortedDice = die.rollDice(budgetMovie);
          distributeEarnings(onTheCardPlayers, sortedDice);
        }
      } else {
        return;
      }
    }
    
    // distribute earnings for players on the card acting
    public static void distributeEarnings(ArrayList<Player> onTheCardPlayers, ArrayList<Integer> diceValue) {
      boolean playersPaid = false;
      while(playersPaid = false){
        int dicePosition = 0;
        for(int i = 0; i < onTheCardPlayers.size(); i++){
          if(diceValue.get(dicePosition) != diceValue.size()){
            int playerMoney = onTheCardPlayers.get(i).getMoney() + diceValue.get(dicePosition);
            onTheCardPlayers.get(i).setMoney(playerMoney);
            dicePosition ++;
          }else{
            dicePosition = 0;
            i--;
          }
        }
        playersPaid = true;
      }
    }
    
    // sort players for distributing money
    public ArrayList<Player> sortPlayers(ArrayList<Player> players, ArrayList<part> parts){
      for (int i = 0; i < players.size(); i++) {
        if (i + 1 < players.size()) {
          Player currPlayer = players.get(i);
          Player nextPlayer = players.get(i+1);
          if (currPlayer.getRoleLevel() < nextPlayer.getRoleLevel()) {
            players.set(i, nextPlayer);
            players.set(i+1, currPlayer);
          }
        }
      }
      return players;
    }
}
