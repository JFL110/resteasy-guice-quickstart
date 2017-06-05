package org.jfl110.quickstart.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jfl110.quickstart.EmbeddedJetty;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Example integration test for TestAppContextListener.
 *
 * @author JFL110
 */
public class TestIntegration {
	
	public static final EmbeddedJetty server = EmbeddedJetty.embeddedJetty()
												.withContextListener(new AppContextListener())
												.build();
	
	
	@BeforeClass
	public static void beforeClass() throws Exception{
		server.start();
	}
	
	
	@AfterClass
	public static void afterClass() throws Exception{
		server.stop();
	}
	
	
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

		assertEquals(406,response.getStatus());
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
		assertEquals(nameIn+"Secure",beanResponse.name);
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
		assertEquals("Dave",bean.name);
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

		assertEquals("bird",word);
	}
	
	
	
	public static class ClientBean{
		final String name;
		
		@JsonCreator
		public ClientBean(@JsonProperty("name") String name){
			this.name = name;
		}
	}
}
