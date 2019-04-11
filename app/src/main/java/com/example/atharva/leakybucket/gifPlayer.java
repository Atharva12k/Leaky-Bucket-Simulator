package com.example.atharva.leakybucket;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class gifPlayer extends AppCompatActivity {

    public static int nop,sop,rop,sob,delEl;
    TextView update1,update2;
    Queue<Integer> queue;
    public static boolean keepRolling=true;
    Handler handler = new Handler();
    Button stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_player);
        update1=findViewById(R.id.update1);
        update2=findViewById(R.id.update2);
        stop=new Button(getApplicationContext());
        update1.setMovementMethod(new ScrollingMovementMethod());
        update2.setMovementMethod(new ScrollingMovementMethod());
        Intent intent=getIntent();
        nop=intent.getIntExtra("nop",0);
        sop=intent.getIntExtra("sop",0);
        rop=intent.getIntExtra("rop",0);
        sob=intent.getIntExtra("sob",0);

        queue=new LinkedList<>();

        int[] time=new int[nop];
        Random random=new Random();
        int total=0;
        for(int i=0;i<nop;i++)
        {
            int k= random.nextInt(3);
            if(k==0)
            {
                i--;
                continue;
            }
            Log.d("Random time: ",""+k);
            time[i]=k;
            total=total+k;
        }
        new myAsyncCounter(time,nop,update1,stop,queue,sop,sob,getApplicationContext()).execute();

        new Thread(new Runnable() {
            @Override
            public void run() {

                int rate=rop;
                while (keepRolling) {

                    if(!queue.isEmpty())
                    {
                        Log.d("Queue size: ",""+queue.size());
                        for (int j = 0; j < queue.size(); j++) {
                            if (rate < sop) {
                                try{
                                    Thread.sleep(200);
                                }catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                rate=rop;
                                continue;
                            }

                            rate = rate - sop;
                            delEl = queue.remove();
                            myAsyncCounter.tempsize=myAsyncCounter.tempsize-sop;

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    update2.setText(update2.getText() + "\nPacket Leaked: " + delEl);
                                }
                            });
                        }
                    }

                }
            }
        }).start();

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keepRolling=false;
                Toast.makeText(getApplicationContext(),"Process Finished !",Toast.LENGTH_SHORT).show();
                startTokenBucket();

            }
        });

    }
    void startTokenBucket()
    {
        TextView txt=findViewById(R.id.txt1);
        txt.setText("TOKEN BUCKET");
        update1.setText("");
        update2.setText("");
        Toast.makeText(getApplicationContext(),"Starting Token Bucket ...",Toast.LENGTH_SHORT);
        try {
            Thread.sleep(400);
        }
        catch (Exception e){}
        TokenBucket  b = new TokenBucket(sob,update1,update2);
        Thread tokens = new Token(b);
        Thread packets = new Packet(b,getApplicationContext());
        try{
            tokens.start();
            packets.start();
        }
        catch(Exception e){}
    }

    }

class Token extends Thread{
    TokenBucket b;
    int i=0;
    Token(TokenBucket b){
        this.b = b;
    }
    public void run(){
        while(i<gifPlayer.nop){
            b.addToken();
            i++;
            try{
                Thread.sleep(300);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

class Packet extends Thread{
    TokenBucket  b;
    int i=0;
    Context context;
    Packet(TokenBucket b, Context context){
        this.b = b;
        this.context=context;
    }

    public void run(){
        Random r=new Random();
        int currentime=0,time;
        while(i<gifPlayer.nop){
            try{
                time=r.nextInt(3);
                Thread.sleep(1000*time);
                currentime+=time;
            }
            catch(Exception e){
                e.printStackTrace();
            }
            int j=b.packetLeak(gifPlayer.sop);
            i=i+j;
        }
        Toast.makeText(context,"Time taken by Token Bucket "+currentime,Toast.LENGTH_SHORT);
    }
}
class TokenBucket{
    public int tokens, maxsize;
    public static boolean keepCounting=true;
    TextView update1,update2;
    TokenBucket(int max,TextView update1,TextView update2){
        tokens = 0;
        maxsize = max;
        this.update1=update1;
        this.update2=update2;
    }
    synchronized void addToken(){
        if(tokens >= maxsize) return;
        tokens += 1;
        update1.setText(update1.getText().toString()+"\nAdded a token. Total:" + tokens);
    }
    synchronized int packetLeak(int n){
        int noptosend=1,totalsize=gifPlayer.sop;
        while(totalsize<tokens)
        {
            noptosend+=1;
            totalsize=+gifPlayer.sop;
        }
        update1.setText(update1.getText().toString()+"\nNew Packet arrived");
        if(n > tokens){
            update1.setText(update1.getText().toString()+"\nNot enough tokens available !! packet discarded");
        }
        else{
            tokens -= n;
            update1.setText(update1.getText().toString()+"\n" +noptosend+" packets Leaked");
        }
        return noptosend;
    }
}


