import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class SetupGame extends StateBasedGame {
	public static int SCREEN_X = 960;
	public static int SCREEN_Y = 900;
	final static int SKY_DIMENSION_Y = 2085;

	public static final String TITLE = "Neural Network Motor Game";
	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int END_GAME = 2;

	/**
	 * Setup game constructor
	 * 
	 * @param gameName the name we want our interface window to be named
	 */
	public SetupGame(String gameName) {
		super(gameName);
		this.addState(new Menu(MENU));
		this.addState(new Play(PLAY));
		this.addState(new EndGame(END_GAME));
	}

	/**
	 * Initializes the game with the different states possible
	 */
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		// TODO Auto-generated method stub
		this.getState(MENU).init(gc, this);
		this.getState(PLAY).init(gc, this);
		this.getState(END_GAME).init(gc, this);
		this.enterState(MENU);
	}
}
