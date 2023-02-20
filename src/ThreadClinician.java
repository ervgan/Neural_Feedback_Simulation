import java.util.ArrayList;

public class ThreadClinician extends Thread {

	/**
	 * Thread for the Clinician GUI to be run at the same time as the game GUI. This
	 * part is in the Menu() class and launches when the player presses "Enter"
	 */
	@Override
	public void run() {
		FMRIClassification c = new FMRIClassification();
		c.svmClassify();
		c.printToText();
		ArrayList<String> actualMoves = c.getActualMoves();
		ArrayList<String> predictedMoves = c.getPredictions();
		GUIClinician clinician = new GUIClinician("fMRI_test.nii");
		clinician.draw(actualMoves, predictedMoves);
	}
}
