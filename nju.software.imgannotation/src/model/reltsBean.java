package model;

public class reltsBean {
	private String m_id = null;
	private String m_tsID = null;
	private String[] m_psID = null;
	private String[] m_plID = null;
	private String m_PSIDString = null;
	private String m_plIDString = null;
	private String m_time = null;
	private String m_authorName = null;
	private String m_authorID = null;
	private tasksetobj m_tsInfo = null;
	private String m_tsTittle = null;
	private String m_tsDesp = null;
	
	//use for the user operator information
	private String m_status = null;
	private String m_canSubmmitString = null;
	
	//图片操作数据
	private String m_content = null;                        //存储数据库中得到的taskset字符串
	
	public reltsBean() {
		m_tsInfo = new tasksetobj();
	}
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getM_tsID() {
		return m_tsID;
	}
	public void setM_tsID(String m_tsID) {
		this.m_tsID = m_tsID;
	}
	public String[] getM_psID() {
		return m_psID;
	}
	public void setM_psID(String[] m_psID) {
		this.m_psID = m_psID;
	}
	public String[] getM_plID() {
		return m_plID;
	}
	public void setM_plID(String[] m_plID) {
		this.m_plID = m_plID;
	}
	public String getM_PSIDString() {
		return m_PSIDString;
	}
	public void setM_PSIDString(String m_PSIDString) {
		this.m_PSIDString = m_PSIDString;
	}
	public String getM_plIDString() {
		return m_plIDString;
	}
	public void setM_plIDString(String m_plIDString) {
		this.m_plIDString = m_plIDString;
	}
	public String getM_time() {
		return m_time;
	}
	public void setM_time(String m_time) {
		this.m_time = m_time;
	}
	public String getM_authorName() {
		return m_authorName;
	}
	public void setM_authorName(String m_authorName) {
		this.m_authorName = m_authorName;
	}
	public String getM_authorID() {
		return m_authorID;
	}
	public void setM_authorID(String m_authorID) {
		this.m_authorID = m_authorID;
	}
	public String getM_status() {
		return m_status;
	}
	public void setM_status(String m_status) {
		this.m_status = m_status;
	}
	public tasksetobj getM_tsInfo() {
		return m_tsInfo;
	}
	public void setM_tsInfo(tasksetobj m_tsInfo) {
		this.m_tsInfo = m_tsInfo;
	}
	public String getM_content() {
		return m_content;
	}
	public void setM_content(String m_content) {
		this.m_content = m_content;
	}
	public String getM_tsTittle() {
		return m_tsTittle;
	}
	public void setM_tsTittle(String m_tsTittle) {
		this.m_tsTittle = m_tsTittle;
	}
	public String getM_tsDesp() {
		return m_tsDesp;
	}
	public void setM_tsDesp(String m_tsDesp) {
		this.m_tsDesp = m_tsDesp;
	}
	public String get_canSubmmit() {
		return m_canSubmmitString;
	}
	public void set_canSubmmit(String m_canSubmmitString) {
		this.m_canSubmmitString = m_canSubmmitString;
	}

}
