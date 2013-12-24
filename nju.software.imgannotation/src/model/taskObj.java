package model;

//define the object
public class taskObj {
	private String m_shapeName=null;
	private String m_shapeID = null;
	private int m_lineWid=1;
	private String m_lineCol=null;
	private boolean m_fill=false;
	private String m_fillCol=null;
	private String m_taskID = null;
	private String m_taskName = null;
	private String m_description = null;

	// constructor
	public taskObj()
	{
		
	}
	
	public taskObj(String taskID , String taskName , String shapeName, String shapeID)
	{
		init(taskID , taskName , shapeName , shapeID , 1 , "" , false , "");
	}
	

	// help for the constructor
	private void init(String taskID , String taskName , String shapeName, String shapeID , int lineWidth , String lineCol , 
			boolean fill , String fillCol) {
		m_taskID = taskID;
		m_taskName = taskName;
		m_shapeName = shapeName;
		m_shapeID = shapeID;
		m_lineWid = lineWidth;
		m_lineCol = lineCol;
		m_fill = fill;
		m_fillCol = fillCol;
	}
	
	public void setShapeName(String shapeName)
	{
		m_shapeName = shapeName;
	}
	public void setShapeID(String shapeID)
	{
		m_shapeID = shapeID;
	}
	public void setTaskID(String taskID)
	{
		m_taskID = taskID;
	}
	public void setTaskName(String taskName)
	{
		m_taskName = taskName;
	}
	public void setLineWidth(int lineWid)
	{
		m_lineWid = lineWid;
	}
	public void setLineCol(String col)
	{
		m_lineCol = col;
	}
	public void setFill(boolean fill)
	{
		m_fill = fill;
	}
	public void setFillCol(String fillCol)
	{
		m_fillCol = fillCol;
	}

	// use to get the data
	public String getShapeName()
	{
		return m_shapeName;
	}
	public String getShapeID()
	{
		return m_shapeID;
	}
	public String getTaskID()
	{
		return m_taskID;
	}
	public String getTaskName()
	{
		return m_taskName;
	}
	public int getLineWidth()
	{
		return m_lineWid;
	}
	public String getLineCol()
	{
		return m_lineCol;
	}
	public boolean getFill()
	{
		return m_fill;
	}
	public String getFillCol()
	{
		return m_fillCol;
	}

	public String getDescription() {
		return m_description;
	}

	public void setDescription(String m_description) {
		this.m_description = m_description;
	}

}
