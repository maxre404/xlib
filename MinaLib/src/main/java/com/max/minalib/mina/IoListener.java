package com.max.minalib.mina;

import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.IoServiceListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


public class IoListener implements IoServiceListener {
//	NioSocketConnector connector;
//	public IoListener(NioSocketConnector connector){
////		this.connector=connector;
//		
//	}
    @Override
    public void serviceActivated(IoService arg0) throws Exception {
        // TODO Auto-generated method stub

    }  
    @Override
    public void serviceDeactivated(IoService arg0) throws Exception {
        // TODO Auto-generated method stub
    }
    @Override
    public void serviceIdle(IoService arg0, IdleStatus arg1) throws Exception {
        // TODO Auto-generated method stub  
    }  
    @Override
    public void sessionCreated(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub  
    }  
  
    @Override
    public void sessionDestroyed(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub  
    	System.out.println("session销毁了");

    }  
}