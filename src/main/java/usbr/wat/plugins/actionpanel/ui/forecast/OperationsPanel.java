/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui.forecast;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;

import com.rma.model.Project;
import com.rma.swing.excel.ExcelTable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rma.swing.EnabledJPanel;
import rma.swing.RmaInsets;
import rma.swing.RmaJTable;
import usbr.wat.plugins.actionpanel.ActionPanelPlugin;
import usbr.wat.plugins.actionpanel.model.forecast.ForecastSimGroup;
import usbr.wat.plugins.actionpanel.model.forecast.OperationsData;

/**
 * @author mark
 *
 */
public class OperationsPanel extends AbstractForecastPanel
{
	private static Logger LOGGER = Logger.getLogger(OperationsPanel.class.getName());
	private RmaJTable _opInfoTable;
	private JButton _importButton;
	private ExcelTable _reservoirTable;
	private RmaJTable _excelTable;
	private ForecastSimGroup _fsg;

	/**
	 * @param forecastPanel
	 */
	public OperationsPanel(ForecastPanel forecastPanel)
	{
		super(forecastPanel);
	}

	@Override
	protected void buildLowerPanel(EnabledJPanel lowerPanel)
	{
		String[] headers = new String[] {"Operations", "File Path", "Description", "Forecast Date"};

		_opInfoTable = new RmaJTable(this, headers)
		{
			@Override
			public Dimension getPreferredScrollableViewportSize()
			{
				Dimension d = super.getPreferredScrollableViewportSize();
				d.height = getRowHeight() *1;
				return d;
			}
			public boolean isCellEditable(int row, int col)
			{
				return false;
			}

		};
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1;
		gbc.weightx   = 1.0;
		gbc.weighty   = 0.1;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.BOTH;
		gbc.insets    = RmaInsets.INSETS5505;
		lowerPanel.add(_opInfoTable.getScrollPane(), gbc);

		_importButton = new JButton("Import...");
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 0.0;
		gbc.weighty   = 0.0;
		gbc.anchor    = GridBagConstraints.WEST;
		gbc.fill      = GridBagConstraints.NONE;
		gbc.insets    = RmaInsets.INSETS5505;
		lowerPanel.add(_importButton, gbc);


		_excelTable = new RmaJTable(this, new String[]{""});
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 1.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.BOTH;
		gbc.insets    = RmaInsets.INSETS5505;
		lowerPanel.add(_excelTable.getScrollPane(), gbc);

	}
	protected void addListeners()
	{
		super.addListeners();
		_importButton.addActionListener(e->importOperationsAction());
	}

	private void importOperationsAction()
	{
		ImportOperationsWindow dlg = new ImportOperationsWindow(ActionPanelPlugin.getInstance().getActionsWindow());
		dlg.fillForm(_fsg);
		dlg.setVisible(true);
		if ( dlg.isCanceled())
		{
			return;
		}
		OperationsData opsData = dlg.getOperationsData();
		ForecastTable opsTable = getTableForPanel();
		Vector<OperationsData> row = new Vector<>();
		row.add(opsData);
		opsTable.appendRow(row);
		_fsg.getOperationsData().add(opsData);
		_fsg.setModified(true);
		tableRowSelected(opsTable.getRowCount() -1);
	}

	@Override
	public ForecastTable getTableForPanel()
	{
		return _opsTable;
	}

	@Override
	protected void savePanel()
	{
		if ( _fsg != null )
		{
			// TODO Auto-generated method stub
			System.out.println("savePanel TODO implement me");
		}

	}

	@Override
	public void setSimulationGroup(ForecastSimGroup fsg)
	{
		clearPanel();
		setEnabled(fsg != null);
		_fsg = fsg;
		ForecastTable table = getTableForPanel();
		//_excelTable.setEnabled(false);
		if ( _fsg != null )
		{
			List<OperationsData> data = _fsg.getOperationsData();
			table.deleteCells();
			Vector<OperationsData> row;
			for (int i = 0; i < data.size(); i++ )
			{
				row = new Vector<>();
				row.add(data.get(i));
				table.appendRow(row);
			}
			if(!data.isEmpty())
			{
				displayOpsData(data.get(data.size()-1));
			}
		}
	}

