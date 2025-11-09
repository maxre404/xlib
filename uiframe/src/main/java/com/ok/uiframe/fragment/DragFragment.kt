package com.ok.uiframe.fragment

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.BindingAdapter
import com.drake.brv.annotaion.ItemOrientation
import com.drake.brv.listener.DefaultItemTouchCallback
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.max.uiframe.R
import com.max.xlib.log.LogFile
import com.ok.uiframe.adapter.AlbumDraggableAdapter
import com.ok.uiframe.data.DraggablePhotoAlbumData
import com.ok.uiframe.data.PhotoItem
import com.ok.uiframe.widget.DragOnlyItemTouchHelperCallback

class DragFragment:Fragment(){
    val TAG = "debug11"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_frag_test, container, false)
        initDragView(view)
       val list = loadPagedPhotos(context!!,1,10)
        Log.d(TAG, "onCreateView: $list")
        return view
    }
    @SuppressLint("ClickableViewAccessibility")
    fun  initDragView(view: View?){
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvDrag)
        val data = mutableListOf(DraggablePhotoAlbumData("Item 1"),DraggablePhotoAlbumData("Item 2"),DraggablePhotoAlbumData("Item 3"),DraggablePhotoAlbumData("Item 4"),DraggablePhotoAlbumData("Item 5"),DraggablePhotoAlbumData("Item 6", ItemOrientation.NONE),DraggablePhotoAlbumData("Item 7", ItemOrientation.NONE),DraggablePhotoAlbumData("Item 8", ItemOrientation.NONE),DraggablePhotoAlbumData("Item 9", ItemOrientation.NONE),)
        recyclerView?.grid(4)?.setup {
            addType<DraggablePhotoAlbumData>(R.layout.item_drag_photo_album)
            onCreate {
                val layoutItem = findView<LinearLayout>(R.id.layoutItem)
                layoutItem.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) { // 如果手指按下则开始拖拽
                        itemTouchHelper?.startDrag(this)
                    }
                    return@setOnTouchListener true
                }
            }
            onBind {
                val textView = findView<TextView>(R.id.item_text)
                textView.text = getModel<DraggablePhotoAlbumData>().data
            }
            itemTouchHelper = ItemTouchHelper(object : DefaultItemTouchCallback() {

                /**
                 * 当拖拽动作完成且松开手指时触发
                 */
                override fun onDrag(
                    source: BindingAdapter.BindingViewHolder,
                    target: BindingAdapter.BindingViewHolder
                ) {
                    super.onDrag(source, target)
                }

            })

        }?.models = data
    }

    /**
     * 分页加载相册照片
     * @param context 上下文
     * @param page 页码 (从 0 开始)
     * @param limit 每页加载数量
     */
    fun loadPagedPhotos(context: Context, page: Int, limit: Int): List<PhotoItem> {
        val photoList = mutableListOf<PhotoItem>()

        // 1. 定义要查询的 URI
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // 2. 定义查询的字段
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )

        // 3. 定义分页参数：OFFSET 和 LIMIT
        val offset = page * limit

        // 4. 定义排序和分页子句
        // 排序：按时间降序 (最新的在前面)
        // 分页：附加 LIMIT 和 OFFSET
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $limit OFFSET $offset"

        context.contentResolver.query(
            collection,
            projection,
            null,  // selection
            null,  // selectionArgs
            sortOrder // 排序和分页
        )?.use { cursor ->
            // 5. 获取列索引
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            // 6. 遍历 Cursor
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)

                // 构造完整的 URI
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                ).toString()

                photoList.add(PhotoItem(id, contentUri, name))
            }
        }
        return photoList
    }


}