/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel;

import java.awt.EventQueue;

import javax.swing.JMenu;

import com.rma.client.Browser;
import com.rma.event.ProjectAdapter;
import com.rma.event.ProjectEvent;
import com.rma.model.Project;

import usbr.wat.plugins.actionpanel.actions.ActionWindowAction;

/**
 * @author Mark Ackerman
 *
 */
public class ActionPanelPlugin
{
	private static ActionPanelPlugin _instance;
	
	private ActionsWindow _actionsWindow;
	public ActionPanelPlugin()
	{
		super();
		addToProjectOpening();
		addToToolsMenu();
	}

	/**
	 * 
	 */
	private void addToToolsMenu()
	{
		JMenu toolsMenu = Browser.getBrowserFrame().getToolsMenu();
		if ( toolsMenu != null )
		{
			toolsMenu.add(new ActionWindowAction());
		}
	}

	/**
	 * 
	 */
	private void addToProjectOpening()
	{
		Project.addStaticProjectListener(new ProjectAdapter()
		{

			@Override
			public void projectLoaded(ProjectEvent e)
			{
				EventQueue.invokeLater(()-> displayActionsWindow());
			}
			
			

			@Override
			public void projectClosed(ProjectEvent e)
			{
				if ( _actionsWindow != null )
				{
					_actionsWindow.setVisible(false);
					_actionsWindow = null;
				}
			}
		});
	}
	
	public void displayActionsWindow()
	{
		if ( _actionsWindow == null )
		{
			_actionsWindow = new ActionsWindow(Browser.getBrowserFrame());
			_actionsWindow.setLocationRelativeTo(Browser.getBrowserFrame());
		}
		_actionsWindow.setVisible(true);
	}
	/**
	 * 
	 * @return
	 */
	public ActionsWindow getActionsWindow()
	{
		return _actionsWindow;
	}

	public static void main(String[] args)
	{
		_instance = new ActionPanelPlugin();
		
	}
	
	public static ActionPanelPlugin getInstance()
	{
		return _instance;
	}
}
