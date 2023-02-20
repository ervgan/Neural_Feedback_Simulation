
public class Sky {
	final float SCROLL_SPEED = 0.3f;
	// sky
	float x;
	float y;

	/**
	 * Sky constructor
	 * 
	 * @param x coordinate of the sky
	 * @param y coordinate of the sky
	 */
	public Sky(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * Scrolling speeds up when input has been detected
	 */
	public static int getModSkySpeed(boolean bIsInputProcessing) {
		if (bIsInputProcessing) {
			return 6;
		}
		return 1;
	}

	/**
	 * Method to make sky move in the background
	 * 
	 * @param mod the speed amplifier parameter
	 */
	public void scroll(int mod) {
		this.y += SCROLL_SPEED * mod;
		if (this.y >= SetupGame.SCREEN_Y) {
			this.y = ((-SetupGame.SKY_DIMENSION_Y * 2) + SetupGame.SCREEN_Y);
		}
	}
}
