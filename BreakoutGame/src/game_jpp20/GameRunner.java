package game_jpp20;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;


/**
 * GameRunner will built a basic rendition of Breakout in JavaFX. Basic class framework taken from Robert Duvall's lab bounce
 * 
 * @author sudhir swain 
 * @author Robert Duvall
 *
 */
public class GameRunner extends Application {
	private static final double SINGLE_PLAYER_BUTTON_Y_POSITION = .15;
	private static final double SINGLE_PLAYER_BUTTON_X_POSITION = 5.3;
	public static final int POINTS_PER_BREAK = 5;
	public static final double MULTIPLAYER_BLOCK_GENERAT0R_KEY = 2.2;
	private static final double BOUNCER_OFFSET_SINGLEPLAYER = .55;
	public static final double BOUNCER_1_OFFSET = .66;
	public static final double BOUNCER_2_OFFSET = .33;
	public static final String TITLE = "Parker's Breakout Game";
    public static final int WINDOW_WIDTH = 400; 
    public static final int WINDOW_HEIGHT = 600; 
    public static final Paint GAME_BACKGROUND = Color.CORNFLOWERBLUE;
    public static final Paint MENU_BACKGROUND = Color.CORNFLOWERBLUE;    
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final String BALL_IMAGE = "tennisBall.png";
    public static final String PLATFORM_IMAGE = "paddleBlu.png";
    public static final String POWER_UP_IMAGE = "powerUP.png";
    public static final int PADDLE_WIDTH = 55;
    public static final int PADDLE_HEIGHT = 15;
    public static final int PLAYER_ONE_Y_POSITION = (int) (WINDOW_HEIGHT *.92);
    public static final int PLAYER_TWO_Y_POSITION = (int) (WINDOW_HEIGHT * .20);
    public static final double TOP_SCORE_COVERAGE = .15; 
    public static final int BLOCK_WIDTH = 40;
    public static final int BLOCK_HEIGHT = 20;
    public static final int STARTING_LIVES_AMOUNT = 3;
    public static final int POWER_UP_SIZE = 20;
    public static final double POWER_UP_DROP_RATE = .1;
    public static final long POWER_UP_TIMEOUT = 20 * 1000;
    public static final int MAX_LEVEL_AMOUNT = 10;
    public static final int STARTING_LEVEL = 1;
    
    
    // private variables
    private Scene mainMenuScene;
    private Scene multiplayerGameScene;
    private Scene singlePlayerGameScene;
    private Stage myStage;
    private PlayerPaddle paddle1;
    private PlayerPaddle paddle2;
    private List<BouncerObject> currentBouncers = new ArrayList<>();
    private List<Block> currentBlocks = new ArrayList<>();
    private List<PowerUp> currentPowerUps = new ArrayList<>();
    private List<PlayerPaddle> currentPaddles = new ArrayList<>();

