package com.example.minimoney.core

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoney.core.injection.coreModule
import com.example.minimoney.core.network.api.ApiHelper
import com.example.minimoney.core.network.auth.AuthHelper
import com.example.minimoney.core.network.model.InvestorProductsModel
import com.example.minimoney.core.network.model.ProductModel
import com.example.minimoney.core.network.model.ProductResponseModel
import com.example.minimoney.core.storage.StorageHelper
import com.example.minimoney.core.storage.entity.UserEntity
import com.example.minimoney.model.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class RepositoryTest: KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockProvider = MockProviderRule.create {
        Mockito.mock(it.java)
    }

    private val dispatcher = UnconfinedTestDispatcher()
    private val repository: Repository by inject()
    private val authHelper: AuthHelper by inject()
    private val storageHelper: StorageHelper by inject()
    private val apiHelper: ApiHelper by inject()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        MockitoAnnotations.openMocks(this).close()
        startKoin {
            modules(coreModule)
        }
        declareMock<AuthHelper>()
        declareMock<StorageHelper>()
        declareMock<ApiHelper>()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun triggerGetUser_investorProductReturnedWithTwoProductResponse_storeUserCalledWithCorrectArguments() {
        runTest {
            whenever(apiHelper.getInvestorProducts()).thenReturn(
                InvestorProductsModel(
                    totalPlanValue = 2000.0,
                    productResponses = listOf(
                        ProductResponseModel(
                            id = 1,
                            planValue = 1000.0,
                            moneybox = 50.0,
                            product = ProductModel(
                                name = "Name1",
                                friendlyName = "Friendly Name1",
                                productHexCode = "#ffffff"
                            )
                        ),
                        ProductResponseModel(
                            id = 2,
                            planValue = 1000.0,
                            moneybox = 0.0,
                            product = ProductModel(
                                name = "Name2",
                                friendlyName = "Friendly Name2",
                                productHexCode = "#ffffff"
                            )
                        )
                    )
                )
            )
            whenever(storageHelper.getUserId()).thenReturn("UserId")
            whenever(storageHelper.getUserInputName()).thenReturn("User Name")
            val userEntity = UserEntity(
                id = "UserId",
                name = "User Name",
                totalPlanValue = 2000.0,
                accounts = listOf(
                    Account(
                        id = 1,
                        name = "Name1",
                        friendlyName = "Friendly Name1",
                        planValue = 1000.0,
                        moneybox = 50.0,
                        colorCode = "#ffffff"
                    ),
                    Account(
                        id = 2,
                        name = "Name2",
                        friendlyName = "Friendly Name2",
                        planValue = 1000.0,
                        moneybox = 0.0,
                        colorCode = "#ffffff"
                    )
                )
            )

            repository.triggerGetUser()

            Mockito.verify(storageHelper).storeUser(userEntity)
        }
    }
}