import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

/**
 * A class that encapsulates what every game object has namely: 
 * position, velocity, health, life status, image, rotation angle, width and height.
 * 
 * @author Mossad Helali
 *
 */

public abstract class GameObject implements AnyGroup  {

	private int x;
	private int y;
	private float vX;									// the velocity of the object on the x coordinate
	private float vY;									// the velocity of the object on the y coordinate
	private boolean alive;
	private int health;
	private BufferedImage img;						// the image of the object, it is declared once in every object constructor
	private int imgWidth;
	private int imgHeight;
	private double rotationAngle;					// the angle used to draw a rotated version of the object;			
	

	// the constructor leaves the declaration of the object image and its width and height to subclasses
	public GameObject(int x, int y, int health, double rotationAngle){
		this.x = x;
		this.y = y;
		this.health = health;
		this.rotationAngle = rotationAngle;
		this.vX = 0;										// initializing the velocities to 0 as object are constant when they are created
		this.vY = 0;
		this.alive = true;									// any new Object is alive by default
	}
	
	public GameObject(){
		
	}
	
	
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	
	public void setY(int y) {
		this.y = y;
	}
	
	public float getvX() {
		return vX;
	}
	
	public void setvX(float vX) {
		this.vX = vX;
	}
	
	public float getvY() {
		return vY;
	}
	
	public void setvY(float vY) {
		this.vY = vY;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public double getRotationAngle() {
		return rotationAngle;
	}
	
	public void setRotationAngle(double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}
	
	public BufferedImage getImg() {
		return img;
	}

	
	
	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	
	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}


	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	
	
	/**
	 * A method that moves the object according to its velocity value.
	 */
	public void move(){
		x = (int) (x + vX);
		y = (int) (y + vY);	
	}
	
	
	/**
	 * note: for simplicity, the bounding box will NOT rotate with the object and therefore, 
	 * the collision between objects is not very accurate.
	 *
	 * @return Rectangle the bounding box of the object
	 */
	public Rectangle getBounds(){
		return new Rectangle(x, y, imgWidth, imgHeight);
	}
	
	/**
	 * Draws the final rotated GameObject on a Graphics2D object given as a parameter.
	 * 
	 * @param g A Graphics2D object to be drawn on.
	 */
	
	public void draw(Graphics2D g){
		
		AffineTransform at = new AffineTransform();		// Creating an AffineTransform to rotate the object before drawing.
		at.translate(x, y);								// 3. Translating the Image to the object's coordinates.
		at.rotate(rotationAngle);						// 2. Rotating the image about its center.
		at.translate(-imgWidth/2, -imgHeight/2);		// 1. Translating the image to rotate about the center.
	
		g.drawImage(img, at, null);						// Drawing the final image.
	
	}
}
