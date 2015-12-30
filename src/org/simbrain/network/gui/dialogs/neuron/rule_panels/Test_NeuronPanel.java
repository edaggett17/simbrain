package org.simbrain.network.gui.dialogs.neuron.rule_panels;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JTextField;

import org.simbrain.network.core.NeuronUpdateRule;
import org.simbrain.network.neuron_update_rules.LinearRule;

/**
 * Class for testing some refactoring ideas relating to neuron dialogs.
 * 
 * Ideally in creating a panel class like this minimal work is needed.
 */
public class Test_NeuronPanel extends Test_AbstractNeuronPanel {
    
    JTextField testField1 = new JTextField("Testing..");
    
    public Test_NeuronPanel() {
        this.addItem("Test 1", testField1);
    }
        
    public static void main(String[] args) {
        
        // Rename neuron  /neuron list? 
        LinearRule rule = new LinearRule();
        List<NeuronUpdateRule> ruleList = new ArrayList();
        
        Test_NeuronPanel panel = new Test_NeuronPanel();
        
        JDialog dialog = new JDialog();
        dialog.setContentPane(panel);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
    }
    

}


