package serverAndClient;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author rao (based on auto-generated file by REST Compile.)
 */
public class RestClient {

    public String DoRestCall(String requestUri, String httpMethod, String postdata) {
        //


        String response = "";
        try {
            // connect to the web service via HTTP GET
            URL url = new URL(requestUri);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
               
            
            connection.setRequestMethod(httpMethod);

            if ("POST".equals(httpMethod)) {

                connection.setDoOutput(true);
                
                connection.setRequestProperty("Content-Type", "application/xml"); 

                OutputStreamWriter outputStream =
                        new OutputStreamWriter(connection.getOutputStream());
                
                outputStream.write(postdata);
                
                outputStream.flush();
            }

            connection.connect();

            response = ReceiveResponse(connection);

            connection.disconnect();

        } catch (IOException e) {
            System.err.println(e.toString());
        }
        return response;


    }

   
    
    
    private String ReceiveResponse(HttpURLConnection connection) throws IOException {
        String response = "";
        try {
            InputStream inputStream = connection.getInputStream();
            // read the contents
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                for (int i = 0; i < length; i++) {
                    response += (char) buffer[i];
                }
            }
            inputStream.close();
        } catch (IOException e) {
            int responseCode = connection.getResponseCode();
            switch (responseCode) {
            }
        }
        return response;
    }
}
