package com.max.xlib

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.max.minalib.mina.MinaConnector
import com.max.minalib.mina.MinaIOListener
import com.max.minalib.mina.TcpMessage
import com.max.xlib.log.LogFile
import com.max.xlib.ui.theme.XlibTheme
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XlibTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Greeting("Android")
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = {
                            connectSocket()
                        }) {
                            Text(text = "连接socket")
                        }
                    }
                }
            }
        }
    }

    private fun connectSocket() {
        LogFile.init(this)
        thread {
            MinaConnector().connect("192.192.191.119",8080,object : MinaIOListener {
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
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    XlibTheme {
        Greeting("Android")
    }
}