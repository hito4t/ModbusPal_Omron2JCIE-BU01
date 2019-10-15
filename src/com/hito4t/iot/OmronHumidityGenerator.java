package com.hito4t.iot;


public class OmronHumidityGenerator extends AbstractOmron2JCIE_BU01Generator {

	@Override
	protected double getValue(Omron2JCIE_BU01 sensor) {
		return sensor.getHumidity() * 100;
	}

}
