import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class ThreadGame extends Thread {

	int SCREEN_X = 960;
	int SCREEN_Y = 900;

	/**
	 * Thread of the GUI game to appear at the same time as the Clinician GUI. This
	 * part will launch the game GUI
	 */
	public void run() {
		SetupGame set = new SetupGame(SetupGame.TITLE);
		AppGameContainer app;
		try {
			app = new AppGameContainer(set);
			app.setDisplayMode(SCREEN_X, SCREEN_Y, false);
			app.setAlwaysRender(true);
			app.start();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
