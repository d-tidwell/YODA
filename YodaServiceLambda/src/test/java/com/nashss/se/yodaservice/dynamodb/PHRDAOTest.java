package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.nashss.se.yodaservice.dynamodb.models.PHR;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PHRDAOTest {

    @Mock
    private DynamoDBMapper dynamoDbMapper;

    @InjectMocks
    private PHRDAO phrDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPHR() {
        String testPHRId = "testId";
        String testDate = "testDate";
        PHR testPHR = new PHR();

        when(dynamoDbMapper.load(PHR.class, testPHRId, testDate)).thenReturn(testPHR);

        PHR phr = phrDAO.getPHR(testPHRId, testDate);
        assertEquals(testPHR, phr, "Expected the returned PHR to be the test PHR");

        verify(dynamoDbMapper, times(1)).load(PHR.class, testPHRId, testDate);
    }

    @Test
    public void testGetPHRsByPHRId() {
        String testPHRId = "testId";
        PHR testPHR = new PHR();

        PaginatedQueryList<PHR> result = Mockito.mock(PaginatedQueryList.class);
        when(result.isEmpty()).thenReturn(false);
        when(result.get(0)).thenReturn(testPHR);
        when(dynamoDbMapper.query(eq(PHR.class), any(DynamoDBQueryExpression.class))).thenReturn(result);

        PHR phr = phrDAO.getPHRsByPHRId(testPHRId);
        assertEquals(testPHR, phr, "Expected the returned PHR to be the test PHR");

        verify(dynamoDbMapper, times(1)).query(eq(PHR.class), any(DynamoDBQueryExpression.class));
    }

    @Test
    public void testSavePHR() {
        PHR testPHR = new PHR();

        doNothing().when(dynamoDbMapper).save(testPHR);

        assertTrue(phrDAO.savePHR(testPHR), "Expected PHR saving to be successful");

        verify(dynamoDbMapper, times(1)).save(testPHR);
    }

    @Test
    public void testSavePHRFailure() {
        PHR testPHR = new PHR();

        doThrow(new AmazonDynamoDBException("Error")).when(dynamoDbMapper).save(testPHR);

        assertFalse(phrDAO.savePHR(testPHR), "Expected PHR saving to be unsuccessful");

        verify(dynamoDbMapper, times(1)).save(testPHR);
    }

    @Test
    public void testGetPhrsForPatient() {
        String testPatientId = "testId";
        PaginatedQueryList<PHR> testPHRs = Mockito.mock(PaginatedQueryList.class);
        when(dynamoDbMapper.query(eq(PHR.class), any(DynamoDBQueryExpression.class))).thenReturn((PaginatedQueryList) testPHRs);

        List<PHR> phrs = phrDAO.getPhrsForPatient(testPatientId);
        assertEquals(testPHRs, phrs, "Expected the returned PHRs to be the test PHRs");

        verify(dynamoDbMapper, times(1)).query(eq(PHR.class), any(DynamoDBQueryExpression.class));
    }

}
