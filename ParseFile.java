
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
import java.awt.Rectangle;

/*ParseFile is an java file that reads xml files and save all necessary data into multiple data structure to build basic settings for the game*/
public class ParseFile {

  public static ArrayList<Card> cards = new ArrayList<Card>();
  public static ArrayList<Player> players = new ArrayList<Player>();

  public static void setPlayers(ArrayList<Player> p) {
      for (int i = 0; i < p.size(); i++) {
          players.add(p.get(i));
      }
  }
/*parseCard parses all cards from cards.xml and save them to data structures for future use.*/
  public static void parseCards(){
    try {
      String cardFile = "xmlFiles/cards.xml";
      File inputFile = new File(cardFile);

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();

      NodeList cardList = doc.getElementsByTagName("card");

      for (int temp = 0; temp < cardList.getLength(); temp++) {
        Node node = cardList.item(temp);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          Element element = (Element) node;
          Card currCard = new Card();
          String name = element.getAttribute("name");
          // System.out.println(name);
          String sceneImage = element.getAttribute("img");
          int budget = Integer.parseInt(element.getAttribute("budget"));
          int sceneNumber = Integer.parseInt(((Element) element.getElementsByTagName("scene").item(0)).getAttribute("number"));
          String description = element.getElementsByTagName("scene").item(0).getTextContent();
          NodeList part = element.getElementsByTagName("part");
          ArrayList<part> parts = new ArrayList<part>();

          for (int tempPart = 0; tempPart < part.getLength(); tempPart++) {

			Node partNode = part.item(tempPart);
			if (partNode.getNodeType() == Node.ELEMENT_NODE) {
				Element partElement = (Element) partNode;

				String partName = partElement.getAttribute("name");
				String partDescription = partElement.getElementsByTagName("line").item(0).getTextContent();
				int level = Integer.parseInt(partElement.getAttribute("level"));

				NodeList area = partElement.getElementsByTagName("area");
				Node nodeHolder = area.item(0);
				Element eHolder = (Element) nodeHolder;

				int x = Integer.parseInt(eHolder.getAttribute("x"));
				int y = Integer.parseInt(eHolder.getAttribute("y"));
				int height = Integer.parseInt(eHolder.getAttribute("h"));
				int width = Integer.parseInt(eHolder.getAttribute("w"));
                // System.out.println(x);
                Rectangle cardPosition = new Rectangle(x,y,width,height);
                part currPart = new part();
                currPart.createPart(partName, level, cardPosition, partDescription);
                parts.add(currPart);
			}
		}
          currCard.createCard(name, sceneImage, budget, sceneNumber, description, parts, "");
          cards.add(currCard);
        }
      }

    } catch (Exception e) {
      System.out.print("Error for parsing the cards.");
    }
  }


  public static HashMap<String,Room> rooms = new HashMap<String,Room>();

