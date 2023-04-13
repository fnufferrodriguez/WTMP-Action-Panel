/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */

package usbr.wat.plugins.actionpanel.model.forecast;

public enum MetDataType
{

	Historic("Historic"),
	L3MTO("L3MTO"),
	NCAR("NCAR");

	private String _typeName;

	private MetDataType(String typeName)
	{
		_typeName = typeName;
	}

	public String toString()
	{
		return _typeName;
	}


}
