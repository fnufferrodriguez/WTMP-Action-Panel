/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui.forecast.temptarget;

final class InvalidDssFileTypeException extends Exception
{
    InvalidDssFileTypeException(String fileName)
    {
        super(fileName + " is not a DSS file");
    }
}
