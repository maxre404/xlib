package com.ok.uiframe.classloader

import android.util.Log
import dalvik.system.DexClassLoader

class CustomDexClassLoader(
    dexPath: String,
    optimizedDirectory: String,
    librarySearchPath: String?,
    parent: ClassLoader
) : DexClassLoader(dexPath, optimizedDirectory, librarySearchPath, parent) {

    @Throws(ClassNotFoundException::class)
    override fun findClass(name: String): Class<*> {
        try {
            // 优先从自定义的 dex 文件加载类
            Log.d("debug11", "findClass: 这里查找类:$name")
            return super.findClass(name)
        } catch (e: ClassNotFoundException) {
            // 如果在自定义 dex 文件中未找到类，交给父类加载器
            Log.d("debug11", "findClass: 找不到 交给父类处理:$name")
            return parent.loadClass(name)
        }
    }
}