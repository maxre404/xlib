


function xlog(msg){
        // var log = Java.use("com.example.obs.player.utils.LogHelper");
        // log.i(msg);
        var log = Java.use("android.util.Log");
        log.i("xlib",msg)
}
Java.perform(function(){
       xlog("+++init success123++++")

    })