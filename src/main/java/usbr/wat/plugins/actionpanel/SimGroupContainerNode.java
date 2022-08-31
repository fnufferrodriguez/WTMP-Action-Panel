/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel;

import java.awt.EventQueue;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.rma.client.Browser;
import com.rma.event.ProjectAdapter;
import com.rma.event.ProjectManagerListener;
import com.rma.factories.ProjectNodeFactory;
import com.rma.model.ManagerProxy;
import com.rma.model.Project;
import com.rma.ui.AbstractContainerNode;
import com.rma.ui.IconNode;
import com.rma.ui.ProjectTree;

import rma.swing.RmaImage;
import usbr.wat.plugins.actionpanel.model.SimulationGroup;
import usbr.wat.plugins.actionpanel.ui.ActionsProjectTab;
import usbr.wat.plugins.actionpanel.ui.SimulationGroupNode;
import usbr.wat.plugins.actionpanel.ui.WtmpTreeNode;

/**
 * @author mark
 * class to display the Simulation Groups in the Study Tree
 */
@SuppressWarnings("serial")
public class SimGroupContainerNode extends AbstractContainerNode
	implements IconNode
{
	private static ImageIcon _folderIcon;
	static
	{
		_folderIcon = RmaImage.getImageIcon("Images/simGroupContainer.png");
	}
	private ProjectAdapter _projectListener;
	public SimGroupContainerNode()
	{
		super("Simulation Groups");
		addManagerListener();
		addNoGroupNode();
	}
	/**
	 * 
	 */
	private void addManagerListener()
	{
		Project.getCurrentProject().addManagerListener(new ProjectManagerListener()
		{

			@Override
			public void managerAdded(ManagerProxy proxy)
			{
				addSimulationGroup(proxy);
				
			}

			@Override
			public void managerDeleted(ManagerProxy proxy)
			{
				MutableTreeNode node = findNodeForManager(proxy);
				if ( node != null )
				{
					int idx = getIndex(node);
					remove(idx);
					Browser.getBrowserFrame().getProjectTree().nodesWereRemoved(SimGroupContainerNode.this, new TreeNode[] {node});
				}
			}

			@Override
			public Class getManagerClass()
			{
				return SimulationGroup.class;
			}
			
		});
	}
	/**
	 * @param proxy
	 */
	public boolean addSimulationGroup(ManagerProxy proxy)
	{
		MutableTreeNode node = findNodeForManager(proxy);
		if ( node == null )
		{
			MutableTreeNode mgrNode = ProjectNodeFactory.getProjectNode(proxy, this);
			add(mgrNode);
			ProjectTree tree = getTree();
			EventQueue.invokeLater(()->tree.nodesWereInserted(this, new TreeNode[] {node}));
			return true;
		}
		return false;
	}
	/**
	 * @return
	 */
	private ProjectTree getTree()
	{
		if ( getParent() instanceof WtmpTreeNode )
		{
			return ActionsProjectTab.getTab().getProjectTree();
		}
		return Browser.getBrowserFrame().getProjectTree();
	}
	/**
	 * 
	 */
	private void addNoGroupNode()
	{
		addNoGroupNode(Project.getCurrentProject());
	}
	private void addNoGroupNode(Project prj)
	{
		SimulationGroup  simGroup = new SimulationGroup()
		{
			@Override
			public boolean readData()
			{
				return true;
			}
			@Override
			public boolean isReadOnly()
			{
				return false;
			}
			@Override
			public boolean isModified()
			{
				return false;
			}
		};
		simGroup.setName("Not in a Group");
		simGroup.setIsTransitory(true);
		simGroup.setIgnoreModifiedEvents(true);
		prj.addManager(simGroup);
		MutableTreeNode node = ProjectNodeFactory.getProjectNode(simGroup, this);
		
		if ( node instanceof SimulationGroupNode )
		{
			SimulationGroupNode sgNode = (SimulationGroupNode) node;
			sgNode.setAddSimsNotInGroup();
			sgNode.setShowCount(true);
			sgNode.setManagerProxy(Project.getCurrentProject().getManagerProxy(simGroup));
			add(node);
		}
	}
	@Override
	public String getManagerType()
	{
		return SimulationGroup.class.getName();
	}
	
	@Override
	public void addManager(ManagerProxy proxy)
	{
		super.addManager(proxy);
	}
	@Override
	public Icon getIcon()
	{
		return _folderIcon;
	}
	/**
	 * @param simGrp
	 */
	public boolean checkAndAddSimGroup(SimulationGroup simGrp)
	{
		if ( simGrp == null )
		{
			return false;
		}
		ManagerProxy proxy = Project.getCurrentProject().getManagerProxy(simGrp);
		if ( proxy != null )
		{
			return addSimulationGroup(proxy);
		}
		return false;
	}

}
