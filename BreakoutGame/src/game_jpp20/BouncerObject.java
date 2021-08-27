// Taken mostly from Bouncer.java by Robert Duvall found at https://coursework.cs.duke.edu/CompSci308_2017Fall/lab_bounce
package game_jpp20;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Simple bouncer based on an image that moves and bounces.
 * 
 * @author Robert C. Duvall (base code)
 * @author sudhir swain
 */

public class BouncerObject {
	public static final double SPEED_DAMPNER = .9;
	public static final double SPEED_MULTIPLIER = 1.1;
	public static final int BOUNCER_SLOW_SPEED = 50;
    public static final int BOUNCER_MEDIUM_SPEED = 200;
    public static final int BOUNCER_FAST_SPEED = 325;
    public static final int BOUNCER_SIZE = 15;
	private static final double BOUNCE_OFFSET = 10;
    
    private boolean lifeLost = false;
    private ImageView myView;
    private Point2D myVelocity;
    private double unplayableAreaPercentage;
    private double startingX;
    private double startingY;
    private Point2D previousVelocity;
    private boolean paused;
    private boolean hitTop;
    private boolean isMultiplayer;



    /**
     *  creates a bouncer
     */
    public BouncerObject (Image img, int xPosition, int yPosition, double topScoreCoverage, boolean multiplayerGame) {
        myView = new ImageView(img);
        // set height and width
        myView.setFitWidth(BOUNCER_SIZE);
        myView.setFitHeight(BOUNCER_SIZE);
        isMultiplayer = multiplayerGame;
        
       startingX = xPosition;
       startingY = yPosition;
       setInitialPosition();
          
        unplayableAreaPercentage = topScoreCoverage;
    }
 
    /**
     * Move by taking one step based on its velocity.
     * 
     * Note, elapsedTime is used to ensure consistent speed across different machines.
     */

    public void move (double elapsedTime) {
        myView.setX(myView.getX() + myVelocity.getX() * elapsedTime);
        myView.setY(myView.getY() + myVelocity.getY() * elapsedTime);
    }

    /**
     * Resets the bouncer to its initial position
     */

    public void setInitialPosition() {
    		
        myView.setX(startingX);
        myView.setY(startingY);
        lifeLost = false;
        hitTop = false;
        myVelocity = new Point2D( 50, BOUNCER_MEDIUM_SPEED);
        togglePause();
    }
    
    /**
     * Allows for either the increasing and decreasing of the bouncer speed
     */

    public void updateSpeed(String speedSelect) {
    		double myXHeading = myVelocity.getX();
    		int multiplier = myVelocity.getY() > 0 ? 1 :-1;
    		if (speedSelect.equals("fast")) {
    	 		
    	 		myVelocity = new Point2D(myXHeading, multiplier * BOUNCER_FAST_SPEED);
    	 	}
    	 	else if ( speedSelect.equals("slow")) {
    	 		myVelocity = new Point2D(myXHeading, multiplier * BOUNCER_SLOW_SPEED);
    	 	}
    	 	else {
    	 		myVelocity = new Point2D(myXHeading, multiplier * BOUNCER_MEDIUM_SPEED);
    	 	}
    	 		
    		
    }

    /**
     * Bounce off the walls represented by the edges of the screen.
     */
    public void wallBounce (double screenWidth, double screenHeight) {
        if (myView.getX() < 0 || myView.getX() > screenWidth - myView.getBoundsInLocal().getWidth()) {
        		
            myVelocity = new Point2D(-myVelocity.getX(), myVelocity.getY());
        }
        if ( myView.getY() < screenHeight * unplayableAreaPercentage ) {
            if (!isMultiplayer) {
            		bounce();
            }
            else {
            		hitTop = true;
            		lifeLost = true;
            		myVelocity = new Point2D(0,0);
            }
            
        }
        if (myView.getY() > screenHeight - myView.getBoundsInLocal().getHeight()  ) {
        		lifeLost = true;
        		myVelocity = new Point2D(0,0);
        }
    }
        
    /**
     * Bounces the bouncer of a surface
     */

    public void bounce () {
        myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());        
    }
    
    public void bounce (boolean  bottomPaddle) {
    		bounce();
    		if(bottomPaddle) {
    			myView.setY(myView.getY()-BOUNCE_OFFSET);
    		}
    		else {
    			myView.setY(myView.getY()+BOUNCE_OFFSET);
    		}
        
    }
    
    /**
     * Increases bouncer speed
     */

    public void increaseSpeed() {
		myVelocity = new Point2D(myVelocity.getX(), SPEED_MULTIPLIER*myVelocity.getY());
    }
    
    /**
     *Slows down the bouncer
     */

    public void reduceSpeed() {
    		System.out.println(myVelocity.getY());
    		if ( myVelocity.getY()*.9 > 50) {
        		myVelocity = new Point2D(myVelocity.getX(), myVelocity.getY()*SPEED_DAMPNER);		
    		}
    }
    /**
     * Freezes and unfreezes the bouncer from its current position
     */

    public void togglePause() {
    		if (!paused ) {
    			previousVelocity = myVelocity;
        		myVelocity = new Point2D(0,0);
        		paused = true;
    		}
    		else {
    			resume();
    			paused = false;
    		}
    		
    }
    
    private void resume() {
    		myVelocity = previousVelocity;
    }
    
    public Node getView () {
        return myView;
    }
    
    public boolean getLifeLost () {
        return lifeLost;
    }

	public boolean getHitTop() {
		return hitTop;
	}
}