/*parseBoard reads board.xml and it is essential part of the program to make board, scene and so on*/
  public static void parseBoard(){
    try {
      String boardFile = "xmlFiles/board.xml";
      File inputFile = new File(boardFile);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(inputFile);
      doc.getDocumentElement().normalize();

      NodeList nList = doc.getElementsByTagName("board");

      // Loop for sets
      for (int temp = 0; temp < nList.getLength(); temp++) {
        Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					NodeList setList = eElement.getElementsByTagName("set");
          NodeList officeList = eElement.getElementsByTagName("office");
          NodeList trailerList = eElement.getElementsByTagName("trailer");

          // loop through each set
					for (int s = 0; s < setList.getLength(); s++) {
						Node set = setList.item(s);
            // grab the room name
						if (set.getNodeType() == Node.ELEMENT_NODE) {
							Element setElement = (Element) set;
							String setName = setElement.getAttribute("name");
              // System.out.printf("roomName: %s \n", roomName);

              NodeList boardParts = setElement.getElementsByTagName("part");
              NodeList boardNeighbors = setElement.getElementsByTagName("neighbor");
              NodeList boardTakes = setElement.getElementsByTagName("take");
              NodeList boardAreas = setElement.getElementsByTagName("area");

              Element boardA = (Element) boardAreas.item(0);
              int xSet = Integer.parseInt(boardA.getAttribute("x"));
              int ySet = Integer.parseInt(boardA.getAttribute("y"));
              int hSet = Integer.parseInt(boardA.getAttribute("h"));
              int wSet = Integer.parseInt(boardA.getAttribute("w"));

              Rectangle setPosition = new Rectangle(xSet,ySet,wSet,hSet);

              Element takeNum = (Element) boardTakes.item(0);

              int number = Integer.parseInt(takeNum.getAttribute("number"));

              // System.out.printf("number: %d \n", number);
              ArrayList<partExtra> extraParts = new ArrayList<partExtra>();
              for (int j = 0; j < boardParts.getLength(); j++) {
                  Element boardPart = (Element) boardParts.item(j);
                  String part = boardPart.getAttribute("name");

                  String line = boardPart.getTextContent().trim();
                  int level = Integer.parseInt(boardPart.getAttribute("level"));
                  NodeList partAreas = boardPart.getElementsByTagName("area");

                  int xPart = 0;
                  int yPart = 0;
                  int hPart = 0;
                  int wPart = 0;
                  for (int l = 0; l < partAreas.getLength(); l++) {
                      // Create neighbor elements
                      Element partArea = (Element) partAreas.item(l);

                      xPart = Integer.parseInt(partArea.getAttribute("x"));
                      yPart = Integer.parseInt(partArea.getAttribute("y"));
                      hPart = Integer.parseInt(partArea.getAttribute("h"));
                      wPart = Integer.parseInt(partArea.getAttribute("w"));
                      // System.out.printf("partX: %d \n", x);
                  }
                  // System.out.println(part);
                  // System.out.printf("x: %s, y: %s, h: %d, w: %d\n", xPart, yPart, wPart, hPart);
                  Rectangle rectPartArea = new Rectangle(xPart,yPart,wPart,hPart);
                  partExtra extras = new partExtra();
                  extras.createPartExtra(part, setName, level, rectPartArea, line);
                  extraParts.add(extras);
              }

              ArrayList<String> neighbors = new ArrayList<String>();
              for (int j = 0; j < boardNeighbors.getLength(); j++) {
                 Element boardNeighbor = (Element) boardNeighbors.item(j);
                 String name = "";
                 if (boardNeighbor.getAttribute("name").equals("office")) {
                     name = "Casting Office";
                 } else {
                     name = boardNeighbor.getAttribute("name");
                 }
                 neighbors.add(name);
              }

              int xTake = 0;
              int yTake = 0;
              int hTake = 0;
              int wTake = 0;
              ArrayList<take> takes = new ArrayList<take>();
              for (int j = 0; j < boardTakes.getLength(); j++) {
                  Element boardTake = (Element) boardTakes.item(j);
                  NodeList takeAreas = boardTake.getElementsByTagName("area");
                  for (int l = 0; l < takeAreas.getLength(); l++) {
                      // Create neighbor elements
                      Element takeArea = (Element) takeAreas.item(l);

                      xTake = Integer.parseInt(takeArea.getAttribute("x"));
                      yTake = Integer.parseInt(takeArea.getAttribute("y"));
                      hTake = Integer.parseInt(takeArea.getAttribute("h"));
                      wTake = Integer.parseInt(takeArea.getAttribute("w"));
                }
                Rectangle takeArea = new Rectangle(xTake,yTake,wTake,hTake);
                take currTake = new take();
                currTake.createTake(setName, number, takeArea);
                takes.add(currTake);
              }
              Room currRoom = new Room();
              currRoom.createRoom(setName, extraParts, number, number, neighbors, setPosition, takes);
              rooms.put(setName, currRoom);
    				}
          }
          // loop through trailer
          for (int s = 0; s < trailerList.getLength(); s++) {
            Node trailerNode = trailerList.item(s);
            // grab the room name
            if (trailerNode.getNodeType() == Node.ELEMENT_NODE) {
              Element trailerElement = (Element) trailerNode;

              NodeList trailerNeighbors = trailerElement.getElementsByTagName("neighbor");
              NodeList trailerAreas = trailerElement.getElementsByTagName("area");

              Element tA = (Element) trailerAreas.item(0);
              int xT = Integer.parseInt(tA.getAttribute("x"));
              int yT = Integer.parseInt(tA.getAttribute("y"));
              int hT = Integer.parseInt(tA.getAttribute("h"));
              int wT = Integer.parseInt(tA.getAttribute("w"));

              Rectangle trailerPosition = new Rectangle(xT,yT,wT,hT);

              ArrayList<String> neighbors = new ArrayList<String>();
              for (int j = 0; j < trailerNeighbors.getLength(); j++) {

                  Element trailerNeighbor = (Element) trailerNeighbors.item(j);
                  String name = "";
                 if (trailerNeighbor.getAttribute("name").equals("office")) {
                     name = "Casting Office";
                 } else {
                     name = trailerNeighbor.getAttribute("name");
                 }
                neighbors.add(name);
              }
              ArrayList<take> takes = new ArrayList<take>();
              ArrayList<partExtra> extraParts = new ArrayList<partExtra>();
              Room currRoom = new Room();
              currRoom.createRoom("trailer", extraParts, takes.size(), takes.size(), neighbors, trailerPosition, takes);
              rooms.put("trailer", currRoom);
            }
          }
          // loop through office
          for (int s = 0; s < officeList.getLength(); s++) {
            Node officeNode = officeList.item(s);
            // grab the room name
            if (officeNode.getNodeType() == Node.ELEMENT_NODE) {
              Element officeElement = (Element) officeNode;

              NodeList officeNeighbors = officeElement.getElementsByTagName("neighbor");
              NodeList officeAreas = officeElement.getElementsByTagName("area");

              Element oA = (Element) officeAreas.item(0);
              int xO = Integer.parseInt(oA.getAttribute("x"));
              int yO = Integer.parseInt(oA.getAttribute("y"));
              int hO = Integer.parseInt(oA.getAttribute("h"));
              int wO = Integer.parseInt(oA.getAttribute("w"));

              Rectangle officePosition = new Rectangle(xO,yO,wO,hO);
              ArrayList<String> neighbors = new ArrayList<String>();
              for (int j = 0; j < officeNeighbors.getLength(); j++) {

                  Element officeNeighbor = (Element) officeNeighbors.item(j);
                  neighbors.add(officeNeighbor.getAttribute("name"));
              }
              CastingOffice co = new CastingOffice();
              co.setArea(officePosition);
              co.setNeighbors(neighbors);
              ArrayList<take> takes = new ArrayList<take>();
              ArrayList<partExtra> extraParts = new ArrayList<partExtra>();
              Room currRoom = new Room();
              currRoom.createRoom("Casting Office", extraParts, takes.size(), takes.size(), neighbors, officePosition, takes);
              rooms.put("Casting Office", currRoom);
            }
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Error in parsing the board.");
    }
  }
}
