package com.wit.hillforts.views.login


import com.google.firebase.auth.FirebaseAuth
import com.wit.hillforts.models.firebase.HillfortFireStore
import com.wit.hillforts.views.BasePresenter
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.VIEW
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread


class LoginPresenter(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var fireStore: HillfortFireStore? = null

    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }
    }


    fun doLogin(email: String, password: String) {
        view?.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchHillforts {
                        view?.hideProgress()
                        view?.navigateTo(VIEW.LIST)
                    }
                } else {
                    view?.toast("Sign Up Failed: ${task.exception?.message}")
                }
                view?.hideProgress()
            }
        }
    }

        fun doSignUp(email: String, password: String) {
            view?.showProgress()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(view!!) { task ->
                    if (task.isSuccessful) {
                        view?.navigateTo(VIEW.LIST)
                    } else {
                        view?.toast("Sign Up Failed: ${task.exception?.message}")
                    }
                    view?.hideProgress()
                }
        }


    }
