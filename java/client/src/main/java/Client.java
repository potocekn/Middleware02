import java.io.InputStream;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import com.google.protobuf.Message;
import com.google.protobuf.AbstractMessage.Builder;
import com.google.protobuf.InvalidProtocolBufferException;

import example.Example.TimeMessage;

public class Client {	
	
    public static void main (String [] args) {

        // Create a socket object and connect it to the server.
        try (Socket server_socket = new Socket (Shared.SERVER_ADDR, Shared.SERVER_PORT)) {

            System.out.println ("Established outgoing connection.");

            try (
                // Wrap the socket streams in appropriate readers and writers.
                InputStream input = server_socket.getInputStream ();
                OutputStream output = server_socket.getOutputStream ();
                InputStreamReader reader = new InputStreamReader (input);
                BufferedReader buffered = new BufferedReader (reader);
                PrintWriter writer = new PrintWriter (output, true);
            ) {
                // Just send something and recieve current date.
            	TimeMessage msg =  TimeMessage.getDefaultInstance ();
            	/**/Builder builder = msg.newBuilder();
            	
            	if (true)
            	{
            		msg = ((example.Example.TimeMessage.Builder) builder).setDay(-1).build(); 
            	}
            	
            	if (true)
            	{
            		msg = ((example.Example.TimeMessage.Builder) builder).setMonth(-1).build(); 
            	}   
            	
            	if (true)
            	{
            		msg = ((example.Example.TimeMessage.Builder) builder).setYear(-1).build(); 
            	} 
            	
            	if (true)
            	{
            		msg = ((example.Example.TimeMessage.Builder) builder).setHours(-1).build(); 
            	} 
            	
            	if (true)
            	{
            		msg = ((example.Example.TimeMessage.Builder) builder).setMinutes(-1).build(); 
            	} 
            	
            	if (true)
            	{
            		msg = ((example.Example.TimeMessage.Builder) builder).setSeconds(-1).build(); 
            	} 
            	/**/
            	DataOutputStream dOut = new DataOutputStream(server_socket.getOutputStream());
            	dOut.writeInt(msg.toByteArray().length); // write length of the message
            	dOut.write(msg.toByteArray());  
            	//System.out.println(msg.toByteArray().length);
                //writer.println(msg.toByteArray());                
            	DataInputStream dIn = new DataInputStream(server_socket.getInputStream());
            	int length = dIn.readInt();   
        		byte[] response;
        		if(length>0) {
        			response = new byte[length];
        		    dIn.readFully(response, 0, response.length); // read the message
        		    TimeMessage timeMsg = TimeMessage.parseFrom(response);
                    System.out.printf("Day: %d\nMonth: %d\nYear: %d\nHours: %d\nMinutes: %d\nSeconds: %d\n", timeMsg.getDay(), timeMsg.getMonth(), timeMsg.getYear(), timeMsg.getHours(), timeMsg.getMinutes(), timeMsg.getSeconds());
        		}
                //String recieved = buffered.readLine();               
               //System.out.printf("Day: %d\nMonth: %d\nYear: %d\nHours: %d\nMinutes: %d\nSeconds: %d\n", msg.getDay(), msg.getMonth(), msg.getYear(), msg.getHours(), msg.getMinutes(), msg.getSeconds());
            }
        }
        catch (Exception e) {
            System.out.println (e);
        }
    }
}
