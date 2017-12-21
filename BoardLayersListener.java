/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/


import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.util.*;
import java.awt.Rectangle;


public class BoardLayersListener extends JFrame {

  // Private Attributes

  // JLabels
  static JLabel boardlabel;
  static JLabel cardlabel;
  static JLabel cardlabelJail;
  static JLabel playerlabel;
  static JLabel mLabel;
  static JLabel[] playerInfo = {new JLabel("Player 1"), new JLabel("Player 2"), new JLabel("Player 3"), new JLabel("Player 4"), new JLabel("Player 5"), new JLabel("Player 6"), new JLabel("Player 7"), new JLabel("Player 8")};


  //JButtons
  static JButton bAct;
  static JButton bRehearse;
  static JButton bMove;
  static JButton bTakeRole;
  static JButton bRankUp;
  static JButton bEndTurn;

  // JLayered Pane
  static JLayeredPane bPane;
  static JPanel panelStatus;
  static JPanel info;

  static Dimension boardSize = new Dimension(1170, 882);
  static Dimension paneSize = new Dimension(1300, 550);

  static ImageIcon icon;

       // JPanel
  static JLabel numDayLable = new JLabel();
  static JLabel playerLable1 = new JLabel();
  static JLabel playerLable2 = new JLabel();
  static JLabel playerLable3 = new JLabel();
  static JLabel playerLable4 = new JLabel();
  static JLabel playerLable5 = new JLabel();

  // Constructor
  public BoardLayersListener() {

       // Set the title of the JFrame
       super("Deadwood");
       // Set the exit option for the JFrame
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       // Create the JLayeredPane to hold the display, cards, dice and buttons
       bPane = getLayeredPane();

       // Create the deadwood board
        boardlabel = new JLabel();
        icon =  new ImageIcon("images/board.jpg");
        boardlabel.setIcon(icon);
        boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

        info = new JPanel();

       // Add the board to the lowest layer
       bPane.add(boardlabel, new Integer(0));

       // Set the size of the GUI
       setSize(icon.getIconWidth()+200,icon.getIconHeight());

     // Create the board for status of player
       panelStatus = new JPanel();
       panelStatus.setLayout(null);
       panelStatus.setBounds(icon.getIconWidth()+10,390,450,500);
       panelStatus.setBackground(Color.WHITE);

      addButtons(icon);
      addCards();
      addPlayers();
      addTakes();
  }
   
   // print the current players information to the board
   public static void playerInformation(Player currentPlayer, int numDays) {
        //JLabel numDayLable = new JLabel();
        numDayLable.setText("Number of days left: " + Integer.toString(numDays));
        numDayLable.setBounds(icon.getIconWidth()+10,400,300,20);
        bPane.add(numDayLable,new Integer(2));

        //JLabel playerLable1 = new JLabel();
        playerLable1.setText("Current player: " + currentPlayer.getPlayer());
        playerLable1.setBounds(icon.getIconWidth()+10,420,300,20);
        bPane.add(playerLable1,new Integer(2));

        //JLabel playerLable2 = new JLabel();
        playerLable2.setText("Player rank: " + Integer.toString(currentPlayer.getRank()));
        playerLable2.setBounds(icon.getIconWidth()+10,440,300,20);
        bPane.add(playerLable2,new Integer(2));

        //JLabel playerLable3 = new JLabel();
        playerLable3.setText("Player money: " + Integer.toString(currentPlayer.getMoney()));
        playerLable3.setBounds(icon.getIconWidth()+10,460,300,20);
        bPane.add(playerLable3,new Integer(2));

        //JLabel playerLable4 = new JLabel();
        playerLable4.setText("Player fame: " + Integer.toString(currentPlayer.getFame()));
        playerLable4.setBounds(icon.getIconWidth()+10,480,300,20);
        bPane.add(playerLable4,new Integer(2));

        //JLabel playerLable5 = new JLabel();
        playerLable5.setText("Current room: " + currentPlayer.getPlayerPosition());
        playerLable5.setBounds(icon.getIconWidth()+10,500,300,20);
        bPane.add(playerLable5,new Integer(2));
    }
   
