package model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class picOperator {
	
	//get the information with picture id array
	public static ArrayList<picObj> getPicInfo(String[] picID)
	{
		//construct the sql statement
		String sqlState = "select id , path , name from picture where id=\"" +picID[0] + "\"";
		for(short i=1 ;i < picID.length ; ++i)
		{
			sqlState += " or id =\"" + picID[i] + "\"";
		}
		return dbOperator.getDbRight().getpicInfo(sqlState);
	}
	
	//get the information with picture id(just one picture)
	public static picObj getPicInfo(String picID)
	{
		String sqlState = "Select id , path , name from picture where id=\"" + picID + "\"";
		ArrayList<picObj> dataInfo = dbOperator.getDbRight().getpicInfo(sqlState);
		//deal the data
		if(dataInfo == null)
			return null;
		else
			return dataInfo.get(0);                       //just one record
	}
	
	//get the picture-set information from the DB with particular picture-set ID
	public static picSetObj getPicSetInfo(String picSetID)
	{
		String sqlState = "SELECT name , path , id FROM picset WHERE id=\"" + picSetID + "\"";
		ArrayList<String> mPSInfo = dbOperator.getDbRight().getPSInfo(sqlState);
		picSetObj mPSData = new picSetObj(mPSInfo.get(2), mPSInfo.get(0), mPSInfo.get(1));
		return mPSData;
	}
	
	//get all the picture-set information from the database
	public static ArrayList<picSetObj> getAllPSInfo()
	{
		String sqlState = "SELECT id , TIME , tittle , description FROM pictureset";
		return dbOperator.getDbRight().getAllPSInfo(sqlState);
	}
	
	//get all the picture-set information
	public static ArrayList<picSetObj> getPSAInfo()
	{
		String sqlStatement = "SELECT id , TIME , author_name , tittle , description FROM pictureset ORDER BY TIME DESC";
		return dbOperator.getDbRight().getAllPicSetInfo(sqlStatement);
	}
	
	//add new picture-set to the database
	public static boolean inNewPicset(picSetObj picset)
	{
		String sqlStatement = "INSERT INTO pictureset VALUES(\"" + picset.getID()
				+ "\" , \"" + picset.getTime() + "\" , \"" + picset.getAuthorName()
				+ "\" , \"" + picset.getTittle() + "\" , \"" + picset.getdesp() +"\" , \""+picset.getContent()+"\")";
		try {
			return dbOperator.getDbRight().insetPSRecord(new String((sqlStatement.getBytes("UTF-8")),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	//update the picture-set tittle/descrption/time/content
	public static boolean updateNewPicset(picSetObj picset)
	{
		String sqlStatement = "UPDATE pictureset SET tittle =\""+picset.getTittle()+"\" , description = \""+picset.getdesp()+"\" ,  TIME=\""+
				picset.getTime()+"\" , content=\""+picset.getContent()+"\" WHERE id=\""+picset.getID()+"\"";
		return dbOperator.getDbRight().updatePSRecord(sqlStatement);
	}
	
	//get picture-set information with the picture-set id
	public static picSetObj getPSInfo(String picID)
	{
		String sqlStatement = "SELECT *  FROM pictureset WHERE id = \""+ picID +"\"";
		picSetObj mpicset = dbOperator.getDbRight().getPSDeatilInfo(sqlStatement);
		if(mpicset == null)
			return mpicset;
		return mpicset;
	}
	
	//get picture-operator data from the database
	public static reltsBean getPicOpeDataFromDB(String relTSID , String picSetID , String picID , String userID)
	{
		String sqlStatement = "SELECT * FROM imgannotation WHERE reltsid='"+relTSID
				+"' AND userid='"+userID+"' AND picturesetID='"+picSetID+"' AND pictureID='"+picID+"'";
		return dbOperator.getDbRight().getRelTSOpeDataFDB(sqlStatement);
	}
	
	//add new picture operator data to the db
	public static boolean insertPicOpeDataToDB(String relTSID , String picSetID , String picID , String userID , String content)
	{
		String sqlStatement = "INSERT INTO imgannotation VALUES('"+relTSID+"' , '"+userID+"' , '"+
				picSetID+"' , '"+picID+"' , '"+content+"')";
		return dbOperator.getDbRight().insertPicOpeRecord(sqlStatement);
	}
	
	//update the picture operator data of the db
	public static boolean updatePicOpeDataOfDB(String relTSID , String picSetID , String picID , String userID , String content)
	{
		String sqlStatement = "UPDATE imgannotation SET content='"+content+"' WHERE reltsid='"+relTSID+"' AND userid='"+ userID+
				"' AND picturesetID='"+picSetID+"' AND pictureID='"+picID+"'";
		return dbOperator.getDbRight().updatepicODofDB(sqlStatement);
	}
}
