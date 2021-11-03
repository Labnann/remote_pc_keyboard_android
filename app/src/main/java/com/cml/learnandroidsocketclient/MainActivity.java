package com.cml.learnandroidsocketclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private Socket client;
    private PrintWriter printWriter;
    private EditText textField, ipInput;
    private Button sendBurron , setIp;
    private String message, host, port;
    char lastChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textField = (EditText) findViewById(R.id.editText1);
        textField.addTextChangedListener(new TextWatcher() {
            int i;
            boolean passedDot = false;
            boolean passedEnter = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                int index = start+before;

                if(index>=0 && index<charSequence.length())
                lastChar = charSequence.charAt(index);
                System.out.println("It's: "+lastChar +"  : index: "+index+" start: "+start+ "  before: "+before);

                handleSentenceEndingSymbol();



            }

            private void handleSentenceEndingSymbol() {
                if (  isADesiredKey() && !passedDot){
                    System.out.println("Dot PRESSED1!!!!!!!!!!!!!!!!!!!S" + i++);
                    passedDot = true;
                   // System.out.println(textField.getText().toString());
                    if(lastChar=='\n'){
                        sendMessage("10@"+textField.getText().toString().substring(0,textField.getText().toString().length()-1),true);
                        return;
                    }
                    String message = textField.getText().toString();
                   // sendMessage(Integer.valueOf(lastChar)+"@"+textField.getText().toString(),true);
                    sendMessage(Integer.valueOf(lastChar) +"@"+message.substring(0,message.length()-1),true);



                }
                else  if (!isADesiredKey())passedDot=false;

            }

            boolean isADesiredKey(){


                return (lastChar== '\n'|| lastChar == '.' || lastChar == '?' || lastChar == '!' || lastChar=='।' );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ipInput =(EditText) findViewById(R.id.ip); 
        sendBurron = (Button) findViewById(R.id.button1);
        setIp = (Button) findViewById(R.id.setIp);
        ipInput.setText("192.168.43.159:6777");
        createIpAndPort();

        sendBurron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("01"+"@"+textField.getText().toString(),true);
            }
        });



        setIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createIpAndPort();
                new SendMessage().searchIp();


            }
        });

    }

    private void sendMessage(String toSend,boolean resetTextBox) {
        message = toSend;
        SendMessage sendMessageTask = new SendMessage();
        sendMessageTask.execute();
        if(resetTextBox)
        textField.setText("");
    }

    private void createIpAndPort() {
        host = ipInput.getText().toString();
        port = "";

        char[] internet = host.toCharArray();
        host = "";
        boolean isIp = true;
        for(int i = 0; i<internet.length;i++){
            if(internet[i] == ':') {isIp = false; continue;}
            if(isIp){
                host += internet[i];
            }
            else {
                port += internet[i];

            }

        }
     //   System.out.println(host +":" +port);
    }

    private class SendMessage extends AsyncTask<Void, Void, Void> {
        void searchIp(){
            for(int i = 200; i>=0; i--){
                try{
                    System.out.println(i +" ");
                if(InetAddress.getByName("192.168.43."+i).isReachable(100))
                {
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa  192.168.43."+i);
                }

                }
                catch (Exception e){
                 //   e.printStackTrace();
                }
            }
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {

                client = new Socket(host, Integer.valueOf(port));
                System.out.println(message);
                printWriter = new PrintWriter(client.getOutputStream(), true);
                printWriter.write(message);
                printWriter.flush();
                printWriter.close();
                client.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


    }






}
