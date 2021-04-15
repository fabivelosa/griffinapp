package com.callfailures.resource;

import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileRequest;
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
public class UploadFileResourceIntTest {
	
	private final static String FILE_UPLOAD = "file/upload";
	private final static String LOGIN = "login/auth";
	private  String token;
	private final String filepath1 = "src/test/resources/data/correctData.xlsx";
	private final String filepath2 = "src/test/resources/data/largeData.xlsx";
	
	@ArquillianResource
	private URL url;
	private ResourceClient resourceClient; 
	
	@Deployment
	public static WebArchive createDeployment() {		
		final WebArchive testWar =  ShrinkWrap
				.create(WebArchive.class)
				.addPackages(true, "com.callfailures")
				.addPackages(true, "org.apache.commons.io")
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
				.postWithFile(getPathFileRequest(LOGIN, "systemAdmin.json"));
		
		token = JsonReader.readAsJsonObject(loginRequest.readEntity(String.class))
				.get("token")
				.getAsString();		
	}
	
	@Test
	@RunAsClient
	public void testUploadFileJob() {
		final String validUploadURI = FILE_UPLOAD;
		final Response responseGet = resourceClient.resourcePath(validUploadURI).postWithContentMultiPart(token, filepath1);
		assertEquals(200, responseGet.getStatus());
	}
	
	@Test
	@RunAsClient
	public void testUploadFileStatus() {
		final String validUploadURI = FILE_UPLOAD;
		final Response responseGet = resourceClient.resourcePath(validUploadURI).postWithContentMultiPart(token, filepath1);
		assertEquals(200, responseGet.getStatus());
		String uploadId = JsonReader.readAsJsonObject(responseGet.readEntity(String.class)).get("uploadID").getAsString();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final String validGetUpload = FILE_UPLOAD+"/"+uploadId;
		final Response responseGet1 = resourceClient.resourcePath(validGetUpload).get(token);
		assertEquals(200, responseGet1.getStatus());
		int uploadStatus = JsonReader.readAsJsonObject(responseGet1.readEntity(String.class)).get("uploadStatus").getAsInt();
		assertEquals(100, uploadStatus);
	}
	
}