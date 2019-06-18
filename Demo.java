/* Made by Daniel Apushkinsky
 * Made on June 1 2018
 * 
 * This class takes every coordinate and angle of your car, and saves it to an
 * arraylist, which later saves to a text file if the writeTo method is called.
 * It is only called when you set a new personal best
 */
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;
public class Demo {
  
  // variables
  private ArrayList<double[]> coordinates = new ArrayList<double[]>();
  private int tick;
  
  // initialize demo class
  protected Demo() {  
  } 
  
  // increase tick
  protected void increaseTick(){
    tick++;
  }
  
  // get current tick value
  protected int getTick(){
    return tick;
  }
  
  // reset the demo
  protected void clear()
  {
    tick = 0;
    coordinates.clear();
  }
  
  // add coordinates and angle to array
  public void addPoint(int x, int y, double a){
    double[] temp = new double[3];
    temp[0] = x;
    temp[1] = y;
    temp[2] = a;
    coordinates.add(temp);
  }
  
  protected void clear(int map){
    try
    {
      //PrintWriter pr = new PrintWriter(file);
      PrintWriter pr = null;
      
      // find out what to name the file
      if (map == 0)
        pr = new PrintWriter("txt/demo0.txt");
      else if (map == 1)
        pr = new PrintWriter("txt/demo1.txt");
        
      pr.println(0 + " " + 0 + " " + 0);
      pr.flush();
      pr.close();
    }
    catch (Exception e)
    {}
  
  }
  
  // save demo as a text file for a certain map
  protected void writeTo(int map){ 
    //write
    try
    {
      //PrintWriter pr = new PrintWriter(file);
      PrintWriter pr = null;
      
      // find out what to name the file
      if (map == 0)
        pr = new PrintWriter("txt/demo0.txt");
      else if (map == 1)
        pr = new PrintWriter("txt/demo1.txt");
        
      // write the file
      for (int i=0; i < coordinates.size() ; i++)
      {
        pr.println(coordinates.get(i)[0] + " " + coordinates.get(i)[1] + " " + coordinates.get(i)[2]);
      }
      
      // close the file
      pr.flush();
      pr.close();
    }
    catch (Exception e)
    {
      System.err.println("Demo failed to save");
    }
  }
}
