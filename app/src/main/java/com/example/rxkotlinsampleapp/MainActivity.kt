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
* range() operators only works on Int types.
* It will emit list items one by one.
*     observable = Observable.range(0, 10)
* Here it will emit 10 items starting from 0 means 0 to 9 items.
* Log Output -
* D/MainActivity: onNext: 0
* D/MainActivity: onNext: 1
* D/MainActivity: onNext: 2
* D/MainActivity: onNext: 3
* D/MainActivity: onNext: 4
* D/MainActivity: onNext: 5
* D/MainActivity: onNext: 6
* D/MainActivity: onNext: 7
* D/MainActivity: onNext: 8
* D/MainActivity: onNext: 9
* D/MainActivity: onComplete:
* */

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var observable: Observable<Int>

    private lateinit var disposableObserver: DisposableObserver<Int>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observable = Observable.range(0, 10)

        compositeDisposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<Int> {
        disposableObserver = object : DisposableObserver<Int>() {
            override fun onNext(item: Int) {
                Log.d(TAG, "onNext: $item")
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
}
