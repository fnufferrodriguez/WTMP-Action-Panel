package usbr.wat.plugins.actionpanel.ui.forecast.temptarget;

final class NonexistentFileException extends Exception
{
    NonexistentFileException(String fileName)
    {
        super(fileName + " does not exist");
    }
}
