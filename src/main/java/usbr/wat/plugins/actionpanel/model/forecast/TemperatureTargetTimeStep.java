/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.model.forecast;

public enum TemperatureTargetTimeStep
{
    REGULAR_HOURLY("1Hour"),
    REGULAR_WEEKLY("1Week");

    private final String _displayName;

    TemperatureTargetTimeStep(String displayName)
    {
        _displayName = displayName;
    }

    @Override
    public String toString()
    {
        return _displayName;
    }
}
