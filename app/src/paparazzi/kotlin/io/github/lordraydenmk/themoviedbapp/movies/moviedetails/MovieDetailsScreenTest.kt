package io.github.lordraydenmk.themoviedbapp.movies.moviedetails

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

@ExperimentalCoroutinesApi
@ExtendWith(MainDispatcherExtension::class)
class MovieDetailsScreenTest {

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
            MovieDetailsScreen(
                Loading,
                movieId = 0,
                actions = Channel()
            )
        }
    }

    @Test
    fun errorStateWithRetry() {
        val viewState = Problem(ErrorTextRes(R.string.error_recoverable_network))

        paparazzi.snapshot {
            MovieDetailsScreen(viewState, movieId = 0, actions = Channel())
        }
    }

    @Test
    fun contentState() {
        val url = "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg".toHttpUrl()
        val viewState = Content(
            MovieDetailsViewEntity(
                "Hulk",
                "Movie overview",
                VoteAverage(0.745f, "7.5"),
                url
            )
        )

        paparazzi.snapshot {
            MovieDetailsScreen(viewState, movieId = 0, actions = Channel())
        }
    }
}