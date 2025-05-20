package com.example.centauri.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class  AuthViewModel: ViewModel() {
    companion object {
        val TAG: String = "AuthViewModel_TAG"
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbViewModel = DbViewModel()

    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: MutableLiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if (auth.currentUser != null){
            Log.i(TAG, "Status checked Status is Authenticated!")
            _authState.value = AuthState.Authenticated

        }else{
            Log.e(TAG, "Status checked Status is Unauthenticated!")
            _authState.value = AuthState.Unauthenticated
        }
    }


    fun login( email: String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading;

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")

                }

            }
    }


    fun signUp( user: UserData){
        Log.i(TAG, "signUp fun is on work")

        if (user.email.isEmpty() || user.password.isEmpty()){
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }


        _authState.value = AuthState.Loading;

        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener{task->
                Log.i(TAG, "signUp fun is on work")
                if(task.isSuccessful){
                    Log.i(TAG, "signUp fun is Succeed")

                    dbViewModel.addUserData(user, {isSucceed ->
                        if (isSucceed){
                            Log.i(TAG, "addUserData fun is Succeed")
                            _authState.value = AuthState.Authenticated
                        }else{
                            Log.i(TAG, "addUserData fun is failed")
                            _authState.value = AuthState.Error("Error in addUserData fun")
                        }
                    })
                }else{
                    Log.i(TAG, "signUp fun is failed")
                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")

                }

            }
    }


    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }


    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

}


sealed class AuthState{
    object Unauthenticated : AuthState()
    object Authenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()

}