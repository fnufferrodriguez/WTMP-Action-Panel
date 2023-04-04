package usbr.wat.plugins.actionpanel.ui.forecast.temptarget;

final class InvalidDssFileTypeException extends Exception
{
    InvalidDssFileTypeException(String fileName)
    {
        super(fileName + " is not a DSS file");
    }
}
