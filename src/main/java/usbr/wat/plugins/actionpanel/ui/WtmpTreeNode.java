/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui;

import javax.swing.tree.MutableTreeNode;

import com.rma.client.Browser;
import com.rma.client.BrowserFrame;
import com.rma.model.ManagerProxy;
import com.rma.model.Project;
import com.rma.ui.ProjectTree;
import com.rma.ui.ProjectTreeNode;

import hec2.wat.client.WatFrame;

import usbr.wat.plugins.actionpanel.SimGroupContainerNode;

/**
 * @author mark
 *
 */
@SuppressWarnings("serial")
public class WtmpTreeNode extends ProjectTreeNode
{
	/**
	 * @param currentProject
	 * @param object
	 */
	public WtmpTreeNode(Project currentProject, MutableTreeNode parent)
	{
		super(currentProject, parent);
	}
	@Override
	public void buildTree()
	{
		removeAllChildren();
		if ( getProject() == null )
		{
			return;
		}
		SimGroupContainerNode containerNode = new SimGroupContainerNode();
		add(containerNode);
		
		getProject().addManagerListener(this);
		
	}
	
	@Override
	protected ProjectTree getProjectTree()
	{
		BrowserFrame browserFrame = Browser.getBrowserFrame();
		if ( browserFrame != null )
		{
			return ((WatFrame)browserFrame).getSchematicTree();
		}
		return null;
	}
	@Override
	public void managerAdded(ManagerProxy proxy)
	{
		if ( proxy == null )
		{
			return;
		}
		
		
	}
	@Override
	public void managerDeleted(ManagerProxy proxy)
	{
		if ( proxy == null )
		{
			return;
		}
	}
	
}
