/*
 * Part of Simbrain--a java-based neural network kit
 * Copyright (C) 2006 Jeff Yoshimi <www.jeffyoshimi.net>
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
package org.simbrain.world.visionworld.view;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import javax.swing.border.EmptyBorder;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;

import edu.umd.cs.piccolo.util.PPaintContext;

import org.dishevelled.disclosuretriangle.DisclosureTriangle;

import org.simbrain.world.visionworld.PixelMatrix;
import org.simbrain.world.visionworld.SensorMatrix;
import org.simbrain.world.visionworld.VisionWorld;
import org.simbrain.world.visionworld.VisionWorldModel;

import org.simbrain.world.visionworld.node.PixelMatrixImageNode;
import org.simbrain.world.visionworld.node.SensorMatrixNode;

/**
 * Normal view.
 */
public final class NormalView
    extends JPanel {

    /** Canvas. */
    private final PCanvas canvas;

    /** Vision world. */
    private final VisionWorld visionWorld;

    /** Pixel matrix node. */
    private PNode pixelMatrixNode;

    /** Map of sensor matrix to sensor matrix node. */
    private final Map<SensorMatrix, SensorMatrixNode> sensorMatrixNodes;

    /** Empty insets. */
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

    /** Field insets. */
    private static final Insets FIELD_INSETS = new Insets(0, 0, 6, 0);


    /**
     * Create a new normal view.
     *
     * @param visionWorld vision world, must not be null
     */
    public NormalView(final VisionWorld visionWorld) {
        super();
        if (visionWorld == null) {
            throw new IllegalArgumentException("visionWorld must not be null");
        }
        canvas = new PCanvas();
        canvas.setOpaque(true);
        canvas.setBackground(Color.WHITE);
        canvas.setDefaultRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        canvas.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        canvas.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        this.visionWorld = visionWorld;
        this.sensorMatrixNodes = new HashMap<SensorMatrix, SensorMatrixNode>();

        setLayout(new BorderLayout());
        add("Center", canvas);
        add("South", new DisclosureTriangle(createDetailsPanel()));

        createNodes();
    }

    /**
     * Create nodes.
     */
    private void createNodes() {
        VisionWorldModel model = visionWorld.getModel();
        pixelMatrixNode = new PixelMatrixImageNode(model.getPixelMatrix());
        canvas.getLayer().addChild(pixelMatrixNode);

        for (SensorMatrix sensorMatrix : model.getSensorMatrices()) {
            SensorMatrixNode sensorMatrixNode = new SensorMatrixNode(sensorMatrix);
            sensorMatrixNodes.put(sensorMatrix, sensorMatrixNode);
            canvas.getLayer().addChild(sensorMatrixNode);
        }
    }

    /**
     * Create and return the details panel.
     *
     * @return the details panel
     */
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        GridBagLayout l = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(l);
        panel.setBorder(new EmptyBorder(6, 6, 6, 6));

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridheight = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = EMPTY_INSETS;
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0;
        c.weightx = 1.0f;
        panel.add(new JLabel("Sensors:"), c);

        c.insets = FIELD_INSETS;
        c.gridy++;
        panel.add(new JScrollPane(new SensorMatrixTableEditor()), c);

        c.insets = EMPTY_INSETS;
        c.gridy++;
        panel.add(new JLabel("Pixels:"), c);

        c.insets = FIELD_INSETS;
        c.gridy++;
        panel.add(new JScrollPane(new PixelMatrixTableEditor()), c);

        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = EMPTY_INSETS;
        c.weighty = 1.0f;
        c.gridy++;
        panel.add(Box.createGlue(), c);

        return panel;
    }


    /**
     * Sensor matrix table editor.
     */
    private class SensorMatrixTableEditor
        extends JTable {

        /**
         * Create a new sensor matrix table editor.
         */
        SensorMatrixTableEditor() {
            super();
            setModel(new SensorMatrixTableEditorModel());
            setPreferredScrollableViewportSize(new Dimension(0, 6 * (getRowHeight() + (2 * getRowMargin()))));
            setDefaultRenderer(SensorMatrix.class, new DefaultTableCellRenderer());
        }
    }

    /**
     * Pixel matrix table editor.
     */
    private class PixelMatrixTableEditor
        extends JTable {

        /**
         * Create a new pixel matrix table editor.
         */
        PixelMatrixTableEditor() {
            super();
            setModel(new PixelMatrixTableEditorModel());
            setPreferredScrollableViewportSize(new Dimension(0, 2 * (getRowHeight() + (2 * getRowMargin()))));
            setDefaultRenderer(PixelMatrix.class, new DefaultTableCellRenderer());
        }
    }

    /**
     * Sensor matrix table editor model.
     */
    private class SensorMatrixTableEditorModel
        extends AbstractTableModel {

        /** {@inheritDoc} */
        public int getRowCount() {
            return visionWorld.getModel().getSensorMatrixCount();
        }

        /** {@inheritDoc} */
        public int getColumnCount() {
            return 6;
        }

        /** {@inheritDoc} */
        public Class getColumnClass(final int column) {
            switch (column) {
            case 0:
            case 1:
            case 2:
                return String.class;
            case 3:
                return Boolean.class;
            case 4:
                return Float.class;
            case 5:
                return SensorMatrix.class;
            default:
                return null;
            }
        }

        /** {@inheritDoc} */
        public String getColumnName(final int column) {
            switch (column) {
            case 0:
                return "Sensor matrix";
            case 1:
                return "Receptive fields";
            case 2:
                return "Receptive field dimensions";
            case 3:
                return "Visible";
            case 4:
                return "Transparency";
            case 5:
                return "Properties editor";
            default:
                return null;
            }
        }

        /** {@inheritDoc} */
        public boolean isCellEditable(final int row, final int column) {
            switch (column) {
            case 0:
            case 1:
            case 2:
                return false;
            case 3:
            case 4:
                return true;
            case 5:
                return false;
            default:
                return false;
            }
        }

        /** {@inheritDoc} */
        public void setValueAt(final Object value, final int row, final int column) {
            SensorMatrix sensorMatrix = visionWorld.getModel().getSensorMatrices().get(row);
            if (sensorMatrix != null) {
                switch (column) {
                case 0:
                case 1:
                case 2:
                    break;
                case 3:
                    sensorMatrixNodes.get(sensorMatrix).setVisible((Boolean) value);
                    break;
                case 4:
                    sensorMatrixNodes.get(sensorMatrix).setTransparency((Float) value);
                    break;
                case 5:
                    break;
                default:
                    break;
                }
            }
        }

        /** {@inheritDoc} */
        public Object getValueAt(final int row, final int column) {
            SensorMatrix sensorMatrix = visionWorld.getModel().getSensorMatrices().get(row);
            if (sensorMatrix != null) {
                switch (column) {
                case 0:
                    return "Sensor matrix";
                case 1:
                    return sensorMatrix.columns() + " x " + sensorMatrix.rows();
                case 2:
                    return sensorMatrix.getReceptiveFieldWidth() + " x " + sensorMatrix.getReceptiveFieldHeight();
                case 3:
                    return sensorMatrixNodes.get(sensorMatrix).getVisible();
                case 4:
                    return sensorMatrixNodes.get(sensorMatrix).getTransparency();
                case 5:
                    return sensorMatrix;
                default:
                    return null;
                }
            }
            return null;
        }
    }

    /**
     * Pixel matrix table editor model.
     */
    private class PixelMatrixTableEditorModel
        extends AbstractTableModel {

        /** {@inheritDoc} */
        public int getRowCount() {
            return 1;
        }

        /** {@inheritDoc} */
        public int getColumnCount() {
            return 5;
        }

        /** {@inheritDoc} */
        public Class getColumnClass(final int column) {
            switch (column) {
            case 0:
            case 1:
                return String.class;
            case 2:
                return Boolean.class;
            case 3:
                return Float.class;
            case 4:
                return PixelMatrix.class;
            default:
                return null;
            }
        }

        /** {@inheritDoc} */
        public String getColumnName(final int column) {
            switch (column) {
            case 0:
                return "Pixel matrix";
            case 1:
                return "Dimensions";
            case 2:
                return "Visible";
            case 3:
                return "Transparency";
            case 4:
                return "Properties editor";
            default:
                return null;
            }
        }

        /** {@inheritDoc} */
        public boolean isCellEditable(final int row, final int column) {
            switch (column) {
            case 0:
            case 1:
                return false;
            case 2:
            case 3:
                return true;
            case 4:
                return false;
            default:
                return false;
            }
        }

        /** {@inheritDoc} */
        public void setValueAt(final Object value, final int row, final int column) {
            SensorMatrix sensorMatrix = visionWorld.getModel().getSensorMatrices().get(row);
            if (sensorMatrix != null) {
                switch (column) {
                case 0:
                case 1:
                    break;
                case 2:
                    pixelMatrixNode.setVisible((Boolean) value);
                    break;
                case 3:
                    pixelMatrixNode.setTransparency((Float) value);
                    break;
                case 4:
                    break;
                default:
                    break;
                }
            }
        }

        /** {@inheritDoc} */
        public Object getValueAt(final int row, final int column) {
            PixelMatrix pixelMatrix = visionWorld.getModel().getPixelMatrix();
            switch (column) {
            case 0:
                return "Pixel matrix";
            case 1:
                return pixelMatrix.getWidth() + " x " + pixelMatrix.getHeight();
            case 2:
                return pixelMatrixNode.getVisible();
            case 3:
                return pixelMatrixNode.getTransparency();
            case 4:
                return pixelMatrix;
            default:
                return null;
            }
        }
    }
}
