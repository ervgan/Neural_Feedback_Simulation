public class GameLauncher {

	/**
	 * This main method is to launch the game
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Thread threadGame = new ThreadGame();
		threadGame.start();
	}

}
