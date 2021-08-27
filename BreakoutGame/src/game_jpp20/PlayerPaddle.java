package game_jpp20;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Simple paddle based on an image that moves and can change sizes
 * 
 * @author sudhir swain
 */
public class PlayerPaddle {
    public static final int PADDLE_MIN_SIZE = 25;
    public static final int PADDLE_MAX_SIZE = 50;
    public static final double MOVE_OFFSET = 15;

    // private variables
    private ImageView myView;
    private int myPaddleWidth;
    private int myScreenWidth;
    private boolean isSuper;
    private boolean isTiny;

    /**
     * Create a paddle with random x position.
     */
    public PlayerPaddle (Image image, int screenWidth, int paddleWidth, int paddleYPosition, int paddleHeight) {
        myView = new ImageView(image);
        myView.setFitWidth(paddleWidth);
        myView.setFitHeight(paddleHeight);

        myView.setX(((int)(Math.random()*(screenWidth / MOVE_OFFSET))) * MOVE_OFFSET);
        myView.setY(paddleYPosition);
       
        myPaddleWidth = paddleWidth;
        myScreenWidth = screenWidth;

      
    }
    /**
     * Moves the paddle right
     */
    public void moveRight () {
    		double newPosition = myView.getX() + MOVE_OFFSET;
    		if (newPosition <= myScreenWidth - myPaddleWidth) {
    			myView.setX(newPosition);
    		}  	
    }
    
    /**
     * Changes the size of the paddle
     */
    public void updateSize(String sizeSelect) {
		if (sizeSelect.equals("big")) {
	 		myView.setFitWidth(myPaddleWidth*2);
	 	}
	 	else if ( sizeSelect.equals("small")) {
	 		myView.setFitWidth(myPaddleWidth/2);
	 	}
	 	else {
	 		myView.setFitWidth(myPaddleWidth);
	 	}
    }
    
    /**
     * Moves the paddle left.
     */
    public void moveLeft () {
    		double newPosition = myView.getX() - MOVE_OFFSET;
    		if (newPosition >= 0 ) { 
    			myView.setX(newPosition);
    		}	
    }

    /**
     * Returns internal view of paddle to interact with other JavaFX methods.
     */
    public Node getView () {
        return myView;
    }

    /**
     * Converts the paddle to a super bouncer paddle
     */
	public void makeSuperBounce() {
		isSuper = true;
		isTiny = false;
	}
	/**
     * returns whether the bouncer is a super bouncer
     */
	public boolean isSuper(){
		return isSuper;
	}

	/**
     * Converts the paddle to a tiny bouncer paddle
     */
	public void makeTinyBounce() {
		isTiny = true;
		isSuper = false;
	}
	
	/**
     * Returns whether the paddle is a tiny paddle
     */
	public boolean isTiny() {
		return isTiny;
	}
	
	/**
     * Converts the paddle back to a normal paddle
     */
	public void makeNormal() {
		isTiny = false;
		isSuper = false;
	}

	
    
}
