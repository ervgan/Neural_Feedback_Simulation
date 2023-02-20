import java.io.File;
import java.util.ArrayList;

public class DataProcessor {
	private int arrayIndex = 36;
	private boolean rightMove;
	static final int INPUT_WAIT = 1000;
	int lastLabelDrawTime = 0;
	boolean bInputSuccess = false;
	long lastInputTime = 0;

	/**
	 * Data processor constructor initializing the inputSuccess check and the time
	 * of the input
	 */
	public DataProcessor() {
		this.bInputSuccess = false;
		this.lastInputTime = 0;
	}

	/**
	 * Check if the game is using data by checking if the file is there. This can
	 * eventually become a scanner taking in the file that the user wants to use but
	 * we did not want to leave any room for confusion so we decided for the user
	 * since we are exclusively using simulated data in our game
	 * 
	 * @return true if the file is found, false otherwise
	 */
	public boolean isUsingData() {
		File f = new File("fmriDataTest.csv");
		if (f.exists()) {
			return true;
		}
		return false;
	}

	
	/**
	 * Check if the input was successfully, meaning that we compare the predicted
	 * move (from SVM model) to the instruction (which is in our case actual move
	 * since we are using simulated data).
	 * 
	 * @return true if the actual move is equal to the predicted move, false otherwise
	 */
	public boolean getData() {
		FMRIClassification cl = new FMRIClassification();
		ArrayList<String> actualMoves = cl.getActualMoves();
		ArrayList<String> predictedMoves = cl.getPredictions();
		try {
			if (actualMoves.get(arrayIndex).equals(predictedMoves.get(arrayIndex))) {
				arrayIndex++;
				rightMove = true;
				return true;
			}
		} catch (IndexOutOfBoundsException e) {
			;
		}
		arrayIndex++;
		rightMove = false;
		return false;
	}

	/**
	 * Checks if the input is processing by comparing the time between current input
	 * and last time there was an input
	 * 
	 * @param currentTime the current time in milliseconds recorded by the system
	 * @return true if the input is processing, false otherwise
	 */
	public boolean isInputProcessing(long currentTime) {
		if (bInputSuccess) {
			long timeElapsed = currentTime - this.lastInputTime;
			if (timeElapsed > Play.MOVE_DELAY) {
				if ((timeElapsed < INPUT_WAIT)){
					return true;
				} else {
					this.bInputSuccess = false;
				}
			}
		}
		return false;
	}

	/**
	 * Set input success to true and update current time after an input
	 * 
	 * @param currTime the current time in milliseconds recorded by the system
	 */
	public void updateInputChecks(long currTime) {
		this.bInputSuccess = true;
		this.lastInputTime = currTime;
	}

	/**
	 * Update input checks if the user is using data, (which is what we are doing in
	 * our case)
	 * 
	 * @param b        a parameter to check if the input is correctly entered
	 *                 (getData() method from DataProcessor)
	 * @param currTime the current time in milliseconds recorded by the system
	 */
	public void updateInputData(boolean b, long currTime) {
		if (b) {
			updateInputChecks(currTime);
		}
	}

	/**
	 * Get method to return if the kid followed instruction or not
	 * 
	 * @return true if kid followed instruction, false otherwise
	 */
	public boolean isRightMove() {
		return rightMove;
	}

	/**
	 * Get method to return the index of the array list containing instructions
	 * (actual moves) after the frame update
	 * 
	 * @return an integer representing the index where the game is in the arraylist
	 */
	public int getArrayIndex() {
		return arrayIndex;
	}
}
