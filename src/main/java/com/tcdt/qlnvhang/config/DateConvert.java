package com.tcdt.qlnvhang.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;

@Configuration
public class DateConvert implements Jackson2ObjectMapperBuilderCustomizer{
	@Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
			//  Override the default Date Deserialization , The first parameter is the class that needs to be deserialized , The second is the specific serialization format 
      jacksonObjectMapperBuilder.deserializerByType(
                Date.class
                ,new DateDeserializers.DateDeserializer(
                        DateDeserializers.DateDeserializer.instance
                        , new SimpleDateFormat("yyyy-MM-dd HH:mm")
                        , null));
      jacksonObjectMapperBuilder.deserializerByType(
              Date.class
              ,new DateDeserializers.DateDeserializer(
                      DateDeserializers.DateDeserializer.instance
                      , new SimpleDateFormat("yyyy-MM-dd")
                      , null));
      jacksonObjectMapperBuilder.deserializerByType(
              Date.class
              ,new DateDeserializers.DateDeserializer(
                      DateDeserializers.DateDeserializer.instance
                      , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                      , null));
    }
}
