package usbr.wat.plugins.actionpanel.ui.forecast;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

final class TrimDeserializer extends JsonDeserializer<String>
{
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        return p == null ? null : p.getText().trim();
    }
}
