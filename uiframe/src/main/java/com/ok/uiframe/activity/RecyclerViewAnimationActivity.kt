package com.ok.uiframe.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.max.uiframe.R
import com.ok.uiframe.adapter.GiftAdapter
import com.ok.uiframe.adapter.GiftItemAnimator
import com.ok.uiframe.data.Gift
import java.util.LinkedList
import java.util.Queue


class RecyclerViewAnimationActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var giftAdapter: GiftAdapter? = null
    private var giftList: List<Gift>? = null
    private val giftQueue: Queue<Gift> = LinkedList<Gift>()
    private val handler: Handler = Handler()
    val MAX_DISPLAY_COUNT: Int = 3
    val GIFT_DISPLAY_DURATION: Int = 3000 // 3 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_animation)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        giftList = ArrayList()
        giftAdapter = GiftAdapter(giftList)
        recyclerView?.adapter = giftAdapter
        recyclerView?.setLayoutManager(LinearLayoutManager(this))


        // Set custom ItemAnimator
        recyclerView?.setItemAnimator(GiftItemAnimator())


        // Simulate receiving gifts
        simulateGiftReceiving()
    }

    private fun simulateGiftReceiving() {
        // Simulate receiving gifts at intervals
        handler.postDelayed(object : Runnable {
            override fun run() {
                val gift = Gift("Gift " + (giftList!!.size + 1), R.drawable.ic_gift)
                if (giftList!!.size < MAX_DISPLAY_COUNT) {
                    giftAdapter!!.addGift(gift)
                    startGiftRemovalTimer()
                } else {
                    giftQueue.add(gift)
                }
                handler.postDelayed(this, 1000) // Add new gift every second
            }
        }, 1000)
    }

    private fun startGiftRemovalTimer() {
        handler.postDelayed({
            if (!giftList!!.isEmpty()) {
                giftAdapter!!.removeGift(0)
                if (!giftQueue.isEmpty()) {
                    val nextGift = giftQueue.poll()
                    giftAdapter!!.addGift(nextGift)
                    startGiftRemovalTimer()
                }
            }
        }, GIFT_DISPLAY_DURATION.toLong())
    }
}