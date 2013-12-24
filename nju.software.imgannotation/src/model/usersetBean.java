package model;

import java.util.ArrayList;

public class usersetBean {
	private String m_id=null;
	private String m_time=null;
	private String m_authorID = null;
	private String m_authorName = null;
	private String m_tittle = null;
	private String m_desp = null;
	private String m_content = null;
	private ArrayList<userObj> m_UserList = null;
	
	public usersetBean(String id)
	{
		setUserList(new ArrayList<userObj>());
		setID(id);
	}

	public String getID() {
		return m_id;
	}

	public void setID(String m_id) {
		this.m_id = m_id;
	}

	public String getTime() {
		return m_time;
	}

	public void setTime(String m_time) {
		this.m_time = m_time;
	}

	public String getAuthorID() {
		return m_authorID;
	}

	public void setAuthorID(String m_authorID) {
		this.m_authorID = m_authorID;
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

	public String getDesp() {
		return m_desp;
	}

	public void setDesp(String m_desp) {
		this.m_desp = m_desp;
	}

	public String getContent() {
		return m_content;
	}

	public void setContent(String m_content) {
		this.m_content = m_content;
	}

	public ArrayList<userObj> getUserList() {
		return m_UserList;
	}

	public void setUserList(ArrayList<userObj> m_UserList) {
		this.m_UserList = m_UserList;
	}
}
