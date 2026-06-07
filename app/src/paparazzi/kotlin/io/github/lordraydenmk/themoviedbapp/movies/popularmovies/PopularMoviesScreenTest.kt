package io.github.lordraydenmk.themoviedbapp.movies.popularmovies

import androidx.compose.material3.ExperimentalMaterial3Api
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.TestName
import io.github.lordraydenmk.themoviedbapp.R
import io.github.lordraydenmk.themoviedbapp.common.ErrorTextRes
import io.github.lordraydenmk.themoviedbapp.common.MainDispatcherExtension
import io.github.lordraydenmk.themoviedbapp.common.setupCoil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@ExtendWith(MainDispatcherExtension::class)
class PopularMoviesScreenTest {

    lateinit var paparazzi: Paparazzi

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        paparazzi = Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            theme = "android:Theme.Material.Light.NoActionBar",
        ).apply {
            setup(
                testName = TestName(
                    packageName = testInfo.testClass.get().`package`?.name.orEmpty(),
                    className = testInfo.testClass.get().simpleName,
                    methodName = testInfo.testMethod.get().name
                )
            )
        }
        setupCoil(paparazzi)
    }

    @AfterEach
    fun tearDown() {
        paparazzi.teardown()
    }

    @Test
    fun loadingState() {
        paparazzi.snapshot {
            PopularMoviesScreen(Loading, actions = Channel())
        }
    }

    @Test
    fun errorWithRetry() {
        paparazzi.snapshot {
            PopularMoviesScreen(
                Problem(ErrorTextRes(R.string.error_recoverable_network)),
                actions = Channel()
            )
        }
    }

    @Test
    fun content() {
        val url = "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg".toHttpUrl()
        val viewState = Content(
            listOf(
                MovieViewEntity(42, "Ant Man", url),
                MovieViewEntity(43, "Spider Man", url),
                MovieViewEntity(44, "Iron Man", url),
                MovieViewEntity(45, "Hulk", url)
            )
        )
        paparazzi.snapshot {
            PopularMoviesScreen(viewState, actions = Channel())
        }
    }
}