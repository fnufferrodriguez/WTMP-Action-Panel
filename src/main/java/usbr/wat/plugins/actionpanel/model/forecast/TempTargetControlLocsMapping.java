/*
 * Copyright 2024 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.model.forecast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TempTargetControlLocsMapping
{
    @JsonProperty("Source DSS file")
    private String _sourceDssFile;

    @JsonProperty("Source DSS record")
    private String _sourceDssRecord;

    @JsonProperty("Number of Destinations")
    private int _numberOfDestinations;

    @JsonProperty("Destination DSS file")
    private String _destinationDssFile;

    @JsonProperty("Destination DSS record")
    private String _destinationDssRecord;

    @JsonIgnore
    public String getSourceDssFile()
    {
        return _sourceDssFile;
    }

    public void setSourceDssFile(String sourceDssFile)
    {
        this._sourceDssFile = sourceDssFile;
    }

    @JsonIgnore
    public String getSourceDssRecord()
    {
        return _sourceDssRecord;
    }

    public void setSourceDssRecord(String sourceDssRecord)
    {
        this._sourceDssRecord = sourceDssRecord;
    }

    @JsonIgnore
    public int getNumberOfDestinations()
    {
        return _numberOfDestinations;
    }

    public void setNumberOfDestinations(int numberOfDestinations)
    {
        this._numberOfDestinations = numberOfDestinations;
    }

    @JsonIgnore
    public String getDestinationDssFile()
    {
        return _destinationDssFile;
    }

    public void setDestinationDssFile(String destinationDssFile)
    {
        this._destinationDssFile = destinationDssFile;
    }

    @JsonIgnore
    public String getDestinationDssRecord()
    {
        return _destinationDssRecord;
    }

    public void setDestinationDssRecord(String destinationDssRecord)
    {
        this._destinationDssRecord = destinationDssRecord;
    }

    @Override
    public String toString()
    {
        return "Configuration{" +
                "sourceDssFile='" + getSourceDssFile() + '\'' +
                ", sourceDssRecord='" + getSourceDssRecord() + '\'' +
                ", numberOfDestinations=" + getNumberOfDestinations() +
                ", destinationDssFile='" + getDestinationDssFile() + '\'' +
                ", destinationDssRecord='" + getDestinationDssRecord() + '\'' +
                '}';
    }
}
