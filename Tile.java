/* Made by Daniel Apushkinsky
 * Made on June 1 2018
 * 
 * This class is responsible for drawing and determining where the map is.
 * Each tile has coordinates, width, height, and the type of tile (road,
 * building, etc).
 */
import java.awt.Rectangle;
public class Tile {
  // variables
  private int x, y, w, h, type;
  
  // constructor
  protected Tile(int x, int y, int w, int h, int type) { 
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.type = type;
  }
  
  // move the current tile to given coordinates
  protected void move(int x, int y){
    this.x = x;
    this.y = y;
  }
  
  // get x coordinate of tile
  protected int getX()
  {
    return x;
  }
  
  // get y coordinate of tile
  protected int getY()
  {
    return y;
  }
  
  // get the type of tile (road, building, finish)
  protected int getType(){
    return type;
  }
  
  // create a rectangle of the tile for collision
  protected Rectangle getBounds(){
    return new Rectangle(x,y,w,h);
  }
}