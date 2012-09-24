package dk.itu.smds.e2012.webservices.basic.server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;


@WebService(name="helloService", targetNamespace = "http://smds-e2012.itu.dk/webservices/basicsample",serviceName="helloService")
public class HelloService 
{
	@WebMethod()
	public String helloOperation(String name) {
		return "Hello " + name + "!";
	}
	/**
	* Publish the service end point.
	* @param args not used.
	*/
	public static void main(String[] args) 
	{
		Endpoint.publish("http://localhost:8085/labexercises/hello", new HelloService());
	}
}
