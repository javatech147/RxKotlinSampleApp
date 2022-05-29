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
* buffer() operator
* Official doc -
* periodically gather items emitted by an Observable into bundles and emit these bundles
* rather than emitting the items one at a time.
* LogOutput -
* D/MainActivity: [0, 1, 2, 3]
* D/MainActivity: [4, 5, 6, 7]
* D/MainActivity: [8, 9, 10, 11]
* D/MainActivity: [12, 13, 14, 15]
* D/MainActivity: [16, 17, 18, 19]
* D/MainActivity: onComplete:
* */

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private lateinit var observable: Observable<Int>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observable = Observable.range(0, 20)

        compositeDisposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(4)
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<MutableList<Int>> {
        return object : DisposableObserver<MutableList<Int>>() {
            override fun onNext(t: MutableList<Int>?) {
                Log.d(TAG, "$t")
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: ")
            }
        }
    }
}
