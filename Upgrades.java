import java.util.*;
import java.awt.Rectangle;

/*Upgrades is an object that is used to upgrade user's rank */
public class Upgrades {

  private int level = 0;
  private String currency = "";
  private int amt = 0;
  private Rectangle area;
/*createUpgrades is an contructor that holds player rank, currency and minimum requirements to upgrade  */
  public void createUpgrade(int level, String currency, int amt, Rectangle area) {
    this.level = level;
    this.currency = currency;
    this.amt = amt;
    this.area = area;
  }

  public int getLevel(){
    return this.level;
  }

  public String getCurrency(){
    return this.currency;
  }

  public Rectangle getArea(){
    return this.area;
  }

  public int getAmt(){
    return this.amt;
  }
}