	private void clearPanel()
	{
		_lowerPanel.remove(_excelTable.getScrollPane());
		_excelTable = new RmaJTable(this, new String[]{""});
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx     = GridBagConstraints.RELATIVE;
		gbc.gridy     = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx   = 1.0;
		gbc.weighty   = 1.0;
		gbc.anchor    = GridBagConstraints.NORTHWEST;
		gbc.fill      = GridBagConstraints.BOTH;
		gbc.insets    = RmaInsets.INSETS5505;
		_lowerPanel.add(_excelTable.getScrollPane(), gbc);
		_lowerPanel.revalidate();
		_lowerPanel.repaint();
	}

	@Override
	protected void tableRowSelected(int selRow)
	{
		if (_fsg != null )
		{
			ForecastTable table = getTableForPanel();
			_opInfoTable.deleteCells();
			if (selRow > -1)
			{
				OperationsData opsData = (OperationsData) table.getValueAt(selRow, 0);
				Vector row = new Vector();
				row.add(opsData.getName());
				row.add(opsData.getOperationsFile());
				row.add(opsData.getDescription());

				_opInfoTable.appendRow(row);
				displayOpsData(opsData);
			}
		}
	}

	private void displayOpsData(OperationsData opsData)
	{
		String opsFilePath = Project.getCurrentProject().getAbsolutePath(opsData.getOperationsFile());
		Sheet sheet = null;
		if(opsFilePath.endsWith(".csv"))
		{

			sheet = readCsv(opsFilePath);
		}
		else
		{
			try (FileInputStream inputStream = new FileInputStream(opsFilePath))
			{
				Workbook workbook = WorkbookFactory.create(inputStream);
				sheet = workbook.getSheetAt(0);
			}
			catch (IOException | InvalidFormatException e)
			{
				LOGGER.log(Level.SEVERE, e, () -> "Failed to read file: " + opsFilePath);
			}
		}
		if(sheet != null)
		{
			_lowerPanel.remove(_excelTable.getScrollPane());
			_excelTable = new ExcelTable(this, sheet);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx     = GridBagConstraints.RELATIVE;
			gbc.gridy     = GridBagConstraints.RELATIVE;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.weightx   = 1.0;
			gbc.weighty   = 1.0;
			gbc.anchor    = GridBagConstraints.NORTHWEST;
			gbc.fill      = GridBagConstraints.BOTH;
			gbc.insets    = RmaInsets.INSETS5505;
			_lowerPanel.add(_excelTable.getScrollPane(), gbc);
			_lowerPanel.revalidate();
			_lowerPanel.repaint();
		}
	}

	private Sheet readCsv(String opsFilePath)
	{
		Sheet retVal = null;
		try(BufferedReader reader = new BufferedReader(new FileReader(opsFilePath)))
		{
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("My Sheet");

			String line;
			int rowNum = 0;
			while ((line = reader.readLine()) != null)
			{
				Row row = sheet.createRow(rowNum);
				rowNum++;
				String[] values = line.split(",", -1);
				for (int i = 0; i < values.length; i++)
				{
					Cell cell = row.createCell(i);
					String val = values[i];
					if(i == 0 && val.contains("View Results:"))
					{
						val = "View Results:";
					}
					cell.setCellValue(val);
				}
			}
			retVal = sheet;
		}
		catch (IOException e)
		{
			LOGGER.log(Level.SEVERE, e, () -> "Failed to read file: " + opsFilePath);
		}
		return retVal;
	}

	private static void convertCsvToExcel(String csvFilePath, String excelFilePath)
	{
		try (FileInputStream fis = new FileInputStream(csvFilePath))
		{
			Workbook workbook = new HSSFWorkbook();
			Sheet sheet = workbook.createSheet("Sheet1");
			String line;
			int rowNum = 0;
			int colNum = 0;
			try (BufferedReader br = new BufferedReader(new java.io.InputStreamReader(fis)))
			{
				while ((line = br.readLine()) != null)
				{
					String[] data = line.split(",");
					Row row = sheet.createRow(rowNum);
					rowNum++;
					colNum = 0;
					for (String value : data)
					{
						Cell cell = row.createCell(colNum);
						colNum++;
						cell.setCellValue(value);
					}
				}
			}
			try (FileOutputStream fos = new FileOutputStream(excelFilePath))
			{
				workbook.write(fos);
			}
		}
		catch (IOException e)
		{
			LOGGER.log(Level.SEVERE, e, () -> "Failed to convert csv file to xls: " + csvFilePath);
		}
	}

}
