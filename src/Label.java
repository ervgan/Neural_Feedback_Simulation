import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Label {
	static final int LABEL_WAIT = 3000;

	boolean bVisible;
	boolean bDraw;
	boolean bUpdated;
	long lastLabelDrawTime;
	String ID;
	
	/**
	 * Label constructor
	 */
	Label() {
		this.bVisible = false;
		this.bDraw = false;
		this.bUpdated = false;
		this.lastLabelDrawTime = 0;
		this.ID = "Resting";
	}

	/**
	 * Check to see if a new label needs to be drawn If so determine which label to
	 * draw and set time drawn
	 * 
	 * @param labelID  the label to draw (foot, rest, lips, finger)
	 * @param currTime the current time in milliseconds recorded by the system
	 */
	public void setLabelDrawInfo(String labelID, long currTime) {
		if (this.bDraw) {
			this.lastLabelDrawTime = currTime;
			this.ID = labelID;
		}
	}

	/**
	 * Check to see if a label should be drawn by comparing the time of the last
	 * label to the current time. If is higher than 3000ms, then draw label.
	 * 
	 * @param currTime the current time in milliseconds recorded by the system
	 * @return true if a label should be drawn, false otherwise
	 */
	public boolean shouldDrawLabel(long currTime) {
		if (((currTime - this.lastLabelDrawTime) > LABEL_WAIT)) {
			return true;
		}

		this.bUpdated = false;
		return false;
	}

	/**
	 * Method to get label image depending for the instruction move
	 * 
	 * @return an image depending on the move from array list
	 * @throws SlickException
	 */
	public Image getLabelImage() throws SlickException {
		switch (this.ID) {
		case "Foot":
			return new Image("sprites/label-foot.png");
		case "Finger":
			return new Image("sprites/label-finger.png");
		case "Lips":
			return new Image("sprites/label-lips.png");
		case "Resting":
			return new Image("sprites/label-rest.png");
		}

		return null;
	}
}
