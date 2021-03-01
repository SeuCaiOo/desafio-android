package com.picpay.desafio.android.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.ui.UserListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserListAdapter

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.user_list_progress_bar)

        adapter = UserListAdapter()
        recyclerView.adapter = adapter

        viewModel.fetchUsers()

        viewModel.users.observe(this, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    adapter.users = it
                }
            }
        })

        viewModel.progressBarVisible.observe(this, Observer {
            if (it) progressBar.visibility = View.VISIBLE else progressBar.visibility = View.GONE
        })

        viewModel.recyclerViewVisible.observe(this, Observer {
            if (it) recyclerView.visibility = View.VISIBLE else recyclerView.visibility = View.GONE
        })

        viewModel.hasError.observe(this, Observer {
            if (it) {
                val message = getString(R.string.error)
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        })

    }


}