package main.java.testsmell.plugin.views;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Custom dialog class to display error messages.
 * Overrides functionality from the IconAndMessageDialog class.
 */
public class TSmellDialog extends IconAndMessageDialog {
	private String title;
	private String message;

	/**
	 * Constructor to initialize the dialog with a shell, title, and message.
	 * @param parentShell
	 * @param t
	 * @param msg
	 */
	public TSmellDialog(Shell parentShell, String t, String msg) {
		super(parentShell);
		title = t;
		message = msg;
	}

	/**
	 * Override that creates a dialog with a title.
	 */
	@Override
	public void create() {
		super.create();
		this.getShell().setText(title);
	}

	/**
	 * Override to fill the dialog with an image and message.
	 */
	@Override
	protected Control createMessageArea(Composite parent) {
		Image image = getImage();
		if (image != null) {
			imageLabel = new Label(parent, SWT.NULL);
			image.setBackground(imageLabel.getBackground());
			imageLabel.setImage(image);
			GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BEGINNING).applyTo(imageLabel);
		}
		if (message != null) {
			messageLabel = new Label(parent, getMessageLabelStyle());
			messageLabel.setText(message);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
					.hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH), SWT.DEFAULT)
					.applyTo(messageLabel);
		}
		return parent;
	}
	
	/**
	 * Override to create a dialog area using the parent.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		createMessageArea(parent);
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		composite.setLayoutData(data);
		return composite;
	}
	
	/**
	 * Override to remove the default buttons on the dialog.
	 */
	@Override
	protected Button createButton(Composite arg0, int arg1, String arg2, boolean arg3) {
		return null;
	}

	/**
	 * Override to change the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
	    final Point size = super.getInitialSize();

	    size.x = convertWidthInCharsToPixels(100);
	    return size;
	}
	
	/**
	 * Override to get the error image.
	 */
	@Override
	protected Image getImage() {
		return getErrorImage();
	}
}