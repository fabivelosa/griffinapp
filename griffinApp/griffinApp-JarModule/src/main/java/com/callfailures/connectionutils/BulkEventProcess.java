package com.callfailures.connectionutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;

import com.callfailures.entity.Events;

@Stateless
@Asynchronous
public class BulkEventProcess {

	/**
	 * Stores the Events object to the database
	 * 
	 * @param event - the object to be persisted
	 */
	public static void createBulk(final List<Events> events) {

		Connection connection = null;
		try {
			connection = com.callfailures.connectionutils.ConnectionHelper.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String sqlInsert = "insert into eventdb.event (cellId, dateTime, duration, causeCode, eventCauseId, failureClass, hier321Id, hier32Id, "
				+ " hier3Id, imsi, countryCode, operatorCode, neVersion, tac)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = null;
		int index = 0;

		try {
			pstmt = connection.prepareStatement(sqlInsert);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (final Events event : events) {
			try {

				LocalDateTime out = event.getDateTime().atZone(ZoneId.systemDefault()).toLocalDateTime();
				pstmt.setInt(1, event.getCellId());
				pstmt.setDate(2, java.sql.Date.valueOf(out.toLocalDate()));
				pstmt.setInt(3, event.getDuration());
				pstmt.setInt(4, event.getEventCause().getEventCauseId().getCauseCode());
				pstmt.setInt(5, event.getEventCause().getEventCauseId().getEventCauseId());
				pstmt.setInt(6, event.getFailureClass().getFailureClass());
				pstmt.setString(7, event.getHier321Id());
				pstmt.setString(8, event.getHier32Id());
				pstmt.setString(9, event.getHier3Id());
				pstmt.setString(10, event.getImsi());
				pstmt.setInt(11, event.getMarketOperator().getMarketOperatorId().getCountryCode());
				pstmt.setInt(12, event.getMarketOperator().getMarketOperatorId().getOperatorCode());
				pstmt.setString(13, event.getNeVersion());
				pstmt.setInt(14, event.getUeType().getTac());
				pstmt.addBatch();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		try {
			pstmt.executeBatch();
			connection.commit();
			pstmt.close();
			connection.setAutoCommit(true);
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
