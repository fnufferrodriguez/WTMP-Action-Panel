/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.model;

import hec2.wat.model.WatSimulation;

/**
 * class to hold the simulations/results information for the reports
 * @author mark
 *
 */
public class SimulationReportInfo
{
	private String _name;
	private WatSimulation _sim;
	private String _simFolder;
	private String _simDssFile;
	private String _lastComputedDate;

	public SimulationReportInfo()
	{
		super();
	}

	/**
	 * the name for the report
	 * @return
	 */
	public String getName()
	{
		return _name;
	}

	/**
	 * the simulation
	 * @return
	 */
	public WatSimulation getSimulation()
	{
		return _sim;
	}

	/**
	 * the simulation's output folder
	 * @return
	 */
	public String getSimFolder()
	{
		return _simFolder;
	}

	/**
	 * the simulation's output dss file
	 * @return
	 */
	public String getSimDssFile()
	{
		return _simDssFile;
	}

	/**
	 * @return
	 */
	public String getLastComputedDate()
	{
		return _lastComputedDate;
	}

	/**
	 * @param sim
	 */
	public void setSimulation(WatSimulation sim)
	{
		_sim = sim;
	}

	/**
	 * @param simulationDssFile
	 */
	public void setSimDssFile(String simulationDssFile)
	{
		_simDssFile = simulationDssFile;
	}

	/**
	 * @param simulationDirectory
	 */
	public void setSimFolder(String simulationDirectory)
	{
		_simFolder = simulationDirectory;
	}

	/**
	 * @param name
	 */
	public void setName(String name)
	{
		_name = name;
	}

	/**
	 * @param lastComputedDate
	 */
	public void setLastComputedDate(String lastComputedDate)
	{
		_lastComputedDate  = lastComputedDate;
	}
}
