package com.ok.uiframe.data

import com.drake.brv.annotaion.ItemOrientation
import com.drake.brv.item.ItemDrag

data class DragData(
    var data:String = "", override var itemOrientationDrag: Int = ItemOrientation.ALL
):ItemDrag
