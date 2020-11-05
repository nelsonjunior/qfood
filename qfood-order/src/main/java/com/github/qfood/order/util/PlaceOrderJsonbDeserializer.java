package com.github.qfood.order.util;

import com.github.qfood.order.domain.dto.PlaceOrderDTO;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import org.apache.kafka.common.header.Headers;

public class PlaceOrderJsonbDeserializer extends JsonbDeserializer<PlaceOrderDTO> {
    public PlaceOrderJsonbDeserializer() {
        super(PlaceOrderDTO.class);
    }

    @Override
    public PlaceOrderDTO deserialize(String topic, Headers headers, byte[] data) {
        System.out.println("---------- DESERIALIZE -----------");
        System.out.println(topic);
        System.out.println(String.valueOf(data));
        return super.deserialize(topic, headers, data);
    }
}
