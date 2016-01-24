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

import java.util.List;

import javax.swing.JTabbedPane;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.gui.dialogs.neuron.AbstractNeuronRulePanel;
import org.simbrain.network.gui.dialogs.neuron.NoiseGeneratorPanel;
import org.simbrain.network.neuron_update_rules.DecayRule;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.widgets.NStateDropDown;

/**
 * <b>DecayNeuronPanel</b> 
 */
public class DecayRulePanel extends AbstractNeuronRulePanel  {

    /** Tabbed pane. */
    private JTabbedPane tabbedPane = new JTabbedPane();

    /** Main tab. */
    private LabelledItemPanel mainTab = new LabelledItemPanel();

    /** A reference to the neuron update rule being edited. */
    private static final DecayRule prototypeRule = new DecayRule();

    /**
     * This method is the default constructor.
     */
    public DecayRulePanel() {
        super();

        init(DecayRule.editorList);
        this.add(tabbedPane);
        NStateDropDown dropdown =  ((NStateDropDown) componentMap.get("relAbs"));
        dropdown.setItems(new String[] { "Relative", "Absolute" });
        dropdown.addActionListener(e -> {
            checkBounds(dropdown.getSelectedIndex());

        });

        mainTab.addItem("", dropdown);
        mainTab.addItem("Base line", componentMap.get("baseLine"));
        mainTab.addItem("Decay amount", componentMap.get("decayAmount"));
        mainTab.addItem("Decay fraction", componentMap.get("decayFraction"));
        mainTab.addItem("Add noise", componentMap.get("addNoise"));
        tabbedPane.add(mainTab, "Main");

        noisePanel = new NoiseGeneratorPanel(); 
        tabbedPane.add(noisePanel, "Noise");
        checkBounds(dropdown.getSelectedIndex());
    }

    /**
     * Checks the relative absolute bounds.
     */
    private void checkBounds(int selectedIndex) {
        if (selectedIndex == 0) {
            componentMap.get("decayAmount").setEnabled(false);
            componentMap.get("decayFraction").setEnabled(true);
        } else {
            componentMap.get("decayAmount").setEnabled(true);
            componentMap.get("decayFraction").setEnabled(false);
        }
    }

    @Override
    protected DecayRule getPrototypeRule() {
        return prototypeRule.deepCopy();
    }

    @Override
    public void fillDefaultValues() {
        fillDefault();
    }

    @Override
    public void commitChanges(Neuron neuron) {
    }

    @Override
    protected void writeValuesToRules(List<Neuron> neurons) {
    }

}