    private int currentScore;
    private int currentLevel = STARTING_LEVEL;
    private int currentLives = STARTING_LIVES_AMOUNT;
    private int player2Lives = STARTING_LIVES_AMOUNT;
    private Text scoreDisplay;
    private Text lifeDisplay;
    private Text player2LifeDisplay;
    private Group singlePlayerRoot;
    private Group multiplayerRoot;
    private int scoreMultiplier = 1;
    private Timer powerUpTimer = new Timer();
    private boolean animationFrameInitialized;
    private  Timeline animation;
    private boolean isMultiplayer;
    
    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage s) {
    		myStage = s;   
    		myStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    powerUpTimer.cancel();
                }
            });
    		
        mainMenuScene = setupMainMenuScene(WINDOW_WIDTH, WINDOW_HEIGHT, MENU_BACKGROUND);
        setToMainMenu();
        myStage.setTitle(TITLE);
        myStage.show();  
        
        if (!animationFrameInitialized) {
	        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
	                e -> step(SECOND_DELAY));
	        animation = new Timeline();
			animation.setCycleCount(Timeline.INDEFINITE);
			animation.getKeyFrames().add(frame);
			animationFrameInitialized = true;
		}
    }

    private void setToMainMenu() {
		currentLives = STARTING_LIVES_AMOUNT;
		player2Lives = STARTING_LIVES_AMOUNT;
		currentScore = 0;
		currentLevel = STARTING_LEVEL;
        myStage.setScene(mainMenuScene);
		
	}

	private Scene setupMainMenuScene(int windowWidth, int windowHeight, Paint background) {
		Group root = new Group();
		
		
		Text GamePrompt = new Text(15, 30, "Breakout: Choose single player or multiplayer");
        root.getChildren().add(GamePrompt);
      
        
	    Scene scene = new Scene(root, windowWidth, windowHeight, background); 
		Button singlePlayerButton = new Button("Single Player");
		singlePlayerButton.setOnAction(e -> setToSingleplayer());   
		singlePlayerButton.setLayoutX(windowWidth/SINGLE_PLAYER_BUTTON_X_POSITION);
		singlePlayerButton.setLayoutY(windowHeight*SINGLE_PLAYER_BUTTON_Y_POSITION);
		root.getChildren().add(singlePlayerButton); 
		
		Button multiplayerButton = new Button("Multi Player");
		multiplayerButton.setOnAction(e -> setToMultiplayer());   
		multiplayerButton.setLayoutX(windowWidth/1.5);
		multiplayerButton.setLayoutY(windowHeight*.15);
		root.getChildren().add(multiplayerButton); 
		
		
		Text instructionPrompt = new Text(50, windowHeight*.35, "In single player use the keys A and D to move left and right. You start with threee lives"
				+ "so be careful. The goal is to break as many blocks as you can before losing all you lives by allowing the ball to pass your paddle."
				+ " In multiplayer player one will still use A and D to move, and now player 2 will use both arrow keys to control a paddle on the top"
				+ "the first play to let three balls past their paddle loses!"
				+ " \n \n Press the space bar to pause and unpause the balls.");
		instructionPrompt.setWrappingWidth(windowWidth *.8);
        root.getChildren().add(instructionPrompt);
        
        
	    return scene;
	}

	private void setToMultiplayer() {
		isMultiplayer = true;
		clearGameObjects();
		multiplayerGameScene = setupMultiPlayerScene(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_BACKGROUND);
		myStage.setScene(multiplayerGameScene);
		animation.play();
	}

	private void setToSingleplayer() {
		isMultiplayer = false;
		clearGameObjects();
        singlePlayerGameScene = setupSinglePlayer(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_BACKGROUND);
		myStage.setScene(singlePlayerGameScene);
		animation.play();
	}
	
    private Scene setupSinglePlayer (int width, int height, Paint background) {
        Group root = new Group();
        singlePlayerRoot = root;
        Scene scene = new Scene(root, width, height, background);
        
        addPaddle1(width, root);

        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));
        addBouncer(width/2 ,height*BOUNCER_OFFSET_SINGLEPLAYER,root); 

        generateSinglePlayerBlocks(width, height, root);
        
       
        drawTopLine(width, height,singlePlayerRoot);

        addText(root);
        
        return scene;
    }

	private void addPaddle1(int width, Group root) {
		// Add player 1's paddle to the scene
        Image paddleImage = new Image(getClass().getClassLoader().getResourceAsStream(PLATFORM_IMAGE));
        paddle1 = new PlayerPaddle(paddleImage,width,PADDLE_WIDTH,PLAYER_ONE_Y_POSITION, PADDLE_HEIGHT);
        root.getChildren().add(paddle1.getView());
        currentPaddles.add(paddle1);
	}

	private void generateSinglePlayerBlocks(int width, int height, Group root) {
		for (int i = 1; i <= currentLevel; i++) {
        		for (int j = 0; j < width; j += BLOCK_WIDTH) {
		        	Block block1 = new Block( j , (int) (height* TOP_SCORE_COVERAGE + (i-1) * BLOCK_HEIGHT ),i, root, BLOCK_WIDTH, BLOCK_HEIGHT);
		        currentBlocks.add(block1);
        		}

        }
	}

	private void addText(Group root) {
		// Add level and score and lives to top of scene
        Text levelPrompt = new Text(15, 30, "Current Level:");
        root.getChildren().add(levelPrompt);
        Text levelDisplay = new Text(15 + levelPrompt.layoutBoundsProperty().getValue().getWidth() + 10, 30, Integer.toString(currentLevel)); 
        root.getChildren().add(levelDisplay);
        
        Text lifePrompt = !isMultiplayer ? new Text(200,30 ,"Current Lives:") :new Text(200,30 ,"Player 1 Lives:") ;
        root.getChildren().add(lifePrompt);
        lifeDisplay = new Text(200 + lifePrompt.layoutBoundsProperty().getValue().getWidth() + 10, 30, Integer.toString(currentLives));
        root.getChildren().add(lifeDisplay);
        
        if (isMultiplayer) {
        		Text scorePrompt = new Text(15, 60 ,"Player 2 Lives:");
            root.getChildren().add(scorePrompt);
            player2LifeDisplay = new Text(15 + scorePrompt.layoutBoundsProperty().getValue().getWidth() + 10, 60, Integer.toString(player2Lives));
            root.getChildren().add(player2LifeDisplay);
        }
        else {
        		Text scorePrompt = new Text(15, 60 ,"Current Score:");
            root.getChildren().add(scorePrompt);
            scoreDisplay = new Text(15 + scorePrompt.layoutBoundsProperty().getValue().getWidth() + 10, 60, Integer.toString(currentScore));
            root.getChildren().add(scoreDisplay);
        }
        
        
       
	}
	
    private Scene setupMultiPlayerScene(int windowWidth, int windowHeight, Paint background) {
    	    Group root = new Group();
        multiplayerRoot = root;
        Scene scene = new Scene(multiplayerRoot, windowWidth, windowHeight, background);
        
        addPaddle1(windowWidth, root);  
        addPaddle2(windowWidth);
        
        scene.setOnKeyPressed(e -> handleMultiplayerKey(e.getCode()));
        scene.setOnMouseClicked(e -> handleMouseInput(e.getX(), e.getY()));

        addBouncer(windowWidth/2 ,windowHeight*BOUNCER_2_OFFSET,multiplayerRoot);
        addBouncer(windowWidth/2 ,windowHeight*BOUNCER_1_OFFSET,multiplayerRoot);
        generateMultiPlayerBlocks(windowWidth, windowHeight, multiplayerRoot);    
        drawTopLine(windowWidth, windowHeight,multiplayerRoot);
        addText(root);
        
        resetLives();        
        return scene;
	}

	private void addPaddle2(int windowWidth) {
		Image paddleImage = new Image(getClass().getClassLoader().getResourceAsStream(PLATFORM_IMAGE));
        paddle2 = new PlayerPaddle(paddleImage,windowWidth,PADDLE_WIDTH,PLAYER_TWO_Y_POSITION, PADDLE_HEIGHT);
        multiplayerRoot.getChildren().add(paddle2.getView());
        currentPaddles.add(paddle2);
	}

	private void drawTopLine(int windowWidth, int windowHeight, Group root) {
		Line line1 = new Line(0 , windowHeight * TOP_SCORE_COVERAGE, windowWidth, windowHeight * TOP_SCORE_COVERAGE);
        root.getChildren().add(line1);
	}
	
	private void resetLives() {
		currentLives = STARTING_LIVES_AMOUNT;
	}
	
	private BouncerObject addBouncer(double xPosition, double yPosition, Group root) {
		BouncerObject currentBouncer = new BouncerObject(new Image(getClass().getClassLoader().getResourceAsStream(BALL_IMAGE)),(int) xPosition, (int) (yPosition), TOP_SCORE_COVERAGE, isMultiplayer);
		currentBouncers.add(currentBouncer);
		root.getChildren().add(currentBouncer.getView());
		return currentBouncer;
	}
	
	
	private void generateMultiPlayerBlocks(int windowWidth, int windowHeight, Group root) {
        for (int i = currentLevel; i > 0; i--) {
        		for (int j = 0; j < windowWidth; j += BLOCK_WIDTH) {
		        	Block block1 = new Block( j , (int) (windowHeight* TOP_SCORE_COVERAGE + (i-1) * BLOCK_HEIGHT  + (windowHeight * (1-TOP_SCORE_COVERAGE)) / MULTIPLAYER_BLOCK_GENERAT0R_KEY),i, root, BLOCK_WIDTH, BLOCK_HEIGHT);
		        currentBlocks.add(block1);
        		}
        		for (int j = 0; j < windowWidth; j += BLOCK_WIDTH) {
        			if (i!= 1) { // dont double spawn in middle
        			 	Block block1 = new Block( j , (int) (windowHeight* TOP_SCORE_COVERAGE - (i-1) * BLOCK_HEIGHT  + (windowHeight * (1-TOP_SCORE_COVERAGE)) / MULTIPLAYER_BLOCK_GENERAT0R_KEY),i, root, BLOCK_WIDTH, BLOCK_HEIGHT);
        		        currentBlocks.add(block1);
        			}    
        		}
        }
	}

    private void playerTwoLoseLife(BouncerObject b) {
    		player2Lives--;
    		player2LifeDisplay.setText(Integer.toString(player2Lives));    			

    		if (player2Lives > 0 ) {
    			if (b != null) {
    				b.setInitialPosition();
    			}
    		}
    		else {
    			handleGameOver();
    		}
    		b.togglePause();
    }
    private void loseLifeSinglePlayer(BouncerObject b) {
    		currentLives--;
    		updateLivesDisplay();
    		
    		if (currentLives > 0 ) {
    			if (b != null) {
    				b.setInitialPosition();
    			}
    		}
    		else {
    			handleGameOver();
    		}
    		
    		if (isMultiplayer) {
    			b.togglePause();
    		}
    		
    }

	private void handleGameOver() {
		animation.stop();
		setToMainMenu();
		
	}

	private void updateLivesDisplay() {
		lifeDisplay.setText(Integer.toString(currentLives));
	}
    
    private void gainLifeSinglePlayer() {
    		currentLives++;
    		updateLivesDisplay();
    }


    private void step (double elapsedTime) {   		
    			moveBouncers(elapsedTime);
    			
    			if (!isMultiplayer) {
        			handlePowerUpMovment();    				
    			}
        	    handleBouncerCollisions();    	
        		checkLevelBeat();
    }

	private void checkLevelBeat() {
		if (currentBlocks.size() == 0) {
			incrementLevel();
		}
		
	}

	private void handleBouncerCollisions() {
	    List<Block> blocksToRemove = new ArrayList<>();

		for (BouncerObject b : currentBouncers) {
			if (b.getView().getBoundsInParent().intersects(paddle1.getView().getBoundsInParent()) ||
					(isMultiplayer && b.getView().getBoundsInParent().intersects(paddle2.getView().getBoundsInParent()))) {
					if(paddle1.isSuper()) {
						b.increaseSpeed();
					}
					else if(paddle1.isTiny()) {
						b.reduceSpeed();
					}
					
					b.bounce(b.getView().getBoundsInParent().intersects(paddle1.getView().getBoundsInParent()));
				
			}
			
			for (Block currentBlock : currentBlocks) {
				if (b.getView().getBoundsInParent().intersects(currentBlock.getView().getBoundsInParent()) && ! currentBlock.getRemoved()) {
					b.bounce();
					blockBreak(currentBlock);
				}
				if (currentBlock.getRemoved()) {
					blocksToRemove.add(currentBlock);
				}
			}
			
			for (Block currentBlock : blocksToRemove) {
				currentBlocks.remove(currentBlock);
			}
			
			b.wallBounce(WINDOW_WIDTH, WINDOW_HEIGHT);	
		}	        
	}

	private void moveBouncers(double elapsedTime) {
		for (BouncerObject b : currentBouncers) {
		    b.move(elapsedTime);
		    if (b.getLifeLost()) {
		    		if (b.getHitTop() && isMultiplayer) {
		    			playerTwoLoseLife(b);
		    		}
		    		else {
			    		loseLifeSinglePlayer(b);
		    		}
		    }
		}
	}

	private void handlePowerUpMovment() {
		List<PowerUp> powerUpsToRemove = new ArrayList<>();
		for (PowerUp a : currentPowerUps) {
		    a.fall();
		    if (a.getOffScreen()) {
		    		a.remove();
		    		powerUpsToRemove.add(a);

		    }
		    else {
			    	for (PlayerPaddle currentPaddle : currentPaddles) {
						if (currentPaddle.getView().getBoundsInParent().intersects(a.getView().getBoundsInParent())) {
								a.remove();
								powerUpsToRemove.add(a);
								givePowerUp(currentPaddle);
						}
					}
			    }
		}	
		for ( PowerUp a : powerUpsToRemove) {
			currentPowerUps.remove(a);
		}	
	}
    
    private void givePowerUp(PlayerPaddle currentPaddle) {
		int powerUpDecider = (int) ( Math.random() * 10);
        switch (powerUpDecider) {
            case 1:  loseLifeSinglePlayer(null);
                     break;
            case 2:  gainLifeSinglePlayer();
                     break;
            case 3: increaseMultiplier();
            			break;
            case 4: changePaddleSize(currentPaddle, "small");
            			break;
            case 5: changePaddleSize(currentPaddle, "big");
            		 	break;
            case 6: powerUpSpeed("fast");
            			break;
            case 7: powerUpSpeed("slow");
            			break;
            	default: gainLifeSinglePlayer();
            			 break;
         }      
	}


	private void powerUpSpeed(String speed) {
		for (BouncerObject b : currentBouncers) {
			b.updateSpeed(speed);
		}
		powerUpTimer.schedule(new TimerTask() {
	    	  public void run() {
	    		  resetBallSpeed();
	    	  }
	    	}, POWER_UP_TIMEOUT);
	}
	
	
	private void resetBallSpeed() {
		for (BouncerObject b : currentBouncers) {
			b.updateSpeed("");
		}	
	}

	private void changePaddleSize(PlayerPaddle modifiedPaddle, String sizeIndicator) {
		 modifiedPaddle.updateSize(sizeIndicator);
		 powerUpTimer.schedule(new TimerTask() {
	    	  public void run() {
	    		  resetPaddleSize(modifiedPaddle);
	    	  }
	    	}, POWER_UP_TIMEOUT);		
	}
	
	private void resetPaddleSize(PlayerPaddle modifiedPaddle) {
		modifiedPaddle.updateSize("");
	}

	private void increaseMultiplier() {
		scoreMultiplier*=2;
	      powerUpTimer.schedule(new TimerTask() {
	    	  public void run() {
	    		  scoreMultiplier = 1;
	    	  }
	    	}, POWER_UP_TIMEOUT);
	}

    private void blockBreak(Block brokenBlock) {
    		currentScore+=POINTS_PER_BREAK*scoreMultiplier;
    		if (!isMultiplayer) {
    			scoreDisplay.setText(Integer.toString(currentScore));    			
    		}
		brokenBlock.loseLife();
		if (Math.random() > (1-POWER_UP_DROP_RATE)) {
			dropRandomPowerUp(brokenBlock.getView().getX(),brokenBlock.getView().getY());
		}
    }
    
    private void dropRandomPowerUp(double xPosition, double yPosition) {
    		if (!isMultiplayer) {
    			currentPowerUps.add(new PowerUp(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream(POWER_UP_IMAGE))),
        				xPosition, yPosition, WINDOW_HEIGHT, singlePlayerRoot,POWER_UP_SIZE));
    		}
    		
	}
    
    private void handleMultiplayerKey ( KeyCode code) {
    	 	if (handleSharedKeyInput(code)) {
         } 
         else if( code == KeyCode.LEFT) {
        	 	paddle2.moveLeft();
         }
         else if( code == KeyCode.RIGHT) {
     	 	paddle2.moveRight();
         }
    }
    
    private boolean handleSharedKeyInput(KeyCode code) {
    	 	if (code == KeyCode.A) {
            paddle1.moveLeft();
            return true;
        }
        else if (code == KeyCode.D) {
        		paddle1.moveRight();
        		return true;
        }
        else if (code == KeyCode.L) {
        		incrementLevel();
        		return true;
        }
        else if (code == KeyCode.SPACE) {
    			pauseOrUnpause();
    			return true;
        }
        else if (code == KeyCode.ENTER) {
        		handleGameOver();
        		return true;
        }
        else {
        		return false;
        }
    	
    }
    
	private void handleKeyInput (KeyCode code) {
		if (handleSharedKeyInput(code)) {
			
		}
        else if (code == KeyCode.R) {
        		setToSingleplayer();
        }
        else if (code == KeyCode.DIGIT1 ||
	        		code == KeyCode.DIGIT2 || code == KeyCode.DIGIT3 ||	code == KeyCode.DIGIT4 ||
	        		code == KeyCode.DIGIT5 || code == KeyCode.DIGIT6 ||	code == KeyCode.DIGIT7 ||
	        		code == KeyCode.DIGIT8 || code == KeyCode.DIGIT9 ||	code == KeyCode.DIGIT0 ) {      		
        		
        		setLevel(Integer.parseInt(code.getName()));
        } 
        else if (code == KeyCode.COMMA) {
        		incrementLife();
        }
		
        else if (code == KeyCode.V) {
        		paddle1.makeTinyBounce();
        }
        else if (code == KeyCode.B) {
        		paddle1.makeSuperBounce();
        }
        else if(code == KeyCode.N) {
        		paddle1.makeNormal();
        }
    }

	private void handleMouseInput (double x, double y) {
	    List<Block> blocksToRemove = new ArrayList<>();
		for (Block b : currentBlocks) {
			if (b.getView().contains(x,y)) {
				blocksToRemove.add(b);	
				b.removeBlock();
			}
		}
		for (Block b : blocksToRemove) {
			currentBlocks.remove(b);
		}

    }
	

	private void incrementLife() {
		currentLives++;
		updateLivesDisplay();		
	}

	private void pauseOrUnpause() {
		for (BouncerObject b : currentBouncers) {
			b.togglePause();
		}
		
	}

	private void setLevel(int levelSelect) {
		if(levelSelect < 10) {
			if (levelSelect == 0) {
				currentLevel = 10;
			}
			else {
				currentLevel = levelSelect;
			}
			setToSingleplayer();
		}
		
	}

	public void incrementLevel() {
		if (currentLevel + 1 <= MAX_LEVEL_AMOUNT) {
			currentLevel++;
		}
		if (!isMultiplayer) {	
			incrementLife();
			setToSingleplayer();
		}
		else {
			setToMultiplayer();
		}
	}
	
	public void clearGameObjects() {
		powerUpTimer.cancel();
		powerUpTimer = new Timer();	
		clearBouncers();
		clearPaddles();
		clearPowerUps();
		clearBlocks();	
	}

	private void clearBlocks() {
		currentBlocks.clear();
	}

	private void clearPowerUps() {
		currentPowerUps.clear();
	}

	private void clearPaddles() {
		currentPaddles.clear();
	}

	private void clearBouncers() {
		currentBouncers.clear();
	}

    /**
     * Launch program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
