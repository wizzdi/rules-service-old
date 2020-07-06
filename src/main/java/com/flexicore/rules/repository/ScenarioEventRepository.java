package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.BaseclassNoSQLRepository;
import com.flexicore.interfaces.AbstractNoSqlRepositoryPlugin;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.request.ScenarioEventFilter;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
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
public class ScenarioEventRepository extends AbstractNoSqlRepositoryPlugin {


	private static AtomicBoolean init = new AtomicBoolean(false);
	private static CodecRegistry pojoCodecRegistry;
	@Autowired
	private MongoClient mongoClient;

	@Qualifier(MONGO_DB)
	@Autowired
	private String mongoDBName;

	@PostConstruct
	private void postConstruct() {
		if (init.compareAndSet(false, true)) {
			PojoCodecProvider.Builder builder = PojoCodecProvider.builder();
			builder.register(ScenarioEvent.class);
			pojoCodecRegistry = fromRegistries(
					MongoClientSettings.getDefaultCodecRegistry(),
					fromProviders(builder.build()));
		}

	}

	public void mergeScenarioEvent(ScenarioEvent o) {
		MongoDatabase db = mongoClient.getDatabase(mongoDBName);
		MongoCollection<ScenarioEvent> collection = db.getCollection(
				BaseclassNoSQLRepository.BASECLASS_NO_SQL,
				ScenarioEvent.class)
				.withCodecRegistry(pojoCodecRegistry);
		collection.insertOne(o);
	}

	public FindIterable<ScenarioEvent> getScenarioEventIterable(
			ScenarioEventFilter eventFiltering) {
		MongoDatabase db = mongoClient.getDatabase(mongoDBName).withCodecRegistry(pojoCodecRegistry);
		MongoCollection<ScenarioEvent> collection = db.getCollection(BaseclassNoSQLRepository.BASECLASS_NO_SQL, ScenarioEvent.class).withCodecRegistry(pojoCodecRegistry);

		Bson pred = getScenarioEventPredicate(eventFiltering);
		FindIterable<ScenarioEvent> base = pred == null ? collection.find(ScenarioEvent.class) : collection.find(pred, ScenarioEvent.class).sort(orderBy(ascending(BaseclassNoSQLRepository.DATE_CREATED)));
		if (eventFiltering.getPageSize() != null && eventFiltering.getCurrentPage() != null) {
			base.limit(eventFiltering.getCurrentPage())
					.skip(eventFiltering.getPageSize() * eventFiltering.getCurrentPage());

		}
		return base;
	}

	private Bson getScenarioEventPredicate(
			ScenarioEventFilter scenarioEventFilter) {
		Bson pred = BaseclassNoSQLRepository
				.getBaseclassNoSQLPredicates(scenarioEventFilter);
		if (scenarioEventFilter.getOnlyIds() != null && !scenarioEventFilter.getOnlyIds().isEmpty()) {
			Set<String> ids = scenarioEventFilter.getOnlyIds();
			Bson bsonPred = in(BaseclassNoSQLRepository.ID, ids);
			pred = pred == null ? bsonPred : and(pred, bsonPred);
		}

		return pred;

	}

	public List<ScenarioEvent> getAllScenarioEvents(
			ScenarioEventFilter scenarioEventFilter) {
		FindIterable<ScenarioEvent> base = getScenarioEventIterable(scenarioEventFilter);

		List<ScenarioEvent> scenarioEvents = new ArrayList<>();
		for (ScenarioEvent alert : base) {
			scenarioEvents.add(alert);
		}
		return scenarioEvents;

	}



	public long countAllScenarioEvents(ScenarioEventFilter scenarioEventFilter) {
		MongoDatabase db = mongoClient.getDatabase(mongoDBName)
				.withCodecRegistry(pojoCodecRegistry);
		MongoCollection<ScenarioEvent> collection = db.getCollection(
				BaseclassNoSQLRepository.BASECLASS_NO_SQL, ScenarioEvent.class)
				.withCodecRegistry(pojoCodecRegistry);

		Bson pred = getScenarioEventPredicate(scenarioEventFilter);

		return pred == null ? collection.countDocuments() : collection.countDocuments(pred);

	}

	public long deleteAllScenarioEvents(
			ScenarioEventFilter scenarioEventFilter) {
		return deleteScenarioEvents(scenarioEventFilter);
	}

	public long deleteScenarioEvents(
			ScenarioEventFilter scenarioEventFilter) {
		MongoDatabase db = mongoClient.getDatabase(mongoDBName)
				.withCodecRegistry(pojoCodecRegistry);
		MongoCollection<ScenarioEvent> collection = db.getCollection(
				BaseclassNoSQLRepository.BASECLASS_NO_SQL,
				ScenarioEvent.class)
				.withCodecRegistry(pojoCodecRegistry);
		Bson pred = getScenarioEventPredicate(scenarioEventFilter);
		DeleteResult deleteResult = collection.deleteMany(pred);
		return deleteResult.getDeletedCount();
	}

}
