package com.ok.uiframe

import android.app.Application
import android.content.Context
import android.util.Log
import com.ok.uiframe.classloader.CustomDexClassLoader
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class App : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 设置自定义的 ClassLoader
//        val optimizedDir = getDir("dex", Context.MODE_PRIVATE).absolutePath
//        val dexPath = optimizedDir + "/app_patch.apk"
//        val customClassLoader = CustomDexClassLoader(dexPath, optimizedDir, null, classLoader)
//
//        // 将当前线程的 ClassLoader 替换为自定义的 ClassLoader
//        replaceClassLoader(customClassLoader)
//        Log.d("debug11", "attachBaseContext: 替换默认加载器")
        doReplace()
    }
    private fun doReplace() {
        // 删除优化后的 dex 文件缓存
        val dexCacheDir = this.getDir("dex", Context.MODE_PRIVATE)
        if (dexCacheDir.exists()) {
            dexCacheDir.deleteRecursively()
        }
        val apkFile =  copyAPKFromAssets(this,"app_patch.apk")
//      val apkFile =  copyAPKFromAssets(this,"patch_dex.dex")

        // 假设插件已下载完成
//        thread {
        Log.d("debug11", "这里多线程进行处理 doReplace: ")
        injectDex(apkFile.absolutePath)
//        }
        replaceClassLoader(this.classLoader)

    }
    fun injectDex(apkPath: String) {
        try {
            // 获取当前 ClassLoader
            val classLoader = this.classLoader

            // 通过反射获取 BaseDexClassLoader 的 pathList 字段
            val pathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
            pathListField.isAccessible = true
            val dexPathList = pathListField.get(classLoader)

            // 获取 DexPathList 中的 dexElements 字段
            val dexElementsField = dexPathList.javaClass.getDeclaredField("dexElements")
            dexElementsField.isAccessible = true
            val dexElements = dexElementsField.get(dexPathList) as Array<*>

            // 使用 DexClassLoader 加载新的 dex
            val optimizedDir = getDir("dex", Context.MODE_PRIVATE)
            val dexClassLoader = DexClassLoader(
                apkPath,
                optimizedDir.absolutePath,
                null,
                classLoader
            )

            val loadClass = dexClassLoader.loadClass("com.ok.uiframe.util.DexUtil")
            val dexUtil = loadClass.getConstructor().newInstance()
// 获取静态方法
            val method = loadClass.getMethod("getDescription")
            method.isAccessible = true
            // 调用静态方法并获取返回值
            val result = method.invoke(dexUtil) as String  // 静态方法的实例传递为 null
            Log.d("debug11", "Static method result: $result")


            // 获取新的 DexElements
            val newPathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
            newPathListField.isAccessible = true
            val newDexPathList = newPathListField.get(dexClassLoader)

            val newDexElementsField = newDexPathList.javaClass.getDeclaredField("dexElements")
            newDexElementsField.isAccessible = true
            val newDexElements = newDexElementsField.get(newDexPathList) as Array<*>

            // 合并旧的和新的 DexElements，将新 DexElements 放在前面
            val combinedDexElements = java.lang.reflect.Array.newInstance(
                dexElements.javaClass.componentType,
                dexElements.size + newDexElements.size
            ) as Array<*>

// 将新的 DexElements 放在前面
            System.arraycopy(newDexElements, 0, combinedDexElements, 0, newDexElements.size)
            System.arraycopy(dexElements, 0, combinedDexElements, newDexElements.size, dexElements.size)

// 将合并后的 DexElements 设置回去
            dexElementsField.set(dexPathList, combinedDexElements)
            Log.d("debug11", "injectDex: 注入完毕")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun copyAPKFromAssets(context: Context, apkName: String?): File {
        val outFile: File = File(context.getCacheDir(), apkName)
        try {
            context.getAssets().open(apkName?:"").use { `is` ->
                FileOutputStream(outFile).use { fos ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while ((`is`.read(buffer).also { length = it }) > 0) {
                        fos.write(buffer, 0, length)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return outFile
    }
    fun replaceClassLoader(customClassLoader: ClassLoader) {
        try {
            val baseDexClassLoaderClass = BaseDexClassLoader::class.java

            // 通过反射获取 pathList 字段
            val pathListField = baseDexClassLoaderClass.getDeclaredField("pathList")
            pathListField.isAccessible = true

            // 获取当前 classLoader 的 pathList
            val originalPathList = pathListField.get(this.classLoader)

            // 将 customClassLoader 的 pathList 替换到原 classLoader 的 pathList
            pathListField.set(this.classLoader, pathListField.get(customClassLoader))

            Log.d("debug11", "ClassLoader replaced successfully.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onCreate() {
        super.onCreate()
    }
}
