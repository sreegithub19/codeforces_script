package com.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MyServiceTest {

    @Test
    public void testGetDataById() {
        // Mock the repository
        MyRepository mockRepository = Mockito.mock(MyRepository.class);

        // Define behavior for the mock
        when(mockRepository.findById(1)).thenReturn("Mocked Data");

        // Create the service with the mocked repository
        MyService service = new MyService(mockRepository);

        // Call the method and assert the result
        String result = service.getDataById(1);
        assertEquals("Mocked Data", result);
    }
}
