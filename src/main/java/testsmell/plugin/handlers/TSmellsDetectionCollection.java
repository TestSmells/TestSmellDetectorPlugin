package main.java.testsmell.plugin.handlers;

import java.util.ArrayList;
import java.util.Collection;
import main.java.testsmell.TestFile;

/**
 * Collection class to handle and store test smells detection objects.
 */
public class TSmellsDetectionCollection {

	private static TSmellsDetectionCollection detectionCollectionObj = null;
	private int nextIndx;
	private ArrayList<TSmellsDetection> collection;
	
	/**
	 * Constructor to initialize a collection object and initialize the next index.
	 */
	private TSmellsDetectionCollection() {
		collection = new ArrayList<TSmellsDetection>();
		nextIndx = 0;
	}

	/**
	 * Gets a static instance of the test smell detection collection object.
	 */
	protected static TSmellsDetectionCollection getInstance() {
		if (detectionCollectionObj == null) {
			detectionCollectionObj = new TSmellsDetectionCollection();
		}
		return detectionCollectionObj;
	}
	
	/**
	 * Gets an existing of a test smells detection collection object.
	 * @return
	 */
	public static TSmellsDetectionCollection getExistingInstance() {
		return detectionCollectionObj;
	}
	
	/**
	 * Adds new test smells detection objects to the collection.
	 * @param t
	 */
	public void addNewDetections(Collection<TestFile> t) {
		for (TestFile tf : t) {
			collection.add(new TSmellsDetection(tf));
		}
	}
	
	/**
	 * Checks if the collection has a next detection.
	 * @return
	 */
	private Boolean hasNextDetection() {
		if (collection.size() > 0 && nextIndx < collection.size()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the next detection found in the collection.
	 * @return
	 */
	public TSmellsDetection getNextDetection() {
		TSmellsDetection detection;
		if (hasNextDetection()) {
			detection = collection.get(nextIndx);
			nextIndx += 1;
			return detection;
		}
		return null;
	}
	
	/**
	 * Resets the next index value for the collection.
	 */
	protected void resetNextIndexVal() {
		nextIndx = 0;
	}

	/**
	 * Clears the test smells collection and resets the next index.
	 */
	protected void clearTSmellsCollection() {
		collection.clear();
		nextIndx = 0;
	}
}