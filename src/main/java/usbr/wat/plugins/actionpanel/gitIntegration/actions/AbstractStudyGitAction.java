/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.gitIntegration.actions;

import java.awt.Window;

import javax.swing.JOptionPane;

import com.rma.client.Browser;
import com.rma.model.Project;

/**
 * class for git actions that need to open and close the study
 * @author mark
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractStudyGitAction extends AbstractGitAction
{

	private String _lastProjectPath;

	public AbstractStudyGitAction(String name,Window parent )
	{
		super(name, parent);
	}
	/**
	 * 
	 */
	protected void openStudy()
	{
		if ( _lastProjectPath != null )
		{
			Browser.getBrowserFrame().projectOpen(_lastProjectPath);
			_lastProjectPath = null;
		}
	}
	protected abstract String getType();
	/**
	 * @return
	 */
	protected boolean askToCloseStudy()
	{
		if ( !Project.getCurrentProject().isNoProject())
		{
			String msg = getType()+" requires that the Study be closed first.  Continue?";
			String title = "Close Study";
			int opt = JOptionPane.showConfirmDialog(getParent(), msg, title, JOptionPane.YES_NO_OPTION);
			if ( JOptionPane.YES_OPTION != opt )
			{
				return false;
			}
		}
		return true;
	}
	protected boolean closeStudy()
	{
		if ( !Project.getCurrentProject().isNoProject())
		{
			_lastProjectPath = Project.getCurrentProject().getProjectFile().getAbsolutePath();

			boolean rv = Browser.getBrowserFrame().closeProjectAction();
			if ( !rv )
			{
				JOptionPane.showMessageDialog(getParent(), "Failed to close Study.", "Close Failed", JOptionPane.INFORMATION_MESSAGE);
			}
			return rv;
		}
		return true;
	}

}
