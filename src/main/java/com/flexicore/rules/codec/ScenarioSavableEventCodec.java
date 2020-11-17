package com.flexicore.rules.codec;

import com.flexicore.rules.events.ScenarioSavableEvent;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;

import java.util.HashMap;
import java.util.Map;

public class ScenarioSavableEventCodec implements Codec<ScenarioSavableEvent> {

    private final Codec<Document> documentCodec;

    public ScenarioSavableEventCodec() {
        this.documentCodec=new DocumentCodec();
    }

    @Override
    public ScenarioSavableEvent decode(BsonReader reader, DecoderContext decoderContext) {
        Document document = documentCodec.decode(reader, decoderContext);

        Map<String,Object> map=new HashMap<>(document);
        return new ScenarioSavableEvent(map);
    }

    @Override
    public void encode(BsonWriter writer, ScenarioSavableEvent value, EncoderContext encoderContext) {
        Document document = new Document(value.get());
        documentCodec.encode(writer, document, encoderContext);


    }

    @Override
    public Class<ScenarioSavableEvent> getEncoderClass() {
        return ScenarioSavableEvent.class;
    }
}
