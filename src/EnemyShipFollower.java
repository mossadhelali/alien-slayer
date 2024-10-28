import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class represents the Follower type of EnemyShip. 
 * A follower enemy has a constant speed and it calculates the direction
 * to the player ship and rotates to follow it.
 * 
 * @author Mossad Helali.
 * @version 1.0.
 *
 */
public class EnemyShipFollower extends EnemyShip{

	public static final int SPEED = 4; 					// the normalized speed of the follower enemy
	
	public EnemyShipFollower(int x, int y) {
			
			super(x, y);					
			
			try{
				super.setImg(ImageIO.read(new File("res/followerEnemy.png")));		// setting the image of the follower enemy.
			}	
			catch(IOException e){
				System.out.println("Problem in loading the followerEnemy image");
			}
			
			setImgWidth(getImg().getWidth());
			setImgHeight(getImg().getHeight());
		
		}
	
	
	/**
	 * This method adjusts the follower velocity to follow the position of the given player.
	 * 
	 * @param player a PlayerShip object to follow
	 */
	public void follow(PlayerShip player){

		// the horizontal and vertical distances between the player ship and the follower enemy
		int xDistance = (player.getX() - this.getX());
		int yDistance = (player.getY() - this.getY());

		double theta = Math.atan(xDistance * 1.0 / yDistance);	// the angle of rotation of the bullet

		// correcting some miscalculations
		if(xDistance <= 0 && yDistance >= 0)
			theta = Math.atan(xDistance * 1.0 / yDistance) - Math.PI;
		else if(xDistance >= 0 && yDistance >= 0)
				theta = Math.atan(xDistance * 1.0 / yDistance) + Math.PI;
		
		this.setRotationAngle(-theta);		
		
		// normalized velocities to allow for consistent speeds regardless of direction.
		int vX = (int) (EnemyShipFollower.SPEED * xDistance / Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));
		int vY = (int) (EnemyShipFollower.SPEED * yDistance / Math.sqrt((xDistance * xDistance) + (yDistance * yDistance)));
		
		// adjusting the x and y velocities 
		this.setvX(vX);
		this.setvY(vY);
		
	}
	

}
