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
    
    private lateinit var observer2: Observer<String>
    private lateinit var disposable2: Disposable
    
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

        observer2 = object : Observer<String> {
            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe2: $d")
                d?.let {
                    disposable2 = it
                }
            }

            override fun onNext(str: String?) {
                Log.d(TAG, "onNext2: ")
                binding.textView.text = str
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError2: $e")
            }

            override fun onComplete() {
                Log.d(TAG, "onComplete2: ")
            }
        }
        observable.subscribe(observer)
        observable.subscribe(observer2)
    }

    override fun onDestroy() {
        disposable.dispose()
        disposable2.dispose()
        super.onDestroy()
    }
}
