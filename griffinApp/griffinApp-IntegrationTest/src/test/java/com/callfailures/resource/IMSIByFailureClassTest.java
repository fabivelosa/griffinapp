package com.callfailures.resource;

import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.callfailures.commontests.utils.JsonReader;
import com.callfailures.commontests.utils.ResourceClient;
import com.callfailures.errors.ErrorMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RunWith(Arquillian.class)
public class IMSIByFailureClassTest {

	//http://localhost:8080/callfailures/api/IMSIs/query/failureClass?=1
	private final static String VALID_IMSI_1 = "344930006766767";
	private final static String VALID_IMSI_2 = "344930000000011";
	private final static String VALID_FAILURE_CLASS= "1";
	private final static String INVALID_FAILURE_CLASS= "999";
	private final static String IMSI_API = "IMSIs/query/failureClass?failureClass=";
	private final static String LOGIN = "login/auth";
	private String token;
	
	@ArquillianResource
	private URL url;
	private ResourceClient resourceClient; 
	
	@Deployment
	public static WebArchive createDeployment() {		
		final WebArchive testWar =  ShrinkWrap
				.create(WebArchive.class)
				.addPackages(true, "com.callfailures")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("import.sql", "META-INF/import.sql")
				.addAsResource("persistence-integration.xml", "META-INF/persistence.xml")
				.setWebXML(new File("src/test/resources/web.xml"))
				.addAsLibraries(
						Maven.resolver().resolve("com.google.code.gson:gson:2.3.1", "org.mockito:mockito-core:1.9.5", 
								"org.apache.poi:poi-ooxml:4.1.2","org.apache.poi:poi:4.1.2", 
								"org.hibernate:hibernate-core:5.3.7.Final")
								.withTransitivity().asFile());
        
        return testWar;
	}	
	
	@Before
	public void initTestCase() {
		this.resourceClient = new ResourceClient(url);
		
		final Response loginRequest = resourceClient.resourcePath(LOGIN)
				.postWithFile(getPathFileRequest(LOGIN, "supportEngineer.json"));
		
		token = JsonReader.readAsJsonObject(loginRequest.readEntity(String.class))
				.get("token")
				.getAsString();
	}
	
	@Test
	@RunAsClient
	public void testGetIMSIfailuresForValidfailureClass() {
		final String valid_input_url = IMSI_API+VALID_FAILURE_CLASS;
		final Response responseGet = resourceClient.resourcePath(valid_input_url).get(token);
		assertEquals(200, responseGet.getStatus());
		
		
		final JsonArray imsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	

		assertEquals(2, imsis.size());
		JsonObject imsiEvent = imsis.get(0).getAsJsonObject();
		JsonObject imsiEvent2 = imsis.get(1).getAsJsonObject();
		assertEquals(VALID_IMSI_1, imsiEvent.get("imsi").getAsString());
		assertEquals(VALID_IMSI_2, imsiEvent2.get("imsi").getAsString());
	}
	
	@Test
	@RunAsClient
	public void testGetIMSIfailuresForNoImsiByFailureClass() {
		final String invalid_input_url = IMSI_API+INVALID_FAILURE_CLASS;
		final Response responseGet = resourceClient.resourcePath(invalid_input_url).get(token);
		assertEquals(200, responseGet.getStatus());
		final JsonArray imsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(0, imsis.size());
	}
	@Test
	@RunAsClient
	public void testGetIMSIfailuresForInvalidFailureClass() {
		final String invalid_input_url = IMSI_API+-999;
		final Response responseGet = resourceClient.resourcePath(invalid_input_url).get(token);
		assertEquals(404, responseGet.getStatus());
		final JsonObject responseMessage = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
		assertEquals(ErrorMessage.INVALID_FAILURECLASS.getMessage(), responseMessage.get("errorMessage").getAsString());
	}
}
