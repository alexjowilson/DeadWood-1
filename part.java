import java.util.*;
import java.awt.Rectangle;

/*part is an object that creates part for all scenes*/
public class part {

  private String name = "";
  private int level = 0;
  private Rectangle area;
  private String line = "";
  private boolean taken = false;

  /*createPart is an constructor that has all attributes of part. For each attribute, there is getter and setter function for it */
  public void createPart(String name, int level, Rectangle area, String line) {
    this.name = name;
    this.level = level;
    this.area = area;
    this.line = line;
  }

  public String getName(){
    return this.name;
  }

  public boolean getTaken(){
    return this.taken;
  }

  public void setTaken(boolean taken){
    this.taken = taken;
  }

  public int getLevel(){
    return this.level;
  }

  public Rectangle getArea(){
    return this.area;
  }

  public String getLine(){
    return this.line;
  }
}
