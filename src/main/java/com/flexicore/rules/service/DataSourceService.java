package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.rules.model.DataSource;
import com.flexicore.rules.repository.DataSourceRepository;
import com.flexicore.rules.request.DataSourceCreate;
import com.flexicore.rules.request.DataSourceFilter;
import com.flexicore.rules.request.DataSourceUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.List;

@PluginInfo(version = 1)
@Extension
@Component
public class DataSourceService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private DataSourceRepository repository;

	@Autowired
	private DynamicInvokersService dynamicInvokersService;

	public void validate(DataSourceFilter dataSourceArgumentFilter,
			SecurityContext securityContext) {

	}

	public void validate(DataSourceCreate creationContainer,
			SecurityContext securityContext) {
		String dynamicExecutionId = creationContainer.getDynamicExecutionId();
		DynamicExecution dynamicExecution = dynamicExecutionId != null
				? getByIdOrNull(dynamicExecutionId, DynamicExecution.class,
						null, securityContext) : null;
		if (dynamicExecution == null && dynamicExecutionId != null) {
			throw new BadRequestException("No Dynamic Execution With id "
					+ dynamicExecutionId);
		}
		creationContainer.setDynamicExecution(dynamicExecution);

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public DataSource createDataSource(
			DataSourceCreate creationContainer,
			SecurityContext securityContext) {
		DataSource dataSource = createDataSourceNoMerge(
				creationContainer, securityContext);
		repository.merge(dataSource);
		return dataSource;

	}

	public DataSource updateDataSource(
			DataSourceUpdate creationContainer,
			SecurityContext securityContext) {
		DataSource dataSource = creationContainer.getDataSource();
		if (updateDataSourceNoMerge(dataSource, creationContainer)) {
			repository.merge(dataSource);

		}
		return dataSource;

	}

	private DataSource createDataSourceNoMerge(
			DataSourceCreate creationContainer,
			SecurityContext securityContext) {
		DataSource dataSource = new DataSource(creationContainer.getName(), securityContext);
		updateDataSourceNoMerge(dataSource, creationContainer);
		return dataSource;
	}

	private boolean updateDataSourceNoMerge(DataSource dataSource,
			DataSourceCreate dataSourceCreate) {
		boolean update = false;
		if (dataSourceCreate.getName() != null
				&& !dataSourceCreate.getName().equals(
						dataSource.getName())) {
			dataSource.setName(dataSourceCreate.getName());
			update = true;
		}
		if (dataSourceCreate.getDescription() != null
				&& !dataSourceCreate.getDescription().equals(
						dataSource.getDescription())) {
			dataSource
					.setDescription(dataSourceCreate.getDescription());
			update = true;
		}
		if (dataSourceCreate.getDynamicExecution() != null
				&& (dataSource.getDynamicExecution() == null || !dataSourceCreate
						.getDynamicExecution().getId()
						.equals(dataSource.getDynamicExecution().getId()))) {
			dataSource.setDynamicExecution(dataSourceCreate
					.getDynamicExecution());
			update = true;
		}
		return update;

	}

	public PaginationResponse<DataSource> getAllDataSources(
			DataSourceFilter filter, SecurityContext securityContext) {
		List<DataSource> list = repository.listAllDataSources(filter,
				securityContext);
		long count = repository
				.countAllDataSources(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}
}
