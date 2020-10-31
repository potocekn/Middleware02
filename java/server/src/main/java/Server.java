import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.time.*;
import java.time.format.DateTimeFormatter;

import com.google.protobuf.Message;
import com.google.protobuf.AbstractMessage.Builder;
import com.google.protobuf.InvalidProtocolBufferException;

import example.Example.TimeMessage;

public class Server {
    public static void main (String [] args) {

        // Create a server socket object and start listening for incoming connections.
        try (ServerSocket server_socket = new ServerSocket (Shared.SERVER_PORT)) {

            System.out.println ("Waiting for incoming connection.");

            while (true) {
                // Wait until an incoming connection arrives and accept it.
                try (Socket client_socket = server_socket.accept ()) {

                    System.out.println ("Accepted an incoming connection.");

                    try (
                        InputStream input = client_socket.getInputStream ();
                        OutputStream output = client_socket.getOutputStream ();
                    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    ) {         
                    	
                    		DataInputStream dIn = new DataInputStream(client_socket.getInputStream());
                    		
                    		int length = dIn.readInt();   
                    		byte[] message;
                    		// read length of incoming message
                    		if(length>0) {
                    		    message = new byte[length];
                    		    dIn.readFully(message, 0, message.length); // read the message
                    		    TimeMessage msg = TimeMessage.parseFrom(message);

                    		    LocalDateTime time = LocalDateTime.now(); 
                    		    Builder builder = msg.newBuilder();
                    		    
                    		    boolean changeYear = false;
                    		    boolean changeMonth = false;
                    		    boolean changeDay = false;
                    		    boolean changeHours = false;
                    		    boolean changeMinutes = false;
                    		    boolean changeSeconds = false;

                    		    
                    		    if (msg.getYear() == -1) changeYear = true;         		                       
                    		    if (msg.getMonth() == -1) changeMonth = true;                    		                     		    
                    		    if (msg.getDay() == -1) changeDay = true;                		                        		    
                    		    if (msg.getHours() == -1) changeHours = true;                    		   
                    		    if (msg.getMinutes() == -1) changeMinutes = true;                		                      		    
                    		    if (msg.getSeconds() == -1) changeSeconds = true;
                    		   
                    		    
                    		    if (changeYear) msg = ((example.Example.TimeMessage.Builder) builder).setYear(time.getYear()).build(); 
                    		    if (changeMonth) msg = ((example.Example.TimeMessage.Builder) builder).setMonth(time.getMonthValue()).build(); 
                    		    if (changeDay) msg = ((example.Example.TimeMessage.Builder) builder).setDay(time.getDayOfMonth()).build();
                    		    if (changeHours) msg = ((example.Example.TimeMessage.Builder) builder).setHours(time.getHour()).build(); 
                    		    if (changeMinutes) msg = ((example.Example.TimeMessage.Builder) builder).setMinutes(time.getMinute()).build(); 
                    		    if (changeSeconds) msg = ((example.Example.TimeMessage.Builder) builder).setSeconds(time.getSecond()).build(); 
                 		                     		
                                DataOutputStream dOut = new DataOutputStream(client_socket.getOutputStream());
                            	dOut.writeInt(msg.toByteArray().length); // write length of the message
                            	dOut.write(msg.toByteArray()); 
                    		}            		                                                                                

                    }
                    System.out.println ("Client disconnected.");
                }
                catch (Exception e) {
                    System.out.println (e);
                }
            }
        }
        catch (Exception e) {
            System.out.println (e);
        }
    }
}
