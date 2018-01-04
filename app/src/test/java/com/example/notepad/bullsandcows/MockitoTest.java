package com.example.notepad.bullsandcows;

import com.example.notepad.bullsandcows.utils.logic.RandomNumberGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MockitoTest {

    private RandomNumberGenerator randomGenerator;

    @Before
    public void setUpTests() {
        randomGenerator = new RandomNumberGenerator();
    }

    @Test
    public void checkReturnString() {
        assertNotNull(randomGenerator.generateRandomNumber(4));
    }

    @Test
    public void checkReturnCorrectNumberOfDigits() {
        int codeDigits = 3;
        String resultOfMethod = randomGenerator.generateRandomNumber(codeDigits);
        assertEquals(codeDigits, resultOfMethod.length());
    }

    @Test
    public void testWithCapture() {
        RandomNumberGenerator randomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
        ArgumentCaptor<Integer> arg = ArgumentCaptor.forClass(Integer.class);
        Integer cod = 4;
        randomNumberGenerator.generateRandomNumber(cod);
        Mockito.verify(randomNumberGenerator).generateRandomNumber(arg.capture());
        assertEquals(cod, arg.getValue());

    }

    @Mock
    private RandomNumberGenerator rNG = Mockito.mock(RandomNumberGenerator.class);

    @Captor
    private ArgumentCaptor<Integer> arg = ArgumentCaptor.forClass(Integer.class);

    @Test
    public void testWithCapture2() {
        Integer cod = 5;
        rNG.generateRandomNumber(cod);
        Mockito.verify(rNG).generateRandomNumber(arg.capture());
        assertEquals(cod, arg.getValue());
    }


    @Test
    public void testWithDoReturn() {
        RandomNumberGenerator randomWithSpy = Mockito.spy(RandomNumberGenerator.class);
        int cod = 3;
        randomWithSpy.generateRandomNumber(cod);
        Mockito.doReturn("333").when(randomWithSpy).generateRandomNumber(cod);
        assertEquals("333", randomWithSpy.generateRandomNumber(cod));
    }

    @Spy
    private RandomNumberGenerator mRandomNumberGenerator = Mockito.spy(RandomNumberGenerator.class);

    @Test
    public void testWithDoReturn2() {
        int cod = 4;
        mRandomNumberGenerator.generateRandomNumber(cod);
        Mockito.doReturn("45632").when(mRandomNumberGenerator).generateRandomNumber(cod);
        String codedNumber = mRandomNumberGenerator.generateRandomNumber(cod);
        assertEquals(5, codedNumber.length());
    }

    @Test
    public void testCheckNumberOfBulls() {
        assertEquals(2, new RandomNumberGenerator().checkNumberOfBulls("2345", "8395"));
        assertEquals(4, new RandomNumberGenerator().checkNumberOfBulls("2345", "2345"));
        assertEquals(0, new RandomNumberGenerator().checkNumberOfBulls("2345", "0123"));
        assertEquals(1, new RandomNumberGenerator().checkNumberOfBulls("2345", "2786"));
    }

    @Test
    public void testCheckNumberOfCows() {
        assertEquals(0, new RandomNumberGenerator().checkNumberOfCows("2345", "8395"));
        assertEquals(4, new RandomNumberGenerator().checkNumberOfCows("1234", "4321"));
        assertEquals(2, new RandomNumberGenerator().checkNumberOfCows("6759", "6579"));
        assertEquals(1, new RandomNumberGenerator().checkNumberOfCows("1845", "8345"));
        assertEquals(1, new RandomNumberGenerator().checkNumberOfCows("18", "83"));
        assertEquals(2, new RandomNumberGenerator().checkNumberOfCows("184", "148"));
        assertEquals(10, new RandomNumberGenerator().checkNumberOfCows("1234567890", "0987654321"));
    }

    @Test
    public void testCheckNumberForCorrectInput() {
        assertEquals(true, new RandomNumberGenerator().checkNumberForCorrectInput("12345", 5));
        assertEquals(false, new RandomNumberGenerator().checkNumberForCorrectInput("1234", 5));
        assertEquals(false, new RandomNumberGenerator().checkNumberForCorrectInput("123", 6));
        assertEquals(false, new RandomNumberGenerator().checkNumberForCorrectInput("", 4));
        assertEquals(false, new RandomNumberGenerator().checkNumberForCorrectInput("12342", 5));
        assertEquals(false, new RandomNumberGenerator().checkNumberForCorrectInput("5677", 4));
    }
}
