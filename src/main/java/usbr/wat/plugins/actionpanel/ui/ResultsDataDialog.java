/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.rma.io.FileManagerImpl;

import hec2.wat.model.WatSimulation;

import rma.swing.ButtonCmdPanel;
import rma.swing.ButtonCmdPanelListener;
import rma.swing.RmaInsets;
import rma.swing.RmaJDialog;
import rma.swing.RmaJTextArea;
import rma.swing.RmaJTextField;
import usbr.wat.plugins.actionpanel.actions.SaveSimulationResultsAction;
import usbr.wat.plugins.actionpanel.model.ResultsData;

/**
 * @author mark
 *
 */
public class ResultsDataDialog extends RmaJDialog
{

	private WatSimulation _sim;
	private RmaJTextField _resultsNameFld;
	private RmaJTextArea _resultsDescFld;
	private ButtonCmdPanel _cmdPanel;
	protected boolean _canceled;

	/**
	 * @param parent
	 * @param sim
	 */
	public ResultsDataDialog(Dialog parent, WatSimulation sim)
	{
		super(parent, "Enter Results Information", true);
		_sim = sim;
		buildControls();
		addListeners();
		pack();
		setSize(500,300);
		setLocationRelativeTo(getParent());
	}

	/**
	 * 
	 */
	private void buildControls()
	{
		getContentPane().setLayout(new GridBagLayout());
		
		JLabel label = new JLabel("Create New Results for "+_sim.getName());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 0.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(label, gbc);
		
		label = new JLabel("Results Name:");
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1; 
		gbc.weightx   = 0.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(label, gbc);
		
		_resultsNameFld = new RmaJTextField();
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.HORIZONTAL;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(_resultsNameFld, gbc);
		
		label = new JLabel("Description:");
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1; 
		gbc.weightx   = 0.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(label, gbc);
		
		_resultsDescFld = new RmaJTextArea();
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 1.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.BOTH;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(new JScrollPane(_resultsDescFld), gbc);	
		
		
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
		_cmdPanel.addCmdPanelListener(new ButtonCmdPanelListener()
		{
			public void buttonCmdActionPerformed(ActionEvent e)
			{
				switch (e.getID())
				{
					case ButtonCmdPanel.OK_BUTTON :
						if ( isValidForm())
						{
							_canceled = false;
							setVisible(false);
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
	protected boolean isValidForm()
	{
		String name = _resultsNameFld.getText().trim();
		if ( name.isEmpty())
		{
			JOptionPane.showMessageDialog(this,  "Please Enter a results name", "No Name", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		String resultsFolder = SaveSimulationResultsAction.getResultsFolder(_sim, name);
		if ( FileManagerImpl.getFileManager().fileExists(resultsFolder))
		{
			JOptionPane.showMessageDialog(this,  "Results for "+name+" already exist. Please enter a unique name", "Duplicate Name", JOptionPane.INFORMATION_MESSAGE);
			return false;
			
		}
		return true;
	}

	public boolean isCanceled()
	{
		return _canceled;
	}

	/**
	 * @return
	 */
	public ResultsData getResultsData()
	{
		String name = _resultsNameFld.getText().trim();
		String resultsFolder = SaveSimulationResultsAction.getResultsFolder(_sim, name);
		
		ResultsData data = new ResultsData(_sim, resultsFolder);
		data.setName(name);
		data.setDescription(_resultsDescFld.getText().trim());
		data.setSavedBy(System.getProperty("user.name"));
		data.setSavedAt(new Date());
		data.setLastComputedTime(_sim.getLastComputedDate());
		
		return data;
	}
	
	

}
