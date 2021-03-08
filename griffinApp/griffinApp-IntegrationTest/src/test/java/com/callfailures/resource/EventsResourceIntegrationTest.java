package com.callfailures.resource;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;
import org.junit.runners.MethodSorters;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.callfailures.commontests.utils.ResourceClient;
import com.callfailures.commontests.utils.ResourceDefinitions;
import com.callfailures.dao.FailureClassDAO;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;


@RunWith(Arquillian.class)
public class EventsResourceIntegrationTest {

	@ArquillianResource
	private URL url; 

	private ResourceClient resourceClient;

	private static final String PATH_RESOURCE = ResourceDefinitions.YOGA_CLASSES.getResourceName();

	@Deployment
	public static Archive<?> createDeployment() {
		final JavaArchive testJar = ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addPackages(true, "com.callfailures.griffinApp-JarModule")
				.addAsResource("persistence-integration.xml", "META-INF/persistence.xml");
		
		final WebArchive testWar =  ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addPackages(true, "com.callfailures.griffinApp-WarModule")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.setWebXML(new File("src/test/resources/web.xml"));
		
        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class)
                .setApplicationXML("application.xml")
                .addAsModule(testJar)
                .addAsModule(testWar);
        
        return testWar;
	}	  
	
	@Before
	public void initTestCase() {
		this.resourceClient = new ResourceClient(url);
	}

	@Test
	@RunAsClient
	public void test() {
		System.out.println("hello");
	}
}