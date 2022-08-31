/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui;

import java.awt.EventQueue;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.rma.event.ProjectAdapter;
import com.rma.event.ProjectEvent;
import com.rma.model.Project;
import com.rma.ui.ContentTree;
import com.rma.ui.ProjectTree;
import com.rma.ui.ProjectTreeModel;
import com.rma.ui.ProjectTreeNode;

/**
 * @author mark
 *
 */
@SuppressWarnings("serial")
public class WtmpTree extends ProjectTree
{
	/**
	 * @param contentTree
	 */
	public WtmpTree(ContentTree contentTree)
	{
		super(contentTree);
		setExpansionRowCount(3);
		setRootVisible(false);
		Project.addStaticProjectListener(new ProjectAdapter()
		{
			@Override
			public void projectOpened(ProjectEvent e)
			{
				EventQueue.invokeLater(()->expandAll());
			}
		});
	}
	
	@Override
	protected ProjectTreeNode createRootNode()
	{
		return new WtmpTreeNode(Project.getCurrentProject() ,null);
	}
	
	@Override
	protected ProjectTreeModel createModel(TreeNode root)
	{
		ProjectTreeModel model = new ProjectTreeModel((ProjectTreeNode)root)
		{
			@Override
			protected ProjectTreeNode createProjectTreeNode(Project proj, 
					MutableTreeNode node)
			{
				return new WtmpTreeNode(Project.getCurrentProject() ,null);
			}
		};
		_model = model;
		return model;
	
	}
	
}
