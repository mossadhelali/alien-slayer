
import java.awt.*;

/**
 * An Interface wrapping all types of game objects for easy referencing.
 * 
 * @author Mossad Helali
 * @version 1.0
 *
 */
public interface AnyGroup {
	
	public void move();
	public boolean isAlive();
	public Rectangle getBounds();
	public void draw(Graphics2D g);
}

/**
 * An Interface representing friendly game objects namely, the player ship and the player bullets.
 * 
 * @author Mossad Helali
 * @version 1.0
 *
 */
interface GroupA extends AnyGroup{
	
	
}

/**
 * An Interface representing enemy game objects namely, Shooter and follower enemies and enemy bullets.
 * it has a method to process the collision between objects of different groups.
 * 
 * @author Mossad Helali
 * @version 1.0
 *
 */
interface GroupB extends AnyGroup{
	
	public void processCollision(GroupA gao);
	
}