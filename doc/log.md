本库使用的日志框架是 xlog 只是做了一层封装
数据将存储在 外部存储相应应用目录下的cache目录下

# 在app onCreate 目录下初始化
    
    LogFile.init(this)


# 在需要打日志的地方
    
    LogFile.log("xxxxxxxx")




