package org.dee.client;

import org.dee.entity.TestEntity;
import org.dee.framework.client.IClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "123")
public interface TestEntityClient extends IClient<TestEntity> {
}
