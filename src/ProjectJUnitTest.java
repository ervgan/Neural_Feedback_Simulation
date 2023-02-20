import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

public class ProjectJUnitTest {

	/**
	 * Tests whether our uploaded training data has the right number of instances
	 * and attributes
	 */
	@Test
	void trainingDataset() {
		FMRIClassificationModel f = new FMRIClassificationModel();
		Instances trainingDataset = f.getTrainingData();
		assertEquals(trainingDataset.numAttributes(), 14819); // 14818 attributes plus one class attribute
		assertEquals(trainingDataset.numInstances(), 108); // the number of rows minus the first row
	}

	/**
	 * Tests whether our uploaded test data has the right number of instances and
	 * attributes
	 */
	@Test
	void testDataset() {
		FMRIClassificationModel f = new FMRIClassificationModel();
		Instances trainingDataset = f.getTestData();
		assertEquals(trainingDataset.numAttributes(), 14819); // 14818 attributes plus one class attribute
		assertEquals(trainingDataset.numInstances(), 72); // the number of rows minus the first row
	}

	/**
	 * Tests classifier performance, SVM should be the best
	 */
	@Test
	void testClassifierPerformance() {
		FMRIClassificationModel classification = new FMRIClassificationModel();
		Instances trainingDataset = classification.getTrainingData();
		Instances testDataset = classification.getTestData();
		Classifier svm = classification.buildSVMClassifier(trainingDataset);
		Classifier nb = classification.buildNBClassifier(trainingDataset);
		Classifier tree = classification.buildTreeClassifier(trainingDataset);

		// record number of correct classification
		int svmCount = 0;
		int nbCount = 0;
		int treeCount = 0;

		for (int i = 0; i < testDataset.numInstances(); i++) {
			// get class double value for current instance
			double actualLabel = testDataset.instance(i).classValue();

			// get Instance object of current instance
			Instance ins = testDataset.instance(i);

			// call classifyInstance, which returns a double value for the class
			double svmPredictedLabel;
			double nbPredictedLabel;
			double treePredictedLabel;

			try {
				svmPredictedLabel = svm.classifyInstance(ins);
				nbPredictedLabel = nb.classifyInstance(ins);
				treePredictedLabel = tree.classifyInstance(ins);

				if (actualLabel == svmPredictedLabel) {
					svmCount++;
				}

				if (actualLabel == nbPredictedLabel) {
					nbCount++;
				}

				if (actualLabel == treePredictedLabel) {
					treeCount++;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		boolean svmIsBetter = false;
		if (svmCount > treeCount && svmCount > nbCount) {
			svmIsBetter = true;
		}
		assertTrue(svmIsBetter);
	}

	/**
	 * Tests whether x dimension in fMRI NIfTI file is read correctly
	 */
	@Test
	void fMRIXDimension() {
		GUIClinician clinician = new GUIClinician("fMRI_test.nii");
		int x = clinician.getNx();
		assertEquals(x, 64);
	}

	/**
	 * Tests whether y dimension in fMRI NIfTI file is read correctly
	 */
	@Test
	void fMRIYDimension() {
		GUIClinician clinician = new GUIClinician("fMRI_test.nii");
		int y = clinician.getNy();
		assertEquals(y, 64);
	}

	/**
	 * Tests whether z dimension in fMRI NIfTI file is read correctly
	 */
	@Test
	void fMRIZDimension() {
		GUIClinician clinician = new GUIClinician("fMRI_test.nii");
		int z = clinician.getNz();
		assertEquals(z, 30);
	}

	/**
	 * Tests whether time dimension in fMRI NIfTI file is read correctly
	 */
	@Test
	void fMRITDimension() {
		GUIClinician clinician = new GUIClinician("fMRI_test.nii");
		int t = clinician.getTask();
		assertEquals(t, 72);
	}
	
	/**
	 * Tests normalization method in GUIClinician
	 */
	@Test
	void testNormalization() {
		GUIClinician clinician = new GUIClinician("fMRI_test.nii");
		int nx = clinician.getNx();
		int ny = clinician.getNy();
		double slice[][] = clinician.convertIntensityToPixelValue(nx, ny, 1, 1);
		double max = 255;
		
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				if (slice[x][y] > max)
					max = slice[x][y];
			}
		}
		
		assertTrue(Math.abs(max - 255) <= 1E-6);
		
	}

	/**
	 * Tests sky speed
	 */
	@Test
	void skySpeed() {
		Sky s = new Sky(0, 0);
		s.scroll(1);

		assertTrue(Math.abs(s.getY() - 0.3) <= 1E-6);
	}

	/**
	 * Tests sky speed modifier
	 */
	@Test
	void skySpeedMod() {
		assertEquals(6, Sky.getModSkySpeed(true));
		assertEquals(1, Sky.getModSkySpeed(false));
	}

	/**
	 * Tests file loading
	 */
	@Test
	void testUsingData() {
		DataProcessor dp = new DataProcessor();
		assertTrue(dp.isUsingData());
	}

	/**
	 * Tests input check between actual move and prediction
	 */
	@Test
	void testGetData() {
		DataProcessor dp = new DataProcessor();
		FMRIClassification cl = new FMRIClassification();
		cl.svmClassify();
		// the 36th actual move of our array list actual moves starting at
		// matches with the 36th prediction so it should return true
		assertTrue(dp.getData());
	}

	/**
	 * Tests input data detection
	 */
	@Test
	void testGameInputUpdate() {
		DataProcessor dp = new DataProcessor();
		dp.updateInputData(true, 1000);
		assertTrue(dp.bInputSuccess);
		assertEquals(1000, dp.lastInputTime);
		assertTrue(dp.isInputProcessing(1500));
	}

	/**
	 * Tests whether label should be drawn
	 */
	@Test
	void testLabelCheck() {
		Label label = new Label();
		label.lastLabelDrawTime = 0;
		assertTrue(label.shouldDrawLabel(4000));
		assertTrue(!label.shouldDrawLabel(1));
	}

	/**
	 * Tests setting of label info
	 */
	@Test
	void testLabelInfo() {
		Label label = new Label();
		label.bDraw = false;
		label.setLabelDrawInfo("Foot", 1000);
		assertEquals("Resting", label.ID);
		assertEquals(0, label.lastLabelDrawTime);

		label.bDraw = true;
		label.setLabelDrawInfo("Foot", 1000);
		assertEquals("Foot", label.ID);
		assertEquals(1000, label.lastLabelDrawTime);
	}
}
