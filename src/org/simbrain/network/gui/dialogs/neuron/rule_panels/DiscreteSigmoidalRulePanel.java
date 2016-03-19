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

import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.neuron_update_rules.SigmoidalRule;
import org.simbrain.util.math.SquashingFunction;

/**
 * <b>SigmoidalRulePanel</b>. A rule panel for editing neurons which use a
 * discrete sigmoid squashing function as their update rule.
 * 
 * @author Zach Tosi
 * @author Jeff Yoshimi
 */
@SuppressWarnings("serial")
public class DiscreteSigmoidalRulePanel extends AbstractSigmoidalRulePanel {

    /** A reference to the neuron rule being edited. */
    private static SigmoidalRule prototypeRule = new SigmoidalRule();

//    /**
//     * Creates a fully functional discrete sigmoidal rule panel.
//     */
//    public static DiscreteSigmoidalRulePanel createSigmoidalRulePanel() {
//        prototypeRule = new SigmoidalRule();
//        final DiscreteSigmoidalRulePanel dsrp = new DiscreteSigmoidalRulePanel();
//        dsrp.cbImplementation.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                SquashingFunction currentFunc = (SquashingFunction) dsrp.cbImplementation
//                        .getSelectedItem();
//                if (!currentFunc.equals(dsrp.initialSfunction)) {
//                    prototypeRule.setSquashFunctionType(currentFunc);
//                    dsrp.fillDefaultValues();
//                }
//                dsrp.repaint();
//            }
//        });
//        dsrp.fillDefaultValues();
//        return dsrp;
//    }

    /**
     * Creates the discrete sigmoidal rule panel, but does not initialize the
     * listeners responsible for altering the panel in response to the selected
     * squashing function.
     */
    public DiscreteSigmoidalRulePanel() {
        super();
        this.add(tabbedPane);
        mainTab.addItem("Implementation", cbImplementation);
        mainTab.addItem("Bias", tfBias);
        mainTab.addItem("Slope", tfSlope);
        mainTab.addItem("Add Noise", isAddNoise);
        tabbedPane.add(mainTab, "Main");
        tabbedPane.add(noisePanel, "Noise");
    }

    /**
     * Fill field values to default values for sigmoidal neuron.
     */
    public void fillDefaultValues() {
        this.fillDefault();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected NeuronUpdateRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

}
