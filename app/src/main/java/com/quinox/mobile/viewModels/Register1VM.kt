package com.quinox.mobile.viewModels

import android.util.Log
import androidx.annotation.NonNull
import com.quinox.domain.entities.Gender
import com.quinox.domain.entities.SignUpModel
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.extensions.takeWhen
import com.quinox.mobile.libs.Environment
import com.quinox.mobile.utils.ValidationService
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface Register1VM {
    interface Inputs {
        fun username(username: String)
        fun email(email: String)
        fun occupation(occupation: String)
        fun datePressed()
        fun gender(gender: Gender)
        fun dateActionReceive(date: String)
        fun nextButtonPressed()
    }
    interface Outputs {
        fun dateTextChanged(): Observable<String>
        fun openDatePicker(): Observable<Unit>
        fun nextButtonIsEnabled(): Observable<Boolean>
        //fun showError(): Observable<String>
        fun register1Action(): Observable<SignUpModel>
    }

    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<Register1VM>(environment), Inputs, Outputs{


        //Inputs
        private val usernameEditTextChanged =  PublishSubject.create<String>()
        private val emailEditTextChanged =  PublishSubject.create<String>()
        private val occupationEditTextChanged =  PublishSubject.create<String>()
        private val dateButtonPressed =  PublishSubject.create<Unit>()
        private val genderSpinnerChanged =  PublishSubject.create<Gender>()
        private val dateActionReceive = PublishSubject.create<String>()
        private val nextButtonPressed = PublishSubject.create<Unit>()

        //Outputs
        private val dateTextChanged =  BehaviorSubject.create<String>()
        private val openDatePicker = BehaviorSubject.create<Unit>()
        private val nextButtonIsEnabled = BehaviorSubject.create<Boolean>()
        //private val showError = BehaviorSubject.create<String>()
        private val register1Action = BehaviorSubject.create<SignUpModel>()

        val inputs : Inputs = this
        val outputs : Outputs = this

        init {
            dateButtonPressed
                .subscribe(openDatePicker)
            dateActionReceive
                .subscribe(dateTextChanged)
            val form = Observables.combineLatest(usernameEditTextChanged, emailEditTextChanged, occupationEditTextChanged)

            form
                .map {
                    val date = dateTextChanged.value
                    return@map ValidationService.validateUserRegister(it.first, it.second, it.third, date)
                }
                .subscribe(nextButtonIsEnabled)
            val data = Observables.combineLatest(form, genderSpinnerChanged)

            data
                .takeWhen(nextButtonPressed)
                .map {
                    val date = dateTextChanged.value
                    return@map SignUpModel(it.second.first.first, it.second.first.second, it.second.first.third, date, it.second.second)
                }
                .subscribe(register1Action)

        }
        override fun username(username: String) = this.usernameEditTextChanged.onNext(username)

        override fun email(email: String) = this.emailEditTextChanged.onNext(email)

        override fun occupation(occupation: String) = this.occupationEditTextChanged.onNext(occupation)

        override fun datePressed() = this.dateButtonPressed.onNext(Unit)

        override fun gender(gender: Gender) = this.genderSpinnerChanged.onNext(gender)

        override fun dateActionReceive(date: String) = this.dateActionReceive.onNext(date)

        override fun nextButtonPressed() = this.nextButtonPressed.onNext(Unit)

        override fun dateTextChanged(): Observable<String> = this.dateTextChanged

        override fun openDatePicker(): Observable<Unit> = this.openDatePicker

        override fun nextButtonIsEnabled(): Observable<Boolean> = this.nextButtonIsEnabled

        //override fun showError(): Observable<String> = this.showError

        override fun register1Action(): Observable<SignUpModel> = this.register1Action
    }
}