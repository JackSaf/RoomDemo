package com.jacksafblaze.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.jacksafblaze.roomdemo.databinding.ActivityMainBinding
import com.jacksafblaze.roomdemo.db.Subscriber
import com.jacksafblaze.roomdemo.db.SubscriberDatabase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ActivityMainViewModel
    private lateinit var adapter: MyRecyclerViewAdapter
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = SubscriberDatabase.getInstance(applicationContext).subscriberDAO()
        val viewModelFactory = ActivityMainViewModelFactory(Repository(dao))
        viewModel = ViewModelProvider(this, viewModelFactory)[ActivityMainViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        initRecyclerView()
    }
    fun setList (subscribers: List<Subscriber>){
        adapter.setList(subscribers)
    }
    private fun initRecyclerView() {
        adapter = MyRecyclerViewAdapter()
        { selectedItem: Subscriber ->
            listItemClicked(selectedItem)
        }
        binding?.subscribersRecyclerView?.layoutManager = LinearLayoutManager(this)
        binding?.subscribersRecyclerView?.adapter = adapter
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subscribers.collect {
                    setList(it)
                }
            }
        }
    }

    private fun listItemClicked(subscriber: Subscriber) {
            viewModel.initUpdateAndDelete(subscriber)
        }
}