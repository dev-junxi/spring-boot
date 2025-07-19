/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.docs.features.springapplication.custom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.pulsar.PulsarAutoConfiguration;
import org.springframework.boot.autoconfigure.pulsar.PulsarReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

/**
 * @SpringBootApplication注解是一个组合注解，主要由@SpringBootConfiguration,@EnableAutoConfiguration和@ComponentScan这三个注解组合而成。
 */
@SpringBootApplication(scanBasePackages = "org.springframework.boot.docs.features.springapplication.custom",
exclude = {
		DataSourceAutoConfiguration.class,
		R2dbcAutoConfiguration.class,
		CassandraAutoConfiguration.class,
		MongoAutoConfiguration.class,
		MetricsAutoConfiguration.class,
		PulsarAutoConfiguration.class,
		PulsarReactiveAutoConfiguration.class
})
public class MyCustomApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyCustomApplication.class, args);
	}

}
