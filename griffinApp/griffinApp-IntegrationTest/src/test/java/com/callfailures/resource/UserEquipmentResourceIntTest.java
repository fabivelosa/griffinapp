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
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.callfailures.commontests.utils.JsonReader;
import com.callfailures.commontests.utils.ResourceClient;
import com.callfailures.dao.UserEquipmentDAO;
import com.callfailures.entity.UserEquipment;
import com.callfailures.exception.FieldNotValidException;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.UserEquipmentService;
import com.callfailures.services.ValidationService;
import com.callfailures.services.impl.UserEquipmentServceImpl;
import com.callfailures.services.impl.ValidationServiceImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


@RunWith(Arquillian.class)
public class UserEquipmentResourceIntTest {
	private final static String ALL_USER_EQUIPMENT = "userEquipment/";

	
	@ArquillianResource
	private URL url; 

	private ResourceClient resourceClient;

	@Deployment
	public static WebArchive createDeployment() {		
		final WebArchive testWar =  ShrinkWrap
				.create(WebArchive.class)
				.addPackages(true, "com.callfailures")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsResource("importUserEquipment.sql", "META-INF/import.sql")
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
	public void testFindAll() {	
		final Response responseGet = resourceClient.resourcePath(ALL_USER_EQUIPMENT).get();
		assertEquals(200, responseGet.getStatus());

		final JsonArray result = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));
		assertEquals(1, result.size());
		JsonObject userEquipment = result.get(0).getAsJsonObject();
		assertEquals(21060800, userEquipment.get("tac").getAsInt());
		assertEquals("VEA3", userEquipment.get("model").getAsString());
	}
	
}