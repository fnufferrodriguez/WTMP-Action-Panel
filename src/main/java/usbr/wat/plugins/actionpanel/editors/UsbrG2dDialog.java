/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.editors;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.rma.io.DssFileManagerImpl;
import com.rma.io.FileManagerImpl;
import com.rma.io.RmaFile;
import com.rma.model.Project;

import hec.gfx2d.G2dDialog;
import hec.gfx2d.G2dObject;
import hec.gfx2d.PairedDataSet;
import hec.gfx2d.TimeSeriesDataSet;
import hec.io.DSSIdentifier;
import hec.io.DataContainer;
import hec.io.PairedDataContainer;
import hec.io.TimeSeriesContainer;

import hec2.wat.model.WatAnalysisPeriod;

import rma.swing.RmaInsets;
import rma.swing.RmaJComboBox;
import rma.swing.RmaNavigationPanel;
import rma.swing.list.RmaListModel;
import rma.util.RMAIO;
import usbr.wat.plugins.actionpanel.ActionsWindow;

/**
 * @author Mark Ackerman
 *
 */
@SuppressWarnings("serial")
public class UsbrG2dDialog extends G2dDialog
{
	public static final String DSS_PATHS_FILE = "shared/dssPlotRecords.csv";
	private RmaNavigationPanel _navPanel; 
	private RmaJComboBox<DssItem> _dssPathCombo;
	private ActionsWindow _parent;
	
	public UsbrG2dDialog(ActionsWindow parent)
	{
		super();
		_parent = parent;
		buildControls();
		addListeners();
		loadDssPaths();
	}
	

	/**
	 * 
	 */
	private void buildControls()
	{
		JPanel panel = new JPanel(new GridBagLayout());
		JLabel label = new JLabel("Location:");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1; 
		gbc.weightx   = 0.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5505;
		panel.add(label, gbc);
		
		
		_dssPathCombo = new RmaJComboBox<DssItem>()
		{
			@Override
			public String getToolTipText(MouseEvent e )
			{
				DssItem dssItem = (DssItem) _dssPathCombo.getSelectedItem();
				if ( dssItem != null )
				{
					return getToolTip(dssItem.dssIds);
				}
				return null;
			}
		};
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1; 
		gbc.weightx   = 1.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.HORIZONTAL;
		gbc.insets    = RmaInsets.INSETS5505;
		panel.add(_dssPathCombo, gbc);
		
		
		_navPanel = new RmaNavigationPanel();
		_navPanel.fillForm(_dssPathCombo);
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 0.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5505;
		panel.add(_navPanel, gbc);
		
		
		
		
		this.getContentPane().add(BorderLayout.NORTH, panel);
	}
	
	/**
	 * @param dssIds
	 * @return
	 */
	protected String getToolTip(List<DSSIdentifier> dssIds)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("<html>");
		DSSIdentifier dssId;
		for (int i = 0; i < dssIds.size(); i++ )
		{
			dssId = dssIds.get(i);
			builder.append(dssId.toString());
			builder.append("<br>");
		}
		return builder.toString();
	}


	protected void addListeners()
	{
		_dssPathCombo.addItemListener(e->dssComboChanged(e));
	}


	/**
	 * @param e
	 * @return
	 */
	private void dssComboChanged(ItemEvent e)
	{
		if ( ItemEvent.DESELECTED == e.getStateChange() )
		{
			return;
		}
		
		Vector plotData = new Vector();
		DssItem dssItem = (DssItem) _dssPathCombo.getSelectedItem();
		List<DSSIdentifier> dssIds = dssItem.dssIds;
		WatAnalysisPeriod ap = _parent.getAnalysisPeriod();
		for (int i = 0;i < dssIds.size(); i++ )
		{
			
			DSSIdentifier dssId = dssIds.get(i);
			if ( ap != null )
			{
				dssId.setStartTime(ap.getRunTimeWindow().getStartTime());
				dssId.setEndTime(ap.getRunTimeWindow().getEndTime());
			}
			DataContainer dc = DssFileManagerImpl.getDssFileManager().readDataContainer(dssId);

			G2dObject g2dObj = null;
			if ( dc instanceof TimeSeriesContainer )
			{
				g2dObj = new TimeSeriesDataSet((TimeSeriesContainer)dc);
			}
			else if ( dc instanceof PairedDataContainer )
			{
				g2dObj = new PairedDataSet((PairedDataContainer)dc);
			}
			else
			{
				System.out.println("dssComboChanged:unknown type "+dc);
				continue;
			}
			plotData.add(g2dObj);
		}
		getPlotpanel().buildComponents(plotData);
		getPlotpanel().useLineStyles(_useLineStyles);
		setTitle(dssItem.name);
	}


	/**
	 * 
	 */
	private void loadDssPaths()
	{
		String dir = Project.getCurrentProject().getProjectDirectory();
		String pathsFile = RMAIO.concatPath(dir,  DSS_PATHS_FILE);
		RmaFile dssPathFile = FileManagerImpl.getFileManager().getFile(pathsFile);
		BufferedReader reader = dssPathFile.getBufferedReader();
		String line;
		List<DssItem>dssItems = new ArrayList<>();
		if ( reader == null ) 
		{
			System.out.println("loadDssPaths:failed to find file "+pathsFile);
			return;
		}
		try
		{
			DssItem dssItem;
			while ( (line = reader.readLine())!= null )
			{
				if ( line.startsWith("#"))
				{
					continue;
				}
				String[] parts = line.split(",");
				if ( parts == null || parts.length != 3 )
				{
					continue;
				}
				String name= parts[0].trim();
				String file = parts[1].trim();
				file = Project.getCurrentProject().getAbsolutePath(file);
				String path = parts[2].trim();
				dssItem = findDssItem(name,dssItems);
				if ( dssItem == null )
				{
					dssItem = new DssItem(parts[0].trim(), file, path);
					dssItems.add(dssItem);
				}
				else 
				{
					dssItem.addLocation(file, path);
				}
			}
			RmaListModel newModel = new RmaListModel(true, dssItems);
			_dssPathCombo.setModel(newModel);
			if ( newModel.size() > 0 )
			{
				_dssPathCombo.setSelectedIndex(0);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if ( reader != null )
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
				}
			}
		}
	}
	
	
	
	/**
	 * @param name
	 * @param dssItems 
	 * @return
	 */
	private static DssItem findDssItem(String name, List<DssItem> dssItems)
	{
		int size=dssItems.size();
		
		for(int i = 0;i < size; i++ )
		{
			if ( dssItems.get(i).name.equalsIgnoreCase(name))
			{
				return dssItems.get(i);
			}
		}
		return null;
	}



	public class DssItem
	{
		String name;
		List<DSSIdentifier>dssIds = new ArrayList<>();
		
		DssItem(String n, String file, String path)
		{
			name = n;
			addLocation(file,path);
		}
		
		/**
		 * @param file
		 * @param path
		 */
		public void addLocation(String file, String path)
		{
			DSSIdentifier dssId = new DSSIdentifier(file,path);
			dssIds.add(dssId);
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
