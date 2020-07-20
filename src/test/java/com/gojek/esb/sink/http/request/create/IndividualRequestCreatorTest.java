package com.gojek.esb.sink.http.request.create;

import com.gojek.esb.config.enums.HttpRequestMethod;
import com.gojek.esb.consumer.EsbMessage;
import com.gojek.esb.exception.DeserializerException;
import com.gojek.esb.sink.http.request.body.JsonBody;
import com.gojek.esb.sink.http.request.entity.EntityBuilder;
import com.gojek.esb.sink.http.request.header.HeaderBuilder;
import com.gojek.esb.sink.http.request.uri.URIBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IndividualRequestCreatorTest {
    @Mock
    private URIBuilder uriBuilder;

    @Mock
    private HeaderBuilder headerBuilder;

    @Mock
    private EntityBuilder entityBuilder;

    @Mock
    private JsonBody jsonBody;

    @Before
    public void setup() {
        initMocks(this);
        EsbMessage esbMessage = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
    }

    @Test
    public void shouldProduceIndividualRequests() throws DeserializerException, URISyntaxException {
        EsbMessage esbMessage1 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        EsbMessage esbMessage2 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        ArrayList<EsbMessage> esbMessages = new ArrayList<>();
        esbMessages.add(esbMessage1);
        esbMessages.add(esbMessage2);

        ArrayList<String> serializedMessages = new ArrayList<>();
        serializedMessages.add("dummyMessage1");
        serializedMessages.add("dummyMessage2");
        when(jsonBody.serialize(esbMessages)).thenReturn(serializedMessages);

        IndividualRequestCreator individualRequestCreator = new IndividualRequestCreator(uriBuilder, headerBuilder, HttpRequestMethod.PUT, jsonBody);
        List<HttpEntityEnclosingRequestBase> requests = individualRequestCreator.create(esbMessages, entityBuilder);

        assertEquals(2, requests.size());
    }

    @Test
    public void shouldSetRequestPropertiesMultipleTimes() throws DeserializerException, URISyntaxException {
        EsbMessage esbMessage1 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        EsbMessage esbMessage2 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        ArrayList<EsbMessage> esbMessages = new ArrayList<>();
        esbMessages.add(esbMessage1);
        esbMessages.add(esbMessage2);

        ArrayList<String> serializedMessages = new ArrayList<>();
        serializedMessages.add("dummyMessage1");
        serializedMessages.add("dummyMessage2");
        when(jsonBody.serialize(esbMessages)).thenReturn(serializedMessages);

        IndividualRequestCreator individualRequestCreator = new IndividualRequestCreator(uriBuilder, headerBuilder, HttpRequestMethod.PUT, jsonBody);
        individualRequestCreator.create(esbMessages, entityBuilder);

        verify(uriBuilder, times(2)).build(any(EsbMessage.class));
        verify(headerBuilder, times(2)).build(any(EsbMessage.class));
        verify(entityBuilder, times(2)).buildHttpEntity(any(String.class));
    }

    @Test
    public void shouldProduceIndividualRequestsWhenPUTRequest() throws DeserializerException, URISyntaxException {
        EsbMessage esbMessage1 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        EsbMessage esbMessage2 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        ArrayList<EsbMessage> esbMessages = new ArrayList<>();
        esbMessages.add(esbMessage1);
        esbMessages.add(esbMessage2);

        ArrayList<String> serializedMessages = new ArrayList<>();
        serializedMessages.add("dummyMessage1");
        serializedMessages.add("dummyMessage2");
        when(jsonBody.serialize(esbMessages)).thenReturn(serializedMessages);

        IndividualRequestCreator individualRequestCreator = new IndividualRequestCreator(uriBuilder, headerBuilder, HttpRequestMethod.PUT, jsonBody);
        List<HttpEntityEnclosingRequestBase> requests = individualRequestCreator.create(esbMessages, entityBuilder);

        assertEquals(2, requests.size());
    }

    @Test
    public void shouldWrapEntityToArrayIfSet() throws DeserializerException, URISyntaxException, IOException {
        EsbMessage esbMessage1 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        EsbMessage esbMessage2 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        ArrayList<EsbMessage> esbMessages = new ArrayList<>();
        esbMessages.add(esbMessage1);
        esbMessages.add(esbMessage2);

        ArrayList<String> serializedMessages = new ArrayList<>();
        serializedMessages.add("dummyMessage1");
        serializedMessages.add("dummyMessage2");
        when(jsonBody.serialize(esbMessages)).thenReturn(serializedMessages);

        entityBuilder = new EntityBuilder().setWrapping(true);

        IndividualRequestCreator individualRequestCreator = new IndividualRequestCreator(uriBuilder, headerBuilder, HttpRequestMethod.PUT, jsonBody);
        List<HttpEntityEnclosingRequestBase> requests = individualRequestCreator.create(esbMessages, entityBuilder);

        byte[] bytes1 = IOUtils.toByteArray(requests.get(0).getEntity().getContent());
        byte[] bytes2 = IOUtils.toByteArray(requests.get(1).getEntity().getContent());
        Assert.assertEquals("[dummyMessage1]", new String(bytes1));
        Assert.assertEquals("[dummyMessage2]", new String(bytes2));
    }

    @Test
    public void shouldNotWrapEntityToArrayIfNot() throws DeserializerException, URISyntaxException, IOException {
        EsbMessage esbMessage1 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        EsbMessage esbMessage2 = new EsbMessage(new byte[]{10, 20}, new byte[]{1, 2}, "sample-topic", 0, 100);
        ArrayList<EsbMessage> esbMessages = new ArrayList<>();
        esbMessages.add(esbMessage1);
        esbMessages.add(esbMessage2);

        ArrayList<String> serializedMessages = new ArrayList<>();
        serializedMessages.add("dummyMessage1");
        serializedMessages.add("dummyMessage2");
        when(jsonBody.serialize(esbMessages)).thenReturn(serializedMessages);

        entityBuilder = new EntityBuilder().setWrapping(false);

        IndividualRequestCreator individualRequestCreator = new IndividualRequestCreator(uriBuilder, headerBuilder, HttpRequestMethod.PUT, jsonBody);
        List<HttpEntityEnclosingRequestBase> requests = individualRequestCreator.create(esbMessages, entityBuilder);

        byte[] bytes1 = IOUtils.toByteArray(requests.get(0).getEntity().getContent());
        byte[] bytes2 = IOUtils.toByteArray(requests.get(1).getEntity().getContent());
        Assert.assertEquals("dummyMessage1", new String(bytes1));
        Assert.assertEquals("dummyMessage2", new String(bytes2));
    }
}
