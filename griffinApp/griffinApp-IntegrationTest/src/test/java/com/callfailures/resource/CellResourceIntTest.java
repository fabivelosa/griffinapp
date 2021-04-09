package com.callfailures.resource;

import static com.callfailures.commontests.utils.FileTestNameUtils.*;
import static com.callfailures.commontests.utils.JsonTestUtils.*;
import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static com.callfailures.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
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


@RunWith(Arquillian.class)
public class CellResourceIntTest {
	private final static int cellID = 4;
	private final static String country = "Antigua and Barbuda";
	private final static String operator = "AT%26T Wireless-Antigua AG";
	private final static String CELLS_RESOURCE = "cells/query?cellId=" + cellID + "&country=" + country + "&operator=" + operator;
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
				.postWithFile(getPathFileRequest(LOGIN, "networkEngineer.json"));
		token = JsonReader.readAsJsonObject(loginRequest.readEntity(String.class))
				.get("token")
				.getAsString();
	}
	
	@Test
	@RunAsClient
	public void testGetEvents() {	
		final Response responseGet = resourceClient.resourcePath(CELLS_RESOURCE).get(token);
		assertEquals(200, responseGet.getStatus());		
		assertJsonResponseWithFile(responseGet, "response.json");
	}
	
	private void assertJsonResponseWithFile(final Response response, final String fileName) {
		assertJsonMatchesFileContent(response.readEntity(String.class), getPathFileResponse("cells", fileName));
	}
	
}
