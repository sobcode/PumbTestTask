package com.projects.pumbtesttask.helper;

import com.projects.pumbtesttask.model.Animal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class XMLHelperTest {
    @InjectMocks
    private XMLHelper xmlHelper;

    @Test
    public void testHasXmlFormat() {
        MockMultipartFile xmlFile = new MockMultipartFile("file",
                "animals.xml", "text/xml", "XML content".getBytes());

        assertTrue(xmlHelper.hasXMLFormat(xmlFile));
    }

    @Test
    public void testValidXmlToAnimals() {
        String name = "Simon";
        int weight = 15;
        String validXml = String.format("<animals><animal><name>%s</name><type>cat</type><sex>male</sex>" +
                "<weight>15</weight><cost>%d</cost></animal></animals>", name, weight);
        InputStream inputStream = new ByteArrayInputStream(validXml.getBytes(StandardCharsets.UTF_8));

        List<Animal> animals = xmlHelper.xmlToAnimals(inputStream);
        Animal animal = animals.get(0);

        assertEquals(1, animals.size());
        assertEquals(name, animal.getName());
        assertEquals(weight, animal.getWeight());

    }

    @Test
    public void testInvalidXmlToAnimals() {
        String invalidXml = "<animal><name>Simon</name><type>cat</type><sex>male</sex>" +
                "<weight>15</weight><cost>-56</cost></animal>";
        InputStream inputStream = new ByteArrayInputStream(invalidXml.getBytes(StandardCharsets.UTF_8));

        assertThrows(RuntimeException.class, () -> xmlHelper.xmlToAnimals(inputStream));
    }

    @Test
    public void testNullInputToAnimals() {
        assertThrows(RuntimeException.class, () -> xmlHelper.xmlToAnimals(null));
    }
}
