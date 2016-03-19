/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2005,2007 The Authors.  See http://www.simbrain.net/credits
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.simbrain.network.gui.dialogs.neuron.rule_panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.gui.dialogs.neuron.NoiseGeneratorPanel;
import org.simbrain.network.neuron_update_rules.IntegrateAndFireRule;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;
import org.simbrain.util.randomizer.Randomizer;
import org.simbrain.util.widgets.TristateDropDown;

/**
 * <b>IntegrateAndFireNeuronPanel</b>.
 */
public class IntegrateAndFireRulePanel extends AbstractNeuronRulePanel {

    /** Tabbed pane. */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Main tab. */
    private LabelledItemPanel mainTab = new LabelledItemPanel();

    /** Time constant field. */
    private JTextField tfTimeConstant = new JTextField();

    /** Threshold field. */
    private JTextField tfThreshold = new JTextField();

    /** Reset field. */
    private JTextField tfReset = new JTextField();

    /** Resistance field. */
    private JTextField tfResistance = new JTextField();

    /** Resting potential field. */
    private JTextField tfRestingPotential = new JTextField();

    /** Background current field. */
    private JTextField tfBackgroundCurrent = new JTextField();

    /** Random tab. */
    private NoiseGeneratorPanel randTab = new NoiseGeneratorPanel();

    /** Add noise combo box. */
    private TristateDropDown isAddNoise = new TristateDropDown();

    /** A reference to the neuron update rule being edited. */
    private static final IntegrateAndFireRule prototypeRule =
        new IntegrateAndFireRule();

    /**
     * Creates a new instance of the integrate and fire neuron panel.
     */
    public IntegrateAndFireRulePanel() {
        super();
        this.add(tabbedPane);
        mainTab.addItem("Threshold (mV)", tfThreshold);
        mainTab.addItem("Resting potential (mV)", tfRestingPotential);
        mainTab.addItem("Reset potential (mV)", tfReset);
        mainTab.addItem("Resistance (M\u03A9)", tfResistance);
        mainTab.addItem("Background Current (nA)", tfBackgroundCurrent);
        mainTab.addItem("Time constant (ms)", tfTimeConstant);
        mainTab.addItem("Add noise", isAddNoise);
        tabbedPane.add(mainTab, "Main");
        tabbedPane.add(randTab, "Noise");
    }

    /**
     * Populate fields with default data.
     */
    public void fillDefaultValues() {
        tfRestingPotential.setText(Double.toString(prototypeRule
            .getRestingPotential()));
        tfResistance.setText(Double.toString(prototypeRule.getResistance()));
        tfReset.setText(Double.toString(prototypeRule.getResetPotential()));
        tfThreshold.setText(Double.toString(prototypeRule.getThreshold()));
        tfBackgroundCurrent.setText(Double.toString(prototypeRule
            .getBackgroundCurrent()));
        tfTimeConstant
            .setText(Double.toString(prototypeRule.getTimeConstant()));
        isAddNoise.setSelected(prototypeRule.getAddNoise());
        randTab.fillDefaultValues();
    }

    /**
     * {@inheritDoc}
     */
    protected IntegrateAndFireRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

}
