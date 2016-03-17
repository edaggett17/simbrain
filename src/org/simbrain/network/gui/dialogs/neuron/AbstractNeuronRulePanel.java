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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.network.neuron_update_rules.interfaces.NoisyUpdateRule;
import org.simbrain.util.ParameterEditor;
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
    protected List<ParameterEditor> editorList = new ArrayList<ParameterEditor>();

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

    /** Noise panel if any, null otherwise. */
    protected NoiseGeneratorPanel noisePanel;

    /**
     * Parameter editor object for the "add noise" parameter. Used in
     * consistency checks.
     */
    private ParameterEditor<NeuronUpdateRule, Boolean> addNoiseEditor = new ParameterEditor<NeuronUpdateRule, Boolean>(
            Boolean.class, "addNoise",
            (r) -> ((NoisyUpdateRule) r).getAddNoise(),
            (r, val) -> ((NoisyUpdateRule) r).setAddNoise((boolean) val));


    /**
     * Should noise be added to this neuron? (Only used for update rules that
     * implement {@link NoisyUpdateRule}).
     */
    private TristateDropDown isAddNoise;

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

    // TODO: Beginning of an alternative way of doing this.
    //protected Map<JComponent, ParameterEditor> componentMap2 = new HashMap<JComponent, ParameterEditor>();

    /**
     * Initialize the component map which associated with String keys with
     * property editors, which contain the getters used in
     * {@link #fillFieldValues(List)} and the setters used in
     * {@link #commitChanges(List)}.
     *
     * @param editorList list of editors.
     */
    protected void init(List<ParameterEditor> editorList) {

        this.editorList = editorList;

        // TODO: Construct the edtior list here based on annotations?
        //for (ParameterEditor<NeuronUpdateRule, ?> editor : editorList) {
        //    if (editor.getType() == Double.class) {
        //        componentMap2.put(new JTextField(), editor);
        //    }
        //}

        // Initialize the component map
        for (ParameterEditor<NeuronUpdateRule, ?> editor : editorList) {
            if (editor.getType() == Double.class) {
                componentMap.put(editor.getKey(), new JTextField());
            } else if (editor.getType() == Boolean.class) {
                componentMap.put(editor.getKey(), new TristateDropDown());
            } else if (editor.getType() == Integer.class) {
                componentMap.put(editor.getKey(), new NStateDropDown());
            }
        }

        if (isNoisePanel()) {
            isAddNoise = new TristateDropDown();
            componentMap.put("addNoise", isAddNoise);
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
        editorList.stream().filter(editor -> editor.getType() == Double.class)
                .forEach(editor -> fillDoubleField(editor, ruleList));
        editorList.stream().filter(editor -> editor.getType() == Boolean.class)
                .forEach(editor -> fillBooleanField(editor, ruleList));
        editorList.stream().filter(editor -> editor.getType() == Integer.class)
                .forEach(editor -> fillIntegerField(editor, ruleList));

        if (isNoisePanel()) {
            fillBooleanField(addNoiseEditor, ruleList);
            noisePanel.fillFieldValues(getRandomizers(ruleList));
        }
    }

    /**
     * Fills text field with a double value.
     *
     * @param editor the editor object to access the update rule
     * @param ruleList rule list, used for the consistency check
     */
    public void fillDoubleField(
            ParameterEditor<NeuronUpdateRule, Double> editor,
            List<NeuronUpdateRule> ruleList) {
        NeuronUpdateRule neuronRef = ruleList.get(0);
        JTextField textField = (JTextField) componentMap.get(editor.getKey());
        if (!NetworkUtils.isConsistent(ruleList, editor.getGetter())) {
            textField.setText(SimbrainConstants.NULL_STRING);
        } else {
            textField.setText(Double
                    .toString(editor.getGetter().getParameter(neuronRef)));
        }
    }

    /**
     * Fills a boolean field to a dropdown with anull.
     *
     * @param editor the editor object to access the update rule
     * @param ruleList rule list, used for the consistency check
     */
    public void fillBooleanField(
            ParameterEditor<NeuronUpdateRule, Boolean> editor,
            List<NeuronUpdateRule> ruleList) {
        NeuronUpdateRule neuronRef = ruleList.get(0);
        TristateDropDown dropDown = (TristateDropDown) componentMap
                .get(editor.getKey());
        if (!NetworkUtils.isConsistent(ruleList, editor.getGetter())) {
            dropDown.setNull();
        } else {
            dropDown.removeNull();
            dropDown.setSelected(editor.getGetter().getParameter(neuronRef));
        }
    }

    /**
     * Sets state of a combo box using an integer index (we do assume all neuron
     * update rule integer fields can be thought of as indices of an enum).
     *
     * @param editor the editor object to access the update rule
     * @param ruleList rule list, used for the consistency check
     */
    public void fillIntegerField(
            ParameterEditor<NeuronUpdateRule, Integer> editor,
            List<NeuronUpdateRule> ruleList) {
        NeuronUpdateRule neuronRef = ruleList.get(0);
        NStateDropDown dropDown = (NStateDropDown) componentMap
                .get(editor.getKey());
        if (!NetworkUtils.isConsistent(ruleList, editor.getGetter())) {
            dropDown.setNull();
        } else {
            dropDown.removeNull();
            int index = editor.getGetter().getParameter(neuronRef);
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

        // Write parameter values in all fields
        editorList.stream().filter(editor -> editor.getType() == Double.class)
                .forEach(editor -> commitDouble(editor, neurons));
        editorList.stream().filter(editor -> editor.getType() == Boolean.class)
                .forEach(editor -> commitBoolean(editor, neurons));
        editorList.stream().filter(editor -> editor.getType() == Integer.class)
                .forEach(editor -> commitInteger(editor, neurons));

        if (isNoisePanel()) {
            commitBoolean(addNoiseEditor, neurons);
            noisePanel.commitRandom(neurons);
        }
    }

    /**
     * Write a double value to a neuron rule.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to
     */
    public void commitDouble(ParameterEditor<NeuronUpdateRule, Double> editor,
            List<Neuron> neurons) {
        double value = Utils.doubleParsable(
                (JTextField) (componentMap.get(editor.getKey())));
        if (!Double.isNaN(value)) {
            neurons.stream().forEach(r -> editor.getSetter()
                    .setParameter(r.getUpdateRule(), value));
        }
    }

    /**
     * Write a boolean value to a neuron rule.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to
     */
    public void commitBoolean(ParameterEditor<NeuronUpdateRule, Boolean> editor,
            List<Neuron> neurons) {
        TristateDropDown tdd = (TristateDropDown) componentMap
                .get(editor.getKey());
        if (!tdd.isNull()) {
            boolean value = tdd.isSelected();
            neurons.stream().forEach(r -> editor.getSetter()
                    .setParameter(r.getUpdateRule(), value));
        }
    }

    /**
     * Write an integer value to a neuron rule, from a combo box.
     *
     * @param editor the editor object
     * @param neurons the neurons to be written to
     */
    public void commitInteger(ParameterEditor<NeuronUpdateRule, Integer> editor,
            List<Neuron> neurons) {
        NStateDropDown cb = (NStateDropDown) componentMap.get(editor.getKey());
        if (!cb.isNull()) {
            int index = cb.getSelectedIndex();
            neurons.stream().forEach(r -> editor.getSetter()
                    .setParameter(r.getUpdateRule(), index));
        }
    }

    /**
     * Populate fields with default data. TODO: Replace with below when done.
     */
    public abstract void fillDefaultValues();

    public void fillDefault() {
        editorList.stream().filter(editor -> editor.getType() == Double.class)
                .forEach(editor -> fillDoubleField(editor,
                        Collections.singletonList(getPrototypeRule())));
        editorList.stream().filter(editor -> editor.getType() == Boolean.class)
                .forEach(editor -> fillBooleanField(editor,
                        Collections.singletonList(getPrototypeRule())));
        editorList.stream().filter(editor -> editor.getType() == Integer.class)
                .forEach(editor -> fillIntegerField(editor,
                        Collections.singletonList(getPrototypeRule())));

        if (isNoisePanel()) {
            fillBooleanField(addNoiseEditor,
                    Collections.singletonList(getPrototypeRule()));
            noisePanel.fillFieldValues(getRandomizers(
                    Collections.singletonList(getPrototypeRule())));
        }
    }

    /**
     * Called to commit changes to a single neuron.
     *
     * Usually this is a template neuron intended to be copied for the purpose
     * of creating many new neurons. Using this method to commit changes to many
     * neurons is not recommended. Instead pass a list of the neurons to be
     * changed into {@link #commitChanges(List) commitChanges}.
     *
     * @param neuron the neuron to which changes are being committed to.
     */
    public abstract void commitChanges(final Neuron neuron);

    // TODO: Remove below when done with other changes.
    /**
     * Edits neuron update rules that already exist. This is the alternative to
     * replacing the rules and occurs when the neuron update rules being edited
     * are the same type as the panel. {@link #replaceUpdateRules} is the flag
     * for whether this method is used for committing or the rules are deleted
     * and replaced entirely, in which case this method is not called.
     *
     * @param neurons the neurons whose rules are being <b>edited</b>, not
     *            replaced.
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

   
    
    List<DoubleGetter> doubleGetters = new ArrayList<DoubleGetter>();
    List<DoubleSetter> doubleSetters = new ArrayList<DoubleSetter>();

    interface DoubleGetter {
        public double interfaceMethod();
    }
    interface DoubleSetter {
        public void interfaceMethod(double val);
    }

    // registerDoubleEditor
    // registerBooleanEditor
    // registerNoisePanel > return useNoise textField + panel
    // registerComboBoxEditor
    
    // TODO: Generify.  Or pass in a string description

    // Associates a Text field with a double field.   
    // You provide the getter and setter, it gives you the text field
    //   then reading and writing data from the neurons, and deailng with  
    //   inconsistencies, is handled automatically.
    protected JComponent registerDoublePropery(DoubleGetter  getter, DoubleSetter setter) {
       doubleGetters.add(getter); // TODO: Load a hashmap
       doubleSetters.add(setter);
       return new JTextField();
    }
}