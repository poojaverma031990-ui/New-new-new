package com.example

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

// ==========================================
// GLASS THEME MODEL & DATA
// ==========================================
data class GlassTheme(
    val id: Int,
    val name: String,
    val styleType: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color,
    val backgroundGradient: Brush
)

val glassThemes = listOf(
    GlassTheme(
        id = 1, name = "Liquid Mercury", styleType = "Metallic",
        primaryColor = Color(0xFFE0E0E0), secondaryColor = Color(0xFF9E9E9E), accentColor = Color(0xFFFAFAFA),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF455A64), Color(0xFF1C313A)))
    ),
    GlassTheme(
        id = 2, name = "Neon Plasma", styleType = "Cyber",
        primaryColor = Color(0xFFFF00FF), secondaryColor = Color(0xFF00FFFF), accentColor = Color(0xFFFFEB3B),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF311B92), Color(0xFF000000)))
    ),
    GlassTheme(
        id = 3, name = "Electric Aurora", styleType = "Nature",
        primaryColor = Color(0xFF00E676), secondaryColor = Color(0xFF1DE9B6), accentColor = Color(0xFFB2FF59),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF004D40), Color(0xFF000000)))
    ),
    GlassTheme(
        id = 4, name = "Obsidian Gold", styleType = "Lux",
        primaryColor = Color(0xFFFFD700), secondaryColor = Color(0xFFB8860B), accentColor = Color(0xFFFFA500),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF212121), Color(0xFF000000)))
    ),
    GlassTheme(
        id = 5, name = "Biolume Slime", styleType = "Organic",
        primaryColor = Color(0xFF76FF03), secondaryColor = Color(0xFF00E676), accentColor = Color(0xFF64FFDA),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF000000)))
    ),
    GlassTheme(
        id = 6, name = "Deep Amethyst", styleType = "Crystal",
        primaryColor = Color(0xFFD500F9), secondaryColor = Color(0xFF651FFF), accentColor = Color(0xFFEA80FC),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF4A148C), Color(0xFF1A237E)))
    ),
    GlassTheme(
        id = 7, name = "Solar Flares", styleType = "Fire",
        primaryColor = Color(0xFFFF3D00), secondaryColor = Color(0xFFFF9100), accentColor = Color(0xFFFFC400),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFFBF360C), Color(0xFF3E2723)))
    ),
    GlassTheme(
        id = 8, name = "Glacial Flow", styleType = "Ice",
        primaryColor = Color(0xFF00B0FF), secondaryColor = Color(0xFF00E5FF), accentColor = Color(0xFF84FFFF),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF01579B), Color(0xFF006064)))
    ),
    GlassTheme(
        id = 9, name = "Cyberpunk Grid", styleType = "Retro",
        primaryColor = Color(0xFFFF007F), secondaryColor = Color(0xFF00F0FF), accentColor = Color(0xFFFEE715),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF1A1A2E), Color(0xFF16213E)))
    ),
    GlassTheme(
        id = 10, name = "Toxic Waste", styleType = "Radioactive",
        primaryColor = Color(0xFFAEEA00), secondaryColor = Color(0xFFC6FF00), accentColor = Color(0xFF1DE9B6),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF33691E), Color(0xFF212121)))
    ),
    GlassTheme(
        id = 11, name = "Magma Chamber", styleType = "Geological",
        primaryColor = Color(0xFFDD2C00), secondaryColor = Color(0xFFFF3D00), accentColor = Color(0xFFFFAB00),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF3E2723), Color(0xFF212121)))
    ),
    GlassTheme(
        id = 12, name = "Chrono Oasis", styleType = "Temporal",
        primaryColor = Color(0xFF00BFA5), secondaryColor = Color(0xFF64FFDA), accentColor = Color(0xFFA7FFEB),
        backgroundGradient = Brush.linearGradient(listOf(Color(0xFF004D40), Color(0xFF263238)))
    )
)

// ==========================================
// HIGH PERFORMANCE OPTIMIZED PARTICLE ENGINE
// ==========================================
class GlassParticle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val maxLife: Float,
    var life: Float,
    val size: Float,
    val color: Color
)

class GlassShockwave(
    val x: Float,
    val y: Float,
    val color: Color,
    var radius: Float,
    val maxRadius: Float,
    var life: Float, // 1.0 down to 0.0
    val speed: Float
)

class GravityVortex(
    val x: Float,
    val y: Float,
    val strength: Float,
    val color: Color,
    var angle: Float = 0f,
    var life: Float = 1.0f
)

data class PaintStroke(
    val points: List<Offset>,
    val color: Color,
    val width: Float
)

// ==========================================
// HIGH PERFORMANCE OFF-GRID FLUID SOLVER
// ==========================================
class FluidSimulation(val cols: Int, val rows: Int) {
    val h = FloatArray(cols * rows)       // Heights (waves)
    val v = FloatArray(cols * rows)       // Height velocity
    val vx = FloatArray(cols * rows)      // Flow velocity X
    val vy = FloatArray(cols * rows)      // Flow velocity Y

    fun update(
        dt: Float, 
        gravityX: Float, 
        gravityY: Float, 
        persistence: Boolean,
        customDamping: Float = 0f,
        customFlowDamping: Float = 0f,
        gravityMult: Float = 1.0f
    ) {
        val damping = if (customDamping > 0f) customDamping else (if (persistence) 0.985f else 0.94f)
        val flowDamping = if (customFlowDamping > 0f) customFlowDamping else 0.96f

        // 2D Wave Equation finite differences solver
        for (y in 1 until rows - 1) {
            val idx = y * cols
            for (x in 1 until cols - 1) {
                val i = idx + x
                val neighborSum = h[i - 1] + h[i + 1] + h[i - cols] + h[i + cols]
                val avg = neighborSum * 0.25f
                
                // Acceleration pulls height to average height of its neighbors
                val accel = (avg - h[i]) * 0.25f
                v[i] = (v[i] + accel) * damping
            }
        }

        // Apply grid displacement & dynamic flow speed equations
        for (i in 0 until cols * rows) {
            h[i] = (h[i] + v[i] * 60f * dt).coerceIn(-130f, 130f)
            
            // Apply subtle damping and tilt force vectors to the fluid flow
            vx[i] = (vx[i] + gravityX * 0.15f * gravityMult) * flowDamping
            vy[i] = (vy[i] + gravityY * 0.15f * gravityMult) * flowDamping
        }

        // Dissolve/Advect velocities lightly to avoid aliasing and keep smooth vortices
        val cloneVx = vx.clone()
        val cloneVy = vy.clone()
        for (y in 1 until rows - 1) {
            val idx = y * cols
            for (x in 1 until cols - 1) {
                val i = idx + x
                vx[i] = cloneVx[i] * 0.7f + (cloneVx[i - 1] + cloneVx[i + 1] + cloneVx[i - cols] + cloneVx[i + cols]) * 0.075f
                vy[i] = cloneVy[i] * 0.7f + (cloneVy[i - 1] + cloneVy[i + 1] + cloneVy[i - cols] + cloneVy[i + cols]) * 0.075f
            }
        }
    }

    fun addForce(cellX: Int, cellY: Int, fx: Float, fy: Float, radius: Int) {
        val rSq = radius * radius
        for (dy in -radius..radius) {
            val ny = cellY + dy
            if (ny < 0 || ny >= rows) continue
            val yOffset = ny * cols
            for (dx in -radius..radius) {
                val nx = cellX + dx
                if (nx < 0 || nx >= cols) continue
                val distSq = dx * dx + dy * dy
                if (distSq <= rSq) {
                    val factor = 1.0f - (distSq.toFloat() / rSq)
                    val i = yOffset + nx
                    // Displace height (create ripple sound/impact wave)
                    v[i] += fy * factor * 0.38f
                    // Inject flow velocity vectors matching hand drags
                    vx[i] += fx * factor * 1.8f
                    vy[i] += fy * factor * 1.8f
                }
            }
        }
    }
}

// ==========================================
// CO-OPERATIVE REAL-TIME SYNTHESIZER MODULE
// ==========================================
class GlassSynth {
    private var audioTrack: AudioTrack? = null
    var isPlaying = false
    private var synthThread: Thread? = null
    
