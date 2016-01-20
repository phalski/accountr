package de.thi.phm6101.accountr.validation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ImageValidatorTest {

    private ImageValidator imageValidator;

    @Before
    public void setUp() throws Exception {
        imageValidator = new ImageValidator();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void thatValidationSucceedsForCorrectSizedJpeg() throws Exception {
        Part mockedPart = mock(Part.class);
        when(mockedPart.getContentType()).thenReturn("image/jpeg");
        when(mockedPart.getSize()).thenReturn((long) 1337);

        try {
            imageValidator.validate(null, null, mockedPart);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void thatValidationSucceedsForCorrectSizedPng() throws Exception {
        Part mockedPart = mock(Part.class);
        when(mockedPart.getContentType()).thenReturn("image/png");
        when(mockedPart.getSize()).thenReturn((long) 1337);

        try {
            imageValidator.validate(null, null, mockedPart);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void thatValidationSucceedsForCorrectSizedGif() throws Exception {
        Part mockedPart = mock(Part.class);
        when(mockedPart.getContentType()).thenReturn("image/gif");
        when(mockedPart.getSize()).thenReturn((long) 1337);

        try {
            imageValidator.validate(null, null, mockedPart);
        } catch (ValidatorException e) {
            fail();
        }
    }

    @Test
    public void thatValidationFailsForWrongFormat() throws Exception {
        Part mockedPart = mock(Part.class);
        when(mockedPart.getContentType()).thenReturn("image/whatever");
        when(mockedPart.getSize()).thenReturn((long) 1337);

        try {
            imageValidator.validate(null, null, mockedPart);
            fail();
        } catch (ValidatorException e) {
            assertEquals("File is not an image", e.getMessage());
        }
    }

    @Test
    public void thatValidationFailsForWrongSize() throws Exception {
        Part mockedPart = mock(Part.class);
        when(mockedPart.getContentType()).thenReturn("image/jpeg");
        when(mockedPart.getSize()).thenReturn((long) 999999999);

        try {
            imageValidator.validate(null, null, mockedPart);
            fail();
        } catch (ValidatorException e) {
            assertEquals("Image is too large", e.getMessage());
        }
    }
}
