package com.callfailures.resource;

import static com.callfailures.commontests.utils.FileTestNameUtils.*;
import static com.callfailures.commontests.utils.JsonTestUtils.*;
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

import com.callfailures.commontests.utils.ResourceClient;


@RunWith(Arquillian.class)
public class UserResourceIntTest {
	private final static String RESOURCE_PATH = "users";

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
	public void testAddUser() {	
		final Response responsePost = resourceClient.resourcePath(RESOURCE_PATH)
				.postWithFile(getPathFileRequest(RESOURCE_PATH, "newUser.json"));
		assertEquals(200, responsePost.getStatus());
		assertJsonResponseWithFile(responsePost, "newUser.json");
	}
	
	@Test
	@RunAsClient
	public void testUpdateUser() {	
		final Response responsePut = resourceClient.resourcePath(RESOURCE_PATH)
				.putWithFile(getPathFileRequest(RESOURCE_PATH, "updatedUser.json"));
		assertEquals(200, responsePut.getStatus());
		assertJsonResponseWithFile(responsePut, "updatedUser.json");
	}
	
	
	private void assertJsonResponseWithFile(final Response response, final String fileName) {
		assertJsonMatchesFileContent(response.readEntity(String.class), getPathFileResponse(RESOURCE_PATH, fileName));
	}
	
}
