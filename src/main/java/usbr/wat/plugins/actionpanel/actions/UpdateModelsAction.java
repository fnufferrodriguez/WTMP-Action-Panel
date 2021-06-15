/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.actions;

import java.awt.event.ActionEvent;

/**
 * @author Mark Ackerman
 *
 */
@SuppressWarnings("serial")
public class UpdateModelsAction extends BaseActionsPanelAction
{
	public UpdateModelsAction()
	{
		super("Get/Update Models");
		setEnabled(false);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		System.out.println("actionPerformed TODO implement me");

	}

}
