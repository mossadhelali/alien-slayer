import java.io.*;
import javax.imageio.*;

/**
 * A class representing the player ship which is a GameObject in GroupA (friendly).
 * every PlayerShip object has its constant speed and a Health of 3.
 * If the player ship crossed the game border, it will appear in the opposite side of the game.
 * 
 * @author Mossad Helali
 * @version 1.0
 *
 */
public class PlayerShip extends GameObject implements GroupA {

	public static final int SPEED = 6;
	public static int HEALTH = 3;		// the initial health for a PlayerShip object

	public PlayerShip(int x, int y, double rotationAngle) {

		super(x, y, HEALTH, rotationAngle); 

		try {
			super.setImg(ImageIO.read(new File("res/spaceship.png")));			// setting the image of the player ship.
		} catch (IOException e) {
			System.out.println("Problem in loading the spaceship image");
		}

		setImgWidth(getImg().getWidth());
		setImgHeight(getImg().getHeight());

	}

	// overriding the GameObject.move() method so the playerShip appears in the
	// opposite side whenever it gets out of border (with some margins)
	public void move() {
		super.move();
		if (this.getX() > AlienSlayer.WIDTH + this.getImgWidth()) {
			setX(0);
		} else if (this.getX() < -this.getImgWidth()) {
			setX(AlienSlayer.WIDTH);
		} else if (this.getY() > AlienSlayer.HEIGHT + this.getImgHeight()) {
			setY(0);
		} else if (this.getY() < -this.getImgHeight()) {
			setY(AlienSlayer.HEIGHT);
		}
	}

}