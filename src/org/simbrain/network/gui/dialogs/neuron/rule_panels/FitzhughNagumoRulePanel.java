package org.simbrain.network.gui.dialogs.neuron.rule_panels;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.gui.dialogs.neuron.NoiseGeneratorPanel;
import org.simbrain.network.neuron_update_rules.FitzhughNagumo;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.widgets.TristateDropDown;

/**
 * @author Amanda Pandey <amanda.pandey@gmail.com>
 */
public class FitzhughNagumoRulePanel extends AbstractNeuronRulePanel {

    /** A variable governs overall rate of recovery equation. */
    private JTextField tfA;

    /** Influence of V on recovery variable. */
    private JTextField tfB;

    /** Influence of W on future values of W. */
    private JTextField tfC;

    /** Constant background current. KEEP */
    private JTextField tfIbg;

    /** Threshold value to signal a spike. KEEP */
    private JTextField tfThreshold;

    private TristateDropDown isAddNoise;
    private JTabbedPane tabbedPane = new JTabbedPane();
    
    /** Main tab. */
    private LabelledItemPanel mainTab = new LabelledItemPanel();

    /** Random tab. */
    private NoiseGeneratorPanel randTab = new NoiseGeneratorPanel();

    /** A reference to the neuron update rule being edited. */
    private static final FitzhughNagumo prototypeRule = new FitzhughNagumo();

    /**
     * Construct panel.
     */
    public FitzhughNagumoRulePanel() {
        super();
        this.add(tabbedPane);
        tfA = registerTextField((r) -> ((FitzhughNagumo) r).getA(),
                (r, val) -> ((FitzhughNagumo) r).setA((double) val));
        tfB = registerTextField((r) -> ((FitzhughNagumo) r).getB(),
                (r, val) -> ((FitzhughNagumo) r).setB((double) val));
        tfC = registerTextField((r) -> ((FitzhughNagumo) r).getC(),
                (r, val) -> ((FitzhughNagumo) r).setC((double) val));
        tfIbg = registerTextField((r) -> ((FitzhughNagumo) r).getiBg(),
                (r, val) -> ((FitzhughNagumo) r).setiBg((double) val));
        tfThreshold = registerTextField(
                (r) -> ((FitzhughNagumo) r).getThreshold(),
                (r, val) -> ((FitzhughNagumo) r).setThreshold((double) val));
        isAddNoise = registerTriStateDropDown(
                (r) -> ((FitzhughNagumo) r).getAddNoise(),
                (r, val) -> ((FitzhughNagumo) r).setAddNoise((Boolean) val));
        mainTab.addItem("A (Recovery Rate): ", tfA);
        mainTab.addItem("B (Rec. Voltage Dependence): ", tfB);
        mainTab.addItem("C (Rec. Self Dependence): ", tfC);
        mainTab.addItem("Background Current (nA)", tfIbg);
        mainTab.addItem("Spike threshold", tfThreshold);
        mainTab.addItem("Add noise: ", isAddNoise);
        tabbedPane.add(mainTab, "Properties");

        noisePanel = new NoiseGeneratorPanel();
        tabbedPane.add(noisePanel, "Noise");
    }

    @Override
    protected final NeuronUpdateRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

}
