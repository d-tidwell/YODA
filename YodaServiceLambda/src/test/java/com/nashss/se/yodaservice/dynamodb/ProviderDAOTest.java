package com.nashss.se.yodaservice.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.nashss.se.yodaservice.dynamodb.models.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProviderDAOTest {

    @Mock
    private DynamoDBMapper dynamoDbMapper;

    @InjectMocks
    private ProviderDAO providerDAO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProvider() {
        String testProviderName = "testName";
        Provider testProvider = new Provider();

        when(dynamoDbMapper.load(Provider.class, testProviderName)).thenReturn(testProvider);

        Optional<Provider> provider = providerDAO.getProvider(testProviderName);
        assertEquals(testProvider, provider.get(), "Expected the returned provider to be the test provider");

        verify(dynamoDbMapper, times(1)).load(Provider.class, testProviderName);
    }

    @Test
    public void testGetProvider_notFound() {
        String testProviderName = "testName";

        when(dynamoDbMapper.load(Provider.class, testProviderName)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            providerDAO.getProvider(testProviderName);
        });

        verify(dynamoDbMapper, times(1)).load(Provider.class, testProviderName);
    }



    @Test
    public void testUpdatePending() {
        Provider testProvider = new Provider();
        boolean isUpdated = providerDAO.updateProvider(testProvider);
        assertTrue(isUpdated, "Expected the update to succeed");

        verify(dynamoDbMapper, times(1)).save(testProvider);
    }

    @Test
    public void testUpdatePending_exception() {
        Provider testProvider = new Provider();

        doThrow(new AmazonDynamoDBException("DynamoDB error")).when(dynamoDbMapper).save(testProvider);

        boolean isUpdated = providerDAO.updateProvider(testProvider);
        assertFalse(isUpdated, "Expected the update to fail when an exception occurs");

        verify(dynamoDbMapper, times(1)).save(testProvider);
    }
}
