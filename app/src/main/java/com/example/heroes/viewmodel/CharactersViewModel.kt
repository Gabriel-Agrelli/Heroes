package com.example.heroes.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heroes.data.api.MarvelService
import com.example.heroes.data.model.Character
import com.example.heroes.data.model.CharacterResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class CharactersViewModel : ViewModel() {
    var marvelService = MarvelService()

    private val disposable = CompositeDisposable()

    val characters = MutableLiveData<List<Character>>()
    val characterLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchCharacters()
    }

    fun search(name: String) {
        fetchCharactersByName(name)
    }

    private fun fetchCharacters() {
        loading.value = true
        disposable.add(
            marvelService.getCharacters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CharacterResponse>() {
                    override fun onSuccess(value: CharacterResponse) {
                        characters.value = value.data.results
                        loading.value = false
                        characterLoadError.value = false
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        characterLoadError.value = true
                    }
                })
        )
    }

    private fun fetchCharactersByName(name: String) {
        loading.value = true
        disposable.add(
            marvelService.getCharactersByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CharacterResponse>() {
                    override fun onSuccess(value: CharacterResponse) {
                        characters.value = value.data.results
                        loading.value = false
                        characterLoadError.value = false
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        characterLoadError.value = true
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}