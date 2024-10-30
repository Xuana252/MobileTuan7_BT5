package com.example.bt5;

public class CallLogClass {
    private String id;
    private int type;

    private String number;

    private String time;


    public CallLogClass (String ID,String NUMBER,String TIME,int TYPE) {
        this.id=ID;
        this.number=NUMBER;
        this.time=TIME;
        this.type=TYPE;
    }

    public String getId(){
        return this.id;
    }
    public String getNumber(){
        return this.number;
    }
    public int getType(){
        return this.type;
    }

    public String getTime(){
        return this.time;
    }
}
