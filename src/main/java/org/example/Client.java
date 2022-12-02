package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
        } catch (IOException e){
            cloneEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e){
            cloneEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    private void cloneEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }





    @Override
    public void run() {
        String messageFromChat;

        while (socket.isConnected()){
            try {
                messageFromChat = bufferedReader.readLine();
                System.out.println(messageFromChat);
            } catch (IOException e){
                cloneEverything(socket, bufferedReader, bufferedWriter);
            }
        }

    }
//    public void ListenForMessage() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String messageFromChat;
//
//                while (socket.isConnected()){
//                    try {
//                        messageFromChat = bufferedReader.readLine();
//                        System.out.println(messageFromChat);
//                    } catch (IOException e){
//                        cloneEverything(socket, bufferedReader, bufferedWriter);
//                    }
//                }
//            }
//        }).start();
//    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username :  ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1212);
        Client client = new Client(socket, username);
//        client.ListenForMessage();
        Thread thread = new Thread(client);
        thread.start();
        client.sendMessage();
    }


}
