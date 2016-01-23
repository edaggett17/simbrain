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
package org.simbrain.network.gui.dialogs.neuron.generator_panels;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.neuron_update_rules.LinearRule;
import org.simbrain.network.neuron_update_rules.activity_generators.StochasticRule;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.SimbrainConstants;

/**
 * <b>StochasticNeuronPanel</b>.
 */
public class StochasticGeneratorPanel extends AbstractNeuronRulePanel {

    /** Main panel. */
    private LabelledItemPanel mainPanel = new LabelledItemPanel();

    /** A reference to the neuron update rule being edited. */
    private static final StochasticRule prototypeRule = new StochasticRule();

    /**
     * Creates an instance of this panel.
     *
     */
    public StochasticGeneratorPanel() {
        super();
        add(mainPanel);
        init(StochasticRule.editorList);
        mainPanel.addItem("Firing Probability", componentMap.get("firingProb"));
    }

    @Override
    protected NeuronUpdateRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

    @Override
    public void fillDefaultValues() {
        // TODO Auto-generated method stub   
    }

    @Override
    public void commitChanges(Neuron neuron) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void writeValuesToRules(List<Neuron> neurons) {
        // TODO Auto-generated method stub
        
    }

}
