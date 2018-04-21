package main.java.testsmell.plugin.views;

import java.io.FileNotFoundException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

/**
 * View class that launches the TSmellsTableViewer class in a workbench view.
 * The view is a wrapper for the TSmellsTableViewer class. It handles the
 * Selection event for the close button.
 */
public class TSmellsView extends ViewPart {
	private TSmellsTableViewer viewer;
	public TSmellsView() {}

	/**
	 * Creates the viewer and initializes it.
	 */
	public void createPartControl(Composite parent) {
		try {
			viewer = new TSmellsTableViewer(parent);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Passes the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * Handles a 'close' event by disposing of the view.
	 */
	public void handleDispose() {
		this.getSite().getPage().hideView(this);
	}
}