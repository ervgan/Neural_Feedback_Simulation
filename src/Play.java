import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;

public class Play extends BasicGameState {
	boolean bUsingData;
	boolean bDrawLabel;
	static private final int FEEDBACK_DURATION = 1100;
	static public final int MOVE_DELAY = 200;
	static public int numSuccess;
	static public int numFail;
	int actualMovesIndex;
	long lastMoveTime;
	long currTime;
	Balloon balloon;
	Sky sky1;
	Sky sky2;
	Label label;
	Random rand;
	DataProcessor dp;
	TrueTypeFont ttf;
	FMRIClassification cl;
	ArrayList<String> actualMoves;

	/**
	 * Game Play constructor
	 * 
	 * @param state the integer representing the state of the game
	 */
	public Play(int state) {
		;
	}

	/**
	 * init class to initialized all objects and parameters needed for render() and
	 * update()
	 */
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		// processor classes
		rand = new Random();
		dp = new DataProcessor();
		cl = new FMRIClassification();
		// sprite classes
		balloon = new Balloon((SetupGame.SCREEN_X / 2 - 50), SetupGame.SCREEN_Y / 2, 0.3f, 0.5f,
				new Image("sprites/balloon-3.png"));
		sky1 = new Sky(0, (-SetupGame.SKY_DIMENSION_Y + SetupGame.SCREEN_Y));
		sky2 = new Sky(0, ((-SetupGame.SKY_DIMENSION_Y * 2) + SetupGame.SCREEN_Y));
		label = new Label();
		lastMoveTime = 0;
		actualMoves = new ArrayList<>();

		// init variables
		dp.bInputSuccess = false;
		bUsingData = false;
		bDrawLabel = false;

		actualMoves = cl.getActualMoves();
		actualMovesIndex = 0;
		Font font = new Font("Helvetica", Font.BOLD, 30);
		ttf = new TrueTypeFont(font, true);
	}

	/**
	 * Displays balloon and sky with different instructions and if the player
	 * followed instruction based on the update method. the update() and render()
	 * are run for each frame
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		Image imgSky = new Image("sprites/bg-sky-vert-1.png");
		Image imgSky2 = new Image("sprites/bg-sky-vert-2.png");

		// balloon and sky positions
		imgSky.draw(sky1.x, sky1.y);
		imgSky2.draw(sky2.x, sky2.y);
		balloon.img.draw(balloon.x, balloon.y, balloon.scale);
		actualMovesIndex = dp.getArrayIndex();

		// create labels
		if ((currTime - label.lastLabelDrawTime) < 1400) {
			if (label.bDraw) {
				label.getLabelImage().draw(125, (SetupGame.SCREEN_Y - 225));
				label.bVisible = true;
			} else {
				label.bVisible = false;
			}
		}

		// write text
		g.setFont(ttf);

		// Displays good job if it the player made the right move, wrong move otherwise
		if (((currTime - lastMoveTime) < FEEDBACK_DURATION) && ((currTime - lastMoveTime) > MOVE_DELAY)) {
			if (dp.isRightMove()) {
				g.drawString("GOOD JOB!", (SetupGame.SCREEN_X / 2 - 100), (SetupGame.SCREEN_Y / 2 - 50));
			} else if (!dp.isRightMove()) {
				g.drawString("WRONG MOVE.", (SetupGame.SCREEN_X / 2 - 120), (SetupGame.SCREEN_Y / 2 - 50));
			}
		}

		g.drawString("Elevation: " + balloon.elevation, SetupGame.SCREEN_X - 250, 0);
		g.drawString("Score: " + numSuccess + " / " + (numSuccess + numFail), SetupGame.SCREEN_X - 250, 40);
	}

	/**
	 * Update method to check if there is data for input, if there is then make the
	 * balloon move up if the player made a right move (comparing predicted moves to
	 * actual moves). If there is no data, player can play by pressing w.
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		// input reader class used here in case of no data
		actualMovesIndex = dp.getArrayIndex();
		
		//wait for clinician view to load
		while (!GUIClinician.clinicianLoadSuccess) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//get the move to perform and set up label info
		if (actualMovesIndex < 72) {
			currTime = System.currentTimeMillis();
			// process input
			if (dp.isUsingData()) {
				while ((currTime - lastMoveTime) > 2500) {
					lastMoveTime = currTime;
					// should label be drawn
					try {
						label.bDraw = true;
						label.setLabelDrawInfo(actualMoves.get(actualMovesIndex), currTime);
						dp.updateInputData(dp.getData(), currTime);
					} catch (IndexOutOfBoundsException e) {
						System.out.println("Play.update IndexOutOfBoundsException");
					}

					// update stats
					if (dp.isRightMove()) {
						numSuccess++;
					} else {
						numFail++;
					}
				}
			}
			
			// update sky speed
			int modSkySpeed = Sky.getModSkySpeed(dp.isInputProcessing(currTime));
			
			// update balloon elevation
			if (currTime - balloon.elevationTimer > (480 / modSkySpeed)) {
				balloon.elevationTimer = currTime;
				balloon.elevation += 1;
			}
			// move sky
			sky1.scroll(modSkySpeed);
			sky2.scroll(modSkySpeed);
		} else if (actualMovesIndex >= 72) {
			sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		return 1;
	}

}
