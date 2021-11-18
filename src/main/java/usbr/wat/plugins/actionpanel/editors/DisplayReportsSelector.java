/*
 * Copyright 2021  Hydrologic Engineering Center (HEC).
 * United States Army Corps of Engineers
 * All Rights Reserved.  HEC PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from HEC
 */
package usbr.wat.plugins.actionpanel.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingWorker;

import hec.util.AnimatedWaitGlassPane;

import hec2.wat.model.WatSimulation;

import rma.swing.ButtonCmdPanel;
import rma.swing.ButtonCmdPanelListener;
import rma.swing.RmaInsets;
import rma.swing.RmaJDialog;
import rma.swing.RmaJTable;
import usbr.wat.plugins.actionpanel.ActionsWindow;
import usbr.wat.plugins.actionpanel.model.ReportPlugin;
import usbr.wat.plugins.actionpanel.model.ReportsManager;
import usbr.wat.plugins.actionpanel.model.SimulationGroup;

/**
 * @author Mark Ackerman
 *
 */
@SuppressWarnings("serial")
public class DisplayReportsSelector extends RmaJDialog
{
	

	private RmaJTable _reportTable;
	private ButtonCmdPanel _cmdPanel;
	private AnimatedWaitGlassPane _agp;
	private Component _glassPane;
	private ActionsWindow _parent;

	public DisplayReportsSelector(ActionsWindow parent)
	{
		super(parent, true);
		_parent =parent;
		buildControls();
		addListeners();
		fillForm();
		pack();
		setSize(500,500);
		setLocationRelativeTo(getParent());
		
	}

	

