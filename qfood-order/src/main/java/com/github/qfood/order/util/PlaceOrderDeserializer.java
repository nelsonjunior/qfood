package com.github.qfood.order.util;

import com.github.qfood.order.domain.dto.PlaceOrderDTO;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PlaceOrderDeserializer extends ObjectMapperDeserializer<PlaceOrderDTO> {
    public PlaceOrderDeserializer() {
        super(PlaceOrderDTO.class);
    }
}
