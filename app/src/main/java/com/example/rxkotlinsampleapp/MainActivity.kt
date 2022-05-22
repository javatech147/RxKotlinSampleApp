package com.example.rxkotlinsampleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rxkotlinsampleapp.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

/*
* create() operator
* If you want to control the items emission by yourself, then you can use create() operator.
* create() operator provides more control on data emission.
* We can decide what objects to emit and we can even change the data we are going to emit.
*     observable = Observable.create(...)
*
* LogOutput -
* D/MainActivity: create:onNext:student1@gmail.com
* D/MainActivity: create:onNext:student2@gmail.com
* D/MainActivity: create:onNext:student3@gmail.com
* D/MainActivity: onNext: student1@gmail.com
* D/MainActivity: onNext: student2@gmail.com
* D/MainActivity: onNext: student3@gmail.com
* D/MainActivity: onComplete:
* */

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var observable: Observable<Student>

    private lateinit var disposableObserver: DisposableObserver<Student>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        observable = Observable.create(object : ObservableOnSubscribe<Student> {
//            override fun subscribe(emitter: ObservableEmitter<Student>) {
//                val studentList = getStudentList()
//                for (student in studentList) {
//                    emitter.onNext(student)
//                }
//                emitter.onComplete()
//            }
//        })

        // lambda expression for above code.
        observable = Observable.create { emitter ->
            val studentList = getStudentList()
            for (student in studentList) {
                emitter.onNext(student)
                Log.d(TAG, "create:onNext:${student.email} ")
            }
            emitter.onComplete()
        }

        compositeDisposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<Student> {
        disposableObserver = object : DisposableObserver<Student>() {
            override fun onNext(item: Student) {
                Log.d(TAG, "onNext: ${item.email}")
            }

            override fun onError(e: Throwable?) = Unit

            override fun onComplete() {
                Log.d(TAG, "onComplete: ")
            }
        }
        return disposableObserver
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun getStudentList(): List<Student> {
        return arrayListOf(
            Student(10, "Sehwag", "student1@gmail.com"),
            Student(11, "Dhoni", "student2@gmail.com"),
            Student(12, "Sachin", "student3@gmail.com")
        )
    }
}
