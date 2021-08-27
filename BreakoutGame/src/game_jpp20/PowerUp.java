package game_jpp20;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Image which falls from a broken block
 */
public class PowerUp {
    private ImageView myView;
	private boolean offScreen;
	private double bottomOfScreen;
	private Group parent;
	
	public PowerUp(ImageView powerupView, double xStart,double yStart, double screenHeight, Group root, int powerUpSize) {
		
		myView = powerupView;
        myView.setFitWidth(powerUpSize);
        myView.setFitHeight(powerUpSize);

		myView.setX(xStart);
		myView.setY(yStart);
	
		bottomOfScreen = screenHeight;
		parent = root;
        root.getChildren().add(myView);

    }
		
	/**
     * Returns the imageview of the power up
     */
	public ImageView getView() {
		return myView;
	}
	
	/**
     * Moves power up towards bottom of screen
     */
	public void fall() {
		myView.setY(myView.getY()+2.5);
		if (myView.getY() > bottomOfScreen) {
			offScreen = true;
			remove();
		}
	}
	
	/**
     * Removes powerup from the scene
     */
	public void remove() {
		parent.getChildren().remove(myView);
	}
	
	/**
     * Returns whether or not the paddle is off the screen
     */
	public boolean getOffScreen(){
		return offScreen;
	}
	
}
