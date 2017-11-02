package org.jfl110.quickstart.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jfl110.quickstart.time.CurrentDateTime;
import org.jfl110.testing.utils.EmbeddedJetty;
import org.joda.time.LocalDateTime;
import org.junit.ClassRule;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Module;

/**
 * Example integration test for TestAppContextListener.
 *
 * @author JFL110
 */
public class TestIntegration {
	
	private static final LocalDateTime CURRENT_TIME = new LocalDateTime(2017, 11, 1, 12, 34, 55);
	@ClassRule
	public static final EmbeddedJetty server = EmbeddedJetty
			.embeddedJetty().withContextListener(
			  new AppContextListener((Module) binder -> 
			    binder.bind(LocalDateTime.class).annotatedWith(CurrentDateTime.class).toProvider(() -> CURRENT_TIME)))
			.build();
	
	/**
	 * Tests the '.../secure-bean' path without the token header
	 */
	@Test
	public void testSecureBeanNoHeader() {
		Response response = ClientBuilder
							.newClient()
							.target(server.getBaseUri())
							.path("service/resource/secure-bean")
							.request(MediaType.APPLICATION_JSON)
							.head();

		assertEquals(406, response.getStatus());
	}
	
	
	/**
	 * Tests the '.../secure-bean' path with the token header
	 */
	@Test
	public void testSecureBeanWithHeader() {
		final String nameIn = "Bob";
		
		ClientBean beanResponse = ClientBuilder
								.newClient()
								.target(server.getBaseUri())
								.path("service/resource/secure-bean")
								.queryParam("name", nameIn)
								.request(MediaType.APPLICATION_JSON)
								.header("token","a-token")
								.get(ClientBean.class);

		assertNotNull(beanResponse);
		assertEquals(nameIn+"Secure", beanResponse.name);
	}
	
	
	
	/**
	 * Tests the '.../read-bean' path
	 */
	@Test
	public void testReadBeanPath() {
		ClientBean bean = ClientBuilder
							.newClient()
							.target(server.getBaseUri())
							.path("service/resource/read-bean")
							.request(MediaType.APPLICATION_JSON).get(ClientBean.class);

		assertNotNull(bean);
		assertEquals("Dave", bean.name);
	}

	
	/**
	 * Tests the '/word' path
	 */
	@Test
	public void testWordPath() {
		String word = ClientBuilder
						.newClient()
						.target(server.getBaseUri())
						.path("word")
						.request(MediaType.TEXT_PLAIN_TYPE).get(String.class);

		assertEquals("bird", word);
	}
	
	
	/**
	 * Tests the '/file' path
	 */
	@Test
	public void testFilePath() {
		String fileContents = ClientBuilder
						.newClient()
						.target(server.getBaseUri())
						.path("file")
						.request(MediaType.TEXT_PLAIN_TYPE).get(String.class);

		assertEquals("Some text!", fileContents);
	}
	

	/**
	 * Tests the '/time' path
	 */
	@Test
	public void testTimePath() {
		String time = ClientBuilder
						.newClient()
						.target(server.getBaseUri())
						.path("service/resource/time")
						.request(MediaType.TEXT_PLAIN_TYPE).get(String.class);

		assertEquals(CURRENT_TIME.toString(), time);
	}
	
	
	/**
	 * Tests the '/time' path
	 */
	@Test
	public void testFileCachedPath() {
		String fileContents = ClientBuilder
						.newClient()
						.target(server.getBaseUri())
						.path("file-cached")
						.request(MediaType.TEXT_PLAIN_TYPE).get(String.class);

		assertEquals("Some text!", fileContents);
	}
	
	
	public static class ClientBean{
		
		final String name;
		
		@JsonCreator
		public ClientBean(@JsonProperty("name") String name){
			this.name = name;
		}
	}
}
