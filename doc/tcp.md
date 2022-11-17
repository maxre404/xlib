
本开源库用的socket底层是mina 其实就是对mina 进行封装  解决断包沾包问题 需要和 socket 服务配合一起使用

# 使用socket 样式代码

    thread {
            MinaConnector().connect("",8080,object : MinaIOListener {
                override fun onConnect(session: Any?) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "连接成功", Toast.LENGTH_SHORT).show()
                        LogFile.log("连接成功")
                    }
                }

                override fun onReConnect(session: Any?) {

                }

                override fun onmessageReceived(session: Any?, byteMessage: ByteArray?) {
                    var tcpMessage = TcpMessage(byteMessage)
                    var cmd = tcpMessage.int
                    var bankNumber = tcpMessage.string
                    LogFile.log("打印cmd:$cmd   number:$bankNumber")
                }
            })
        }