/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.editors;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.rma.client.Browser;
import com.rma.io.FileManagerImpl;
import com.rma.io.RmaFile;
import com.rma.model.Manager;
import com.rma.model.Project;

import hec.gui.NameDescriptionPanel;

import hec2.wat.factories.NewAnalysisPeriodFactory;
import hec2.wat.model.WatAnalysisPeriod;
import hec2.wat.model.WatSimulation;

import rma.swing.ButtonCmdPanel;
import rma.swing.ButtonCmdPanelListener;
import rma.swing.RmaInsets;
import rma.swing.RmaJComboBox;
import rma.swing.RmaJDialog;
import rma.swing.RmaJTable;
import rma.swing.list.RmaListModel;
import rma.util.RMAIO;
import usbr.wat.plugins.actionpanel.ActionsWindow;
import usbr.wat.plugins.actionpanel.commands.NewSimulationGroupCmd;
import usbr.wat.plugins.actionpanel.model.SimulationGroup;

/**
 * @author Mark Ackerman
 *
 */
@SuppressWarnings("serial")
public class NewSimulationGroupDialog extends RmaJDialog
{

	private static final int SELECTED_COLUMN = 0;
	private static final int SIMULATION_COLUMN = 1;
	
	
	private NameDescriptionPanel _nameDescPanel;
	private RmaJComboBox<WatAnalysisPeriod> _apCombo;
	private JButton _newApButton;
	private RmaJTable _simTable;
	private ButtonCmdPanel _cmdPanel;
	protected boolean _canceled;
	private SimulationGroup _simGroup;

	/**
	 * @param parent
	 * @param b
	 */
	public NewSimulationGroupDialog(ActionsWindow parent, boolean modal)
	{
		super(parent, modal);
		buildControls();
		addListeners();
		fillForm();
		pack();
		setLocationRelativeTo(getParent());
	}

	

	/**
	 * 
	 */
	private void buildControls()
	{
		getContentPane().setLayout(new GridBagLayout());
		setTitle("New Simulation Group");
		
		_nameDescPanel = new NameDescriptionPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.HORIZONTAL;
		gbc.insets    = RmaInsets.INSETS5005;
		getContentPane().add(_nameDescPanel, gbc);
		
		
		JLabel label = new JLabel("Analysis Period:");
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1; 
		gbc.weightx   = 0.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(label, gbc);
		
		_apCombo = new RmaJComboBox<>();
		label.setLabelFor(_apCombo);
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1; 
		gbc.weightx   = 1.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.fill      = GridBagConstraints.HORIZONTAL;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(_apCombo, gbc);
		
		_newApButton = new JButton("...");
		_newApButton.setToolTipText("Create New Analaysis Period");
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 0.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(_newApButton, gbc);
	
		String[] headers = new String[] {"Select", "Simulation", "Description"};
		_simTable = new RmaJTable(this, headers);
		_simTable.setRowHeight(_simTable.getRowHeight()+5);
		_simTable.setCheckBoxCellEditor(0);
		_simTable.setColumnEnabled(false,  1);
		_simTable.setColumnEnabled(false,  2);
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 1.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.BOTH;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(_simTable.getScrollPane(), gbc);
	
		
		_cmdPanel = new ButtonCmdPanel(ButtonCmdPanel.OK_CANCEL_BUTTONS);
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.SOUTHWEST;
		gbc.fill      = GridBagConstraints.HORIZONTAL;
		gbc.insets    = RmaInsets.INSETS5555;
		getContentPane().add(_cmdPanel, gbc);
	}

	/**
	 * 
	 */
	private void addListeners()
	{
		_newApButton.addActionListener(e->createAnalysisPeriodAction());
		
		_cmdPanel.addCmdPanelListener(new ButtonCmdPanelListener()
		{
			public void buttonCmdActionPerformed(ActionEvent e)
			{
				switch (e.getID())
				{
					case ButtonCmdPanel.OK_BUTTON :
						if ( isValidData())
						{
							if ( createSimulationGroup())
							{
								_canceled = false;
								setVisible(false);
							}
						}
						break;
					case ButtonCmdPanel.CANCEL_BUTTON :
						_canceled = true;
						setVisible(false);
						break;
				}
			}
		});
	}
	
	/**
	 * @return
	 */
	private void createAnalysisPeriodAction()
	{
		Manager mgr = Browser.getBrowserFrame().createNewManager(new NewAnalysisPeriodFactory());
		fillAnalysisPeriodCombo();
		/*  not sure why this code isn't working
		if ( mgr instanceof WatAnalysisPeriod )
		{
			RmaListModel<WatAnalysisPeriod> model = (RmaListModel<WatAnalysisPeriod>) _apCombo.getModel();
			model.addElement((WatAnalysisPeriod) mgr);
			_apCombo.setSelectedItem(mgr);
		}
		*/
	}



