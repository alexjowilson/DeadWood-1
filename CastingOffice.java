import java.util.*;
import java.awt.Rectangle;

public class CastingOffice {

    int money = 0;
    int fame = 0;
    //int playerNumber = 0;
    String whichPlayer = "";
    int rank = 0;
    boolean rehearseMarker = false;
    Rectangle officeArea;
    ArrayList<String> neighbors = new ArrayList<String>();
    Player player;
    public static BoardLayersListener board;

    public boolean checkPlayerRank() {
        if(this.rank >=1 && this.rank <=6){
            return true;
        } else{
            return false;
        }
    }

    public String getWhichPlayer(){
        return this.whichPlayer;
    }

    public int getRank(){
        return this.rank;
    }

    public void setArea(Rectangle officeArea){
      this.officeArea = officeArea;
    }

    public void setNeighbors(ArrayList<String> neighbors){
      this.neighbors = neighbors;
    }

    public ArrayList<String> getNeighbors(){
      return this.neighbors;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    public int getMoney(){
        return this.money;
    }

    public int getFame(){
        return this.fame;
    }
    
    // upgrade rank with fame
    public static boolean upgradeRankWithFame(Player currentPlayer, int level) {
        boolean returnValue = false;
        switch(level) {
            case 2:
                if(currentPlayer.getFame() >=5) {
                    currentPlayer.setFame( currentPlayer.getFame() - 5 );
                    currentPlayer.setRank(2);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;

                } else {
                    System.out.println("To rank up to 2, you need at least 5 fames");
                    returnValue = false;
                }
                break;
            case 3:
                if(currentPlayer.getFame() >=10){
                    currentPlayer.setFame( currentPlayer.getFame() - 10 );
                    currentPlayer.setRank(3);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                } else{
                    System.out.println("To rank up to 3, you need at least 10 fames");
                    returnValue = false;
                }
                break;
            case 4:
                if(currentPlayer.getFame() >=15){
                    currentPlayer.setFame( currentPlayer.getFame() - 15 );
                    currentPlayer.setRank(4);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                } else {
                    System.out.println("To rank up to 4, you need at least 15 fames");
                    returnValue = false;
                }
                break;
            case 5:
                if(currentPlayer.getFame() >=20){
                    currentPlayer.setFame( currentPlayer.getFame() - 20 );
                    currentPlayer.setRank(5);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                } else {
                    System.out.println("To rank up to 5, you need at least 20 fames");
                    returnValue = false;
                }
                break;
            case 6:
                if(currentPlayer.getFame() >=25){
                    currentPlayer.setFame( currentPlayer.getFame() - 25 );
                    currentPlayer.setRank(6);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                } else {
                    System.out.println("To rank up to 6, you need at least 25 fames");
                    returnValue = false;
                }
                break;
        }
        return returnValue;
    }
    
    // upgrade rank with money
    public static boolean upgradeRankWithMoney(Player currentPlayer, int level) {
        boolean returnValue = false;
        switch(level){
            case 2:
                if(currentPlayer.getMoney() >= 4){
                    currentPlayer.setMoney( currentPlayer.getMoney() - 4 );
                    currentPlayer.setRank(2);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                }else{
                    System.out.println("To rank up to 2, you need at least 4 dollars");
                    returnValue = false;
                }
                break;
            case 3:
                if(currentPlayer.getMoney() >= 10){
                    currentPlayer.setMoney( currentPlayer.getMoney() - 10 );
                    currentPlayer.setRank(3);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                }else{
                    System.out.println("To rank up to 3, you need at least 10 dollars");
                    returnValue = false;
                }
                break;
            case 4:
                if(currentPlayer.getMoney() >= 18){
                    currentPlayer.setMoney( currentPlayer.getMoney() - 18 );
                    currentPlayer.setRank(4);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                }else{
                    System.out.println("To rank up to 4, you need at least 18 dollars");
                    returnValue = false;
                }
                break;
            case 5:
                if(currentPlayer.getMoney() >= 28) {
                    currentPlayer.setMoney( currentPlayer.getMoney() - 28 );
                    currentPlayer.setRank(5);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                }else{
                    System.out.println("To rank up to 5, you need at least 28 dollars");
                    returnValue = false;
                }
                break;
            case 6:
                if(currentPlayer.getMoney() >= 40) {
                    currentPlayer.setMoney( currentPlayer.getMoney() - 40 );
                    currentPlayer.setRank(6);
                    board.playerInformation(currentPlayer,Deadwood.numDays);
                    returnValue = true;
                }else{
                    System.out.println("To rank up to 6, you need at least 40 dollars");
                    returnValue = false;
                }
                break;
        }
        return returnValue;
    }
    
    // take a player money
    public void takeMoney(int subtractMoney) {
        this.money = this.money - subtractMoney;
    }
    
    // take a players fame
    public void takeFame(int subtractFame) {
        this.fame = this.fame - subtractFame;
    }
    
    // upgrade by Reharse Marker
    public void upgradeByRehearseMarker() {
        if(this.rehearseMarker){
            this.rank = this.rank+1;
        }
    }
    
    // ask player how they want to upgrade
    public boolean askIfUpgrade() {
        boolean getOutOfLoop = false;
        String answer = "";
        while(!getOutOfLoop){
            Scanner input = new Scanner(System.in);
            System.out.println("If you want to upgrade, type in y. Else if you don't want, type in n");
            answer = input.next();
            System.out.println("answer: " + answer);

            if(!answer.toLowerCase().equals("y") && !answer.toLowerCase().equals("n")){
                System.out.println("Just type in y or n.");
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
    
    // how to upgrade
    public String howToUpgrade() {
        boolean getOutOfLoop = false;
        String fameOrDollar = "";
        System.out.println("Would you like to use fame or dollar to rank up? Type in f or d.");
        while(!getOutOfLoop){
            Scanner input = new Scanner(System.in);
            fameOrDollar = input.next();
            if(!fameOrDollar.toLowerCase().equals("f") && !fameOrDollar.toLowerCase().equals("d")){
                System.out.println("Type in f or d. I ain't got no time to mess with you");
            } else {
                getOutOfLoop = true;
            }
        }
        return fameOrDollar;
    }
    
    // figure out a players rank
    public int whatRank(){
        boolean getOutOfLoop = false;
        int whatRank = 0;
        System.out.println("What rank would you like to upgrade to?");
        while(!getOutOfLoop){
            Scanner input = new Scanner(System.in);
            whatRank = input.nextInt();
            if(whatRank >1 && whatRank < 6){
                getOutOfLoop = true;
            } else {
                System.out.println("Type in a number between 1 and 6");
            }
        }
        return whatRank;
    }

}
