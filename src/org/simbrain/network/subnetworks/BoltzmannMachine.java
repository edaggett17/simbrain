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

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.simbrain.network.connections.RadialSimple;
import org.simbrain.network.core.Network;
import org.simbrain.network.core.Neuron;
import org.simbrain.network.core.Synapse;
import org.simbrain.network.groups.Group;
import org.simbrain.network.groups.NeuronGroup;
import org.simbrain.network.groups.Subnetwork;
import org.simbrain.network.layouts.GridLayout;
import org.simbrain.network.trainers.Trainable;
import org.simbrain.network.trainers.TrainingSet;
import org.simbrain.network.util.NetworkLayoutManager;
import org.simbrain.network.util.NetworkLayoutManager.Direction;

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
    private final NeuronGroup hiddenUnits;

    /** The input layer. */
    private final NeuronGroup visibleUnits;

    /** Training set. */
    private final TrainingSet trainingSet = new TrainingSet();

    /**
     * Construct a Boltzmann Machine Network.
     *
     * @param net parent network. Set to null when this is used simply as a
     *            holder for param values.
     * @param numVisibleNeurons number of neurons in the input layer
     * @param numHiddenNeurons number of neurons in the Boltzmann layer.
     * @param initialPosition bottom corner where network will be placed.
     */
    public BoltzmannMachine(Network net, int numVisibleNeurons, int numHiddenNeurons,
                      Point2D initialPosition) {
        super(net);
        this.setLabel("Boltzmann Machine");

        // Boltzmann machine
        hiddenUnits = new NeuronGroup(net, initialPosition, numHiddenNeurons);
        hiddenUnits.setLayoutBasedOnSize();
        hiddenUnits.setLabel("Hidden Units");
        this.addNeuronGroup(hiddenUnits);

        // Set up input layer
        visibleUnits = new NeuronGroup(net, initialPosition, numVisibleNeurons);
        visibleUnits.setLayoutBasedOnSize();
        this.addNeuronGroup(visibleUnits);
        visibleUnits.setLabel("Visible Units");
        visibleUnits.setClamped(true);

        // Layout groups
        NetworkLayoutManager.offsetNeuronGroup(visibleUnits, hiddenUnits,
                Direction.EAST, 100);

        // Wire up network
        RadialSimple connection = new RadialSimple(net, hiddenUnits.getNeuronList());
        connection.setExcitatoryRadius(100);
        connection.setExcitatoryProbability(1);
        connection.setInhibitoryProbability(0);
        connection.connectNeurons(true); //TODO: Use synapse group?

    }

    @Override
    public void update(){

        hiddenUnits.clearActivations();

        // Choose randomly
        Neuron chosenNode = hiddenUnits.getNeuronList()
                .get(ThreadLocalRandom.current().nextInt(0,
                        hiddenUnits.getNeuronList().size()));

        int summedFanIn = 0;

        for (Synapse fanInWeight : chosenNode.getFanIn()) {
            summedFanIn += fanInWeight.getStrength()
                    * fanInWeight.getSource().getActivation();
        }

        double delta_c = 1 - 2 * chosenNode.getActivation() + summedFanIn;
        double acceptChangeProb = 1 / (1 + Math.exp(-delta_c / temperature));

        if (Math.random() < acceptChangeProb) {
            chosenNode.setActivation(1 - chosenNode.getActivation());
            this.temperature = .95 * temperature;
        }

        System.out.println(temperature);
    }

    @Override
    public List<Neuron> getInputNeurons() {
        return visibleUnits.getNeuronList();
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
        return visibleUnits;
    }

    @Override
    public Group getNetwork() {
        return this;
    }

    /**
     * @return the temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * @param temperature the temperature to set
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
