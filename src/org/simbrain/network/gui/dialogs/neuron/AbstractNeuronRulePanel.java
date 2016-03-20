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
import java.awt.Component;
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
    private List<Editor> editorList = new ArrayList<Editor>();

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
        this.setLayout(new BorderLayout());
    }

    /**
     * Todo.  Default text field for doubles
     *
     * @param getter
     * @param setter
     * @return
     */
    public JTextField registerTextField(
            ParameterGetter<NeuronUpdateRule, Double> getter,
            ParameterSetter<NeuronUpdateRule, Double> setter) {
        return (JTextField) this.<Double> registerProperty(
                Double.class, getter, setter);
    }

    /**
     * Todo.  Specify float.  used in HH for now.
     *
     * @param type
     * @param getter
     * @param setter
     * @return
     */
    public <V> JTextField registerTextField(Class<V> type,
            ParameterGetter<NeuronUpdateRule, V> getter,
            ParameterSetter<NeuronUpdateRule, V> setter) {
        return (JTextField) this.<V> registerProperty(type,
                getter, setter);
    }

    /**
     * Todo.
     *
     * @param getter
     * @param setter
     * @return
     */
    public TristateDropDown registerTriStateDropDown(
            ParameterGetter<NeuronUpdateRule, Boolean> getter,
            ParameterSetter<NeuronUpdateRule, Boolean> setter) {
        return (TristateDropDown) this.<Boolean> registerProperty(Boolean.class,
                        getter, setter);
    }

    /**
     * Todo.
     *
     * @param getter
     * @param setter
     * @return
     */
    public NStateDropDown registerNStateDropDown(
            ParameterGetter<NeuronUpdateRule, Integer> getter,
            ParameterSetter<NeuronUpdateRule, Integer> setter) {
        return (NStateDropDown) this
                .<Integer> registerProperty(Integer.class,
                        getter, setter);
    }

    /**
     * Register a component, a getter and a setter... TODO ... Rename
     * registerEditor or editableProperty?
     *
     * @param type
     * @param getter
     * @param setter
     * @return the component (text field, drop-down, etc) associated with this
     *         editor.
     */
    private <V> JComponent registerProperty(Class<V> type,
            ParameterGetter<NeuronUpdateRule, V> getter,
            ParameterSetter<NeuronUpdateRule, V> setter) {

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

    /**
     * Populate all fields with default values based on the underlying rule
     * classes (i.e. what an instance of a given rule instantiated using the
     * no-argument constructor would have.)
     */
    public final void fillDefaultValues() {

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
    public final void fillFieldValues(final List<NeuronUpdateRule> ruleList) {

        // Iterate through editable properties of the update rule
        // and fill corresponding field values using registered property
        // getters.
        editorList.stream().filter(editor -> editor.type == Double.class)
                .forEach(editor -> fillDoubleField(editor, ruleList));
        editorList.stream().filter(editor -> editor.type == Boolean.class)
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
     * @param editor the editor object to access the update rule..
     * @param ruleList rule list, used for the consistency check
     */
    private void fillDoubleField(final Editor<Double> editor,
            final List<NeuronUpdateRule> ruleList) {
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
    private void fillBooleanField(final Editor<Boolean> editor,
            final List<NeuronUpdateRule> ruleList) {
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

    /**
     * Sets state of a combo box using an integer index (we do assume all neuron
     * update rule integer fields can be thought of as indices of an enum).
     *
     * @param editor the editor object to access the update rule
     * @param ruleList rule list, used for the consistency check
     */
    private void fillIntegerField(final Editor<Integer> editor,
            final List<NeuronUpdateRule> ruleList) {
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
    public final void commitChanges(final List<Neuron> neurons) {

        // Change all neuron types to the indicated type
        if (isReplacingUpdateRules()) {
            if (getPrototypeRule() != null) {
                NeuronUpdateRule neuronRef = getPrototypeRule().deepCopy();
                neurons.forEach(n -> n.setUpdateRule(neuronRef.deepCopy()));
            }
        }

        // TODO: Deal with floats. Somehow use number?
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
    private void commitDouble(final Editor<Double> editor,
            final List<Neuron> neurons) {
        double value = Utils.doubleParsable((JTextField) editor.component);
        if (!Double.isNaN(value)) {
            neurons.stream().forEach(
                    r -> editor.setter.setParameter(r.getUpdateRule(), value));
        }
    }

    /**
     * Write a boolean value to a neuron rule.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to
     */
    private void commitBoolean(final Editor<Boolean> editor,
            final List<Neuron> neurons) {
        TristateDropDown tdd = (TristateDropDown) editor.component;
        if (!tdd.isNull()) {
            boolean value = tdd.isSelected();
            neurons.stream().forEach(
                    r -> editor.setter.setParameter(r.getUpdateRule(), value));
        }
    }

    /**
     * Write an integer value to a neuron rule, from a combo box.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to
     */
    private void commitInteger(final Editor<Integer> editor,
            final List<Neuron> neurons) {
        NStateDropDown cb = (NStateDropDown) editor.component;
        if (!cb.isNull()) {
            int index = cb.getSelectedIndex();
            neurons.stream().forEach(
                    r -> editor.setter.setParameter(r.getUpdateRule(), index));
        }
    }

    /**
     * Override to add custom notes or other text to bottom of panel. Can be
     * html formatted.
     *
     * @param text Text to be added
     */
    public final void addBottomText(final String text) {
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
    protected final boolean isReplacingUpdateRules() {
        return replaceUpdateRules;
    }

    /**
     * Tells this panel whether it is going to be editing neuron update rules,
     * or creating new ones and replacing the update rule of each of the neurons
     * being edited.
     *
     * @param replace true if replacing; false if editing
     */
    protected final void setReplacingUpdateRules(final boolean replace) {
        this.replaceUpdateRules = replace;
    }

    /**
     * Todo.
     *
     * @param ruleList
     * @return
     * @throws ClassCastException
     */
    public static ArrayList<Randomizer> getRandomizers(
            final List<NeuronUpdateRule> ruleList) throws ClassCastException {
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
    
    /**
     * Forces adds to respect borderlayout so that addBottomText works properly.
     */
    @Override
    public final Component add(Component comp) {
        return this.add(BorderLayout.CENTER, comp);
    }


    /**
     * Associates a JComponent with methods used to set a property on a
     * neuron update rule object. 
     *
     * @param <V> type of property being edited (double, boolean, etc).
     */
    private class Editor<V> {

        /** The type being edited. */
        private final Class<V> type;
        
        /** The Component (text field, dropdown, etc) used to edit this property. */
        private final JComponent component;
        
        /** The getter. */
        private final ParameterGetter<NeuronUpdateRule, V> getter;
        
        /** The setter. */
        private final ParameterSetter<NeuronUpdateRule, V> setter;

        /**
         * Construct the editor object.
         *
         * @param type type of the edited property
         * @param component associated component
         * @param getter get property
         * @param setter set property
         */
        public Editor(Class<V> type, JComponent component,
                ParameterGetter<NeuronUpdateRule, V> getter, ParameterSetter<NeuronUpdateRule, V> setter) {
            this.type = type;
            this.component = component;
            this.getter = getter;
            this.setter = setter;
        }
    }
}