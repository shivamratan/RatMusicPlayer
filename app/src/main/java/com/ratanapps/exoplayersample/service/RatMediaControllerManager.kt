package com.ratanapps.exoplayersample.service

import android.content.ComponentName
import android.content.Context
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RatMediaControllerManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var controllerFuture: ListenableFuture<MediaController>? = null

    var controller: MediaController? = null
        private set

    fun connect(onConnected: (MediaController) -> Unit) {
        controller?.let {
            onConnected(it)
            return
        }

        controllerFuture?.let {
            it.addListener({ controller?.let { mycontroller -> onConnected(mycontroller) }},
                MoreExecutors.directExecutor()
            )
            return
        }

        val sessionToken = SessionToken(context, ComponentName(context, RatMusicService::class.java))
        val future = MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture = future
        controllerFuture?.addListener({
                try {
                    controller = future.get()
                    controller?.let { mycontroller -> onConnected(mycontroller) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            MoreExecutors.directExecutor()
        )
    }

    fun release() {
        controllerFuture?.let {
            MediaController.releaseFuture(it)
            controllerFuture = null
        }
        controller = null
    }

}