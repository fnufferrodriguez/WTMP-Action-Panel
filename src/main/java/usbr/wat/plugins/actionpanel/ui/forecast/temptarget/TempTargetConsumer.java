/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui.forecast.temptarget;

import usbr.wat.plugins.actionpanel.model.forecast.TemperatureTargetSet;

import java.util.List;

final class TempTargetConsumer
{
    private final TempTargetPanel _panel;

    TempTargetConsumer(TempTargetPanel panel)
    {
        _panel = panel;
    }

    public void accept(List<TemperatureTargetSet> t) throws TempTargetSaveFailedException
    {
        _panel.tempTargetSetsSelected(t);
    }

}
