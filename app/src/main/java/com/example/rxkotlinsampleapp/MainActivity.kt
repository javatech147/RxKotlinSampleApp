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
* just() operators emits the objects once.
* whether it is list just() operator will emit all list items at once.
* If you want to emits item one by one, then you need to pass each items separated by comma to the just() operator.
* Ex - observable = Observable.just("Hello A", "Hello B", "Hello C")
* In this case it requires - Observable<String>, Disposable<String>
* If you pass list or iterable to just() operator, it will emits all items at once like below -
* observable = Observable.just(namesList)
* If you pass each item to just() operator, it will emit item one by one.
* Log Output -
* D/MainActivity: onNext: Hello A
* D/MainActivity: onNext: Hello B
* D/MainActivity: onNext: Hello C
* */

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var observable: Observable<String>

    private lateinit var disposableObserver: DisposableObserver<String>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observable = Observable.just("Hello A", "Hello B", "Hello C")

        compositeDisposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<String> {
        disposableObserver = object : DisposableObserver<String>() {
            override fun onNext(str: String?) {
                Log.d(TAG, "onNext: $str")
            }

            override fun onError(e: Throwable?) = Unit

            override fun onComplete() = Unit
        }
        return disposableObserver
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
