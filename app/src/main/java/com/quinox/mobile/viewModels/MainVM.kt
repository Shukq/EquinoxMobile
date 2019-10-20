package com.quinox.mobile.viewModels

import android.icu.util.Output
import androidx.annotation.NonNull
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface MainVM {
    interface Inputs{
        fun signInBtnPressed()
        fun signUpBtnPressed()
    }
    interface Outputs{
        fun signInNav():Observable<Unit>
        fun signUpNav():Observable<Unit>

    }
    class ViewModel(@NonNull val environment: Environment): ActivityViewModel<MainVM>(environment), Inputs, Outputs{

        val input:Inputs = this
        val output: Outputs = this

        private val signInBtnPressed = PublishSubject.create<Unit>()
        private val signUpBtnPressed = PublishSubject.create<Unit>()

        private val signInNav = BehaviorSubject.create<Unit>()
        private val signUpNav = BehaviorSubject.create<Unit>()

        init {
            signInBtnPressed
                .subscribe(signInNav)
            signUpBtnPressed
                .subscribe(signUpNav)
        }

        override fun signInBtnPressed() {
            return signInBtnPressed.onNext(Unit)
        }

        override fun signUpBtnPressed() {
            return signUpBtnPressed.onNext(Unit)
        }

        override fun signInNav(): Observable<Unit> = this.signInNav

        override fun signUpNav(): Observable<Unit> = this.signUpNav

    }
}