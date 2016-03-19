package org.simbrain.network.gui.dialogs.neuron.rule_panels;

import java.util.Collections;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.gui.dialogs.neuron.NoiseGeneratorPanel;
import org.simbrain.network.neuron_update_rules.AdExIFRule;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.ParameterGetter;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;
import org.simbrain.util.widgets.TristateDropDown;

public class AdExIFRulePanel extends AbstractNeuronRulePanel {

    /** Excitatory Reversal field. */
    private JTextField tfER = new JTextField();

    /** Inhibitory Reversal field. */
    private JTextField tfIR = new JTextField();

    /** Leak Reversal field. */
    private JTextField tfLR = new JTextField();

    private JTextField tfGeBar = new JTextField();
    
    private JTextField tfGiBar = new JTextField();
    
    private JTextField tfGL = new JTextField();
    
    private JTextField tfCap = new JTextField();

    /** Threshold for output function. */
    private JTextField tfThreshold = new JTextField();
    
    private JTextField tfPeak = new JTextField();
    
    private JTextField tfV_Reset = new JTextField();

    /** Bias for excitatory inputs. */
    private JTextField tfBgCurrent = new JTextField();

    private JTextField tfAdaptTC = new JTextField();
    
    private JTextField tfSlopeFactor = new JTextField();
    
    private JTextField tfAdaptCouplingConst = new JTextField();
    
    private JTextField tfAdaptResetParam = new JTextField();

    /** Tabbed pane. */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Main tab. */
    private LabelledItemPanel mainTab = new LabelledItemPanel();
    
    /** Main tab. */
    private LabelledItemPanel currentTab = new LabelledItemPanel();

    /** Inputs tab. */
    private LabelledItemPanel adaptationTab = new LabelledItemPanel();

    /** Random tab. */
    private NoiseGeneratorPanel randTab = new NoiseGeneratorPanel();

    /** Add noise combo box. */
    private TristateDropDown isAddNoise = new TristateDropDown();
    
    /** A reference to the neuron update rule being edited. */
    private static final AdExIFRule prototypeRule = new AdExIFRule();

    /**
     * Creates an instance of this panel.
     */
    public AdExIFRulePanel() {
        super();
        this.add(tabbedPane);
        mainTab.addItem("Peak Voltage (mV)", tfPeak);
        mainTab.addItem("Threshold Voltage (mV)", tfThreshold);
        mainTab.addItem("Reset Voltage (mV)" , tfV_Reset);
        mainTab.addItem("Capacitance (pF)", tfCap);
        mainTab.addItem("Background Current (nA)", tfBgCurrent);
        mainTab.addItem("Slope Factor", tfSlopeFactor);
        currentTab.addItem("Leak Conductance (nS)", tfGL);
        currentTab.addItem("Max Ex. Conductance (nS)" , tfGeBar);
        currentTab.addItem("Max In. Conductance (nS)", tfGiBar);
        currentTab.addItem("Leak Reversal (mV)", tfLR);
        currentTab.addItem("Excitatory Reversal (mV)", tfER);
        currentTab.addItem("Inhibitory Reversal (mV)", tfIR);
        adaptationTab.addItem("Reset (nA)", tfAdaptResetParam);
        adaptationTab.addItem("Coupling Const.", tfAdaptCouplingConst);
        adaptationTab.addItem("Time Constant (ms)", tfAdaptTC);
        tabbedPane.add(mainTab, "Membrane Voltage");
        tabbedPane.add(currentTab, "Input Currents");
        tabbedPane.add(adaptationTab, "Adaptation");
    }

	@Override
	public void fillDefaultValues() {
		tfER.setText(Double.toString(prototypeRule.getExReversal()));
		tfIR.setText(Double.toString(prototypeRule.getInReversal()));
		tfLR.setText(Double.toString(prototypeRule.getLeakReversal()));
		tfGeBar.setText(Double.toString(prototypeRule.getG_e_bar()));
		tfGiBar.setText(Double.toString(prototypeRule.getG_i_bar()));
		tfGL.setText(Double.toString(prototypeRule.getG_L()));
		tfCap.setText(Double.toString(prototypeRule.getMemCapacitance()));
		tfThreshold.setText(Double.toString(prototypeRule.getV_Th()));
		tfPeak.setText(Double.toString(prototypeRule.getV_Peak()));
		tfV_Reset.setText(Double.toString(prototypeRule.getV_Reset()));
		tfBgCurrent.setText(Double.toString(prototypeRule.getI_bg()));
		tfAdaptTC.setText(Double.toString(prototypeRule.getTauW()));
		tfSlopeFactor.setText(Double.toString(prototypeRule.getSlopeFactor()));
		tfAdaptCouplingConst.setText(Double.toString(prototypeRule.getA()));
		tfAdaptResetParam.setText(Double.toString(prototypeRule.getB()));
	}

	@Override
	protected AdExIFRule getPrototypeRule() {
		return prototypeRule.deepCopy();
	}

}
