
/**
 * A class that represents both enemy and friendly bullets.
 * The bullets will vanish when they go outside the game borders.
 * 
 * @author Mossad Helali.
 * @version 1.0.
 *
 */
public abstract class Bullet extends GameObject {

	public static final int SPEED = 6; // the normalized speed of every bullet
	public static int HEALTH = 1;		// the initial health for a bullet is 1

	public Bullet(int x, int y, double rotationAngle) {

		super(x, y, HEALTH, rotationAngle); 

	}

	// overriding the GameObject.move() method so the bullets are removed if
	// they went outside the game border (with some margins).
	public void move() {
		super.move();
		if (this.getX() < -this.getImgWidth()
				|| this.getX() > AlienSlayer.WIDTH + this.getImgWidth()
				|| this.getY() < -this.getImgHeight()
				|| this.getY() > AlienSlayer.HEIGHT + this.getImgHeight()) {
			this.setAlive(false);
		}
	}

}
