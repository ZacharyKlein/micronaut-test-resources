/*
 * Copyright 2003-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.test.extensions.testresources;

import io.micronaut.context.annotation.Value;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.extensions.testresources.annotation.TestResourcesProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@TestResourcesProperties(
    providers = ConfigurationFilesTestResourcesPropertyProvidersTest.MyProvider.class
)
public class ConfigurationFilesTestResourcesPropertyProvidersTest {

    @Value("${derived.toto}")
    Integer value;

    @Value("${test-resources-config-visible}")
    Boolean testResourcesConfigurationIsVisible;

    @Test
    @DisplayName("configuration properties are accessible to the provider")
    public void canCallTestResourcesClient() {
        assertEquals(42, value);
        assertEquals(Boolean.TRUE, testResourcesConfigurationIsVisible);
    }

    public static class MyProvider implements TestResourcesPropertyProvider {
        @Override
        public Map<String, String> provide(Map<String, Object> testProperties) {
            return Map.of(
                "derived.toto", String.valueOf(2 + (Integer) testProperties.get("toto")),
                "test-resources-config-visible", String.valueOf(testProperties.containsKey("test-resources.containers.nats.image-name"))
            );
        }
    }
}
