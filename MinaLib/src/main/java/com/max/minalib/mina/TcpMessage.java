package com.max.minalib.mina;

public class TcpMessage {
    byte[] message;
    int index = 0;
    public TcpMessage(byte[] message){
        this.message = message;
    }
    public int getInt(){
       int value =  BaseNetTool.getInt(message,index);
       index+=4;
       return value;
    }
    public String getString(){
        int len = getInt();
        String res = new String(BaseNetTool.getUTF_8(message,index,len));
        index+=len;
        return res;
    }
}
