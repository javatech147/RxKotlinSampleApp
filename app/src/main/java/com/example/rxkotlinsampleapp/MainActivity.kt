package com.example.rxkotlinsampleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.rxkotlinsampleapp.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private val helloWorld = "Hello World in RxKotlin"
    private lateinit var observable: Observable<String>

    private lateinit var disposableObserver: DisposableObserver<String>
    private lateinit var disposableObserver2: DisposableObserver<String>

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observable = Observable.just(helloWorld)
        disposableObserver = object : DisposableObserver<String>() {
            override fun onNext(str: String?) {
                Log.d(TAG, "onNext: ")
                binding.textView.text = str
            }

            override fun onError(e: Throwable?) = Unit

            override fun onComplete() = Unit
        }
        compositeDisposable.add(disposableObserver)
        observable.subscribe(disposableObserver)

        disposableObserver2 = object : DisposableObserver<String>() {
            override fun onNext(str: String?) {
                binding.textView.text = str
            }

            override fun onError(e: Throwable?) = Unit

            override fun onComplete() = Unit
        }
        compositeDisposable.add(disposableObserver2)
        observable.subscribe(disposableObserver2)
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
