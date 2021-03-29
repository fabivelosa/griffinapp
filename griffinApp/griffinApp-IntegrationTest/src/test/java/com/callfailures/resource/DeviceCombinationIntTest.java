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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@RunWith(Arquillian.class)
public class DeviceCombinationIntTest {

	private final static int VALID_CELL = 4;
	private final static int VALID_OPERATOR = 930;
	private final static int VALID_MARKET= 344;
	private final static String fromTime = "1616061600000"; // March 18, 2021 10:00AM
	private final static String toTime = "1616065200000"; // March 18, 2021 11:00AM
	private final static String COMBINATION_PATH = "Combinations/query?from="+fromTime+"&to="+toTime;
	private final static String LOGIN = "login/auth";
	private String token;
	//http://localhost:8080/callfailures/api/Combinations/query?from=1546300800000&to=1616065200000
	
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
				.postWithFile(getPathFileRequest(LOGIN, "networkEngineer.json"));
		
		token = JsonReader.readAsJsonObject(loginRequest.readEntity(String.class))
				.get("token")
				.getAsString();
	}
	
	@Test
	@RunAsClient
	public void testGetComboByValidDate() {
		final Response responseGet = resourceClient.resourcePath(COMBINATION_PATH).get(token);
		assertEquals(200, responseGet.getStatus());

		final JsonArray combinations = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(1, combinations.size());
		
		JsonObject combination = combinations.get(0).getAsJsonObject();
		assertEquals(VALID_CELL, combination.get("cellId").getAsInt());
		JsonObject marketOperator = combination.get("marketOperator").getAsJsonObject();
		assertEquals(VALID_OPERATOR, marketOperator.get("operatorCode").getAsInt());
		assertEquals(VALID_MARKET, combination.get("countryCode").getAsInt());
	
		/*
		JsonObject eventCause = imsiEvent.get("eventCause").getAsJsonObject();
		assertEquals(4098, eventCause.get("eventCauseId").getAsJsonObject().get("eventCauseId").getAsInt());
		assertEquals(1, eventCause.get("eventCauseId").getAsJsonObject().get("causeCode").getAsInt());
		*/
	}
	
	
	
}
