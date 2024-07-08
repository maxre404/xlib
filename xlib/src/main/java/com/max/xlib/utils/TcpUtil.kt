package com.max.xlib.utils

import com.max.minalib.mina.MinaConnector
import com.max.minalib.mina.MinaIOListener
import com.max.minalib.mina.TcpMessage
import com.max.xlib.log.LogFile
import kotlin.concurrent.thread

class TcpUtil {

    companion object{

        fun onReceive(cmd:Int){

        }
        fun init(ip:String?=null,port:Int?=0){
            ip?.let {
                if (0!=port){
                    thread {
                        MinaConnector().connect(ip,port!!,object : MinaIOListener {
                            override fun onConnect(session: Any?) {
                                LogFile.log("++++++++socket 连接成功+++++++++++++")

                            }

                            override fun onReConnect(session: Any?) {

                            }

                            override fun onmessageReceived(session: Any?, byteMessage: ByteArray?) {
                                var tcpMessage = TcpMessage(byteMessage)
                                var cmd = tcpMessage.int
                                var bankNumber = tcpMessage.string
                                onReceive(cmd)
                            }
                        })
                    }
                }
            }
        }


    }
}