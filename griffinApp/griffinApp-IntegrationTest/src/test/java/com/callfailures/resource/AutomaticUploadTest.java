package com.callfailures.resource;

import static com.callfailures.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.callfailures.commontests.utils.JsonReader;
import com.callfailures.commontests.utils.ResourceClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.undertow.util.FileUtils;

@RunWith(Arquillian.class)
public class AutomaticUploadTest {

	private final String resourcePath = "src/test/resources/mini_data.xlsx";
	final String serverfilePath = "opt/wildfly/bin/fileUploads";
	final String localFilePath = "\\fileUploads\\mini_data.xlsx";
	
	final String uploadPath = "";
	
	@ArquillianResource
	private URL url; 

	private ResourceClient resourceClient;
	
	private final static String LOGIN = "login/auth";
	private String token;

	
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
				.postWithFile(getPathFileRequest(LOGIN, "systemAdmin.json"));
		
		token = JsonReader.readAsJsonObject(loginRequest.readEntity(String.class))
				.get("token")
				.getAsString();
	}
	
	@Test
	@RunAsClient
	public void testItem() throws InterruptedException, IOException {	

		//check responcse size
	
		
		//Assert file is found in dir
		final String allUploads = "/file/upload/all";
		//response
		final Response responseGet = resourceClient.resourcePath(allUploads).get(token);
		assertEquals(200, responseGet.getStatus());
		final JsonArray uploads = JsonReader.readAsJsonArray(responseGet.readEntity(String.class));
		assertEquals(4, uploads.size());
		JsonObject uploadList = uploads.get(0).getAsJsonObject();
	
		//check responcse size
		File testFile = new File(System.getProperty("user.dir")+ "\\src\\test\\resources\\mini_data.xlsx");
		File dest = new File(System.getProperty("user.dir")+localFilePath);
		FileUtils.copyFile(testFile, dest);
		Thread.sleep(10000);
		
		final Response responseGet2 = resourceClient.resourcePath(allUploads).get(token);
		assertEquals(200, responseGet2.getStatus());
		final JsonArray uploads2 = JsonReader.readAsJsonArray(responseGet2.readEntity(String.class));
		assertEquals(5, uploads2.size());
		
		//check response size again
	
	}
	
	
	@After
	public void removeItems() {
		final File newFile = new File(System.getProperty("user.dir")+"\\fileUploads\\mini_data.xlsx");
		newFile.delete();
	}
	
	
}
