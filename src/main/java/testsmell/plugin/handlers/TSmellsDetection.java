package main.java.testsmell.plugin.handlers;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import main.java.testsmell.AbstractSmell;
import main.java.testsmell.TestFile;
import main.java.testsmell.TestSmellDetector;

/**
 * Detection class to encapsulate test smell detection objects.
 */
public class TSmellsDetection {
	private SimpleEntry<String, String> paths;
	private ArrayList<String> smells = new ArrayList<String>();
	private TestFile testFile;
	
	/**
	 * Constructor that takes in a test file to create the test smell detection object.
	 * @param tf
	 */
	protected TSmellsDetection(TestFile tf) {
		testFile = tf;
	}
	
	/**
	 * Creates an instance of the test smell detector and runs the detect smells on a list of test files.
	 */
	public void detectSmells() {
		TestSmellDetector testSmellDetector = TestSmellDetector.createTestSmellDetector();
		TestFile tempFile;
		try {
			tempFile = testSmellDetector.detectSmells(testFile);
			paths = new SimpleEntry<String, String>(testFile.getProductionFilePath(),testFile.getTestFilePath());
			for (AbstractSmell smell : tempFile.getTestSmells()) {
				try {
					if (smell.getHasSmell()) {
						smells.add(smell.getSmellName());
					}
				} catch (NullPointerException e) {
	
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets production file paths.
	 * @return
	 */
	public String[] getProdFilePath() {
		String[] tempProdFilePaths = new String[smells.size()];
		String prodFile = null;
		if (paths != null)
			if (paths.getKey() != null)
				prodFile = paths.getKey();
			else return null;
		else return null;
		for (int i = 0; i < tempProdFilePaths.length; i++) {
			tempProdFilePaths[i] =  prodFile;
		}
		return tempProdFilePaths;
	}
	
	/**
	 * Gets test file paths.
	 * @return
	 */
	public String[] getTestFilePath() {
		String[] tempTestFilePaths = new String[smells.size()];
		String tempTestFile = null;
		if (paths != null )
			if (paths.getKey() != null)
				tempTestFile = paths.getValue();
			else return null;
		else return null;
		for (int i = 0; i < tempTestFilePaths.length; i++) {
			tempTestFilePaths[i] = tempTestFile;
		}
		return tempTestFilePaths;
	}
	
	/**
	 * Gets test smell names.
	 * @return
	 */
	public String[] getSmellNames() {
		String[] tempSmellNames = new String[smells.size()];
		for (int i = 0; i < smells.size(); i++) {
			tempSmellNames[i] = smells.get(i);
		}
		return tempSmellNames;
	}
}
