package org.jfl110.quickstart.example;

import com.google.inject.ImplementedBy;

@ImplementedBy(ExampleService.Impl.class)
interface ExampleService {

	JsonBean readBean();

	JsonBean getSecureBean(String name);

	class Impl implements ExampleService {
		@Override
		public JsonBean readBean() {
			return new JsonBean("Dave");
		}

		@Override
		public JsonBean getSecureBean(String name) {
			return new JsonBean(name + "Secure");
		}
	}

	static final class JsonBean {
		private final String name;

		JsonBean(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}