package com.egtourguide.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.egtourguide.core.domain.usecases.GetFromDataStoreUseCase
import com.egtourguide.core.presentation.navigation.AppGraph
import com.egtourguide.core.presentation.navigation.AppNavigation
import com.egtourguide.core.presentation.ui.theme.EGTourGuideTheme
import com.egtourguide.core.utils.Constants.TAG
import com.egtourguide.core.utils.DataStoreKeys.IS_LOGGED_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getFromDataStoreUseCase: GetFromDataStoreUseCase
    private var showSplash = true
    private var isLogged: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            showSplash
        }
        hideSplash()

        lifecycleScope.launch(Dispatchers.IO) {
            isLogged = getFromDataStoreUseCase(key = IS_LOGGED_KEY)
        }

        setContent {
            EGTourGuideTheme {
//                val navController = rememberNavController()
//                AppNavigation(
//                    navController = navController,
//                    startDestination = getStartDestination()
//                )
                var isRefreshing by remember { mutableStateOf(false) }
                val scope = rememberCoroutineScope()

                PullToRefresh(
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        scope.launch {
                            isRefreshing = true
                            delay(2000L)
                            isRefreshing = false
                        }
                    }
                )
            }
        }
    }

    private fun hideSplash() {
        lifecycleScope.launch {
            delay(1000L)
            showSplash = false
        }
    }

    private fun getStartDestination(): String {
        return if (isLogged == true) AppGraph.Main.route
        else AppGraph.Auth.route
    }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefresh(
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {}
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Hello!",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(20) {
                    Text(
                        text = "Item #$it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                Timber.tag(TAG).d("PullToRefresh: Refreshing")
                onRefresh()
            }
        }

        LaunchedEffect(isRefreshing) {
            if (isRefreshing) pullToRefreshState.startRefresh()
            else pullToRefreshState.endRefresh()
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}