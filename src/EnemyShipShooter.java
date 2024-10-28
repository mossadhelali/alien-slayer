import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A class representing the Shooter enemies. every shooter enemy shoots a bullet
 * after a certain amount of time. The class has a frameCounter that is
 * incremented after every game frame to indicate if the shooter has to shoot.
 * 
 * @author Mossad Helali
 * @version 1.0
 * 
 *
 */
public class EnemyShipShooter extends EnemyShip{
	public static double TIMER = 1.5;				// a timer to determine when the enemy has to shoot.
	private boolean timeToShoot = false;			// determines if the shooter enemy has to shoot at a specific time
	private int frameCounter;
		
	public EnemyShipShooter(int x, int y) {
			
			super(x, y);							
			
			try{
				super.setImg(ImageIO.read(new File("res/shooterEnemy.png")));
			}	
			catch(IOException e){
				System.out.println("Problem in loading the shooterEnemy image");
			}
			
			setImgWidth(getImg().getWidth());
			setImgHeight(getImg().getHeight());
			
			frameCounter = 0;
		
		}
			
	public boolean timeToShoot(){
		return timeToShoot;
	}

	public void setTimeToShoot(boolean flag){
		timeToShoot = flag;
	}
	
	public int getFrameCounter() {
		return frameCounter;
	}

	public void setFrameCounter(int frameCounter) {
		this.frameCounter = frameCounter;
	}

	
}
