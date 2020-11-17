package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.BaseclassNoSQLRepository;
import com.flexicore.interfaces.AbstractNoSqlRepositoryPlugin;
import com.flexicore.model.nosql.BaseclassNoSQL;
import com.flexicore.rules.codec.ScenarioSavableEventCodec;
import com.flexicore.rules.events.ScenarioSavableEvent;
import com.flexicore.rules.request.ScenarioSavableEventFilter;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.flexicore.service.MongoConnectionService.MONGO_DB;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioSavableEventRepository extends AbstractNoSqlRepositoryPlugin {


	private static AtomicBoolean init = new AtomicBoolean(false);
	private static CodecRegistry pojoCodecRegistry;
	private static final String SCENARIO_EVENTS_COLLECTION_NAME="ScenarioEvents";
	@Autowired
	private MongoClient mongoClient;

	@Qualifier(MONGO_DB)
	@Autowired
	private String mongoDBName;

	private CodecRegistry getRegistry() {
		if(pojoCodecRegistry==null){
			PojoCodecProvider.Builder builder = PojoCodecProvider.builder();
			builder.register(BaseclassNoSQL.class,ScenarioSavableEvent.class);

			pojoCodecRegistry = fromRegistries(
					CodecRegistries.fromCodecs(new ScenarioSavableEventCodec()),
					MongoClientSettings.getDefaultCodecRegistry(),
					fromProviders(builder.build()));
		}
		return pojoCodecRegistry;



	}

	public void mergeScenarioSavableEvent(ScenarioSavableEvent o) {
		MongoDatabase db = mongoClient.getDatabase(mongoDBName);
		MongoCollection<ScenarioSavableEvent> collection = db.getCollection(
				SCENARIO_EVENTS_COLLECTION_NAME,
				ScenarioSavableEvent.class)
				.withCodecRegistry(getRegistry());
		collection.insertOne(o);
	}

	public FindIterable<ScenarioSavableEvent> getScenarioSavableEventIterable(
			ScenarioSavableEventFilter eventFiltering) {
		MongoDatabase db = mongoClient.getDatabase(mongoDBName).withCodecRegistry(getRegistry());
		MongoCollection<ScenarioSavableEvent> collection = db.getCollection(SCENARIO_EVENTS_COLLECTION_NAME, ScenarioSavableEvent.class).withCodecRegistry(getRegistry());

		Bson pred = getScenarioSavableEventPredicate(eventFiltering);
		FindIterable<ScenarioSavableEvent> base = pred == null ? collection.find(ScenarioSavableEvent.class) : collection.find(pred, ScenarioSavableEvent.class).sort(orderBy(ascending(BaseclassNoSQLRepository.DATE_CREATED)));
		if (eventFiltering.getPageSize() != null && eventFiltering.getCurrentPage() != null) {
			base.limit(eventFiltering.getCurrentPage())
					.skip(eventFiltering.getPageSize() * eventFiltering.getCurrentPage());

		}
		return base;
	}

	private Bson getScenarioSavableEventPredicate(
			ScenarioSavableEventFilter ScenarioSavableEventFilter) {
		Bson pred = BaseclassNoSQLRepository
				.getBaseclassNoSQLPredicates(ScenarioSavableEventFilter);
		if (ScenarioSavableEventFilter.getScenarioEventIds() != null && !ScenarioSavableEventFilter.getScenarioEventIds().isEmpty()) {
			Set<String> ids = ScenarioSavableEventFilter.getScenarioEventIds().stream().map(f->f.getId()).collect(Collectors.toSet());
			Bson bsonPred = in(ScenarioSavableEvent.SCENARIO_EVENT_ID, ids);
			pred = pred == null ? bsonPred : and(pred, bsonPred);
		}

		return pred;

	}

	public List<ScenarioSavableEvent> getAllScenarioSavableEvents(
			ScenarioSavableEventFilter ScenarioSavableEventFilter) {
		FindIterable<ScenarioSavableEvent> base = getScenarioSavableEventIterable(ScenarioSavableEventFilter);

		List<ScenarioSavableEvent> ScenarioSavableEvents = new ArrayList<>();
		for (ScenarioSavableEvent alert : base) {
			ScenarioSavableEvents.add(alert);
		}
		return ScenarioSavableEvents;

	}



	public long countAllScenarioSavableEvents(ScenarioSavableEventFilter ScenarioSavableEventFilter) {
		MongoDatabase db = mongoClient.getDatabase(mongoDBName)
				.withCodecRegistry(getRegistry());
		MongoCollection<ScenarioSavableEvent> collection = db.getCollection(
				SCENARIO_EVENTS_COLLECTION_NAME, ScenarioSavableEvent.class)
				.withCodecRegistry(getRegistry());

		Bson pred = getScenarioSavableEventPredicate(ScenarioSavableEventFilter);

		return pred == null ? collection.countDocuments() : collection.countDocuments(pred);

	}

	public long deleteAllScenarioSavableEvents(
			ScenarioSavableEventFilter ScenarioSavableEventFilter) {
		return deleteScenarioSavableEvents(ScenarioSavableEventFilter);
	}

	public long deleteScenarioSavableEvents(
			ScenarioSavableEventFilter ScenarioSavableEventFilter) {
		MongoDatabase db = mongoClient.getDatabase(mongoDBName)
				.withCodecRegistry(pojoCodecRegistry);
		MongoCollection<ScenarioSavableEvent> collection = db.getCollection(
				SCENARIO_EVENTS_COLLECTION_NAME,
				ScenarioSavableEvent.class)
				.withCodecRegistry(getRegistry());
		Bson pred = getScenarioSavableEventPredicate(ScenarioSavableEventFilter);
		DeleteResult deleteResult = collection.deleteMany(pred);
		return deleteResult.getDeletedCount();
	}

}
