package com.example.notepad.bullsandcows;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
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
    public void checkReturnString(){
        assertNotNull(randomGenerator.generateRandomNumber(4));
    }

    @Test
    public void checkReturnCorrectNumberOfDigits(){
        int codeDigits = 3;
        String resultOfMethod = randomGenerator.generateRandomNumber(codeDigits);
        assertEquals(codeDigits, resultOfMethod.length());
    }

    @Test
    public void testWithCapture(){
        RandomNumberGenerator randomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
        ArgumentCaptor<Integer> arg = ArgumentCaptor.forClass(Integer.class);
        Integer cod = 4;
        randomNumberGenerator.generateRandomNumber(cod);
        Mockito.verify(randomNumberGenerator).generateRandomNumber(arg.capture());
        assertEquals(cod, arg.getValue());

    }
    @Mock
    RandomNumberGenerator rNG = Mockito.mock(RandomNumberGenerator.class);

    @Captor
    ArgumentCaptor<Integer> arg = ArgumentCaptor.forClass(Integer.class);

    @Test
    public void testWithCapture2(){
        Integer cod = 5;
        rNG.generateRandomNumber(cod);
       Mockito.verify(rNG).generateRandomNumber(arg.capture());
        assertEquals(cod, arg.getValue());
    }


    @Test
    public void testWithDoReturn(){
        RandomNumberGenerator randomWithSpy = Mockito.spy(RandomNumberGenerator.class);
        int cod = 3;
        randomWithSpy.generateRandomNumber(cod);
        Mockito.doReturn("333").when(randomWithSpy).generateRandomNumber(cod);
        assertEquals("333", randomWithSpy.generateRandomNumber(cod));
    }

    @Spy
    RandomNumberGenerator mRandomNumberGenerator = Mockito.spy(RandomNumberGenerator.class);

    @Test
    public void testWithDoReturn2(){
        int cod = 4;
        mRandomNumberGenerator.generateRandomNumber(cod);
        Mockito.doReturn("45632").when(mRandomNumberGenerator).generateRandomNumber(cod);
        String codedNumber = mRandomNumberGenerator.generateRandomNumber(cod);
        assertEquals(5, codedNumber.length());
    }
}
