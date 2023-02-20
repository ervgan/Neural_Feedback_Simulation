import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

public class FMRIClassificationModel {

	/**
	 * Method to load training data which will be used to build classifier models
	 * 
	 * @return the training data set
	 */
	public Instances getTrainingData() {
		Instances trainingDataset = null;
		try {
			DataSource source = new DataSource("fmriDataTrain.csv");
			trainingDataset = source.getDataSet();
			// set class index
			trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("The file has not been found. Please check your filename!");
			e.printStackTrace();
		}
		return trainingDataset;
	}

	/**
	 * Method to load test data which will be used to test models
	 * 
	 * @return the test data set
	 */
	public Instances getTestData() {
		Instances testDataset = null;
		try {
			DataSource testSource = new DataSource("fmriDataTest.csv");
			testDataset = testSource.getDataSet();
			testDataset.setClassIndex(testDataset.numAttributes() - 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("The file has not been found. Please check your filename!");
			e.printStackTrace();
		}
		return testDataset;
	}

	/**
	 * Attribute selection: get rid of attributes not useful for classification.
	 * Evaluates worth of a subset of attributes by considering individual
	 * predictive ability and level of redundancy between them. Search backwards and
	 * delete attributes until evaluation decreases. Will not be used in this case
	 * as the number of attributes (over 14k) is too great and the running time is
	 * too long but might be used for future data sets
	 * 
	 * @param trainingDataset the training data set
	 * @return the new instances with the applied filter
	 */
	public Instances filterData(Instances trainingDataset) {
		Instances newTrainingDataset = null;
		AttributeSelection filter = new AttributeSelection();
		CfsSubsetEval eval = new CfsSubsetEval();
		GreedyStepwise search = new GreedyStepwise();
		search.setSearchBackwards(true);
		filter.setEvaluator(eval);
		filter.setSearch(search);
		try {
			filter.setInputFormat(trainingDataset);
			newTrainingDataset = Filter.useFilter(trainingDataset, filter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newTrainingDataset;
	}

	/**
	 * Naive Bayes classification: based on Baye's theorem, independence assumptions
	 * between predictors
	 * 
	 * @param trainingData the training data set
	 * @return the Naive Bayes classifier on our training data set
	 */
	public Classifier buildNBClassifier(Instances trainingDataset) {
		NaiveBayes nb = new NaiveBayes();
		try {
			nb.buildClassifier(trainingDataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nb;
	}

	/**
	 * Decision Tree classification: breaks down data set into smaller and smaller
	 * subsets while associating a decision tree incrementally. decision nodes and
	 * leaf nodes. ID3 algorithm uses entropy and information gain.
	 * 
	 * @param trainingData the training data set
	 * @return the Decision Tree classifier on our training data set
	 */
	public Classifier buildTreeClassifier(Instances trainingDataset) {
		String[] options = new String[2];
		// pruning to lower risk of overfitting (will increase accuracy of unseen data)
		options[0] = "-C";
		options[1] = "0.1";
		J48 tree = new J48();
		try {
			tree.setOptions(options);
			tree.buildClassifier(trainingDataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tree;
	}

	/**
	 * SVM classification: support vector machine. It finds the best "line" that
	 * separates classes
	 * 
	 * @param trainingData the training data set
	 * @return the SVM classifier on our training data set
	 */
	public Classifier buildSVMClassifier(Instances trainingDataset) {
		SMO svm = new SMO();
		try {
			svm.buildClassifier(trainingDataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return svm;
	}

	/**
	 * Combining models using AdaBoostM1: boosting a weak classifier by running the
	 * model multiple times and learns sequentially in an adaptive way by tweaking
	 * the subsequent weak models
	 * 
	 * @param model           the model we want to boost
	 * @param trainingDataset the training data set
	 * @return the boosted model
	 */
	public AdaBoostM1 combineModels(Classifier model, Instances trainingDataset) {
		AdaBoostM1 m1 = new AdaBoostM1();
		m1.setClassifier(model);
		m1.setNumIterations(10);
		try {
			m1.buildClassifier(trainingDataset);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m1;
	}

	/**
	 * Returns statistics showing the performance of the model used
	 * 
	 * @param model           the classifier model used (Naive Bayes, etc)
	 * @param trainingDataset the training data set
	 * @param testDataset     the test data set
	 */
	public void modelEvaluation(Classifier model, Instances trainingDataset, Instances testDataset) {
		Evaluation evalTest = null;
		try {
			evalTest = new Evaluation(trainingDataset);
			evalTest.evaluateModel(model, testDataset);
			System.out.println(evalTest.toSummaryString("Evaluation results:\n", false));
			System.out.println(evalTest.toMatrixString("--- Overall Confusion Matrix --- \n"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
