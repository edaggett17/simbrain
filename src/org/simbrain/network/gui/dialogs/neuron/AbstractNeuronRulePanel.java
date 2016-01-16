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
package org.simbrain.network.gui.dialogs.neuron;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.gui.dialogs.neuron.rule_panels.PropertyEditor;
import org.simbrain.network.neuron_update_rules.interfaces.NoisyUpdateRule;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;
import org.simbrain.util.randomizer.Randomizer;
import org.simbrain.util.widgets.TristateDropDown;

/**
 * <b>AbstractNeuronPanel</b> is the parent class for all panels used to set
 * parameters of specific neuron rule types.
 *
 * Optimization has been emphasized for methods intended for neuron creation
 * rather than editing on the assumption that the former will be far more common
 * for large numbers of neurons.
 */
@SuppressWarnings("serial")
public abstract class AbstractNeuronRulePanel extends JPanel {

    /** List of editor objects associated with this type of neuron. */
    protected List<PropertyEditor> editorList = new ArrayList<PropertyEditor>();

    /**
     * Mapping from key strings to components associated with this rule panel.
     * Populated on the basis of the editor list
     */
    protected Map<String, JComponent> componentMap = new HashMap<String, JComponent>();
    
    /**
     * Each neuron panel contains a static final subclass of NeuronUpdateRule
     * variable called a prototype rule. The specific subclass of
     * NeuronUpdateRule corresponds to the rule specified by the panel name.
     * 
     * Used so that other classes can query specific properties of the rule the
     * panel edits. Also used internally to make deep copies.
     *
     * @return an instance of the neuron rule which corresponds to the panel.
     */
    protected abstract NeuronUpdateRule getPrototypeRule();

    /**
     * A flag used to indicate whether this panel will be replacing neuron
     * update rules or simply writing to them.
     * 
     *  If true, create new update rules
     *  If false, edit existing update rules
     *  
     *  Set based on correspondence between neuron rule and the current rule panel.
     * 
     *  Optimization to prevent multiple "instanceof" checks.
     */
    private boolean replaceUpdateRules = true; 

    /**
     * This method is the default constructor.
     */
    public AbstractNeuronRulePanel() {
    }

    /**
     * Initialize the component map which associated with String keys with
     * property editors, which contain the getters used in
     * {@link #fillFieldValues(List)} and the setters used in
     * {@link #commitChanges(List)}
     *
     * @param editorList list of editors.
     */
    protected void init(List<PropertyEditor> editorList) {
        
        this.editorList = editorList;

        // Initialize the component map
        for (PropertyEditor<NeuronUpdateRule, ?> editor : editorList) {
            if (editor.type == Double.class) {
                componentMap.put(editor.key, new JTextField());
            } else if (editor.type == Boolean.class) {
                componentMap.put(editor.key, new TristateDropDown());
            }
        }
        // TODO use Collectors.groupingBy above?
    }

    /**
     * Populate neuron panel fields based on the list of rules.  If there are
     * inconsistencies use a "...".
     *
     * @param ruleList the list of neuron update rules to use.
     */
    public void fillFieldValues(List<NeuronUpdateRule> ruleList) {

        // Go through each editable property and fill the corresponding field
        // value.
        editorList.stream().filter(editor -> editor.type == Double.class)
                .forEach(editor -> fillDoubleField(editor, ruleList));
        editorList.stream().filter(editor -> editor.type == Boolean.class)
                .forEach(editor -> fillBooleanField(editor, ruleList));
        // TODO: It may be possible to do the above using
        // Collectors.groupingBy
    }

    /**
     * Fills a double field. 
     *
     * @param editor the editor object to access the update rule
     * @param ruleList rule list, used for the consistency check
     */
    public void fillDoubleField(PropertyEditor<NeuronUpdateRule, Double> editor, List<NeuronUpdateRule> ruleList) {
        NeuronUpdateRule neuronRef = ruleList.get(0);
        JTextField textField = (JTextField) componentMap.get(editor.key);
        if (!NetworkUtils.isConsistent(ruleList, editor.getter)) {
            textField.setText(SimbrainConstants.NULL_STRING);
        } else {
            textField.setText(Double
                    .toString(editor.getter.getParameter(neuronRef)));
        }
    }

    /**
     * Fills a boolean field. 
     *
     * @param editor the editor object to access the update rule
     * @param ruleList rule list, used for the consistency check
     */
    public void fillBooleanField(PropertyEditor<NeuronUpdateRule, Boolean> editor,
            List<NeuronUpdateRule> ruleList) {
        NeuronUpdateRule neuronRef = ruleList.get(0);
        TristateDropDown dropDown = (TristateDropDown) componentMap
                .get(editor.key);
        if (!NetworkUtils.isConsistent(ruleList, editor.getter)) {
            dropDown.setNull();
        } else {
            dropDown.setSelected(editor.getter.getParameter(neuronRef));
        }
    }

