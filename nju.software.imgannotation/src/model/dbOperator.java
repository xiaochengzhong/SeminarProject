package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class dbOperator {
	//get the right of the database
	public static dbOperator getDbRight()
	{
		if(m_dbObj == null)
			m_dbObj = new dbOperator();
		return m_dbObj;
	}
	
	
	//get the user name list
	public ArrayList<userObj> getUserName(String sqlStatement)
	{
		ArrayList<userObj> userInfo = new ArrayList<userObj>();
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				userInfo.add(new userObj(m_reslut.getString(1), m_reslut.getString(2)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return userInfo;
	}
	
	//get the permisson data
	public String getPermissionCode(String sqlStatement)
	{
		String dataString = "0";
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				dataString = m_reslut.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return dataString;
	}
	
	//get the userInfo with user-id
	public userObj getUserInfo(String sqlStatement)
	{
		userObj mUser = null;
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				mUser = new userObj(m_reslut.getString(1), m_reslut.getString(2));
				mUser.setTSLsist(m_reslut.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mUser;
	}
	
	//get the user's task-set
	public ArrayList<reltsBean> getUserTSList(String sqlStatement)
	{
		ArrayList<reltsBean> mDataArrayList = null;
		openDB();
		try {
			mDataArrayList = new ArrayList<reltsBean>();
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				reltsBean mTSBean = new reltsBean();
				mTSBean.setM_id(m_reslut.getString(1));
				mTSBean.setM_status(m_reslut.getString(3));
				mTSBean.set_canSubmmit(m_reslut.getString(4));
				mDataArrayList.add(mTSBean);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mDataArrayList;
	}
	
	//get the picture information list
	public ArrayList<picObj> getpicInfo(String sqlStatement)
	{
		
		ArrayList<picObj> picInfo = new ArrayList<picObj>();
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				picInfo.add(new picObj(m_reslut.getString(1), m_reslut.getString(2) , m_reslut.getString(3)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return picInfo;
	}
	
	//get current picture index
	public String getCurrentPic(String sqlStatement)
	{
		String mData = null;
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				mData = m_reslut.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mData;
	}
	
	//insert new taskset into the database
	public boolean insetTSRecord(String sqlStatement)
	{
		boolean opSuccess = false;
		openDB();
		try {
			int tempCnt = m_statement.executeUpdate(sqlStatement);
			if(tempCnt == 1)
				opSuccess = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return opSuccess;
	}
	
	//update data to the database
	public boolean updateDB(String sqlStatement)
	{
		boolean opSuccess = false;
		openDB();
		try {
			int tempCnt = m_statement.executeUpdate(sqlStatement);
			if(tempCnt == 1)
				opSuccess = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return opSuccess;
	}
	
	//get the unrelease tasksets
	public ArrayList<tasksetobj> getUnRelTS(String sqlStatement)
	{
		ArrayList<tasksetobj> pData = new ArrayList<tasksetobj>();
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				tasksetobj mData = new tasksetobj();
				mData.setTSID(m_reslut.getString(1));
				mData.setTStime(m_reslut.getString(2).replace(".0", ""));
				mData.setTSAuthor(m_reslut.getString(3));
				mData.setTittle(m_reslut.getString(4));
				mData.setDescription(m_reslut.getString(5));
				mData.setContent(m_reslut.getString(6));
				pData.add(mData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return pData;
	}
	
	//get the taskset content with the taskset ID
	public tasksetobj getTSContentWithID(String sqlStatement)
	{
		tasksetobj pData = null;
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				pData = new tasksetobj();
				pData.setTSID(m_reslut.getString(1));
				pData.setTStime(m_reslut.getString(2).replace(".0", ""));
				pData.setTSAuthor(m_reslut.getString(3));
				pData.setTittle(m_reslut.getString(4));
				pData.setDescription(m_reslut.getString(5));
				pData.setContent(m_reslut.getString(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return pData;
	}
	
	//get all the release tasksets
	public ArrayList<tasksetobj> getAllRelTS(String sqlStatement)
	{
		ArrayList<tasksetobj> pData = new ArrayList<tasksetobj>();
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				tasksetobj mData = new tasksetobj();
				mData.setTSID(m_reslut.getString(1));
				mData.setTStime(m_reslut.getString(2).replace(".0", ""));
				mData.setTSAuthor(m_reslut.getString(3));
				mData.setTittle(m_reslut.getString(4));
				mData.setDescription(m_reslut.getString(5));
				mData.setContent(m_reslut.getString(6));
				pData.add(mData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return pData;
	}
	
	//get all the relts data
	public ArrayList<reltsBean> getRelTSDataInfo(String sqlStatement)
	{
		ArrayList<reltsBean> pData = new ArrayList<reltsBean>();
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				reltsBean mData = new reltsBean();
				mData.setM_tsID(m_reslut.getString(2));
				mData.setM_id(m_reslut.getString(1));
				mData.setM_PSIDString(m_reslut.getString(3));
				mData.setM_time(m_reslut.getString(4).replace(".0", ""));
				mData.setM_authorName(m_reslut.getString(5));
				mData.setM_tsTittle(m_reslut.getString(6));
				mData.setM_tsDesp(m_reslut.getString(7));
				pData.add(mData);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return pData;
	}
	
	//close the connect
	public void closeDB() {
		try {
			if(m_reslut != null)
				m_reslut.close();
			if(m_statement != null)
				m_statement.close();
			if(m_connect != null)
				m_connect.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//get the picture-set information with the picture-set id
	public ArrayList<String> getPSInfo(String sqlStatement)
	{
		ArrayList<String> mData = new ArrayList<String>();
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				mData.add(m_reslut.getString(1));   //name
				mData.add(m_reslut.getString(2));   //path
				mData.add(m_reslut.getString(3));   //id
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mData;
	}
	
	//get the all picture-set information
	public ArrayList<picSetObj> getAllPSInfo(String sqlStatement)
	{
		ArrayList<picSetObj> mData = new ArrayList<picSetObj>();
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				picSetObj mps = new picSetObj(m_reslut.getString(1), m_reslut.getString(3));
				mps.setdesp(m_reslut.getString(4));
				mps.setTime(m_reslut.getString(2).replace(".0", ""));
				mData.add(mps);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mData;
	}
	
	//insert new person task into the database
	public boolean insertPerTS(String sqlStatement)
	{
		return insertRecordToDB(sqlStatement);
	}
	
	// insert new picture-set into the database
	public boolean insetPSRecord(String sqlStatement) {
		return insertRecordToDB(sqlStatement);
	}
	
	//insert new taskset in to the database
	public boolean insertTSRecord(String sqlStatement) {
		return insertRecordToDB(sqlStatement);
	}
	
	//insert new releaseTaskset into the database
	public boolean insertRelTSRecord(String sqlStatement)
	{
		return insertRecordToDB(sqlStatement);
	}
	
	//insert a new picture operator data to db
	public boolean insertPicOpeRecord(String sqlStatement)
	{
		return insertRecordToDB(sqlStatement);
	}
	
	//insert a new person set to the database
	public boolean insertPerSetRecord(String sqlStatement)
	{
		return insertRecordToDB(sqlStatement);
	}
	
	//insert function operator
	public boolean insertRecordToDB(String sqlStatement) {
		boolean opSuccess = false;
		openDB();
		try {
			int tempCnt = m_statement.executeUpdate(sqlStatement);
			if (tempCnt == 1)
				opSuccess = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return opSuccess;
	}
	
	//update the picture-set with the picture-set id
	public boolean updatePSRecord(String sqlStatement)
	{
		return updateRecordToDB(sqlStatement);
	}
	
	//update the taskset with the taskset id
	public boolean updateTSRecord(String sqlStatement)
	{
		return updateRecordToDB(sqlStatement);
	}
	
	//update the user tasksetlist 
	public boolean updateUserTSList(String sqlStatement)
	{
		return updateRecordToDB(sqlStatement);
	}
	
	//update the picture operator of the db
	public boolean updatepicODofDB(String sqlStatement)
	{
		return updateDB(sqlStatement);
	}
	
	//update the person set of the db
	public boolean updateperSetToDB(String sqlStatement)
	{
		return updateDB(sqlStatement);
	}
	
	//update the current picture of the db
	public boolean updateperCurrentPic(String sqlStatement)
	{
		return updateDB(sqlStatement);
	}
	
	//update function
	public boolean updateRecordToDB(String sqlStatement)
	{
		boolean opSuccess = false;
		openDB();
		try {
			int tempCnt = m_statement.executeUpdate(sqlStatement);
			if (tempCnt == 1)
				opSuccess = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return opSuccess;
	}
	
	// get all the picture-set from the database
	public ArrayList<picSetObj> getAllPicSetInfo(String sqlStatement)
	{
		ArrayList<picSetObj> mData = new ArrayList<picSetObj>();
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				picSetObj mTemp = new picSetObj(m_reslut.getString(1), m_reslut.getString(4));
				mTemp.setTime(m_reslut.getString(2).replace(".0", ""));
				mTemp.setAuthorName(m_reslut.getString(3));
				mTemp.setdesp(m_reslut.getString(5));
				mData.add(mTemp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mData;
	}
	
	//get the picture-set information with the picture-set id
	public picSetObj getPSDeatilInfo(String sqlStatement) {
		picSetObj mData = null;
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				mData = new picSetObj(m_reslut.getString(1), m_reslut.getString(4));
				mData.setAuthorName(m_reslut.getString(3));
				mData.setTime(m_reslut.getString(2).replace(".0", ""));
				mData.setdesp(m_reslut.getString(5));
				mData.setContent(m_reslut.getString(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mData;
	}
	
	//get the relts-set information
	public reltsBean getRelTSInfo(String sqlStatement)
	{
		reltsBean mRelTS = null;
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				mRelTS = new reltsBean();
				mRelTS.setM_id(m_reslut.getString(1));
				mRelTS.setM_status(m_reslut.getString(2));
				mRelTS.setM_tsID(m_reslut.getString(3));
				mRelTS.setM_PSIDString(m_reslut.getString(4));
				mRelTS.setM_plIDString(m_reslut.getString(5));
				mRelTS.setM_time(m_reslut.getString(6).replace(".0", ""));
				mRelTS.setM_authorID(m_reslut.getString(7));
				mRelTS.setM_authorName(m_reslut.getString(8));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mRelTS;
	}
	
	//get the relts data from the database just one picture operator data
	public reltsBean getRelTSOpeDataFDB(String sqlStatement)
	{
		reltsBean mRelTS = null;
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				mRelTS = new reltsBean();
				mRelTS.setM_id(m_reslut.getString(1));
				mRelTS.setM_authorID(m_reslut.getString(2));
				mRelTS.setM_content(m_reslut.getString(5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mRelTS;
	}
	
	//get all the person-set from the db
	public ArrayList<usersetBean> getAllPerSet(String sqlStatement)
	{
		ArrayList<usersetBean> mData = null;
		openDB();
		try {
			mData = new ArrayList<usersetBean>();
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				usersetBean mTemp = new usersetBean(m_reslut.getString(1));
				mTemp.setTime(m_reslut.getString(2).replace(".0", ""));
				mTemp.setAuthorID(m_reslut.getString(3));
				mTemp.setAuthorName(m_reslut.getString(4));
				mTemp.setTittle(m_reslut.getString(5));
				mTemp.setDesp(m_reslut.getString(6));
				mTemp.setContent(m_reslut.getString(7));
				mData.add(mTemp);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return mData;
	}

	//get the person-set information frome the database
	public usersetBean getPerSet(String sqlStatement)
	{
		usersetBean muserset = null;
		openDB();
		try {
			m_reslut = m_statement.executeQuery(sqlStatement);
			while (m_reslut.next()) {
				muserset =  new usersetBean(m_reslut.getString(1));
				muserset.setTime(m_reslut.getString(2).replace(".0", ""));
				muserset.setAuthorID(m_reslut.getString(3));
				muserset.setAuthorName(m_reslut.getString(4));
				muserset.setTittle(m_reslut.getString(5));
				muserset.setDesp(m_reslut.getString(6));
				muserset.setContent(m_reslut.getString(7));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeDB();
		return muserset;
	}
	
	//open the connect
	public void openDB()
	{
		//后期可以修改为从配置文件读取
		String driver = "com.mysql.jdbc.Driver";                    //the driver name
    	String url = "jdbc:mysql://127.0.0.1:3306/imgannotation?useUnicode=true&characterEncoding=UTF-8";   //the name of the database which need visited
    	String user = "imgannotation";                                       //the name of the operator
    	String password = "helloimg93";                                 //password
    	
    	try {
    		Class.forName(driver);
			//if (m_connect.isClosed())
				m_connect = DriverManager.getConnection(url, user, password);
			//if (m_statement.isClosed())
				m_statement = m_connect.createStatement(); // use the statement to excute the sql
    	}
    	catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
	}
	
	
	private dbOperator() {
		// TODO Auto-generated constructor stub
	}
	
	private static dbOperator m_dbObj = null;
	private Connection m_connect = null;
	private Statement m_statement = null;
	private ResultSet m_reslut = null;

}
