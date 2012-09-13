/**
 * 
 */
package xml;

import java.util.*;
import java.io.*;
import javax.xml.bind.annotation.*;
/**
 * @author Yndal
 *
 */
@XmlRootElement(name = "cal")
public class Cal implements Serializable {
	
	@XmlElementWrapper(name = "tasks")
	@XmlElement(name = "task")
	public ArrayList<Task> tasks;
	
	
	@XmlElementWrapper(name = "users")
	@XmlElement(name = "user")
	public ArrayList<User> users;
	
	@Override
	public String toString(){
		
		return "Tasks: " + tasks.size() + "\nUsers: " + users.size();
			
	}
}
