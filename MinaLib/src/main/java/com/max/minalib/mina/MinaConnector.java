package com.max.minalib.mina;

import com.max.minalib.TcpLogUtil;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinaConnector {


    private ConnectFuture cf;

    public void connect(final String address, final int port, final MinaIOListener ioListener){

        final NioSocketConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("mycoder", new ProtocolCodecFilter(new MyCodecFcatory(new MyEncoder(), new MyDecoder())));
        ExecutorService executorService = Executors.newCachedThreadPool();
        connector.getFilterChain().addLast("threadPool", new ExecutorFilter(executorService));
        connector.getSessionConfig().setTcpNoDelay(true);
        connector.setConnectTimeoutMillis(6000);
        connector.getSessionConfig().setKeepAlive(true);
        connector.setHandler(new IoHandler() {
            @Override
            public void sessionCreated(IoSession ioSession) throws Exception {

            }

            @Override
            public void sessionOpened(IoSession ioSession) throws Exception {

            }

            @Override
            public void sessionClosed(IoSession ioSession) throws Exception {

            }

            @Override
            public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {

            }

            @Override
            public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {

            }

            @Override
            public void messageReceived(IoSession ioSession, Object message) throws Exception {
                byte[] ioBuffer = (byte[]) message;
                int tt = 0;
//			System.out.println("打印包的长度："+ioBuffer.length);
                byte[] utf_8 = BaseNetTool.getUTF_8(ioBuffer, tt, 1);
                if ("|".equals(new String(utf_8))) {
                    tt += 1;
                    int length = BaseNetTool.Getint(ioBuffer, tt);
                    tt += 4;
                    byte[] reciveData = BaseNetTool.getbyte(ioBuffer, tt, length);
                    tt += length;
                    int k = 0;
                    for (k = 0; k < reciveData.length; k++) {
                        reciveData[k] ^= 255;
                    }
                    int position = 0;
                    int cmd = BaseNetTool.Getint(reciveData, position);
                    ioListener.onmessageReceived(ioSession,reciveData);
                }
            }

            @Override
            public void messageSent(IoSession ioSession, Object o) throws Exception {

            }
        });//
        connector.addListener(new IoListener());
        TcpLogUtil.log(".........................开始连接服务器..........................."+address+":"+port+"................");
         cf = connector.connect(new InetSocketAddress(address, port));//
        connector.getFilterChain().addFirst("reconnection",new IoFilterAdapter(){
            @Override
            public void sessionIdle(NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
                super.sessionIdle(nextFilter, session, status);
                if (status == IdleStatus.READER_IDLE) {
                    System.out.println("50秒没有读取到数据,进入读的空闲状态");
                    session.close(true);//
                }
//
            }

            @Override
            public void sessionClosed(NextFilter nextFilter, IoSession session) throws Exception {
                super.sessionClosed(nextFilter, session);
                TcpLogUtil.log("......................掉线了............");
                tag: for(;;) {
                    try {
                        Thread.sleep(3000);
                        cf = connector.connect(new InetSocketAddress(address, port));
                        cf.awaitUninterruptibly();// 等待连接创建成功
                        session = cf.getSession();// 获取会话
                        ioListener.onReConnect(session);
                        if (session.isConnected()) {
                            TcpLogUtil.log(".............................重连成功....................");
                            break tag;
                        }
                    } catch (Exception ex) {
                        cf.cancel();
                        TcpLogUtil.log("重连服务器登录失败,3秒再连接一次:" + ex.getMessage());
                    }
                }
            }
        });
        cf.awaitUninterruptibly();// 等待连接创建成功
        if(cf.isConnected()) {
            TcpLogUtil.log(".........................服务器连接成功...........................");
            ioListener.onConnect(cf.getSession());
        }else{
            TcpLogUtil.log(".........................服务器连接失败...........................");
        }







    }



}
