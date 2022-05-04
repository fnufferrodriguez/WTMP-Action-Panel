/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

import hec.gui.RenameDlg;

import rma.util.RMAIO;
import usbr.wat.plugins.actionpanel.ActionPanelPlugin;
import usbr.wat.plugins.actionpanel.model.ResultsData;

/**
 * @author mark
 *
 */
public class ResultsTreeTableNode extends AbstractMutableTreeTableNode
	 implements ActionsTreeTableNode
{

	private String[] _headers;
	private ResultsData _resultsData;
	private Object _selected;

	/**
	 * @param resultsData
	 */
	public ResultsTreeTableNode(ResultsData resultsData)
	{
		super(resultsData);
		_resultsData = resultsData;
		_headers = SimulationTreeTableModel._headers;
	}

	@Override
	public Object getValueAt(int column)
	{
		if ( _resultsData == null )
		{
			return "";
		}

		switch (column)
		{
			case SimulationTreeTableModel.SELECTED_COLUMN:
				return _selected;
			case SimulationTreeTableModel.SIMULATION_COLUMN:
				return _resultsData;
			case SimulationTreeTableModel.DISPLAY_IN_MAPS_COLUMN:
				return "Display In Map";
			case SimulationTreeTableModel.VIEW_REPORT_COLUMN:
				return "View";
		}
		return null;
	}
	@Override
	public void setValueAt(Object obj, int col)
	{
		if (col < 0 || col >= _headers.length)
		{
			return;
		}
		if (obj == null)
		{
			obj = "";
		}
		
		switch (col)
		{
			case SimulationTreeTableModel.SELECTED_COLUMN:
				 _selected = RMAIO.parseBoolean(obj.toString(), false);
				 break;
			case SimulationTreeTableModel.SIMULATION_COLUMN:
				break;
			case SimulationTreeTableModel.DISPLAY_IN_MAPS_COLUMN:
				break;
			case SimulationTreeTableModel.VIEW_REPORT_COLUMN:
				break;
		}
	}
	/**
	 * get the TreePath to the root node for the TreeNode
	 * @return the TreePath to root
	 */
	public TreePath getPath() 
	{
	    List<TreeNode> list = new ArrayList<>();
	    TreeNode node = this;
	    while (node != null) 
	    {
	        list.add(node);
	        node = node.getParent();
	    }
	    Collections.reverse(list);

	    return new TreePath(list.toArray());
	}

	@Override
	public int getColumnCount()
	{
		return _headers.length;
	}
	
	public ResultsData getResultsData()
	{
		return _resultsData;
	}

	/**
	 * @return
	 */
	public String getToolTipText()
	{
		StringBuilder tip = new StringBuilder();
		tip.append("<html>");
		String desc = _resultsData.getDescription();
		if ( desc != null && !desc.isEmpty()) 
		{
			tip.append("<b>Description:</b><br>");
			desc = RMAIO.toHtmlString(_resultsData.getDescription());
			desc = RMAIO.replace(desc, "<html>", "");
			desc = RMAIO.replace(desc, "</html>", "");
			tip.append(desc);
			tip.append("<br>");
		}
		tip.append("<b>Created by:</b> ");
		tip.append(_resultsData.getSavedBy());
		tip.append("<br><b>Created On:</b> ");
		tip.append(_resultsData.getSavedAt());
		tip.append("<br><b>Last Computed On:</b> ");
		tip.append(new Date(_resultsData.getLastComputedTime()));
		tip.append("</html>");
		return tip.toString().trim();
	}

	public void addPopupMenuItems(JPopupMenu popup)
	{
		JMenuItem renameMenu = new JMenuItem("Rename...");
		renameMenu.addActionListener(e->renameResults());
		popup.add(renameMenu);
	}

	/**
	 * @return
	 */
	private boolean renameResults()
	{
		SimulationTreeTableNode parent = (SimulationTreeTableNode) getParent();
		
		ResultsData results = getResultsData();
		
		List<String> resultsNames = parent.getResultsNames();
		
		RenameDlg dlg = new RenameDlg(ActionPanelPlugin.getInstance().getActionsWindow(),"Rename "+results.getName(), true);
		
		dlg.setName(results.getName());
		dlg.setDescription(results.getDescription());
		
		dlg.setFileNameVisible(false);
		dlg.setExistingNames(resultsNames);
		dlg.setVisible(true);
		if ( dlg.getCanceled())
		{
			return false;
		}
		if ( results.renameTo(dlg.getName()))
		{
			results.setDescription(dlg.getDescription());
			setUserObject(results);
			parent.getTreeTableModel().fireNodeChanged(this);
			return true;
		}
		JOptionPane.showMessageDialog(ActionPanelPlugin.getInstance().getActionsWindow(), "Faild to rename "
				+results.getName()+" to "+dlg.getName(),"Rename Failed", JOptionPane.INFORMATION_MESSAGE);
		return false;
	}

}
