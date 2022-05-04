/*
 * Copyright 2018  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.ui.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import rma.swing.RmaImage;
import rma.swing.RmaJXTreeTable;
/**
 *  tree cell renderer to render the tree icons for DataLocation
 * @author Mark Ackerman
 *
 */
class IconTableCellRenderer 
	implements  TreeCellRenderer
{
	private TreeCellRenderer _defTreeRenderer;
	private ImageIcon _watIcon;
	private ImageIcon _resultsIcon;
	private RmaJXTreeTable _treeTable;

	/**
	 *  Constructor for the IconTableCellRenderer object
	 */
	public IconTableCellRenderer(RmaJXTreeTable treeTable, TreeCellRenderer defRenderer) 
	{ 
		super();
		_defTreeRenderer = defRenderer;
		_treeTable = treeTable;
		_watIcon = RmaImage.getImageIcon("Images/comp16x16.gif");
		_resultsIcon = RmaImage.getImageIcon("Images/tabulate18.gif");
	}
	
	
	

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
	 */
	/**
	 * {@inheritDoc}
	 * 
	 * renders the tree cell and then sets the correct icon for it.
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean isLeaf, int row,
			boolean hasFocus)
	{
		Component comp = null ;
		try
		{
			comp = _defTreeRenderer.getTreeCellRendererComponent(tree, value, 
					selected, expanded, isLeaf, row, hasFocus);
			if ( comp instanceof JLabel )
			{
				JLabel label = (JLabel) comp;
				boolean isGroup = !isLeaf;
				if ( value instanceof SimulationTreeTableNode )
				{
					label.setIcon(_watIcon);
				}
				else if ( value instanceof ResultsTreeTableNode )
				{
					label.setIcon(_resultsIcon);
				}
				label.setOpaque(true);
			
				if ( !selected )
				{
					Color fg = _treeTable.getRowForeground(row);
					label.setForeground(fg);
				}
				
			}
				
			
			return comp;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return comp;
	}

}