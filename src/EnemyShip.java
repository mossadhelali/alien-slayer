/**
 * A class representing an Enemy Ship which is a GroupB object.
 * The health of an enemy is set to 1.
 * A collision between an EnemyShip object and a GroupA object (friendlies)
 * will cause the vanishing of the enemy ship and a decrement by one of the
 * colliding GroupA object's health.
 * 
 * @author Mossad Helali.
 * @version 1.0.
 *
 */
public abstract class EnemyShip extends GameObject implements GroupB {

	public static int HEALTH = 1;		// the initial health for enemies is 1
	
	public EnemyShip(int x, int y) {

		super(x, y, HEALTH, 0); // the enemies are initially at rotation angle zero.
	}

	/**
	 * handles the collision between a GroupB (enemy) object and a GroupA
	 * (friendly) object. Any collision between two objects will result in a
	 * decrement by one health point to both objects. A dead object is an object
	 * whose health is zero.
	 * 
	 * @param gao AnyGroup object whether it is friendly or enemy.
	 */
	public void processCollision(GroupA gao) {

		GameObject obj = (GameObject) gao;

		this.setHealth(this.getHealth() - 1);
		if (this.getHealth() == 0)
			this.setAlive(false);

		obj.setHealth(obj.getHealth() - 1);
		if (obj.getHealth() == 0)
			obj.setAlive(false);
	}

}
