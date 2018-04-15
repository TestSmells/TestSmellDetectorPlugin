package main.java.testsmell.plugin.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import main.java.testsmell.TestFile;

/**
 * A handler class for the the test smells plugin.
 * 
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class TSmellsHandler extends AbstractHandler {
	private ArrayList<IResource> testPaths = new ArrayList<IResource>();
	private HashMap<IResource, IResource> paths = new HashMap<>();
	private IProject project;
	private List<TestFile> testFiles = new ArrayList<TestFile>();
	
	@Override
	/**
	 * Launches the Test Smells Detector view when a project is selected and the
	 * plugin icon is activated.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		try {
			project = getCurrentProject(event);
			getProjectInfo(project);
		} catch (CoreException | NullPointerException ex) {
			ex.printStackTrace();
			MessageDialog.openError(activeShell, "TestSmellDetector", "Please select a project");
			return null;
		}
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			TSmellsDetectionCollection colllectionObj = TSmellsDetectionCollection.getInstance();
			colllectionObj.addNewDetections(testFiles);
			TSmellsDetection detect = colllectionObj.getNextDetection();
			while (detect != null)
			{
				detect.detectSmells();
				detect = colllectionObj.getNextDetection();
			}
			colllectionObj.resetNextIndexVal();
			page.showView("TestSmellsDetector.view");

		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the current project that is selected.
	 * 
	 * @param e
	 * @return
	 */
	private IProject getCurrentProject(ExecutionEvent e) {
		ISelection sel = HandlerUtil.getCurrentSelection(e);

		if (sel instanceof IStructuredSelection) {
			Object selected = ((IStructuredSelection) sel).getFirstElement();

			IResource resource = (IResource) Platform.getAdapterManager().getAdapter(selected, IResource.class);

			if (resource != null) {
				IProject project = resource.getProject();
				return project;
			}
		}
		return null;
	}

	/**
	 * Gets information about the given project.
	 * 
	 * @param project
	 * @throws CoreException
	 * @throws JavaModelException
	 */
	private void getProjectInfo(IProject project) throws CoreException, JavaModelException {
		System.out.println("Working in project " + project.getName());
		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
			IJavaProject javaProject = JavaCore.create(project);
			IPackageFragment[] packages = javaProject.getPackageFragments();
			for (IPackageFragment pkg : packages) {
				if (pkg.getKind() == IPackageFragmentRoot.K_SOURCE) {
					createTestFiles(pkg);
				}
			}
			if (testPaths.size() > 0) {
				try {
					getProdFiles();

					TestFile testFile;
					
					for (Entry<IResource, IResource> fullPaths : paths.entrySet()) {
						String prodFilePath = fullPaths.getKey().getRawLocation().toString();
						String testFilePath = fullPaths.getValue().getRawLocation().toString();
						testFile = new TestFile(project.getName(), testFilePath, prodFilePath);
						testFiles.add(testFile);
				}
				} catch (NullPointerException e) {
					Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
					String errorStr = "Recheck production and test file names. Below are the test file/production file mappings that triggered the exception: \n\n";
					for (Entry<IResource, IResource> fullPaths : paths.entrySet())
					{
						if (fullPaths.getKey() == null)
						{
							errorStr += "Production File: null ,";
						}
						else 
						{
							errorStr += "Production File: " + fullPaths.getKey().getRawLocation().toString() + " ,";
						}
						
						if (fullPaths.getValue() == null)
						{
							errorStr += "Test File: null ,";
						}
						else 
						{
							errorStr += "Test File: " + fullPaths.getValue().getRawLocation().toString() + " \n\n"; 
						}
							
					}
					MessageDialog.openError(activeShell, "Failed to load files" , errorStr );
				}
			}
		}
	}

	/**
	 * Create test files that can be analyzed for smells using both production and
	 * test files.
	 * 
	 * @param pkg
	 * @throws JavaModelException
	 */
	private void createTestFiles(IPackageFragment pkg) throws JavaModelException {
		for (ICompilationUnit unit : pkg.getCompilationUnits()) {
			testPaths = getTestFiles(unit);
		}
	}

	/**
	 * Gets the production file paths.
	 * 
	 * @param unit
	 * @return
	 * @throws JavaModelException
	 */
	private void getProdFiles() throws JavaModelException {
		HashMap<String, IResource> possibleProdFileNames = new HashMap<String, IResource>();
		HashMap<String, IResource> potentialProdFiles = new HashMap<>();
		for (IResource test : testPaths) {

			String fileName = test.getName();
			int prefixLength = "test".length();
			String prodFileName = fileName.substring(prefixLength, fileName.length());
			possibleProdFileNames.put(prodFileName, test);
		}

		IContainer container = containerForPath(project.getFullPath());
		try {
			potentialProdFiles = findJavaFiles(container);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		for (Entry<String, IResource> possibleNames : possibleProdFileNames.entrySet()) {
			String key = possibleNames.getKey();
			IResource prodIRes = potentialProdFiles.get(key);
			IResource testIRes = possibleNames.getValue();
			paths.put(prodIRes, testIRes);
		}
	}

	/**
	 * Gets the container for a given path.
	 * 
	 * @param path
	 * @return
	 */
	private IContainer containerForPath(IPath path) {
		if (path.segmentCount() == 1) {
			return ResourcesPlugin.getWorkspace().getRoot().getProject(path.segment(0));
		} else {
			return ResourcesPlugin.getWorkspace().getRoot().getFolder(path);
		}
	}

	/**
	 * Finds all .java file paths in a given container.
	 * 
	 * @param container
	 * @return
	 * @throws CoreException
	 */
	private HashMap<String, IResource> findJavaFiles(IContainer container) throws CoreException {
		IResource[] members = container.members();
		String path;
		HashMap<String, IResource> paths = new HashMap<String, IResource>();
		for (IResource member : members) {
			HashMap<String, IResource> findProdFiles = new HashMap<String, IResource>();

			if (member instanceof IContainer) {
				findProdFiles = findJavaFiles((IContainer) member);
				if (!findProdFiles.isEmpty()) {
					paths.putAll(findProdFiles);
				}
			} else if (member instanceof IFile) {
				path = member.getRawLocation().makeAbsolute().toString();
				if (path.substring(path.lastIndexOf("."), path.length()).toLowerCase().equals(".java")) {
					paths.put(path.substring(path.lastIndexOf("/") + 1, path.length()), member);
				} else {
					continue;
				}
			}
		}
		return paths;
	}

	/**
	 * Gets the test file paths by checking if test methods exist in a file.
	 * 
	 * @param unit
	 * @return
	 * @throws JavaModelException
	 */
	@SuppressWarnings("deprecation")
	private ArrayList<IResource> getTestFiles(ICompilationUnit unit) throws JavaModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.accept(new ASTVisitor() {
			public boolean visit(MethodDeclaration node) {
				if (node.getName().getIdentifier().startsWith("test")
						|| node.getName().getIdentifier().startsWith("Test")
								&& !testPaths.contains(unit.getResource())) {
					testPaths.add(unit.getResource());
					return true;
				}
				return false;
			}
		});
		return testPaths;
	}
}