package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import com.quinox.domain.entities.UserStateResult
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface GetSessionVM {
    interface Inputs {
        fun onCreate()
    }
    interface Outputs {
        fun signedInAction(): Observable<Unit>
        fun signedOutAction(): Observable<Unit>
    }

    class ViewModel(@NonNull val environment: Environment): ActivityViewModel<ViewModel>(environment),Inputs, Outputs{

        val inputs : Inputs = this
        val outputs : Outputs = this

        //inputs
        private val onCreate = PublishSubject.create<Unit>()
        //outputs
        private val signedInAction = BehaviorSubject.create<Unit>()
        private val signedOutAction = BehaviorSubject.create<Unit>()

        init {
            val session = onCreate
                .flatMap { environment.authenticationUseCase().getCurrentUserState() }
                .share()
            session
                .filter { it == UserStateResult.signedOut }
                .map { Unit }
                .subscribe(this.signedOutAction)

            session
                .filter {  it == UserStateResult.signedIn }
                .map { Unit }
                .subscribe(this.signedInAction)
        }

        override fun onCreate() {
            return this.onCreate.onNext(Unit)
        }

        override fun signedInAction(): Observable<Unit> = this.signedInAction

        override fun signedOutAction(): Observable<Unit> = this.signedOutAction

    }
}