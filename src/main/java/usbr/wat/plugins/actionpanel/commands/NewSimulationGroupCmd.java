/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.commands;

import java.util.List;

import com.rma.commands.AbstractNewManagerCommand;
import com.rma.io.FileManagerImpl;
import com.rma.io.RmaFile;
import com.rma.model.Project;

import hec2.wat.model.WatAnalysisPeriod;
import hec2.wat.model.WatSimulation;
import hec2.wat.model.WatSimulationContainer;

import rma.util.RMAIO;
import usbr.wat.plugins.actionpanel.model.SimulationGroup;

/**
 * @author Mark Ackerman
 *
 */
public class NewSimulationGroupCmd extends AbstractNewManagerCommand
{
	private List<WatSimulation> _sims;
	private WatAnalysisPeriod _ap;

	/**
	 * @param project
	 * @param name
	 * @param descr
	 * @param dir
	 */
	public NewSimulationGroupCmd(Project project, String name, String descr,
			RmaFile dir, WatAnalysisPeriod ap, List<WatSimulation>sims)
	{
		super(project, name, descr, dir);
		_ap = ap;
		_sims = sims;
		
	}

	@Override
	public String getExtension()
	{
		return SimulationGroup.FILE_EXT;
	}

	@Override
	public String getManagerClass()
	{
		return SimulationGroup.class.getName();
	}

	@Override
	public String getManagerType()
	{
		return "Simulation Group";
	}

	/**
	 * @return
	 */
	public SimulationGroup getSimulationGroup()
	{
		return (SimulationGroup) getManager();
	}

	@Override
	public boolean doCommand()
	{
		boolean rv = super.doCommand();
		SimulationGroup simgroup = getSimulationGroup();
		if ( simgroup != null )
		{
			simgroup.setAnalysisPeriod(_ap);
		}
		WatSimulation sim;
		WatSimulationContainer container ;
		
		for (int i = 0;i < _sims.size(); i++ )
		{
			sim = _sims.get(i);
			
			String newSimName = sim.getName()+"-"+simgroup.getName();
			container = new WatSimulationContainer();
			container.setProgramOrder(sim.getProgramOrder());
			container.setName(newSimName);
			container.setProject(_project);
			String fileName = _project.getProjectDirectory();
			fileName = RMAIO.concatPath(fileName, "wat");
			fileName = RMAIO.concatPath(fileName, "sims");
			fileName = RMAIO.concatPath(fileName, RMAIO.userNameToFileName(newSimName).concat(".container"));
			container.setFile(FileManagerImpl.getFileManager().getFile(fileName));
			container.setAnalysisPeriod(_ap);
			container.setAlternative(sim.getContainerParent().getAlternative());
			_project.addManager(container);
			
			WatSimulation newSim = new WatSimulation();
			sim.setProject(_project);
			// read in the original simulation's data
			newSim.setFile(sim.getFile());
			newSim.readData();
			newSim.setName(newSimName);
			fileName = _project.getProjectDirectory();
			fileName = RMAIO.concatPath(fileName, "wat");
			fileName = RMAIO.concatPath(fileName, "sims");
			fileName = RMAIO.concatPath(fileName, RMAIO.userNameToFileName(newSimName).concat(".simulation"));
			
			newSim.setFile(FileManagerImpl.getFileManager().getFile(fileName));
			
			newSim.setSimulationContainer(container);
			container.addSimulation(newSim);
			_project.addManager(newSim);
			
			
			simgroup.addSimulation(newSim);
		}	
		_project.saveProject();
		return rv;
	}

}
