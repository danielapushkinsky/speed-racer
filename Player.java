/* Made by Daniel Apushkinsky
 * Made on June 1 2018
 * 
 * This class is responsible for keeping track of the player. It keeps track of
 * all movements, (left, right, up, down) as well as the player's position and
 * angle which is later used to create a polygon to test collision.
 */
import java.awt.Polygon;
public class Player {
  // variables
  private int x, y, w, h;
  private double angle;
  private boolean goingLeft, goingRight, goingForward, goingBackward;
  
  // constructor
  protected Player(int x, int y, int w, int h, double angle){ 
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.angle = angle;
  }  
  
  // get if the player is moving left
  protected boolean getGoingLeft(){
    return goingLeft;
  }
  
  // get if the player is moving right
  protected boolean getGoingRight(){
    return goingRight;
  }
  
  // get if the player is moving forward
  protected boolean getGoingForward(){
    return goingForward;
  }
  
  // get if the player is moving backward
  protected boolean getGoingBackward(){
    return goingBackward;
  }
  
  // set the players state of going left
  protected void setGoingLeft(boolean a){
    goingLeft = a;
  }
  
  // set the players state of going right
  protected void setGoingRight(boolean a){
    goingRight = a;
  }
  
  // set the players state of going forward
  protected void setGoingForward(boolean a){
    goingForward = a;
  }
  
  // set the players state of going backward
  protected void setGoingBackward(boolean a){
    goingBackward = a;
  }
  
  // set the players angle
  protected void setAngle(double a){
    angle = a;
  }
  
  // get the players angle
  protected double getAngle(){
    return angle;
  }
  
  // get players width
  protected int getWidth(){
    return w;
  }
  
  // get players height
  protected int getHeight(){
    return h;
  }
  
  // create a polygon of the players current position including the angle of roatation in order to check for collision
  protected Polygon getBounds(){
    int CANVAS_WIDTH = 800;
    int CANVAS_HEIGHT = 600;
    Polygon poly = new Polygon();
    poly.addPoint((int)(10*Math.cos(angle) - 25*Math.sin(angle)) + CANVAS_WIDTH/2, (int)(10*Math.sin(angle) + 25*Math.cos(angle)) + CANVAS_HEIGHT/2);
    poly.addPoint((int)(-10*Math.cos(angle) - 25*Math.sin(angle)) + CANVAS_WIDTH/2, (int)(-10*Math.sin(angle) + 25*Math.cos(angle)) + CANVAS_HEIGHT/2);
    poly.addPoint((int)(10*Math.cos(angle) + 25*Math.sin(angle)) + CANVAS_WIDTH/2, (int)(10*Math.sin(angle) - 25*Math.cos(angle)) + CANVAS_HEIGHT/2);
    poly.addPoint((int)(-10*Math.cos(angle) + 25*Math.sin(angle)) + CANVAS_WIDTH/2, (int)(-10*Math.sin(angle) - 25*Math.cos(angle)) + CANVAS_HEIGHT/2);
    return poly;
  }
}