	/**
	 * 
	 */
	private void fillForm()
	{
		Project proj = Project.getCurrentProject();
		fillAnalysisPeriodCombo();
		
		_simTable.deleteCells();
		List<WatSimulation> sims = proj.getManagerListForType(WatSimulation.class);
		WatSimulation sim;
		Vector row;
		List<SimulationGroup>simGroups = proj.getManagerListForType(SimulationGroup.class);
		for (int i = 0;i < sims.size(); i++ )
		{
			sim = sims.get(i);
			if ( sim.getClass().equals(WatSimulation.class)&& !simPartOfGroup(sim, simGroups))   // no FRA simulations for now
			{
				row = new Vector();
				row.add(Boolean.FALSE);
				row.add(sim);
				row.add(sim.getDescription());
				_simTable.appendRow(row);
			}
		}
	}
	
	/**
	 * 
	 */
	private void fillAnalysisPeriodCombo()
	{
		Project proj = Project.getCurrentProject();
		List<WatAnalysisPeriod> aps = proj.getManagerListForType(WatAnalysisPeriod.class);
		RmaListModel newmodel = new RmaListModel(true, aps);
		_apCombo.setModel(newmodel);
		
	}



	/**
	 * check to see if the simulation is already part of a group 
	 * @param sim
	 * @return true if it is part of a simulation group
	 */
	private boolean simPartOfGroup(WatSimulation sim, List<SimulationGroup>simGroups)
	{
		int size = simGroups.size();
		SimulationGroup simGroup;
		for (int i = 0;i < size; i++ )
		{
			simGroup = simGroups.get(i);
			if (simGroup.containsSimulation(sim))
			{
				return true;
			}
		}
		return false;
	}



	/**
	 * @return
	 */
	protected boolean createSimulationGroup()
	{
		String name = _nameDescPanel.getName();
		String desc = _nameDescPanel.getDescription();
		RmaFile file = getSimulationGroupFolder();
		WatAnalysisPeriod ap = (WatAnalysisPeriod) _apCombo.getSelectedItem();
		List<WatSimulation> sims =  getSelectedSimulations();
		NewSimulationGroupCmd cmd = new NewSimulationGroupCmd(Project.getCurrentProject(),name, desc,file, ap, sims);
		cmd.doCommand();
		_simGroup = cmd.getSimulationGroup();
		return _simGroup != null;
	}

	/**
	 * @return
	 */
	private static RmaFile getSimulationGroupFolder()
	{
		String dir = Project.getCurrentProject().getProjectDirectory();
		dir = RMAIO.concatPath(dir, "wat");
		dir = RMAIO.concatPath(dir, "simGroups");
		return FileManagerImpl.getFileManager().getFile(dir);
	}

	public boolean isCanceled()
	{
		return _canceled;
	}
	/**
	 * @return
	 */
	protected boolean isValidData()
	{
		Project proj = Project.getCurrentProject();
		String name = _nameDescPanel.getName();
		if ( proj.getManagerProxy(name, SimulationGroup.class) != null )
		{
			JOptionPane.showMessageDialog(this, "A Simulation Group named "+name+" already exists. Please enter a unique name", 
					"Duplicate Name", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		WatAnalysisPeriod ap = (WatAnalysisPeriod) _apCombo.getSelectedItem();
		if ( ap == null )
		{
			JOptionPane.showMessageDialog(this, "No Analysis Period has been selected. Please select an Analysis Period",
					"No Analaysis Period", JOptionPane.INFORMATION_MESSAGE);
			return false;
			
		}
		List<WatSimulation> selectedSimulations =  getSelectedSimulations();
		if ( selectedSimulations.isEmpty() )
		{
			JOptionPane.showMessageDialog(this, "No Simulations have been selected. Please select at least one Simulation",
					"No Simulations", JOptionPane.INFORMATION_MESSAGE);
			return false;
			
		}
		
		return true;
	}

	/**
	 * @return
	 */
	private List<WatSimulation> getSelectedSimulations()
	{
		List<WatSimulation>selectedSims = new ArrayList<>();
		int rowCnt = _simTable.getRowCount();
		Object obj;
		WatSimulation sim;
		for (int r = 0;r < rowCnt; r++  )
		{
			obj = _simTable.getValueAt(r, SELECTED_COLUMN);
			if (obj == null )
			{
				continue;
			}
			
			if ( RMAIO.parseBoolean(obj.toString(), false))
			{
				sim = (WatSimulation) _simTable.getValueAt(r, SIMULATION_COLUMN);
				selectedSims.add(sim);
			}
		}
		return selectedSims;
	}

	/**
	 * @return
	 */
	public SimulationGroup getSimulationGroup()
	{
		return _simGroup;
	}

}
