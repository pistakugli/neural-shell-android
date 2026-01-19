package com.neuralshell.android.terminal

import java.io.*

class PtyProcess(command: Array<String>, environment: Map<String, String>) {
    
    private var process: Process? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private var errorStream: InputStream? = null
    
    init {
        val processBuilder = ProcessBuilder(*command)
        processBuilder.environment().putAll(environment)
        processBuilder.redirectErrorStream(true)
        
        try {
            process = processBuilder.start()
            outputStream = process?.outputStream
            inputStream = process?.inputStream
            errorStream = process?.errorStream
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    fun write(data: String) {
        try {
            outputStream?.write(data.toByteArray())
            outputStream?.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    fun read(): String? {
        return try {
            val buffer = ByteArray(4096)
            val bytesRead = inputStream?.read(buffer) ?: -1
            if (bytesRead > 0) {
                String(buffer, 0, bytesRead)
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }
    
    fun isAlive(): Boolean = process?.isAlive ?: false
    
    fun destroy() {
        try {
            outputStream?.close()
            inputStream?.close()
            errorStream?.close()
            process?.destroy()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    fun waitFor(): Int = process?.waitFor() ?: -1
}
