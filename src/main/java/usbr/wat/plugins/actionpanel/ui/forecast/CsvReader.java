/*
 * Copyright 2023 United States Bureau of Reclamation (USBR).
 * United States Department of the Interior
 * All Rights Reserved. USBR PROPRIETARY/CONFIDENTIAL.
 * Source may not be released without written approval
 * from USBR
 */
package usbr.wat.plugins.actionpanel.ui.forecast;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CsvReader
{

    private CsvReader()
    {
        throw new AssertionError("Utility class. Don't instantiate");
    }
    public static <T> List<T> readCsv(Path csvFilePath, Class<?> valueType) throws IOException
    {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schema().withHeader();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new TrimDeserializer());
        csvMapper.registerModule(module);

        List<T> retVal = new ArrayList<>();

        try (MappingIterator<T> iterator = csvMapper.readerFor(valueType)
                .with(csvSchema)
                .readValues(Files.newBufferedReader(csvFilePath)))
        {
            while (iterator.hasNext())
            {
                T object = iterator.next();
                retVal.add(object);
            }
        }

        return retVal;
    }
}
