/* Made by Daniel Apushkinsky
 * Made on June 1 2018
 * 
 * This class is responsible for all the gui, as well as initializing
 * all other classes (except main)
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.net.URL;
import javax.sound.sampled.*;
import java.text.DecimalFormat;

public class GUI extends JFrame implements ActionListener, KeyListener{
  
  // variables
  private static final int CANVAS_WIDTH = 800;
  private static final int CANVAS_HEIGHT = 600;
  
  private static final Color CANVAS_BACKGROUND = Color.GREEN;
  
  private DrawCanvas canvas; // the custom drawing canvas (extends JPanel)
  
  // textures
  private BufferedImage[][] carTextures = new BufferedImage[5][3];
  private BufferedImage[] textures = new BufferedImage[30];
  private BufferedImage[] mainMenuImg = new BufferedImage[5];
  private BufferedImage[] garageImg = new BufferedImage[4];
  private BufferedImage[] mapImg = new BufferedImage[2];
  private BufferedImage aboutImg;
  private BufferedImage settingImg;
  
  
  private int mainMenuOption = 0;
  private int mapOption = 0;
  private int carMenuOption = 0;
  private int carSelected = 0;
  
  //private ArrayList<Int[2]> pbText = new ArrayList();
  private Timer timer;
  
  private Font font1 = new Font("TimesRoman", Font.BOLD, 20);
  
  private enum STATE{
    MENU,
      MAPMENU,
      GAME,
      GARAGE,
      ABOUT,
      EXIT
  };
  
  private DecimalFormat df = new DecimalFormat();
  
  private Clip bgMusic = null;
  
  
  private Game game;
  private Player player;
  private Demo saveDemo;
  private Player ghostPlayer;
  private Ghost ghost;
  private Highscores highscores;
  
  private STATE state = STATE.MENU;
  
  // constructor
  protected GUI() {
    // initialize all variables
    timer = new Timer(10, this);
    
    df.setMinimumFractionDigits(3);
    
    player = new Player(CANVAS_WIDTH/2-10, CANVAS_HEIGHT/2-25, 20, 40, 0);
    ghost = new Ghost();
    game = new Game(); // set up the game  
    saveDemo = new Demo();
    highscores = new Highscores();
    
    // Set up a custom drawing JPanel
    canvas = new DrawCanvas();
    canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    
    // Add both panels to this JFrame
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(canvas, BorderLayout.CENTER);
    
    // "this" JFrame fires KeyEvent
    addKeyListener(this);
    
    loadImages();
    loadMusic();
    
    // Handle the CLOSE button
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    setTitle("SpeedRacer by Daniel Apushkinsky");
    pack();           // pack all the components in the JFrame
    setVisible(true); // show it
    requestFocus();   // set the focus to JFrame to receive KeyEvent
    timer.start();
  }
  
  // import images
  protected void loadImages(){
    boolean found = true;
    
    BufferedImage buildingSheet = null;
    BufferedImage carSheet = null;
    BufferedImage roadSheet = null;
    try {
      // main menu
      mainMenuImg[0] = ImageIO.read(Game.class.getResourceAsStream("images/menu0.png"));
      mainMenuImg[1] = ImageIO.read(Game.class.getResourceAsStream("images/menu1.png"));
      mainMenuImg[2] = ImageIO.read(Game.class.getResourceAsStream("images/menu2.png"));
      mainMenuImg[3] = ImageIO.read(Game.class.getResourceAsStream("images/menu3.png"));
      mainMenuImg[4] = ImageIO.read(Game.class.getResourceAsStream("images/menu4.png"));
      
      // map selection
      mapImg[0] = ImageIO.read(Game.class.getResourceAsStream("images/mapmenu0.png"));
      mapImg[1] = ImageIO.read(Game.class.getResourceAsStream("images/mapmenu1.png"));
      
      // about game
      aboutImg = ImageIO.read(Game.class.getResourceAsStream("images/aboutmenu.png"));
      
      // roads
      roadSheet = ImageIO.read(Game.class.getResourceAsStream("images/roadSheet.png"));
      textures[1] = roadSheet.getSubimage(0,0,250,250);
      textures[2] = roadSheet.getSubimage(250,0,250,250);
      textures[3] = roadSheet.getSubimage(500,0,250,250);
      textures[4] = roadSheet.getSubimage(750,0,250,250);
      textures[5] = roadSheet.getSubimage(0,250,250,250);
      textures[6] = roadSheet.getSubimage(250,250,250,250);
      textures[7] = roadSheet.getSubimage(500,250,250,250);
      textures[9] = roadSheet.getSubimage(750,250,250,250);
      
      // garage menu
      garageImg[0] = ImageIO.read(Game.class.getResourceAsStream("images/garage0.png"));
      garageImg[1] = ImageIO.read(Game.class.getResourceAsStream("images/garage1.png"));
      garageImg[2] = ImageIO.read(Game.class.getResourceAsStream("images/garage2.png"));
      garageImg[3] = ImageIO.read(Game.class.getResourceAsStream("images/garage3.png"));
      
      // different cars and animations
      carSheet = ImageIO.read(Game.class.getResourceAsStream("images/carSheet.png"));
      carTextures[0][0] = carSheet.getSubimage(0,0,150,280);
      carTextures[0][1] = carSheet.getSubimage(150,0,150,280);
      carTextures[0][2] = carSheet.getSubimage(300,0,150,280);
      carTextures[1][0] = carSheet.getSubimage(0,280,150,280);
      carTextures[1][1] = carSheet.getSubimage(150,280,150,280);
      carTextures[1][2] = carSheet.getSubimage(300,280,150,280);
      carTextures[2][0] = carSheet.getSubimage(0,560,150,280);
      carTextures[2][1] = carSheet.getSubimage(150,560,150,280);
      carTextures[2][2] = carSheet.getSubimage(300,560,150,280);
      carTextures[3][0] = carSheet.getSubimage(0,840,150,280);
      carTextures[3][1] = carSheet.getSubimage(150,840,150,280);
      carTextures[3][2] = carSheet.getSubimage(300,840,150,280);
      carTextures[4][0] = carSheet.getSubimage(0,1120,150,280);
      
      // buildings
      buildingSheet = ImageIO.read(Game.class.getResourceAsStream("images/buildings.png"));
      textures[10] = buildingSheet.getSubimage(0,0,250,250);
      textures[11] = buildingSheet.getSubimage(250,0,250,250);
      textures[12] = buildingSheet.getSubimage(500,0,250,250);
      textures[13] = buildingSheet.getSubimage(750,0,250,250);
      textures[14] = buildingSheet.getSubimage(1000,0,250,250);
      
      textures[15] = buildingSheet.getSubimage(0,250,250,250);
      textures[16] = buildingSheet.getSubimage(250,250,250,250);
      textures[17] = buildingSheet.getSubimage(500,250,250,250);
      textures[18] = buildingSheet.getSubimage(750,250,250,250);
      textures[19] = buildingSheet.getSubimage(1000,250,250,250);
      
      textures[20] = buildingSheet.getSubimage(0,250,250,250);
      textures[21] = buildingSheet.getSubimage(250,250,250,250);
      textures[22] = buildingSheet.getSubimage(500,250,250,250);
      textures[23] = buildingSheet.getSubimage(750,250,250,250);
      textures[24] = buildingSheet.getSubimage(1000,250,250,250);
      
      textures[25] = buildingSheet.getSubimage(0,250,250,250);
      textures[26] = buildingSheet.getSubimage(250,250,250,250);
    } catch (Exception e)
    {
      System.err.println("Failed to import textures");
    }
  }
  
  // Key pressed action method
  public void keyPressed(KeyEvent evt) {
    if (state == STATE.GAME)
    {
      switch(evt.getKeyCode()) {
        case 37:           
          //left
          player.setGoingLeft(true);
          player.setGoingRight(false);
          break;
        case 38:
          //up
          player.setGoingForward(true);
          player.setGoingBackward(false);
          break;
        case 39:           
          //right
          player.setGoingRight(true);
          player.setGoingLeft(false);
          break;
        case 40:
          //down
          player.setGoingBackward(true);
          player.setGoingForward(false);
          break;
      }
    }
  }
  
  // method to detect action performed
  public void actionPerformed(ActionEvent e)
  {
    // timer gets called every 10ms
    if(e.getSource() == timer)
    {
      // if player is in game move him
      if (state == STATE.GAME)
      {
        game.move(player);
        //ghost.tick++;
        saveDemo.increaseTick();
        saveDemo.addPoint(CANVAS_WIDTH/2-(int)game.getMapX(), CANVAS_HEIGHT/2-(int)game.getMapY(), player.getAngle());
        // when player just hit the finish line
        if (game.isFinished())
        {
          state = STATE.MAPMENU;
          // if this run is a new PB save demo and save to high scores
          if (game.getTime() < highscores.getTime(mapOption))
          {
            highscores.setTime(mapOption, game.getTime());
            saveDemo.writeTo(mapOption);
          }
        }
      }
      // redraw image every 10ms
      canvas.repaint(); 
      if (state == STATE.EXIT){
        timer.stop();
        System.exit(0);
      }
    }
  }
  
  // Delay method
  private static void delay(int milli){
    try{
      Thread.sleep(milli);
    }
    catch(Exception e){}
  }
  
  // Key released action method
  public void keyReleased(KeyEvent evt)
  {
    // if player is currently in game
    if (state == STATE.GAME)
    {
      switch(evt.getKeyCode()) {
        case 37:           
          //left
          player.setGoingLeft(false);
          break;
        case 38:
          //up
          player.setGoingForward(false);
          break;
        case 39:           
          //right
          player.setGoingRight(false);
          break;
        case 40:
          //down
          player.setGoingBackward(false);
          break;
        case 27:
          state = STATE.MAPMENU;
          break;
      }
      // if player is in main menu
    } else if (state == STATE.MENU)
    {
      switch(evt.getKeyCode()) {
        // up
        case 38:
          mainMenuOption--;
          if (mainMenuOption < 0)
            mainMenuOption = 4;
          break;
          // down
        case 40:
          mainMenuOption++;
          if (mainMenuOption > 4)
            mainMenuOption = 0;
          break;
          // enter
        case KeyEvent.VK_ENTER:
          switch(mainMenuOption) {
          case 0:
            // Play
            state = STATE.MAPMENU;
            break;
          case 1:
            // Garage
            state = STATE.GARAGE;
            break;
          case 2:
            // reset
            highscores.reset();
            //highscores.reset();
            //System.out.println("del");
            saveDemo.clear(0);
            saveDemo.clear(1);
            break;
          case 3:
            state = STATE.ABOUT;
            break;
          case 4:
            // Exit
            bgMusic.stop();
            dispose();
            state = STATE.EXIT;
            break;
        }
      }
      // if player is in map menu
    }else if (state == STATE.MAPMENU)
    {
      switch(evt.getKeyCode()) {
        // left
        case 37:
          mapOption--;
          if (mapOption < 0)
            mapOption = 1;
          break;
          // right
        case 39:
          mapOption++;
          if (mapOption > 1)
            mapOption = 0;
          break;
          // enter
        case KeyEvent.VK_ENTER:
          //System.out.println(mapOption);
          saveDemo = new Demo();
          ghost = new Ghost();
          game.importMap(mapOption);
          game.setupGame(player);
          ghost.loadGhost(mapOption);
          saveDemo.clear();
          state = STATE.GAME;
          break;
          //escape
        case 27:
          mapOption = 0;
          state = STATE.MENU;
          break;
      }
      // if player is in garage menu
    } else if (state == STATE.GARAGE)
    {
      switch(evt.getKeyCode()) {
        // left
        case 37:
          carMenuOption--;
          if (carMenuOption < 0)
            carMenuOption = 1;
          break;
          // right
        case 39:
          carMenuOption++;
          if (carMenuOption > 3)
            carMenuOption = 0;
          break;
          //excape
        case 27:
          carMenuOption = carSelected;
          state = STATE.MENU;
          break;
          // enter
        case KeyEvent.VK_ENTER:
          // save the car selected
          carSelected = carMenuOption;
          state = STATE.MENU;
          break;
      }
      // if player is in about screen
    }else if (state == STATE.ABOUT)
    {
      switch(evt.getKeyCode()) {
        // escape
        case 27:
          state = STATE.MENU;
          break;
          // enter
        case KeyEvent.VK_ENTER:
          state = STATE.MENU;
          break;
      }
    }
  }
  
  public void keyTyped(KeyEvent evt) {}
  
  // load and start background music
  private void loadMusic(){
    try {
      // Open an audio input stream.
      URL url = this.getClass().getClassLoader().getResource("sounds/MicroMachinesMusic.wav");
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
      // Get a sound clip resource.
      bgMusic = AudioSystem.getClip();
      // Open audio clip and load samples from the audio input stream.
      bgMusic.open(audioIn);
      bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
      bgMusic.start();
    } catch (Exception e) {
      System.out.println("Failed to load background music");
    }
  }
  
  // draw main menu
  private void drawMenu(Graphics g){
    g.drawImage(mainMenuImg[mainMenuOption], 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
  }
  
  // draw tiled map
  private void drawMap(Graphics g){  
    Graphics2D g2d = (Graphics2D)g;
    
    game.updateTiles();
    
    for(int y = 0; y < game.getMapWidth(); y++)
    {       
      for(int x = 0; x < game.getMapHeight(); x++)
      {
        g2d.drawImage(textures[game.getTextureType(x, y)], game.getTileX(x,y), game.getTileY(x,y), game.getTileSize(), game.getTileSize(), null);
      }
    }
    g2d.setFont(font1); 
    g2d.setColor(Color.white);
    g2d.drawString("HIGHSCORE: " + df.format(game.getTime()) + " SECONDS", 10, 20);
  }
  
  // draw players car and animate it
  private void drawPlayer(Graphics g){
    Graphics2D g2d = (Graphics2D)g;
    // rotate car accordingly
    g2d.rotate(player.getAngle(), CANVAS_WIDTH/2, CANVAS_HEIGHT/2);
    // check which image to use (left, right, straight)
    if (player.getGoingLeft())
      g2d.drawImage(carTextures[carSelected][1], CANVAS_WIDTH/2-player.getWidth()/2, CANVAS_HEIGHT/2-player.getHeight()/2, player.getWidth(), player.getHeight(), null);
    else if (player.getGoingRight())
      g2d.drawImage(carTextures[carSelected][2], CANVAS_WIDTH/2-player.getWidth()/2, CANVAS_HEIGHT/2-player.getHeight()/2, player.getWidth(), player.getHeight(), null);
    else
      g2d.drawImage(carTextures[carSelected][0], CANVAS_WIDTH/2-player.getWidth()/2, CANVAS_HEIGHT/2-player.getHeight()/2, player.getWidth(), player.getHeight(), null);
  }
  
  // draw ghost car
  private void drawGhost(Graphics g){ 
    //if ghost is needed and is not done
    if (ghost.isFound() && !ghost.isDone())
    {
      Graphics2D g2d = (Graphics2D)g;
      int x = (int)ghost.getX(saveDemo.getTick())+(int)game.getMapX()-player.getWidth()/2;
      int y = (int)ghost.getY(saveDemo.getTick())+(int)game.getMapY()-player.getHeight()/2;
      g2d.rotate(ghost.getAngle(saveDemo.getTick()), x+player.getWidth()/2, y+player.getHeight()/2);
      g2d.drawImage(carTextures[4][0], x, y, player.getWidth(), player.getHeight(), null);
      g2d.rotate(-ghost.getAngle(saveDemo.getTick()), x+player.getWidth()/2, y+player.getHeight()/2);
    }
  }
  
  // draw map menu
  private void drawMapMenu(Graphics g){
    Graphics2D g2d = (Graphics2D)g;
    g2d.setFont(font1); 
    g2d.setColor(Color.white);
    
    g2d.drawImage(mapImg[mapOption], 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
    
    // draw highscore and ghost availability
    if (highscores.getTime(mapOption) != 999.0)
    {
      g2d.drawString("GHOST AVAILABLE", 400, 280);
      g2d.drawString("HIGHSCORE: " + df.format(highscores.getTime(mapOption)) + " SECONDS", 400, 240);
    }
    else
    {
      g2d.drawString("GHOST UNABAILABLE", 400, 280);
      g2d.drawString("HIGHSCORE: N/A", 400, 240);
    }
  }
  
  // draw garage menu
  private void drawGarage(Graphics g){
    Graphics2D g2d = (Graphics2D)g;
    g2d.drawImage(garageImg[carMenuOption], 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
  }
  
  // draw about screen
  private void drawAbout(Graphics g){
    Graphics2D g2d = (Graphics2D)g;
    g2d.drawImage(aboutImg, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
  }  
  
  // main class to draw game
  class DrawCanvas extends JPanel {
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      // determine which screen to draw
      if (state == STATE.GAME)
      {
        drawMap(g);  
        drawGhost(g);
        drawPlayer(g);
      } else if (state == STATE.MENU)
      {
        drawMenu(g);
      } else if (state == STATE.MAPMENU)
      {
        drawMapMenu(g);
      }else if (state == STATE.GARAGE)
      {
        drawGarage(g);
      } else if (state == STATE.ABOUT)
      {
        drawAbout(g);
      }
    }
  }
}
