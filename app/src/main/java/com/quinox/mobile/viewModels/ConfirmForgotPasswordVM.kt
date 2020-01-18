package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import com.quinox.domain.entities.Result
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.extensions.takeWhen
import com.quinox.mobile.libs.Environment
import com.quinox.mobile.utils.ValidationService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.regex.Pattern

interface ConfirmForgotPasswordVM {
    interface Inputs {
        fun passwordChanged(password: String)
        fun confirmationCodeTextChanged(verificationCode: String)
        fun nextButtonPressed()
    }

    interface Outputs {
        fun nextButtonEnabled() : Observable<Boolean>
        fun passwordChangedAction() : Observable<Boolean>
        fun showError() : Observable<String>
        fun loadingEnabled() : Observable<Boolean>

        fun validPasswordLenght() : Observable<Boolean>
        fun validPasswordCapitalLowerLetters() : Observable<Boolean>
        fun validPasswordNumbers() : Observable<Boolean>
        fun validPasswordSpecialCharacters() : Observable<Boolean>
    }

    class ViewModel(@NonNull val environment: Environment): ActivityViewModel<ConfirmForgotPasswordVM>(environment), Inputs, Outputs {
        val inputs : Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val passwordChanged = PublishSubject.create<String>()
        private val confirmationCodeTextChanged = PublishSubject.create<String>()
        private val nextButtonPressed = PublishSubject.create<Unit>()

        //Outputs
        private val nextButtonEnabled = BehaviorSubject.create<Boolean>()
        private val validPasswordCapitalLowerLetters = BehaviorSubject.create<Boolean>()
        private val validPasswordLenght = BehaviorSubject.create<Boolean>()
        private val validPasswordNumbers = BehaviorSubject.create<Boolean>()
        private val validPasswordSpecialCharacters = BehaviorSubject.create<Boolean>()
        private val passwordChangedAction = PublishSubject.create<Boolean>()
        private val showError = PublishSubject.create<String>()
        private val loadingEnabled = BehaviorSubject.create<Boolean>()

        init {

            val formData = Observables.combineLatest(
                confirmationCodeTextChanged,passwordChanged)

            formData
                .map { ValidationService.validateConfirmForgotPassword(it.first,it.second) }
                .subscribe(this.nextButtonEnabled)

            passwordChanged
                .map {validatePasswordLenght(it)}
                .subscribe(this.validPasswordLenght)

            passwordChanged
                .map { validatePasswordNumbers(it)}
                .subscribe(this.validPasswordNumbers)

            passwordChanged
                .map { validatePasswordCapitalLowerLetters(it)}
                .subscribe(this.validPasswordCapitalLowerLetters)

            passwordChanged
                .map { validatePasswordSpecialCharacters(it)}
                .subscribe(this.validPasswordSpecialCharacters)

            val confirmEvent = formData
                .takeWhen(this.nextButtonPressed)
                .map { it.second }
                .flatMap { this.confirmForgotPassword(it.second, it.first) }
                .share()

            confirmEvent
                .filter { it.isFail() }
                .map { "Ocurrió un error con el servidor, intente de nuevo más tarde" }
                .subscribe(this.showError)

            confirmEvent
                .filter { !it.isFail() }
                .map { it.successValue() }
                .subscribe(this.passwordChangedAction)


        }

        override fun passwordChanged(password: String) = this.passwordChanged.onNext(password)

        override fun confirmationCodeTextChanged(verificationCode: String) = this.confirmationCodeTextChanged.onNext(verificationCode)

        override fun nextButtonPressed() = this.nextButtonPressed.onNext(Unit)

        override fun nextButtonEnabled(): Observable<Boolean> = this.nextButtonEnabled

        override fun passwordChangedAction(): Observable<Boolean> = this.passwordChangedAction

        override fun showError(): Observable<String> = this.showError

        override fun loadingEnabled(): Observable<Boolean> = this.loadingEnabled

        override fun validPasswordLenght(): Observable<Boolean> = this.validPasswordLenght

        override fun validPasswordCapitalLowerLetters(): Observable<Boolean> = this.validPasswordCapitalLowerLetters

        override fun validPasswordNumbers(): Observable<Boolean> = this.validPasswordNumbers

        override fun validPasswordSpecialCharacters(): Observable<Boolean> = this.validPasswordSpecialCharacters

        //Helpers
        private fun validatePasswordLenght(password : String) : Boolean{
            val minPasswordLenght = 8
            val maxPasswordLenght = 20
            if(password.length < minPasswordLenght || password.length > maxPasswordLenght){
                return false
            }
            return true
        }

        private fun validatePasswordCapitalLowerLetters(password: String) : Boolean {
            val pattern1 = Pattern.compile(".*[A-Z].*")
            val pattern1Sub = Pattern.compile(".*[a-z].*")
            if (pattern1.matcher(password).matches() && pattern1Sub.matcher(password).matches()){
                return true
            }
            return false
        }

        private fun validatePasswordNumbers(password: String): Boolean{
            val pattern2 = Pattern.compile(".*\\d.*")
            if(pattern2.matcher(password).matches()){
                return true
            }
            return false
        }

        private fun validatePasswordSpecialCharacters(password: String) : Boolean {
            val pattern3 = Pattern.compile(".*[!\$#@_.+-].*")
            if(pattern3.matcher(password).matches()){
                return true
            }
            return false
        }

        private fun confirmForgotPassword(password: String,code: String): Observable<Result<Boolean>>{
            return environment.authenticationUseCase().confirmForgotPassword(password,code)
                .doOnComplete { this.loadingEnabled.onNext(false) }
                .doOnSubscribe { this.loadingEnabled.onNext(true) }
        }
    }
}