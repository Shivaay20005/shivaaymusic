package com.shivaay20005.shivaaymusic

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastFirstOrNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.shivaay20005.innertube.YouTube
import com.shivaay20005.innertube.models.SongItem
import com.shivaay20005.innertube.models.WatchEndpoint
import com.shivaay20005.shivaaymusic.constants.AppBarHeight
import com.shivaay20005.shivaaymusic.constants.DarkModeKey
import com.shivaay20005.shivaaymusic.constants.DefaultOpenTabKey
import com.shivaay20005.shivaaymusic.constants.DisableScreenshotKey
import com.shivaay20005.shivaaymusic.constants.DynamicThemeKey
import com.shivaay20005.shivaaymusic.constants.MiniPlayerHeight
import com.shivaay20005.shivaaymusic.constants.NavigationBarAnimationSpec
import com.shivaay20005.shivaaymusic.constants.NavigationBarHeight
import com.shivaay20005.shivaaymusic.constants.PauseSearchHistoryKey
import com.shivaay20005.shivaaymusic.constants.PlayerBackgroundStyle
import com.shivaay20005.shivaaymusic.constants.PlayerBackgroundStyleKey
import com.shivaay20005.shivaaymusic.constants.PureBlackKey
import com.shivaay20005.shivaaymusic.constants.SearchSource
import com.shivaay20005.shivaaymusic.constants.SearchSourceKey
import com.shivaay20005.shivaaymusic.constants.SlimNavBarKey
import com.shivaay20005.shivaaymusic.constants.StopMusicOnTaskClearKey
import com.shivaay20005.shivaaymusic.db.MusicDatabase
import com.shivaay20005.shivaaymusic.db.entities.SearchHistory
import com.shivaay20005.shivaaymusic.extensions.toEnum
import com.shivaay20005.shivaaymusic.models.toMediaMetadata
import com.shivaay20005.shivaaymusic.playback.DownloadUtil
import com.shivaay20005.shivaaymusic.playback.MusicService
import com.shivaay20005.shivaaymusic.playback.MusicService.MusicBinder
import com.shivaay20005.shivaaymusic.playback.PlayerConnection
import com.shivaay20005.shivaaymusic.playback.queues.YouTubeQueue
import com.shivaay20005.shivaaymusic.ui.component.BottomSheetMenu
import com.shivaay20005.shivaaymusic.ui.component.IconButton
import com.shivaay20005.shivaaymusic.ui.component.LocalMenuState
import com.shivaay20005.shivaaymusic.ui.component.LocaleManager
import com.shivaay20005.shivaaymusic.ui.component.Lyrics
import com.shivaay20005.shivaaymusic.ui.component.TopSearch
import com.shivaay20005.shivaaymusic.ui.component.rememberBottomSheetState
import com.shivaay20005.shivaaymusic.ui.component.shimmer.ShimmerTheme
import com.shivaay20005.shivaaymusic.ui.menu.YouTubeSongMenu
import com.shivaay20005.shivaaymusic.ui.player.BottomSheetPlayer
import com.shivaay20005.shivaaymusic.ui.screens.Screens
import com.shivaay20005.shivaaymusic.ui.screens.navigationBuilder
import com.shivaay20005.shivaaymusic.ui.screens.search.LocalSearchScreen
import com.shivaay20005.shivaaymusic.ui.screens.search.OnlineSearchScreen
import com.shivaay20005.shivaaymusic.ui.screens.settings.AvatarPreferenceManager
import com.shivaay20005.shivaaymusic.ui.screens.settings.DarkMode
import com.shivaay20005.shivaaymusic.ui.screens.settings.NavigationTab
import com.shivaay20005.shivaaymusic.ui.theme.ColorSaver
import com.shivaay20005.shivaaymusic.ui.theme.DefaultThemeColor
import com.shivaay20005.shivaaymusic.ui.theme.shivaaymusicTheme
import com.shivaay20005.shivaaymusic.ui.theme.extractThemeColor
import com.shivaay20005.shivaaymusic.ui.utils.appBarScrollBehavior
import com.shivaay20005.shivaaymusic.ui.utils.backToMain
import com.shivaay20005.shivaaymusic.ui.utils.resetHeightOffset
import com.shivaay20005.shivaaymusic.utils.SyncUtils
import com.shivaay20005.shivaaymusic.utils.Updater
import com.shivaay20005.shivaaymusic.utils.dataStore
import com.shivaay20005.shivaaymusic.utils.get
import com.shivaay20005.shivaaymusic.utils.rememberEnumPreference
import com.shivaay20005.shivaaymusic.utils.rememberPreference
import com.shivaay20005.shivaaymusic.utils.reportException
import com.valentinilk.shimmer.LocalShimmerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

// The original code of the application belongs to: Shivaay (shivaay20005) Any resemblance is copy and paste of my original code

