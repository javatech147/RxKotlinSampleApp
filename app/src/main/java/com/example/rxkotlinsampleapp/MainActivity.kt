package com.example.rxkotlinsampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.rxkotlinsampleapp.databinding.ActivityMainBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private val helloWorld = "Hello World in RxKotlin"
    private lateinit var observable: Observable<String>
    private lateinit var observer: Observer<String>
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observable = Observable.just(helloWorld)
        observer = object : Observer<String> {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe: $d")
                d?.let {
                    disposable = it
                }
            }

            override fun onNext(str: String?) {
                Log.d(TAG, "onNext: ")
                binding.textView.text = str
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError: $e")
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete: ")
            }
        }
        observable.subscribe(observer)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        disposable.dispose()
        super.onDestroy()
    }
}
