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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.callfailures.commontests.utils.JsonReader;
import com.callfailures.commontests.utils.ResourceClient;
import com.callfailures.errors.ErrorMessage;
import com.google.gson.JsonObject;


@RunWith(Arquillian.class)
public class EventsResourceIntegrationTest {
	private final static String imsi = "344930000000011";
	private final static String invalid_imsi = "3449300000000119";
	private final static String inexistent_imsi= "244930000000011";
	private final static String fromTime = "1616061600000"; // March 18, 2021 10:00AM
	private final static String toTime = "1616065200000"; // March 18, 2021 11:00AM
	private final static boolean summary = true;
	private final static String IMSI_SUMMARY_BY_DATE = "events/query?imsi=" + imsi + "&from=" + fromTime + "&to=" + toTime + "&summary=" + summary;

	
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
	public void testGetIMSISummaryByDate() {	
		final Response responseGet = resourceClient.resourcePath(IMSI_SUMMARY_BY_DATE).get();
		assertEquals(200, responseGet.getStatus());

		final JsonObject imsiSummaryJSON = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
		assertEquals(imsi, imsiSummaryJSON.get("imsi").getAsString());
		assertEquals(2L, imsiSummaryJSON.get("callFailuresCount").getAsLong());
		assertEquals(2000L, imsiSummaryJSON.get("totalDurationMs").getAsLong());
	}
	
	@Test
	@RunAsClient
	public void testGetIMSISummaryByDateInvalidIMSI() {	
		final String IMSI_SUMMARY_BY_DATE = "events/query?imsi=" + invalid_imsi + "&from=" + fromTime + "&to=" + toTime + "&summary=" + summary;

		final Response responseGet = resourceClient.resourcePath(IMSI_SUMMARY_BY_DATE).get();
		assertEquals(404, responseGet.getStatus());

		final JsonObject imsiSummaryJSON = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
		assertEquals(ErrorMessage.INVALID_IMSI.getMessage(), imsiSummaryJSON.get("errorMessage").getAsString());
	}
	
	@Test
	@RunAsClient
	public void testGetIMSISummaryByDateInvalidDate() {	
		final String IMSI_SUMMARY_BY_DATE = "events/query?imsi=" + imsi + "&from=" + toTime + "&to=" + fromTime + "&summary=" + summary;

		final Response responseGet = resourceClient.resourcePath(IMSI_SUMMARY_BY_DATE).get();
		assertEquals(404, responseGet.getStatus());

		final JsonObject imsiSummaryJSON = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
		assertEquals(ErrorMessage.INVALID_DATE.getMessage(), imsiSummaryJSON.get("errorMessage").getAsString());
	}
	
	@Test
	@RunAsClient
	public void testGetIMSISummaryByDateZeroResult() {	
		final String IMSI_SUMMARY_BY_DATE = "events/query?imsi=" + inexistent_imsi + "&from=" + fromTime + "&to=" + toTime + "&summary=" + summary;
		
		final Response responseGet = resourceClient.resourcePath(IMSI_SUMMARY_BY_DATE).get();
		assertEquals(200, responseGet.getStatus());

		final JsonObject imsiSummaryJSON = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
		assertEquals(inexistent_imsi, imsiSummaryJSON.get("imsi").getAsString());
		assertEquals(0, imsiSummaryJSON.get("callFailuresCount").getAsLong());
		assertEquals(0, imsiSummaryJSON.get("totalDurationMs").getAsLong());
	}
}