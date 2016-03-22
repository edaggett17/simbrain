package org.simbrain.network.gui.dialogs.neuron.rule_panels;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.gui.dialogs.neuron.NoiseGeneratorPanel;
import org.simbrain.network.neuron_update_rules.AdExIFRule;
import org.simbrain.util.LabelledItemPanel;

public class AdExIFRulePanel extends AbstractNeuronRulePanel {

    /** Tabbed pane. */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Main tab. */
    private LabelledItemPanel mainTab = new LabelledItemPanel();

    /** Main tab. */
    private LabelledItemPanel currentTab = new LabelledItemPanel();

    /** Inputs tab. */
    private LabelledItemPanel adaptationTab = new LabelledItemPanel();

    /** A reference to the neuron update rule being edited. */
    private static final AdExIFRule prototypeRule = new AdExIFRule();

    /**
     * Creates an instance of this panel.
     */
    public AdExIFRulePanel() {
        super();
        this.add(tabbedPane);
        JTextField tfPeak = registerTextField(
                (r) -> ((AdExIFRule) r).getV_Peak(),
                (r, val) -> ((AdExIFRule) r).setV_Peak((double) val));
        JTextField tfThreshold = registerTextField(
                (r) -> ((AdExIFRule) r).getV_Th(),
                (r, val) -> ((AdExIFRule) r).setV_Th((double) val));
        JTextField tfV_Reset = registerTextField(
                (r) -> ((AdExIFRule) r).getV_Reset(),
                (r, val) -> ((AdExIFRule) r).setV_Reset((double) val));
        JTextField tfCap = registerTextField(
                (r) -> ((AdExIFRule) r).getMemCapacitance(),
                (r, val) -> ((AdExIFRule) r).setMemCapacitance((double) val));
        JTextField tfBgCurrent = registerTextField(
                (r) -> ((AdExIFRule) r).getI_bg(),
                (r, val) -> ((AdExIFRule) r).setI_bg((double) val));
        JTextField tfSlopeFactor = registerTextField(
                (r) -> ((AdExIFRule) r).getSlopeFactor(),
                (r, val) -> ((AdExIFRule) r).setSlopeFactor((double) val));
        JTextField tfGL = registerTextField((r) -> ((AdExIFRule) r).getG_L(),
                (r, val) -> ((AdExIFRule) r).setG_L((double) val));
        JTextField tfGeBar = registerTextField(
                (r) -> ((AdExIFRule) r).getG_e_bar(),
                (r, val) -> ((AdExIFRule) r).setG_e_bar((double) val));
        JTextField tfGiBar = registerTextField(
                (r) -> ((AdExIFRule) r).getG_i_bar(),
                (r, val) -> ((AdExIFRule) r).setG_i_bar((double) val));
        JTextField tfLR = registerTextField(
                (r) -> ((AdExIFRule) r).getLeakReversal(),
                (r, val) -> ((AdExIFRule) r).setLeakReversal((double) val));
        JTextField tfER = registerTextField(
                (r) -> ((AdExIFRule) r).getExReversal(),
                (r, val) -> ((AdExIFRule) r).setExReversal((double) val));
        JTextField tfIR = registerTextField(
                (r) -> ((AdExIFRule) r).getInReversal(),
                (r, val) -> ((AdExIFRule) r).setInReversal((double) val));
        JTextField tfAdaptResetParam = registerTextField(
                (r) -> ((AdExIFRule) r).getB(),
                (r, val) -> ((AdExIFRule) r).setB((double) val));
        JTextField tfAdaptCouplingConst = registerTextField(
                (r) -> ((AdExIFRule) r).getA(),
                (r, val) -> ((AdExIFRule) r).setA((double) val));
        JTextField tfAdaptTC = registerTextField(
                (r) -> ((AdExIFRule) r).getTauW(),
                (r, val) -> ((AdExIFRule) r).setTauW((double) val));
        mainTab.addItem("Peak Voltage (mV)", tfPeak);
        mainTab.addItem("Threshold Voltage (mV)", tfThreshold);
        mainTab.addItem("Reset Voltage (mV)", tfV_Reset);
        mainTab.addItem("Capacitance (pF)", tfCap);
        mainTab.addItem("Background Current (nA)", tfBgCurrent);
        mainTab.addItem("Slope Factor", tfSlopeFactor);
        mainTab.addItem("Add noise", this.getAddNoise());
        currentTab.addItem("Leak Conductance (nS)", tfGL);
        currentTab.addItem("Max Ex. Conductance (nS)", tfGeBar);
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

        tabbedPane.add(this.getNoisePanel(), "Noise");
    }

    @Override
    protected AdExIFRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

}