	/**
	 * 
	 */
	protected void buildControls()
	{
		getContentPane().setLayout(new GridBagLayout());
		setTitle("Select Reports");
		
		((JComponent)getContentPane()).setBorder(BorderFactory.createTitledBorder("Available Reports"));
	
		String[] headers = new String[] {"Select", "Report", "Description"};
		_reportTable = new RmaJTable(this, headers);
		_reportTable.setCheckBoxCellEditor(0);
		_reportTable.setRowHeight(_reportTable.getRowHeight()+5);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 1.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.BOTH;
		gbc.insets    = RmaInsets.INSETS5505;
		getContentPane().add(_reportTable.getScrollPane(), gbc);
		
		_cmdPanel = new ButtonCmdPanel(ButtonCmdPanel.OK_CANCEL_BUTTONS);
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.SOUTHWEST;
		gbc.fill      = GridBagConstraints.HORIZONTAL;
		gbc.insets    = RmaInsets.INSETS5505;
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
						createReports();
						setVisible(false);
						break;
					case ButtonCmdPanel.CANCEL_BUTTON :
						setVisible(false);
						break;
				}
			}
		});
	}
	
	/**
	 * 
	 */
	private void fillForm()
	{
		List<ReportPlugin> plugins = ReportsManager.getPlugins();
		List<WatSimulation> sims = _parent.getSelectedSimulations();
		SimulationGroup simGroup = _parent.getSimulationGroup();
		boolean canBeComparisionReport = sims.size()>1;
		
		List<ReportPlugin>reportsToUse = new ArrayList<>();
		ReportPlugin reportPlugin;
		for (int i = 0; i < plugins.size(); i++ )
		{
			reportPlugin = plugins.get(i);
			if ( reportPlugin.isComparisonReport() )
			{
				if ( canBeComparisionReport )
				{
					reportsToUse.add(reportPlugin);
				}
			}
			else
			{
				reportsToUse.add(reportPlugin);
			}
		}
		fillTable(reportsToUse);
	}
	/**
	 * @param reportsToUse
	 */
	private void fillTable(List<ReportPlugin> reportsToUse)
	{
		_reportTable.deleteCells();
		Vector row;
		ReportPlugin plugin;
		for(int i = 0;i < reportsToUse.size();i++ )
		{
			row = new Vector();
			plugin = reportsToUse.get(i);
			row.add(shouldBeSelected(plugin));
			row.add(plugin);
			row.add(plugin.getDescription());
			_reportTable.appendRow(row);
		}
	}



	/**
	 * @param plugin
	 * @return
	 */
	private Object shouldBeSelected(ReportPlugin plugin)
	{
		// TODO Auto-generated method stub
		System.out.println("shouldBeSelected TODO implement me");
		return null;
	}



	/**
	 * @return
	 */
	protected List<ReportPlugin> getSelectedReports()
	{
		List<ReportPlugin>reportPlugins = new ArrayList<>();
		int rowCnt = _reportTable.getRowCount();
		return reportPlugins;
	}
	/**
	 * 
	 */
	protected void createReports()
	{
		_agp = new AnimatedWaitGlassPane();
		_agp.setTransparency(0.8f);
		setGlassPane("Creating Reports...");
		
		List<ReportPlugin> plugins = ReportsManager.getPlugins();
		List<ReportPlugin> pluginReports = getSelectedReports();
		int maxThreads = Math.min(pluginReports.size(), Runtime.getRuntime().availableProcessors());
		ExecutorService threadPool = Executors.newFixedThreadPool(maxThreads);
		
		try
		{

			SwingWorker<Void, Future<ReportCreator>> worker = new SwingWorker<Void, Future<ReportCreator>>()
			{
				@Override
				public Void doInBackground()
				{
					
					for (int i = 0; i< pluginReports.size();i++ )
					{
						Future<ReportCreator> future = createReport(pluginReports.get(i));
						this.publish(future);
					}

					return null;
				}
				private Future<ReportCreator> createReport( ReportPlugin reportPlugin)
				{
					return (Future<ReportCreator>) threadPool.submit(new ReportCreator(reportPlugin));
				}
				@Override
				public void process(List<Future<ReportCreator>> chunks)
				{
					if ( chunks == null || chunks.isEmpty())
					{
						return;
					}
					for (int i = 0;i < chunks.size(); i++ )
					{
						ReportCreator rc;
						try
						{
							rc = chunks.get(i).get();
							if ( !rc.wasReportSuccessFul())
							{
								_agp.setMessage("Failed to create report "+rc.getReportPlugin().getName());
							}
						}
						catch (InterruptedException | ExecutionException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
				@Override
				public void done() 
				{ 
					_agp.setMessage("Reports Complete");
					try
					{
						
					}
					finally
					{
						resetGlassPane();
					}
				}
			};
			worker.execute();

		}
		finally
		{


		}
	}
	

	public void setGlassPane(String msg)
	{
		_glassPane = getGlassPane();
		_agp = new AnimatedWaitGlassPane();
		_agp.setColor(Color.BLACK);
		setGlassPane(_agp);
		_agp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		_agp.setMessage(msg);
		_agp.setActive(true);
		_agp.setVisible(true);

	}

	public void resetGlassPane()
	{
		if(_agp != null && _glassPane != null)
		{
			_agp.setCursor(Cursor.getDefaultCursor());
			_agp.setActive(false);
			_agp.setVisible(false);
			setGlassPane(_glassPane);
			_agp = null;
			_glassPane = null;
		}
	}
	
	/**
	 * @author Mark Ackerman
	 *
	 */
	public class ReportCreator
	implements Runnable
	{

		private ReportPlugin _reportPlugin;
		private boolean _reportRv;

		/**
		 * @param reportPlugin
		 */
		public ReportCreator(ReportPlugin reportPlugin)
		{
			super();
			_reportPlugin = reportPlugin;
		}

		@Override
		public void run()
		{
			_agp.setMessage("Creating report for "+_reportPlugin.getName());
			_reportRv = _reportPlugin.createReport();
		}

		public boolean wasReportSuccessFul()
		{
			return _reportRv;
		}

		/**
		 * @return
		 */
		public ReportPlugin getReportPlugin()
		{
			return _reportPlugin;
		}

	}
}
