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
import org.simbrain.network.neuron_update_rules.IzhikevichRule;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;
import org.simbrain.util.randomizer.Randomizer;
import org.simbrain.util.widgets.TristateDropDown;

/**
 * <b>IzhikevichNeuronPanel</b>.
 */
public class IzhikevichRulePanel extends AbstractNeuronRulePanel {

    /** A field. */
    private JTextField tfA = new JTextField();

    /** B field. */
    private JTextField tfB = new JTextField();

    /** C field. */
    private JTextField tfC = new JTextField();

    /** D field. */
    private JTextField tfD = new JTextField();
    
    /** A text field for entering constant background current value. */
    private JTextField tfIBg = new JTextField();

    /** Add noise combo box. */
    private TristateDropDown tsNoise = new TristateDropDown();

    /** Tabbed pane. */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Main tab. */
    private LabelledItemPanel mainTab = new LabelledItemPanel();

    /** Random tab. */
    private NoiseGeneratorPanel randTab = new NoiseGeneratorPanel();

    /** A reference to the neuron update rule being edited. */
    private static final IzhikevichRule prototypeRule = new IzhikevichRule();

    /**
     * Creates an instance of this panel.
     */
    public IzhikevichRulePanel() {
        super();
        this.add(tabbedPane);
        mainTab.addItem("A", tfA);
        mainTab.addItem("B", tfB);
        mainTab.addItem("C", tfC);
        mainTab.addItem("D", tfD);
        mainTab.addItem("Ibg", tfIBg);
        mainTab.addItem("Add noise", tsNoise);
        tabbedPane.add(mainTab, "Main");
        tabbedPane.add(randTab, "Noise");
        this.addBottomText("<html>For a list of useful parameter settings<p>"
                + "press the \"Help\" Button.</html>");
    }

    /**
     * Populate fields with default data.
     */
    public void fillDefaultValues() {
        tfA.setText(Double.toString(prototypeRule.getA()));
        tfB.setText(Double.toString(prototypeRule.getB()));
        tfC.setText(Double.toString(prototypeRule.getC()));
        tfD.setText(Double.toString(prototypeRule.getD()));
        tfIBg.setText(Double.toString(prototypeRule.getiBg()));
        tsNoise.setSelected(prototypeRule.getAddNoise());
        randTab.fillDefaultValues();
    }

    /**
     * {@inheritDoc}
     */
    protected IzhikevichRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

}
