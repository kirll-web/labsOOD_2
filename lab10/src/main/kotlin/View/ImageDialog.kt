package View

import ViewModel.ImageDialog.IImageDialogViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


class ImageDialog(
    private val viewModel: IImageDialogViewModel
) {
    @Composable
    fun draw() {
        val path = remember { mutableStateOf("") }
        val messageState by viewModel.messageError
        if (viewModel.isDialogVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xAA000000))
                    .clickable(
                        onClick = { viewModel.hide() },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center,

                ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            TextField(
                                value = path.value,
                                onValueChange = { path.value = it },
                                label = { Text("Введите путь") }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(onClick = {
                                    viewModel.tryAddImage(path.value)
                                }) {
                                    Text("Создать файл")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = { viewModel.hide() }) {
                                    Text("Закрыть")
                                }
                            }
                        }
                    }
                    if (messageState != null) {
                        Box(
                            modifier = Modifier
                                .width(400.dp)
                                .height(200.dp)
                                .background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = messageState ?: "",
                                color = Color.Red,
                                modifier = Modifier
                                    .background(Color.White, shape = RoundedCornerShape(4.dp))
                                    .padding(8.dp)
                            )
                        }

                          LaunchedEffect(messageState) {
                              kotlinx.coroutines.delay(5000)
                              viewModel.setErrorMessage(null)
                          }
                    }
                }
            }
        }
    }

}