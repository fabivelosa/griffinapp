package com.callfailures.connectionutils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test {
	
	public static void main(String args []) {
		
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  String sqlInsert = "insert into eventdb.event (cellId, dateTime, duration, causeCode, eventCauseId, failureClass, hier321Id, hier32Id, "
					+ " hier3Id, imsi, countryCode, operatorCode, neVersion, tac)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		  try {
			PreparedStatement pstmt = connection.prepareStatement(sqlInsert);
			pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
