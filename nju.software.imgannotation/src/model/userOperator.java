package model;

import java.util.ArrayList;

public class userOperator {
	/**
	 * get the user information
	 * @param userID 用户的ID String数组
	 * @return 由ArrayList构成的userObj数组
	 */
	public static ArrayList<userObj> getUserName(String[] userID)
	{
		//construct the sql statement
		String sqlState = "select id , name from user where id='" +userID[0] + "'";
		for(short i=1 ;i < userID.length ; ++i)
		{
			sqlState += " or id ='" + userID[i] + "'";
		}
		return dbOperator.getDbRight().getUserName(sqlState);
	}
	
	/**
	 * get the user information which not in the person-set
	 * @param userID 用户的ID String数组
	 * @return 由ArrayList构成的userObj数组
	 */
	public static ArrayList<userObj> getLeaveUseName(String[] userID)
	{
		String sqlStatement = "SELECT id , name FROM user WHERE id NOT IN (SELECT id FROM USER WHERE id='" +userID[0] + "'";
		for(short i=0; i < userID.length; ++i)
		{
			sqlStatement += " or id='"+ userID[i] + "'";
		}
		sqlStatement += ")";
		return dbOperator.getDbRight().getUserName(sqlStatement);
	}
	
	/**
	 * get all the user information
	 * @return 由ArrayList构成的userObj数组
	 */
	public static ArrayList<userObj> getAllUserInfo()
	{
		String sqlState = "SELECT id , name FROM user";
		return dbOperator.getDbRight().getUserName(sqlState);
	}
	
	/**
	 * 得到指定用户的taskset的集合
	 * @param userID 用户的ID
	 * @return 用户的useObj详细信息
	 */
	public static userObj getUserTSList(String userID)
	{
		String sqlStatement = "SELECT * FROM USER WHERE id=\"" + userID +"\"";
		return dbOperator.getDbRight().getUserInfo(sqlStatement);
	}
	
	/**
	 * 更新指定用户的tasksetlist的内容
	 * @param mUser 用户的信息信息
	 * @return 操作是否成功
	 */
	public static boolean updateUserTSList(userObj mUser)
	{
		String sqlStatement = "UPDATE USER SET tasksetlist='" + mUser.getTSLsist() +"' WHERE id=\"" + mUser.getID() +"\"";
		return dbOperator.getDbRight().updateUserTSList(sqlStatement);
	}
	
	/**
	 * create a new person-set to the database
	 * @param tittle 用户集的标题
	 * @param description 用户集的描述
	 * @param userIDList 用户列表（String）
	 * @param adminInfo 生成该person-set的用户信息
	 * @return 操作是否成功
	 */
	public static boolean addNewPerSetToDB(String tittle , String description , String userIDList , String[] adminInfo)
	{
		String sqlStatement = "INSERT INTO personset VALUES('PES"+stringOperator.timeFormat()+adminInfo[0]+"' , "
				+"'"+stringOperator.timeFormatT()+"' , '"+adminInfo[0]+"' , '"+adminInfo[1]+"' ,'"+tittle+"' , '"
				+description+"' , '"+userIDList+"')";
		return dbOperator.getDbRight().insertPerSetRecord(sqlStatement);
	}
	
	/**
	 * update the person-set with the ID
	 * @param tittle 新的用户集的标题
	 * @param description 新的用户集的描述
	 * @param content 新的用户列表（String）
	 * @param persetID 所要更新的用户集ID
	 * @return 操作是否成功
	 */
	public static boolean updatePerSetFDB(String tittle , String description, String content , String persetID)
	{
		String sqlStatement = "UPDATE personset SET time='"+stringOperator.timeFormatT()+"' , tittle='"+tittle+"', description='"+description+"' , content='"+content+"'  WHERE id='"+persetID+"'";
		return dbOperator.getDbRight().updateperSetToDB(sqlStatement);
	}

	/**
	 * get all the person-set from the database
	 * @return 所有人员集构成的ArrayList的usersetBean
	 */
	public static ArrayList<usersetBean> getAllPersonList()
	{
		String sqlStatement = "SELECT * FROM personset ORDER BY TIME DESC";
		return dbOperator.getDbRight().getAllPerSet(sqlStatement);
	}
	
	/**
	 * get the person-set information from the database
	 * @param persetID 用户集ID
	 * @return 用户集的详细信息
	 */
	public static usersetBean getPerSerInfoWithID(String persetID)
	{
		String sqlStatement = "SELECT * FROM personset WHERE id = '"+persetID+"'";
		return dbOperator.getDbRight().getPerSet(sqlStatement);
	}
	
	//to judgment the user permission
	public static boolean judgePermission(String userID)
	{
		boolean judgeData = false;
		String sqlStatement = "SELECT permission FROM USER WHERE id='"+ userID+"'";
		judgeData = Integer.valueOf(dbOperator.getDbRight().getPermissionCode(sqlStatement)) > 0 ? true : false ;
		return judgeData;
	}
	
	//得到用户的任务列表
	public static ArrayList<reltsBean> getrelTSList(String userID)
	{
		String sqlStatement = "SELECT * FROM usertasks WHERE userID = '"+userID+"'";
		return dbOperator.getDbRight().getUserTSList(sqlStatement);
	}
	
	//向数据库中添加用户任务
	public static boolean insertTSFUser(String userID , String relTSID)
	{
		String sqlStatement = "INSERT INTO usertasks VALUES('"+relTSID+"' , '"+
				userID +"' , 0 , 0 , '0')";
		return dbOperator.getDbRight().insertPerTS(sqlStatement);
	}
	
	//更新用户当前的图片的索引
	public static boolean updateCurentPic(String userID , String relTSID , String picID)
	{
		String sqlStatement = "UPDATE usertasks SET currentpic = '"+picID+"' WHERE reltsid='"+relTSID+"' AND userid='"+userID+"'";
		return dbOperator.getDbRight().updateperCurrentPic(sqlStatement);
	}
	
	//得到用户当前的图片索引
	public static String getCurrentPic(String userID , String relTSID)
	{
		String sqlStatement = "SELECT currentpic FROM usertasks WHERE reltsid='"+relTSID+"' AND userid='"+userID+"'";
		return dbOperator.getDbRight().getCurrentPic(sqlStatement);
	}
	
	//允许用户提交指定的任务集
	public static void updateTSofUser(String userID , String relTSID , String picID)
	{
		String sqlStatement = "UPDATE usertasks SET cansubmit=1 , currentpic = '"+picID+"' WHERE reltsid='"+relTSID+"' AND userid='"+userID+"'";
		dbOperator.getDbRight().updateperCurrentPic(sqlStatement);
	}
	
	//更改用户用户任务的状态（提交任务）
	public static boolean updateTSStatus(String userID , String relTSID )
	{
		String sqlStatement = "UPDATE usertasks SET tsstatus=1 WHERE reltsid='"+relTSID+"' AND userid='"+userID+"'";
		return dbOperator.getDbRight().updateperCurrentPic(sqlStatement);
	}
}
