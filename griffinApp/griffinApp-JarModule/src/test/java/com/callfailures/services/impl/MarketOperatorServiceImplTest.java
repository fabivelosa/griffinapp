package com.callfailures.services.impl;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;
import com.callfailures.dao.MarketOperatorDAO;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.ValidationService;

@RunWith(MockitoJUnitRunner.class)
public class MarketOperatorServiceImplTest {

	private final MarketOperatorDAO marketOperatorDAO = mock(MarketOperatorDAO.class);
	private final ValidationService validationService = mock(ValidationService.class);
	private MarketOperator marketOperator;
	private MarketOperatorPK marketOperatorPK;
	private final String absolutePath = Paths.get("src", "test", "resources").toFile().getAbsolutePath();

	@InjectMocks
	private MarketOperatorServiceImpl marketOperatorServiceImpl;

	@Before
	public void setUp() throws Exception {
		marketOperatorPK = new MarketOperatorPK();
		marketOperator = new MarketOperator();	
	}

	@Test
	public void testSuccessForFindById() {
		marketOperatorPK.setCountryCode(28);
		marketOperatorPK.setOperatorCode(2);
		marketOperator.setCountryDesc("Antigua");
		marketOperator.setOperatorDesc("xxxx");
		marketOperator.setMarketOperatorId(marketOperatorPK);
		when(marketOperatorDAO.getMarketOperator(marketOperatorPK)).thenReturn(marketOperator);
		final MarketOperator marketOperatorObj = marketOperatorServiceImpl.findById(marketOperatorPK);
		verify(marketOperatorDAO, new Times(1)).getMarketOperator(marketOperatorPK);
		assertEquals(marketOperatorPK, marketOperatorObj.getMarketOperatorId());
	}

	@Test
	public void testFailureForFindById() {
		when(marketOperatorDAO.getMarketOperator(marketOperatorPK)).thenReturn(marketOperator);
		assertNotEquals(marketOperator, marketOperatorServiceImpl.findById(new MarketOperatorPK(1,1)));
	}

	@Test
	public void testSuccessForCreate() {
		Mockito.doNothing().when(marketOperatorDAO).create(marketOperator);
		marketOperatorServiceImpl.create(marketOperator);
		verify(marketOperatorDAO, new Times(1)).create(marketOperator);

	}

	@Test
	public void testSuccessForRead() {
		final File workbookFile = new File(absolutePath + "/marketOperatorService/validData.xlsx");
		Mockito.doNothing().when(marketOperatorDAO).create(any(MarketOperator.class));
		when(validationService.checkExistingMarketOperator(any(MarketOperator.class))).thenReturn(null);
		final ParsingResponse<MarketOperator> parseResult = marketOperatorServiceImpl.read(workbookFile);
		final Collection<MarketOperator> validObjects = parseResult.getValidObjects();
		assertFalse(validObjects.isEmpty());
		final MarketOperator marketOperator = validObjects.iterator().next();
		assertEquals("Denmark", marketOperator.getCountryDesc());
		assertEquals("TDC-DK", marketOperator.getOperatorDesc());
		assertEquals(238, marketOperator.getMarketOperatorId().getCountryCode());
		assertEquals(1, marketOperator.getMarketOperatorId().getOperatorCode());

	}

	@Test
	public void testFailureForRead() {
		final File workbookFile = new File(absolutePath + "/marketOperatorService/invalidData.xlsx");
		Mockito.doThrow(Exception.class).when(marketOperatorDAO).create(any(MarketOperator.class));
		final ParsingResponse<MarketOperator> parseResult = marketOperatorServiceImpl.read(workbookFile);
		final Collection<InvalidRow> invalidRows = parseResult.getInvalidRows();
		assertEquals(false, invalidRows.isEmpty());
	}

}
