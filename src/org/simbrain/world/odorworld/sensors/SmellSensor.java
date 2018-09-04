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
package org.simbrain.world.odorworld.sensors;

import org.simbrain.util.UserParameter;
import org.simbrain.util.environment.SmellSource;
import org.simbrain.util.math.SimbrainMath;
import org.simbrain.util.propertyeditor2.EditableObject;
import org.simbrain.workspace.Producible;
import org.simbrain.world.odorworld.entities.OdorWorldEntity;

/**
 * A sensor which is updated based on the presence of SmellSources near it.
 *
 * @see org.simbrain.util.environment.SmellSource
 */
public class SmellSensor extends Sensor {

    /**
     * Default label.
     */
    public static final String DEFAULT_LABEL = "SmellSensor";

    /**
     * Angle of whisker in radians.
     */
    public static double DEFAULT_THETA = Math.PI / 4;

    /**
     * Initial length of mouse whisker.
     */
    public static final double DEFAULT_RADIUS = 23;

    /**
     * Relative location of the sensor in polar coordinates.
     */
    @UserParameter(label = "Sensor angle", description = "The angle at which the smell sensor will be added. "
            + "A sensor angle of 0 a smell sensor that is directly in front of the agent. "
            + "A positive sensor angle locates the sensor at a position to the left of the agent's heading. "
            + "A negative sensor angle locates the sensor at a position to the right of the agent's heading.",
            defaultValue = "" + (Math.PI / 4), order = 3)
    private double theta = DEFAULT_THETA;

    /**
     * Relative location of the sensor in polar coordinates.
     */
    @UserParameter(label = "Sensor length",
            description = "The distance from the center of the entity to which the smell sensor is to be added."
                    + "A sensor length of 0 makes sensor angle irrelevant since located at the center of the agent.",
            defaultValue = "" + DEFAULT_RADIUS, order = 4)
    private double radius = DEFAULT_RADIUS;

    /**
     * Current value of this sensor, as an array of doubles.
     */
    // TODO: Settable numDims! Problem: what if the largest smell vector has more than
    //    10 components.
    private double[] currentValue = new double[10];

    /**
     * Construct a smell sensor.
     *
     * @param parent parent
     * @param label  label for this sensor (entity name will be added)
     * @param theta  offset from straight in degrees radians
     * @param radius length of "whisker"
     */
    public SmellSensor(final OdorWorldEntity parent, final String label, double theta, double radius) {
        super(parent, label);
        this.parent = parent;
        this.theta = theta;
        this.radius = radius;
    }

    /**
     * Construct a smell sensor.
     *
     * @param parent parent
     */
    public SmellSensor(final OdorWorldEntity parent) {
        super(parent, DEFAULT_LABEL);
        this.parent = parent;
    }

    /**
     * @return the location
     */
    public double[] getLocation() {
        // TODO: Formalize rule that this sensor applies to rotating entity
        // only,
        // or relax the code so that it will work for non-rotating entities
        OdorWorldEntity parent = this.getParent();
        double x = parent.getCenterLocation()[0] + (radius * Math.cos(parent.getHeadingRadians() + theta));
        double y = parent.getCenterLocation()[1] - (radius * Math.sin(parent.getHeadingRadians() + theta));
        return new double[]{x, y};
    }

    @Override
    public void update() {
        double[] temp = new double[currentValue.length];
        for (OdorWorldEntity entity : parent.getParentWorld().getEntityList()) {

            // Don't smell yourself
            if (entity != parent) {
                SmellSource smell = entity.getSmellSource();
                if (smell != null) {
                    temp = SimbrainMath.addVector(temp, smell.getStimulus(SimbrainMath.distance(getLocation(),
                        entity.getCenterLocation())));
                }
            }
        }
        currentValue = temp;
    }

    /**
     * @return the currentValue
     */
    @Producible(idMethod = "getId")
    public double[] getCurrentValues() {
        return currentValue;
    }

    /**
     * @return the theta
     */
    public double getTheta() {
        return theta;
    }

    /**
     * @param theta the theta to set
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public String getTypeDescription() {
        return "Smell";
    }

    @Override
    public EditableObject copy() {
        return new SmellSensor(parent, getLabel(), theta, radius);
    }

    @Override
    public String getName() {
        return "Smell";
    }
}
