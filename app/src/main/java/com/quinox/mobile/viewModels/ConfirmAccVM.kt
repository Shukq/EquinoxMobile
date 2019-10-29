package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import com.quinox.domain.entities.Result
import com.quinox.domain.entities.SignInResult
import com.quinox.domain.entities.SignUpModel
import com.quinox.domain.entities.SignUpResult
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.extensions.takeWhen
import com.quinox.mobile.libs.Environment
import com.quinox.mobile.utils.ValidationService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.lang.Exception

interface ConfirmAccVM {

    interface Inputs{
        fun code(code : String)
        fun acceptButtonPressed()
        fun resendButtonPressed()
    }

    interface Outputs{
        fun acceptButtonEnabled() : Observable<Boolean>
        fun showError() : Observable<String>
        fun loadingEnabled() : Observable<Boolean>
        fun resendButtonAction(): Observable<Boolean>
        fun acceptButtonAction(): Observable<Unit>
    }
    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<ConfirmAccVM>(environment), Inputs, Outputs{

        val inputs : Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val codeEditTextChanged = PublishSubject.create<String>()
        private val acceptButtonPressed = PublishSubject.create<Unit>()
        private val resendButtonPressed = PublishSubject.create<Unit>()

        //Outputs
        private val acceptButtonEnabled = BehaviorSubject.create<Boolean>()
        private val loadingEnabled = BehaviorSubject.create<Boolean>()
        private val showError = BehaviorSubject.create<String>()
        private val resendAccAction = BehaviorSubject.create<Boolean>()
        private val acceptAccAction = BehaviorSubject.create<Unit>()

        init{
            val model  = intent()
                .filter { it.hasExtra("SignUpResult") }
                .map {
                    return@map it.getSerializableExtra("SignUpResult") as SignUpResult
                }

            codeEditTextChanged
                .map { ValidationService.validateVerificationCode(it) }
                .subscribe(acceptButtonEnabled)

            val resendEvent = model
                .takeWhen(resendButtonPressed)
                .map { it.second.username }
                .flatMap { return@flatMap resendCode(it) }
                .share()

            resendEvent
                .filter { it.isFail() }
                .map { return@map "No se pudo enviar el codigo" }
                .subscribe(showError)
            resendEvent
                .filter { !it.isFail() }
                .map { return@map it.successValue() }
                .subscribe(resendAccAction)

            val modelAndCode = Observables
                .combineLatest(model,codeEditTextChanged)

            val confirmEvent = modelAndCode
                .takeWhen(acceptButtonPressed)
                .map { it.second }
                .flatMap { return@flatMap codeConfirmAction(it.first,it.second) }
                .share()

            confirmEvent
                .filter { it.isFail() }
                .map { return@map "No se pudo confirmar su cuenta" }
                .subscribe(showError)
            confirmEvent
                .filter { !it.isFail() }
                .map { return@map it.successValue() }
                .map { Unit }
                .subscribe(acceptAccAction)
        }

        override fun code(code: String) = this.codeEditTextChanged.onNext(code)

        override fun acceptButtonPressed() = this.acceptButtonPressed.onNext(Unit)

        override fun resendButtonPressed() = this.resendButtonPressed.onNext(Unit)

        override fun acceptButtonEnabled(): Observable<Boolean> = this.acceptButtonEnabled

        override fun showError(): Observable<String> = this.showError

        override fun loadingEnabled(): Observable<Boolean> = this.loadingEnabled

        override fun resendButtonAction(): Observable<Boolean> = this.resendAccAction

        override fun acceptButtonAction(): Observable<Unit> = this.acceptAccAction

        private fun codeConfirmAction(model: SignUpResult, code: String) : Observable<Result<SignInResult>>{
            return Observable.create<Result<SignInResult>>create@{observer ->
                val verifyEvent = environment.authenticationUseCase().confirmSignUp(model.username,code)
                    .share()
                verifyEvent
                    .filter { it.isFail() }
                    .subscribe{
                        observer.onNext(Result.failure(Exception()))
                        observer.onComplete()
                    }
                val signInEvent = verifyEvent
                    .filter { !it.isFail() }
                    .flatMap { environment.authenticationUseCase().signIn(model.username, model.password)}
                    .share()
                signInEvent
                    .filter { it.isFail() }
                    .subscribe {
                        observer.onNext(Result.failure(Exception()))
                        observer.onComplete()
                    }
                signInEvent
                    .filter{ !it.isFail()}
                    .map { return@map it.successValue() }
                    .subscribe{
                        observer.onNext(Result.success(it))
                        observer.onComplete()
                    }
            }
                .doOnComplete { this.loadingEnabled.onNext(false) }
                .doOnSubscribe { this.loadingEnabled.onNext(true) }
        }

        private fun resendCode(user: String): Observable<Result<Boolean>>{
            return environment.authenticationUseCase().resendConfirmationCode(user)
        }

    }
}