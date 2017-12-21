import java.util.*;
import java.awt.Rectangle;

/*partExtra is an object that is responsible for all extra roles in each scene*/
public class partExtra {

  private String partName = "";
  private String setName = "";
  private int level = 0;
  private Rectangle area;
  private String line = "";
  private boolean taken = false;
  
/*createPartExtra is an constructor that includes all attributes of extra roles*/
  public void createPartExtra(String partName, String setName, int level, Rectangle area, String line) {
    this.partName = partName;
    this.setName = setName;
    this.level = level;
    this.area = area;
    this.line = line;
  }

  public String getPartName(){
    return this.partName;
  }

  public boolean getTaken(){
    return this.taken;
  }

  public void setTaken(boolean taken){
    this.taken = taken;
  }

  public String getSetName(){
    return this.setName;
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
