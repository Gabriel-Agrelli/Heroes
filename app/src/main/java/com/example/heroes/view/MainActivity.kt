package com.example.heroes.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heroes.R
import com.example.heroes.viewmodel.CharactersViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: CharactersViewModel
    private val charactersAdapter: CharactersAdapter by lazy { CharactersAdapter() }

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(CharactersViewModel::class.java)
        viewModel.refresh()

        rvCharacters.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = charactersAdapter
        }

        setListeners()
        observeViewModel()
    }

    private fun setListeners() {
        edtCharacterName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (edtCharacterName.text.toString().trim().isNotEmpty()) {
                    searchJob?.cancel()
                    searchJob = coroutineScope.launch {
                        edtCharacterName.text.toString().trim().let {
                            delay(600)
                            viewModel.search(it)
                        }
                    }
                } else {
                    searchJob?.cancel()
                    viewModel.refresh()
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.characters.observe(this, Observer { characters ->
            characters?.let {
                rvCharacters.visibility = View.VISIBLE
                charactersAdapter.setData(it)
            }
        })

        viewModel.characterLoadError.observe(this, Observer { isError ->
            isError?.let {
                txtError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                pbCharacters.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    txtError.visibility = View.GONE
                    rvCharacters.visibility = View.GONE
                }
            }
        })
    }
}