package com.neuralshell.android.terminal

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class TerminalSession(
    private val shell: String = "/system/bin/sh"
) {
    private var ptyProcess: PtyProcess? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    private val _output = MutableStateFlow<List<String>>(emptyList())
    val output: StateFlow<List<String>> = _output
    
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning
    
    fun start() {
        if (_isRunning.value) return
        
        val environment = mapOf(
            "TERM" to "xterm-256color",
            "HOME" to "/data/data/com.neuralshell.android",
            "PATH" to "/system/bin:/system/xbin",
            "TMPDIR" to "/data/local/tmp"
        )
        
        ptyProcess = PtyProcess(arrayOf(shell), environment)
        _isRunning.value = true
        
        // Start reading output
        scope.launch {
            while (_isRunning.value && ptyProcess?.isAlive() == true) {
                val output = ptyProcess?.read()
                if (output != null) {
                    appendOutput(output)
                }
                delay(50)
            }
        }
    }
    
    fun executeCommand(command: String) {
        if (!_isRunning.value) return
        
        appendOutput("$ $command")
        ptyProcess?.write(command + "\n")
    }
    
    private fun appendOutput(text: String) {
        val lines = text.split("\n")
        _output.value = _output.value + lines
        
        // Keep last 1000 lines
        if (_output.value.size > 1000) {
            _output.value = _output.value.takeLast(1000)
        }
    }
    
    fun stop() {
        _isRunning.value = false
        ptyProcess?.destroy()
        scope.cancel()
    }
    
    fun clear() {
        _output.value = emptyList()
    }
}
