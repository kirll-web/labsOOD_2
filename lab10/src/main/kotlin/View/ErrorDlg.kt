package View

import ViewModel.ErrorDlgViewModel
import ViewModel.ErrorState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ErrorDlg(
    private val viewModel: ErrorDlgViewModel,
) {
    private var mState: ErrorState by mutableStateOf(ErrorState(false, null))
    init {
        viewModel.viewModelScope.launch {
            viewModel.state.onEach {
                mState = it
                println("${it.message}")
            }.stateIn(viewModel.viewModelScope)
        }
    }

    @Composable
    fun draw() {
            if (mState.isShowError) {
                Box(
                    modifier = Modifier
                        .width(400.dp)
                        .height(200.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mState.message ?: "",
                        color = Color.Red,
                        modifier = Modifier
                            .background(Color.LightGray, shape = RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )
                }
            }

    }
}
