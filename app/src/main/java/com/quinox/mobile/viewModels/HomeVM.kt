package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import com.quinox.domain.entities.Result
import com.quinox.domain.entities.UserStateResult
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface HomeVM {
    interface Inputs {
        fun signOutButtonPressed()
    }
    interface Outputs {
        fun signOutAction() : Observable<Unit>
    }
    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<HomeVM>(environment), Inputs, Outputs{

        val inputs : Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val signOutButtonPressed = PublishSubject.create<Unit>()

        //Outputs
        private val signOutAction = BehaviorSubject.create<Unit>()

        init {

            val signOutEvent = signOutButtonPressed
                .flatMap { this.signOut() }
                .share()
            signOutEvent
                .map {  return@map  }
                .subscribe(this.signOutAction)

        }

        override fun signOutButtonPressed() {
            return this.signOutButtonPressed.onNext(Unit)
        }

        override fun signOutAction(): Observable<Unit> = this.signOutAction

        private fun signOut() : Observable<Result<UserStateResult>>{
            return environment.authenticationUseCase().signOut()
        }
    }
}