import java.util.*;
import java.awt.Rectangle;
import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.*;

/*
take is an object that's being used for all takes in each scene. createTake is an contructor that includes the name of scene and number along with
actual area
*/
public class take {

  private String setName = "";
  private int number = 0;
  private Rectangle area;
  public JLabel takelabel;

  public void createTake(String setName, int number, Rectangle area) {
    this.setName = setName;
    this.number = number;
    this.area = area;
  }
  /*getter function for name of set*/
  public String getSetName(){
    return this.setName;
  }

  /*getter function for number of set*/
  public int getNumber(){
    return this.number;
  }

  /*getter function for area of set*/
  public Rectangle getArea(){
    return this.area;
  }

  /*setter function for take of set*/
  public void setTake(JLabel takelabel){
    this.takelabel = takelabel;
  }

  /*getter function for take label*/
  public JLabel getTake(){
    return this.takelabel;
  }

}
