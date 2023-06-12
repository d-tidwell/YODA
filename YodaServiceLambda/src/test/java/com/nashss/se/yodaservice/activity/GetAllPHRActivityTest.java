//package com.nashss.se.yodaservice.activity;
//
//import com.nashss.se.yodaservice.activity.GetAllPHRActivity;
//import com.nashss.se.yodaservice.activity.requests.GetAllPHRRequest;
//import com.nashss.se.yodaservice.activity.results.GetAllPHRResult;
//import com.nashss.se.yodaservice.dynamodb.PHRDAO;
//import com.nashss.se.yodaservice.dynamodb.PatientDAO;
//import com.nashss.se.yodaservice.dynamodb.models.PHR;
//import com.nashss.se.yodaservice.dynamodb.models.Patient;
//import com.nashss.se.yodaservice.models.PHRModel;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class GetAllPHRActivityTest {
//
//    @Mock
//    private PatientDAO patientDAO;
//
//    @Mock
//    private PHRDAO phrdao;
//
//    @InjectMocks
//    private GetAllPHRActivity handler;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testHandleRequest() {
//        // setup
//        String patientId = "patientId";
//        GetAllPHRRequest request = GetAllPHRRequest.builder()
//                .withPatientId(patientId)
//                .build();
//
//        // mock some PHR data
//        List<PHR> mockPHRList = new ArrayList<>();
//        PHR phr1 = new PHR();
//        phr1.setPhrId("phrId1");
//        phr1.setPatientId(patientId);
//        PHR phr2 = new PHR();
//        phr2.setPhrId("phrId2");
//        phr2.setPatientId(patientId);
//        mockPHRList.add(phr1);
//        mockPHRList.add(phr2);
//
//        when(patientDAO.getPatient(patientId)).thenReturn(new Patient());
//        when(phrdao.getPhrsForPatient(patientId)).thenReturn(mockPHRList);
//
//        // execute
//        GetAllPHRResult result = handler.handleRequest(request);
//
//        // verify
//        verify(patientDAO, times(1)).getPatient(patientId);
//        verify(phrdao, times(1)).getPhrsForPatient(patientId);
//
//        List<PHRModel> resultList = result.getPhrId();
//        assertEquals(mockPHRList.size(), resultList.size());
//        for (int i = 0; i < mockPHRList.size(); i++) {
//            assertEquals(mockPHRList.get(i).getPhrId(), resultList.get(i).getPhrId());
//            assertEquals(mockPHRList.get(i).getPatientId(), resultList.get(i).getPatientId());
//        }
//    }
//}
//
