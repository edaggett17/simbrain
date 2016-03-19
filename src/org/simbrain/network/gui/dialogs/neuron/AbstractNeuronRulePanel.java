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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.neuron_update_rules.interfaces.NoisyUpdateRule;
import org.simbrain.util.ParameterGetter;
import org.simbrain.util.ParameterSetter;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;
import org.simbrain.util.randomizer.Randomizer;
import org.simbrain.util.widgets.NStateDropDown;
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
    private List<Editor> editorList = new ArrayList();

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

    /** Noise panel if any, null otherwise. */
    protected NoiseGeneratorPanel noisePanel;

    /**
     * A flag used to indicate whether this panel will be replacing neuron
     * update rules or simply writing to them.
     *
     * If true, create new update rules If false, edit existing update rules
     *
     * Set based on correspondence between neuron rule and the current rule
     * panel.
     *
     * Optimization to prevent multiple "instanceof" checks.
     */
    private boolean replaceUpdateRules = true;

    /**
     * This method is the default constructor.
     */
    public AbstractNeuronRulePanel() {
    }
    
    // Todo; add helper methods on top
    //
    // getComboBox
    // getTextField
    

    //TODO Docs

    // This assumes double.  Can make new ones for float or one where the type is specified,
    //  e.g for reading an int
    public JTextField registerTextField(
            ParameterGetter<NeuronUpdateRule, Double> getter,
            ParameterSetter<NeuronUpdateRule, Double> setter) {
        return (JTextField) this.<NeuronUpdateRule, Double> registerProperty(
                Double.class, getter, setter);
    }
    
    public <V> JTextField registerTextField(Class<V> type,
            ParameterGetter<NeuronUpdateRule, V> getter,
            ParameterSetter<NeuronUpdateRule, V> setter) {
        return (JTextField) 
                this.<NeuronUpdateRule, V>registerProperty(type, getter, setter);
    }

    // Change name? registerBoolean
    // Assumes boolean
    public TristateDropDown registerTriStateDropDown(
            ParameterGetter<NeuronUpdateRule, Boolean> getter,
            ParameterSetter<NeuronUpdateRule, Boolean> setter) {
        return (TristateDropDown) this.<NeuronUpdateRule, Boolean> registerProperty(
                Boolean.class, getter, setter);
    }

    public JComboBox registerComboBox(
            ParameterGetter<NeuronUpdateRule, Integer> getter,
            ParameterSetter<NeuronUpdateRule, Integer> setter) {
        return (JComboBox) this.<NeuronUpdateRule, Integer> registerProperty(
                Integer.class, getter, setter);
    }
    
    public NStateDropDown registerNStateDropDown(
            ParameterGetter<NeuronUpdateRule, Integer> getter,
            ParameterSetter<NeuronUpdateRule, Integer> setter) {
        return (NStateDropDown) this.<NeuronUpdateRule, Integer> registerProperty(
                Integer.class, getter, setter);
    }


    /**
     * Register a component, a getter and a setter... TODO ... Rename registerEditor or editableProperty?
     *
     * @param type
     * @param getter
     * @param setter
     * @return the component (text field, drop-down, etc) associated with this editor.
     */
    protected <O,V> JComponent registerProperty(Class<V> type, ParameterGetter<O,V> getter,
            ParameterSetter<O,V> setter) {
        
        if (type == Double.class || type == Float.class) {
            JTextField field = new JTextField();
            editorList.add(new Editor(type, field, getter, setter));
            return field;
        } else if (type == Boolean.class) {
            TristateDropDown dropDown = new TristateDropDown();
            editorList.add(new Editor(type, dropDown, getter, setter));
            return dropDown;
        } else if (type == Integer.class) {
            NStateDropDown dropDown = new NStateDropDown();
            editorList.add(new Editor(type, dropDown, getter, setter));
            return dropDown;            
        }

        return null;
    }

    public abstract void fillDefaultValues();

    //TODO: Spread below? Get rid of those getdefault things I have?
    public void fillDefault() {
        
        editorList.stream()
                .filter(editor -> (editor.type == Double.class)
                        || (editor.type == Float.class))
                .forEach(editor -> fillDoubleField(editor,
                        Collections.singletonList(getPrototypeRule())));
        editorList.stream().filter(editor -> editor.type == Boolean.class)
                .forEach(editor -> fillBooleanField(editor,
                        Collections.singletonList(getPrototypeRule())));
        editorList.stream().filter(editor -> editor.type == Integer.class)
                .forEach(editor -> fillIntegerField(editor,
                        Collections.singletonList(getPrototypeRule())));

        if (isNoisePanel()) {
            noisePanel.fillFieldValues(getRandomizers(
                    Collections.singletonList(getPrototypeRule())));
        }
    }

    
    /**
     * Populate neuron panel fields based on the list of rules. If there are
     * inconsistencies use a "...".
     *
     * @param ruleList the list of neuron update rules to use.
     */
    public void fillFieldValues(List<NeuronUpdateRule> ruleList) {
        
        // Iterate through editable properties of the update rule
        // and fill corresponding field values.
        editorList.stream().filter(editor -> editor.type == Double.class)
                .forEach(editor -> fillDoubleField(editor, ruleList));
        editorList.stream().filter(editor -> editor.type  == Boolean.class)
                .forEach(editor -> fillBooleanField(editor, ruleList));
        editorList.stream().filter(editor -> editor.type == Integer.class)
                .forEach(editor -> fillIntegerField(editor, ruleList));

        if (isNoisePanel()) {
            noisePanel.fillFieldValues(getRandomizers(ruleList));
        }
    }

    /**
     * Fills text field with a double value.
     *
     * @param editor the editor object to access the update rule.  TODO.Here and next 7 or 8 javadocs.
     * @param ruleList rule list, used for the consistency check
     */
    public void fillDoubleField(Editor editor,
            List<NeuronUpdateRule> ruleList) {
        NeuronUpdateRule neuronRef = ruleList.get(0);
        JTextField textField = (JTextField) editor.component;
        if (!NetworkUtils.isConsistent(ruleList, editor.getter)) {
            textField.setText(SimbrainConstants.NULL_STRING);
        } else {
            textField.setText("" + editor.getter.getParameter(neuronRef));
        }
    }
    
    /**
     * Fills a boolean field to a dropdown with anull.
     *
     * @param editor the editor object to access the update rule
     * @param ruleList rule list, used for the consistency check
     */
    public void fillBooleanField(Editor editor,
            List<NeuronUpdateRule> ruleList) {
        NeuronUpdateRule neuronRef = ruleList.get(0);
        TristateDropDown dropDown = (TristateDropDown) editor.component;
        if (!NetworkUtils.isConsistent(ruleList, editor.getter)) {
            dropDown.setNull();
        } else {
            dropDown.removeNull();
            dropDown.setSelected(
                    (Boolean) editor.getter.getParameter(neuronRef));
        }
    }
    
    //TODO: Make all these private
    
    /**
     * Sets state of a combo box using an integer index (we do assume all neuron
     * update rule integer fields can be thought of as indices of an enum).
     *
     * @param editor the editor object to access the update rule
     * @param ruleList rule list, used for the consistency check
     */
    private void fillIntegerField(
            Editor editor,
            List<NeuronUpdateRule> ruleList) {
        NeuronUpdateRule neuronRef = ruleList.get(0);
        NStateDropDown dropDown = (NStateDropDown) editor.component;
        if (!NetworkUtils.isConsistent(ruleList, editor.getter)) {
            dropDown.setNull();
        } else {
            dropDown.removeNull();
            int index = (Integer) editor.getter.getParameter(neuronRef);
            dropDown.setSelectedIndex(index);
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
        
        //TODO: Deal with floats.  Somehow use number?
        // Write parameter values in all fields
        editorList.stream().filter(editor -> editor.type == Double.class)
                .forEach(editor -> commitDouble(editor, neurons));
        editorList.stream().filter(editor -> editor.type == Boolean.class)
                .forEach(editor -> commitBoolean(editor, neurons));
        editorList.stream().filter(editor -> editor.type == Integer.class)
                .forEach(editor -> commitInteger(editor, neurons));

        if (isNoisePanel()) {
            noisePanel.commitRandom(neurons);
        }
    }

    /**
     * Write a double value to a neuron rule.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to
     */

    public void commitDouble(Editor<NeuronUpdateRule, Double> editor,
            List<Neuron> neurons) {
        double value = Utils.doubleParsable((JTextField) editor.component);
        if (!Double.isNaN(value)) {
            neurons.stream().forEach(r -> editor.setter
                    .setParameter(r.getUpdateRule(), value));
        }
    }


    /**
     * Write a boolean value to a neuron rule.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to
     */
    public void commitBoolean(Editor<NeuronUpdateRule, Boolean> editor,
            List<Neuron> neurons) {
        TristateDropDown tdd = (TristateDropDown) editor.component;
        if (!tdd.isNull()) {
            boolean value = tdd.isSelected();
            neurons.stream().forEach(r -> editor.setter
                    .setParameter(r.getUpdateRule(), value));
        }
    }

    /**
     * Write an integer value to a neuron rule, from a combo box.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to
     */
    public void commitInteger(Editor<NeuronUpdateRule, Integer> editor,
            List<Neuron> neurons) {
        NStateDropDown cb = (NStateDropDown) editor.component;
        if (!cb.isNull()) {
            int index = cb.getSelectedIndex();
            neurons.stream().forEach(r -> editor.setter
                    .setParameter(r.getUpdateRule(), index));
        }
    }

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

    /**
     * @return List of randomizers.
     */
    public static ArrayList<Randomizer> getRandomizers(
            List<NeuronUpdateRule> ruleList) throws ClassCastException {
        return (ArrayList<Randomizer>) ruleList.stream()
                .map(r -> ((NoisyUpdateRule) r).getNoiseGenerator())
                .collect(Collectors.toList());
    }

    /**
     * Check if this panel represents a neuron associated with a noise
     * generator.
     *
     * @return true if the panel represents a noisy update rule, false otherwise
     */
    private boolean isNoisePanel() {
        if (this.getPrototypeRule() instanceof NoisyUpdateRule) {
            return true;
        }
        return false;
    }
    
    
    //TODO: Do we need fillDefault at what level?  Check all.

    /**
     * TODO.  Doc. Also convert to getter/setter?
     *
     * @author Jeff Yoshimi
     *
     * @param <O>
     * @param <V>
     */
    private class Editor <O,V> {

        private final Class<V> type;
        private final JComponent component;
        private final ParameterGetter<O,V> getter;
        private final ParameterSetter<O,V> setter;
        
        public Editor(Class<V> type, JComponent component, ParameterGetter<O,V>  getter,
                ParameterSetter<O,V> setter) {
            this.type = type;
            this.component = component;
            this.getter = getter;
            this.setter = setter;
        }
    }

}