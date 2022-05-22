package com.example.rxkotlinsampleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rxkotlinsampleapp.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers

/*
* just() operators emits the objects once.
* whether it is list just() operator will emit all list items at once.
* It will not emit list items one by one.
* LogOutput - D/MainActivity: onNext: [Hello A, Hello B, Hello C]
* */

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private val helloWorld = arrayListOf("Hello A", "Hello B", "Hello C")
    private lateinit var observable: Observable<List<String>>

    private lateinit var disposableObserver: DisposableObserver<List<String>>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observable = Observable.just(helloWorld)

        compositeDisposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<List<String>> {
        disposableObserver = object : DisposableObserver<List<String>>() {
            override fun onNext(@NonNull str: @NonNull List<String>?) {
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
