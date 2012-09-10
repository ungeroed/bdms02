/**
 * 
 */
package xml;

import java.io.Serializable;

import javax.xml.bind.annotation.*;
/**
 * @author Yndal
 *
 */
@XmlRootElement(name = "task")
public class Task implements Serializable {

	@XmlAttribute
	public String id;
	
	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String date;
	
	@XmlAttribute
	public String status;
	
//	@XmlElement
	public String description;
	
//	@XmlElement
	public String attendant;

}
