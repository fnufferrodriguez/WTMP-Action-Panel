/*
 * Copyright 2022 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui.tree;

import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

/**
 * @author mark
 *
 */
public interface ActionsTreeTableNode
{
	String getToolTipText();
	TreePath getPath();
	/**
	 * @param popup
	 */
	void addPopupMenuItems(JPopupMenu popup);
}
