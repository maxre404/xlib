package com.ok.uiframe.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.drake.brv.PageRefreshLayout
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import com.ok.uiframe.OnPermissionRequestCallBack
import com.ok.uiframe.data.AlbumData
import com.ok.uiframe.util.PermissionManager
import com.ok.uiframe.util.PermissionManager.requestPermission
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Random
import kotlin.math.abs


class AlbumFragment: Fragment() {

    private val permissionRuestMap: HashMap<Int, OnPermissionRequestCallBack> = HashMap() //请求权限时候 保存回调与requestCode
    private val TAG = "debug11"
    var pageIndex = 1
    private var pageLimit = 50
    var albumList = mutableListOf<AlbumData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album, container, false)
        initView(view)
//        initDragView(view)
        return view
    }
    var tvName:TextView?=null
    @SuppressLint("CheckResult", "SetTextI18n", "NotifyDataSetChanged")
    private fun initView(view: View?) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvPhoto)
        val btnRead = view?.findViewById<Button>(R.id.btnRead)
        btnRead?.setOnClickListener {
//            val list = getRecentImageUris(activity!!,1000)
//            LogFile.log("打印list:$list")
//            val test = getAllAlbumNames(activity!!)
//            tvName?.text = "所有相册集合:$test"
//            LogFile.log("打印所有相册名称:$test")
        }
        view?.findViewById<Button>(R.id.button4)?.setOnClickListener {
            applayPermission()
        }
        tvName = view?.findViewById<TextView>(R.id.tvName)
        val list = getImageUrisPaged(activity!!,pageIndex,pageLimit)
        list.map { uri->
            AlbumData(data = uri)
            albumList.add(AlbumData(data = uri))
        }
        recyclerView?.grid(4)?.setup {
            addType<AlbumData>(R.layout.item_photo_album)
            onBind {
              val model =  getModel<AlbumData>()
               val image =  findView<ImageView>(R.id.image)
                Glide.with(activity!!).load(model.data).into(image)
            }
        }?.models = albumList
        val page = view?.findViewById<PageRefreshLayout>(R.id.page)
        page?.setEnableLoadMore(true)
        page?.onRefresh {
            Log.d(TAG, "initView: 下啦刷新")
        }
        page?.onLoadMore {
            pageIndex += 1
            Log.d(TAG, "加载更多 pageIndex:$pageIndex")
            val newList = getImageUrisPaged(activity!!,pageIndex,pageLimit).map { uri->
                AlbumData(data = uri)
            }
            Log.d(TAG, "新的list:  $newList")
            albumList.addAll(newList)
            recyclerView?.adapter?.notifyDataSetChanged()
            page.finishLoadMore()


        }
    }
    @SuppressLint("Range")
    fun getFrontCameraImageUris(context: Context, limit: Int = 100): List<Uri> {
        val frontCameraUris = mutableListOf<Uri>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )

        // 查询所有图片，因为我们无法在 SQL 级别筛选
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,   // 不设置 selection
            null,   // 不设置 selectionArgs
            sortOrder
        )
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var count = 0

            while (it.moveToNext() && count < limit) {
                val id = it.getLong(idColumn)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                // 【核心步骤：调用 Exif 判断函数】
                if (isFrontCameraPhoto(context, uri)) {
                    frontCameraUris.add(uri)
                    count++
                }

                // 如果已经达到 limit，则跳出循环
                if (frontCameraUris.size >= limit) break
            }
        }

        return frontCameraUris
    }

    fun isFrontCameraPhoto(context: Context, imageUri: Uri): Boolean {
        // 检查Uri是否为空
        if (imageUri == Uri.EMPTY) {
            return false
        }

        try {
            // 使用ContentResolver获取InputStream
            val inputStream = context.contentResolver.openInputStream(imageUri)
                ?: return false

            // 创建ExifInterface对象
            val exif = ExifInterface(inputStream)

            // Android 10 (API 29)及以上版本，直接使用TAG_LENS_FACING

            // 旧版本或其他未记录LENS_FACING的情况
            // 尝试通过其他EXIF标签进行启发式判断
            // 注意: 这种方法非常不可靠
            val focalLength = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)
            val imageDescription = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION)

            // 启发式判断：焦距通常较短，或者描述中可能包含"selfie"等关键字
            if (focalLength != null) {
                // 这里需要根据具体设备的前后置焦距差异来判断，没有通用标准
                // 例如，如果已知后置焦距为4mm，前置为2mm，可以这样判断：
                // if (focalLength.toFloatOrNull() ?: 0f < 3.0f) return true
            }

            if (imageDescription != null) {
                // 检查是否有"selfie"等关键字（极度不可靠）
                // if (imageDescription.lowercase().contains("selfie")) return true
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }



    @SuppressLint("Range")
    fun getSelfieImages(context: Context, limit: Int = 100): List<Uri> {
        val imageUris = mutableListOf<Uri>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,                 // 旧路径字段（Android Q 以前有效）
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME   // 相册名称
        )

        val selection = "${MediaStore.Images.Media.DATA} LIKE ? OR ${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} LIKE ?"
        val selectionArgs = arrayOf("%Selfie%", "%Front%")

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var count = 0
            while (it.moveToNext() && count < limit) {
                val id = it.getLong(idColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageUris.add(uri)
                count++
            }
        }

        return imageUris
    }






    private fun applayPermission() {
        val permissionList: MutableList<String> = ArrayList()
        permissionList.add(Manifest.permission.CAMERA)
        permissionList.add(Manifest.permission.RECORD_AUDIO)
        permissionList.add(Manifest.permission.READ_PHONE_STATE)
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionList.add(Manifest.permission.BLUETOOTH)
        permissionList.add(Manifest.permission.BLUETOOTH_ADMIN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        checkPermissionAndRequest(
            permissionList.toTypedArray(),
            object : OnPermissionRequestCallBack {
                override fun requestSuccess() {
                    LogFile.log("请求成功")
                }

                override fun requestFailed(isShowRequest: Boolean) {
                    LogFile.log("权限申请失败")
                }

            })
    }

    fun requestPermission(permissions: Array<String?>?, callBack: OnPermissionRequestCallBack?) {
        val random = Random()
        val requestCode = abs(random.nextInt(500).toDouble()).toInt()
        permissionRuestMap.put(requestCode, callBack!!)
//        permissionRuestMap[requestCode] = callBack
        requestPermission(activity, permissions, requestCode)
    }
    fun checkPermissionAndRequest(
        permissions: Array<String?>?,
        callBack: OnPermissionRequestCallBack
    ) {
        if (PermissionManager.checkPermission(context, permissions)) {
            callBack.requestSuccess()
        } else {
            requestPermission(permissions, callBack)
        }
    }


    @SuppressLint("Range")
    fun getImageUrisPaged(
        context: Context,
        page: Int,
        pageSize: Int
    ): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        // 排序规则
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        // 分页参数
        val offset = (page - 1) * pageSize
        val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // ✅ Android 11+ 可用 LIMIT & OFFSET
            val queryArgs = Bundle().apply {
                putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, sortOrder)
                putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
            }

            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                queryArgs,
                null
            )
        } else {
            // ✅ Android <= 10 fallback（手动 OFFSET）
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                null,
                null,
                "$sortOrder LIMIT $pageSize OFFSET $offset"
            )
        }

        cursor?.use {
            val columnId = it.getColumnIndex(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val id = it.getLong(columnId)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imageUris.add(uri)
            }
        }

        return imageUris
    }

    @SuppressLint("Range")
    fun getFavoriteImageUris(context: Context, limit: Int = 100): List<Uri> {
        val imageUris = mutableListOf<Uri>()

        // 【修改点 1：添加 IS_FAVORITE 字段】
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.MediaColumns.IS_FAVORITE // Android 11+ 支持
        )

        // 【修改点 2：设置筛选条件，只查找收藏的图片】
        val selection = "${MediaStore.MediaColumns.IS_FAVORITE} = ?"
        val selectionArgs = arrayOf("1") // 1 代表收藏 (true)

        // 按添加时间降序排列
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,   // 使用筛选条件
            selectionArgs, // 传入筛选参数
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            var count = 0
            while (it.moveToNext() && count < limit) {
                val id = it.getLong(idColumn)
                // 构造图片的 content:// URI
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                imageUris.add(uri)
                count++
            }
        }

        return imageUris
    }

    fun getAllAlbumNames(context: Context): List<String> {
        val albums = mutableSetOf<String>()
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            while (it.moveToNext()) {
                try {
                    albums.add(it.getString(nameColumn))
                } catch (e: Exception) {
                    LogFile.log("eeeeeeee:$e")
                }
            }
        }

        return albums.toList()
    }


}