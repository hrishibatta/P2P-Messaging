import  java.security.InvalidKeyException;
import  java.security.NoSuchAlgorithmException;
import  java.util.*;

import  javax.crypto.BadPaddingException;
import  javax.crypto.Cipher;
import  javax.crypto.IllegalBlockSizeException;
import  javax.crypto.KeyGenerator;
import  javax.crypto.NoSuchPaddingException;
import  javax.crypto.SecretKey;
import  java.io.BufferedReader;
import  java.io.IOException;
import  java.io.InputStreamReader;
import  java.io.PrintWriter;
import  java.net.ServerSocket;
import  java.net.Socket;
import  java.util.HashSet;
import java.util.logging.Handler;


public class Server {
    public static String KEY = "KEYKEYXX";
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    private static final int PORT = 8888;

    public static void main(String[] args) throws Exception {
        System.out.println("Peer Chat server is now running");
        //connect the clients to the server
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }//create a "messaging queue, which takes in messages, processes it, and send itout

    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public Handler(Socket socket) {
            this.socket = socket;
        }


        public void print(boolean x) {
            System.out.println(x);
        }

        public void print(String x) {
            System.out.println(x);
        }


        public void run() {
            try {
                String end;

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    out.println("LOGIN");
                    name = (in.readLine());

                    synchronized (names) {
                        if (name.equals("hrishi1234")) {
                            name = "hrishi";
                            names.add(name);
                            print(name + " joined");
                            break;
                        }
                        if (name.equals("shripad0000")) {
                            name = "shripad";
                            names.add(name);
                            print(name + " joined");
                            break;
                        }
                    }
                }

                out.println(("HANDSHAKE"));
                writers.add(out);
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("STREAM " + name + ": " + input);
                    }
                }

            } catch (IOException e) {
                System.out.println(e);
            } finally {
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}