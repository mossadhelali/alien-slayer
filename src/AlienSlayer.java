
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;

/**
 * <h1> Alien Slayer </h1>
 * 
 * <p> 
 * Alien Slayer is a game where the player takes the role of a space ship
 * Playing to kill the incoming Alien ships. The player will face the danger of being killed
 * by two types of alien ships: Shooters that try to kill the player by shooting bullets and Followers 
 * that try to kill the player by colliding with the player ship. In order to kill the aliens, 
 * the player shoots bullets on them by controlling the mouse and avoids collision with them by moving 
 * with WASD keyboard controls.
 * </p>
 * 
 * <p> the following are the sources of the game image graphics and sounds:<br>
 * Player Ship: webtoolhub.com , under the GPL license.<br>
 * Enemies: http://www.vector-eps.com , under the constraints of usage for personal or educational purposes.<br>
 * Bullets: http://flash-game-programming-tutorials.blogspot.com , open license.<br>
 * Background: http://joolzanfire.deviantart.com , open license.<br>
 * Crosshair: http://commons.wikimedia.org , under the  CC0 1.0 Universal Public Domain Dedication license.<br>
 * Shooting sounds: http://cd.textfiles.com/ , open license.<br>
 * Player and Alien dying sounds: http://freesfx.co.uk/ , open license.<br>
 * Player win sound: http://www.consumingfireartisticcreations.com/, open license
 * </p>
 * 
 * 
 * @author Mossad Helali
 * @version 1.0
 * @
 *
 */
@SuppressWarnings("serial")				// just removing the warning given by eclipse
public class AlienSlayer extends JFrame {
	
	public static final int WIDTH = 960;	// the game border width and height
	public static final int HEIGHT = 720;
	
	public static void main (String [] args){
		new AlienSlayer();
		
	}
	
	public AlienSlayer (){
		setTitle("Alien Slayer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		
		MyPanel panel = new MyPanel();
		
		add(panel);
		setVisible(true);
		
		panel.requestFocus();
		panel.init();
		Thread t = new Thread(panel);
		t.start();							// the looping part of the game is run in a separate thread
		
		
	}
	
	
	
}

@SuppressWarnings("serial")			// just removing the warning given by eclipse
class MyPanel extends JPanel implements Runnable{
	private static final int FPS = 60;			// Frames Per Second.
	private long time;							// A timer to draw every frame.
	public static int playerScore = 0;			// the score of the player(increases by killing an enemy and decreases by taking a hit).
	private static boolean gameRunning = true;	// indicates that the game is Running (i.e the player is alive or the game didn't start).			
	private static boolean mainMenu = false;	// indicates that the game is in Main Menu.
	public static boolean gamePlaying = true;	// a  boolean that indicates that the game is playing (i.e the player is alive).
	public static boolean scoreMenu = false;	// indicates that the game is over and it is time to show the score board.
	private BufferedImage background ;			// the background image.
	private BufferedImage healthBar;			// the full health bar image.
	private BufferedImage healthPoint;			// A health point in the bar.
	private GameEngine engine = new GameEngine();			// The Game Engine object that handles the whole game.
	private Controller controller = new Controller();		// The Controller object that handles the user key input.
	
