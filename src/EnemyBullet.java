import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class represents the enemy bullets which are of GroupB.
 * A collision between a bullet and an Object from GroupA (friendlies) will 
 * cause the bullet to vanish and a decrement by one of the other GroupA object.
 * 
 * @author Mossad Helali.
 * @version 1.0.
 *
 */
public class EnemyBullet extends Bullet implements GroupB {

	
	public EnemyBullet(int x, int y, double rotationAngle) {
			
			super(x, y, rotationAngle);		
			
			try{
				super.setImg(ImageIO.read(new File("res/enemyBullet.png")));		//setting the image of the enemy bullet.
			}	
			catch(IOException e){
				System.out.println("Problem in loading the enemyBullet image");
			}
			
			setImgWidth(getImg().getWidth());
			setImgHeight(getImg().getHeight());
		
		}
	
	/**
	 * handles the collision between a GroupB (enemy) object and a GroupA (friendly) object.
	 * Any collision between two objects will result in a decrement by one health point to both objects.
	 * A dead object is an object whose health is zero.
	 * 
	 * @param gao AnyGroup object whether it is friendly or enemy.
	 */
	public void processCollision(GroupA gao){
		
		GameObject obj = (GameObject) gao;
		
		this.setHealth(this.getHealth() -1);
		if(this.getHealth() == 0)
			this.setAlive(false);
		
		obj.setHealth(obj.getHealth() -1);
		if(obj.getHealth() == 0)
			obj.setAlive(false);
		
	}
	
}
