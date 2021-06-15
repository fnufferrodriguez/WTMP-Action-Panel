/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.actions;

import javax.swing.AbstractAction;

/**
 * @author Mark Ackerman
 *
 */
@SuppressWarnings("serial")
public abstract class BaseActionsPanelAction extends AbstractAction
{
	public BaseActionsPanelAction(String text)
	{
		super(text);
		
	}

}
