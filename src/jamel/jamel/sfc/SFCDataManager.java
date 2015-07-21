package jamel.jamel.sfc;

import java.awt.Component;
import java.io.File;

import org.w3c.dom.Element;

import jamel.basic.Circuit;
import jamel.basic.data.BasicDataManager;
import jamel.basic.util.InitializationException;
import jamel.basic.util.Timer;

/**
 * A basic data manager with extensions to check stock-flow consistency.
 */
public class SFCDataManager extends BasicDataManager {

	/** The balance sheet matrix. */
	final private BalanceSheetMatrix balanceSheetMatrix;
	
	/** The circuit. */
	private final Circuit circuit;

	/** The data validator. */
	final private DataValidator dataValidator;

	/**
	 * Creates a new data manager.
	 * @param circuit the circuit.
	 * @param settings the settings.
	 * @param timer the timer.
	 * @param path the path to the scenario file.
	 * @param name the name of the scenario file.
	 * @throws InitializationException If something goes wrong.
	 */
	public SFCDataManager(Circuit circuit, Element settings, Timer timer, String path, String name) throws InitializationException {
		super(settings,timer,path,name);
		this.circuit = circuit;
		this.balanceSheetMatrix = getNewBalanceSheetMatrix(settings,timer,path);
		this.dataValidator = this.getNewDataValidator(settings, timer, path);
	}
	
	/**
	 * Creates and returns a new balance sheet matrix.
	 * @param settings the settings.
	 * @param timer the timer.
	 * @param path the path to the scenario file.
	 * @return the new balance sheet matrix.
	 * @throws InitializationException If something goes wrong.
	 */
	private BalanceSheetMatrix getNewBalanceSheetMatrix(Element settings, Timer timer, String path) throws InitializationException {		
		final BalanceSheetMatrix balanceSheetMatrix;
		final String fileName = settings.getAttribute("sfcMatrixConfigFile");
		if ("".equals(fileName)) {
			throw new InitializationException("Missing attribute: sfcMatrixConfigFile");
		}
		final File file = new File(path+"/"+fileName);
		balanceSheetMatrix = new BasicBalanceSheetMatrix(file,timer,macroDatabase);
		return balanceSheetMatrix;
	}

	/**
	 * Creates and returns a new data validator.
	 * @param settings the settings.
	 * @param timer the timer.
	 * @param path the path to the scenario file.
	 * @return a new data validator.
	 * @throws InitializationException If something goes wrong.
	 */
	private DataValidator getNewDataValidator(Element settings, Timer timer, String path) throws InitializationException {
		DataValidator result;
		final String fileName = settings.getAttribute("validationConfigFile");
		if ("".equals(fileName)) {
			throw new InitializationException("Missing attribute: validationConfigFile");
		}
		final File file = new File(path+"/"+fileName);
		result = new BasicDataValidator(file,timer,this.macroDatabase);
		return result;
	}

	@Override
	public Component[] getPanelList() {
		final Component[] chartPanels = this.chartManager.getPanelList();
		final Component[] panels = new Component[chartPanels.length+2];
		int i=0;
		for (;i<chartPanels.length;i++) {
			panels[i]=chartPanels[i];
		}
		panels[i]=this.balanceSheetMatrix.getPanel();
		panels[i+1]=this.dataValidator.getPanel();
		return panels;
	}

	@Override
	public void update() {
		this.updateSeries();
		this.balanceSheetMatrix.update();
		final boolean isConsistent = this.dataValidator.checkConsistency();
		if (!isConsistent) {
			final String name = this.dataValidator.getName();
			circuit.warning("Inconsistency","Please check the "+name+" tab for more details");
		}
	}

}

// ***
