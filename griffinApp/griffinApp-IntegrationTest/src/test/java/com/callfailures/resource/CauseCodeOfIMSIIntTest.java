package com.callfailures.resource;

import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static com.callfailures.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
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

@RunWith(Arquillian.class)
public class CauseCodeOfIMSIIntTest {
	
	private final static String IMSI_VALID = "344930000000011";
	private final static String IMSI_INVALID = "3449300000000119";
	private final static String RESOURCE_PATH = "causecodes";
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
				.postWithFile(getPathFileRequest(LOGIN, "customerSupport.json"));
		token = JsonReader.readAsJsonObject(loginRequest.readEntity(String.class))
				.get("token")
				.getAsString();
	}
	
	@Test
	@RunAsClient
	public void testGetIMSIfailuresForValidIMSI() {
		final String validImsiURI = RESOURCE_PATH+ "/" + IMSI_VALID;
		final Response responseGet = resourceClient.resourcePath(validImsiURI).get(token);
		assertEquals(200, responseGet.getStatus());
		assertJsonResponseWithFile(responseGet, "response.json");
		
	}
	
	@Test
	@RunAsClient
	public void testGetIMSIfailuresForInvalidIMSI() {
		final String validImsiURI = RESOURCE_PATH+ "/" + IMSI_INVALID;
		final Response responseGet = resourceClient.resourcePath(validImsiURI).get(token);
		assertEquals(404, responseGet.getStatus());
		assertJsonResponseWithFile(responseGet, "errorResponse.json");
	}
	
	private void assertJsonResponseWithFile(final Response response, final String fileName) {
		assertJsonMatchesFileContent(response.readEntity(String.class), getPathFileResponse(RESOURCE_PATH, fileName));
	}
	
}