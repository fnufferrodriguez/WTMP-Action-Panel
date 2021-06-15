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

/**
 * @author Mark Ackerman
 *
 */
@SuppressWarnings("serial")
public class PostResultsAction extends AbstractAction
{
	public PostResultsAction()
	{
		super("Post Results");
		setEnabled(false);   // for now
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		System.out.println("actionPerformed TODO implement me");

	}

}
