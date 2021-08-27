package game_jpp20;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block {
	private ImageView myImageView;
	private int livesRemaining;
	private Group parent;
    private boolean removed = false;

    /**
     * Simple block based on a stationary image that looks different given the amount of lives it has left
     * 
     * @author sudhir swain
     */
	public Block( int xPos, int yPos, int startingLives, Group root, int blockWidth, int blockHeight) {
		livesRemaining = startingLives;
		Image blockImage = getCurrentImage();
		myImageView = new ImageView(blockImage);
        myImageView.setFitHeight(blockHeight);
        myImageView.setFitWidth(blockWidth);

        myImageView.setX(xPos);
        myImageView.setY(yPos);
        
        root.getChildren().add(myImageView);

        
        livesRemaining = startingLives;
        parent = root;
	}
	
	private Image getCurrentImage() {
		Image returnImage;
		switch( livesRemaining) {
			case 1:	
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick1.png"));
				break;
					
			case 2: 
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick2.png"));
				break;
			
			case 3:	
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick3.png"));
				break;
					
			case 4: 
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick4.png"));
				break;
				
			case 5:	
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick5.png"));
				break;
					
			case 6: 
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick6.png"));
				break;
				
			case 7:	
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick7.png"));
				break;
					
			case 8: 
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick8.png"));
				break;
			case 9: 
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick9.png"));
				break;
			case 10: 
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick10.png"));
				break;
				
			default: 
				returnImage = new Image(getClass().getClassLoader().getResourceAsStream("brick1.png"));
				break;	
		}
		return returnImage;
	}

	/**
	 * Returns the image view for the block
	 */
	public ImageView getView() {
		return myImageView;
	}
	
	/**
	 * Reduces the amount of lives the block has by 1
	 */
	public void loseLife() {
		livesRemaining--;
		if (livesRemaining <= 0) {
			this.removeBlock();
		}
		myImageView.setImage(getCurrentImage()); 
	}
	
	/**
	 * Removes the block from the scene
	 */
	public void removeBlock() {
		parent.getChildren().remove(myImageView);
		removed = true;
	}
	
	/**
	 * Returns whether or not the block has been removed from the scene
	 */
	public boolean getRemoved() {
		return removed;
	}
	

}
