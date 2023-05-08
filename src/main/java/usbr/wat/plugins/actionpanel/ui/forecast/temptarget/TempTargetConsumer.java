/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui.forecast.temptarget;

import usbr.wat.plugins.actionpanel.model.forecast.TemperatureTargetSet;

import javax.swing.JOptionPane;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

final class TempTargetConsumer implements Consumer<List<TemperatureTargetSet>>
{
    private static final Logger LOGGER = Logger.getLogger(TempTargetConsumer.class.getName());
    private final TempTargetPanel _panel;
    private boolean _success = true;

    TempTargetConsumer(TempTargetPanel panel)
    {
        _panel = panel;
    }

    @Override
    public void accept(List<TemperatureTargetSet> t)
    {
        try
        {
            _panel.tempTargetSetsSelected(t);
            _success = true;
        }
        catch (TempTargetSaveFailedException e)
        {
            _success = false;
            JOptionPane.showMessageDialog(_panel, e.getMessage(),
                    "DSS Write Failed", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.CONFIG, e, () -> "Temp Target save failed: " + e.getMessage());
        }
    }

    boolean wasSuccessful()
    {
        return _success;
    }
}
