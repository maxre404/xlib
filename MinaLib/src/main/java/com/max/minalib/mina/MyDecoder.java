package com.max.minalib.mina;

import com.max.minalib.TcpLogUtil;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class MyDecoder extends CumulativeProtocolDecoder {
	/**

     * 这个方法的返回值是重点：

     * 1 、当内容刚好时，返回 false ，告知父类接收下一批内容

     * 2 、内容不够时需要下一批发过来的内容，此时返回 false ，这样父类 CumulativeProtocolDecoder

     *    会将内容放进 IoSession 中，等下次来数据后就自动拼装再交给本类的 doDecode

     * 3 、当内容多时，返回 true ，因为需要再将本批数据进行读取，父类会将剩余的数据再次推送本

     * 类的 doDecode

     */
	
	private int DEFAULT_CAPACITY = 64;
	private IoBuffer buffer = IoBuffer.allocate(DEFAULT_CAPACITY);
	public MyDecoder(){
		
	}
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
                               ProtocolDecoderOutput out) throws Exception {
		if (in.remaining()>0) {// 有数据时，读取前判断消息长度
			 byte[] b = new byte[5];
				in.mark();
			if (in.remaining()<5){
				TcpLogUtil.log(".................................小于5个字节重新获取......................................");
				return false;
			}
			in.get(b,0,5);
		       int posion=0;
		       byte[] utf_8 = BaseNetTool.getUTF_8(b, posion, 1);
		       posion+=1;
			if (!new String(utf_8).equals("|")) {
				TcpLogUtil.log("包头不对:以"+new String(utf_8)+"开始的");
				return false;
			}
		       int length = BaseNetTool.getInt(b, posion);
				in.reset();
		      	 if (length>in.remaining()) {// 如果消息内容不够，则重置，相当于不读取 size
		    	   TcpLogUtil.log("数据不够   数据包总长度:"+length+"当前收到的长度:"+in.remaining());
		    	   return false ; // 父类接收新数据，以拼凑成完整数据
			}else {
					byte [] bytes = new byte [length+5];
//					 System.out.println("长度"+in.limit()+"管道剩余长度:"+in.remaining()+"回传过去的长度:"+bytes.length);
					 if (in.remaining()<length+5){
						 TcpLogUtil.log("............................包的长度不够 重新获取.........................................");
						 return false;
					 }
					 in.get(bytes,0,length+5);
					out.write(bytes);
				if (in.remaining()>0) {
					TcpLogUtil.log("....................................沾包处理......................");
					return true ;
				}
//				return true;
			}
		}
//			in.position(in.limit());
			return false ; // 处理成功，让父类进行接收下个包
	}
} 