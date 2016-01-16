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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.core.NeuronUpdateRule.InputType;
import org.simbrain.network.gui.NetworkUtils;
import org.simbrain.util.SimbrainConstants;
import org.simbrain.util.Utils;
import org.simbrain.util.widgets.DropDownTriangle;
import org.simbrain.util.widgets.DropDownTriangle.UpDirection;
import org.simbrain.util.widgets.EditablePanel;
import org.simbrain.util.widgets.TristateDropDown;

/**
 * A panel containing more detailed generic information about neurons. Generally
 * speaking, this panel is not meant to exist in a dialog by itself, it is a set
 * of commonly used (hence generic) neuron value fields which is shared by
 * multiple complete dialogs.
 *
 * Values included are: Activation, upper / lower bounds, label, priority and
 * increment.
 *
 * @author ztosi
 * @author jyoshimi
 *
 */
@SuppressWarnings("serial")
public class GeneralNeuronPropertiesPanel extends JPanel implements EditablePanel {
    
    /** The neurons being modified. */
    private List<Neuron> neuronList;

    /** Activation field. */
    private JTextField tfActivation = new JTextField();

    /** Label Field. */
    private final JTextField tfNeuronLabel = new JTextField();

    /** The neuron Id. */
    private final JLabel idLabel = new JLabel();
    
    /**
     * A triangle that switches between an up (left) and a down state Used for
     * showing/hiding extra neuron data.
     */
    private final DropDownTriangle detailTriangle;
    
    /**
     * The extra data panel. Includes: increment, upper bound, lower bound, and
     * priority.
     */
    private final JPanel detailPanel = new JPanel();

    /** Increment field. */
    private final JTextField tfIncrement = new JTextField();

    /** Priority Field. */
    private final JTextField tfPriority = new JTextField();

    //TODO;
    private final TristateDropDown inputType = new TristateDropDown(
        InputType.WEIGHTED.toString(), InputType.SYNAPTIC.toString());
 
    /**
     * Whether or not the neuron is clamped (i.e. will not update/change its
     * activation once set).
     */
    private final TristateDropDown clamped = new TristateDropDown();

    /** Parent reference so pack can be called. */
    private final Window parent;

    /**
     * If true, displays ID info and other fields that would only make sense to show if
     * only one neuron was being edited. This value is set automatically unless
     * otherwise specified at construction. TODO
     */
    private boolean displayIDInfo;

    //TODO
    private BoundsClippingPanel boundsClippingPanel;

    /**
     * Creates a basic neuron info panel. Here whether or not to display ID info
     * is automatically set based on the state of the neuron list.
     *
     * @param neuronList
     *            the neurons whose information is being displayed/made
     *            available to edit on this panel
     * @param parent
     *            the parent window for dynamic resizing.
     * @return A basic neuron info panel with the specified parameters
     */
    public static GeneralNeuronPropertiesPanel createPanel(
            final List<Neuron> neuronList, final Window parent) {
        return createPanel(neuronList, parent,
                !(neuronList == null || neuronList.size() != 1));
    }

    /**
     * Creates a basic neuron info panel. Here the whether or not ID info is
     * displayed is manually set. This is the case when the number of neurons
     * (such as when adding multiple neurons) is unknown at the time of display.
     * In fact this is probably the only reason to use this factory method over
     * {@link #createBasicNeuronInfoPanel(List, Window)}.
     *
     * @param neuronList
     *            the neurons whose information is being displayed/made
     *            available to edit on this panel
     * @param parent
     *            the parent window for dynamic resizing
     * @param displayIDInfo
     *            whether or not to display ID info
     * @return A basic neuron info panel with the specified parameters
     */
    public static GeneralNeuronPropertiesPanel createPanel(
            final List<Neuron> neuronList, final Window parent,
            final boolean displayIDInfo) {
        GeneralNeuronPropertiesPanel bnip = new GeneralNeuronPropertiesPanel(neuronList,
                parent, displayIDInfo);
        bnip.addListeners();
        return bnip;
    }
    
    /**
    * TODO
    * @param neuronList
    * @param parent
    * @param displayIDInfo
    */
   private GeneralNeuronPropertiesPanel(final List<Neuron> neuronList,
           final Window parent, final boolean displayIDInfo) {
       this.neuronList = neuronList;
       this.parent = parent;
       this.displayIDInfo = displayIDInfo;
       detailTriangle = new DropDownTriangle(UpDirection.LEFT,
               displayIDInfo, "More", "Less", parent);
       boundsClippingPanel  = new BoundsClippingPanel(neuronList, parent);
       initializeLayout();
       fillFieldValues();
   }
    
