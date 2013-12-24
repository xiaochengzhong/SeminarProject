package model;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class xmlOperator {
	
	public static xmlOperator xmlOpeRight()
	{
		if(m_xmlOpe == null)
			m_xmlOpe = new xmlOperator();
		return m_xmlOpe;
	}
	
	public static String dealString(String str)
	{
		str = str.replaceAll("\n\r","").replaceAll("\n","");
		String regex = "(.*\\S+)(\\s+$)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.matches()) {
			str = m.group(1);
		}
		return str;
	}
	
	/*Beacase use the Document create the file could not convenient
	 * we just think the filedata has been the content of the file , so just create a new file in the path
	 * filePath : the path which put the file
	*/
	public boolean createXmlFile(String filePath , String fileData)
	{
		fileData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><INFORMATION>" + fileData + "</INFORMATION>";
		return fileOperator.saveFileWithStream(filePath, fileData);
	}
	
	//constructor
	private xmlOperator()
	{
		
	}
	
	
	/*use to operator the disk files
	 * fileName: point the path where the file is
	 * doc: the xml data
	 */
	private boolean saveXml(String filePath, Document doc)
	{
		TransformerFactory transFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("indent", "yes");

			DOMSource source = new DOMSource();
			source.setNode(doc);
			StreamResult result = new StreamResult();
			result.setOutputStream(new FileOutputStream(filePath));

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (TransformerException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private static xmlOperator m_xmlOpe;

}