    // Polyphonic frequencies corresponding to direct fingers on the playground surface
    val activeFrequencies = mutableListOf<Float>()
    var volume = 0.4f

    fun start(context: Context? = null) {
        if (isPlaying) return
        isPlaying = true
        val sampleRate = 22050
        val minBufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        
        try {
            var builder = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(minBufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)

            audioTrack = builder.build()
            audioTrack?.play()
        } catch (e: Exception) {
            try {
                @Suppress("DEPRECATION")
                audioTrack = AudioTrack(
                    android.media.AudioManager.STREAM_MUSIC,
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize,
                    AudioTrack.MODE_STREAM
                )
                audioTrack?.play()
            } catch (e2: Exception) {
                return
            }
        }

        synthThread = Thread {
            val sampleRateDouble = sampleRate.toDouble()
            var phase = 0.0
            val buffer = ShortArray(1024)
            var currentVol = 0.0f
            
            while (isPlaying) {
                val freqs = synchronized(activeFrequencies) { activeFrequencies.toList() }
                val targetVol = if (freqs.isEmpty()) 0.0f else volume * 0.15f
                
                // Exponential volume dampening to avoid dynamic pops & clicks!
                currentVol = currentVol * 0.95f + targetVol * 0.05f

                for (i in buffer.indices) {
                    var sampleSum = 0.0
                    if (freqs.isNotEmpty() && currentVol > 0.001f) {
                        for (freq in freqs) {
                            sampleSum += sin(phase * 2.0 * Math.PI * freq / sampleRateDouble)
                        }
                        sampleSum /= freqs.size
                    }
                    
                    val modulated = sampleSum * 32767.0 * currentVol
                    buffer[i] = modulated.toInt().coerceIn(-32768, 32767).toShort()
                    
                    phase += 1.0
                    if (phase >= sampleRateDouble) {
                        phase -= sampleRateDouble
                    }
                }
                audioTrack?.write(buffer, 0, buffer.size)
            }
        }.apply { start() }
    }

    fun triggerAtmosphereTone(freq: Float) {
        val toneVol = volume
        if (toneVol <= 0.01f) return
        Thread {
            val sampleRate = 22050
            val samples = ShortArray(4000)
            for (i in samples.indices) {
                val progress = i.toFloat() / samples.size
                val envelope = (1.0f - progress) * sin(progress * Math.PI.toFloat()) // chime sweep
                val s = sin(i * 2.0 * Math.PI * freq / sampleRate) * envelope * 18000.0 * toneVol
                samples[i] = s.toInt().toShort()
            }
            try {
                val tempTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(samples.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()
                tempTrack.write(samples, 0, samples.size)
                tempTrack.play()
                // Auto-cleanup after time
                Thread.sleep(400)
                tempTrack.release()
            } catch (e: Exception) {
                // Ignore fallback silences
            }
        }.start()
    }

    fun stop() {
        isPlaying = false
        try {
            synthThread?.join(300)
        } catch (e: Exception) {}
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {}
        audioTrack = null
        synthThread = null
    }
}

// ==========================================
// MAIN ACTIVITY ENTRY POINT
// ==========================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var reloadKey by remember { mutableStateOf(0) }
            key(reloadKey) {
                LiquidGlassApp(
                    onHardReset = {
                        reloadKey++
                    }
                )
            }
        }
    }
}

fun Offset.distanceTo(other: Offset): Float {
    return (this - other).getDistance()
}

fun Offset.normalized(): Offset {
    val d = getDistance()
    return if (d > 0f) this / d else Offset.Zero
}

// Touch point metadata containing position and pressure value
data class TouchInfo(
    val position: Offset,
    val pressure: Float
)

// Reflection-based utility to safely obtain MotionEvent from PointerEvent (bypassing internal access rules)
fun androidx.compose.ui.input.pointer.PointerEvent.getNativeMotionEvent(): android.view.MotionEvent? {
    return try {
        val getter = this::class.java.getMethod("getMotionEvent")
        getter.isAccessible = true
        getter.invoke(this) as? android.view.MotionEvent
    } catch (e: Exception) {
        try {
            val field = this::class.java.getDeclaredField("motionEvent")
            field.isAccessible = true
            field.get(this) as? android.view.MotionEvent
        } catch (e2: Exception) {
            try {
                val pGetter = this::class.java.getMethod("getPlatformEvent")
                pGetter.isAccessible = true
                pGetter.invoke(this) as? android.view.MotionEvent
            } catch (e3: Exception) {
                null
            }
        }
    }
}

fun getCalibratedPressure(rawPressure: Float): Float {
    return if (rawPressure < 0.25f) {
        0.08f // tiny tactile touch
    } else {
        // High range non-linear mapping (Thumb -> massive majestic impact, Pinky -> precise spark)
        Math.pow(rawPressure.toDouble(), 1.6).toFloat().coerceIn(0.12f, 4.2f)
    }
}

fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
    val hsv = floatArrayOf(hue, saturation, value)
    return Color(android.graphics.Color.HSVToColor(hsv))
}

