/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui.forecast.temptarget;

public final class TempTargetSaveFailedException extends Exception
{
    public TempTargetSaveFailedException(String error, String fileName, String statusCode)
    {
        super("Error writing " + error + "\n to " + fileName +"\n Error Status: " + statusCode);
    }

    public TempTargetSaveFailedException(String error)
    {
        super(error);
    }
}
