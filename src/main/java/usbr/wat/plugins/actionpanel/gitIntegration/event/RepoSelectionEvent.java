/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.gitIntegration.event;

/**
 * @author mark
 *
 */
public class RepoSelectionEvent
{

	private String _repoName;
	private String _repoUrl;
	private String _repoPath;

	/**
	 * @param repoUrl
	 * @param repoPath
	 */
	public RepoSelectionEvent(String repoName, String repoUrl, String repoPath)
	{
		super();
		_repoName = repoName;
		_repoUrl = repoUrl;
		_repoPath = repoPath;
	}
	
	public String getRepoName()
	{
		return _repoName;
	}
	public String getRepoUrl()
	{
		return _repoUrl;
	}
	
	public String getRepoPath()
	{
		return _repoPath;
	}	

}
