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
    
    /** Influence of V on recovery variable */
    private JTextField tfB;
    
    /** Influence of W on future values of W */
    private JTextField tfC;

    /** Constant background current. KEEP */
    private JTextField tfIbg;

    /** Threshold value to signal a spike. KEEP */
    private JTextField tfThreshold;

    private TristateDropDown isAddNoise;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private LabelledItemPanel mainTab = new LabelledItemPanel();
    /** Random tab. */
    private NoiseGeneratorPanel randTab = new NoiseGeneratorPanel();
    
    /** A reference to the neuron update rule being edited. */
    private static final FitzhughNagumo prototypeRule = new FitzhughNagumo();

    public FitzhughNagumoRulePanel() {
        super();
        this.add(tabbedPane);
        tfA = (JTextField) registerProperty(Double.class,
                (r) -> ((FitzhughNagumo) r).getA(),
                (r, val) -> ((FitzhughNagumo) r).setA((double) val));
        tfB = (JTextField) registerProperty(Double.class,
                (r) -> ((FitzhughNagumo) r).getB(),
                (r, val) -> ((FitzhughNagumo) r).setB((double) val));
        tfC = (JTextField) registerProperty(Double.class,
                (r) -> ((FitzhughNagumo) r).getC(),
                (r, val) -> ((FitzhughNagumo) r).setC((double) val));
        tfIbg = (JTextField) registerProperty(Double.class,
                (r) -> ((FitzhughNagumo) r).getiBg(),
                (r, val) -> ((FitzhughNagumo) r).setiBg((double) val));
        tfThreshold = (JTextField) registerProperty(Double.class,
                (r) -> ((FitzhughNagumo) r).getThreshold(),
                (r, val) -> ((FitzhughNagumo) r).setThreshold((double) val));
        isAddNoise = (TristateDropDown) registerProperty(
                Boolean.class, (r) -> ((FitzhughNagumo) r).getAddNoise(),
                (r, val) -> ((FitzhughNagumo) r).setAddNoise((Boolean) val));
        mainTab.addItem("A (Recovery Rate): ", tfA);
        mainTab.addItem("B (Rec. Voltage Dependence): ", tfB);
        mainTab.addItem("C (Rec. Self Dependence): ", tfC);
        mainTab.addItem("Background Current (nA)", tfIbg);
        mainTab.addItem("Spike threshold", tfThreshold);
        mainTab.addItem("Add noise: ", isAddNoise);
        tabbedPane.add(mainTab, "Properties");
        tabbedPane.add(randTab, "Noise");
    }
    
    @Override
    public void fillDefaultValues() {
        tfA.setText(Double.toString(prototypeRule.getA()));
        tfB.setText(Double.toString(prototypeRule.getB()));
        tfC.setText(Double.toString(prototypeRule.getC()));
        tfIbg.setText(Double.toString(prototypeRule.getiBg()));
        tfThreshold.setText(Double.toString(prototypeRule.getThreshold()));
        randTab.fillDefaultValues();
    }

    @Override
    protected NeuronUpdateRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

}
