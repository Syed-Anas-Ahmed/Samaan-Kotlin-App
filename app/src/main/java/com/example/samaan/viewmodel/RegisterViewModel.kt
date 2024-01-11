package com.example.samaan.viewmodel
import androidx.lifecycle.ViewModel
import com.example.samaan.data.User
import com.example.samaan.util.Constants.USER_COLLECTION
import com.example.samaan.util.RegisterFieldsState
import com.example.samaan.util.RegisterValidation
import com.example.samaan.util.Resource
import com.example.samaan.util.validateEmail
import com.example.samaan.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
):ViewModel() {

    private val _registerUserStatus =
        MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _registerUserStatus

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()


    fun createUserWithEmailAndPassword(user: User, password: String) {

        if(checkValidation(user, password)){
            runBlocking {
                _registerUserStatus.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user?.let {
                        saveUserInfo(it.uid, user)
                        //_registerUserStatus.value = Resource.Success(it)
                    }
                }
                .addOnFailureListener {
                    _registerUserStatus.value = Resource.Error(it.message.toString())

                }
        }
        else{
            val registerFieldState = RegisterFieldsState(
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldState)
            }
        }


    }

    private fun saveUserInfo(userUid:String, user:User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener{
                _registerUserStatus.value=Resource.Success(user)
            }.addOnFailureListener{
_registerUserStatus.value=Resource.Error(it.message.toString())
            }

    }

    private fun checkValidation(user: User, password: String):Boolean{
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister = emailValidation is RegisterValidation.Success &&
                passwordValidation is RegisterValidation.Success
        return shouldRegister
    }
}