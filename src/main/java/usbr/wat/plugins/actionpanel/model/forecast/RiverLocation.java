/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.model.forecast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class RiverLocation
{
    @JsonProperty("Id")
    private String _id;

    @JsonProperty("Name")
    private String _name;

    public RiverLocation()
    {
        // Default constructor needed for Jackson deserialization
    }

    public RiverLocation(String name, String id)
    {
        this._id = id;
        this._name = name;
    }
    @JsonIgnore
    public String getId()
    {
        return _id;
    }

    @JsonIgnore
    public void setId(String id)
    {
        this._id = id;
    }

    @JsonIgnore
    public String getName()
    {
        return _name;
    }

    @JsonIgnore
    public void setName(String name)
    {
        this._name = name;
    }

    @Override
    public String toString()
    {
        return _name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        RiverLocation that = (RiverLocation) o;
        return Objects.equals(_id, that._id) && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(_id, _name);
    }
}
