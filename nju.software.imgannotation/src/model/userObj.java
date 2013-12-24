package model;

public class userObj {
	private String m_id=null;
	private String m_name=null;
	private String m_tsLsist = null;
	
	public userObj(String id , String name)
	{
		m_id = id;
		m_name = name;
	}
	
	//get data function
	public String getID()
	{
		return m_id;
	}
	
	public String getName()
	{
		return m_name;
	}

	public String getTSLsist() {
		return m_tsLsist;
	}

	public void setTSLsist(String m_tsLsist) {
		this.m_tsLsist = m_tsLsist;
	}

}
