package model;

public class shapeBean {
	private String taskID=null;
	private String shapeID=null;
	private int lineWidth=0;
	private String lineColor="";
	private String Data=null;
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
	public String getShapeID() {
		return shapeID;
	}
	public void setShapeID(String shapeID) {
		this.shapeID = shapeID;
	}
	public int getLineWidth() {
		return lineWidth;
	}
	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}
	public String getData() {
		return Data;
	}
	public void setData(String data) {
		Data = data;
	}
	public String getLineColor() {
		return lineColor;
	}
	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}
	
}
