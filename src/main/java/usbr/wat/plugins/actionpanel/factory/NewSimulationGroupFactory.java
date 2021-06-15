/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.factory;

import com.rma.commands.AbstractNewManagerCommand;
import com.rma.factories.AbstractNewManagerFactory;
import com.rma.io.RmaFile;
import com.rma.model.Project;
import com.rma.util.I18n;

/**
 * @author Mark Ackerman
 *
 */
public class NewSimulationGroupFactory extends AbstractNewManagerFactory
{
	/**
	 * @param info
	 */
	public NewSimulationGroupFactory(I18n info)
	{
		super(info);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected AbstractNewManagerCommand createCommand(Project proj, String name,
			String desc, RmaFile file)
	{
		return null;
	}
	
}
