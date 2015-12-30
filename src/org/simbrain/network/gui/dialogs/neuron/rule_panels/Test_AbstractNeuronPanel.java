package org.simbrain.network.gui.dialogs.neuron.rule_panels;

import java.util.List;

import org.simbrain.util.LabelledItemPanel;

/**
 * Class for testing some refactoring ideas relating to neuron dialogs
 * 
 * It should be possible to put much of the work involved in constructing a
 * neuron panel here.
 */
public class Test_AbstractNeuronPanel extends LabelledItemPanel {
    
    public void init(List rules) {
        // Deal with overall consistency of neuron list
        // Special initialization based on what interfaces are implemented (e.g. boundedrule)
        // Other setup
    }

    public void fillFieldValues(List rules) {
        // Go through each settable property in the neuron rule list
        //   Check consistency
        //        Set value in this panel or use ...
        
    };

    public void commitChanges(List rules) {
        //  Go through each settable property in the panel
        //      set the corresponding neuron values
        
    };

    
}