    /**
     * Construct the panel using default .. TODO
     *
     * @param neuronList
     *            list of neurons to represent.
     * @param parent
     *            parent window so pack can be called
     */
    public GeneralNeuronPropertiesPanel(final List<Neuron> neuronList,
        final Window parent) {
        this(neuronList, parent, true); // TODO: Last true should be a field.
    }

    /**
     * Lays out the panel
     */
    private void initializeLayout() {

        setLayout(new BorderLayout());

        JPanel basicStatsPanel = new JPanel();
        basicStatsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridLayout gL = new GridLayout(0, 2);
        gL.setVgap(2);
        basicStatsPanel.setLayout(gL);
        if (displayIDInfo) {
            basicStatsPanel.add(new JLabel("Neuron Id:"));
            basicStatsPanel.add(idLabel);
        }
        basicStatsPanel.add(new JLabel("Activation:"));
        basicStatsPanel.add(tfActivation);
        // if (!multiFlag) {
        // TODO: Visible or not if multiple or no neurons are being edited?
        basicStatsPanel.add(new JLabel("Label:"));
        basicStatsPanel.add(tfNeuronLabel);
        // }

        JPanel trianglePanel = new JPanel();
        trianglePanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        trianglePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        trianglePanel.add(detailTriangle);
       
        BoxLayout layout = new BoxLayout(detailPanel, BoxLayout.Y_AXIS);
        detailPanel.setLayout(layout);

        GridLayout gl = new GridLayout(0, 2);
        gl.setVgap(5);

        JPanel clampP = new JPanel(gl);
        clampP.add(new JLabel("Clamped: "));
        clampP.add(clamped);
        clampP.setAlignmentX(CENTER_ALIGNMENT);
        detailPanel.add(clampP);

        detailPanel.add(Box.createVerticalStrut(5));

        detailPanel.add(boundsClippingPanel);
        

        JPanel subP = new JPanel(gl);
        subP.add(new JLabel("Increment: "));
        subP.add(tfIncrement);
        subP.add(new JLabel("Priority:"));
        subP.add(tfPriority);
        subP.add(new JLabel("Input Type:"));
        subP.add(inputType);
        subP.setAlignmentX(CENTER_ALIGNMENT);
        detailPanel.add(subP);
        
        this.add(basicStatsPanel, BorderLayout.NORTH);
        this.add(trianglePanel, BorderLayout.CENTER);
        detailPanel.setVisible(detailTriangle.isDown());
        this.add(detailPanel, BorderLayout.SOUTH);

        TitledBorder tb = BorderFactory.createTitledBorder("Neuron Properties");
        this.setBorder(tb);
 
    }
    