	public MyPanel(){
	}	
	
	
	/**
	 * The starting method for the Panel. In this method, the following is done:
	 * Initializing the background.
	 * adding the PlayerShip and the Enemies to the game engine.
	 * modifying the mouse cursor to a custom crosshair cursor.
	 * adding a mouseListener, a MouseAdapter and a KeyListner to process the different user Inputs.
	 */
	public void init(){
		
		//getting the background image
		try{
			background = ImageIO.read(new File("res/background.png"));
			healthBar = ImageIO.read(new File("res/healthBar.png"));
			healthPoint = ImageIO.read(new File("res/healthPoint.png"));
		}
		catch(IOException e){
			System.out.println("problem in reading the images");
		}
		
		
		engine.addGameObject(new PlayerShip(480, 360, 0));		// adding the player to the game world
		// adding 2 Shooter and 3 Follower enemies at random spawn positions.
		engine.addGameObject(new EnemyShipShooter((int)(Math.random() * AlienSlayer.WIDTH), (int)(Math.random() * AlienSlayer.HEIGHT)));
		engine.addGameObject(new EnemyShipShooter((int)(Math.random() * AlienSlayer.WIDTH), (int)(Math.random() * AlienSlayer.HEIGHT)));
		engine.addGameObject(new EnemyShipFollower((int)(Math.random() * AlienSlayer.WIDTH), (int)(Math.random() * AlienSlayer.HEIGHT)));
		engine.addGameObject(new EnemyShipFollower((int)(Math.random() * AlienSlayer.WIDTH), (int)(Math.random() * AlienSlayer.HEIGHT)));
		engine.addGameObject(new EnemyShipFollower((int)(Math.random() * AlienSlayer.WIDTH), (int)(Math.random() * AlienSlayer.HEIGHT)));
		
		// setting a custom mouse cursor
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor crosshair = null;
		try {
			crosshair = toolkit.createCustomCursor(ImageIO.read(new File("res/crosshair.png")), new Point(0,0), "crosshair");
		} catch (IOException e1) {}
		this.setCursor(crosshair);
				
		
		// Adding a mouseMotionListener to rotate the playerShip with the movement of the mouse courser
		this.addMouseMotionListener(new MouseMotionListener(){
			
			
			public void mouseMoved(MouseEvent e){
				// the horizontal and vertical distances between the mouse courser and the playerShip		
				int xDistance = (e.getX() - engine.getPlayer().getX()); 			
				int yDistance = (e.getY() - engine.getPlayer().getY());				
				
				double theta = Math.atan(xDistance * 1.0 / yDistance);	// the angle of rotation of the playerShip

				// correcting some miscalculations
				if(xDistance <= 0 && yDistance >= 0)
					theta = Math.atan(xDistance * 1.0 / yDistance) - Math.PI;
				else if(xDistance >= 0 && yDistance >= 0)
						theta = Math.atan(xDistance * 1.0 / yDistance) + Math.PI;
				
				// modifying the rotation angle for the playerShip.
				engine.getPlayer().setRotationAngle(-theta);
				
			}
			
			public void mouseDragged(MouseEvent e){
			}
			
		});
		
		
		
		 // Adding a MouseAdapter to the panel to process the user mouse click and play the player shooting sound
		this.addMouseListener(new MouseAdapter(){
			
			// Adding a PlayerBullet directed to the mouse cursor and playing the player shooting sound
			public void mousePressed(MouseEvent e) {
				if (gamePlaying) {
					engine.addPlayerBullet(e);
					playSound("res/playerShoot.wav");
				}
			}
			
		});
		
		
		// Adding a keyListener to the panel to process the user key input
		this.addKeyListener(new KeyListener() {

			// changes the values of the boolean fields of the controller
			// whenever a controlling key is pressed
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_W)
					controller.setwPressed(true);

				else if (e.getKeyCode() == KeyEvent.VK_A)
					controller.setaPressed(true);

				else if (e.getKeyCode() == KeyEvent.VK_S)
					controller.setsPressed(true);

				else if (e.getKeyCode() == KeyEvent.VK_D)
					controller.setdPressed(true);
			}

			// changes the controller field back to false when no keys are
			// pressed
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_W)
					controller.setwPressed(false);

				else if (e.getKeyCode() == KeyEvent.VK_A)
					controller.setaPressed(false);

				else if (e.getKeyCode() == KeyEvent.VK_S)
					controller.setsPressed(false);

				else if (e.getKeyCode() == KeyEvent.VK_D)
					controller.setdPressed(false);
			}

