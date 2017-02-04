package org.simbrain.network.gui.dialogs.network;

import org.simbrain.network.groups.Group;
import org.simbrain.network.gui.NetworkPanel;
import org.simbrain.network.gui.dialogs.group.GroupPropertiesPanel;
import org.simbrain.util.LabelledItemPanel;
import org.simbrain.util.widgets.EditablePanel;

import javax.swing.JPanel;

/**
 * TODO: implement this class
 *
 * @author Amanda Pandey <amanda.pandey@gmail.com>
 */
public class BoltzmannPropertiesPanel extends JPanel implements GroupPropertiesPanel,
        EditablePanel {

    /** Main Panel. */
    private LabelledItemPanel mainPanel = new LabelledItemPanel();

    /** Top Panel for creation panels. */
    private LabelledItemPanel topPanel = new LabelledItemPanel();

    /** Parent Network Panel. */
    private NetworkPanel networkPanel;

    /** model group */
//    private final BoltzmannMachine boltzmannMachine;

    public BoltzmannPropertiesPanel(NetworkPanel networkPanel) {
        this.networkPanel = networkPanel;

        //TODO: init main panel
    }

    @Override
    public Group getGroup() {
        return null; // implement this
    }

    @Override
    public String getHelpPath() {
        return null;
    }

    @Override
    public void fillFieldValues() {

    }

    @Override
    public boolean commitChanges() {
        return false;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
