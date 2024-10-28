
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Class that handles the management, storage, removing, movement 
 * and collision of our game objects.
 * 
 * For storing the objects, we use LinkedLists which you can just assume
 * at this level behave like arrays. But because we want to delete and add
 * objects easily, LinkedLists are more suited for these operations. 
 * 
 * @author Moayad Alnammi, Mossad helali.
 *
 */

public class GameEngine {
	private LinkedList<AnyGroup> allGameObjects;	//stores the state of all our game objects
	private LinkedList<GroupA> ga;					//stores only the state of GroupA game objects; GoupA are friendlies
	private LinkedList<GroupB> gb;					//stores only the state of GroupB game objects; GroupB are enemies
	
	private LinkedList<Bullet> bullets;				//stores only the bullets to add EACH FRAME. this is done since we
													//might be adding many bullets from different shooters each frame. 
													//(player, timed enemies, etc.)
	
	private PlayerShip player; 						//we keep a reference to the player for easy access

	public GameEngine() {
		ga = new LinkedList<GroupA>();
		gb = new LinkedList<GroupB>();
		allGameObjects = new LinkedList<AnyGroup>();
		bullets = new LinkedList<Bullet>();
	}
	
	/**
	 * 
	 * @return returns a linked list containing all the game objects
	 */
	public LinkedList<AnyGroup> getGameObjects()
	{
		return allGameObjects;
	}
	
	public PlayerShip getPlayer()
	{
		return player;
	}
	
	/**
	 * This method adds a game object to our game.
	 * It is stored in allGameObjects and ga or gb
	 * depending on its Group type. The player is special 
	 * and we keep a reference to it for easy access.
	 * 
	 * @param gObject the game object to be added to our game
	 */
	public void addGameObject(GameObject gObject)
	{
		allGameObjects.add(gObject);
		
		if(gObject instanceof PlayerShip)
		{
			player = (PlayerShip) gObject;
		}
		
		//add the object to its respective group
		if(gObject instanceof GroupA)
			ga.add((GroupA)gObject);
		else if(gObject instanceof GroupB)
			gb.add((GroupB)gObject);
	}