			// left empty as the keyPressed method is used.
			public void keyTyped(KeyEvent e) {

			}

		});

	}

	/**
	 * The looping method. In this method, the following is done every game frame:
	 * the key inputs are processed to move the player ship.
	 * the Shooter enemies are updated to check if it is time to shoot
	 * the game engine updates/moves all game objects.
	 * the collisions between objects are processed.
	 * repainting.
	 */
	public void run(){
		time = System.nanoTime();	
		
		while(true){
			// processing the game every frame if we are in the playing phase
			if((System.nanoTime() - time)/1000000 >= (1000/FPS)){
				
				if (gamePlaying) {				// we process the game if we are in the playing phase
					
					updateEnemyShooters();
					
					processKeyInput();
					
					engine.processCollisions();
					
					engine.update();
					
					repaint();
				}
				
				time = System.nanoTime();				//resetting the timer back.
				
			}
						
		}
		
		
	}
	
	/**
	 *	Draws all the game components, including background, enemies and friendlies.
	 *	it also draws the player health bar and score. 
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		
		g2d.drawImage(background, 0,0, null);			//drawing the background
		
		
		if(gameRunning){
			
			if(mainMenu){
				
			}
			else if(gamePlaying){
				
				//drawing the full health bar
				g2d.drawImage(healthBar, 10, AlienSlayer.HEIGHT-80, null);
				
				//drawing the player health
				if (engine.getPlayer() != null) {
					
					g2d.setColor(new Color(11, 164, 230));
					g2d.setFont(new Font("Times", 20, 20));
					g2d.drawString("HEALTH", 10, AlienSlayer.HEIGHT - 110);
					
					for (int i = 0; i < engine.getPlayer().getHealth(); i++) {
						g2d.drawImage(healthPoint, 10 + (i * 50),
								AlienSlayer.HEIGHT - 80, null);
					}
				}
				
				// drawing the player Score (0 if it is negative)
				if(playerScore > 0)
					g2d.drawString("SCORE: " + playerScore, AlienSlayer.WIDTH - 150, AlienSlayer.HEIGHT - 50);
				else
					g2d.drawString("SCORE: " + 0, AlienSlayer.WIDTH - 150, AlienSlayer.HEIGHT - 50);
				
				// drawing all game objects 
				for(AnyGroup ag: engine.getGameObjects())
					ag.draw(g2d);
				
			}
			
			// if we are in the score menu state, i.e, either the player died or won.
			else if(scoreMenu){
				
				g2d.setFont(new Font("Times", 50, 50));
				g2d.setColor(new Color(11, 164, 230));
				if(playerScore > 0 && engine.getPlayer().isAlive()){				// if the player won.	
					g2d.drawString("You Win" , AlienSlayer.WIDTH/2 - 120, AlienSlayer.HEIGHT/2 - 100);
					g2d.setColor(new Color(128, 219, 15));
					g2d.drawString("YOUR SCORE IS:   " + playerScore , AlienSlayer.WIDTH/2 - 250, AlienSlayer.HEIGHT/2 );
				}
				else
					g2d.drawString("YOU LOST" , AlienSlayer.WIDTH/2 - 120, AlienSlayer.HEIGHT/2 - 100);
				
			}
			
		}
	}
	
	
	public void processKeyInput(){
		if (controller.isaPressed()) {
			engine.getPlayer().setX(engine.getPlayer().getX() - PlayerShip.SPEED);
		}
		if (controller.isdPressed()) {
			engine.getPlayer().setX(engine.getPlayer().getX() + PlayerShip.SPEED);
		}
		if (controller.iswPressed()) {
			engine.getPlayer().setY(engine.getPlayer().getY() - PlayerShip.SPEED);
		}
		if (controller.issPressed()) {
			engine.getPlayer().setY(engine.getPlayer().getY() + PlayerShip.SPEED);
		}
	}
	
	/**
	 * updates the EnemyShipShooter objects by incrementing the frameCounter of which by one 
	 * and  if it is the time to shoot after a certain amount of time depending on the game FPS,
	 * the enemy will shoot and the enemy shooting sound is played.
	 * 
	 */
	public void updateEnemyShooters(){
		
		for(AnyGroup gObject: engine.getGameObjects()){
			if(gObject instanceof EnemyShipShooter){
				EnemyShipShooter shooter = (EnemyShipShooter) gObject;
				shooter.setFrameCounter(shooter.getFrameCounter() + 1);
				if(shooter.getFrameCounter() ==  (int) (EnemyShipShooter.TIMER * FPS)){
					shooter.setTimeToShoot(true);
					playSound("res/enemyShoot.wav");
				}
			}
		}
		
	}
	
	/**
	 *  This local method is used to play the game sounds.
	 *  it takes the name of the file in the res folder 
	 *  and starts a new thread to play it.
	 * 
	 * @param str	the name of the file to be played
	 */
	protected static void playSound(final String str){
		Thread t = new Thread(new Runnable(){
			public void run(){
				AudioInputStream aStream = null;
				Clip clip = null;
				try {
					aStream = AudioSystem.getAudioInputStream(new File(str));
					clip = AudioSystem.getClip();
					clip.open(aStream);
				} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {}
				clip.start();
			}
		});
		
		t.run();
	}

	
}