// ==========================================
// CORE APP COMPOSABLE MAIN SURFACE
// ==========================================
@Composable
fun LiquidGlassApp(onHardReset: () -> Unit = {}) {
    // Canvas Paint Engine States
    var isPaintingMode by remember { mutableStateOf(false) }
    var selectedBrushColor by remember { mutableStateOf(Color(0xFF00FFD1)) }
    var brushSizeSetting by remember { mutableFloatStateOf(8.0f) }
    var isPhysicsPaused by remember { mutableStateOf(false) }
    
    // Custom Color picker HSV state
    var pickerHue by remember { mutableFloatStateOf(180f) }
    var pickerSat by remember { mutableFloatStateOf(1.0f) }
    var pickerValue by remember { mutableFloatStateOf(1.0f) }

    // Persistent Strokes
    val paintStrokes = remember { mutableStateListOf<PaintStroke>() }
    // Temporary strokes mapped to pointer ID
    val activePaintPaths = remember { mutableStateMapOf<Int, MutableList<Offset>>() }
    
    var isColorPickerOpen by remember { mutableStateOf(false) }

    var activeTheme by remember { mutableStateOf(glassThemes[2]) } // Standard starting theme Electric Aurora
    var isMenuOpen by remember { mutableStateOf(false) }
    var isSettingsOpen by remember { mutableStateOf(false) }

    // Multi-Touch tracking maps tracking TouchInfo instead of simple Offset
    val activeTouches = remember { mutableStateMapOf<Int, TouchInfo>() }
    var isPressed by remember { mutableStateOf(false) }

    // Settings Parameters
    var synthesizerVolume by remember { mutableFloatStateOf(0.40f) }
    var isPersistenceEnabled by remember { mutableStateOf(false) }
    var userScaleFactor by remember { mutableFloatStateOf(1.0f) }
    var maxParticlesSetting by remember { mutableFloatStateOf(150f) }
    var isTiltPhysicsEnabled by remember { mutableStateOf(true) }

    // Advanced Live Physics & Tuning parameters
    var waveDampingSetting by remember { mutableFloatStateOf(0.94f) }
    var fluidFlowPersistenceSetting by remember { mutableFloatStateOf(0.96f) }
    var dragVelocityMultiplier by remember { mutableFloatStateOf(1.5f) }
    var particleSpeedSetting by remember { mutableFloatStateOf(1.0f) }
    var particleLifeSetting by remember { mutableFloatStateOf(1.0f) }
    var gravityIntensityScale by remember { mutableFloatStateOf(1.0f) }
    var particlesDensitySetting by remember { mutableFloatStateOf(0.016f) } // emission cooldown timer limit
    var isLiveHudVisible by remember { mutableStateOf(true) }

    // ==========================================
    // ULTRA PRO MAX PHASE 3 DYNAMIC PHYSICS PRESETS
    // ==========================================
    class PhysicsPreset(
        val id: Int,
        val name: String,
        val emoji: String,
        val description: String,
        val damping: Float,
        val flowDamping: Float,
        val dragVelocity: Float,
        val particleSpeed: Float,
        val particleLife: Float,
        val gravityScale: Float,
        val themeId: Int
    )

    val physicsPresets = remember {
        listOf(
            PhysicsPreset(0, "Fluid Aurora", "⚡", "Harmonic quantum fluid wave simulation", 0.94f, 0.96f, 1.5f, 1.0f, 1.0f, 1.0f, 3),
            PhysicsPreset(1, "Melted Ruby", "🌋", "Ultra-viscous, warm magma lava wave chamber", 0.982f, 0.993f, 0.7f, 0.5f, 1.6f, 0.5f, 11),
            PhysicsPreset(2, "Bouncy Helium", "🧪", "Frictionless superfluid helium micro-ripples", 0.92f, 0.94f, 2.4f, 2.0f, 0.8f, 1.8f, 8),
            PhysicsPreset(3, "Cosmic Vortex", "🌌", "Dense event horizon with gravitational vortex orbits", 0.965f, 0.98f, 1.8f, 1.3f, 2.2f, 0.4f, 6),
            PhysicsPreset(4, "Toxic Waste", "☢️", "Radioactive high-velocity green sludge plasma", 0.88f, 0.93f, 2.8f, 2.2f, 0.6f, 2.2f, 10)
        )
    }

    var activePresetId by remember { mutableIntStateOf(0) }
    var isVortexMode by remember { mutableStateOf(false) }
    val gravityVortices = remember { mutableStateListOf<GravityVortex>() }
    var particleTimer by remember { mutableFloatStateOf(0f) }

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val density = LocalDensity.current

    // Initialize Synthsizer Engine
    val synth = remember { GlassSynth() }
    
    // Accelerometer Motion sensor setup
    var tiltX by remember { mutableStateOf(0f) }
    var tiltY by remember { mutableStateOf(0f) }

    DisposableEffect(isTiltPhysicsEnabled) {
        if (!isTiltPhysicsEnabled) {
            tiltX = 0f
            tiltY = 0f
            onDispose {}
        } else {
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        // Smooth tilt values
                        tiltX = tiltX * 0.85f - event.values[0] * 0.15f
                        tiltY = tiltY * 0.85f + event.values[1] * 0.15f
                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    // Direct audio synthesis frequency mapping and active loop life
    LaunchedEffect(synthesizerVolume) {
        synth.volume = synthesizerVolume
    }

    LaunchedEffect(Unit) {
        synth.start(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            synth.stop()
        }
    }

    // 2D Fluid Mesh Simulation State (Aesthetic 24x48 finite grid)
    val fluidCols = 24
    val fluidRows = 48
    val fluid = remember { FluidSimulation(fluidCols, fluidRows) }
    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }

    // Active Particle Memory Pool for 60FPS fluid simulation
    val particles = remember { mutableStateListOf<GlassParticle>() }
    val shockwaves = remember { mutableStateListOf<GlassShockwave>() }

    // Global Animating Frame Ticker
    val timeMillis = remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (isActive) {
            withFrameMillis { frameTime ->
                val prevTime = timeMillis.longValue
                timeMillis.longValue = frameTime
                val dt = if (prevTime == 0L) 0.016f else (frameTime - prevTime) / 1000f

                // 1. Update Fluid Physics Engine Simulation Grid with Advanced Parameters
                if (!isPhysicsPaused) {
                    fluid.update(
                        dt = dt, 
                        gravityX = tiltX, 
                        gravityY = tiltY, 
                        persistence = isPersistenceEnabled,
                        customDamping = waveDampingSetting,
                        customFlowDamping = fluidFlowPersistenceSetting,
                        gravityMult = gravityIntensityScale
                    )

                    // Update gravity vortices spin and life
                    val vIter = gravityVortices.iterator()
                    while (vIter.hasNext()) {
                        val v = vIter.next()
                        v.angle += dt * 3.8f // spin velocity
                        v.life -= dt * 0.08f // slowly decays over ~12s
                        if (v.life <= 0f) {
                            vIter.remove()
                        }
                    }

                    // Apply Gravitational Vortex Attraction to 2D Fluid mesh grid
                    if (gravityVortices.isNotEmpty()) {
                        val w = canvasSize.width
                        val h = canvasSize.height
                        if (w > 0f && h > 0f) {
                            val colSpacing = w / (fluidCols - 1)
                            val rowSpacing = h / (fluidRows - 1)
                            gravityVortices.forEach { vortex ->
                                for (y in 0 until fluidRows) {
                                    for (x in 0 until fluidCols) {
                                        val idx = y * fluidCols + x
                                        val nodeX = x * colSpacing
                                        val nodeY = y * rowSpacing
                                        val dx = vortex.x - nodeX
                                        val dy = vortex.y - nodeY
                                        val distSq = dx * dx + dy * dy
                                        val dist = kotlin.math.sqrt(distSq)
                                        if (dist > 1f && dist < 650f) {
                                            val pull = (vortex.strength * 4.2f / (distSq / 130f + 25f)).coerceIn(0f, 6.5f) * vortex.life
                                            val tx = -dy / dist
                                            val ty = dx / dist
                                            // Induce flowing orbits
                                            fluid.vx[idx] += (dx / dist * pull * 0.07f + tx * pull * 0.28f) * dt * 60f
                                            fluid.vy[idx] += (dy / dist * pull * 0.07f + ty * pull * 0.28f) * dt * 60f
                                            // Dip height inside the vortex core
                                            fluid.h[idx] = (fluid.h[idx] - pull * 1.6f).coerceIn(-130f, 130f)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Synthesize frequencies based on Touch/Pointers
                synchronized(synth.activeFrequencies) {
                    synth.activeFrequencies.clear()
                    activeTouches.values.forEach { info ->
                        val pt = info.position
                        val baseFreq = 180f + (pt.y / 2.5f).coerceIn(40f, 600f) + (pt.x / 4f).coerceIn(10f, 150f)
                        synth.activeFrequencies.add(baseFreq)
                    }
                    // Add extra dynamic frequencies for rotating vortex centers to create eerie high-tech harmonic drones!
                    gravityVortices.forEach { vortex ->
                        val baseFreq = 120f + (vortex.y / 4f).coerceIn(30f, 180f) + (vortex.angle * 8f) % 150f
                        synth.activeFrequencies.add(baseFreq)
                    }
                }

                // 3. Update active particles list (influenced by 2D fluid flow velocities and gravity vortices)
                if (!isPhysicsPaused) {
                    val w = canvasSize.width
                    val h = canvasSize.height
                    val iter = particles.iterator()
                    while (iter.hasNext()) {
                        val p = iter.next()
                        p.life -= dt
                        if (p.life <= 0f) {
                            iter.remove()
                        } else {
                            if (w > 0f && h > 0f) {
                                val cx = (p.x / w * (fluidCols - 1)).toInt().coerceIn(0, fluidCols - 1)
                                val cy = (p.y / h * (fluidRows - 1)).toInt().coerceIn(0, fluidRows - 1)
                                val fIdx = cy * fluidCols + cx
                                
                                val flowInfluence = 0.28f
                                val fvx = fluid.vx[fIdx]
                                val fvy = fluid.vy[fIdx]
                                
                                p.vx = p.vx * (1f - flowInfluence) + fvx * flowInfluence + tiltX * 1.5f * dt * gravityIntensityScale
                                p.vy = p.vy * (1f - flowInfluence) + fvy * flowInfluence + tiltY * 1.5f * dt * gravityIntensityScale
                            } else {
                                p.vx += tiltX * 1.5f * dt * gravityIntensityScale
                                p.vy += tiltY * 1.5f * dt * gravityIntensityScale
                                p.vx *= 0.98f
                                p.vy *= 0.98f
                            }

                            // Dynamic Gravitational attraction and orbit swirls on particles!
                            gravityVortices.forEach { vortex ->
                                val dx = vortex.x - p.x
                                val dy = vortex.y - p.y
                                val distSq = dx * dx + dy * dy
                                val dist = kotlin.math.sqrt(distSq)
                                if (dist > 1f && dist < 1100f) {
                                    val force = (vortex.strength * 220f / (distSq / 180f + 35f)).coerceIn(0f, 18f) * vortex.life
                                    val tx = -dy / dist
                                    val ty = dx / dist
                                    p.vx += (dx / dist * force * 0.14f + tx * force * 0.45f) * dt * 60f
                                    p.vy += (dy / dist * force * 0.45f) * dt * 60f
                                }
                            }

                            p.x += p.vx
                            p.y += p.vy
                        }
                    }
                }

                // Update diagnostic & sensory shockwaves
                if (!isPhysicsPaused) {
                    val swIter = shockwaves.iterator()
                    while (swIter.hasNext()) {
                        val sw = swIter.next()
                        sw.life -= dt * 1.8f
                        sw.radius += sw.speed * dt
                        if (sw.life <= 0f) {
                            swIter.remove()
                        }
                    }
                }

                // 4. Emit customized glowing particles with Calibrated Pressure sizes (Throttled based on custom steps)
                if (!isPhysicsPaused) {
                    particleTimer += dt
                    if (particleTimer >= particlesDensitySetting) {
                        particleTimer = 0f
                        if (activeTouches.isNotEmpty() && particles.size < maxParticlesSetting.toInt()) {
                            activeTouches.values.forEach { info ->
                                val pt = info.position
                                val angle = (0..359).random() * (Math.PI / 180f).toFloat()
                                val speed = kotlin.random.Random.nextDouble(1.8, 6.2).toFloat() * particleSpeedSetting
                                val calibratedPressure = getCalibratedPressure(info.pressure)
                                particles.add(
                                    GlassParticle(
                                        x = pt.x,
                                        y = pt.y,
                                        vx = cos(angle) * speed,
                                        vy = sin(angle) * speed,
                                        maxLife = particleLifeSetting,
                                        life = kotlin.random.Random.nextDouble((particleLifeSetting * 0.4f).toDouble(), particleLifeSetting.toDouble()).toFloat(),
                                        size = kotlin.random.Random.nextDouble(8.0, 20.0).toFloat() * (0.3f + 1.2f * calibratedPressure),
                                        color = if (kotlin.random.Random.nextBoolean()) activeTheme.primaryColor else activeTheme.accentColor
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Playground Canvas Surface
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Move) {
                            event.changes.forEach { change ->
                                val wFloat = size.width.toFloat()
                                val hFloat = size.height.toFloat()
                                if (wFloat > 0f && hFloat > 0f) {
                                    val prevPt = change.previousPosition
                                    val currPt = change.position
                                    val drag = currPt - prevPt
                                    if (drag.getDistance() > 1f) {
                                        val cellX = (currPt.x / wFloat * (fluidCols - 1)).toInt().coerceIn(0, fluidCols - 1)
                                        val cellY = (currPt.y / hFloat * (fluidRows - 1)).toInt().coerceIn(0, fluidRows - 1)
                                        fluid.addForce(
                                            cellX = cellX,
                                            cellY = cellY,
                                            fx = drag.x * dragVelocityMultiplier * userScaleFactor * 0.4f,
                                            fy = drag.y * dragVelocityMultiplier * userScaleFactor * 0.4f,
                                            radius = 2
                                        )
                                        if (kotlin.random.Random.nextFloat() < 0.2f) {
                                            particles.add(
                                                GlassParticle(
                                                    x = currPt.x,
                                                    y = currPt.y,
                                                    vx = drag.x * 0.04f + kotlin.random.Random.nextDouble(-0.8, 0.8).toFloat(),
                                                    vy = drag.y * 0.04f + kotlin.random.Random.nextDouble(-0.8, 0.8).toFloat(),
                                                    maxLife = 0.8f,
                                                    life = kotlin.random.Random.nextDouble(0.3, 0.8).toFloat(),
                                                    size = kotlin.random.Random.nextDouble(4.0, 10.0).toFloat(),
                                                    color = if (isPaintingMode) selectedBrushColor.copy(alpha = 0.5f) else activeTheme.primaryColor.copy(alpha = 0.5f)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    isPressed = true
                    if (isMenuOpen) isMenuOpen = false

                    if (isVortexMode) {
                        gravityVortices.add(
                            GravityVortex(
                                x = down.position.x,
                                y = down.position.y,
                                strength = 5.0f * userScaleFactor,
                                color = if (isPaintingMode) selectedBrushColor else activeTheme.accentColor,
                                life = 1.0f
                            )
                        )
                        if (gravityVortices.size > 4) {
                            gravityVortices.removeAt(0)
                        }
                        // Trigger deep extra synth tone
                        synth.triggerAtmosphereTone(110f + kotlin.random.Random.nextInt(50))
                    }

                    // If painting, register start of path
                    val firstPointerId = down.id.value.toInt()
                    if (isPaintingMode) {
                        activePaintPaths[firstPointerId] = mutableListOf(down.position)
                    }

                    // Simple internal tone trigger
                    synth.triggerAtmosphereTone(440f + (down.position.y / 4f).coerceIn(10f, 400f))

                    // Calibrated pressure mapping
                    val initialPt = down.position
                    val initialPressure = down.pressure.coerceIn(0.15f, 3.0f)
                    val initialCalibrated = getCalibratedPressure(initialPressure)

                    // Inject instant localized displacement force into fluid solver!
                    val wFloat = size.width.toFloat()
                    val hFloat = size.height.toFloat()
                    if (wFloat > 0f && hFloat > 0f) {
                        val cellX = (initialPt.x / wFloat * (fluidCols - 1)).toInt().coerceIn(0, fluidCols - 1)
                        val cellY = (initialPt.y / hFloat * (fluidRows - 1)).toInt().coerceIn(0, fluidRows - 1)
                        fluid.addForce(cellX, cellY, 0f, 32f * initialCalibrated, if (initialCalibrated > 1.5f) 3 else 2)
                    }

                    // Spawn beautiful YouTube Shorts style interaction shockwave on down!
                    shockwaves.add(
                        GlassShockwave(
                            x = initialPt.x,
                            y = initialPt.y,
                            color = if (isPaintingMode) selectedBrushColor else activeTheme.accentColor,
                            radius = 2.dp.toPx(),
                            maxRadius = 140.dp.toPx() * userScaleFactor * (0.5f + 1.2f * initialCalibrated),
                            life = 1.0f,
                            speed = 460.dp.toPx()
                        )
                    )

                    // Trigger structural particle explosion at the touch point!
                    val pCount = 12
                    for (i in 0 until pCount) {
                        val angle = (i * (360f / pCount)) * (Math.PI / 180f).toFloat()
                        val speed = kotlin.random.Random.nextDouble(2.2, 5.8).toFloat()
                        particles.add(
                            GlassParticle(
                                x = initialPt.x,
                                y = initialPt.y,
                                vx = cos(angle) * speed,
                                vy = sin(angle) * speed,
                                maxLife = 1.2f,
                                life = kotlin.random.Random.nextDouble(0.5, 1.2).toFloat(),
                                size = kotlin.random.Random.nextDouble(12.0, 26.0).toFloat() * (0.5f + 0.8f * initialCalibrated),
                                color = if (isPaintingMode) selectedBrushColor else (if (kotlin.random.Random.nextBoolean()) activeTheme.primaryColor else activeTheme.accentColor)
                            )
                        )
                    }

                    do {
                        val event = awaitPointerEvent()
                        val motionEvent = event.getNativeMotionEvent()
                        // Multi touch tracking indices with MotionEvent pressure mapped
                        activeTouches.clear()
                        event.changes.forEach { change ->
                            if (change.pressed) {
                                val pointerId = change.id.value.toInt()
                                var pressure = 1.0f
                                if (motionEvent != null) {
                                    val pointerIndex = motionEvent.findPointerIndex(pointerId)
                                    if (pointerIndex >= 0 && pointerIndex < motionEvent.pointerCount) {
                                        pressure = motionEvent.getPressure(pointerIndex)
                                    }
                                }
                                val calibrated = getCalibratedPressure(pressure)
                                activeTouches[pointerId] = TouchInfo(change.position, pressure)

                                // If painting, append to path
                                if (isPaintingMode) {
                                    if (activePaintPaths.containsKey(pointerId)) {
                                        activePaintPaths[pointerId]?.add(change.position)
                                    } else {
                                        activePaintPaths[pointerId] = mutableListOf(change.position)
                                    }
                                }

                                // Inject dynamic dragging vector forces to make swirling vortices!
                                if (wFloat > 0f && hFloat > 0f) {
                                    val prevPt = change.previousPosition
                                    val currPt = change.position
                                    val drag = currPt - prevPt
                                    if (drag.getDistance() > 0.1f) {
                                        val cellX = (currPt.x / wFloat * (fluidCols - 1)).toInt().coerceIn(0, fluidCols - 1)
                                        val cellY = (currPt.y / hFloat * (fluidRows - 1)).toInt().coerceIn(0, fluidRows - 1)
                                        fluid.addForce(
                                            cellX = cellX,
                                            cellY = cellY,
                                            fx = drag.x * dragVelocityMultiplier * userScaleFactor,
                                            fy = drag.y * dragVelocityMultiplier * userScaleFactor,
                                            radius = if (calibrated > 1.5f) 3 else 2
                                        )
                                    }
                                }

                                // Check if this pointer has just been pressed down (multi-finger addition)
                                if (!change.previousPressed) {
                                    val pt = change.position
                                    if (isPaintingMode) {
                                        activePaintPaths[pointerId] = mutableListOf(pt)
                                    }
                                    shockwaves.add(
                                        GlassShockwave(
                                            x = pt.x,
                                            y = pt.y,
                                            color = if (isPaintingMode) selectedBrushColor else activeTheme.primaryColor,
                                            radius = 2.dp.toPx(),
                                            maxRadius = 110.dp.toPx() * userScaleFactor * (0.5f + 1.2f * calibrated),
                                            life = 1.0f,
                                            speed = 400.dp.toPx()
                                        )
                                    )
                                    // Trigger dynamic accompanying tone
                                    synth.triggerAtmosphereTone(480f + kotlin.random.Random.nextInt(120))
                                }
                            }
                        }

                        // Compute manual Pinch to Zoom factor dynamically if multi-fingers active, safely avoiding jumps
                        val activeMovingFingers = event.changes.filter { it.pressed && it.previousPressed }
                        if (activeMovingFingers.size >= 2) {
                            val p1 = activeMovingFingers[0].position
                            val p2 = activeMovingFingers[1].position
                            val prev1 = activeMovingFingers[0].previousPosition
                            val prev2 = activeMovingFingers[1].previousPosition
                            val currentDist = (p1 - p2).getDistance()
                            val originalDist = (prev1 - prev2).getDistance()
                            if (originalDist > 10.dp.toPx()) { // stable threshold
                                val scaleRatio = currentDist / originalDist
                                if (scaleRatio in 0.82f..1.18f) { // prevent extreme jumps
                                    userScaleFactor = (userScaleFactor * scaleRatio).coerceIn(0.5f, 3.0f)
                                }
                            }
                        }

                        event.changes.forEach { it.consume() }
                    } while (event.changes.any { it.pressed })

                    // Commit finished paths when pointers lift
                    if (isPaintingMode) {
                        activePaintPaths.forEach { (_, points) ->
                            if (points.isNotEmpty()) {
                                paintStrokes.add(
                                    PaintStroke(
                                        points = points.toList(),
                                        color = selectedBrushColor,
                                        width = brushSizeSetting
                                    )
                                )
                            }
                        }
                        activePaintPaths.clear()
                    }

                    isPressed = false
                    activeTouches.clear()
                    synchronized(synth.activeFrequencies) {
                        synth.activeFrequencies.clear()
                    }
                }
            }
        ) {
            val w = size.width
            val h = size.height

            // 1. Sleek Midnight Glass base (Vibrant Palette style specs)
            drawRect(color = Color(0xFF08080A), size = size)

            // Dynamic Ambient Orbs
            val t = timeMillis.longValue / 1000f
            // Tilt influences background layers
            val bgTiltX = tiltX * 8.dp.toPx()
            val bgTiltY = tiltY * 8.dp.toPx()

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(activeTheme.primaryColor.copy(alpha = 0.28f), Color.Transparent),
                    center = Offset(-60.dp.toPx() + bgTiltX, -60.dp.toPx() + bgTiltY),
                    radius = 350.dp.toPx()
                ),
                center = Offset(-60.dp.toPx() + bgTiltX, -60.dp.toPx() + bgTiltY),
                radius = 350.dp.toPx()
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(activeTheme.secondaryColor.copy(alpha = 0.22f), Color.Transparent),
                    center = Offset(w + 100.dp.toPx() + bgTiltX, h / 2f + bgTiltY),
                    radius = 420.dp.toPx()
                ),
                center = Offset(w + 100.dp.toPx() + bgTiltX, h / 2f + bgTiltY),
                radius = 420.dp.toPx()
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(activeTheme.accentColor.copy(alpha = 0.16f), Color.Transparent),
                    center = Offset(w / 4f + bgTiltX, h + 80.dp.toPx() + bgTiltY),
                    radius = 300.dp.toPx()
                ),
                center = Offset(w / 4f + bgTiltX, h + 80.dp.toPx() + bgTiltY),
                radius = 300.dp.toPx()
            )

            // Specular background refraction highlights
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.12f), Color.Transparent),
                    center = Offset(w * 0.60f, h * 0.45f),
                    radius = w * 0.45f
                ),
                center = Offset(w * 0.60f, h * 0.45f),
                radius = w * 0.45f
            )

            // 2. Draw Multi-Touch Liquid Warp Grid (Fluid Simulation Solver output)
            if (canvasSize.width != w || canvasSize.height != h) {
                canvasSize = androidx.compose.ui.geometry.Size(w, h)
            }

            val colSpacing = w / (fluidCols - 1)
            val rowSpacing = h / (fluidRows - 1)

            for (y in 0 until fluidRows) {
                for (x in 0 until fluidCols) {
                    val i = y * fluidCols + x
                    val basePos = Offset(x * colSpacing, y * rowSpacing)
                    
                    // Wave height creates displacement, velocity creates flow drift
                    val dy = fluid.h[i] * 0.40f
                    val vxNorm = fluid.vx[i] * 0.32f
                    val vyNorm = fluid.vy[i] * 0.32f
                    
                    val drawPos = basePos + Offset(vxNorm, vyNorm + dy)
                    
                    // Highlight active nodes dynamically
                    val absoluteHeight = kotlin.math.abs(fluid.h[i])
                    val rippleHighlight = (absoluteHeight / 130f).coerceIn(0f, 1f)
                    
                    val dotRadius = 1.6.dp.toPx() + rippleHighlight * 2.8.dp.toPx()
                    val dotColor = if (rippleHighlight > 0.12f) {
                        androidx.compose.ui.graphics.lerp(activeTheme.primaryColor.copy(alpha = 0.38f), activeTheme.accentColor, rippleHighlight)
                    } else {
                        // Blend standard mesh dot color
                        activeTheme.primaryColor.copy(alpha = 0.38f + rippleHighlight * 0.42f)
                    }

                    drawCircle(
                        color = dotColor,
                        center = drawPos,
                        radius = dotRadius
                    )
                }
            }

            // 2.5 Draw Neon Paint Canvas Strokes layers
            paintStrokes.forEach { stroke ->
                if (stroke.points.size > 1) {
                    for (index in 0 until stroke.points.size - 1) {
                        val p1 = stroke.points[index]
                        val p2 = stroke.points[index + 1]
                        
                        // Wide Neon Shadow Glow Outer Layer
                        drawLine(
                            color = stroke.color.copy(alpha = 0.25f),
                            start = p1,
                            end = p2,
                            strokeWidth = (stroke.width * 2.5f).dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        // Middle Primary Solid Color Layer
                        drawLine(
                            color = stroke.color,
                            start = p1,
                            end = p2,
                            strokeWidth = stroke.width.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        // Thin High-intensity Silver White Center Core
                        drawLine(
                            color = Color.White.copy(alpha = 0.85f),
                            start = p1,
                            end = p2,
                            strokeWidth = (stroke.width * 0.3f).dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // Draw Active Ongoing Ink Paths
            activePaintPaths.values.forEach { points ->
                if (points.size > 1) {
                    for (index in 0 until points.size - 1) {
                        val p1 = points[index]
                        val p2 = points[index + 1]
                        
                        // Wide Glow
                        drawLine(
                            color = selectedBrushColor.copy(alpha = 0.25f),
                            start = p1,
                            end = p2,
                            strokeWidth = (brushSizeSetting * 2.5f).dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        // Middle Color
                        drawLine(
                            color = selectedBrushColor,
                            start = p1,
                            end = p2,
                            strokeWidth = brushSizeSetting.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                        // Intense White Core
                        drawLine(
                            color = Color.White.copy(alpha = 0.85f),
                            start = p1,
                            end = p2,
                            strokeWidth = (brushSizeSetting * 0.3f).dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // 3. Render Particles in Action
            particles.forEach { p ->
                val lifeRatio = (p.life / p.maxLife).coerceIn(0f, 1f)
                val alpha = 0.75f * lifeRatio
                
                // Draw trailing glowing line
                val speed = kotlin.math.sqrt(p.vx * p.vx + p.vy * p.vy)
                if (speed > 0.4f) {
                    val trailLength = (1.6f * userScaleFactor).coerceIn(1.0f, 4.2f)
                    drawLine(
                        color = p.color.copy(alpha = alpha * 0.28f),
                        start = Offset(p.x, p.y),
                        end = Offset(p.x - p.vx * trailLength, p.y - p.vy * trailLength),
                        strokeWidth = (p.size * 0.45f * lifeRatio).coerceAtLeast(1f)
                    )
                }

                // Draw solid particle core
                drawCircle(
                    color = p.color.copy(alpha = alpha),
                    center = Offset(p.x, p.y),
                    radius = p.size * lifeRatio
                )
                
                // Draw soft overlay spark glow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = alpha * 0.70f), Color.Transparent),
                        center = Offset(p.x, p.y),
                        radius = (p.size * 1.6f * lifeRatio).coerceAtLeast(1f)
                    ),
                    center = Offset(p.x, p.y),
                    radius = (p.size * 1.6f * lifeRatio).coerceAtLeast(1f),
                    blendMode = BlendMode.Screen
                )
            }

            // 4. Render satisfying neon sound shockwave halos (YouTube Shorts Satisfying Myth Style)
            shockwaves.forEach { sw ->
                val progress = (1.0f - sw.life).coerceIn(0f, 1f)
                val currentRadius = sw.radius * progress
                val alpha = sw.life * 0.75f
                
                // Multi-layered visual explosion
                // Layer A: Beautiful neon halo outline
                drawCircle(
                    color = sw.color.copy(alpha = alpha),
                    center = Offset(sw.x, sw.y),
                    radius = currentRadius,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = (4.dp.toPx() * sw.life).coerceAtLeast(1f)
                    )
                )
                // Layer B: Subtle accent inner ring
                drawCircle(
                    color = activeTheme.primaryColor.copy(alpha = alpha * 0.40f),
                    center = Offset(sw.x, sw.y),
                    radius = currentRadius * 0.7f,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = (1.5.dp.toPx() * sw.life).coerceAtLeast(1f)
                    )
                )
                // Layer C: Soft radial color expansion
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(sw.color.copy(alpha = alpha * 0.35f), Color.Transparent),
                        center = Offset(sw.x, sw.y),
                        radius = currentRadius
                    ),
                    center = Offset(sw.x, sw.y),
                    radius = currentRadius
                )
            }

            // 4.5 Render Satisfying Cosmic Gravitational Vortex Event Horizons
            gravityVortices.forEach { vortex ->
                val baseRadius = 35.dp.toPx() * vortex.life
                val alpha = vortex.life
                val spinAngle = vortex.angle

                // Conical / Radial central core
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(vortex.color.copy(alpha = alpha * 0.7f), Color.Transparent),
                        center = Offset(vortex.x, vortex.y),
                        radius = baseRadius * 1.5f
                    ),
                    center = Offset(vortex.x, vortex.y),
                    radius = baseRadius * 1.5f,
                    blendMode = BlendMode.Screen
                )

                // Concentric spinning orbital halos
                for (ring in 0..2) {
                    val angleOffsetValue = ring * 45f
                    val ringRadius = baseRadius * (0.4f + ring * 0.5f)
                    val strokeWidth = (3.dp.toPx() - ring * 0.8.dp.toPx()) * vortex.life
                    
                    // Draw a spinning orbital arc
                    drawArc(
                        color = vortex.color.copy(alpha = alpha * 0.45f),
                        startAngle = spinAngle * (120f + ring * 25f) + angleOffsetValue,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(vortex.x - ringRadius, vortex.y - ringRadius),
                        size = androidx.compose.ui.geometry.Size(ringRadius * 2f, ringRadius * 2f),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                            width = strokeWidth.coerceAtLeast(1f),
                            cap = StrokeCap.Round
                        ),
                        blendMode = BlendMode.Screen
                    )
                }

                // Inner absolute cosmic vacuum center (black drop)
                drawCircle(
                    color = Color.Black.copy(alpha = alpha * 0.9f),
                    center = Offset(vortex.x, vortex.y),
                    radius = baseRadius * 0.35f
                )
                drawCircle(
                    color = vortex.color,
                    center = Offset(vortex.x, vortex.y),
                    radius = baseRadius * 0.08f
                )
            }

            // 5. Multi-Touch Specular Highlights
            activeTouches.values.forEach { touchInfo ->
                val touchPt = touchInfo.position
                val pressureFactor = touchInfo.pressure.coerceIn(0.15f, 3.0f)
                
                // Map larger pressure value proportionately to beautiful tactile highlight radius
                // Thumb is large (~75dp), pointer finger is medium (~36dp), pinky is small/precise (~16dp)
                val baseRadius = 30.dp.toPx() * userScaleFactor * (0.3f + 1.2f * pressureFactor)
                
                // Overlay liquid fluid blobs
                for (i in 0..1) {
                    val offsetAngle = t * (i + 1) + (i * 3f)
                    val dx = cos(offsetAngle) * 8.dp.toPx() * pressureFactor
                    val dy = sin(offsetAngle * 1.5f) * 8.dp.toPx() * pressureFactor
                    val center = touchPt + Offset(dx, dy)
                    val radius = baseRadius * 0.8f + sin(t * 3f + i) * 4.dp.toPx()

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                activeTheme.accentColor.copy(alpha = 0.35f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = radius
                        ),
                        center = center,
                        radius = radius,
                        blendMode = BlendMode.Screen
                    )
                }

                // Main Specular highlight center point
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            activeTheme.secondaryColor.copy(alpha = 0.85f),
                            Color.Transparent
                        ),
                        center = touchPt,
                        radius = baseRadius * 0.5f
                    ),
                    center = touchPt,
                    radius = baseRadius * 0.5f,
                    blendMode = BlendMode.Plus
                )
            }
        }

        // ==========================================
        // FLOATING TOP ACTION BAR
        // ==========================================
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.2f),
                        RoundedCornerShape(32.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACTIVE PROFILE // ${activeTheme.styleType.uppercase()}",
                        color = Color(0xFF00FFD1).copy(alpha = 0.85f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = activeTheme.name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hard Reset / Reload App Button
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onHardReset()
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFFF5252).copy(alpha = 0.22f), RoundedCornerShape(22.dp))
                            .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.6f), RoundedCornerShape(22.dp))
                    ) {
                        Text(
                            text = "🔄",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Vortex Mode Toggle Button
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isVortexMode = !isVortexMode
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                if (isVortexMode) activeTheme.primaryColor.copy(alpha = 0.32f) 
                                else Color.White.copy(alpha = 0.12f), 
                                RoundedCornerShape(22.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isVortexMode) activeTheme.primaryColor else Color.Transparent,
                                shape = RoundedCornerShape(22.dp)
                            )
                    ) {
                        Text(
                            text = "🌪️",
                            fontSize = 16.sp
                        )
                    }

                    // Clear Vortices Button (only visible when vortices are present)
                    if (gravityVortices.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                gravityVortices.clear()
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(22.dp))
                        ) {
                            Text(
                                text = "💥",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Open Settings Panel Button
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isSettingsOpen = true
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(22.dp))
                    ) {
                        Icon(Icons.Filled.Settings, contentDescription = "Playground Settings", tint = Color.White)
                    }

                    // Open Theme Selector
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            isMenuOpen = !isMenuOpen
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(22.dp))
                    ) {
                        Icon(Icons.Filled.Palette, contentDescription = "Themes", tint = Color.White)
                    }
                }
            }

            // ==========================================
            // ULTRA PRO MAX - PHASE 3 PRESET SELECTION ROW
            // ==========================================
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(physicsPresets) { preset ->
                    val isSelected = activePresetId == preset.id
                    val bgColor = if (isSelected) {
                        activeTheme.primaryColor.copy(alpha = 0.28f)
                    } else {
                        Color.White.copy(alpha = 0.08f)
                    }
                    val textColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(bgColor)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) activeTheme.primaryColor else Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                activePresetId = preset.id
                                
                                // Load preset physics settings
                                waveDampingSetting = preset.damping
                                fluidFlowPersistenceSetting = preset.flowDamping
                                dragVelocityMultiplier = preset.dragVelocity
                                particleSpeedSetting = preset.particleSpeed
                                particleLifeSetting = preset.particleLife
                                gravityIntensityScale = preset.gravityScale
                                
                                // Set preset theme
                                if (preset.themeId >= 0 && preset.themeId < glassThemes.size) {
                                    activeTheme = glassThemes[preset.themeId]
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = preset.emoji, fontSize = 14.sp)
                        Text(
                            text = preset.name,
                            color = textColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Real-time Cyber Diagnostic Telemetry HUD
            if (isLiveHudVisible) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 6.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.55f))
                        .border(
                            1.dp,
                            activeTheme.primaryColor.copy(alpha = 0.35f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📡 SYSTEM REALTIME METRICS",
                            color = activeTheme.primaryColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(activeTheme.accentColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "LATENCY: <4ms",
                                color = activeTheme.accentColor,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "✦ PHYSICS GRID : 24x48 CELLS (${24 * 48} NODES)",
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 9.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Text(
                        text = "✦ ACTIVE SPARKS : ${particles.size} / ${maxParticlesSetting.toInt()} CORES",
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 9.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Text(
                        text = "✦ FLUID VISCOSITY : ${String.format("%.3f", waveDampingSetting)} DAMP",
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 9.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Text(
                        text = "✦ GRAVITY TILT : X=${String.format("%.2f", tiltX)}, Y=${String.format("%.2f", tiltY)} (x${String.format("%.1f", gravityIntensityScale)})",
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 9.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Text(
                        text = "✦ SOUND CHANNELS : ${activeTouches.size} FLUIDIC STREAMS",
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 9.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Text(
                        text = "✦ VORTEX THRUST  : x${String.format("%.1f", dragVelocityMultiplier)} COEFFICIENT",
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 9.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }

            // Center Dynamic Scale display
            if (userScaleFactor != 1.0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .clickable { userScaleFactor = 1.0f } // reset scale on click
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Scale: ${String.format("%.2fx", userScaleFactor)} (Tap to Reset)",
                            color = Color(0xFF00FFD1),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Spacer instructions for empty surface
            if (activeTouches.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isPaintingMode) "Drag on screen to paint glowing neon waves" else "Touch/Pinch surface with multiple fingers",
                        color = Color.White.copy(alpha = 0.35f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // ==========================================
            // CANVASES & INTERACTIVE CONTROLS BAR (GLASS PANEL)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.65f))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Top control buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT: Mode Selector
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // INTERACTION MODE Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (!isPaintingMode) activeTheme.primaryColor.copy(alpha = 0.3f) else Color.Transparent)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isPaintingMode = false
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "🌀 INTERACT",
                                color = if (!isPaintingMode) activeTheme.primaryColor else Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // PAINT MODE Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isPaintingMode) activeTheme.primaryColor.copy(alpha = 0.3f) else Color.Transparent)
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isPaintingMode = true
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "🎨 NEON PAINT",
                                color = if (isPaintingMode) activeTheme.primaryColor else Color.White.copy(alpha = 0.6f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // RIGHT: State Actions (Pause, Clear, Undo)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Undo Button
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (paintStrokes.isNotEmpty()) {
                                    paintStrokes.removeAt(paintStrokes.size - 1)
                                }
                            },
                            enabled = paintStrokes.isNotEmpty(),
                            modifier = Modifier
                                .size(38.dp)
                                .background(
                                    if (paintStrokes.isNotEmpty()) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.03f),
                                    RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                text = "↰",
                                color = if (paintStrokes.isNotEmpty()) Color.White else Color.White.copy(alpha = 0.3f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Play / Pause Button
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                isPhysicsPaused = !isPhysicsPaused
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .background(
                                    if (isPhysicsPaused) Color(0xFFFF5252).copy(alpha = 0.25f) else Color.White.copy(alpha = 0.08f),
                                    RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                text = if (isPhysicsPaused) "▶" else "⏸",
                                color = if (isPhysicsPaused) Color(0xFFFF5252) else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Clear Button
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                paintStrokes.clear()
                                activePaintPaths.clear()
                                particles.clear()
                                shockwaves.clear()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                text = "🧹",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

                        // Hard Reset / Force Rerender Button
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                onHardReset()
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFFFF5252).copy(alpha = 0.16f), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                text = "🔄",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Expandable color picker panel / sliders
                if (isPaintingMode) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Brush Size Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "SIZE: ${brushSizeSetting.toInt()}DP",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(75.dp)
                            )
                            Slider(
                                value = brushSizeSetting,
                                onValueChange = { brushSizeSetting = it },
                                valueRange = 2f..24f,
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = selectedBrushColor,
                                    activeTrackColor = selectedBrushColor,
                                    inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                )
                            )
                        }

                        // Quick palette rounds + Custom Color Picker button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "BRUSH COLOR",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val paletteColors = listOf(
                                    Color(0xFF00FFD1), // Cyan/Mint
                                    Color(0xFFFF00FF), // Neon Pink
                                    Color(0xFF00B0FF), // Glacier Blue
                                    Color(0xFFFFD700), // Liquid Gold
                                    Color(0xFFFF3D00)  // Sunset Red
                                )

                                paletteColors.forEach { color ->
                                    val isSelected = selectedBrushColor == color
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(color)
                                            .border(
                                                width = if (isSelected) 2.dp else 0.dp,
                                                color = Color.White,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                                selectedBrushColor = color
                                                // Sync HSV state to the picked color for sliders
                                                val hsv = FloatArray(3)
                                                android.graphics.Color.colorToHSV(color.value.toInt(), hsv)
                                                pickerHue = hsv[0]
                                                pickerSat = hsv[1]
                                                pickerValue = hsv[2]
                                            }
                                    )
                                }

                                // Interactive custom color picker trigger
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(
                                            Brush.sweepGradient(
                                                listOf(
                                                    Color.Red, Color.Yellow, Color.Green, Color.Cyan,
                                                    Color.Blue, Color.Magenta, Color.Red
                                                )
                                            )
                                        )
                                        .border(
                                            width = if (isColorPickerOpen) 2.dp else 1.dp,
                                            color = Color.White,
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            isColorPickerOpen = !isColorPickerOpen
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "🌈",
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        // Sliding Custom Hue spectrum picker
                        if (isColorPickerOpen) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "SELECT SPECTRUM HUE",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                // Real-time Spectrum track drawing
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    Color.Red, Color.Yellow, Color.Green, Color.Cyan,
                                                    Color.Blue, Color.Magenta, Color.Red
                                                )
                                            )
                                        )
                                )

                                Slider(
                                    value = pickerHue,
                                    onValueChange = {
                                        pickerHue = it
                                        val c = hsvToColor(pickerHue, pickerSat, pickerValue)
                                        selectedBrushColor = c
                                        // Also dynamically updates ActiveTheme choice so fluid matches picked color perfectly!
                                        activeTheme = activeTheme.copy(
                                            primaryColor = c,
                                            accentColor = hsvToColor((pickerHue + 180f) % 360f, pickerSat * 0.8f, pickerValue),
                                            secondaryColor = hsvToColor((pickerHue + 60f) % 360f, pickerSat, pickerValue),
                                            backgroundGradient = Brush.linearGradient(listOf(c.copy(alpha = 0.18f), Color(0xFF08080A)))
                                        )
                                    },
                                    valueRange = 0f..360f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = selectedBrushColor,
                                        activeTrackColor = Color.Transparent,
                                        inactiveTrackColor = Color.Transparent
                                    )
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "SATURATION",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${(pickerSat * 100).toInt()}%",
                                        color = selectedBrushColor,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Slider(
                                    value = pickerSat,
                                    onValueChange = {
                                        pickerSat = it
                                        val c = hsvToColor(pickerHue, pickerSat, pickerValue)
                                        selectedBrushColor = c
                                    },
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = selectedBrushColor,
                                        activeTrackColor = selectedBrushColor,
                                        inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                    )
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "BRIGHTNESS",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${(pickerValue * 100).toInt()}%",
                                        color = selectedBrushColor,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Slider(
                                    value = pickerValue,
                                    onValueChange = {
                                        pickerValue = it
                                        val c = hsvToColor(pickerHue, pickerSat, pickerValue)
                                        selectedBrushColor = c
                                    },
                                    valueRange = 0.1f..1f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = selectedBrushColor,
                                        activeTrackColor = selectedBrushColor,
                                        inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // ==========================================
            // HORIZONTAL CAROUSEL SELECTOR
            // ==========================================
            AnimatedVisibility(
                visible = isMenuOpen,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(200, easing = FastOutLinearInEasing)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SWITCH ATMOSPHERE",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF00FFD1).copy(alpha = 0.18f), RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFF00FFD1).copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "12 STYLES",
                                color = Color(0xFF00FFD1),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        items(glassThemes) { theme ->
                            val isSelected = theme.id == activeTheme.id
                            Card(
                                modifier = Modifier
                                    .size(105.dp, 130.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        activeTheme = theme
                                        // Trigger a chime
                                        synth.triggerAtmosphereTone(520f + theme.id * 30f)
                                    }
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) theme.accentColor else Color.White.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(20.dp)
                                    ),
                                shape = RoundedCornerShape(20.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 0.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(theme.backgroundGradient)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.radialGradient(
                                                    colors = listOf(
                                                        theme.primaryColor.copy(alpha = 0.60f),
                                                        Color.Transparent
                                                    )
                                                )
                                            )
                                    )
                                    Text(
                                        text = theme.name,
                                        color = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(10.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }

    // ==========================================
    // INTERACTIVE PLAYGROUND SETTINGS DIALOG
    // ==========================================
    if (isSettingsOpen) {
        var selectedTab by remember { mutableStateOf(0) }
        AlertDialog(
            onDismissRequest = { isSettingsOpen = false },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { isSettingsOpen = false }) {
                    Text("DONE", color = activeTheme.primaryColor, fontWeight = FontWeight.Bold)
                }
            },
            shape = RoundedCornerShape(28.dp),
            containerColor = Color(0xFF101014),
            icon = {
                Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = activeTheme.primaryColor)
            },
            title = {
                Text(
                    text = "PLAYGROUND TUNER",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Styled Cyber Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("PHYSICS", "SPARKS", "SYSTEM").forEachIndexed { index, label ->
                            val isTabSelected = selectedTab == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isTabSelected) activeTheme.primaryColor.copy(alpha = 0.18f) else Color.Transparent)
                                    .clickable {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        selectedTab = index
                                    }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    color = if (isTabSelected) activeTheme.primaryColor else Color.White.copy(alpha = 0.5f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    when (selectedTab) {
                        0 -> {
                            // TAB 0: FLUID PHYSICS
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                // Viscosity (Wave Damping)
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Flow Viscosity (Damp)", color = Color.White, fontSize = 13.sp)
                                        Text(String.format("%.3f", waveDampingSetting), color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = waveDampingSetting,
                                        onValueChange = { waveDampingSetting = it },
                                        valueRange = 0.85f..0.995f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }

                                // Vortex Flow Persistence
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Vortex Flow Decay", color = Color.White, fontSize = 13.sp)
                                        Text(String.format("%.3f", fluidFlowPersistenceSetting), color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = fluidFlowPersistenceSetting,
                                        onValueChange = { fluidFlowPersistenceSetting = it },
                                        valueRange = 0.80f..0.99f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }

                                // Vortex Drag Sensitivity (Optimise Touch Velocity)
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Touch Force Boost", color = Color.White, fontSize = 13.sp)
                                        Text(String.format("%.1fx", dragVelocityMultiplier), color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = dragVelocityMultiplier,
                                        onValueChange = { dragVelocityMultiplier = it },
                                        valueRange = 0.5f..5.0f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }
                            }
                        }
                        1 -> {
                            // TAB 1: SPARK PARTICLES
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                // Max Particles Pool Limit
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Max Spark Limit", color = Color.White, fontSize = 13.sp)
                                        Text("${maxParticlesSetting.toInt()}", color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = maxParticlesSetting,
                                        onValueChange = { maxParticlesSetting = it },
                                        valueRange = 20f..350f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }

                                // Spark Dispersal Speed
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Spark Launch Velocity", color = Color.White, fontSize = 13.sp)
                                        Text(String.format("%.1fx", particleSpeedSetting), color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = particleSpeedSetting,
                                        onValueChange = { particleSpeedSetting = it },
                                        valueRange = 0.2f..3.0f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }

                                // Spark Lifetime
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Spark Decay Lifetime", color = Color.White, fontSize = 13.sp)
                                        Text(String.format("%.2fs", particleLifeSetting), color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = particleLifeSetting,
                                        onValueChange = { particleLifeSetting = it },
                                        valueRange = 0.3f..2.5f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }

                                // Emission Density (inverse of particlesDensitySetting delay timer)
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Emission Pulse Rate", color = Color.White, fontSize = 13.sp)
                                        val displayDns = ((0.16f - particlesDensitySetting) * 625f).toInt()
                                        Text("$displayDns%", color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = 0.16f - particlesDensitySetting,
                                        onValueChange = { particlesDensitySetting = (0.16f - it).coerceIn(0.005f, 0.15f) },
                                        valueRange = 0.01f..0.155f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }
                            }
                        }
                        2 -> {
                            // TAB 2: AUDIO & SYSTEM CONFIG
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                // Audio Synthesizer Volume Control
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Synthesizer Volume", color = Color.White, fontSize = 13.sp)
                                        Text("${(synthesizerVolume * 100).toInt()}%", color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = synthesizerVolume,
                                        onValueChange = { synthesizerVolume = it },
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }

                                // Gravity Force Scale
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Gravity Tilt Sensitivity", color = Color.White, fontSize = 13.sp)
                                        Text(String.format("%.1fx", gravityIntensityScale), color = activeTheme.primaryColor, fontSize = 13.sp)
                                    }
                                    Slider(
                                        value = gravityIntensityScale,
                                        onValueChange = { gravityIntensityScale = it },
                                        valueRange = 0.0f..3.0f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = activeTheme.primaryColor,
                                            activeTrackColor = activeTheme.primaryColor,
                                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                                        )
                                    )
                                }

                                // Accelerometer Tilt switch
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Gravity Tilt Physics", color = Color.White, fontSize = 13.sp)
                                        Text("Warp water waves by tilting device", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                                    }
                                    Switch(
                                        checked = isTiltPhysicsEnabled,
                                        onCheckedChange = { isTiltPhysicsEnabled = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = activeTheme.primaryColor,
                                            checkedTrackColor = activeTheme.primaryColor.copy(alpha = 0.35f)
                                        )
                                    )
                                }

                                // Interactive Diagnostics HUD toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Realtime Diagnostics HUD", color = Color.White, fontSize = 13.sp)
                                        Text("Display live telemetry matrices", color = Color.White.copy(alpha = 0.4f), fontSize = 10.sp)
                                    }
                                    Switch(
                                        checked = isLiveHudVisible,
                                        onCheckedChange = { isLiveHudVisible = it },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = activeTheme.primaryColor,
                                            checkedTrackColor = activeTheme.primaryColor.copy(alpha = 0.35f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
