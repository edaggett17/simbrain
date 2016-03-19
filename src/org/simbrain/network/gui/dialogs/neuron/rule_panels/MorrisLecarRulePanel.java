package org.simbrain.network.gui.dialogs.neuron.rule_panels;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.gui.dialogs.neuron.NoiseGeneratorPanel;
import org.simbrain.network.neuron_update_rules.FitzhughNagumo;
import org.simbrain.network.neuron_update_rules.MorrisLecarRule;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.ParameterGetter;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;
import org.simbrain.util.widgets.TristateDropDown;

import javax.swing.*;

import java.util.Collections;
import java.util.List;

/**
 * @author Amanda Pandey <amanda.pandey@gmail.com>
 */
public class MorrisLecarRulePanel extends AbstractNeuronRulePanel{

    /** Calcium channel conductance (micro Siemens/cm^2). */
    private JTextField tfG_Ca = new JTextField();

    /** Potassium channel conductance (micro Siemens/cm^2). */
    private JTextField tfG_K = new JTextField();

    /** Leak conductance (micro Siemens/cm^2). */
    private JTextField tfG_L = new JTextField();

    /** Resting potential calcium (mV). */
    private JTextField tfVRest_Ca = new JTextField();

    /** Resting potential potassium (mV). */
    private JTextField tfvRest_k = new JTextField();

    /** Resting potential for leak current (mV). */
    private JTextField tfVRest_L = new JTextField();

    /** Membrane capacitance per unit area (micro Farads/cm^2). */
    private JTextField tfCMembrane = new JTextField();

    /** Membrane voltage constant 1. */
    private JTextField tfV_M1 = new JTextField();

    /** Membrane voltage constant 2. */
    private JTextField tfV_M2 = new JTextField();

    /** Potassium channel constant 1. */
    private JTextField tfV_W1 = new JTextField();

    /** Potassium channel constant 2. */
    private JTextField tfV_W2 = new JTextField();

    /** Fraction of open potassium channels. */
    private JTextField tfW_K = new JTextField();

    /** Potassium channel time constant/decay rate (s^-1). */
    private JTextField tfPhi = new JTextField();

    /** Background current (mA). */
    private JTextField tfI_Bg = new JTextField();

    /** Threshold for neurotransmitter release (mV) */
    private JTextField tfThreshold = new JTextField();

    private TristateDropDown isAddNoise = new TristateDropDown();

    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Random tab. */
    private NoiseGeneratorPanel randTab = new NoiseGeneratorPanel();
    
    /** A reference to the neuron update rule being edited. */
    private static final MorrisLecarRule prototypeRule = new MorrisLecarRule();

    public MorrisLecarRulePanel() {
        super();
        this.add(tabbedPane);
        LabelledItemPanel cellPanel = new LabelledItemPanel();
        cellPanel.addItem("Capacitance (\u03BCF/cm\u00B2)", tfCMembrane);
        cellPanel.addItem("Voltage const. 1", tfV_M1);
        cellPanel.addItem("Voltage const. 2", tfV_M2);
        cellPanel.addItem("Threshold (mV)", tfThreshold);
        cellPanel.addItem("Background current (nA)", tfI_Bg);
        cellPanel.addItem("Add noise: ", isAddNoise);
        
        LabelledItemPanel ionPanel = new LabelledItemPanel();
        ionPanel.addItem("Ca\u00B2\u207A conductance (\u03BCS/cm\u00B2)",
                tfG_Ca);
        ionPanel.addItem("K\u207A conductance (\u03BCS/cm\u00B2)", tfG_K);
        ionPanel.addItem("Leak conductance (\u03BCS/cm\u00B2)", tfG_L);
        ionPanel.addItem("Ca\u00B2\u207A equilibrium (mV)", tfVRest_Ca);
        ionPanel.addItem("K\u207A equilibrium (mV)", tfvRest_k);
        ionPanel.addItem("Leak equilibrium (mV)", tfVRest_L);

        LabelledItemPanel potas = new LabelledItemPanel();
        potas.addItem("K\u207A const. 1", tfV_W1);
        potas.addItem("K\u207A const. 2", tfV_W2);
        potas.addItem("Open K\u207A channels", tfW_K);
        potas.addItem("K\u207A \u03C6", tfPhi);

        tabbedPane.add(cellPanel, "Membrane Properties");
        tabbedPane.add(ionPanel, "Ion Properties");
        tabbedPane.add(potas, "K\u207A consts.");
        tabbedPane.add(randTab, "Noise");
    }

    @Override
    public void fillDefaultValues() {
        tfG_Ca.setText(Double.toString(prototypeRule.getG_Ca()));
        tfG_K.setText(Double.toString(prototypeRule.getG_K()));
        tfG_L.setText(Double.toString(prototypeRule.getG_L()));
        tfVRest_Ca.setText(Double.toString(prototypeRule.getvRest_Ca()));
        tfvRest_k.setText(Double.toString(prototypeRule.getvRest_k()));
        tfVRest_L.setText(Double.toString(prototypeRule.getvRest_L()));
        tfCMembrane.setText(Double.toString(prototypeRule.getcMembrane()));
        tfV_M1.setText(Double.toString(prototypeRule.getV_m1()));
        tfV_M2.setText(Double.toString(prototypeRule.getV_m2()));
        tfV_W1.setText(Double.toString(prototypeRule.getV_w1()));
        tfV_W2.setText(Double.toString(prototypeRule.getV_w2()));
        tfW_K.setText(Double.toString(prototypeRule.getW_K()));
        tfPhi.setText(Double.toString(prototypeRule.getPhi()));
        tfI_Bg.setText(Double.toString(prototypeRule.getI_bg()));
        tfThreshold.setText(Double.toString(prototypeRule.getThreshold()));
        randTab.fillDefaultValues();
    }

    @Override
    protected NeuronUpdateRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }
}
