/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;

import javax.swing.JButton;

import com.rma.event.ProjectEvent;
import com.rma.model.Project;
import com.rma.ui.ContentTree;
import com.rma.ui.ProjectTab;
import com.rma.ui.ProjectTree;

import rma.swing.RmaInsets;
import usbr.wat.plugins.actionpanel.actions.ActionWindowAction;

/**
 * @author mark
 *
 */
@SuppressWarnings("serial")
public class ActionsProjectTab extends ProjectTab
{
	private static ActionsProjectTab _instance;
	public ActionsProjectTab()
	{
		super();
		_instance = this;
	}
	@Override
	protected ProjectTree createProjectTree(ContentTree contentTree)
	{
		return new WtmpTree(contentTree);
	}

	public void projectOpened(ProjectEvent evt)
	{
		Project proj = evt.getProject();
		EventQueue.invokeLater(()->getProjectTree().expandAll());
	}

	/**
	 * 
	 */
	@Override
	protected void buildControls()
	{
		super.buildControls();
		
		ActionWindowAction action = new ActionWindowAction();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx     = 0;
		gbc.gridy     = 10;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.SOUTH;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5555;
		add(new JButton(action), gbc);
	}

	
	/**
	 * @return
	 */
	public static ActionsProjectTab getTab()
	{
		return _instance;
	}
	
}
