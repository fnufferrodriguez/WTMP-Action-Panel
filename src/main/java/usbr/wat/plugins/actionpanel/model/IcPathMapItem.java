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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hec.heclib.dss.DSSPathname;
import hec.io.DSSIdentifier;
import hec.lang.NamedType;
import rma.util.RMAIO;

public class IcPathMapItem extends NamedType
{
	public static final int MIN_NUM_PARTS = 4;

	private List<DssItem> _destDssItems = new ArrayList<>();
	/** dest and source DSSIdentifiers */
	private Map<DSSIdentifier, DSSIdentifier> _dssIdMap = new HashMap<>();

	public IcPathMapItem()
	{
		super();
	}

	public String getReservoirName()
	{
		return getName();
	}


	public boolean parseLine(String[] parts)
	{
		if ( parts == null || parts.length < MIN_NUM_PARTS )
		{
			return false;
		}
		//Reservoir, Number of Destinations, Destination DSS file, Destination DSS record, ...
		// 0         1                        2                      3

		setName(parts[0].trim());

		int numDests = RMAIO.parseInt(parts[1].trim());
		for(int i =2; i< 2+numDests; i+=2)
		{
			DssItem dssItem = new DssItem(parts[i].trim(),parts[i+1].trim());
			_destDssItems.add(dssItem);
		}
		return true;
	}



	public int getNumberOfDests()
	{
		return _destDssItems.size();
	}

	public String getDestDssFile(int num)
	{
		return _destDssItems.get(num).getDssFile();
	}

	public String getDestDssPath(int num)
	{
		return _destDssItems.get(num).getDssPath();
	}

	public Map<DSSIdentifier, DSSIdentifier>getDssIdMap()
	{
		return _dssIdMap;
	}

	public static boolean dssPathsEqual(String path1, String path2)
	{
		DSSPathname src1 = new DSSPathname(path1);
		src1.setCollectionSequence(null);
		DSSPathname src2 = new DSSPathname(path2);
		src2.setCollectionSequence(null);
		return dssPathsEqual(src1, src2);
	}

	public static boolean dssPathsEqual(DSSPathname path1, DSSPathname path2)
	{
		return ( path1.getAPart().equalsIgnoreCase(path2.getAPart())
				&& path1.getBPart().equalsIgnoreCase(path2.getBPart())
				&& path1.getCPart().equalsIgnoreCase(path2.getCPart())
				&& path1.getEPart().equalsIgnoreCase(path2.getEPart())
				&& path1.getFPart().equalsIgnoreCase(path2.getFPart()) );
	}

	class DssItem
	{
		private String _dssFile;
		private String _dssPath;

		DssItem(String dssFile, String dssPath)
		{
			super();
			_dssFile = dssFile;
			_dssPath = dssPath;
		}

		public String getDssFile()
		{
			return _dssFile;
		}

		public String getDssPath()
		{
			return _dssPath;
		}

	}
}
