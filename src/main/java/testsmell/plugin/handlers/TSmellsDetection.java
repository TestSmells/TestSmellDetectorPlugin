package main.java.testsmell.plugin.handlers;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import main.java.testsmell.AbstractSmell;
import main.java.testsmell.TestFile;
import main.java.testsmell.TestSmellDetector;

public class TSmellsDetection {
	
	private SimpleEntry<String, String> paths;
	private ArrayList<String> smells = new ArrayList<String>();
	private TestFile testFile;

	protected TSmellsDetection(TestFile tf) {
		testFile = tf;
	}

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
//		for (TestFile file : testFiles) {
//			try {
//				tempFile = testSmellDetector.detectSmells(file);
//				paths.put(file.getProductionFilePath(), file.getTestFilePath());
//				for (AbstractSmell smell : tempFile.getTestSmells()) {
//					try {
//						smells.put(smell.getSmellName(), smell.getHasSmell());
//					} catch (NullPointerException e) {
//
//					}
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	public String[] getProdFilePath() {
		String[] tempProdFilePaths = new String[smells.size()];
		String prodFile = paths.getKey();
		for (int i = 0; i < tempProdFilePaths.length; i++) {
			tempProdFilePaths[i] = (String) prodFile;
		}
		return tempProdFilePaths;
	}

	public String[] getTestFilePath() {
		String[] tempTestFilePaths = new String[smells.size()];
		String tempTestFile = paths.getValue();
		for (int i = 0; i < tempTestFilePaths.length; i++) {
			tempTestFilePaths[i] = tempTestFile;
		}
		return tempTestFilePaths;
	}

	public String[] getSmellNames() {
		String[] tempSmellNames = new String[smells.size()];
		for (int i = 0; i < smells.size(); i++)
		{
			tempSmellNames[i] = smells.get(i);
		}
		return tempSmellNames;
	}

//	public String[] hasSmells() {
//		String[] tempHasSmells = new String[smells.size()];
//		Object[] tempHasSmellsValues = smells.values().toArray();
//		for (int i = 0; i < tempHasSmells.length; i++) {
//			tempHasSmells[i] = ((Boolean) tempHasSmellsValues[i]).toString();
//		}
//		return tempHasSmells;
//	}
}
