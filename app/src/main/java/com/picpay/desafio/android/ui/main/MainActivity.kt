package com.picpay.desafio.android.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.repositories.UserRepository
import com.picpay.desafio.android.data.sources.remote.UsersRemoteDataSource
import com.picpay.desafio.android.domain.usecases.GetUsersUseCase
import com.picpay.desafio.android.ui.UserListAdapter

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserListAdapter
    private lateinit var viewModel: MainViewModel

    private val service by lazy { PicPayService.service }
    private val remoteDataSource by lazy { UsersRemoteDataSource(service) }
    private val repository by lazy { UserRepository(remoteDataSource) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(
            this, MainViewModelFactory(GetUsersUseCase(repository))
        ).get(MainViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.user_list_progress_bar)

        adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar.visibility = View.VISIBLE

        viewModel.users.observe(this, Observer {
            it?.let {
                if (it.isEmpty()) {
                    val message = getString(R.string.error)

                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE

                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                } else {
                    progressBar.visibility = View.GONE
                    adapter.users = it
                }
            } ?: run {
                val message = getString(R.string.error)

                progressBar.visibility = View.GONE
                recyclerView.visibility = View.GONE

                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        })

    }



}
