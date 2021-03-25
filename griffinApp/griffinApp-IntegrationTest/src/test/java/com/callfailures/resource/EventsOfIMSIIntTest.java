package com.callfailures.resource;

import static org.junit.Assert.*;

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
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.callfailures.commontests.utils.JsonReader;
import com.callfailures.commontests.utils.ResourceClient;
import com.callfailures.errors.ErrorMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RunWith(Arquillian.class)
public class EventsOfIMSIIntTest {
	
	private final static String IMSI_VALID = "344930000000011";
	private final static String IMSI_INVALID = "3449300000000119";
	private final static String IMSI_INEXISTENT= "244930000000011";
	private final static String IMSI_FAILURE_DATA = "failures/";
	
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
	}
	
	@Test
	@RunAsClient
	public void testGetIMSIfailuresForValidIMSI() {
		final String validImsiURI = IMSI_FAILURE_DATA+IMSI_VALID;
		final Response responseGet = resourceClient.resourcePath(validImsiURI).get();
		assertEquals(200, responseGet.getStatus());

		final JsonArray imisiEvents = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	

		assertEquals(1, imisiEvents.size());
		//Get event from array
		JsonObject imsiEvent = imisiEvents.get(0).getAsJsonObject();
		
		//Get cause code and ID
		JsonObject eventCause = imsiEvent.get("eventCause").getAsJsonObject();
		assertEquals(4098, eventCause.get("eventCauseId").getAsJsonObject().get("eventCauseId").getAsInt());
		assertEquals(1, eventCause.get("eventCauseId").getAsJsonObject().get("causeCode").getAsInt());
	}
	
	@Test
	@RunAsClient
	public void testGetIMSIfailuresForInvalidIMSI() {
		final String validImsiURI = IMSI_FAILURE_DATA+IMSI_INVALID;
		final Response responseGet = resourceClient.resourcePath(validImsiURI).get();
		assertEquals(404, responseGet.getStatus());

		final JsonObject imsievent = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
		assertEquals(ErrorMessage.INVALID_IMSI.getMessage(), imsievent.get("errorMessage").getAsString());
	}
	
}