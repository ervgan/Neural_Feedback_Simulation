# Neural Feedback Simulation for Children with Autism

## Project name
### Real-time Neural Feedback Simulation System for Children with Autism

## Description
Autistic children perceive external environment atypically according to current research. This real-time neural feedback simulation system is to help children with Autism to learn how to perform motor tasks correctly. It will take neural data (fMRI) as the input, use machine learning classifier to classify the signal, show the classification result in 2 perspectives - clinician's view and kid's view. Clinician's GUI shows brain axial scans with expected and detected labels. Kid's GUI uses a balloon flight game to tell kids feedback if they are performing the task correctly. 

Presentation video link: https://vimeo.com/351929429

## Team
Benjamin Lopez Barba, Ervin Gan, Xinhui Li

## Work Breakdown
Benjamin: build Game GUI, add corresponding JUnit tests, record video \
Ervin: build machine learning classifiers, help with Game GUI, add corresponding JUnit tests, organize all libraries and scripts \
Xinhui: test machine learning classifiers, build Clinician GUI, preprocess fMRI data, add corresponding JUnit tests

## Environment Set-up
1 Open terminal, go to directory you want to save the project folder, type 
```shell
git clone https://github.com/ervgan/Neural_Feedback_Simulation.git
```
2 Create a new Java project in Eclipse, uncheck "Use default location", choose your local project folder, and also choose **JavaSE-11** as execution environment under "JRE". If you haven't installed JavaSE-11, please use the link to download and install: https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html

![GitHub Logo](/readme-img/JavaEnv.jpeg)

3 Make sure all libraries (Weka, Slick2D, Nifti) are imported. If it is not the case, right click on your project, go to build path / configure build path / libraries / external JARs and search the Weka folder to add the Weka.Jar file (not the Weka-src.jar), then go the the nifti folder and add the niftijio-1.0-SNAPSHOT.jar file and finally add all .jar files from the Slick2d folder.

4 In Eclipse, open src/(default package)/GameLauncher.java, run it as Java Application

## Execution Instructions

### Introduction page

Press Enter to go to instruction page. In real life, kids will lay down in a fMRI scanner or wear an EEG headset so their neural signal will be recorded while they are performing motor tasks.

![GitHub Logo](/readme-img/intro.jpeg)


### Instruction page

Press Enter to see simulated GUI. In real life, kids will read the instruction page and press Enter when they are ready to take the tasks. 

![GitHub Logo](/readme-img/instruction.jpeg)


### Kid GUI
An instruction is shown to tell kids to perform a certain motor task, like "Move Finger". At the meantime, a pre-trained machine learning classifier will read their neural signal and detect whether the kid is performing the task correctly. 

![GitHub Logo](/readme-img/move-instruction.jpeg)

If the kid performs the task correctly, the balloon will go up faster!

![GitHub Logo](/readme-img/kidGUI-right.jpeg)

If the kid doesn't perform the task correctly, the balloon will stop going up.

![GitHub Logo](/readme-img/kidGUI-wrong.jpeg)


### Clinician GUI
A clinician GUI will synchronize with kid GUI so the clinician can learn how the kid is performing and also check the brain scan to make diagnosis decision.

![GitHub Logo](/readme-img/clinicianGUI-right.jpeg)
![GitHub Logo](/readme-img/clinicianGUI-wrong.jpeg)


## Design
### Machine Learning 
Classification:

Our training data consists of 108 tasks (rows) and 14818 1D ROIs (columns) and our testing data consists of 72 tasks (rows) and 14818 1D ROIs (columns).

The fMRIClassificationModel class will load the data by specifying that the last column of each data set has the class attribute (the expected result in our supervised learning model). It will then build 5 models based on the training data set: the Naive Bayes model, the Decision Tree model, the Support Vector Machine, the Naive Bayes Model boosted 10 times using AdaBoostM1 and the Decision Tree model boosted 10 times using AdaboostM1. It then evaluates each model using the test data set and compare them by accuracy rate (which is the percentage of predicted values equal to the expeted values). 
We also used 10-fold cross validation tests (we use 10 different combinations of training data set and test data set) on the whole data set (training data set + test data set) to check for problems related to overfitting. 
When comparing results, the Support Vector Machine model comes on top with an accuracy rate of around 93% and performs in line with other models in the cross-validation test with an accuracy rate in the range 60-70%.

After finding that the Support Vector Machine Model is the best classifier for our data set, we use this model to make task predictions on our test data which we use as real data set (since we are in a simulation). These predicted tasks are then stored in an arraylist and the expected tasks are stored in a separate arraylist. These two arraylists are then used for the clinician GUI and the gaming GUI.

Important: since we do not have a real kid with brain sensors on performing task for us, we used the expected moves as the instructions to the kid and we used the predicted moves as the moves performed by the kid. This is because, the instruction and the predicted moves have to correspond to our data set since we do not have real live data. Of course in a real-life environment, the instruction can be anything (move foot, move lips, move finger, rest) and after the kid has performed a move, the machine learning model will classify the move based on the data retrieved from the brain sensors on the kid.