@Suppress("DEPRECATION", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var database: MusicDatabase

    @Inject
    lateinit var downloadUtil: DownloadUtil

    @Inject
    lateinit var syncUtils: SyncUtils

    private var playerConnection by mutableStateOf<PlayerConnection?>(null)
    private val serviceConnection =
        object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?,
            ) {
                if (service is MusicBinder) {
                    playerConnection =
                        PlayerConnection(this@MainActivity, service, database, lifecycleScope)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                playerConnection?.dispose()
                playerConnection = null
            }
        }

    private var latestVersionName by mutableStateOf(BuildConfig.VERSION_NAME)

    override fun onStart() {
        super.onStart()
        startService(Intent(this, MusicService::class.java))
        bindService(
            Intent(this, MusicService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }

    @SuppressLint("ImplicitSamInstance")
    override fun onDestroy() {
        super.onDestroy()
        if (dataStore.get(
                StopMusicOnTaskClearKey,
                false
            ) && playerConnection?.isPlaying?.value == true && isFinishing
        ) {
            stopService(Intent(this, MusicService::class.java))
            unbindService(serviceConnection)
            playerConnection = null
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocaleManager.getInstance(newBase).applyLocaleToContext(newBase)
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            dataStore.data
                .map { it[DisableScreenshotKey] ?: false }
                .distinctUntilChanged()
                .collectLatest {
                    if (it) {
                        window.setFlags(
                            WindowManager.LayoutParams.FLAG_SECURE,
                            WindowManager.LayoutParams.FLAG_SECURE,
                        )
                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                    }
                }
        }

        intent?.let { handlevideoIdIntent(it) }

        setContent {
            LaunchedEffect(Unit) {
                if (System.currentTimeMillis() - Updater.lastCheckTime > 1.days.inWholeMilliseconds) {
                    Updater.getLatestVersionName().onSuccess {
                        latestVersionName = it
                    }
                }
            }

            var showFullscreenLyrics by remember { mutableStateOf(false) }


            var sliderPosition by remember { mutableStateOf<Long?>(null) }

            val enableDynamicTheme by rememberPreference(DynamicThemeKey, defaultValue = true)
            val darkTheme by rememberEnumPreference(DarkModeKey, defaultValue = DarkMode.AUTO)

            val pureBlack by rememberPreference(PureBlackKey, defaultValue = false)
            val isSystemInDarkTheme = isSystemInDarkTheme()
            val useDarkTheme =
                remember(darkTheme, isSystemInDarkTheme) {
                    if (darkTheme == DarkMode.AUTO) isSystemInDarkTheme else darkTheme == DarkMode.ON
                }
            LaunchedEffect(useDarkTheme) {
                setSystemBarAppearance(useDarkTheme)
            }
            var themeColor by rememberSaveable(stateSaver = ColorSaver) {
                mutableStateOf(DefaultThemeColor)
            }

            LaunchedEffect(playerConnection, enableDynamicTheme, isSystemInDarkTheme) {
                val playerConnection = playerConnection
                if (!enableDynamicTheme || playerConnection == null) {
                    themeColor = DefaultThemeColor
                    return@LaunchedEffect
                }
                playerConnection.service.currentMediaMetadata.collectLatest { song ->
                    themeColor =
                        if (song != null) {
                            withContext(Dispatchers.IO) {
                                val result =
                                    imageLoader.execute(
                                        ImageRequest
                                            .Builder(this@MainActivity)
                                            .data(song.thumbnailUrl)
                                            .allowHardware(false) // pixel access is not supported on Config#HARDWARE bitmaps
                                            .build(),
                                    )
                                (result.drawable as? BitmapDrawable)?.bitmap?.extractThemeColor()
                                    ?: DefaultThemeColor
                            }
                        } else {
                            DefaultThemeColor
                        }
                }
            }

            shivaaymusicTheme(
                darkTheme = useDarkTheme,
                pureBlack = pureBlack,
                themeColor = themeColor,
            ) {
                BoxWithConstraints(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface),
                ) {
                    val focusManager = LocalFocusManager.current
                    val density = LocalDensity.current
                    val windowsInsets = WindowInsets.systemBars
                    val bottomInset = with(density) { windowsInsets.getBottom(density).toDp() }

                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val (previousTab) = rememberSaveable { mutableStateOf("home") }

                    val navigationItems = remember { Screens.MainScreens }
                    val (slimNav) = rememberPreference(SlimNavBarKey, defaultValue = false)
                    val defaultOpenTab =
                        remember {
                            dataStore[DefaultOpenTabKey].toEnum(defaultValue = NavigationTab.HOME)
                        }
                    val tabOpenedFromShortcut =
                        remember {
                            when (intent?.action) {
                                ACTION_LIBRARY -> NavigationTab.LIBRARY
                                ACTION_EXPLORE -> NavigationTab.EXPLORE
                                else -> null
                            }
                        }

                    val topLevelScreens =
                        listOf(
                            Screens.Home.route,
                            Screens.Explore.route,
                            Screens.Library.route,
                            "settings",
                        )

                    val (query, onQueryChange) =
                        rememberSaveable(stateSaver = TextFieldValue.Saver) {
                            mutableStateOf(TextFieldValue())
                        }

                    var active by rememberSaveable {
                        mutableStateOf(false)
                    }

                    val onActiveChange: (Boolean) -> Unit = { newActive ->
                        active = newActive
                        if (!newActive) {
                            focusManager.clearFocus()
                            if (navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route }) {
                                onQueryChange(TextFieldValue())
                            }
                        }
                    }

                    var searchSource by rememberEnumPreference(SearchSourceKey, SearchSource.ONLINE)

                    val searchBarFocusRequester = remember { FocusRequester() }

                    val onSearch: (String) -> Unit = {
                        if (it.isNotEmpty()) {
                            onActiveChange(false)
                            navController.navigate("search/${URLEncoder.encode(it, "UTF-8")}")
                            if (dataStore[PauseSearchHistoryKey] != true) {
                                database.query {
                                    insert(SearchHistory(query = it))
                                }
                            }
                        }
                    }

                    var openSearchImmediately: Boolean by remember {
                        mutableStateOf(intent?.action == ACTION_SEARCH)
                    }

                    val shouldShowSearchBar =
                        remember(active, navBackStackEntry) {
                            active ||
                                    navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route } ||
                                    navBackStackEntry?.destination?.route?.startsWith("search/") == true
                        }

                    val shouldShowNavigationBar =
                        remember(navBackStackEntry, active) {
                            navBackStackEntry?.destination?.route == null ||
                                    navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route } &&
                                    !active
                        }

                    val navigationBarHeight by animateDpAsState(
                        targetValue = if (shouldShowNavigationBar) NavigationBarHeight else 0.dp,
                        animationSpec = NavigationBarAnimationSpec,
                        label = "",
                    )

                    val playerBottomSheetState =
                        rememberBottomSheetState(
                            dismissedBound = 0.dp,
                            collapsedBound = bottomInset + (if (shouldShowNavigationBar) NavigationBarHeight else 0.dp) + MiniPlayerHeight,
                            expandedBound = maxHeight,
                        )

                    val playerAwareWindowInsets =
                        remember(
                            bottomInset,
                            shouldShowNavigationBar,
                            playerBottomSheetState.isDismissed
                        ) {
                            var bottom = bottomInset
                            if (shouldShowNavigationBar) bottom += NavigationBarHeight
                            if (!playerBottomSheetState.isDismissed) bottom += MiniPlayerHeight
                            windowsInsets
                                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                                .add(WindowInsets(top = AppBarHeight, bottom = bottom))
                        }

                    appBarScrollBehavior(
                        canScroll = {
                            navBackStackEntry?.destination?.route?.startsWith("search/") == false &&
                                    (playerBottomSheetState.isCollapsed || playerBottomSheetState.isDismissed)
                        }
                    )

                    val searchBarScrollBehavior =
                        appBarScrollBehavior(
                            canScroll = {
                                navBackStackEntry?.destination?.route?.startsWith("search/") == false &&
                                        (playerBottomSheetState.isCollapsed || playerBottomSheetState.isDismissed)
                            },
                        )
                    val topAppBarScrollBehavior =
                        appBarScrollBehavior(
                            canScroll = {
                                navBackStackEntry?.destination?.route?.startsWith("search/") == false &&
                                        (playerBottomSheetState.isCollapsed || playerBottomSheetState.isDismissed)
                            },
                        )

                    LaunchedEffect(navBackStackEntry) {
                        if (navBackStackEntry?.destination?.route?.startsWith("search/") == true) {
                            val searchQuery =
                                withContext(Dispatchers.IO) {
                                    if (navBackStackEntry
                                            ?.arguments
                                            ?.getString(
                                                "query",
                                            )!!
                                            .contains(
                                                "%",
                                            )
                                    ) {
                                        navBackStackEntry?.arguments?.getString(
                                            "query",
                                        )!!
                                    } else {
                                        URLDecoder.decode(
                                            navBackStackEntry?.arguments?.getString("query")!!,
                                            "UTF-8"
                                        )
                                    }
                                }
                            onQueryChange(
                                TextFieldValue(
                                    searchQuery,
                                    TextRange(searchQuery.length)
                                )
                            )
                        } else if (navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route }) {
                            onQueryChange(TextFieldValue())
                        }
                        searchBarScrollBehavior.state.resetHeightOffset()
                        topAppBarScrollBehavior.state.resetHeightOffset()
                    }
                    LaunchedEffect(active) {
                        if (active) {
                            searchBarScrollBehavior.state.resetHeightOffset()
                            topAppBarScrollBehavior.state.resetHeightOffset()
                            searchBarFocusRequester.requestFocus()
                        }
                    }

                    LaunchedEffect(playerConnection) {
                        val player = playerConnection?.player ?: return@LaunchedEffect
                        if (player.currentMediaItem == null) {
                            if (!playerBottomSheetState.isDismissed) {
                                playerBottomSheetState.dismiss()
                            }
                        } else {
                            if (playerBottomSheetState.isDismissed) {
                                playerBottomSheetState.collapseSoft()
                            }
                        }
                    }

                    DisposableEffect(playerConnection, playerBottomSheetState) {
                        val player =
                            playerConnection?.player ?: return@DisposableEffect onDispose { }
                        val listener =
                            object : Player.Listener {
                                override fun onMediaItemTransition(
                                    mediaItem: MediaItem?,
                                    reason: Int,
                                ) {
                                    if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED &&
                                        mediaItem != null &&
                                        playerBottomSheetState.isDismissed
                                    ) {
                                        playerBottomSheetState.collapseSoft()
                                    }
                                }
                            }
                        player.addListener(listener)
                        onDispose {
                            player.removeListener(listener)
                        }
                    }

                    var shouldShowTopBar by rememberSaveable { mutableStateOf(false) }

                    LaunchedEffect(navBackStackEntry) {
                        shouldShowTopBar =
                            !active && navBackStackEntry?.destination?.route in topLevelScreens && navBackStackEntry?.destination?.route != "settings"
                    }

                    val coroutineScope = rememberCoroutineScope()
                    var sharedSong: SongItem? by remember {
                        mutableStateOf(null)
                    }
                    DisposableEffect(Unit) {
                        val listener =
                            Consumer<Intent> { intent ->
                                val uri = intent.data ?: intent.extras?.getString(Intent.EXTRA_TEXT)
                                    ?.toUri() ?: return@Consumer
                                when (val path = uri.pathSegments.firstOrNull()) {
                                    "playlist" ->
                                        uri.getQueryParameter("list")?.let { playlistId ->
                                            if (playlistId.startsWith("OLAK5uy_")) {
                                                coroutineScope.launch {
                                                    YouTube
                                                        .albumSongs(playlistId)
                                                        .onSuccess { songs ->
                                                            songs.firstOrNull()?.album?.id?.let { browseId ->
                                                                navController.navigate("album/$browseId")
                                                            }
                                                        }.onFailure {
                                                            reportException(it)
                                                        }
                                                }
                                            } else {
                                                navController.navigate("online_playlist/$playlistId")
                                            }
                                        }

                                    "browse" ->
                                        uri.lastPathSegment?.let { browseId ->
                                            navController.navigate("album/$browseId")
                                        }

                                    "channel", "c" ->
                                        uri.lastPathSegment?.let { artistId ->
                                            navController.navigate("artist/$artistId")
                                        }

                                    else ->
                                        when {
                                            path == "watch" -> uri.getQueryParameter("v")
                                            uri.host == "youtu.be" -> path
                                            else -> null
                                        }?.let { videoId ->
                                            coroutineScope.launch {
                                                withContext(Dispatchers.IO) {
                                                    YouTube.queue(listOf(videoId))
                                                }.onSuccess {
                                                    playerConnection?.playQueue(
                                                        YouTubeQueue(
                                                            WatchEndpoint(videoId = it.firstOrNull()?.id),
                                                            it.firstOrNull()?.toMediaMetadata()
                                                        )
                                                    )
                                                }.onFailure {
                                                    reportException(it)
                                                }
                                            }
                                        }
                                }
                            }

                        addOnNewIntentListener(listener)
                        onDispose { removeOnNewIntentListener(listener) }
                    }

                    val currentTitle = remember(navBackStackEntry) {
                        when (navBackStackEntry?.destination?.route) {
                            Screens.Home.route -> R.string.home
                            Screens.Explore.route -> R.string.explore
                            Screens.Library.route -> R.string.filter_library
                            else -> null
                        }
                    }

                    CompositionLocalProvider(
                        LocalDatabase provides database,
                        LocalContentColor provides contentColorFor(MaterialTheme.colorScheme.surface),
                        LocalPlayerConnection provides playerConnection,
                        LocalPlayerAwareWindowInsets provides playerAwareWindowInsets,
                        LocalDownloadUtil provides downloadUtil,
                        LocalShimmerTheme provides ShimmerTheme,
                        LocalSyncUtils provides syncUtils,
                    ) {
                        Scaffold(
                            topBar = {
                                val playerBackground by rememberEnumPreference(
                                    key = PlayerBackgroundStyleKey,
                                    defaultValue = PlayerBackgroundStyle.DEFAULT
                                )

                                if (shouldShowTopBar) {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        // Base layer with background color always visible
                                        Box(
                                            modifier = Modifier
                                                .matchParentSize()
                                                .background(MaterialTheme.colorScheme.surface)
                                        )

                                        // Safer validation for the background
                                        val safeSelectedValue = when {
                                            playerBackground == PlayerBackgroundStyle.BLUR &&
                                                    Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> {
                                                PlayerBackgroundStyle.DEFAULT // No blur on versions < Android 12 (S)
                                            }

                                            else -> playerBackground
                                        }

                                        // Only show blur if safeSelectedValue is BLUR
                                        if (safeSelectedValue == PlayerBackgroundStyle.BLUR) {
                                            val playerConnection = LocalPlayerConnection.current

                                            // Safer verification of playerConnection
                                            playerConnection?.let { connection ->
                                                val mediaMetadata by connection.mediaMetadata.collectAsState()

                                                mediaMetadata?.thumbnailUrl?.let { imageUrl ->
                                                    AsyncImage(
                                                        model = imageUrl,
                                                        contentDescription = null,
                                                        contentScale = ContentScale.FillBounds,
                                                        modifier = Modifier
                                                            .matchParentSize()
                                                            .blur(35.dp)
                                                            .alpha(0.6f)
                                                            .drawWithContent {
                                                                drawContent()
                                                                drawRect(
                                                                    brush = Brush.verticalGradient(
                                                                        colors = listOf(
                                                                            Color.Black.copy(alpha = 0.5f),
                                                                            Color.Transparent
                                                                        ),
                                                                        startY = 0f,
                                                                        endY = size.height * 0.6f
                                                                    ),
                                                                    blendMode = BlendMode.DstIn
                                                                )
                                                            },
                                                    )
                                                }
                                            }
                                        }

                                        TopAppBar(
                                            title = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Image(
                                                        painter = painterResource(R.drawable.shivaaymusic),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(27.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = stringResource(R.string.app_name),
                                                        style = MaterialTheme.typography.titleLarge,
                                                        fontWeight = FontWeight.Bold,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                            },

                                            actions = {
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    val context = LocalContext.current

                                                    // Notification icon for new releases
                                                    IconButton(
                                                        onClick = {
                                                            try {
                                                                navController.navigate("new_release")
                                                            } catch (e: Exception) {
                                                                logErrorToDownloads(context, e)
                                                                Toast.makeText(
                                                                    context,
                                                                    R.string.navigation_error,
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(R.drawable.notification_on),
                                                            contentDescription = stringResource(R.string.new_release_albums),
                                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                    IconButton(
                                                        onClick = { onActiveChange(true) }
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(R.drawable.search),
                                                            contentDescription = stringResource(R.string.search),
                                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }


                                                    ProfileIconWithUpdateBadge(
                                                        currentVersion = BuildConfig.VERSION_NAME,
                                                        onProfileClick = {
                                                            try {
                                                                navController.navigate("settings")
                                                            } catch (e: Exception) {
                                                                logErrorToDownloads(context, e)
                                                                // Show error message to user
                                                                Toast.makeText(
                                                                    context,
                                                                    R.string.navigation_error,
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }
                                                    )
                                                }
                                            },
                                            scrollBehavior = searchBarScrollBehavior,
                                            colors = TopAppBarDefaults.topAppBarColors(
                                                containerColor = Color.Transparent
                                            )
                                        )
                                    }
                                }

                                // Safer verification for the route
                                val isSearchRoute =
                                    navBackStackEntry?.destination?.route?.startsWith("search/") == true

                                if (active || isSearchRoute) {
                                    TopSearch(
                                        query = query,
                                        onQueryChange = onQueryChange,
                                        onSearch = onSearch,
                                        active = active,
                                        onActiveChange = onActiveChange,
                                        placeholder = {
                                            Text(
                                                text = stringResource(
                                                    when (searchSource) {
                                                        SearchSource.LOCAL -> R.string.search_library
                                                        SearchSource.ONLINE -> R.string.search_yt_music
                                                    }
                                                ),
                                            )
                                        },
                                        leadingIcon = {
                                            val currentRoute = navBackStackEntry?.destination?.route
                                            val isInNavigationItems =
                                                navigationItems.fastAny { it.route == currentRoute }
                                            val context = LocalContext.current

                                            IconButton(
                                                onClick = {
                                                    when {
                                                        active -> onActiveChange(false)
                                                        !isInNavigationItems -> {
                                                            try {
                                                                // Enhanced navigation safety check
                                                                if (navController.previousBackStackEntry != null) {
                                                                    navController.navigateUp()
                                                                } else {
                                                                    // Fallback to main screen if no previous entry
                                                                    navController.navigate(Screens.Home.route) {
                                                                        popUpTo(navController.graph.startDestinationId) {
                                                                            inclusive = false
                                                                        }
                                                                        launchSingleTop = true
                                                                    }
                                                                }
                                                            } catch (e: Exception) {
                                                                Log.e(
                                                                    "Navigation",
                                                                    "Error navigating up: ${e.message}",
                                                                    e
                                                                )
                                                                // Emergency fallback to home screen
                                                                try {
                                                                    navController.navigate(Screens.Home.route) {
                                                                        popUpTo(navController.graph.startDestinationId) {
                                                                            inclusive = false
                                                                        }
                                                                        launchSingleTop = true
                                                                    }
                                                                } catch (fallbackException: Exception) {
                                                                    Log.e(
                                                                        "Navigation",
                                                                        "Emergency fallback failed: ${fallbackException.message}",
                                                                        fallbackException
                                                                    )
                                                                    // Show toast to user about navigation issue
                                                                    Toast.makeText(
                                                                        context,
                                                                        context.getString(R.string.navigation_error),
                                                                        Toast.LENGTH_SHORT
                                                                    ).show()
                                                                }
                                                            }
                                                        }

                                                        else -> onActiveChange(true)
                                                    }
                                                },
                                                onLongClick = {
                                                    when {
                                                        active -> { /* No action */
                                                        }

                                                        !isInNavigationItems -> {
                                                            try {
                                                                navController.backToMain()
                                                            } catch (e: Exception) {
                                                                Log.e(
                                                                    "Navigation",
                                                                    "Error navigating to main: ${e.message}",
                                                                    e
                                                                )
                                                                // Show user-friendly error message
                                                                Toast.makeText(
                                                                    context,
                                                                    context.getString(R.string.navigation_error),
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }

                                                        else -> { /* No action */
                                                        }
                                                    }
                                                },
                                            ) {
                                                Icon(
                                                    painterResource(
                                                        if (active || !isInNavigationItems) {
                                                            R.drawable.arrow_back
                                                        } else {
                                                            R.drawable.search
                                                        }
                                                    ),
                                                    contentDescription = stringResource(
                                                        if (active || !isInNavigationItems) {
                                                            R.string.back
                                                        } else {
                                                            R.string.search
                                                        }
                                                    ),
                                                )
                                            }
                                        },
                                        trailingIcon = {
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                if (active) {
                                                    if (query.text.isNotEmpty()) {
                                                        IconButton(
                                                            onClick = {
                                                                onQueryChange(TextFieldValue(""))
                                                            },
                                                        ) {
                                                            Icon(
                                                                painter = painterResource(R.drawable.close),
                                                                contentDescription = null,
                                                            )
                                                        }
                                                    }
                                                    IconButton(
                                                        onClick = {
                                                            searchSource =
                                                                if (searchSource == SearchSource.ONLINE) {
                                                                    SearchSource.LOCAL
                                                                } else {
                                                                    SearchSource.ONLINE
                                                                }
                                                        },
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(
                                                                when (searchSource) {
                                                                    SearchSource.LOCAL -> R.drawable.library_music
                                                                    SearchSource.ONLINE -> R.drawable.language
                                                                }
                                                            ),
                                                            contentDescription = stringResource(
                                                                when (searchSource) {
                                                                    SearchSource.LOCAL -> R.string.search_online
                                                                    SearchSource.ONLINE -> R.string.search_library
                                                                }
                                                            ),
                                                        )
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .focusRequester(searchBarFocusRequester)
                                            .align(Alignment.TopCenter)
                                            .fillMaxWidth(),
                                        focusRequester = searchBarFocusRequester
                                    ) {
                                        Crossfade(
                                            targetState = searchSource,
                                            label = "search_content_transition",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(
                                                    bottom = if (!playerBottomSheetState.isDismissed) {
                                                        MiniPlayerHeight
                                                    } else {
                                                        0.dp
                                                    }
                                                )
                                                .navigationBarsPadding(),
                                        ) { currentSearchSource ->
                                            when (currentSearchSource) {
                                                SearchSource.LOCAL -> LocalSearchScreen(
                                                    query = query.text,
                                                    navController = navController,
                                                    onDismiss = { onActiveChange(false) },
                                                    pureBlack = pureBlack,
                                                )

                                                SearchSource.ONLINE -> OnlineSearchScreen(
                                                    query = query.text,
                                                    onQueryChange = onQueryChange,
                                                    navController = navController,
                                                    onSearch = { searchQuery ->
                                                        try {
                                                            val encodedQuery = URLEncoder.encode(
                                                                searchQuery,
                                                                "UTF-8"
                                                            )
                                                            navController.navigate("search/$encodedQuery")

                                                            // Check preferences before saving history
                                                            if (dataStore[PauseSearchHistoryKey] != true) {
                                                                database.query {
                                                                    insert(SearchHistory(query = searchQuery))
                                                                }
                                                            }
                                                        } catch (e: Exception) {
                                                            Log.e(
                                                                "SearchNavigation",
                                                                "Error navigating to search: ${e.message}",
                                                                e
                                                            )
                                                        }
                                                    },
                                                    onDismiss = { onActiveChange(false) },
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            bottomBar = {
                                Box {
                                    BottomSheetPlayer(
                                        state = playerBottomSheetState,
                                        navController = navController,
                                        onOpenFullscreenLyrics = {
                                            showFullscreenLyrics = true
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )



                                    AnimatedVisibility(
                                        visible = showFullscreenLyrics,
                                        enter = slideInVertically(
                                            initialOffsetY = { it },
                                            animationSpec = tween(300)
                                        ) + fadeIn(animationSpec = tween(300)),
                                        exit = slideOutVertically(
                                            targetOffsetY = { it },
                                            animationSpec = tween(300)
                                        ) + fadeOut(animationSpec = tween(300))
                                    ) {
                                        Lyrics(
                                            sliderPositionProvider = {
                                                sliderPosition
                                            },
                                            onNavigateBack = {
                                                showFullscreenLyrics = false
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    NavigationBar(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(15.dp))
                                            .align(Alignment.BottomCenter)
                                            .offset {
                                                if (navigationBarHeight == 0.dp) {
                                                    IntOffset(
                                                        x = 0,
                                                        y = (bottomInset + NavigationBarHeight).roundToPx(),
                                                    )
                                                } else {
                                                    val slideOffset =
                                                        (bottomInset + NavigationBarHeight) *
                                                                playerBottomSheetState.progress.coerceIn(
                                                                    0f,
                                                                    1f
                                                                )
                                                    val hideOffset =
                                                        (bottomInset + NavigationBarHeight) *
                                                                (1 - navigationBarHeight / NavigationBarHeight)
                                                    IntOffset(
                                                        x = 0,
                                                        y = (slideOffset + hideOffset).roundToPx(),
                                                    )
                                                }
                                            },
                                    ) {
                                        var lastTapTime by remember { mutableLongStateOf(0L) }
                                        var lastTappedIcon by remember { mutableStateOf<Int?>(null) }
                                        var navigateToExplore by remember { mutableStateOf(false) }

                                        navigationItems.fastForEach { screen ->
                                            val isSelected =
                                                navBackStackEntry?.destination?.hierarchy?.any {
                                                    it.route == screen.route
                                                } == true

                                            NavigationBarItem(
                                                selected = isSelected,
                                                icon = {
                                                    Icon(
                                                        painter = painterResource(
                                                            id = if (isSelected) {
                                                                screen.iconIdActive
                                                            } else {
                                                                screen.iconIdInactive
                                                            }
                                                        ),
                                                        contentDescription = stringResource(screen.titleId),
                                                    )
                                                },
                                                label = {
                                                    if (!slimNav) {
                                                        Text(
                                                            text = stringResource(screen.titleId),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }
                                                },
                                                onClick = {
                                                    val currentTapTime = System.currentTimeMillis()
                                                    val timeSinceLastTap =
                                                        currentTapTime - lastTapTime
                                                    val isDoubleTap =
                                                        screen.titleId == R.string.explore &&
                                                                lastTappedIcon == R.string.explore &&
                                                                timeSinceLastTap < 300L

                                                    lastTapTime = currentTapTime
                                                    lastTappedIcon = screen.titleId

                                                    if (screen.titleId == R.string.explore) {
                                                        if (isDoubleTap) {
                                                            onActiveChange(true)
                                                            navigateToExplore = false
                                                        } else {
                                                            navigateToExplore = true
                                                            coroutineScope.launch {
                                                                delay(300L)
                                                                if (navigateToExplore) {
                                                                    try {
                                                                        navigateToScreen(
                                                                            navController,
                                                                            screen
                                                                        )
                                                                    } catch (e: Exception) {
                                                                        Log.e(
                                                                            "Navigation",
                                                                            "Error navigating to screen",
                                                                            e
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (isSelected) {
                                                            // Scroll to top on current screen
                                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                                "scrollToTop",
                                                                true
                                                            )
                                                            coroutineScope.launch {
                                                                try {
                                                                    searchBarScrollBehavior.state.resetHeightOffset()
                                                                } catch (e: Exception) {
                                                                    Log.e(
                                                                        "ScrollBehavior",
                                                                        "Error resetting scroll",
                                                                        e
                                                                    )
                                                                }
                                                            }
                                                        } else {
                                                            try {
                                                                navigateToScreen(
                                                                    navController,
                                                                    screen
                                                                )
                                                            } catch (e: Exception) {
                                                                Log.e(
                                                                    "Navigation",
                                                                    "Error navigating to screen",
                                                                    e
                                                                )
                                                            }
                                                        }
                                                    }
                                                },
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(searchBarScrollBehavior.nestedScrollConnection)
                                .background(MaterialTheme.colorScheme.surface)
                        ) {
                            var transitionDirection =
                                AnimatedContentTransitionScope.SlideDirection.Left

                            if (navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route }) {
                                if (navigationItems.fastAny { it.route == previousTab }) {
                                    val curIndex = navigationItems.indexOf(
                                        navigationItems.fastFirstOrNull {
                                            it.route == navBackStackEntry?.destination?.route
                                        }
                                    )

                                    val prevIndex = navigationItems.indexOf(
                                        navigationItems.fastFirstOrNull {
                                            it.route == previousTab
                                        }
                                    )

                                    if (prevIndex > curIndex)
                                        AnimatedContentTransitionScope.SlideDirection.Right.also {
                                            transitionDirection = it
                                        }
                                }
                            }

                            NavHost(
                                navController = navController,
                                startDestination = when (tabOpenedFromShortcut ?: defaultOpenTab) {
                                    NavigationTab.HOME -> Screens.Home
                                    NavigationTab.EXPLORE -> Screens.Explore
                                    NavigationTab.LIBRARY -> Screens.Library
                                }.route,
                                enterTransition = {
                                    if (initialState.destination.route in topLevelScreens && targetState.destination.route in topLevelScreens) {
                                        fadeIn(tween(250))
                                    } else {
                                        fadeIn(tween(250)) + slideInHorizontally { it / 2 }
                                    }
                                },
                                exitTransition = {
                                    if (initialState.destination.route in topLevelScreens && targetState.destination.route in topLevelScreens) {
                                        fadeOut(tween(200))
                                    } else {
                                        fadeOut(tween(200)) + slideOutHorizontally { -it / 2 }
                                    }
                                },
                                popEnterTransition = {
                                    if ((initialState.destination.route in topLevelScreens || initialState.destination.route?.startsWith(
                                            "search/"
                                        ) == true) && targetState.destination.route in topLevelScreens
                                    ) {
                                        fadeIn(tween(250))
                                    } else {
                                        fadeIn(tween(250)) + slideInHorizontally { -it / 2 }
                                    }
                                },
                                popExitTransition = {
                                    if ((initialState.destination.route in topLevelScreens || initialState.destination.route?.startsWith(
                                            "search/"
                                        ) == true) && targetState.destination.route in topLevelScreens
                                    ) {
                                        fadeOut(tween(200))
                                    } else {
                                        fadeOut(tween(200)) + slideOutHorizontally { it / 2 }
                                    }
                                },
                                modifier = Modifier.nestedScroll(
                                    if (navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route } ||
                                        navBackStackEntry?.destination?.route?.startsWith("search/") == true
                                    ) {
                                        searchBarScrollBehavior.nestedScrollConnection
                                    } else {
                                        topAppBarScrollBehavior.nestedScrollConnection
                                    }
                                )
                            ) {
                                navigationBuilder(
                                    navController,
                                    topAppBarScrollBehavior,
                                    latestVersionName
                                )
                            }
                        }

                        BottomSheetMenu(
                            state = LocalMenuState.current,
                            modifier = Modifier.align(Alignment.BottomCenter)
                        )

                        sharedSong?.let { song ->
                            playerConnection?.let {
                                Dialog(
                                    onDismissRequest = { sharedSong = null },
                                    properties = DialogProperties(usePlatformDefaultWidth = false),
                                ) {
                                    Surface(
                                        modifier = Modifier.padding(24.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        color = AlertDialogDefaults.containerColor,
                                        tonalElevation = AlertDialogDefaults.TonalElevation,
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            YouTubeSongMenu(
                                                song = song,
                                                navController = navController,
                                                onDismiss = { sharedSong = null },
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    LaunchedEffect(shouldShowSearchBar, openSearchImmediately) {
                        if (shouldShowSearchBar && openSearchImmediately) {
                            onActiveChange(true)
                            try {
                                delay(100)
                                searchBarFocusRequester.requestFocus()
                            } catch (_: Exception) {
                            }
                            openSearchImmediately = false
                        }
                    }
                }
            }
        }
    }

    private fun navigateToScreen(
        navController: NavHostController,
        screen: Screens
    ) {
        navController.navigate(screen.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    private fun handlevideoIdIntent(intent: Intent) {
        val uri = intent.data ?: intent.extras?.getString(Intent.EXTRA_TEXT)?.toUri() ?: return
        when {
            uri.pathSegments.firstOrNull() == "watch" -> uri.getQueryParameter("v")
            uri.host == "youtu.be" -> uri.pathSegments.firstOrNull()
            else -> null
        }?.let { videoId ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    YouTube.queue(listOf(videoId))
                }.onSuccess {
                    playerConnection?.playQueue(
                        YouTubeQueue(
                            WatchEndpoint(videoId = it.firstOrNull()?.id),
                            it.firstOrNull()?.toMediaMetadata()
                        )
                    )
                }.onFailure {
                    reportException(it)
                }
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun setSystemBarAppearance(isDark: Boolean) {
        WindowCompat.getInsetsController(window, window.decorView.rootView).apply {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.statusBarColor =
                (if (isDark) Color.Transparent else Color.Black.copy(alpha = 0.2f)).toArgb()
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            window.navigationBarColor =
                (if (isDark) Color.Transparent else Color.Black.copy(alpha = 0.2f)).toArgb()
        }
    }

    companion object {
        const val ACTION_SEARCH = "com.shivaay20005.shivaaymusic.action.SEARCH"
        const val ACTION_EXPLORE = "com.shivaay20005.shivaaymusic.action.EXPLORE"
        const val ACTION_LIBRARY = "com.shivaay20005.shivaaymusic.action.LIBRARY"
    }
}

val LocalDatabase = staticCompositionLocalOf<MusicDatabase> { error("No database provided") }
val LocalPlayerConnection =
    staticCompositionLocalOf<PlayerConnection?> { error("No PlayerConnection provided") }
val LocalPlayerAwareWindowInsets =
    compositionLocalOf<WindowInsets> { error("No WindowInsets provided") }
val LocalDownloadUtil = staticCompositionLocalOf<DownloadUtil> { error("No DownloadUtil provided") }
val LocalSyncUtils = staticCompositionLocalOf<SyncUtils> { error("No SyncUtils provided") }

@Composable
fun NotificationPermissionPreference() {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    // Function to check permissions extracted for better readability
    val checkNotificationPermission = remember {
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED // Usar PackageManager en lugar de PermissionChecker
            } else {
                // For earlier versions, check if notifications are enabled
                NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        // Optional: add callback to handle the result
        if (!isGranted) {
            // Handle case when user rejects permission
            Log.d("NotificationPermission", "Notification permissions denied")
        }
    }

    // Check permissions on initialization and when app returns to foreground
    LaunchedEffect(Unit) {
        permissionGranted = checkNotificationPermission()
    }

    // Listen for changes when app returns from background
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionGranted = checkNotificationPermission()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    SwitchPreference(
        title = { Text(stringResource(R.string.notification)) },
        icon = {
            Icon(
                painter = painterResource(
                    id = if (permissionGranted) R.drawable.notification_on
                    else R.drawable.notification_off
                ),
                contentDescription = stringResource(
                    if (permissionGranted) R.string.notifications_enabled
                    else R.string.notifications_disabled
                )
            )
        },
        checked = permissionGranted,
        onCheckedChange = { checked ->
            when {
                checked && !permissionGranted -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        // For earlier versions, direct to settings
                        openNotificationSettings(context)
                    }
                }

                !checked && permissionGranted -> {
                    // If user tries to deactivate, direct to system settings
                    openNotificationSettings(context)
                }
            }
        }
    )
}

@Composable
fun SwitchPreference(
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true, // Add enabled parameter
    subtitle: (@Composable () -> Unit)? = null // Add optional subtitle
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onCheckedChange(!checked) }, // Make entire surface clickable
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center // Center the icon
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides if (enabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    }
                ) {
                    icon()
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides if (enabled) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    }
                ) {
                    title()
                }

                subtitle?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    CompositionLocalProvider(
                        LocalContentColor provides if (enabled) {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                        }
                    ) {
                        it()
                    }
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled
            )
        }
    }
}

// Helper function to open notification settings
private fun openNotificationSettings(context: Context) {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
    } else {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e("NotificationSettings", "Could not open notification settings", e)
        // Fallback: open general settings
        context.startActivity(Intent(Settings.ACTION_SETTINGS))
    }
}

suspend fun checkForUpdates(): String? = withContext(Dispatchers.IO) {
    try {
        val url = URL("https://api.github.com/repos/shivaay20005/shivaaymusic/releases/latest")
        val connection = url.openConnection()
        connection.connect()
        val json = connection.getInputStream().bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)
        return@withContext jsonObject.getString("tag_name")
    } catch (e: Exception) {
        e.printStackTrace()
        return@withContext null
    }
}

fun isNewerVersion(remoteVersion: String, currentVersion: String): Boolean {
    val remote = remoteVersion.removePrefix("v").split(".").map { it.toIntOrNull() ?: 0 }
    val current = currentVersion.removePrefix("v").split(".").map { it.toIntOrNull() ?: 0 }

    for (i in 0 until maxOf(remote.size, current.size)) {
        val r = remote.getOrNull(i) ?: 0
        val c = current.getOrNull(i) ?: 0
        if (r > c) return true
        if (r < c) return false
    }
    return false
}

@Composable
fun ProfileIconWithUpdateBadge(
    currentVersion: String,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val avatarManager = remember { AvatarPreferenceManager(context) }
    val customAvatarUri by avatarManager.getCustomAvatarUri.collectAsState(initial = null)
    var showUpdateBadge by remember { mutableStateOf(false) }
    val updatedOnClick = rememberUpdatedState(onProfileClick)

    // Safe update control
    LaunchedEffect(currentVersion) {
        try {
            val latestVersion = withContext(Dispatchers.IO) { checkForUpdates() }
            showUpdateBadge = latestVersion?.let { isNewerVersion(it, currentVersion) } ?: false
        } catch (e: Exception) {
            Timber.tag("ProfileIcon").e("Error checking for updates: ${e.message}")
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                try {
                    updatedOnClick.value()
                } catch (e: Exception) {
                    logErrorToDownloads(context, e)
                }
            }
    ) {
        // Avatar or default icon
        Box(contentAlignment = Alignment.Center) {
            customAvatarUri?.toUri()?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(uri)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = "Custom avatar",
                    modifier = modifier
                        .size(28.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Icon(
                    painter = painterResource(R.drawable.person),
                    contentDescription = "Default avatar",
                    modifier = modifier
                )
            }

            // Update badge
            if (showUpdateBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(28.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.update),
                        contentDescription = "Update available",
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}


fun logErrorToDownloads(context: Context, e: Exception) {
    val fileName = "error_log_${
        SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
    }.txt"
    val logText = buildString {
        append("Exception: ${e.message}\n")
        append("Stacktrace:\n")
        e.stackTrace.forEach { append("$it\n") }
    }

    try {
        val outputStream: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri == null) {
                Toast.makeText(context, "Error creating file in Downloads", Toast.LENGTH_LONG)
                    .show()
                return
            }

            val stream = resolver.openOutputStream(uri)
            if (stream == null) {
                Toast.makeText(
                    context,
                    "Could not open file for writing",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            // We write before changing IS_PENDING
            stream.use {
                it.write(logText.toByteArray())
            }

            // Mark file as available
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)

            // Return null because we already used it
            null
        } else {
            val downloadsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) downloadsDir.mkdirs()
            val file = File(downloadsDir, fileName)
            FileOutputStream(file)
        }

        // Only for API < 29
        outputStream?.use {
            it.write(logText.toByteArray())
        }

        Toast.makeText(context, "Log saved in Downloads as $fileName", Toast.LENGTH_LONG)
            .show()

    } catch (ex: Exception) {
        ex.printStackTrace()
        Toast.makeText(context, "Error saving log: ${ex.message}", Toast.LENGTH_LONG).show()
    }
}

