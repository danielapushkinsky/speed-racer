/* Made by Daniel Apushkinsky
 * Made on June 1 2018
 * 
 * This class is responsible for drawing the ghost. It loads the demo from
 * a text file and converts it to an array list with x, y coordinates and
 * the angle. It also returns the x, y and angle when a tick is called
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
public class Ghost {
  private ArrayList<double[]> coordinates = new ArrayList<double[]>();
 private boolean found = true;
 private boolean done = false;
  
// constructor
  protected Ghost() { 
  }
  
  // return if demo file was found
  protected boolean isFound(){
    return found;
  }
  
  // return if ghost is done
  protected boolean isDone(){
    return done;
  }
  
  // get x coordinate at certain tick
  protected double getX(int a) {
    if (a >= coordinates.size())
    {
      done = true;
      return coordinates.get(coordinates.size()-1)[0];
    }else
      return coordinates.get(a)[0];
  }
  
  // get y coordinate at certain tick
  protected double getY(int a) {
    if (a >= coordinates.size())
    {
      done = true;
      return coordinates.get(coordinates.size()-1)[1];
    }else
    return coordinates.get(a)[1];
  }
  
  // get angle at certain tick
  protected double getAngle(int a) {
    if (a >= coordinates.size())
    {
      return coordinates.get(coordinates.size()-1)[1];
    }else
    return coordinates.get(a)[2];  
  }
  
  // load ghost from text file
  protected void loadGhost(int map)
  {
    //tick = 0;
    found = true;
    done = false;
    
    Scanner dataScan = null;
    
    // check which map to load
    try {
      if (map == 0)
        dataScan = new Scanner(new File("txt/demo0.txt"));
      else if (map == 1)
        dataScan = new Scanner(new File("txt/demo1.txt"));
    }
    catch (Exception e) {
      //System.err.println("No demo file saved on this map");
      found = false;
    }
    
// will run as long as file exists and the exception did not run
    if (found) {
      while (dataScan.hasNext()) {
        double[] temp = new double[3];
        temp[0] = dataScan.nextDouble();
        temp[1] = dataScan.nextDouble();
        temp[2] = dataScan.nextDouble();
       
        coordinates.add(temp);
      }
    } 
  }
}
  
