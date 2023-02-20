import org.newdawn.slick.Image;

public class Balloon {
	final float BALLOON_FALL = 0.2f;
	int elevation;
	// allows elevation to be incremented over time instead of per frame
	long elevationTimer;
	float x;
	float y;
	float speed;
	float scale;
	Image img;
	
	/**
	 * Balloon constructor
	 * @param x
	 * @param y
	 * @param speed
	 * @param scale
	 * @param img the image of the balloon
	 */
	public Balloon(int x, int y, float speed, float scale, Image img) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.elevationTimer = 0;
		this.scale = scale;
		this.img = img;
	}
	
	/**
	 * Updates the positions of the balloon
	 * @param balloon a balloon object
	 */
	public static void updatePosition(Balloon balloon) {
		balloon.y -= balloon.speed;
		if (balloon.y < -250) {
			balloon.y = SetupGame.SCREEN_Y;
		}
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

}
