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
* concatMap() operator
* concatMap() operator is similar to flatMap() operator.
* flatMap() doesn't preserves the order of the elements.
* concatMap() cares about the order of the elements.
* The difference from flatMap() is that concatMap() maintains order.
* So if you want to maintain order, use concatMap() otherwise use flatMap()
*
* LogOutput -
* D/MainActivity: onNext: Email-student1@gmail.com Name: Sehwag
* D/MainActivity: onNext: Email-student1@gmail.com Name: Sehwag
* D/MainActivity: onNext: Email-student2@gmail.com Name: Dhoni
* D/MainActivity: onNext: Email-student2@gmail.com Name: Dhoni
* D/MainActivity: onNext: Email-student3@gmail.com Name: Sachin
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

                // Here flatMap() operator returns 3 Observable, each emits 2 Student objects.
                .concatMap { student ->
                    val studentNew = Student(student.id, student.name, student.email)
                    Student(student.id, student.name.uppercase(), student.email)
                    Observable.just(student, studentNew)
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
