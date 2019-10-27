package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import androidx.lifecycle.Transformations.map
import com.quinox.domain.entities.Result
import com.quinox.domain.entities.SignUpModel
import com.quinox.domain.entities.SignUpResult
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.extensions.takeWhen
import com.quinox.mobile.libs.Environment
import com.quinox.mobile.utils.ValidationService.validatePassword
import com.quinox.mobile.utils.ValidationService.validatePasswordCapitalLowerLetters
import com.quinox.mobile.utils.ValidationService.validatePasswordLenght
import com.quinox.mobile.utils.ValidationService.validatePasswordNumbers
import com.quinox.mobile.utils.ValidationService.validatePasswordSpecialCharacters
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Register2VM {
    interface Inputs{
        fun password(password : String)
        fun signUpButtonPressed()
    }
    interface Outputs{
        fun signUpButtonEnabled() : Observable<Boolean>
        fun validPasswordLenght() : Observable<Boolean>
        fun validPasswordCapitalLowerLetters() : Observable<Boolean>
        fun validPasswordNumbers() : Observable<Boolean>
        fun validPasswordSpecialCharacters() : Observable<Boolean>
        fun showError() : Observable<String>
        fun loadingEnabled() : Observable<Boolean>
        fun confirmAccAction() : Observable<SignUpResult>
    }
    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<Register2VM>(environment), Inputs, Outputs {


        val inputs : Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val passwordEditTextChanged = PublishSubject.create<String>()
        private val signUpButtonPressed = PublishSubject.create<Unit>()

        //Outputs
        private val signUpButtonEnabled = BehaviorSubject.create<Boolean>()
        private val validPasswordCapitalLowerLetters = BehaviorSubject.create<Boolean>()
        private val validPasswordLenght = BehaviorSubject.create<Boolean>()
        private val validPasswordNumbers = BehaviorSubject.create<Boolean>()
        private val validPasswordSpecialCharacters = BehaviorSubject.create<Boolean>()
        private val loadingEnabled = BehaviorSubject.create<Boolean>()
        private val showError = BehaviorSubject.create<String>()
        private val confirmAccAction = BehaviorSubject.create<SignUpResult>()

        init {
            val validPassword = passwordEditTextChanged
                    .map { validatePassword(it) }
            validPassword.subscribe(signUpButtonEnabled)

            passwordEditTextChanged
                .map {validatePasswordLenght(it)}
                .subscribe(this.validPasswordLenght)

            passwordEditTextChanged
                .map { validatePasswordNumbers(it)}
                .subscribe(this.validPasswordNumbers)

            passwordEditTextChanged
                .map { validatePasswordCapitalLowerLetters(it)}
                .subscribe(this.validPasswordCapitalLowerLetters)

            passwordEditTextChanged
                .map { validatePasswordSpecialCharacters(it)}
                .subscribe(this.validPasswordSpecialCharacters)

            val model  = intent()
                .filter { it.hasExtra("model") }
                .map {
                    return@map it.getSerializableExtra("model") as SignUpModel
                }


            val modelAndPassword = Observables.combineLatest(passwordEditTextChanged,model)

            val signUpEvent = modelAndPassword
                .takeWhen(signUpButtonPressed)
                .map { it.second }
                .flatMap { return@flatMap this.signUpAction(it.second, it.first) }
                .share()

            signUpEvent
                .filter { it.isFail() }
                .map { return@map "No se pudo registrar la cuenta" }
                .subscribe(showError)
            signUpEvent
                .filter { !it.isFail() }
                .map { return@map it.successValue() }
                .subscribe(confirmAccAction)
        }


        override fun signUpButtonPressed() = this.signUpButtonPressed.onNext(Unit)

        override fun password(password: String){
            return this.passwordEditTextChanged.onNext(password)
        }

        override fun signUpButtonEnabled(): Observable<Boolean> = this.signUpButtonEnabled

        override fun validPasswordLenght(): Observable<Boolean> = this.validPasswordLenght

        override fun validPasswordCapitalLowerLetters(): Observable<Boolean> = this.validPasswordCapitalLowerLetters

        override fun validPasswordNumbers(): Observable<Boolean> = this.validPasswordNumbers

        override fun validPasswordSpecialCharacters(): Observable<Boolean> = this.validPasswordSpecialCharacters

        override fun showError(): Observable<String> = this.showError

        override fun loadingEnabled(): Observable<Boolean> = this.loadingEnabled

        override fun confirmAccAction(): Observable<SignUpResult> = this.confirmAccAction

        private fun signUpAction(model: SignUpModel, password: String) : Observable<Result<SignUpResult>>{
            return environment.authenticationUseCase().signUp(model.email,password,model.name,model.gender)
                .doOnComplete { this.loadingEnabled.onNext(false) }
                .doOnSubscribe { this.loadingEnabled.onNext(true) }
        }
    }


}