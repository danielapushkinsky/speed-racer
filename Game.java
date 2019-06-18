/* Made by Daniel Apushkinsky
 * Made on June 1 2018
 * 
 * This class calculates the correct movement for your player to perform.
 * This class is called every tick (in this case every 10ms from GUI class)
 */
import java.awt.geom.Area;
import java.awt.Shape;
import java.util.Scanner;
import java.util.Random;
import java.io.File;

public class Game{
  
// Variables
  private long stopwatch = 0;

  private static double vX, vY, speed, acc, angle, vAngle, mapX, mapY;
  
  private double time;

  private static int tileSize = 120;
  private static int mapWidth = 20;
  private static int mapHeight = 20;
  
  private Tile[][] tiles = new Tile[mapHeight][mapWidth];
  private int[][] textureType = new int[mapHeight][mapWidth];
  
  private boolean finished;
  
  // Constructor
  protected Game() {
  }
  
  // setup/reset game
  protected void setupGame(Player player){
    finished = false;
    mapX = -260;
    mapY = -265;
    vX = 0;
    vY = 0;
    speed = 0;
    acc = 0;
    vAngle = 0;
    player.setGoingLeft(false);
    player.setGoingRight(false);
    player.setGoingForward(false);
    player.setGoingBackward(false);
    player.setAngle(0); 
    stopwatch = System.currentTimeMillis();
  }
  
  // get requested map from text file
  protected void importMap(int level)
  {
    // for processing each token found in each line
    Scanner dataScan = null;
    boolean found = true;
    
    try {
//inputData is just a text file
      if (level == 0)
        dataScan = new Scanner(new File("txt/map1.txt"));
      else if (level == 1)
        dataScan = new Scanner(new File("txt/map2.txt"));
      //dataScan.useDelimiter(" ");
    }
    catch (Exception e) {
      System.err.println("Map file missing");
      found = false;
    }
    
// will run as long as file exists and the exception did not run
    if (found) {
      int tempX = (int)mapX;
      int tempY = (int)mapY;
      int counter = 0;
      String nLine;
      int tileType;
      while (dataScan.hasNext()) {
        for(int i = 0; i < mapWidth; i++){
          // convert from text file to Tile class
          int temp = dataScan.nextInt();
          if (temp == 0)
            tiles[counter][i] = new Tile(tempX, tempY, tileSize, tileSize, 0);     
          else if (temp >= 1 && temp <= 8)
            tiles[counter][i] = new Tile(tempX, tempY, tileSize, tileSize, 1);
          else if (temp == 9)
            tiles[counter][i] = new Tile(tempX, tempY, tileSize, tileSize, 2);
          
          Random r = new Random();
          if (temp == 0)
            textureType[counter][i] = r.nextInt(26-10) + 10;
          else
            textureType[counter][i] = temp;
          tempY += tileSize;
        }
        counter++;
        tempY += tileSize;
        tempY = (int)mapY;
      }
      //System.out.println("Map Imported");
    }
  }
  
  // update Tiles
  protected void updateTiles(){
    int tempX = (int)mapX;
    int tempY = (int)mapY;
    
    for(int y = 0; y < mapWidth; y++)
    {       
      for(int x = 0; x < mapHeight; x++)
      {
        tiles[x][y].move(tempX, tempY);
        
        tempY += tileSize;
      }
      tempX += tileSize;
      tempY = (int)mapY;
    }  
  }
  
