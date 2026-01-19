package com.neuralshell.android.accessibility

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.util.Log

class NeuralAccessibilityService : AccessibilityService() {
    
    private val TAG = "NeuralA11yService"
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        
        val eventType = AccessibilityEvent.eventTypeToString(event.eventType)
        Log.d(TAG, "Event: $eventType, Package: ${event.packageName}")
        
        // Get root node
        val rootNode = rootInActiveWindow
        if (rootNode != null) {
            val screenContext = extractScreenContext(rootNode)
            // TODO: Send to terminal/AI for context awareness
            rootNode.recycle()
        }
    }
    
    private fun extractScreenContext(node: AccessibilityNodeInfo): ScreenContext {
        val textElements = mutableListOf<String>()
        val clickableElements = mutableListOf<UIElement>()
        
        traverseNode(node) { n ->
            // Extract text
            n.text?.let { textElements.add(it.toString()) }
            n.contentDescription?.let { textElements.add(it.toString()) }
            
            // Extract clickable elements
            if (n.isClickable) {
                val bounds = android.graphics.Rect()
                n.getBoundsInScreen(bounds)
                clickableElements.add(
                    UIElement(
                        text = n.text?.toString() ?: n.contentDescription?.toString() ?: "",
                        bounds = bounds,
                        className = n.className?.toString() ?: ""
                    )
                )
            }
        }
        
        return ScreenContext(
            texts = textElements,
            clickableElements = clickableElements
        )
    }
    
    private fun traverseNode(node: AccessibilityNodeInfo, callback: (AccessibilityNodeInfo) -> Unit) {
        callback(node)
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                traverseNode(child, callback)
                child.recycle()
            }
        }
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Service connected")
    }
}

data class ScreenContext(
    val texts: List<String>,
    val clickableElements: List<UIElement>
)

data class UIElement(
    val text: String,
    val bounds: android.graphics.Rect,
    val className: String
)
