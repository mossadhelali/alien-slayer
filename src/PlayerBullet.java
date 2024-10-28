import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A class representing The player bullets which is a type of Bullet.
 * PlayerBullet is of GroupA which is the friendlies group.
 * 
 * @author MoSeMoS
 *
 */
public class PlayerBullet extends Bullet implements GroupA{

	
public PlayerBullet(int x, int y, double rotationAngle) {
		
		super(x, y, rotationAngle);		
		
		try{
			super.setImg(ImageIO.read(new File("res/playerBullet.png")));			// setting the image of the player bullets.
		}	
		catch(IOException e){
			System.out.println("Problem in loading the playerBullet image");
		}
		
		setImgWidth(getImg().getWidth());
		setImgHeight(getImg().getHeight());
	
	}
	
}
