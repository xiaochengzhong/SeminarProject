package model;

import java.util.ArrayList;

//define the taskset object
public class tasksetobj {
	private String m_taskSetID = null;
	private String m_taskSettime = null;
	private String m_author = null;
	private String m_authorID = null;
	private ArrayList<taskObj> m_task = null;
	private String m_content = null;                        //存储数据库中得到的taskset字符串
	private String m_status = null;                         //主要用于用户以及管理员查看任务使用
	private String m_tittle = null;
	private String m_description = null;
	
	public String getauthorID() {
		return m_authorID;
	}

	public String getstatus() {
		return m_status;
	}

	public void setstatus(String m_status) {
		this.m_status = m_status;
	}

	public void setauthorID(String m_authorID) {
		this.m_authorID = m_authorID;
	}
	
	public tasksetobj()
	{
		m_task = new ArrayList<taskObj>();
	}
	
	//set the data
	public void setTSID(String taskSetID)
	{
		m_taskSetID = taskSetID;
	}
	
	public void setTStime(String time)
	{
		m_taskSettime = time;
	}
	
	public void setTSAuthor(String author)
	{
		m_author = author;
	}
	
	public void addTask(taskObj newTask)
	{
		m_task.add(newTask);
	}
	
	//get the data
	public String getTSID()
	{
		return m_taskSetID;
	}
	
	public String getTSTime()
	{
		return m_taskSettime;
	}
	
	public String getTSAuthor()
	{
		return m_author;
	}
	
	public ArrayList<taskObj> getTSTask()
	{
		return m_task;
	}

	public String getContent() {
		return m_content;
	}

	public void setContent(String m_content) {
		this.m_content = m_content;
	}

	public String getTittle() {
		return m_tittle;
	}

	public void setTittle(String m_tittle) {
		this.m_tittle = m_tittle;
	}

	public String getDescription() {
		return m_description;
	}

	public void setDescription(String m_description) {
		this.m_description = m_description;
	}
	
}
