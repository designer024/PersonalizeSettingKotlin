package com.leapsy.personalizesettingkotlin.recyclerAdapterViewHolders.tools

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener {
    companion object {
        var firstVisiblePosition = 0
        var lastVisiblePosition = 0

        fun setRecyclerViewScrollEventListener(aRecyclerView : RecyclerView, aLayoutManager : LinearLayoutManager, aScrollUpButton : ImageButton, aScrollDownButton : ImageButton) {
            aRecyclerView.setOnScrollChangeListener(object : View.OnScrollChangeListener{
                override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                    firstVisiblePosition = 0
                    lastVisiblePosition = 0

                    val firstVisibleItem : Int = aLayoutManager.findFirstCompletelyVisibleItemPosition()
                    val lastVisibleItem : Int = aLayoutManager.findLastCompletelyVisibleItemPosition()

                    aScrollUpButton.isEnabled = firstVisibleItem > 1
                    aScrollDownButton.isEnabled = lastVisibleItem < aLayoutManager.itemCount - 1

                    if (firstVisibleItem >= 0) {
                        firstVisiblePosition = firstVisibleItem
                    }
                    if (lastVisibleItem >= 0) {
                        lastVisiblePosition = lastVisibleItem
                    }
                }
            })
        }

        fun scrollUpRecyclerView(aRecyclerView : RecyclerView, aLayoutManager : LinearLayoutManager) {
            if (firstVisiblePosition == 0) return
            var nextViewPosition : Int = if (firstVisiblePosition - 4 >= 0) firstVisiblePosition - 4 else 1
            aRecyclerView.smoothScrollToPosition(nextViewPosition)
        }

        fun scrollDownRecyclerView(aRecyclerView : RecyclerView, aLayoutManager : LinearLayoutManager) {
            if (lastVisiblePosition == 0) return
            var nextViewPosition : Int = if (lastVisiblePosition + 4 < aLayoutManager.itemCount) lastVisiblePosition + 4 else lastVisiblePosition + 1
            aRecyclerView.smoothScrollToPosition(nextViewPosition)
        }
    }
}