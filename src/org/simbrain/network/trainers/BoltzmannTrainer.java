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
package org.simbrain.network.trainers;

import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.Synapse;
import org.simbrain.network.subnetworks.BoltzmannMachine;

import java.util.HashMap;
import java.util.Map;

/**
 * A trainer for SOM Networks. Just goes through input data sets input node and
 * updates the SOM Group, which has the training code built in.
 * <p>
 * TODO: Possibly refactor to an "unsupervised trainer" class for use by
 * competitive too, which is similar.
 *
 * @author Jeff Yoshimi
 */
public class BoltzmannTrainer extends Trainer {

    /**
     * Reference to trainable network.
     */
    private final BoltzmannMachine network;

    /**
     * Flag used for iterative training methods.
     */
    private boolean updateCompleted = true;

    /**
     * Iteration number. An epoch.
     */
    private int iteration = 0;

    /**
     * Map of Synapse statistics.
     */
    private Map<Synapse,Integer> pcMap = new HashMap<>();


    private Map<Synapse,Integer> pfMap = new HashMap<>();


    /**
     * Construct the UnsupervisedNeuronGroupTrainer trainer.
     *
     * @param network the parent network
     */
    public BoltzmannTrainer(BoltzmannMachine network) {
        super(network);
        this.network = network;
        this.setIteration(0);

    }

    @Override
    public void apply() throws DataNotInitializedException {

        // Q: Shouldn't we check target data too?
        if (network.getTrainingSet().getInputData() == null) {
            throw new DataNotInitializedException("Input data not initalized");
        }

        int numRows = network.getTrainingSet().getInputData().length;
        for (int row = 0; row < numRows; row++) {
            double[] inputs = network.getTrainingSet().getInputData()[row];

            // Compute PC: given that visible units are clamped, compute
            // probabilty that a given pair of neurons are both on relative to the training set,
            // and at equilbrium
            network.getInputLayer().forceSetActivations(inputs);
            network.getInputLayer().setClamped(true);

            // Steps 4-10
            // Get network to equilibrium
            // Update network N(default value 10) times
            for (int i = 0; i < BoltzmannMachine.DEFAULT_INIT_SIZE; i++) {
                network.update();
            }

            // Gather statistics for clamped case
            // Steps 12-19
            for (Synapse synapse : network.getFlatSynapseList()) {
                pcMap.put(synapse, 0); //Q: all values in PC map are 0?
            }
            for (Neuron neuron : network.getFlatNeuronList()) {
                for (Synapse synapse : neuron.getFanIn()) {
                    if (synapse.getTarget().getActivation() > 0) {
                        pcMap.put(synapse, pcMap.get(synapse) + 1);
                        // increment synapse.aux by 1 //Q: if we do not increment aux in synapse, then how do we increment aux?
                    }
                }
            }

            // Compute PF: given that visible units are "free", compute
            // probabilty that a given pair of neurons are both on relative to the training set,
            // and at equilibrium

            network.getInputLayer().forceSetActivations(inputs);
            network.getInputLayer().setClamped(false);

            // Do the stuff above again for pfmap

            //Do I need for pfMap?
           /** for(int i = 0; i < BoltzmannMachine.DEFAULT_INIT_SIZE; i++){
               network.update();
            }**/

            for(Synapse synapse : network.getFlatSynapseList()){
                pfMap.put(synapse, 0);
            }

            for(Neuron neuron : network.getFlatNeuronList()){
                for(Synapse synapse : neuron.getFanIn()){
                    //if(synapse.getTarget().getActivation() > 0){
                    if(pcMap.get(synapse) == null){ // only those that are not clampped
                        //if doesn't exist, insert and set count to 1
                        pfMap.put(synapse, pfMap.get(synapse));
                    }
                }
            }

            for (Synapse synapse : network.getFlatSynapseList()) {
                synapse.incrementWeight();
                // Increment that synapse in the pcmap //Q what does it mean?
            }

            // Step 36-37: print PC & PF
            for (Synapse synapse : network.getFlatSynapseList()) {
                System.out.println("PC " + synapse.getId() + ":" + pcMap.get(synapse));
                System.out.println("PF " + synapse.getId() + ":" + pfMap.get(synapse));
            }
           // network.getBol.update(); // Call a function here to be overriden in subclasses?
        }
        // Q: Doing it here or should it be done after revalidateSynapseGroups()?


        incrementIteration();

        // Make sure excitatory/inhibitory are in proper lists
        revalidateSynapseGroups();

    }

    /**
     * @return boolean updated completed.
     */
    public boolean isUpdateCompleted() {
        return updateCompleted;
    }

    /**
     * Sets updated completed value.
     *
     * @param updateCompleted Updated completed value to be set
     */
    public void setUpdateCompleted(final boolean updateCompleted) {
        this.updateCompleted = updateCompleted;
    }

    /**
     * Increment the iteration number by 1.
     */
    public void incrementIteration() {
        iteration++;
    }

    /**
     * @param iteration the iteration to set
     */
    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    /**
     * Return the current iteration.
     *
     * @return current iteration.
     */
    public int getIteration() {
        return iteration;
    }

}
