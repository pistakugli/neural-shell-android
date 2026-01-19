package com.neuralshell.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neuralshell.android.ui.viewmodel.TerminalViewModel
import kotlinx.coroutines.launch

@Composable
fun TerminalScreen(viewModel: TerminalViewModel = viewModel()) {
    val outputLines by viewModel.outputLines.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(outputLines.size) {
        if (outputLines.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(outputLines.size - 1)
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(8.dp)
    ) {
        // Output area
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState
        ) {
            items(outputLines) { line ->
                Text(
                    text = line,
                    color = Color(0xFF00FF00),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Extra keys bar
        ExtraKeysBar(
            onKeyClick = { key ->
                viewModel.onInputChange(inputText + key)
            }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Input area
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = inputText,
                onValueChange = { viewModel.onInputChange(it) },
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF2D2D2D))
                    .padding(12.dp),
                textStyle = TextStyle(
                    color = Color(0xFF00FF00),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = { viewModel.executeCommand() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0078D4)
                )
            ) {
                Text("Run")
            }
        }
    }
}

@Composable
fun ExtraKeysBar(onKeyClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2D2D2D))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val keys = listOf("ESC", "TAB", "CTRL", "/", "-", "~", "|", ">")
        
        keys.forEach { key ->
            Button(
                onClick = { 
                    val value = when(key) {
                        "ESC" -> "\u001B"
                        "TAB" -> "\t"
                        "CTRL" -> "^"
                        else -> key
                    }
                    onKeyClick(value)
                },
                modifier = Modifier.weight(1f).height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF404040)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = key,
                    fontSize = 10.sp,
                    color = Color.White
                )
            }
        }
    }
}
