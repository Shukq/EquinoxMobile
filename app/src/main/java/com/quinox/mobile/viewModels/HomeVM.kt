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
        fun onCreate()
    }
    interface Outputs {
        fun signOutAction() : Observable<Unit>
        fun profileInfo() : Observable<List<Pair<String, String>>>
    }
    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<HomeVM>(environment), Inputs, Outputs{

        val inputs : Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val signOutButtonPressed = PublishSubject.create<Unit>()
        private val onCreate = PublishSubject.create<Unit>()

        //Outputs
        private val signOutAction = BehaviorSubject.create<Unit>()
        private val profileInfo = BehaviorSubject.create<List<Pair<String, String>>>()

        init {

            val signOutEvent = signOutButtonPressed
                .flatMap { this.signOut() }
                .share()
            signOutEvent
                .map {  return@map  }
                .subscribe(this.signOutAction)

            val profileEvent = onCreate
                .flatMap { getProfile() }
                .share()

            profileEvent
                .subscribe(profileInfo)



        }

        override fun signOutButtonPressed() {
            return this.signOutButtonPressed.onNext(Unit)
        }

        override fun onCreate() {
            return this.onCreate.onNext(Unit)
        }

        override fun profileInfo(): Observable<List<Pair<String, String>>> = this.profileInfo

        override fun signOutAction(): Observable<Unit> = this.signOutAction

        private fun signOut() : Observable<Result<UserStateResult>>{
            return environment.authenticationUseCase().signOut()
        }

        //helpers
        private fun getProfile() : Observable<List<Pair<String, String>>>{
            return environment.authenticationUseCase().getAttributes()
        }
    }
}