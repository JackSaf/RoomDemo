package com.jacksafblaze.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.jacksafblaze.roomdemo.databinding.ActivityMainBinding
import com.jacksafblaze.roomdemo.db.Subscriber
import com.jacksafblaze.roomdemo.db.SubscriberDatabase
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ActivityMainViewModel
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = SubscriberDatabase.getInstance(applicationContext).subscriberDAO()
        val viewModelFactory = ActivityMainViewModelFactory(Repository(dao))
        viewModel = ViewModelProvider(this, viewModelFactory)[ActivityMainViewModel::class.java]
        Log.i("Flow", "viewmodel here")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        initRecyclerView()
    }
    private fun initRecyclerView() {
        binding?.subscribersRecyclerView?.layoutManager = LinearLayoutManager(this)
        Log.i("Flow", "init here")
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.i("Flow", "coroutine here")
                viewModel.subscribers.collect {
                    Log.i("Flow", "Collect here")
                    binding?.subscribersRecyclerView?.adapter = MyRecyclerViewAdapter(it)
                }
            }
        }
    }
}