package com.hito4t.iot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import modbuspal.generator.Generator;

import org.w3c.dom.NodeList;

public abstract class AbstractOmron2JCIE_BU01Generator extends Generator {

    private static Logger logger = Logger.getLogger(AbstractOmron2JCIE_BU01Generator.class.getName());

    private static boolean initialized;
    private static Omron2JCIE_BU01 sensor;
	private static long lastUpdatedTime = Long.MIN_VALUE;

	protected static synchronized Omron2JCIE_BU01 getSensor() {
		try {
			if (!initialized) {
				initialized = true;

				Properties properties = new Properties();
				try (InputStream in = new FileInputStream("modbuspal.properties")) {
					properties.load(in);
				}

				String portName = properties.getProperty("Omron2JCIE_BU01.port");
				logger.info("Omron2JCIE_BU01 port = " + portName);
				sensor = new Omron2JCIE_BU01(portName);
			}


			if (sensor != null && lastUpdatedTime + 1000 <= System.currentTimeMillis()) {
				try {
					sensor.update();
				} catch (IOException e) {
					e.printStackTrace();
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
				lastUpdatedTime = System.currentTimeMillis();
			}
			return sensor;

		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, e.getMessage(), e);
			//logger.severe(toString(e));
			//throw new RuntimeException(e);
			return null;
		}
	}

	@Override
	public double getValue(double time) {
		Omron2JCIE_BU01 sensor = getSensor();
		if (sensor == null) {
			return 0;
		}
		return getValue(sensor);
	}

	protected abstract double getValue(Omron2JCIE_BU01 sensor);

	private static String toString(Exception exception) {
		try {
			StringWriter writer = new StringWriter();
			exception.printStackTrace(new PrintWriter(writer));
			writer.close();
			return writer.toString();
		} catch (IOException e) {
			return exception.toString();
		}
	}


	@Override
	public void loadGeneratorSettings(NodeList list) {
	}

	@Override
	public void saveGeneratorSettings(OutputStream out) throws IOException {
	}

	@Override
	public String getClassName() {
		return getClass().getSimpleName();
	}

}