  // intital box that asks for how many players
  public static int askNumPlayers() {
      int playerNumber = 0;
      //asks for number of players
      String[] options = new String[] {"2", "3", "4", "5", "6", "7", "8"};
      int option =  JOptionPane.showOptionDialog(null, "Choose a number of players", "Message",
      JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
      return (Integer.parseInt(options[option]));
  }
   
  // add the takes to the board
  public void addTakes(){
      ParseFile parse = new ParseFile();
      HashMap<String,Room> rooms = parse.rooms;
      for (String key: rooms.keySet()){
        Room currRoom = rooms.get(key);
        if ((key != "trailer") && (key != "Casting Office")){
            ArrayList<take> takes = currRoom.getTakes();
            Rectangle roomArea = currRoom.getCardArea();
            for (int i = 0; i < takes.size(); i++) {
                take t = takes.get(i);
                Rectangle takeArea = t.getArea();
                // Add a scene card to this room
                cardlabel = new JLabel();
                t.setTake(cardlabel);
                ImageIcon cardImage =  new ImageIcon("images/shot.png");
                cardlabel.setIcon(cardImage);
                // x+4 and y-4
                cardlabel.setBounds((int)takeArea.getX(),(int)takeArea.getY(), cardImage.getIconWidth(),cardImage.getIconHeight());
                cardlabel.setOpaque(true);
                // Add the card to the lower layer
                bPane.add(cardlabel, new Integer(2));
            }
        }
      }
  }
   
   // remove a take
  public static void removeTake(take currTake) {
      JLabel takelabel = currTake.getTake();
      bPane.remove(takelabel);
      bPane.revalidate();
      bPane.repaint();
  }

  // add cards to the board
  public void addCards(){
      ParseFile parse = new ParseFile();
      HashMap<String,Room> rooms = parse.rooms;
      for (String key: rooms.keySet()){
        Room currRoom = rooms.get(key);
        if ((key != "trailer") && (key != "Casting Office")){
            Card currCard = currRoom.getCard();
            Rectangle roomArea = currRoom.getCardArea();
            placeCards(currCard, currRoom.getCardArea());
            placeCardBacks(currCard, currRoom.getCardArea());
        }
      }
  }
  
  public void placeCards(Card card, Rectangle cardArea){
      // Add a scene card to this room
      cardlabel = new JLabel();
      ImageIcon cardImage =  new ImageIcon("images/" + card.getCardImg());
      cardlabel.setIcon(cardImage);
      // x+4 and y-4
      cardlabel.setBounds((int)cardArea.getX(),(int)cardArea.getY(),cardImage.getIconWidth(),cardImage.getIconHeight());
      cardlabel.setOpaque(true);
      card.setCardLabel(cardlabel);
      // Add the card to the lower layer
      bPane.add(cardlabel, new Integer(2));
  }
   
  // add the back of the cards to the board
  public void placeCardBacks(Card card, Rectangle cardArea){
      // Add a scene card to this room
      cardlabel = new JLabel();
      ImageIcon cardBack =  new ImageIcon("images/backOfCard.png");
      cardlabel.setIcon(cardBack);
      // x+4 and y-4
      cardlabel.setBounds((int)cardArea.getX()+4,(int)cardArea.getY()-4,cardBack.getIconWidth(),cardBack.getIconHeight());
      cardlabel.setOpaque(true);
      card.setBackCardLabel(cardlabel);
      // Add the card to the lower layer
      bPane.add(cardlabel, new Integer(3));

  }
   
   // add the buttons to the board
  public void addButtons(ImageIcon icon) {
      // Create the Menu for action buttons
      mLabel = new JLabel("MENU");
      mLabel.setBounds(icon.getIconWidth()+40,0,100,20);
      bPane.add(mLabel,new Integer(2));

      // Create Action buttons
      bAct = new JButton("ACT");
      bAct.setBackground(Color.white);
      bAct.setBounds(icon.getIconWidth()+10, 40,120, 40);
      bAct.addMouseListener(new boardMouseListener());

      bRehearse = new JButton("REHEARSE");
      bRehearse.setBackground(Color.white);
      bRehearse.setBounds(icon.getIconWidth()+10,100,120, 40);
      bRehearse.addMouseListener(new boardMouseListener());

      bMove = new JButton("MOVE");
      bMove.setBackground(Color.white);
      bMove.setBounds(icon.getIconWidth()+10,160,120, 40);
      bMove.addMouseListener(new boardMouseListener());

      bTakeRole = new JButton("TAKE ROLE");
      bTakeRole.setBackground(Color.white);
      bTakeRole.setBounds(icon.getIconWidth()+10,220,120, 40);
      bTakeRole.addMouseListener(new boardMouseListener());

      bRankUp = new JButton("RANK UP");
      bRankUp.setBackground(Color.white);
      bRankUp.setBounds(icon.getIconWidth()+10,280,120, 40);
      bRankUp.addMouseListener(new boardMouseListener());

      bEndTurn = new JButton("END TURN");
      bEndTurn.setBackground(Color.white);
      bEndTurn.setBounds(icon.getIconWidth()+10,340,120, 40);
      bEndTurn.addMouseListener(new boardMouseListener());


      // Place the action buttons in the top layer
      bPane.add(bAct, new Integer(2));
      bPane.add(bRehearse, new Integer(2));
      bPane.add(bMove, new Integer(2));
      bPane.add(bTakeRole, new Integer(2));
      bPane.add(bRankUp, new Integer(2));
      bPane.add(bEndTurn, new Integer(2));
  }
   
  // add the players to trailer intially
  public void addPlayers() {
      ParseFile pf = new ParseFile();
      ArrayList<Player> players = pf.players;
      int widthOffset = 0;
      int heightOffset = 0;
      for (int i = 0; i < players.size(); i++) {
        Player currPlayer = players.get(i);
        String img = getPlayerImage(currPlayer.getPlayer(), currPlayer.getRank());
        playerlabel = new JLabel();
        ImageIcon pIcon = new ImageIcon(img);
        playerlabel.setIcon(pIcon);
        if (i == 5) {
            heightOffset += pIcon.getIconHeight();
            widthOffset = 0;
        }
        playerlabel.setBounds(991+widthOffset,248+heightOffset,pIcon.getIconWidth(),pIcon.getIconHeight());
        currPlayer.setPlayerLabel(playerlabel);
        bPane.add(playerlabel,new Integer(3));
        widthOffset += pIcon.getIconWidth();
      }
  }
   
  // remove a player from its current position
  public static void removePlayer(Player player) {
      JLabel playerlabel = player.getPlayerLabel();
      bPane.remove(playerlabel);
      bPane.revalidate();
      bPane.repaint();
  }
   
  // remove a card from its current position
  public static void removeCard(Card card) {
      JLabel cardlabel = card.getCardLabel();
      bPane.remove(cardlabel);
      bPane.revalidate();
      bPane.repaint();
  }
   
  // add a player to a on card part
  public static void onCardMove(Player player, Rectangle cardArea, Rectangle roomArea) {
      JLabel playerlabel = player.getPlayerLabel();
      Icon pIcon = playerlabel.getIcon();
      playerlabel.setBounds((int)cardArea.getX()+(int)roomArea.getX(),(int)cardArea.getY()+(int)roomArea.getY(),
      pIcon.getIconWidth(),pIcon.getIconHeight());
      bPane.add(playerlabel,new Integer(4));
  }
   
  // move a player
  public static void movePlayer(Player player, Rectangle cardArea, Room room) {
      int players = room.getPlayersOnCard();
      JLabel playerlabel = player.getPlayerLabel();
      Icon pIcon = playerlabel.getIcon();
      playerlabel.setBounds((int)cardArea.getX()+(players * pIcon.getIconWidth()),(int)cardArea.getY(),pIcon.getIconWidth(),pIcon.getIconHeight());
      bPane.add(playerlabel,new Integer(4));
  }
  
  // get the image for the player
  public static String getPlayerImage(String playerName, int playerRank){
      char color = Character.toLowerCase(playerName.charAt(0));
      String img = "images/" + color + playerRank + ".png";
      return img;
  }
  
  // flip a card
  public static void flipCard(Room room) {
      Card card = room.getCard();
      JLabel cardlabel = card.getBackCardLabel();
      bPane.remove(cardlabel);
      bPane.revalidate();
      bPane.repaint();
  }
   
  // build lower panel
  public static void buildLowerPanel(String option){
   int numPlayers = Integer.parseInt(option);
   info = new JPanel();

   info.setBounds(boardSize.width + 10, 400, 120, 40);
   // info. add days remaining
   for (int i = 0; i <= numPlayers; i++){
       info.add(playerInfo[i]);
   }
   bPane.add(info, JLayeredPane.DEFAULT_LAYER);

   }
   
   // if move button is called
   public void movetoAdjacentScene(Player currentPlayer) {
        Map<String,Room> map = ParseFile.rooms;
        ArrayList<String> obj = new ArrayList<String>();

        for(Map.Entry<String, Room> entry : map.entrySet()) {
            String key = entry.getKey();
            if( currentPlayer.getPlayerPosition().equals(key) ) {
                Room room = entry.getValue();

                for(int i = 0; i < room.neighbors.size(); i++) {
                    obj.add(room.neighbors.get(i));
                }
            }
        }

        String[] sceneNeighbors = new String[obj.size()];
        for(int i = 0; i < obj.size(); i++) {
            sceneNeighbors[i] = obj.get(i);
        }

        int option =  JOptionPane.showOptionDialog(null, "Choose which scene to move to", "Message",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
        null, sceneNeighbors, sceneNeighbors[0]);

        if (option != JOptionPane.CLOSED_OPTION) {
            // System.out.println(sceneNeighbors[option]);

            int lengthSceneArray = 0;
            for(int i = 0; i < sceneNeighbors[option].length(); i++){
                //this doesn't work
                if(sceneNeighbors[option].contains(" ")) {
                    //two words
                    lengthSceneArray = 3;

                } else {
                    //one words
                    lengthSceneArray = 2;
                }
            }
            String[] result = sceneNeighbors[option].split("\\s+");
            String[] destination = new String[lengthSceneArray];
            if(lengthSceneArray == 2) {
                destination[0] = "move";
                destination[1] = result[0];
            } else if(lengthSceneArray ==3 ){
                destination[0] = "move";
                destination[1] = result[0];
                destination[2] = result[1];
            }

            for(int i = 0; i < destination.length; i++){
                System.out.println(destination[i]);
            }

            Move move = new Move();
            move.move(currentPlayer, destination);
            playerInformation(currentPlayer,Deadwood.numDays);
        } else {
            System.out.println("No option selected");
        }
    }


  // This class implements Mouse Events
    class boardMouseListener implements MouseListener {

        // Code for the different button clicks
        public void mouseClicked(MouseEvent e) {

            if (e.getSource()== bAct){
                Deadwood.startTurn(Deadwood.globalPlayer, "act");
            }
            else if (e.getSource()== bRehearse){
                Deadwood.startTurn(Deadwood.globalPlayer, "rehearse");
            }
            else if (e.getSource()== bMove){
               movetoAdjacentScene(Deadwood.globalPlayer);
            }
            else if (e.getSource()== bTakeRole){
                takeRoleButton(Deadwood.globalPlayer);
            }
            else if (e.getSource()== bRankUp){
                rankButton(Deadwood.globalPlayer);
            }
            else if (e.getSource()== bEndTurn){
                endTurnButton(Deadwood.globalPlayer);
            }
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }

    }
   
   // if the end turn button is pressed
    public void endTurnButton(Player currentPlayer){    
        Deadwood.startTurn(currentPlayer, "end");
    }


    // user requests to increase rank by pressing rank button
    public void rankButton(Player currentPlayer) {
        // check how they would like to increase rank
        String[] rankIncrease;
        String rankMethod = askRankOrMoney();
        System.out.println("rank method" + rankMethod);
        int level = askRanklevel();
        if(rankMethod.equals("Money")){
            rankIncrease = "upgrade $".split(" ");
        }else{
            rankIncrease = "upgrade cr".split("");
        }
        Deadwood.playerUpgrade(currentPlayer, rankIncrease, level);


    }
    // ask what level you would like to upgrade to
    private int askRanklevel() {

        int playerNumber = 0;
        //asks for desired level
        String[] options = new String[] {"2", "3", "4", "5", "6"};
        int option =  JOptionPane.showOptionDialog(null, "Choose level to upgrade too", "Message",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, options[0]);
        return (Integer.parseInt(options[option]));
    }

    // ask how the user wants to upgrade, return response to casting office
    public String askRankOrMoney(){
        String[] options = new String[] {"Money", "Credits"};
        int option =  JOptionPane.showOptionDialog(null, "Select a method for rank increase", "Message",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        return (options[option]);
    }

    // allow user to take a role by typing their selected role in pop up
    public void takeRoleButton(Player player){
        String position = player.getPlayerPosition();
        HashMap<String,Room> rooms = ParseFile.rooms;
        Room room = rooms.get(position);
        ArrayList<partExtra> extraParts = room.getParts();
        Card card = room.getCard();
        ArrayList<part> parts = card.getCardParts();
        String[] extraPartsArray = new String[(extraParts.size()+parts.size())];
        for (int i = 0; i < extraParts.size(); i++) {
            extraPartsArray[i] = extraParts.get(i).getPartName();
            }
        for (int i = (extraParts.size()); i < (parts.size()+extraParts.size()); i++) {
            extraPartsArray[i] = parts.get(i-extraParts.size()).getName();
            }
        Object selected = JOptionPane.showInputDialog(null, "What role would you like to take:", "Selection",
                JOptionPane.DEFAULT_OPTION, null, extraPartsArray, extraPartsArray[0]);
        if ( selected != null ){//null if the user cancels.
            String selectedString = selected.toString();
            Deadwood.startTurn(Deadwood.globalPlayer, "work " + selectedString);
            }
    }

    // ask if the user wants to act on or off the card, return response to main program
    public String askActOnOrOff(){
        String response = "";
        String[] options = new String[] {"On", "Off"};
        int option =  JOptionPane.showOptionDialog(null, "Would you like to work on or off the card?", "Message",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        return response;
    }

    // display information to be shown to the user as a pop up menu
    public static void displayGenericMessage(String message){
        JOptionPane.showMessageDialog(null, message);



    }
}
