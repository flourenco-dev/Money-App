package com.example.minimoney.model

enum class LoginRequestState {
    Success,
    WrongEmailOrPassword,
    TooManyAttempts,
    GenericError
}