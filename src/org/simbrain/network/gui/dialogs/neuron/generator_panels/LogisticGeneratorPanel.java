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

import java.util.List;

import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.neuron_update_rules.activity_generators.LogisticRule;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.SimbrainConstants;

/**
 * <b>LogisticNeuronPanel</b> TODO: Work into new Input Generator Framework,
 * currently no implementation.
 */
public class LogisticGeneratorPanel extends AbstractNeuronRulePanel {

    /** Growth rate field. */
    private JTextField tfGrowthRate = new JTextField();

    /** Main panel. */
    private LabelledItemPanel mainPanel = new LabelledItemPanel();

    // TODO:Lists?
    /** A reference to the neuron rule being edited. */
    private LogisticRule neuronRef = new LogisticRule();

    /**
     * Creates an instance of this panel.
     */
    public LogisticGeneratorPanel() {
        super();
        mainPanel.addItem("Growth Rate", tfGrowthRate);
        add(mainPanel);
        // this.addBottomText("<html>Note 1: This is not a sigmoidal logistic
        // function. <p>"
        // + "For that, set update rule to sigmoidal.<p> "
        // + " Note 2: for chaos, try growth rates between 3.6 and 4</html>");
    }

    /**
     * Populate fields with current data.
     */
    public void fillFieldValues() {
        tfGrowthRate.setText(Double.toString(neuronRef.getGrowthRate()));


    }

    /**
     * Populate fields with default data.
     */
    public void fillDefaultValues() {
        LogisticRule neuronRef = new LogisticRule();
        tfGrowthRate.setText(Double.toString(neuronRef.getGrowthRate()));
    }

    /**
     * Called externally when the dialog is closed, to commit any changes made.
     */
    public void commitChanges() {

        if (!tfGrowthRate.getText().equals(SimbrainConstants.NULL_STRING)) {
            neuronRef.setGrowthRate(Double.parseDouble(tfGrowthRate.getText()));
        }
    }

    @Override
    public void commitChanges(Neuron neuron) {
        if (neuron.getUpdateRule() instanceof LogisticRule) {
            neuronRef = (LogisticRule) neuron.getUpdateRule();
        } else {
            neuron.setUpdateRule(neuronRef);
        }
        if (!tfGrowthRate.getText().equals(SimbrainConstants.NULL_STRING)) {
            neuronRef.setGrowthRate(Double.parseDouble(tfGrowthRate.getText()));
        }
    }

    @Override
    public void commitChanges(List<Neuron> neurons) {

        // Firing Probability
        if (!tfGrowthRate.getText()
                .equals(SimbrainConstants.NULL_STRING))
            neuronRef.setGrowthRate(Double
                    .parseDouble(tfGrowthRate.getText()));

        for (Neuron n : neurons) {
            n.setUpdateRule(neuronRef);
        }

    }

    @Override
    public void fillFieldValues(List<NeuronUpdateRule> ruleList) {
        // Handle consistency of multiple selections
        if (!NetworkUtils.isConsistent(ruleList, LogisticRule.class,
                "getGrowthRate")) {
            tfGrowthRate.setText(SimbrainConstants.NULL_STRING);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NeuronUpdateRule getPrototypeRule() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void writeValuesToRules(List<Neuron> neurons) {
        // TODO Auto-generated method stub

    }
}
