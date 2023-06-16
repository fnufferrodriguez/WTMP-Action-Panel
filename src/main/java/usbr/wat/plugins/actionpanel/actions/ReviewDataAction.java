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
import javax.swing.JOptionPane;

import usbr.wat.plugins.actionpanel.ActionPanelPlugin;
import usbr.wat.plugins.actionpanel.ActionsWindow;
import usbr.wat.plugins.actionpanel.editors.UsbrG2dDialog;

/**
 * @author Mark Ackerman
 *
 */
@SuppressWarnings("serial")
public class ReviewDataAction extends AbstractAction
{
	public ReviewDataAction()
	{
		super("Review Data");
		setEnabled(false);
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		ActionsWindow parent = ActionPanelPlugin.getInstance().getActionsWindow();
		if (parent.getSimulationGroup() == null )
		{
			JOptionPane.showMessageDialog(parent,"Please create or select a Simulation Group first",
					"No Simulation Group Selected", JOptionPane.INFORMATION_MESSAGE);
			return ;
			
		}
		UsbrG2dDialog dialog = new UsbrG2dDialog(parent);
		dialog.setVisible(true);
	}

}
