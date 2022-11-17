package com.max.minalib.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyCodecFcatory implements ProtocolCodecFactory {

private ProtocolEncoder encoder = null ;

private ProtocolDecoder decoder = null ;

public MyCodecFcatory(ProtocolEncoder encoder, CumulativeProtocolDecoder decoder) {

this . encoder = encoder;

this . decoder = decoder;

}

//@Override
//
//public ProtocolEncoder getEncoder(IoSession session) throws Exception {
//
//return this . encoder ;
//
//}
//
//@Override
//
//public ProtocolDecoder getDecoder(IoSession session) throws Exception {
//
//return this . decoder ;
//
//}

@Override
public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
	// TODO Auto-generated method stub
	return this.decoder;
}

@Override
public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
	// TODO Auto-generated method stub
	return this. encoder;
}

}