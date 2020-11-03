package com.github.qfood.order.util;

import com.github.qfood.order.domain.dto.PlaceOrderDTO;
import io.quarkus.kafka.client.serialization.JsonbDeserializer;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PlaceOrderJsonbDeserializer extends JsonbDeserializer<PlaceOrderDTO> {
    public PlaceOrderJsonbDeserializer() {
        super(PlaceOrderDTO.class);
    }
}