  // figure out player movements
  protected void move(Player player){
    if (!finished)
    { 
      time = (System.currentTimeMillis() - stopwatch)/1000.0;
      // Figure out acceleration
      if (player.getGoingForward())
        acc = 0.045;
      else if (player.getGoingBackward())
        acc = -0.025;
      else if (speed > 0)
        acc = -0.02;
      else if (speed < 0)
        acc = 0.02;
      
      // Figure out angle of car
      if (player.getGoingLeft())
        vAngle = -0.038;
      else if (player.getGoingRight())
        vAngle = 0.038;
      else
        vAngle = 0;
      
      // Set the speed and angle of car
      speed += acc;
      player.setAngle(player.getAngle() + vAngle);
      
      // Keep the angle between -3.14 and 3.14
      if (angle > Math.PI*2)
        angle -= Math.PI*2;
      else if (angle < -Math.PI)
        angle += Math.PI*2;
      
      // Set max speed to 5 or -1 if going backwards
      if (player.getGoingForward())
        speed = Math.min(speed, 5);
      else if (player.getGoingBackward())
        speed = Math.max(speed,-1);
      
      // Find x and y displacement
      vY = speed * Math.cos(player.getAngle());
      vX = speed * Math.sin(player.getAngle());
      
      // Move the map to apply effect of moving car
      
      
      mapX -= vX;
      mapY += vY;

      // Collision detection
      for(int x = 0; x < mapHeight; x++)
      {       
        for(int y = 0; y < mapWidth; y++)
        {
          if (tiles[x][y] != null)
          {
            if (tiles[x][y].getType() == 0)
            {
              // Collision
              if (testIntersection(player.getBounds(), tiles[x][y].getBounds()))
              { 
                // top intersects
                boolean topBotHit = false;
                boolean leftRightHit = false;
                // top not hit
                if (player.getBounds().intersects(tiles[x][y].getX(), tiles[x][y].getY(), vX, tileSize) || player.getBounds().intersects(tiles[x][y].getX()+tileSize+vX, tiles[x][y].getY(), -vX, tileSize))
                  topBotHit = true;
                if (player.getBounds().intersects(tiles[x][y].getX(), tiles[x][y].getY()+tileSize, tileSize, vY) || player.getBounds().intersects(tiles[x][y].getX(), tiles[x][y].getY(), tileSize, -vY))
                  leftRightHit = true;
                
                // if hit a corner stop car
                if (topBotHit && leftRightHit)
                {
                  mapX += vX;
                  mapY -= vY;
                  speed = 0;
                }
                
                // if hit side slow down
                if (topBotHit)
                {
                  mapX += vX;
                  mapY -= vY;
                  speed *= 0.8;
                  vY = speed * Math.cos(player.getAngle());
                  vX = speed * Math.sin(player.getAngle()+Math.PI);
                  mapX -= vX;
                  mapY += vY;
                } else if (leftRightHit)
                {
                  mapX += vX;
                  mapY -= vY;
                  speed *= 0.8;  
                  vY = speed * Math.cos(player.getAngle()+Math.PI);
                  vX = speed * Math.sin(player.getAngle());
                  mapX -= vX;
                  mapY += vY;
                }
              }
            }
            // if hit the finish line
            if (tiles[x][y].getType() == 2)
            {
              if (testIntersection(player.getBounds(), tiles[x][y].getBounds()))
              {
                
                finished = true;
              }
            }
          }
        }
      }
    }
  }
  
  // Test intersection of two shapes  
  protected static boolean testIntersection(Shape shapeA, Shape shapeB) {
    Area areaA = new Area(shapeA);
    areaA.intersect(new Area(shapeB));
    return !areaA.isEmpty();
  }
  
  
  // get current time of run
  protected double getTime(){
    return time;
  }
  
  // get map size
  protected int getMapWidth(){
    return mapWidth;
  }
  
  // get map size
  protected int getMapHeight(){
    return mapHeight;
  }
  
  // get tile x coordinate
  protected int getTileX(int x, int y){
    return tiles[x][y].getX();
  }
  
  // get tile y coordinate
  protected int getTileY(int x, int y){
    return tiles[x][y].getY();
  }
  
  // get tile size
  protected int getTileSize(){
    return tileSize;
  }
  
  // get type of tile (road/building/finish)
  protected int getTextureType(int x, int y){
    return textureType[x][y];
  }
  
  // get current x map shift
  protected double getMapX(){
    return mapX;
  }
  
  // get current y map shift
  protected double getMapY(){
    return mapY;
  }
  
  // return if player crossed the finish line
  protected boolean isFinished(){
    return finished;
  }
}
