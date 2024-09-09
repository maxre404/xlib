package com.ok.uiframe.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.max.uiframe.R
import com.ok.uiframe.adapter.ViewPagerAdapter
import com.ok.uiframe.util.DexUtil
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class CoordinatorLayoutActivity : AppCompatActivity() {
    private var viewPager: ViewPager2? = null
    private var tabLayout: TabLayout? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coording_layout)

        // 初始化 Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        // 初始化 ViewPager2 和 TabLayout
        viewPager = findViewById<ViewPager2>(R.id.view_pager)
        tabLayout = findViewById<TabLayout>(R.id.tab_layout)


        // 设置 ViewPager2 适配器
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager?.adapter = viewPagerAdapter


        // 绑定 TabLayout 和 ViewPager2
        TabLayoutMediator(
            tabLayout!!, viewPager!!
        ) { tab, position -> tab.setText("Tab " + (position + 1)) }.attach()
        val frameLayout = findViewById<FrameLayout>(R.id.frameLayout)
        val textView = findViewById<TextView>(R.id.tvLayout)
        frameLayout.setOnClickListener {
            textView.text = DexUtil.getDescription()
        }
        doReplace()
    }

    private fun doReplace() {
      val apkFile =  copyAPKFromAssets(this,"app_patch.apk")

        // 假设插件已下载完成
        val pluginApkPath = "app_patch.apk"
        injectDex(apkFile.absolutePath)
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
    fun injectDex(apkPath: String) {
        try {
            // 获取当前 ClassLoader
            val classLoader = classLoader

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

// 获取静态方法
            val method = loadClass.getMethod("getDescription")

            // 调用静态方法并获取返回值
            val result = method.invoke(null) as String  // 静态方法的实例传递为 null
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

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}