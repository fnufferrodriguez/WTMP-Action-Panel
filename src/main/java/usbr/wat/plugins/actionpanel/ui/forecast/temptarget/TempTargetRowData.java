package usbr.wat.plugins.actionpanel.ui.forecast.temptarget;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

final class TempTargetRowData
{
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private LocalDate _localDate;
    private final Map<Integer, Double> _tempTargetColumnToValueMapInRow = new HashMap<>();

    TempTargetRowData(LocalDate localDate)
    {
        _localDate = localDate;
    }

    LocalDate getDate()
    {
        return _localDate;
    }

    Double getValueForTempTargetColumn(Integer column)
    {
        return _tempTargetColumnToValueMapInRow.get(column);
    }

    void setDate(LocalDate date)
    {
        _localDate = date;
    }

    void setValueForTempTargetColumn(Integer temperatureTargetColumn, Double value)
    {
        _tempTargetColumnToValueMapInRow.put(temperatureTargetColumn, value);
    }
}
