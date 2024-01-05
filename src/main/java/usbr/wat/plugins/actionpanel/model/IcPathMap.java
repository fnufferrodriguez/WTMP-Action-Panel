/*
 *
 *  * Copyright 2023 United States Bureau of Reclamation (USBR).
 *  * United States Department of the Interior
 *  * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 *  * Source may not be released without written approval
 *  * from USBR
 *
 */

package usbr.wat.plugins.actionpanel.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.flogger.FluentLogger;
import com.rma.io.FileManagerImpl;
import com.rma.io.RmaFile;
import hec.heclib.dss.DSSPathname;
import hec.hecmath.DSS;
import hec.io.DSSIdentifier;
import hec2.model.DataLocation;
import hec2.model.DssDataLocation;
import hec2.wat.model.WatSimulation;
import usbr.wat.plugins.actionpanel.model.forecast.DssPathMap;
import usbr.wat.plugins.actionpanel.model.forecast.DssPathMapItem;
import usbr.wat.plugins.actionpanel.model.forecast.InitialConditions;
import usbr.wat.plugins.actionpanel.model.forecast.Profile;

/**
 * class to read the Initial Conditions config file
 */
public class IcPathMap
{
	private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();
	private final WatSimulation _sim;
	private final String _configFile;
	private final InitialConditions _ic;
	List<IcPathMapItem> _dssPathMapList = new ArrayList<>();
	private String _sourceDssFile;
	private String _sourceDssFPart;

	public IcPathMap(WatSimulation sim, String icConfigPath, InitialConditions ic)
	{
		super();
		_configFile = icConfigPath;
		_sim = sim;
		_ic = ic;
	}
	public boolean readDssPathsFile()
	{
		RmaFile file = FileManagerImpl.getFileManager().getFile(_configFile);
		if ( !file.exists())
		{
			_sim.addErrorMessage("DSS Paths Map file "+file.getAbsolutePath()+" doesn't exist.");
			return false;
		}
		BufferedReader reader = file.getBufferedReader();
		if ( reader == null )
		{
			_sim.addErrorMessage("Failed to get reader for DSS Paths map file "+file.getAbsolutePath());
			return false;
		}
		String line;
		String[] parts;
		_dssPathMapList = new ArrayList<>();
		IcPathMapItem dssPathMapItem;
		//Location, parameter, Source DSS file, Source DSS record, Number of Destinations, Destination DSS file, Destination DSS record, ...
		try
		{
			reader.readLine();
			while ((line = reader.readLine()) != null)
			{
				if ( line.trim().isEmpty()|| line.startsWith("#")) // empty or comment
				{
					continue;
				}
				parts = line.split(",");
				if (parts == null || parts.length < IcPathMapItem.MIN_NUM_PARTS)
				{
					LOGGER.atWarning().log("Invalid line found: "+line);
					continue;
				}
				if ( line.startsWith("#"))
				{
					continue;
				}
				dssPathMapItem = new IcPathMapItem();
				if (dssPathMapItem.parseLine(parts))
				{
					_dssPathMapList.add(dssPathMapItem);
				}
			}
			return true;
		}
		catch ( IOException ioe)
		{
			LOGGER.atWarning().withCause(ioe).log("Error reading "+file.getAbsolutePath());
			_sim.addErrorMessage("Error reading DSS Paths map file "+file.getAbsolutePath()+" error:"+ioe);
			return false;
		}
		finally
		{
			try
			{
				reader.close();
			} catch (IOException e)
			{ }
		}

	}

	public DSSIdentifier getSourceDSSIdentifierFor(String reservoirName)
	{
		if ( reservoirName == null )
		{
			return null;
		}
		Profile profile = _ic.getSelectedProfile(reservoirName);
		String dssFile = profile.getDssFileName();
		String dssPath = profile.getDssPath();
		DSSIdentifier srcDssId = new DSSIdentifier(dssFile, dssPath);
		return srcDssId;
	}







	public List<DSSIdentifier> getDestDssIdentifiersFor(String reservoirName)
	{
		List<DSSIdentifier>destDssIds = new ArrayList<>();
		if ( reservoirName == null )
		{
			return destDssIds;
		}
		IcPathMapItem dssItem;
		DSSIdentifier destDssId;
		for (int i = 0;i < _dssPathMapList.size(); i++ )
		{
			dssItem = _dssPathMapList.get(i);
			String resName = dssItem.getReservoirName();

			if ( reservoirName.equalsIgnoreCase(resName))
			{
				for(int j = 0;j < dssItem.getNumberOfDests(); j++ )
				{
					destDssId = new DSSIdentifier(dssItem.getDestDssFile(j), dssItem.getDestDssPath(j));
					if ( !destDssIds.contains(destDssId))
					{
						destDssIds.add(destDssId);
					}
				}
			}
		}
		return destDssIds;
	}
}
