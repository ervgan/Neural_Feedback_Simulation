import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState {
	final int NUM_BALLOONS = 5;
	final int INPUT_DELAY = 50;

	long enterPressedTime;
	long enterPressedTwiceTime;

	boolean bEnterPressedOnce;
	boolean bEnterPressedTwice;
	Balloon balloons[];

	/**
	 * Game Menu constructor
	 * 
	 * @param state the integer representing the state of the game
	 */
	public Menu(int state) {
		;
	}

	/**
	 * Initialize menu with different balloons
	 */
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		bEnterPressedOnce = false;

		balloons = new Balloon[NUM_BALLOONS];
		balloons[0] = new Balloon(300, 300, 0.3f, 0.7f, new Image("sprites/balloon-1.png"));
		balloons[1] = new Balloon(500, 500, 0.4f, 1f, new Image("sprites/balloon-2.png"));
		balloons[2] = new Balloon(700, 700, 0.2f, 0.5f, new Image("sprites/balloon-3.png"));
		balloons[3] = new Balloon(100, 100, 0.4f, 1.f, new Image("sprites/balloon-4.png"));
		balloons[4] = new Balloon(200, 600, 0.3f, 0.9f, new Image("sprites/balloon-5.png"));
	}

	/**
	 * method to display menu on the GUI
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {

		// draw background
		Image screenStart = new Image("sprites/screen-start.png");
		screenStart.draw(0, 0);

		// update balloon position
		for (int i = 0; i < NUM_BALLOONS; i++) {
			balloons[i].img.draw(balloons[i].x, balloons[i].y, balloons[i].scale);
		}

		if (bEnterPressedTwice) {
			Image loading = new Image("sprites/loading.png");
			loading.draw(0, 0);
		} else if (bEnterPressedOnce) {
			Image instructions = new Image("sprites/instructions.png");
			instructions.draw(270, 230);
		} else {
			Image screenStartTitle = new Image("sprites/screen-start-title.png");
			screenStartTitle.draw(275, 400);
		}
	}

	/**
	 * Method to update the game menu. If "Enter" is pressed, then the Clinician GUI
	 * is launched with a pause allowing it to load and we go to the state number 1
	 * of the game which is the play() state
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		long currTime = System.currentTimeMillis();
		// update balloon position
		for (int i = 0; i < NUM_BALLOONS; i++) {
			Balloon.updatePosition(balloons[i]);
		}
		Input input = gc.getInput();
		if (input.isKeyDown(Input.KEY_ENTER) && bEnterPressedOnce) {
			if ((currTime - enterPressedTime) > INPUT_DELAY) {
				bEnterPressedTwice = true;
				enterPressedTwiceTime = currTime;
			}
		}
		if (bEnterPressedTwice) {
			if ((currTime - enterPressedTwiceTime) > INPUT_DELAY) {
				sbg.enterState(1);
				Thread threadClinician = new ThreadClinician();
				threadClinician.start();
			}
		}
		if (input.isKeyDown(Input.KEY_ENTER)) {
			enterPressedTime = System.currentTimeMillis();
			bEnterPressedOnce = true;
		}

	}

	@Override
	public int getID() {
		return 0;
	}

}
