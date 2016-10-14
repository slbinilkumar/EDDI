package io.sls.resources.rest.behavior;

import io.sls.persistence.IResourceStore;
import io.sls.resources.rest.behavior.model.BehaviorConfiguration;

import java.util.List;

/**
 * User: jarisch
 * Date: 21.07.12
 * Time: 12:38
 */
public interface IBehaviorStore extends IResourceStore<BehaviorConfiguration> {
    List<String> readBehaviorRuleNames(String id, Integer version, String filter, String order, Integer limit) throws ResourceStoreException, ResourceNotFoundException;
}
