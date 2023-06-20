/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import usbr.wat.plugins.actionpanel.ActionPanelPlugin;
import usbr.wat.plugins.actionpanel.ActionsWindow;
import usbr.wat.plugins.actionpanel.extract.ui.ExtractDialog;
import usbr.wat.plugins.actionpanel.model.AbstractSimulationGroup;

/**
 * @author Mark Ackerman
 *
 */
@SuppressWarnings("serial")
public class UpdateDataAction extends AbstractAction
{
	public UpdateDataAction()
	{
		super("Get/Update Data");
		setEnabled(false);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		updateData(ActionPanelPlugin.getInstance().getActionsWindow().getSimulationGroup());
	}
	/**
	 * @param simulationGroup
	 */
	public void updateData(AbstractSimulationGroup simulationGroup)
	{
		ExtractDialog dlg = new ExtractDialog(ActionPanelPlugin.getInstance().getActionsWindow(), simulationGroup);
		dlg.setVisible(true);
	}

}
