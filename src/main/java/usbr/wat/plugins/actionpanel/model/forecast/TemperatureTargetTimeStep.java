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