	/**
	 * This method adds a player bullet to our world according
	 * to the location given by paramMouseEvent. You need to finish 
	 * implementing this method given the guidelines below. 
	 * You should work out the actual invocation of this method through 
	 * the use of Mouse Listeners. Or you can just issue this command
	 * each frame by storing the status of the mouse.
	 * 
	 * @param paramMouseEvent
	 */
	public void addPlayerBullet(MouseEvent paramMouseEvent) {
		
		//mx and my give the location of the mouse when the mouse event was triggered
				int mx = paramMouseEvent.getX();
				int my = paramMouseEvent.getY();

		
		
		//calculating the rotation angle for the bullet
			
		// the horizontal and vertical distances between the mouse and the playerShip		
		int xDistance = (mx - player.getX()); 			
		int yDistance = (my - player.getY());			
		
		double theta = Math.atan(xDistance * 1.0 / yDistance);	// the angle of rotation of the bullet

		// correcting miscalculations in case if the bullet should be shot in the third or the forth quadrant
		if(xDistance <= 0 && yDistance >= 0)
			theta = Math.atan(xDistance * 1.0 / yDistance) - Math.PI;
		else if(xDistance >= 0 && yDistance >= 0)
				theta = Math.atan(xDistance * 1.0 / yDistance) + Math.PI;
		
		//creating a bullet in front of the enemy
		// the bullet is initially at the center of the shooter image 
		PlayerBullet bullet = new PlayerBullet(player.getX(), player.getY(), -theta);	
																						
		
		// normalized velocities to allow for consistent speeds regardless of direction.
		
		float vX = (float) (Bullet.SPEED * xDistance / Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));
		float vY = (float) (Bullet.SPEED * yDistance / Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));
		
		bullet.setvX(vX);
		bullet.setvY(vY);
		
		
		//add bullet to the bullet list
		bullets.add(bullet);
		
		
		//moving the bullet few times to start at the tip of the playerShip
		for (int i = 0; i<10; i++)
			bullet.move();
		
	}
	
	
	/**
	 * This method adds an enemy bullet to the shooter ship directed to
	 * the location of the player. 
	 * 
	 * @param shooter the shooter enemy that shoots the bullet
	 */
	private void addEnemyBullet(EnemyShipShooter shooter) {


		// the horizontal and vertical distances between the enemy and the playerShip		
		int xDistance = (player.getX() - shooter.getX()); 			
		int yDistance = (player.getY() - shooter.getY());			
		
		
		
		double theta = Math.atan(xDistance * 1.0 / yDistance);	// the angle of rotation of the bullet

		// correcting miscalculations in case if the bullet should be shot in the third or the forth quadrant
		if(xDistance <= 0 && yDistance >= 0)
			theta = Math.atan(xDistance * 1.0 / yDistance) - Math.PI;
		else if(xDistance >= 0 && yDistance >= 0)
				theta = Math.atan(xDistance * 1.0 / yDistance) + Math.PI;
		
		//creating a bullet in front of the enemy
		// the bullet is initially at the center of the shooter image 
		EnemyBullet bullet = new EnemyBullet(shooter.getX(), shooter.getY(), -theta);	
																						
		
		
		//normalized velocities to allow for consistent speeds regardless of direction.
		float vX = (float) (Bullet.SPEED * xDistance / Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));
		float vY = (float) (Bullet.SPEED * yDistance / Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));
		
		// setting the horizontal and vertical velocities to the bullet.
		bullet.setvX(vX);
		bullet.setvY(vY);

		
		//add bullet to our game engine
		
		bullets.add(bullet);
		
		//moving the bullet few times to start at the tip of the EnemyShip
		for (int i = 0; i<10; i++)
			bullet.move();
		
		/* you can call a method in the shooter here to indicate that he has shot
		 * you would want to do this if you want the shooter to have a delay of 
		 * 1 second for example. So you would restart the timer every time he shot.
		 */
	}
	
	
	/**
	 * This method will loop on all our game objects and update their locations.
	 * It makes sense to make a move method for each object to do so. Also, if 
	 * it is time for my shooters to shoot, then I should create bullets for them.
	 *
	 *	EDIT: updated the move() call to make the follower enemy move towards the player ship. 
	 */
	public void update()
	{
		
		
		//update/move all objects
		for(AnyGroup gObject : allGameObjects)
		{
			// adjusting the velocity (speed and direction) of every follower enemy 
			if(gObject instanceof EnemyShipFollower){
				EnemyShipFollower follower = (EnemyShipFollower) gObject;
				follower.follow(player);
			}
				
			gObject.move();
			
			if(gObject instanceof EnemyShipShooter)
			{
				EnemyShipShooter shooter = (EnemyShipShooter) gObject;
				if(shooter.timeToShoot())
				{
					addEnemyBullet(shooter);
					shooter.setTimeToShoot(false);
					shooter.setFrameCounter(0);
				}
			}
		}
		
		// remove all dead objects
		removeDeadObjects();
		
		//add bullets to the world and clear the bullet list
		addBullets();
	}
	
	/**
	 * This method processes the collisions between my GroupA objects and GroupB objects.
	 * This means that an object x does not collide with an object y if they are in the same group.
	 * The iterator objects being created are just a way to loop on linkedlists.
	 * EDIT: the method also stops the game if the player died or won (killed all enemies).
	 */
	public void processCollisions()
	{
		//check which group B objects is colliding with AnyGroup object
		
		Iterator<GroupA> gaIter = ga.iterator();
		Iterator<GroupB> gbIter = gb.iterator();

		GroupA gao = null;
		GroupB gbo = null;

		while(gaIter.hasNext())
		{
			gao = gaIter.next();
			
			gbIter = gb.iterator(); //reset iterator
			
			if(!gbIter.hasNext()){
				MyPanel.gamePlaying = false; 		// if there are no objects in the gb list (no enemies), the game stops
				MyPanel.scoreMenu = true;			// the Score Menu appears after the player wins.
				MyPanel.playSound("res/youWin.wav");	// if all enemies are dead, then the player win sounds plays
			}
			while(gbIter.hasNext())
			{
				gbo = gbIter.next();
					if (gao.getBounds().intersects(gbo.getBounds())) {
						
						gbo.processCollision(gao); 		//a collision will result in a decrement of both objects by 1 HP.
						
						if(gao instanceof PlayerShip){
							PlayerShip ps = (PlayerShip) gao;
							MyPanel.playerScore--;						//decrementing the playerScore as the ship was hit.
							MyPanel.playSound("res/playerHit.wav");		//the player hitting sound
							if(!ps.isAlive()){
								MyPanel.gamePlaying = false;	// if the player dies, the game stops.
							}
						}
					}
			}
		}
	}

	
	/**
	 * This method is called at the end of update() to add all the newly stored bullets
	 * each frame. Then erase the bullet list.
	 */
	private void addBullets()
	{
		for(Bullet bullet : bullets)
			addGameObject(bullet);
		
		bullets.clear();
	}
	
	/**
	 * This method is called to remove all dead objects from our world.
	 * As a rudimentary solution, we call this method each frame. However,
	 * in an intensive game world, we would mark these objects as dead (using boolean)
	 * and then perhaps remove them by calling this method in a thread when our list becomes
	 * filled with dead objects to justify the removal overhead.
	 * 
	 * EDIT: added a call to play the dying sounds whenever the player or an enemy dies.
	 * 
	 */

	public void removeDeadObjects()
	{
		Iterator<AnyGroup> iter = allGameObjects.iterator();

		AnyGroup go = null;

		while(iter.hasNext())
		{
			go = iter.next();
			
			if(!go.isAlive())
			{	
				if(go instanceof PlayerShip){
					MyPanel.playSound("res/mouse.wav");		// if the player died, the loosing sound plays.
					MyPanel.scoreMenu = true;			// the Score Menu appears after the player dies.
				}
				else if(go instanceof EnemyShip){
					MyPanel.playSound("res/enemyDie.wav");
					MyPanel.playerScore++;					// incrementing the player score if an enemy Dies.
				}
				
				iter.remove();
				
				//remove the objects from their respective group
				if(go instanceof GroupA)
					ga.remove((GroupA)go);
				else if(go instanceof GroupB)
					gb.remove((GroupB)go);
			}
		}
	}
	
}
