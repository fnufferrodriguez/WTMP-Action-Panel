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
public interface RepoSelectionListener
{

	/**
	 * @param event
	 */
	void repoSelectionChanged(RepoSelectionEvent event);

}
