package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import com.quinox.domain.entities.SignInResult
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import com.quinox.domain.entities.Result
import com.quinox.mobile.extensions.takeWhen
import com.quinox.mobile.utils.ValidationService

interface SignInVM {
    interface Inputs {
        fun username(username: String)
        fun password(password: String)
        fun signInButtonPressed()
        fun forgotPasswordPressed()

    }
    interface Outputs {
        fun loadingEnabled(): Observable<Boolean>
        fun showError() : Observable<String>
        fun signInButtonIsEnabled() : Observable<Boolean>
        fun signedInAction(): Observable<Unit>
        fun showConfirmationAlert() : Observable<Unit>
        fun forgotPasswordAction() : Observable<Unit>
    }

    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<SignInVM>(environment), Inputs, Outputs{


        //Inputs
        private val usernameEditTextChanged =  PublishSubject.create<String>()
        private val passwordEditTextChanged =  PublishSubject.create<String>()
        private val signInButtonPressed = PublishSubject.create<Unit>()
        private val forgotPasswordPressed = PublishSubject.create<Unit>()

        //Outputs
        private val signInButtonIsEnabled = BehaviorSubject.create<Boolean>()
        private val loadingEnabled = BehaviorSubject.create<Boolean>()
        private val showError = BehaviorSubject.create<String>()
        private val showConfirmationAlert = BehaviorSubject.create<Unit>()
        private val signedInAction = BehaviorSubject.create<Unit>()
        private val forgotPasswordAction = BehaviorSubject.create<Unit>()


        val inputs : Inputs = this
        val outputs : Outputs = this

        init {
            val usernameAndPassword =
                Observables.combineLatest(usernameEditTextChanged, passwordEditTextChanged)

            val valid = usernameAndPassword.map {
                ValidationService.validateUserCredentials(it.first, it.second)
            }
            valid.subscribe(this.signInButtonIsEnabled)

            val signInEvent : Observable<Result<SignInResult>> = usernameAndPassword
                .takeWhen(signInButtonPressed)
                .flatMap { result ->
                    return@flatMap this.signIn(result.second.first, result.second.second)
                }
                .share()

            signInEvent
                .filter { it.isFail() }
                .map { return@map "Ocurrió un error, favor intente de nuevo" }
                .subscribe(this.showError)

            /*
            showError
                .filter { it ->
                    ((it.error) as? SignInError).let {
                        when(it){
                            SignInError.userNotConfirmed-> return@filter true
                            else -> return@filter false
                        }
                    }
                }
                .map{ it -> Unit}
                .subscribe(this.showConfirmationAlert)*/


            signInEvent
                .filter { !it.isFail() }
                .map { return@map it.successValue() }
                .map { Unit }
                .subscribe(this.signedInAction)

            forgotPasswordPressed
                .subscribe(this.forgotPasswordAction)

        }
        override fun username(username: String) = this.usernameEditTextChanged.onNext(username)

        override fun password(password: String) = this.passwordEditTextChanged.onNext(password)

        override fun signInButtonPressed() = this.signInButtonPressed.onNext(Unit)

        override fun forgotPasswordPressed() = this.forgotPasswordPressed.onNext(Unit)

        override fun forgotPasswordAction(): Observable<Unit> = this.forgotPasswordAction

        override fun signInButtonIsEnabled(): Observable<Boolean> = this.signInButtonIsEnabled

        override fun loadingEnabled(): Observable<Boolean> = this.loadingEnabled

        override fun showError(): Observable<String> = this.showError

        override fun signedInAction(): Observable<Unit> = this.signedInAction

        override fun showConfirmationAlert(): Observable<Unit> = this.showConfirmationAlert


        private fun signIn(username: String, password: String) : Observable<Result<SignInResult>> {
            return environment.authenticationUseCase().signIn(username, password)
                .doOnComplete { this.loadingEnabled.onNext(false) }
                .doOnSubscribe { this.loadingEnabled.onNext(true) }
        }
    }
}