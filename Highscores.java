/* Made by Daniel Apushkinsky
 * Made on June 1 2018
 * 
 * This class is responsible for keeping track of a players high scores.
 * It takes the time from a txt file and stores it an an array which can
 * be updated, read, and written to. This class can also reset the high
 * scores as well as save them back to a text file.
 */
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;
public class Highscores {
  double[] scores = new double[2];
  
  // constructor, gets scores from file
  protected Highscores(){ 
    getScores();
  }
  
  // get best time on certain map
  protected double getTime(int map)
  {
    return scores[map];
  }
  
  // set time on certain map
  protected void setTime(int map, double time)
  {
    scores[map] = time;
    saveScores();
  }
  
  // save scores to a text file
  protected void saveScores(){ 
    try
    {
      PrintWriter pr = new PrintWriter("txt/highscores.txt");
      pr.println(scores[0]);
      pr.println(scores[1]);
      pr.flush();
      pr.close();
    }
    catch (Exception e)
    {
      System.err.println("Failed to save scores");
    }
  }
  
  // reset scores (set to 999 and write to file)
  protected void reset(){
    scores[0] = 999;
    scores[1] = 999;
    saveScores();
  }
  
  // get scores from text file (called in constructor)
  protected void getScores()
  {
    boolean found = true;    
    Scanner dataScan = null;
    
    try {
      dataScan = new Scanner(new File("txt/highscores.txt"));
    }
    catch (Exception e) {
      System.err.println("Failed to import highscores file");
      found = false;
    }
    
// will run as long as file exists and the exception did not run
    if (found) {
      scores[0] = dataScan.nextDouble();
      scores[1] = dataScan.nextDouble();
    }
  }
}
