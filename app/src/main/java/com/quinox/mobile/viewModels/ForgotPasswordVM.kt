package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import com.quinox.domain.entities.Result
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.extensions.takeWhen
import com.quinox.mobile.libs.Environment
import com.quinox.mobile.utils.ValidationService
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface ForgotPasswordVM {
    interface Inputs{
        fun usernameChanged(username: String)
        fun nextButtonPressed()
    }
    interface Outputs{
        fun nextButtonEnabled() : Observable<Boolean>
        fun loadingEnabled() : Observable<Boolean>
        fun showError() : Observable<String>
        fun forgotPasswordStatus() : Observable<String>
    }
    class ViewModel(@NonNull val environment: Environment): ActivityViewModel<ForgotPasswordVM>(environment), Inputs, Outputs{
        val inputs: Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val usernameChanged = BehaviorSubject.create<String>()
        private val nextButtonPressed = PublishSubject.create<Unit>()

        //Outputs
        private var nextButtonEnabled = BehaviorSubject.create<Boolean>()
        private var loadingEnabled = BehaviorSubject.create<Boolean>()
        private var showError = PublishSubject.create<String>()
        private var forgotPasswordStatus = PublishSubject.create<String>()

        init {

            usernameChanged
                .map { ValidationService.isValidEmail(it) }
                .subscribe(nextButtonEnabled)

            val forgotPasswordAction = usernameChanged
                .takeWhen(nextButtonPressed)
                .flatMap { result ->
                    return@flatMap this.initForgotPassword(result.second)
                }
                .share()

            forgotPasswordAction
                .filter { it.isFail() }
                .map { "Ocurrió un error con el servidor, intente de nuevo más tarde" }
                .subscribe(this.showError)

            forgotPasswordAction
                .filter{!it.isFail()}
                .map { it.successValue() }
                .subscribe(this.forgotPasswordStatus)
        }

        override fun usernameChanged(username: String) = this.usernameChanged.onNext(username)

        override fun nextButtonPressed() = this.nextButtonPressed.onNext(Unit)

        override fun nextButtonEnabled(): Observable<Boolean> = this.nextButtonEnabled

        override fun loadingEnabled(): Observable<Boolean> = this.loadingEnabled

        override fun showError(): Observable<String> = this.showError

        override fun forgotPasswordStatus(): Observable<String> = this.forgotPasswordStatus

        private fun initForgotPassword(email: String): Observable<Result<String>>{
            return environment.authenticationUseCase().initForgotPassword(email)
                .doOnComplete { this.loadingEnabled.onNext(false) }
                .doOnSubscribe { this.loadingEnabled.onNext(true) }
        }
    }
}