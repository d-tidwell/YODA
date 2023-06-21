package com.nashss.se.yodaservice.activity;

import com.nashss.se.yodaservice.activity.UpdatePHRActivity;
import com.nashss.se.yodaservice.activity.requests.UpdatePHRRequest;
import com.nashss.se.yodaservice.activity.results.UpdatePHRResult;
import com.nashss.se.yodaservice.dynamodb.DictationDAO;
import com.nashss.se.yodaservice.dynamodb.PHRDAO;
import com.nashss.se.yodaservice.dynamodb.PatientDAO;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UpdatePHRActivityTest {

    @Mock
    private PHRDAO phrdao;

    @InjectMocks
    private UpdatePHRActivity updatePHRActivity;

    private UpdatePHRRequest request;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        request = mock(UpdatePHRRequest.class);
    }

    @Test
    public void testHandleRequest() {
        // Arrange
        String status = "new status";
        PHR phr = new PHR();
        phr.setStatus("oldStatus");
        when(phrdao.getPHRsByPHRId(request.getPhrId())).thenReturn(phr);
        when(phrdao.savePHR(phr)).thenReturn(true);

        when(request.getStatus()).thenReturn(status);

        // Act
        UpdatePHRResult result = updatePHRActivity.handleRequest(request);

        // Assert
        verify(phrdao, times(1)).getPHRsByPHRId(request.getPhrId());
        verify(phrdao, times(1)).savePHR(phr);
        assertEquals(status, phr.getStatus());
        assertTrue(result.withSuccess());
    }
}


