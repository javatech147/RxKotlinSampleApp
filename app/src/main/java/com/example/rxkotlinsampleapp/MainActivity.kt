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
* fromArray() operators will work only with array type, not on list type.
*       private val greetings = arrayOf("Hello A", "Hello B", "Hello C")
*       ...
*       observable = Observable.fromArray(*greetings)
*
* Log Output -
* D/MainActivity: onNext: Hello A
* D/MainActivity: onNext: Hello B
* D/MainActivity: onNext: Hello C
*
* Note - If you data is of List<T> type, then use fromIterable() operator to emit one by one items from the list.
* */

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private val greetings = arrayOf("Hello A", "Hello B", "Hello C")

    private lateinit var observable: Observable<String>

    private lateinit var disposableObserver: DisposableObserver<String>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observable = Observable.fromArray(*greetings)

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
