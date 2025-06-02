import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.index.ui.theme.IndexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IndexTheme {
                VideoBackgroundScreen()
            }
        }
    }
}

@Composable
fun VideoBackgroundScreen() {
    val context = LocalContext.current

    // ExoPlayer 생성
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("asset:///background_video.mp4") // assets에 넣은 영상
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // 📺 영상 재생 View
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    this.player = player
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 🎨 위에 덧씌울 UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("환영합니다!", color = Color.White, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { /* 일반 회원 진입 */ }) {
                Text("일반 회원으로 시작")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = { /* 관리자 로그인 */ }) {
                Text("관리자 로그인")
            }
        }
    }
}
