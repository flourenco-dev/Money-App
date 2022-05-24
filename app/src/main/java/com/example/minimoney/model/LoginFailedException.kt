package com.example.minimoney.model

import java.io.IOException

class LoginFailedException(val loginRequestState: LoginRequestState): IOException()