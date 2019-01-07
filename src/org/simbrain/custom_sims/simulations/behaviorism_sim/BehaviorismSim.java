package org.simbrain.custom_sims.simulations.behaviorism_sim;

import org.simbrain.custom_sims.RegisteredSimulation;
import org.simbrain.custom_sims.helper_classes.ControlPanel;
import org.simbrain.custom_sims.helper_classes.NetBuilder;
import org.simbrain.custom_sims.helper_classes.OdorWorldBuilder;
import org.simbrain.custom_sims.simulations.cerebellum.Cerebellum;
import org.simbrain.network.core.Network;
import org.simbrain.network.core.Neuron;
import org.simbrain.network.groups.NeuronGroup;
import org.simbrain.util.piccolo.TileMap;
import org.simbrain.workspace.gui.SimbrainDesktop;
import org.simbrain.world.odorworld.entities.OdorWorldEntity;
import org.simbrain.world.odorworld.sensors.SmellSensor;

import javax.swing.*;

/**
 * to do
 *
 * @author tim meyer
 */
public class BehaviorismSim extends RegisteredSimulation {

    // References
    Network network;
    ControlPanel panel;
    Neuron cowNeuron;

    @Override
    public void run() {
        // Clear workspace
        sim.getWorkspace().clearWorkspace();

        NetBuilder net = sim.addNetwork(10, 10, 543, 545,
            "mousenetwork");
        network = net.getNetwork();
        NeuronGroup sensoryNet = net.addNeuronGroup(-9.25, 95.93, 3);
        sensoryNet.setClamped(true);
        sensoryNet.setLabel("Sensory");
        Neuron cheeseNeuron = sensoryNet.getNeuronList().get(0);
        cheeseNeuron.setLabel("Cheese");
        Neuron flowerNeuron = sensoryNet.getNeuronList().get(1);
        flowerNeuron.setLabel("Flower");
        cowNeuron = sensoryNet.getNeuronList().get(2); //what does the number in get refer to?
        cowNeuron.setLabel("Cow");


        // Set up odor world
        OdorWorldBuilder world = sim.addOdorWorld(547, 5, 504, 548, "World");
        world.getWorld().setObjectsBlockMovement(false);
        world.getWorld().setTileMap(new TileMap("empty.tmx"));
        OdorWorldEntity mouse = world.addEntity(120, 245, OdorWorldEntity.EntityType.MOUSE);
        mouse.addSensor(new SmellSensor(mouse, "Smell", 0,0));

        mouse.setHeading(90);
        OdorWorldEntity cheese = world.addEntity(120, 180, OdorWorldEntity.EntityType.SWISS, new double[] {1, .5, .2});
        cheese.getSmellSource().setDispersion(65);
        OdorWorldEntity flower = world.addEntity(320, 180, OdorWorldEntity.EntityType.FLOWER, new double[] {.2, 1, .3});
        flower.getSmellSource().setDispersion(65);
        OdorWorldEntity cow = world.addEntity(320, 180, OdorWorldEntity.EntityType.COW , new double[] {.2, .3, 1});
        cow.getSmellSource().setDispersion(65);

        setUpControlPanel();

        // Make couplings
        sim.couple((SmellSensor) mouse.getSensor("Smell"),sensoryNet);

    }

    /**
     * Set up the controls.
     */
    void setUpControlPanel() {
        panel = ControlPanel.makePanel(sim, "Train / Test", 5, 10);
        NeuronGroup inputs = (NeuronGroup) network
                .getGroupByLabel("From Spinal Cord");
        Neuron target = network.getNeuronByLabel("Target");
        Neuron dopamine = network.getNeuronByLabel("Basal Ganglia (GPi)");

        // Just give the input of each to the model, without giving it a target
        // (and hence no Dopamine)
        panel.addButton("Activate cow", () -> {
            cowNeuron.setInputValue(5);
            System.out.println("worked");
        });


        panel.pack();

    }


    public BehaviorismSim(SimbrainDesktop desktop) {
        super(desktop);
    }

    public BehaviorismSim() {
        super();
    }

    @Override
    public String getName() {
        return "Behaviorism Simulation";
    }

    @Override
    public BehaviorismSim instantiate(SimbrainDesktop desktop) {
        return new BehaviorismSim(desktop);
    }

}