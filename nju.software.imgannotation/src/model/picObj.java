package model;

import java.util.ArrayList;

public class picObj {
	private String m_id = null;
	private String m_path = null;
	private String m_name = null;
	private String m_status = null;                         //主要用于用户以及管理员查看任务使用
	private String m_PSID = null;                           // 图像集的Id号
	private ArrayList<taskObj> m_taskOp = null;             //主要用于用户图画操作以及管理员端的操作再现工作
	private ArrayList<shapeBean> m_shape = null;                       //用于显示一副图片中用户的操作
	private String width = "-1";                       //用于还原图像数据时使用
	private String height = "-1";                       //用于还原图像数据时使用
	
	//用于构造用户的任务图像列表
	public picObj(String id , String path)
	{
		m_id = id;
		m_path = path;
		m_taskOp = new ArrayList<taskObj>();
	}
	
	public picObj(String id , String path , String name)
	{
		m_id = id;
		m_path = path;
		m_name = name;
		m_taskOp = new ArrayList<taskObj>();
	}
	
	//set function
	public void setID(String id)
	{
		m_id = id;
	}
	public void setPath(String path)
	{
		m_path = path;
	}
	public void setName(String name)
	{
		m_name = name;
	}
	public void setStatus(String status)
	{
		m_status = status;
	}
	public boolean addTaskObj(taskObj newTask)
	{
		return m_taskOp.add(newTask);
	}
	
	//get function
	public String getID()
	{
		return m_id;
	}
	public String getPath()
	{
		return m_path;
	}
	public String getName()
	{
		return m_name;
	}
	public String getStatus()
	{
		return m_status;
	}
	public ArrayList<taskObj> getPicTask()
	{
		return m_taskOp;
	}

	public String getPSID() {
		return m_PSID;
	}

	public void setPSID(String m_PSID) {
		this.m_PSID = m_PSID;
	}

	public ArrayList<shapeBean> getShapeList() {
		return m_shape;
	}

	public void setShapeList(ArrayList<shapeBean> m_shape) {
		this.m_shape = m_shape;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

}
