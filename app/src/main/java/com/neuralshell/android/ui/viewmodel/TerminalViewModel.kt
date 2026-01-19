package com.neuralshell.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neuralshell.android.ai.ClaudeService
import com.neuralshell.android.terminal.TerminalSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TerminalViewModel : ViewModel() {
    
    private val terminalSession = TerminalSession()
    
    private val _outputLines = MutableStateFlow<List<String>>(listOf("Neural Shell v0.1.0", "Type 'help' for commands", ""))
    val outputLines: StateFlow<List<String>> = _outputLines
    
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText
    
    init {
        terminalSession.start()
        
        // Collect terminal output
        viewModelScope.launch {
            terminalSession.output.collect { lines ->
                _outputLines.value = _outputLines.value + lines
            }
        }
    }
    
    fun onInputChange(text: String) {
        _inputText.value = text
    }
    
    fun executeCommand() {
        val command = _inputText.value.trim()
        if (command.isEmpty()) return
        
        _inputText.value = ""
        
        viewModelScope.launch {
            // Check if command looks like natural language
            if (isNaturalLanguage(command)) {
                appendOutput("🤖 Parsing: $command")
                val shellCommand = ClaudeService.parseCommand(command)
                appendOutput("→ $shellCommand")
                terminalSession.executeCommand(shellCommand)
            } else {
                // Execute directly
                terminalSession.executeCommand(command)
            }
        }
    }
    
    private fun isNaturalLanguage(input: String): Boolean {
        // Simple heuristic: if it contains spaces and common words
        val naturalWords = setOf("show", "list", "find", "what", "how", "get", "display", "check")
        return input.contains(" ") && naturalWords.any { input.lowercase().contains(it) }
    }
    
    private fun appendOutput(text: String) {
        _outputLines.value = _outputLines.value + text
    }
    
    override fun onCleared() {
        super.onCleared()
        terminalSession.stop()
    }
}
