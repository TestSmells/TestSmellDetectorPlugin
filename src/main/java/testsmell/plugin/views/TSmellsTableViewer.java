package main.java.testsmell.plugin.views;
import java.util.Arrays;


import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * Launches a table viewer with a grid that displays the results for the test smells.
 * Shows all the files, test smells detected, and descriptions of each.
 */

public class TSmellsTableViewer {
	
	/**
	 * Constructor for the table viewer.
	 * @param parent
	 */
	public TSmellsTableViewer(Composite parent) {
		
		this.addChildControls(parent);
	}

	private Table table;
	private TableViewer tableViewer;
	private Button closeButton;
	
	// Set the table column property names
	private final String FILES_COLUMN 			= "files";
	private final String TEST_SMELLS_COLUMN 	= "test smells";
	private final String DETECTED_COLUMN 		= "detected";
	private final String DESCRIPTION_COLUMN 	= "description";

	// Set column names
	private String[] columnNames = new String[] { 
			FILES_COLUMN, 
			TEST_SMELLS_COLUMN,
			DETECTED_COLUMN,
			DESCRIPTION_COLUMN };

	/**
	 * Main method to launch the window.
	 * @param args
	 */
	public static void main(String[] args) {

		Shell shell = new Shell();
		shell.setText("Test Smells Detector");

		// Set layout for shell
		GridLayout layout = new GridLayout();
		shell.setLayout(layout);
		
		// Create a composite to hold the children
		Composite composite = new Composite(shell, SWT.NONE);
		final TSmellsTableViewer tableViewer = new TSmellsTableViewer(composite);
		
		tableViewer.getControl().addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				tableViewer.dispose();			
			}
			
		});

		shell.open();
		tableViewer.run(shell);
	}

	/**
	 * Runs and waits for a close event.
	 * @param shell Instance of Shell
	 */
	private void run(Shell shell) {
		
		// Add a listener for the close button
		closeButton.addSelectionListener(new SelectionAdapter() {
       	
			// Close the view i.e. dispose of the composite's parent
			public void widgetSelected(SelectionEvent e) {
				table.getParent().getParent().dispose();
			}
		});
		
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Releases resources.
	 */
	public void dispose() {		
		tableViewer.getLabelProvider().dispose();
	}

	/**
	 * Creates a new shell, adds the widgets, opens the shell.
	 * @return the shell that was created	 
	 */
	private void addChildControls(Composite composite) {

		// Create a composite to hold the children
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_BOTH);
		composite.setLayoutData (gridData);

		// Set numColumns to 3 for the buttons 
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 4;
		composite.setLayout (layout);

		// Create the table 
		createTable(composite);
		
		// Create and setup the TableViewer
		createTableViewer();
	}

	/**
	 * Creates the table with a grid of columns.
	 * @param parent
	 */
	private void createTable(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
					SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

		table = new Table(parent, style);
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalSpan = 3;
		table.setLayoutData(gridData);		
					
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column = new TableColumn(table, SWT.CENTER, 0);		
		column.setText("File");
		column.setWidth(100);
		
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Test Smell");
		column.setWidth(100);

		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("Detected?");
		column.setWidth(100);
		

		column = new TableColumn(table, SWT.CENTER, 3);
		column.setText("Description");
		column.setWidth(300);
	}

	/**
	 * Creates the TableViewer. 
	 */
	private void createTableViewer() {

		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		
		tableViewer.setColumnProperties(columnNames);
	}

	/**
	 * Closes the window and disposes of resources.
	 */
	public void close() {
		Shell shell = table.getShell();

		if (shell != null && !shell.isDisposed())
			shell.dispose();
	}

	/**
	 * Returns the column names in a collection.
	 * @return List  containing column names
	 */
	public java.util.List<String> getColumnNames() {
		return Arrays.asList(columnNames);
	}

	/**
	 * Gets currently selected item in the table viewer.
	 * @return currently selected item
	 */
	public ISelection getSelection() {
		return tableViewer.getSelection();
	}

	/**
	 * Returns the parent composite.
	 */
	public Control getControl() {
		return table.getParent();
	}

	/**
	 * Returns the 'close' Button.
	 */
	public Button getCloseButton() {
		return closeButton;
	}
}