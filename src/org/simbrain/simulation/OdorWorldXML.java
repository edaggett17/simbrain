package org.simbrain.simulation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Convenience class for reading an xml file containing a description of a
 * world. Facilitates a simple xml representation of a world.
 *
 * An example xml representation is at the bottom of this document.
 */
@XmlRootElement(name = "entityList")
//CHECKSTYLE:OFF
public class OdorWorldXML {

    @XmlElement(name = "entity")
    private List<EntityDescription> entities = new ArrayList<EntityDescription>();

    /**
     * Simple description of an odor world entity.
     */
    public static class EntityDescription {
        public String imageName;
        public int x;
        public int y;
        public String stim;
        public double dispersion;
    }

    /**
     * @return the entities
     */
    public List<EntityDescription> getEntities() {
        return entities;
    }

}

//
// Sample xml file
//
//<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
//<entityList>
//    <entity>
//        <imageName>Swiss.gif</imageName>
//        <x>215</x>
//        <y>29</y>
//        <stim>0,1,0,0,0,1</stim>
//        <dispersion>400</dispersion>
//    </entity>
//    <entity>
//        <imageName>Swiss.gif</imageName>
//        <x>215</x>
//        <y>215</y>
//        <stim>0,1,0,0,0,1</stim>
//        <dispersion>400</dispersion>
//    </entity>
//</entityList>