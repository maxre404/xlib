package com.max.minalib.mina;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZInputStream;
import com.jcraft.jzlib.ZOutputStream;
import com.max.minalib.TcpLogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BaseNetTool {

	public static final int MAX_BUF = 1024 * 8;

	public BaseNetTool() {
		// TODO Auto-generated constructor stub
	
	}

//	public static byte[] appendHead(byte[] buf) throws IOException {
//		// char int
//		// head = ( | + len )
//		// | + len + { cmd + .... }
//		// compress  -------------- 1  没锟斤拷
////		byte[] bufCompress = gZip(buf);
//		
//		
//		// encode ------------------ 2
//		for(int i=0;i<buf.length;i++){
//			buf[i] ^= 255;
//		}
//		byte[] realBuf = new byte[MAX_BUF];
//		//-----------------------------3
//		realBuf[0] = 124;
//		
//		//------------------------------4
//		writeInt(buf.length, realBuf, 1);
//		
//		int i = 0, len = buf.length;
//		for (i = 0; i < len; i++) {
//			realBuf[5 + i] = buf[i];
//		}
//		
//
//		return realBuf;
//	}
	

	
	public static byte[] appendHead2(byte[] buf) throws IOException {
		// char int
		// head = ( | + len )
		// | + len + { cmd + .... }
		// compress  -------------- 1  没锟斤拷
		//-----------------------------3
		int pp=0;
		int cmd = BaseNetTool.getInt(buf, pp);
		TcpLogUtil.log(">>>>>>>>>>>>>>>>发送出去的cmd>>>>>>>>>>>>>>>>>>>>>"+cmd);
		for(int j=0;j<buf.length;j++){//加密
			buf[j] ^= 255;
		}
		byte[] realBuf = new byte[buf.length+7];
		realBuf[0] = 124;
		//------------------------------4
		writeInt(buf.length, realBuf, 1);

		int i = 0, len = buf.length;
		for (i = 0; i < len; i++) {
			realBuf[5 + i] = buf[i];
		}
		realBuf[realBuf.length-2]=36;
		realBuf[realBuf.length-1]=36;


				
		return realBuf;
	}
	

	public static void send2Serv() {
		

	}
	public static byte[] gZip(byte[] data) {
		  byte[] b = null;
		  try {
		   ByteArrayOutputStream bos = new ByteArrayOutputStream();
		   GZIPOutputStream gzip = new GZIPOutputStream(bos);
		   gzip.write(data);
		   gzip.finish();
		   gzip.close();
		   b = bos.toByteArray();
		   bos.close();
		  } catch (Exception ex) {
		   ex.printStackTrace();
		  }
		  return b;
		 }
	 /***
	  * 压缩Zip
	  * 
	  * @param data
	  * @return
	  */
	 public static byte[] zip(byte[] data) {
	  byte[] b = null;
	  try {
	   ByteArrayOutputStream bos = new ByteArrayOutputStream();
	   ZipOutputStream zip = new ZipOutputStream(bos);
	   ZipEntry entry = new ZipEntry("zip");
	   entry.setSize(data.length);
	   zip.putNextEntry(entry);
	   zip.write(data);
	   zip.closeEntry();
	   zip.close();
	   b = bos.toByteArray();
	   bos.close();
	  } catch (Exception ex) {
	   ex.printStackTrace();
	  }
	  return b;
	 }
	
	 
		/*
		 * 压缩数据
		 */
		 public static byte[] jzlib(byte[] object) {
			  byte[] data = null;
			  try {
			   ByteArrayOutputStream out = new ByteArrayOutputStream();
			   ZOutputStream zOut = new ZOutputStream(out,
			     JZlib.Z_DEFAULT_COMPRESSION);
			   DataOutputStream objOut = new DataOutputStream(zOut);
			   objOut.write(object);
			   objOut.flush();
			   zOut.close();
			   data = out.toByteArray();
			   out.close();
			  } catch (IOException e) {
			   e.printStackTrace();
			  }
			  return data;
			 }
	 
	 /*
	  * 解压数据
	  */
	 public static byte[] unjzlib(byte[] object) {

		  byte[] data = null;
		  try {
		   ByteArrayInputStream in = new ByteArrayInputStream(object);
		   ZInputStream zIn = new ZInputStream(in);
		   byte[] buf = new byte[1024*2];
		   int num = -1;
		   ByteArrayOutputStream baos = new ByteArrayOutputStream();
		   while ((num = zIn.read(buf, 0, buf.length)) != -1) {
		    baos.write(buf, 0, num);
		   }
		   data = baos.toByteArray();
		   baos.flush();
		   baos.close();
		   zIn.close();
		   in.close();
		   
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  return data;
		 }
	/*
	  * 解压数据
	  */
	public static byte[] unjzlib(byte[] object,int len) {

		byte[] data = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(object);
			ZInputStream zIn = new ZInputStream(in);
			byte[] buf = new byte[len];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = zIn.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			TcpLogUtil.log("最后的长度"+num);
			data = baos.toByteArray();
			baos.flush();
			baos.close();
			zIn.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
/*
 * 鍘嬬缉	
 */
	 public static final byte[] compress(String paramString) {
	        if (paramString == null)  
	            return null;  
	        ByteArrayOutputStream byteArrayOutputStream = null;
	        ZipOutputStream zipOutputStream = null;
	        byte[] arrayOfByte;  
	        try {  
	            byteArrayOutputStream = new ByteArrayOutputStream();
	            zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
	            zipOutputStream.putNextEntry(new ZipEntry("0"));
	            zipOutputStream.write(paramString.getBytes());  
	            zipOutputStream.closeEntry();  
	            arrayOfByte = byteArrayOutputStream.toByteArray();  
	        } catch (IOException localIOException5) {
	            arrayOfByte = null;  
	        } finally {  
	            if (zipOutputStream != null)  
	                try {  
	                    zipOutputStream.close();  
	                } catch (IOException localIOException6) {
	            }  
	            if (byteArrayOutputStream != null)  
	                try {  
	                    byteArrayOutputStream.close();  
	                } catch (IOException localIOException7) {
	            }  
	        }  
	        return arrayOfByte;  
	    }  

	public static byte[] writeInt(int num, byte[] arr, int pos) {
		arr[pos] = (byte) (num);
		arr[pos + 1] = (byte) (num >>> 8);
		arr[pos + 2] = (byte) (num >>> 16);
		arr[pos + 3] = (byte) (num >>> 24);
		return arr;
	}

	public static void writeLong(long longValue,byte[] arr, int pos)
	{
		arr[pos+7] = (byte) (longValue >> 56);
		arr[pos+6] = (byte) (longValue >> 48);
		arr[pos+5] = (byte) (longValue >> 40);
		arr[pos+4] = (byte) (longValue >> 32);
		arr[pos+3] = (byte) (longValue >> 24);
		arr[pos+2] = (byte) (longValue >> 16);
		arr[pos+1] = (byte) (longValue >> 8);
		arr[pos+0] = (byte) (longValue >> 0);
	}
    public static int byte2int(byte[] res) {   
	    int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) //  
	    | ((res[2] << 24) >>> 8) | (res[3] << 24);   
	    return targets;   
    }   
    public static void int2byteArray(int num, byte[] arr, int pos) {
		arr[pos] = (byte) (num);
		arr[pos + 1] = (byte) (num >>> 8);
		arr[pos + 2] = (byte) (num >>> 16);
		arr[pos + 3] = (byte) (num >>> 24);
	}
//    public static int writeUTF8(String str, byte[] arr, int pos) {
//		try {
//			byte[] tmpArr = str.getBytes("UTF-8");
//			int i, len = tmpArr.length;
//
//			System.out.println("tmpArr.length = " + tmpArr.length);
//			int2byteArray(len, arr, pos);
//			pos += 4;
//
//			for (i = 0; i < len; i++) {
//				arr[pos + i] = tmpArr[i];
//			}
//			return (len + 4);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return -1;
//	}
    /*
     * 对数据进行解密和解压缩
     */
    public static byte[] getJYBuf(InputStream is) throws IOException {
		byte[] tmpLenBuf = new byte[4];
		is.read(tmpLenBuf, 0, 4);
		int bufLen = BaseNetTool.byte2int(tmpLenBuf);
		if (bufLen<0) {
			return "".getBytes();
		}
		TcpLogUtil.log("------------------");
		TcpLogUtil.log("-->" + bufLen);

		byte[] getBuf = new byte[bufLen];
		is.read(getBuf, 0, bufLen);
		int k = 0;
		for (k = 0; k < bufLen; k++) {
			getBuf[k] ^= 255;
		}
			byte[]serBuf = BaseNetTool.unjzlib(getBuf);//瑙ｅ帇
		return serBuf;
	}
    public static boolean isSuccess(InputStream iso) throws IOException, InterruptedException {//鍒ゆ柇鏄惁瑙ｆ瀽澶撮儴淇℃伅鎴愬姛
    	Thread.sleep(800);
    	int out = iso.available();
		TcpLogUtil.log("解析状态"+out+"df");
    	char head = '|';
    	byte[] __head;
    	__head = new byte[1];
		int rst = iso.read(__head);
		TcpLogUtil.log("解析头部文件"+ new String(__head));
		if ((char) __head[0] == head) {
			return true;
		}else {
			return false;
		}
    	
    }
    public static int getInt(byte[] buff, int pos){
    	byte [] jkl={buff[pos],buff[pos+1],buff[pos+2],buff[pos+3]};
    	int result = BaseNetTool.byte2int(jkl);
    	return result;
    }
    public static byte[] getUTF_8(byte[] buff,int pos1,int len){
		byte [] msg=new byte[len];
    	for (int i = 0; i < len; i++) {
			msg [i]=buff[pos1+i];
		}
		return msg;
    }
    public static byte[] getbyte(byte[]buff,int pos,int len){
    	byte[]result=new byte[len];
		for (int i = 0; i < len; i++) {
			result[i]=buff[pos+i];
		}
		
		return result;
	}
    public static byte[] writeUTF8_2(String str, byte[] arr, int pos) {
    	byte[] tmpArr=str.getBytes();
    	int len=tmpArr.length;
    	for (int i = 0; i <len; i++) {
			arr[i+pos]=tmpArr[i];
		}
    	
		return arr;
		
   	}
    public static void writeFloat( float x, byte[] bb,int index) {
        // byte[] b = new byte[4];
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = new Integer(l).byteValue();
            l = l >> 8;
        }
    }
	// 从byte数组的index处的连续4个字节获得一个int
	public static int getFInt(byte[] arr, int index) {
		return  (0xff000000     & (arr[index+0] << 4))  |
				(0x00ff0000     & (arr[index+1] << 16))  |
				(0x0000ff00     & (arr[index+2] << 8))   |
				(0x000000ff     &  arr[index+3]);
	}
	public static float getFloat(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
	}
	public static float readFloat(byte[] res, int pos) {
		int readFloat_targets;
		float readFloat_out;
			readFloat_targets = res[pos] & 255 | res[pos + 1] << 8 & '\uff00' | res[pos + 2] << 24 >>> 8 | res[pos + 3] << 24;
			readFloat_out = Float.intBitsToFloat(readFloat_targets);
			return readFloat_out;

	}
    public static void writeShort(short num, byte[] arr, int pos) {
		 arr[pos + 1] = (byte) (num >> 8);
	     arr[pos + 0] = (byte) (num >> 0);
	}
	 public static short getShort(byte[] b, int index) {
	      return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	}
	 public static byte[] writeDouble(double num, byte[] arr, int pos){
		 // byte[] b = new byte[8];  
        long l = Double.doubleToLongBits(num);
        for (int i = 0; i < 8; i++) {  
        	arr[pos + i] = new Long(l).byteValue();
            l = l >> 8;  
        }  
		
		return arr;
	}

	public static double readDouble(byte[] res, int pos) {
		return Double.longBitsToDouble((((long)res[pos+0] << 56) +
				((long)(res[pos+1] & 255) << 48) +
				((long)(res[pos+2] & 255) << 40) +
				((long)(res[pos+3] & 255) << 32) +
				((long)(res[pos+4] & 255) << 24) +
				((res[pos+5] & 255) << 16) +
				((res[pos+6] & 255) <<  8) +
				((res[pos+7] & 255) <<  0)));
	}


	 public static double getDouble(byte[] b, int index) {
	        long l;
	        l = b[index];
	        l &= 0xff;
	        l |= ((long) b[index+1] << 8);
	        l &= 0xffff;
	        l |= ((long) b[index+2] << 16);
	        l &= 0xffffff;
	        l |= ((long) b[index+3] << 24);
	        l &= 0xffffffffl;
	        l |= ((long) b[index+4] << 32);
	        l &= 0xffffffffffl;
	        l |= ((long) b[index+5] << 40);
	        l &= 0xffffffffffffl;
	        l |= ((long) b[index+6] << 48);
	        l &= 0xffffffffffffffl;
	        l |= ((long) b[index+7] << 56);
	        return Double.longBitsToDouble(l);
	    }  
	//java 合并两个byte数组  
	    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
	        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
	        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
	        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
	        return byte_3;  
	    } 

}
