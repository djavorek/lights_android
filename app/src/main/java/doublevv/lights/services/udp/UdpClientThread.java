package doublevv.lights.services.udp;

import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UdpClientThread extends Thread{

    private static boolean isReady = true;

    private String destinationIP;
    private String message;
    private UdpClientHandler handler;

    private int destinationPort;
    private int responsePort;

    {
        destinationPort = 2390;
        responsePort = 2391;
    }

    public UdpClientThread(String address, String message, UdpClientHandler handler) {
        super();
        this.destinationIP = address;
        this.message = message;
        this.handler = handler;
    }

    @Override
    public void run() {
        if(isReady)
        {
            isReady = false;
            DatagramSocket socket = null;
            byte[] buffer = new byte[256];
            String responseStatus = null;

            try {
                socket = new DatagramSocket(responsePort);
                InetAddress address = InetAddress.getByName(destinationIP);

                DatagramPacket packet =
                        new DatagramPacket(message.getBytes(), message.getBytes().length, address, destinationPort);
                socket.send(packet);

                try {
                    packet = new DatagramPacket(buffer, buffer.length);
                    socket.setSoTimeout(2000);
                    socket.receive(packet);
                    responseStatus = new String(packet.getData(), 0, packet.getLength());

                    ResponseMessage responseMessage = new ResponseMessage(packet.getAddress().getHostAddress(), responseStatus);
                    handler.sendMessage(
                            Message.obtain(handler, handler.UPDATE, responseMessage));
                }
                catch (SocketTimeoutException e) {
                    handler.sendEmptyMessage(handler.NO_RESPONSE);
                }
                catch (NullPointerException e) {
                    System.err.println("Response received, but was null.. (no way)");
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(socket != null){
                    socket.close();
                }
                handler.sendEmptyMessage(handler.DONE);
                isReady = true;
            }
        }
    }
}