package com.plcoding.cryptotracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.cryptotracker.core.presentation.util.ObserveAsEvents
import com.plcoding.cryptotracker.core.presentation.util.toString
import com.plcoding.cryptotracker.crypto.presentation.coin_detail.CoinDetailScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListActions
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListScreen
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val viewModel: CoinListViewModel = koinViewModel<CoinListViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    val sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true
                    )
                    val scope = rememberCoroutineScope()
                    var showBottomSheet by remember { mutableStateOf(false) }

                    val context = LocalContext.current
                    ObserveAsEvents(events = viewModel.events) { event ->
                        when (event) {
                            is CoinListEvent.Error -> {
                                Toast.makeText(
                                    context,
                                    event.error.toString(context),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    CoinListScreen(
                        modifier = Modifier.padding(innerPadding),
                        state = state,
                        onAction = { action ->
                            viewModel.onAction(action)
                            if (action is CoinListActions.OnCoinClick) {
                                showBottomSheet = true
                            }

                        }
                    )

                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = false
                            },
                            modifier = Modifier.statusBarsPadding().fillMaxSize(),
                            containerColor = Color.Transparent,
                            sheetState = sheetState,
                            dragHandle = { }
                        ) {
                            CoinDetailScreen(
                                state = state,
                                onClose = {
                                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showBottomSheet = false
                                        }
                                    }
                                },
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

