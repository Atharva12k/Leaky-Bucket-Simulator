package com.example.atharva.leakybucket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Queue;

public class myAsyncCounter extends AsyncTask<Integer,Integer,Integer> {

    int[] time;
    private int size,currentTime=0,sob,sop;
    static  int tempsize=0;
    private boolean keepCounting=true;
    Queue<Integer> queue;
    @SuppressLint("StaticFieldLeak")
    Button stop;
    @SuppressLint("StaticFieldLeak")
    TextView update;
    Context context;
    myAsyncCounter(int[] time,int size,View view,Button stop,Queue<Integer> queue,int sop,int sob,Context context) {
        this.time=time;
        this.size=size;
        this.update=(TextView)view;
        this.stop=stop;
        this.queue=queue;
        this.sop=sop;
        this.sob=sob;
        this.context=context;
    }
    @Override
    protected Integer doInBackground(Integer... arg0)
    {
        for(int i=0;i<size;i++) {
            int ct=0,k=0;
            for (int j=0;j<=time[i];j++)
            {
                try {
                    Thread.sleep(1000);
                    ct++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            currentTime=currentTime+ct;
            tempsize=tempsize+sop;
            publishProgress(currentTime,i+1);
        }
        return currentTime;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (tempsize<sob) {
            update.setText(update.getText() + "\nPacket Received in Bucket: " + values[1] + " At time: " + values[0] + " sec");
            queue.add(values[1]);
        }else {
            //Toast.makeText(context,"Bucket Full !\nPacket OverFlooded: "+values[1],Toast.LENGTH_SHORT).show();
            update.setText(update.getText() + "\nBucket Full... Packet OverFlooded: "+values[1]);
        }
        }


    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        Toast.makeText(context,"Time taken by leaky bucket "+currentTime,Toast.LENGTH_SHORT);
        stop.callOnClick();
    }
}
