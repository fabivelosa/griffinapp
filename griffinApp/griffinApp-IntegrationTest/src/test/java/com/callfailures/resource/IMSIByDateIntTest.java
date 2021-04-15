package com.callfailures.resource;

import static com.callfailures.commontests.utils.FileTestNameUtils.*;
import static com.callfailures.commontests.utils.JsonTestUtils.*;
import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static com.callfailures.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
import org.junit.Ignore;


import com.callfailures.commontests.utils.JsonReader;
import com.callfailures.commontests.utils.ResourceClient;
import com.callfailures.errors.ErrorMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@RunWith(Arquillian.class)
public class IMSIByDateIntTest {
	private final static String IMSI = "344930000000011";
	private final static String FROM_TIME = "1616061600000"; // March 18, 2021 10:00AM
	private final static String TO_TIME = "1616065200000"; // March 18, 2021 11:00AM
	private final static String FROM_TIME_INVALID = "1516061600000"; 
	private final static String TO_TIME_INVALID = "1516065200000"; 
	private final static String INVALID_TIME = "151600000";
	private final static String UNIQUE_IMSIS_URL = "IMSIs/query?from=";
	private final static String UNIQUE_TOP_IMSIS_URL = "IMSIs/query/limit?number=10&from=";
	//IMSIs/query?from=1546300800000&to=1616065200000
	private final static String IMSIS_BY_PHONE_MODEL = "events/query/ue/imsi";
	private final static String IMSIS_URL = "IMSIs/query/all";
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
	public void testGetUniqueIMSIByDate() {	
		final String urlForIMSI = UNIQUE_IMSIS_URL + FROM_TIME + "&to=" + TO_TIME;
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(200, responseGet.getStatus());
		
		final JsonArray uniqueImsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(2,uniqueImsis.size());
		JsonObject uniqueIMSI = uniqueImsis.get(0).getAsJsonObject();
		
		
		final String imsiOne = uniqueIMSI.get("imsi").getAsString();
		final String imsiTwo = uniqueImsis.get(1).getAsJsonObject().get("imsi").getAsString();
		assertFalse(imsiOne.equals(imsiTwo));
	}
	
	@Test
	@RunAsClient
	public void testGetUniqueIMSI_No_Unique() {	
		final String urlForIMSI = UNIQUE_IMSIS_URL + FROM_TIME_INVALID + "&to=" + TO_TIME_INVALID;
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(200, responseGet.getStatus());
		final JsonArray uniqueImsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(0,uniqueImsis.size());
	}
	
	@Test
	@RunAsClient
	public void testGetUniqueIMSI_Invalid_Date() {	
		final String urlForIMSI = UNIQUE_IMSIS_URL + FROM_TIME_INVALID + "&to=" + INVALID_TIME;
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(404, responseGet.getStatus());
		
		final JsonObject uniqueImsis = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
		assertEquals(ErrorMessage.INVALID_DATE.getMessage(), uniqueImsis.get("errorMessage").getAsString());
	}
	
	@Test
	@RunAsClient
	public void testGetUniqueIMSIAll() {	
		final String urlForIMSI = IMSIS_URL ;
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(200, responseGet.getStatus());
		
		final JsonArray uniqueImsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(2,uniqueImsis.size());
		JsonObject uniqueIMSI = uniqueImsis.get(0).getAsJsonObject();
		
		
		final String imsiOne = uniqueIMSI.get("imsi").getAsString();
		final String imsiTwo = uniqueImsis.get(1).getAsJsonObject().get("imsi").getAsString();
		assertFalse(imsiOne.equals(imsiTwo));
	}
	
	@Test
	@RunAsClient
	public void testGetTopUniqueIMSIByDate() {	
		final String urlForIMSI = UNIQUE_TOP_IMSIS_URL + FROM_TIME + "&to=" + TO_TIME;
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(200, responseGet.getStatus());
		
		final JsonArray uniqueImsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(2,uniqueImsis.size());
		JsonObject uniqueIMSI = uniqueImsis.get(0).getAsJsonObject();
		
		
		final String imsiOne = uniqueIMSI.get("imsi").getAsString();
		final String imsiTwo = uniqueImsis.get(1).getAsJsonObject().get("imsi").getAsString();
		assertFalse(imsiOne.equals(imsiTwo));
	}
	
	@Test
	@RunAsClient
	public void testGetTopUniqueIMSI_No_Unique() {	
		final String urlForIMSI = UNIQUE_TOP_IMSIS_URL + FROM_TIME_INVALID + "&to=" + TO_TIME_INVALID;
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(200, responseGet.getStatus());
		final JsonArray uniqueImsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(0,uniqueImsis.size());
	}
	
	@Test
	@RunAsClient
	public void testGetTopUniqueIMSI_Invalid_Date() {	
		final String urlForIMSI = UNIQUE_TOP_IMSIS_URL + TO_TIME + "&to=" + FROM_TIME;
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(404, responseGet.getStatus());
		
		final JsonObject uniqueImsis = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
		assertEquals(ErrorMessage.INVALID_DATE.getMessage(), uniqueImsis.get("errorMessage").getAsString());
	}
	
	@Test
	@RunAsClient
	public void testGetListOfEventsOfIMSIByDate() {	
		final String urlForIMSI = UNIQUE_IMSIS_URL + FROM_TIME + "&to=" + TO_TIME + "&imsi=" + IMSI;
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(200, responseGet.getStatus());
		assertJsonResponseWithFile(responseGet, "response.json");
	}
	
	private void assertJsonResponseWithFile(final Response response, final String fileName) {
		assertJsonMatchesFileContent(response.readEntity(String.class), getPathFileResponse("imsis", fileName));
	}
	
	@Test
	@RunAsClient
	public void testGetListOfEventsOfIMSIByPhoneModel() {
		final String urlForIMSI = IMSIS_BY_PHONE_MODEL+"?model=VEA3&from="+FROM_TIME+"&to="+TO_TIME+"";
		final Response responseGet = resourceClient.resourcePath(urlForIMSI).get(token);
		assertEquals(200, responseGet.getStatus());
		final JsonArray imsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(3,imsis.size());
	}
}
