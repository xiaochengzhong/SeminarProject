package model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class tasksetOperator {
	
	//add new task-set to the database
	public static boolean addNewTSToDB(String mContent , String mTittle ,String mDesp , String[] adminInfo)
	{
		String sqlStatement = "INSERT INTO taskset VALUES(\"TS"+stringOperator.timeFormat()+adminInfo[0]
				+ "\" , \"" + stringOperator.timeFormatT() + "\" , \"" + adminInfo[1]
				+ "\" , \"" + mTittle + "\" , \"" + mDesp +"\" , \""+mContent+"\")";
		try {
			return dbOperator.getDbRight().insertTSRecord(new String((sqlStatement.getBytes("UTF-8")),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**获取为发布的任务集
	 * @return： 未发布的taskset的ID，以及该任务集的发布者名字
	 */
	public static ArrayList<tasksetobj> getUnReleaseTS()
	{
		ArrayList<tasksetobj> pData=null;
		String sql = "SELECT * FROM taskset WHERE id NOT IN (SELECT tsid FROM reltaskset) ORDER BY TIME DESC";
		pData = dbOperator.getDbRight().getUnRelTS(sql);
		return pData;
	}
	
	//得到所有已发布的reltaskset 中得taskset的内容
	public static ArrayList<tasksetobj> getAllReleaseTS()
	{
		String sqlStatement = "SELECT * FROM taskset WHERE id  IN (SELECT tsid FROM reltaskset) ORDER BY TIME DESC";
		return dbOperator.getDbRight().getAllRelTS(sqlStatement);
	}
	
	//得到reltaskset的相关信息
	public static ArrayList<reltsBean> getAllReleaeTSData()
	{
		String sqlStatement = "SELECT DISTINCT(b.id), taskset.id ,  b.psid , b.time , b.author_name, taskset.tittle , taskset.description FROM taskset , reltaskset b WHERE taskset.id  IN (SELECT tsid FROM reltaskset a WHERE a.id = b.id) ORDER BY b.time DESC";
		return dbOperator.getDbRight().getRelTSDataInfo(sqlStatement);
	}
	
	//通过给定tasksetId得到taskset的配置内容
	public static tasksetobj getTSInfo(String tasksetID)
	{
		String sqlstatement = "SELECT * FROM taskset WHERE id = \""+tasksetID+"\"";
		return dbOperator.getDbRight().getTSContentWithID(sqlstatement);
	}
	
	//update the task-set tittle/descrption/time/content
	public static boolean updateNewTaskset(String mContent , String mtittle , String mDesp , String tsID)
	{
		String sqlStatement = "UPDATE taskset SET tittle =\""+mtittle+"\" , description = \""+mDesp+"\" ,  time=\""+
				stringOperator.timeFormatT() +"\" , content=\""+mContent+"\" WHERE id=\""+tsID+"\"";
		return dbOperator.getDbRight().updateTSRecord(sqlStatement);
	}
	
	//发布任务集
	public static boolean addNewRelTaskSet(String tasksetID,String picsetID, String persID , String[] adminInfo , String relTSID)
	{
		String sqlStatement = "INSERT INTO reltaskset VALUES(\""+ relTSID +"\" , 0 , "
				+ "\""+tasksetID+"\" , \""+ picsetID +"\" , \""+ persID +"\" , " +
				" \""+ stringOperator.timeFormatT() +" \" , \""+adminInfo[0]+"\" , \""+adminInfo[1]+"\" )";
		return dbOperator.getDbRight().insertRelTSRecord(sqlStatement);
	}
	
	//得到release tasket的所有信息
	public static reltsBean getRelTSInfo(String relTSID)
	{
		String sqlStatement = "SELECT * FROM reltaskset WHERE id=\""+ relTSID +"\"";
		return dbOperator.getDbRight().getRelTSInfo(sqlStatement);
	}
}