    /**
     * Write the values of the GUI fields to the neurons themselves.
     *
     * @param neurons the neurons to be written to
     */
    public void commitChanges(List<Neuron> neurons) {

        // Change all neuron types to the indicated type
        if (isReplacingUpdateRules()) {
            NeuronUpdateRule neuronRef = getPrototypeRule().deepCopy();
            neurons.forEach(n -> n.setUpdateRule(neuronRef.deepCopy()));
        }

        // Write parameter values in all fields 
        editorList.stream().filter(editor -> editor.type == Double.class)
                .forEach(editor -> commitDouble(editor, neurons));
        editorList.stream().filter(editor -> editor.type == Boolean.class)
                .forEach(editor -> commitBoolean(editor, neurons));
        // TODO: It may be possible to do the above using Collectors.groupingBy
    }

    /**
     * Write a double value to a neuron rule.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to 
     */
    public void commitDouble(PropertyEditor<NeuronUpdateRule, Double> editor, List<Neuron> neurons) {
        double value = Utils.doubleParsable(
                (JTextField) (componentMap.get(editor.key)));
        if (!Double.isNaN(value)) {
            for (int i = 0; i < neurons.size(); i++) {
                editor.setter.setParameter(
                        ((NeuronUpdateRule) neurons.get(i).getUpdateRule()),
                        value);
            }
        }
    }

    /**
     * Write a boolean value to a neuron rule.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to 
     */
    public void commitBoolean(PropertyEditor<NeuronUpdateRule, Boolean> editor, List<Neuron> neurons) {
        boolean value = ((TristateDropDown) componentMap
                .get(editor.key)).isSelected();
        for (int i = 0; i < neurons.size(); i++) {
            editor.setter.setParameter(
                    ((NeuronUpdateRule) neurons.get(i).getUpdateRule()), value);
        }
    }
    /**
     * Populate fields with default data. TODO: Think.
     */
    public abstract void fillDefaultValues();

    /**
     * Called to commit changes to a single neuron.
     * 
     *  Usually this is a template
     * neuron intended to be copied for the purpose of creating many new
     * neurons. Using this method to commit changes to many neurons is not
     * recommended. Instead pass a list of the neurons to be changed into
     * {@link #commitChanges(List) commitChanges}.
     *
     * @param neuron the neuron to which changes are being committed to.
     */
    public abstract void commitChanges(final Neuron neuron);

    //TODO: Remove below when done with other changes.
    /**
     * Edits neuron update rules that already exist. This is the alternative to
     * replacing the rules and occurs when the neuron update rules being edited
     * are the same type as the panel. {@link #replaceUpdateRules} is the flag for
     * whether this method is used for committing or the rules are deleted and
     * replaced entirely, in which case this method is not called.
     *
     * @param neurons the neurons whose rules are being <b>edited</b>, not replaced.
     */
    protected abstract void writeValuesToRules(final List<Neuron> neurons);

    /**
     * Override to add custom notes or other text to bottom of panel. Can be
     * html formatted.
     *
     * @param text Text to be added
     */
    public void addBottomText(final String text) {
        JPanel labelPanel = new JPanel();
        JLabel theLabel = new JLabel(text);
        labelPanel.add(theLabel);
        this.add(labelPanel, BorderLayout.SOUTH);
    }

    /**
     * Are we replacing rules or editing them? Replacing happens when
     * {@link #commitChanges(List)} is called on a neuron panel whose rule is
     * different from the rules of the neurons being edited.
     *
     * @return true if replacing; false if editing
     */
    protected boolean isReplacingUpdateRules() {
        return replaceUpdateRules;
    }

    /**
     * Tells this panel whether it is going to be editing neuron update rules,
     * or creating new ones and replacing the update rule of each of the neurons
     * being edited.
     *
     * @param replace true if replacing; false if editing
     */
    protected void setReplacingUpdateRules(boolean replace) {
        this.replaceUpdateRules = replace;
    }

    //TODO: Think about how to handle this
    /**
     * @return List of randomizers.
     */
    public static ArrayList<Randomizer> getRandomizers(
            List<NeuronUpdateRule> ruleList) throws ClassCastException {
        ArrayList<Randomizer> ret = new ArrayList<Randomizer>();
        for (int i = 0; i < ruleList.size(); i++) {
            ret.add(((NoisyUpdateRule) ruleList.get(i)).getNoiseGenerator());
        }
        return ret;
    }

}