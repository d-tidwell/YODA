//package com.nashss.se.yodaservice.activity;
//
//import com.nashss.se.yodaservice.activity.GetPHRActivity;
//import com.nashss.se.yodaservice.activity.requests.GetPHRRequest;
//import com.nashss.se.yodaservice.activity.results.GetPHRResult;
//import com.nashss.se.yodaservice.dynamodb.PHRDAO;
//import com.nashss.se.yodaservice.dynamodb.models.PHR;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class GetPHRActivityTest {
//
//    @Mock
//    private PHRDAO phrdao;
//
//    @InjectMocks
//    private GetPHRActivity handler;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testHandleRequest() {
//        // setup
//        String phrId = "phrId";
//        GetPHRRequest request = new GetPHRRequest(phrId);
//        PHR phr = new PHR();
//        phr.setPhrId(phrId);
//        phr.setPatientId("patientId");
//        phr.setProviderName("providerName");
//        phr.setDate("2023-05-26");
//        phr.setStatus("CREATED");
//
//        when(phrdao.getPHRsByPHRId(phrId)).thenReturn(phr);
//
//        // execute
//        GetPHRResult result = handler.handleRequest(request);
//
//        // verify
//        verify(phrdao, times(1)).getPHRsByPHRId(phrId);
//        assertEquals(phr.getPatientId(), result.getPatientId());
//        assertEquals(phr.getProviderName(), result.getProviderName());
//        assertEquals(phr.getDate(), result.getDate());
//        assertEquals(phr.getStatus(), result.getStatus());
//    }
//
//}
