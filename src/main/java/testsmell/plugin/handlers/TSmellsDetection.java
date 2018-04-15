package main.java.testsmell.plugin.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import main.java.testsmell.AbstractSmell;
import main.java.testsmell.TestFile;
import main.java.testsmell.TestSmellDetector;

public class TSmellsDetection {
	
	private HashMap<String, String> paths = new HashMap<>();
	private HashMap<String, Boolean> smells = new HashMap<String, Boolean>();
	private static TSmellsDetection detectionObj;
	private static Collection<TestFile> testFiles;

	private TSmellsDetection(Collection<TestFile> t) {
		testFiles = t;
	}

	/**
	 * Create a static method to get instance.
	 */
	public static TSmellsDetection getInstance(Collection<TestFile> t) {
		if (detectionObj == null) {
			detectionObj = new TSmellsDetection(t);
		}
		return detectionObj;
	}

	/**
	 * Create a static method to get instance.
	 */
	public static TSmellsDetection getInstance() {
		return detectionObj;
	}

	public void detectSmells() {
		TestSmellDetector testSmellDetector = TestSmellDetector.createTestSmellDetector();

		TestFile tempFile;
		for (TestFile file : testFiles) {
			try {
				tempFile = testSmellDetector.detectSmells(file);
				paths.put(file.getProductionFilePath(), file.getTestFilePath());
				for (AbstractSmell smell : tempFile.getTestSmells()) {
					try {
						smells.put(smell.getSmellName(), smell.getHasSmell());
					} catch (NullPointerException e) {

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String[] getProdFilePath() {
		String[] tempProdFilePaths = new String[paths.keySet().size()];
		Object[] prodFileKeySet = paths.keySet().toArray();
		for (int i = 0; i < tempProdFilePaths.length; i++) {
			tempProdFilePaths[i] = (String) prodFileKeySet[i];
		}
		return tempProdFilePaths;
	}

	public String[] getTestFilePath() {
		String[] tempTestFilePaths = new String[paths.values().size()];
		Object[] tempTestFileValues = paths.values().toArray();
		for (int i = 0; i < tempTestFilePaths.length; i++) {
			tempTestFilePaths[i] = (String) tempTestFileValues[i];
		}
		return tempTestFilePaths;
	}

	public String[] getSmellNames() {
		String[] tempSmellNames = new String[smells.keySet().size()];
		Object[] tempSmellNamesKeySet = smells.keySet().toArray();
		for (int i = 0; i < tempSmellNames.length; i++) {
			tempSmellNames[i] = (String) tempSmellNamesKeySet[i];
		}
		return tempSmellNames;
	}

	public String[] hasSmells() {
		String[] tempHasSmells = new String[smells.values().size()];
		Object[] tempHasSmellsValues = smells.values().toArray();
		for (int i = 0; i < tempHasSmells.length; i++) {
			tempHasSmells[i] = ((Boolean) tempHasSmellsValues[i]).toString();
		}
		return tempHasSmells;
	}
}
