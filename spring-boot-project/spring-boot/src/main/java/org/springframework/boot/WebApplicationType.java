/*
 * Copyright 2012-2022 the original author or authors.
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

package org.springframework.boot;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.util.ClassUtils;

/**
 * An enumeration of possible types of web application.
 *
 * @author Andy Wilkinson
 * @author Brian Clozel
 * @since 2.0.0
 */
public enum WebApplicationType {

	/**
	 * The application should not run as a web application and should not start an
	 * embedded web server.
	 */
	NONE,

	/**
	 * The application should run as a servlet-based web application and should start an
	 * embedded servlet web server.
	 */
	SERVLET,

	/**
	 * The application should run as a reactive web application and should start an
	 * embedded reactive web server.
	 */
	REACTIVE;

	private static final String[] SERVLET_INDICATOR_CLASSES = { "jakarta.servlet.Servlet",
			"org.springframework.web.context.ConfigurableWebApplicationContext" };

	private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

	private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

	private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

	/**
	 * 这段代码是 Spring Boot 中用于 自动推断当前应用类型 的核心方法，通过检查类路径（classpath）上是否存在特定的关键类，
	 * 决定应用是 Servlet Web 应用（如 Spring MVC）、响应式 Web 应用（如 WebFlux） 还是 非 Web 应用。
	 * @return
	 *
	 * 场景 1：Spring MVC 项目
	 *
	 *     依赖：spring-boot-starter-web
	 *
	 *     类路径：存在 DispatcherServlet，不存在 DispatcherHandler
	 *
	 *     推断结果：WebApplicationType.SERVLET
	 *
	 * 场景 2：WebFlux 项目
	 *
	 *     依赖：spring-boot-starter-webflux，不包含 spring-boot-starter-web
	 *
	 *     类路径：存在 DispatcherHandler，不存在 DispatcherServlet
	 *
	 *     推断结果：WebApplicationType.REACTIVE
	 *
	 * 场景 3：控制台应用
	 *
	 *     依赖：无 Web 相关 starter
	 *
	 *     类路径：缺少 Servlet 和 WebFlux 类
	 *
	 *     推断结果：WebApplicationType.NONE
	 */
	static WebApplicationType deduceFromClasspath() {
		if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
				&& !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
			return WebApplicationType.REACTIVE;
		}
		for (String className : SERVLET_INDICATOR_CLASSES) {
			if (!ClassUtils.isPresent(className, null)) {
				return WebApplicationType.NONE;
			}
		}
		return WebApplicationType.SERVLET;
	}

	static class WebApplicationTypeRuntimeHints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			for (String servletIndicatorClass : SERVLET_INDICATOR_CLASSES) {
				registerTypeIfPresent(servletIndicatorClass, classLoader, hints);
			}
			registerTypeIfPresent(JERSEY_INDICATOR_CLASS, classLoader, hints);
			registerTypeIfPresent(WEBFLUX_INDICATOR_CLASS, classLoader, hints);
			registerTypeIfPresent(WEBMVC_INDICATOR_CLASS, classLoader, hints);
		}

		private void registerTypeIfPresent(String typeName, ClassLoader classLoader, RuntimeHints hints) {
			if (ClassUtils.isPresent(typeName, classLoader)) {
				hints.reflection().registerType(TypeReference.of(typeName));
			}
		}

	}

}
