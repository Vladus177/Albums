package com.vladus177.albums.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vladus177.albums.common.Resource
import com.vladus177.albums.common.extension.*
import com.vladus177.albums.domain.GetUserListUseCase
import com.vladus177.albums.domain.SetFavoriteUseCase
import com.vladus177.albums.ui.mapper.UserViewMapper
import com.vladus177.albums.ui.model.UserView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserListViewModel @Inject constructor(
    private val getUserListUseCase: GetUserListUseCase,
    private val setFavoriteUseCase: SetFavoriteUseCase,
    private val usersListMapper: UserViewMapper
) : ViewModel() {

    val users = MutableLiveData<Resource<List<UserView>>>()

    private val compositeDisposable = CompositeDisposable()
    private val setFavoriteDisposable = CompositeDisposable()

    fun loadUserList(forceUpdate: Boolean) {
        compositeDisposable.add(getUserListUseCase.getUserList(forceUpdate)
            .doOnSubscribe { users.setLoading() }
            .subscribeOn(Schedulers.io())
            .map { it.map { with(usersListMapper) { it.fromDomainToView() } } }
            .subscribe({ users.setSuccess(it) }, { users.setError(it.message) })
        )
    }

    fun setFavorite(userId: Long, favorite: Boolean) {
        setFavoriteDisposable.add(setFavoriteUseCase.setFavorite(userId, favorite)
            .subscribeOn(Schedulers.io())
            .subscribe { loadUserList(false)}
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        setFavoriteDisposable.dispose()
        super.onCleared()
    }
}