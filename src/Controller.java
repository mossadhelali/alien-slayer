
/**
 * This Class is used to handle the keyboard controlling of the player position
 * The pressed keys are stored so the handling is easy
 * 
 * @author Mossad Helali
 * 
 */

public class Controller  {

	private boolean wPressed;						// Every Controlling key (W, A, S, D) has a boolean variable
	private boolean aPressed;						// so the controls handling is easier
	private boolean sPressed;
	private boolean dPressed;
	
	public Controller(){
		wPressed = false;
		aPressed = false;
		sPressed = false;
		dPressed = false;
	}

	
	public boolean iswPressed() {
		return wPressed;
	}

	public void setwPressed(boolean wPressed) {
		this.wPressed = wPressed;
	}

	public boolean isaPressed() {
		return aPressed;
	}

	public void setaPressed(boolean aPressed) {
		this.aPressed = aPressed;
	}

	public boolean issPressed() {
		return sPressed;
	}

	public void setsPressed(boolean sPressed) {
		this.sPressed = sPressed;
	}

	public boolean isdPressed() {
		return dPressed;
	}

	public void setdPressed(boolean dPressed) {
		this.dPressed = dPressed;
	}
	
	
	
}
