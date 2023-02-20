import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class FMRIClassification {
	private static ArrayList<String> predictions = new ArrayList<String>();
	private static ArrayList<String> actualMoves = new ArrayList<String>();

	/**
	 * Method to get the predictions based on the SVM model. Tested Naive Bayes
	 * model, Decision Tree model, Naive Bayes Model boosted 10 times by Adaboost,
	 * and Decision Tree model boosted 10 times by Adaboost and Support Vector
	 * Model. The support vector model is the best performing one with around 93%
	 * accuracy while boosted models only reached up to 86%. 10-fold cross
	 * validation testing was also used. Here we use our test data set as our
	 * simulated data set.
	 * 
	 * @return an array list of string with each element being a predicted move
	 */
	public ArrayList<String> svmClassify() {
		FMRIClassificationModel classification = new FMRIClassificationModel();
		Instances trainingDataset = classification.getTrainingData();
		Instances testDataset = classification.getTestData();
		// build SVM model
		Classifier svmModel = classification.buildSVMClassifier(trainingDataset);
		classification.modelEvaluation(svmModel, trainingDataset, testDataset);
		for (int i = 0; i < testDataset.numInstances(); i++) {
			Instance newInstance = testDataset.instance(i);
			try {
				double actualIndex = testDataset.instance(i).classValue();
				String actualMove = testDataset.classAttribute().value((int) actualIndex);
				double predIndex = svmModel.classifyInstance(newInstance);
				String predictedMove = testDataset.classAttribute().value((int) predIndex);
				predictions.add(predictedMove);
				actualMoves.add(actualMove);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return predictions;
	}

	/**
	 * Prints the instruction (actual move) and the prediction in sequence in a text
	 * file for the TA to check all moves done.
	 */
	public void printToText() {
		String fileName = "labels.txt";
		String instructionsAndPredictions = "";
		for (int i = 0; i < predictions.size(); i++) {
			instructionsAndPredictions = instructionsAndPredictions + actualMoves.get(i) + "," + predictions.get(i)
					+ "\n";
		}
		FileWriter fw;
		try {
			fw = new FileWriter(fileName);
			fw.write(instructionsAndPredictions);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return arrayList containing all predictions made by SVM model. We use these
	 *         predictions as a proxy for the actual moves made my the kid. We thus
	 *         expect the kid to follow instructions 93% of the time (i.e. accuracy
	 *         of the model)
	 */
	public ArrayList<String> getPredictions() {
		return predictions;
	}

	/**
	 * 
	 * @return arrayList containing all actual moves expected from data. We will use
	 *         these moves as instructions in the game since we do not have a real
	 *         kid and brain sensors performing movements. The game will essentially
	 *         compare instruction (actual move in our simulation) and actual move
	 *         (predicted move in our simulation)
	 */
	public ArrayList<String> getActualMoves() {
		return actualMoves;
	}

}
