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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.neuron_update_rules.ContinuousSigmoidalRule;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;
import org.simbrain.util.math.SquashingFunction;
import org.simbrain.util.widgets.TristateDropDown;

/**
 *
 * @author Zach Tosi
 *
 */
@SuppressWarnings("serial")
public class ContinuousSigmoidalRulePanel extends AbstractSigmoidalRulePanel {

    /** Time constant field. */
    private JTextField tfTimeConstant = new JTextField();

    private JTextField tfLeakConstant = new JTextField();

    /** A reference to the neuron rule being edited. */
    private static ContinuousSigmoidalRule prototypeRule;

    /**
     * Creates a fully functional continuous sigmoidal rule panel.
     * 
     * @return
     */
    public static ContinuousSigmoidalRulePanel
        createContinuousSigmoidalRulePanel() {
        prototypeRule = new ContinuousSigmoidalRule();
        final ContinuousSigmoidalRulePanel csrp =
            new ContinuousSigmoidalRulePanel();
        csrp.cbImplementation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SquashingFunction currentFunc = (SquashingFunction)
                    csrp.cbImplementation.getSelectedItem();
                if (!currentFunc.equals(csrp.initialSfunction)) {
                    prototypeRule.setSquashFunctionType(currentFunc);
                    csrp.fillDefaultValues();
                }
                csrp.repaint();
            }
        });
        csrp.fillDefaultValues();
        return csrp;
    }

    /**
     * Creates the continuous sigmoidal rule panel, but does not initialize the
     * listeners responsible for altering the panel in response to the selected
     * squashing function.
     */
    private ContinuousSigmoidalRulePanel() {
        super();
        this.add(tabbedPane);
        mainTab.addItem("Implementation", cbImplementation);
        mainTab.addItem("Time Constant", tfTimeConstant);
        mainTab.addItem("Leak Constant", tfLeakConstant);
        mainTab.addItem("Bias", tfBias);
        mainTab.addItem("Slope", tfSlope);
        mainTab.addItem("Add Noise", isAddNoise);
        tabbedPane.add(mainTab, "Main");
        tabbedPane.add(randTab, "Noise");
    }

//    @Override
    public void fillDefaultValues() {
        cbImplementation.setSelectedItem(prototypeRule.getSquashFunctionType());
        tfTimeConstant.setText(Double.toString(prototypeRule
            .getTimeConstant()));
        tfLeakConstant.setText(Double.toString(prototypeRule
            .getLeakConstant()));
        tfBias.setText(Double.toString(prototypeRule.getBias()));
        tfSlope.setText(Double.toString(prototypeRule.getSlope()));
        isAddNoise.setSelected(prototypeRule.getAddNoise());
        randTab.fillDefaultValues();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NeuronUpdateRule getPrototypeRule() {
        return prototypeRule;
    }

}