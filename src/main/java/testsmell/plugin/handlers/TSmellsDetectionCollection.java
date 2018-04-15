/**
 * 
 */
package main.java.testsmell.plugin.handlers;

import java.util.ArrayList;
import java.util.Collection;

import main.java.testsmell.TestFile;

public class TSmellsDetectionCollection {

	private static TSmellsDetectionCollection detectionCollectionObj = null;
	private int nextIndx;
	private ArrayList<TSmellsDetection> collection;
	
	private TSmellsDetectionCollection() {
		collection = new ArrayList<TSmellsDetection>();
		nextIndx = 0;
	}
	
	/**
	 * Create a static method to get instance.
	 */
	protected static TSmellsDetectionCollection getInstance() {
		if (detectionCollectionObj == null) {
			detectionCollectionObj = new TSmellsDetectionCollection();
		}
		return detectionCollectionObj;
	}
	public static TSmellsDetectionCollection getInstanceExisting() {
		return detectionCollectionObj;
	}
	
	public void addNewDetections(Collection<TestFile> t)
	{
		for (TestFile tf : t)
		{
			collection.add(new TSmellsDetection(tf));
		}
		
	}
	private Boolean hasNextDetection()
	{
		if (collection.size() > 0 && nextIndx < collection.size())
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	public TSmellsDetection getNextDetection()
	{
		TSmellsDetection detection;
		if (hasNextDetection())
		{
			detection = collection.get(nextIndx);
			nextIndx += 1;
			return detection;
		}
		return null;
	}
	
	protected void resetNextIndexVal()
	{
		nextIndx = 0;
	}
	
	
	/**
	 * Create a static method to get instance.
	 */
}