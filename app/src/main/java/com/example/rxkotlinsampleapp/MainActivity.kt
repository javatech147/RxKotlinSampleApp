package com.example.rxkotlinsampleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rxkotlinsampleapp.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

/*
* flatMap() operator
* flatMap() operator behaves very much like map()
* The difference from map() is that the function it applies returns an Observable.
* Official doc - transform the items emitted by an Observable into Observables, then
* flatten the emissions from those into a single Observable.
*
* When you want to convert item emitted to another type, prefer map() operator.
* If you want a non-observable object then you just use map() operator.
* If you want an Observable object then you use flatMap() operator.
* flatMap() unwraps the Observable, picks the returned object and wraps it with its own Observable and emits it.
* i.e. map() takes and item and returns an item, but flatMap() takes an item and returns Observable.
*
* LogOutput -
* D/MainActivity: onNext: Email-student1@gmail.com Name: Sehwag
* D/MainActivity: onNext: Email-student2@gmail.com Name: Dhoni
* D/MainActivity: onNext: Email-student3@gmail.com Name: Sachin
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
            }
            emitter.onComplete()
        }

        compositeDisposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .map { student ->
//                    Student(student.id, student.name.uppercase(), student.email)
//                }
                .flatMap { student ->
                    Student(student.id, student.name.uppercase(), student.email)
                    Observable.just(student)
                }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<Student> {
        disposableObserver = object : DisposableObserver<Student>() {
            override fun onNext(item: Student) {
                Log.d(TAG, "onNext: Email-${item.email} Name: ${item.name}")
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