    /**
     * Add listeners.
     */
    private void addListeners() {

        // Add a listener to display/hide extra editable neuron data
        detailTriangle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Repaint to show/hide extra data
                detailPanel.setVisible(detailTriangle.isDown());
                detailPanel.repaint();
                // Alert the panel/dialog/frame this is embedded in to
                // resize itself accordingly
                parent.pack();
            }
        });
    }
    
    /**
     * Update field visibility based on whether rule is bounded and/or clipped.
     *
     * @param rule
     *            the current rule
     */
    public void updateFieldVisibility(NeuronUpdateRule rule) {
        boundsClippingPanel.updateFieldVisibility(rule);
        if(rule != null) {
            inputType.setSelectedItem(rule.getInputType().toString());            
        }
    }

    @Override
    public void fillFieldValues() {
        if (neuronList == null || neuronList.isEmpty()) {
            return;
        }
        Neuron neuronRef = neuronList.get(0);
        if (neuronRef == null) {
            return;
        }
        
        //TODO:In Neuron, may not need editor but just a getter

        // Handle Activation
        if (!NetworkUtils.isConsistent(neuronList, Neuron.activationEditor.getter)) {
            tfActivation.setText(SimbrainConstants.NULL_STRING);
        } else {
            tfActivation.setText(Double.toString(neuronRef.getActivation()));
        }

        // Handle Label
        if (!NetworkUtils.isConsistent(neuronList, Neuron.labelEditor.getter)) {
            tfNeuronLabel.setText(SimbrainConstants.NULL_STRING);
        } else {
            tfNeuronLabel.setText(neuronRef.getLabel());
        }
       
        // Handle bounds and clipping
        boundsClippingPanel.fillFieldValues();

        //TODO: Finish below
        List<NeuronUpdateRule> ruleList = Neuron.getRuleList(neuronList);

        // Handle Increment
        if (!NetworkUtils.isConsistent(ruleList,
            NeuronUpdateRule.class, "getIncrement")) {
            tfIncrement.setText(SimbrainConstants.NULL_STRING);
        } else {
            tfIncrement.setText(Double.toString(neuronRef.getUpdateRule()
                .getIncrement()));
        }

        // Handle Priority
        if (!NetworkUtils.isConsistent(neuronList, Neuron.class,
            "getUpdatePriority")) {
            tfPriority.setText(SimbrainConstants.NULL_STRING);
        } else {
            tfPriority.setText(Integer.toString(neuronRef.getUpdatePriority()));
        }
        // Handle Clamped
        if (!NetworkUtils.isConsistent(neuronList, Neuron.class, "isClamped")) {
            clamped.setNull();
        } else {
            clamped.setSelected(neuronList.get(0).isClamped());
        }

        // Handle input type
        if (!NetworkUtils.isConsistent(ruleList, NeuronUpdateRule.class,
            "getInputType")) {
            inputType.setNull();
        } else {
            inputType.setSelectedItem(ruleList.get(0).getInputType()
                .toString());
        }
    }

    /**
     * Uses the values from text fields to alter corresponding values in the
     * neuron(s) being edited. Called externally to apply changes. Returns a
     * success value that is false if, for example, text was placed in a numeric
     * field.
     */
    @Override
    public boolean commitChanges() {
        boolean success = true;
        
        // Commit activations
        double act = Utils.doubleParsable(tfActivation);
        if (!Double.isNaN(act)) {
            neuronList.stream().forEach(n -> n.forceSetActivation(
                    Double.parseDouble(tfActivation.getText())));
        } else {
            // Only successful if the field can't be parsed because
            // it is a NULL_STRING standing in for multiple values
            success &= tfActivation.getText().matches(SimbrainConstants.NULL_STRING);
        }
        
        // Label
        if (!tfNeuronLabel.getText().equals(SimbrainConstants.NULL_STRING)) {
            neuronList.stream().forEach(n -> n.setLabel(tfNeuronLabel.getText()));
        }
        
        // Todo: Tosi can this move down?
        // Update neurons 
        if (!neuronList.isEmpty()) {
            neuronList.get(0).getNetwork().fireNeuronsUpdated(neuronList);
        }
        
        // Clamped
        if (!clamped.isNull()) {
            neuronList.stream()
                    .forEach(n -> n.setClamped(clamped.isSelected()));
        }

        // Bounds and clipping
        success &= boundsClippingPanel.commitChanges();
        
        // Increment
        double increment = Utils.doubleParsable(tfIncrement);
        if (!Double.isNaN(increment)) {
            neuronList.stream().forEach(n -> n.setIncrement(increment));
        } else {
            // Only successful if the field can't be parsed because
            // it is a NULL_STRING standing in for multiple values
            success &= tfIncrement.getText().matches(
                SimbrainConstants.NULL_STRING);
        }

        // Priority
        Integer priority = Utils.parseInteger(tfPriority);
        if (priority != null) {
            // for integers to use as a flag).
            neuronList.stream().forEach(n -> n.setUpdatePriority(priority));
        } else {
            // Only successful if the field can't be parsed because
            // it is a NULL_STRING standing in for multiple values
            success &= tfPriority.getText().matches(
                SimbrainConstants.NULL_STRING);
        }

        // Input type
        if (((String) inputType.getSelectedItem())
            .matches(InputType.WEIGHTED.toString())) {
            neuronList.stream().forEach(
                    n -> n.getUpdateRule().setInputType(InputType.WEIGHTED));

        } else if (((String) inputType.getSelectedItem())
            .matches(InputType.SYNAPTIC.toString())) {
            neuronList.stream().forEach(
                    n -> n.getUpdateRule().setInputType(InputType.SYNAPTIC));
        }
        return success;
    }
    
    @Override
    public JPanel getPanel() {
        return this;
    }

}
