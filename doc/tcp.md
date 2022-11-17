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