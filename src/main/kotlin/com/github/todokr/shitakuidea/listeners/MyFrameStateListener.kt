package com.github.todokr.shitakuidea.listeners

import com.intellij.ide.FrameStateListener
import com.intellij.openapi.diagnostic.thisLogger

internal class MyFrameStateListener : FrameStateListener {

    override fun onFrameActivated() {
        thisLogger().warn("いらないサンプルコードの削除を忘れずにね `plugin.xml`.")
    }
}
