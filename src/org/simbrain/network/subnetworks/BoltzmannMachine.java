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
package org.simbrain.network.subnetworks;

import org.simbrain.network.core.Network;
import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.Synapse;
import org.simbrain.network.groups.Group;
import org.simbrain.network.groups.NeuronGroup;
import org.simbrain.network.groups.Subnetwork;
import org.simbrain.network.trainers.Trainable;
import org.simbrain.network.trainers.TrainingSet;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;

/**
 * <b>BoltzmannMachine</b> An input layer and input data
 * have been added so that the Boltzmann Machine can be easily trained
 * using existing Simbrain GUI tools
 *
 * TODO: Review and correct comments.
 *
 * @author Jeff Yoshimi
 */
public class BoltzmannMachine  extends Subnetwork implements Trainable {

    /** Default initial temperature */
    public static final double DEFAULT_INIT_SIZE = 10;


    /** Temperature */
    private double temperature = DEFAULT_INIT_SIZE;


    /** The input layer. */
    private final NeuronGroup inputLayer;

    /** Training set. */
    private final TrainingSet trainingSet = new TrainingSet();

    /**
     * Construct a Boltzmann Machine Network.
     *
     * @param net parent network. Set to null when this is used simply as a
     *            holder for param values.
     * @param numBoltzmannNeurons number of neurons in the Boltzmann layer.
     * @param numInputNeurons number of neurons in the input layer
     * @param initialPosition bottom corner where network will be placed.
     */
    public BoltzmannMachine(Network net, int numBoltzmannNeurons, int numInputNeurons,
                      Point2D initialPosition) {
        super(net);
        this.setLabel("Boltzmann Machine");
        inputLayer = new NeuronGroup(net, initialPosition, numInputNeurons);
        inputLayer.setLayoutBasedOnSize();
        if (net == null) {
            return;
        }
        this.addNeuronGroup(inputLayer);
        for (Neuron neuron : inputLayer.getNeuronList()) {
            neuron.setLowerBound(0);
        }
        inputLayer.setLabel("Input layer");
        inputLayer.setClamped(true);
        layoutNetwork();
    }

    @Override
    public void update(){
        int x = 0;

        Neuron chosenNode = getFlatNeuronList().get(x);

        int summedFanIn=0;

        for(Synapse fanInWeight: chosenNode.getFanIn()){
            summedFanIn += fanInWeight.getStrength() * fanInWeight.getSource().getActivation();
        }

        double delta_c = 1 - 2 * chosenNode.getActivation() + summedFanIn;
        double acceptChangeProb = 1/(1 + Math.exp(-delta_c/temperature));

        if (Math.random() < acceptChangeProb) {
            chosenNode.setActivation(1 - chosenNode.getActivation());
            this.temperature = .95 * temperature;
        }
    }

    /**
     * Set the layout of the network.
     */
    public void layoutNetwork() {
        // TODO: Would be easy to set the layout and redo it...
       // NetworkLayoutManager.offsetNeuronGroup(inputLayer, som,
                //Direction.NORTH, 250);
    }

    @Override
    public List<Neuron> getInputNeurons() {
        return inputLayer.getNeuronList();
    }

    @Override
    public List<Neuron> getOutputNeurons() {
        //TODO: implement
        return Collections.emptyList();
    }

    @Override
    public TrainingSet getTrainingSet() {
        return trainingSet;
    }

    @Override
    public void initNetwork() {
        // TODO: implement
    }

    /**
     * @return the inputLayer
     */
    public NeuronGroup getInputLayer() {
        return inputLayer;
    }

    @Override
    public Group getNetwork() {
        return this;
    }
}
