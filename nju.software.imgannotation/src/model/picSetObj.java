package model;

import java.util.ArrayList;

public class picSetObj {
	private String m_id = null;
	private String m_path = null;
	private String m_name = null;
	private ArrayList<picObj> m_picList = null;             //主要用于用户图画操作以及管理员端的操作再现工作
	private String m_authorName = null;
	private String m_tittle = null;
	private String m_desp = null;
	private String m_time = null;
	private String m_content = null;
	private ArrayList<String> m_picPath = null;
	
	public picSetObj(String id , String name , String path)
	{
		m_id = id;
		m_path = path;
		m_name = name;
		m_picList = new ArrayList<picObj>();
	}
	
	public picSetObj(String id , String tittle)
	{
		this.m_id = id;
		this.m_tittle =tittle;
		m_picPath = new ArrayList<String>();
	}
	
	public void addPicsetPath(String path)
	{
		m_picPath.add(path);
	}
	public ArrayList<String> getPicPath()
	{
		return m_picPath;
	}
	//get function
	public String getID() {return m_id;}
	public String getPath() {return m_path;}
	public String getName() {return m_name;}
	public ArrayList<picObj> getPicList() {return m_picList;}
	
	//set the function
	public void setID(String id) {m_id = id;}
	public void setName(String name) {m_name =name;}
	public void setPath(String path) {m_path = path;}
	
	//add the picture to the picture arraylist
	public void addPicture(picObj newpic)
	{
		m_picList.add(newpic);
	}
	public String getAuthorName() {
		return m_authorName;
	}
	public void setAuthorName(String m_authorName) {
		this.m_authorName = m_authorName;
	}
	public String getTittle() {
		return m_tittle;
	}
	public void setTittle(String m_tittle) {
		this.m_tittle = m_tittle;
	}
	public String getdesp() {
		return m_desp;
	}
	public void setdesp(String m_desp) {
		this.m_desp = m_desp;
	}
	public String getTime() {
		return m_time;
	}
	public void setTime(String m_time) {
		this.m_time = m_time;
	}
	public String getContent() {
		return m_content;
	}
	public void setContent(String content) {
		this.m_content = content;
	}
}
