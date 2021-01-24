package com.wit.hillforts.views.hillfortlist


import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wit.hillforts.helpers.SwipeToDeleteCallback
import com.wit.hillforts.helpers.SwipeToEditCallback
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.views.BasePresenter
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.VIEW
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.*

class HillfortListPresenter(view: BaseView) : BasePresenter(view) {

    var favCheck = false


    init {
        if (view.intent.hasExtra("Fav")) {
            favCheck = true
        }

    }

    fun loadHillforts() {
        doAsync {
            val hillforts = app.hillforts.findAll()
            var favHillforts = mutableListOf<HillfortModel>()
            uiThread {
                if (favCheck) {
                    for (element in hillforts) {
                        if (element.fav == true) {
                            favHillforts.add(element)
                        }
                    }
                    view?.showHillforts(favHillforts)
                    if (favHillforts.isEmpty()) {
                        view!!.none.setVisibility(View.VISIBLE)
                        view!!.none.setText("No Favourites")
                    }
                } else {
                    view?.showHillforts(hillforts)
                    if (hillforts.isEmpty()) {
                        view!!.none.setVisibility(View.VISIBLE)
                        view!!.none.setText("No Hillforts")
                    }
                }
            }
        }
    }


    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT)

    }


    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "hillfort_edit", hillfort)
    }

    fun doShowSettings() {
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun doFav() {
        view?.navigateTo(VIEW.FAVS, 0, "Fav")
    }

    fun doMap() {
        view?.navigateTo(VIEW.MAP)
    }


    fun doSwipeHandler() {

        val swipeDeleteHandler = object : SwipeToDeleteCallback(view!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var hillfort =   view?.findById(viewHolder.itemView.tag as Long)
                swipeDelete(hillfort!!)

            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(view!!.recyclerView)


        val swipeEditHandler = object : SwipeToEditCallback(view!!) {
             override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               var hillfort =   view?.findById(viewHolder.itemView.tag as Long)
               doEditHillfort(hillfort!!)
                        }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(view!!.recyclerView)

    }

    fun doFindById(id: Long): HillfortModel? {
        return app.hillforts.findById(id)
    }

    fun swipeDelete(hillfort: HillfortModel) {
        doAsync {
            app.hillforts.delete(hillfort)
            uiThread {
                if (favCheck) doFav()
                else view?.navigateTo(VIEW.LIST)
            }
        }
    }
}
