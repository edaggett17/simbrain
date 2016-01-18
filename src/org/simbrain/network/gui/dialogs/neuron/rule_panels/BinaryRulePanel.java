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

import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.neuron_update_rules.BinaryRule;
import org.simbrain.network.neuron_update_rules.LinearRule;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;

/**
 * <b>BinaryNeuronPanel</b> creates a dialog for setting preferences of binary
 * neurons.
 */
public class BinaryRulePanel extends AbstractNeuronRulePanel {

    /** Threshold for this neuron. */
    private JTextField tfThreshold = new JTextField();

    /** Ceiling */
    private JTextField tfUpbound = new JTextField();

    /** Floor */
    private JTextField tfLowbound = new JTextField();

    /** Bias for this neuron. */
    private JTextField tfBias = new JTextField();

    /** Main tab for neuron preferences. */
    private LabelledItemPanel mainTab = new LabelledItemPanel();

    /** A reference to the neuron rule being edited. */
    private static final BinaryRule prototypeRule = new BinaryRule();

    /**
     * Creates binary neuron preferences panel.
     */
    public BinaryRulePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        init(BinaryRule.editorList);
        mainTab.addItem("Threshold", componentMap.get("threshold"));
        mainTab.addItem("On Value", componentMap.get("upper"));
        mainTab.addItem("Off Value", componentMap.get("lower"));
        mainTab.addItem("Bias", componentMap.get("bias"));
        mainTab.setAlignmentX(CENTER_ALIGNMENT);
        this.add(mainTab);
    }

    /**
     * Fill field values to default values for binary neuron.
     */
    public void fillDefaultValues() {
        tfThreshold.setText(Double.toString(prototypeRule.getThreshold()));
        tfUpbound.setText(Double.toString(prototypeRule.getUpperBound()));
        tfLowbound.setText(Double.toString(prototypeRule.getLowerBound()));
        tfBias.setText(Double.toString(prototypeRule.getBias()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BinaryRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

    @Override
    public void commitChanges(Neuron neuron) {
        // TODO Auto-generated method stub // to be removed!
        
    }

    @Override
    protected void writeValuesToRules(List<Neuron> neurons) {
        // TODO Auto-generated method stub
        
    }

}
