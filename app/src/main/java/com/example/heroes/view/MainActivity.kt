package com.example.heroes.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heroes.R
import com.example.heroes.data.model.Character
import com.example.heroes.viewmodel.CharactersViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: CharactersViewModel
    private val charactersAdapter = CharactersAdapter(this::onClickCharacter)

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null

    var offset = 0
    var pastVisibleItems = 0
    var totalItemCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(CharactersViewModel::class.java)
        viewModel.getHeroes(offset)

        setupRecyclerView()
        setListeners()
        observeViewModel()
    }

    private fun onClickCharacter(character: Character) {
        CharacterDetailsActivity.open(this, character)
    }

    private fun setupRecyclerView() {
        rvCharacters.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(context)
            adapter = charactersAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                        totalItemCount = layoutManager.itemCount
                        pastVisibleItems = layoutManager.findLastVisibleItemPosition()

                        if (viewModel.loading.value == false) {
                            if (pastVisibleItems >= totalItemCount - 1) {
                                offset += 20
                                viewModel.getHeroes(offset)
                            }
                        }
                    }
                }
            })
        }
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
                            viewModel.searchHeroes(it)
                        }
                    }
                } else {
                    searchJob?.cancel()
                    charactersAdapter.clearData()
                    viewModel.getHeroes(0)
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.characters.observe(this, Observer { characters ->
            characters?.let {
                rvCharacters.visibility = View.VISIBLE
                charactersAdapter.addData(it)
            }
        })

        viewModel.searchCharacters.observe(this, Observer { characters ->
            characters?.let {
                rvCharacters.visibility = View.VISIBLE
                charactersAdapter.updateData(it)
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