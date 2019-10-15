package com.hito4t.iot;


public class OmronTemperatureGenerator extends AbstractOmron2JCIE_BU01Generator {

	@Override
	protected double getValue(Omron2JCIE_BU01 sensor) {
		return sensor.getTemperature() * 100;
	}

}
