package com.callfailures.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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


public class UniqueIMSIRetreival {

	private final static String fromTime = "1546300800000"; // March 18, 2021 10:00AM
	private final static String toTime = "1616065200000"; // March 18, 2021 11:00AM
	private final static String ALL_USER_EQUIPMENT = "userEquipment/";
	private final static String UNIQUE_IMSIS = "IMSIs/query?from=" + fromTime + "&to=" + toTime;
	//IMSIs/query?from=1546300800000&to=1616065200000
	
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
		System.out.println(UNIQUE_IMSIS);
		final Response responseGet = resourceClient.resourcePath(UNIQUE_IMSIS).get();
		assertEquals(200, responseGet.getStatus());

		final JsonArray uniqueImsis = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));	
		assertEquals(1,uniqueImsis.size());
		JsonObject uniqueImsi = uniqueImsis.get(0).getAsJsonObject();
		System.out.println(uniqueImsi);
		//Assert Count = x
		//assert not null
		//Assert Date
		//assertEquals(imsi, imsiSummaryJSON.get("imsi").getAsString());
	}
	
	
	
}