Definitions: 
-Naive Bayes: based on Baye's theorem which assumes independence between predictors
-Decision Tree: breaks down data set into smaller and smaller subsets while associating a decision tree incrementally
-Support Vector Machine: It finds the best "line" that separates different classes
-AdaBoostM1: Boosts a weak classifier by running the model multiple times and learns sequentially in an adaptive way by tweaking the subsequent weak models

### Clinician GUI
fMRI data is stored as NIfTI format and a public library niftijio (https://github.com/cabeen/niftijio) is used to read the data. FourDimensionalArray, LEDataInputStream, LEDataInputStream, NiftiHeader and NiftiVolume classes are from that library. 

GUIClinician class is designed to show fMRI brain scans to clinicians. First fMRI 4 dimensional data is read using niftijio library, and then intensities in original data are converted to gray scale pixel values. PennDraw is used to plot the brain image as an animation and also expected and detected labels are shown so Clinicians can match brain scans with kids’ performance.   

### Game GUI

The Game GUI takes in instructions (the expected moves from the data) and show them on the screen for the player to see. If the kid has followed the instruction (meaning that the predicted move equals the expected move) then the balloon goes up. The game ends after 36 tasks and shows the accuracy of the kid's moves. The 36 tasks are the last 36 tasks of the test data set, this was to reduce the running time of the simulated game.

## Classes and methods
### Machine Learning Classifier
#### FMRIClassificationModel
getTrainingData(): read preprocessed fMRI training data csv file

getTestData(): read preprocessed fMRI test data csv file

filterData(): get rid of attributes not useful for classification. Evaluate worth of a subset of attributes by considering individual predictive ability and level of redundancy between them. Search backwards and delete attributes until evaluation decreases. Will not be used in this case as the number of attributes (over 14k) is too great and the running time is too long but might be used for future datasets

buildNBClassifier(): build Naive Bayes classifier

buildTreeClassifier(): build Decision Tree classifier

buildSVMClassifier(): build Support Vector Machine classifier

combineModels(): combine models using AdaBoostM1. Boost a weak classifier by running the model multiple times and learns sequentially in an adaptive way by tweaking the subsequent weak models

modelEvaluation(): evaluate model performance. Return statistics showing the performance of the model used

#### FMRIClassifification
svmClassify(): load training and test datasets, then implements classifier with highest accuracy on test set and get classification results on test set which are then stored in an array list.

printToText(): prints to Labels.txt all expected moves and predicted moves

### Clinician GUI
#### GUIClinician
convertIntensityToPixelValue(): Method to convert intensities in fMRI raw data to gray scale values

draw(): The draw method allows to draw with the Nifti library and Penndraw the brain scans in 2D for each move expected (instruction)

### Game GUI
#### GameLauncher
main(): Launches the game

#### DataProcessor
isUsingData(): Checks if the game is using data, otherwise the "w" key has to be pressed to play

getData(): Check if the input was successfully, meaning that we compare the predicted move (from SVM model) to the instruction (which is in our case actual move since we are using simulated data)

isInputProcessing(): Checks if the input is processing by comparing the time between current input and last time there was an input

updateInputChecks(): Set input success to true and updates current time after an input

updateInputData(): Update input checks if the user is using data, (which is what we are doing in our case)

#### Balloon
updatePosition(): Updates the positions of the balloon

#### Sky
getModSkySpeed(): Scrolling speeds up when input has been detected

scroll(): Method to make sky move in the background

#### Label
setLabelDrawInfo(): Check to see if a new label needs to be drawn if so determine which label to draw and set time drawn

shouldDrawLabel(): Check to see if a label should be drawn by comparing the time of the last label to the current time. If is higher than 3000ms, then draw label.

getLabelImage(): Method to get label image depending for the instruction move

#### Menu
init(): Initializes menu game state

render(): Display menu on the GUI. 

update(): Updates states of the game and thus screens when pressing enter

#### Play
init(): Initializes play game state, in particular all variables and objects needed for update() and render()

render(): Displays the sky, the balloon, the instruction label, the current score, and an indication to the kid for "good move" or "wrong move"

update(): Updates the game by going through each element of the array list of actualMoves (i.e. instructions) at a fixed interval to match with Clinician GUI speed

#### EndGame
init(): Initializes endgame state

render(): Displays the state of the end of the game, shows the score and an ending message

update(): Updates the accuracy rate to display at the end

#### SetupGame
initStatesList(): Initializes all game states and specify which state to start with (which is the menu state)

#### ThreadClinician
run(): runs the machine learning classification and launches the Clinician GUI to show brain scans for all instructions. Thread created to run at the same time as gaming GUI

#### ThreadGame
run(): launches the gaming GUI. Thread created to be launched at the same time as Clinician GUI

## Libraries
Weka 3-8-3, Niftijio, Slick2D

Copyright (C) 1998-2018  University of Waikato
