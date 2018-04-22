package main.java.testsmell.plugin.handlers;

import java.util.ArrayList;
import java.util.Collection;
import main.java.testsmell.TestFile;

/**
 * Collection class to handle and store test smell detection objects.
 */
public class TSmellDetectionCollection {

	private static TSmellDetectionCollection detectionCollectionObj = null;
	private int nextIndx;
	private ArrayList<TSmellDetection> collection;
	
	/**
	 * Constructor to initialize a collection object and initialize the next index.
	 */
	private TSmellDetectionCollection() {
		collection = new ArrayList<TSmellDetection>();
		nextIndx = 0;
	}

	/**
	 * Gets a static instance of the test smell detection collection object.
	 */
	protected static TSmellDetectionCollection getInstance() {
		if (detectionCollectionObj == null) {
			detectionCollectionObj = new TSmellDetectionCollection();
		}
		return detectionCollectionObj;
	}
	
	/**
	 * Gets an existing of a test smell detection collection object.
	 * @return
	 */
	public static TSmellDetectionCollection getExistingInstance() {
		return detectionCollectionObj;
	}
	
	/**
	 * Adds new test smell detection objects to the collection.
	 * @param t
	 */
	public void addNewDetections(Collection<TestFile> t) {
		for (TestFile tf : t) {
			collection.add(new TSmellDetection(tf));
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
	public TSmellDetection getNextDetection() {
		TSmellDetection detection;
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
	 * Clears the test smell collection and resets the next index.
	 */
	protected void clearTSmellsCollection() {
		collection.clear();
		nextIndx = 0;
	}
}