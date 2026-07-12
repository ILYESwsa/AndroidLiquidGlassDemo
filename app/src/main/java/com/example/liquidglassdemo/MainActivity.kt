package com.example.liquidglassdemo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.emptyBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberBackdrop
import com.kyant.backdrop.backdrops.rememberCanvasBackdrop
import com.kyant.backdrop.backdrops.rememberCombinedBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (supportsLiveBackdropEffects()) {
                LiquidGlassDemoApp()
            } else {
                LiquidGlassCompatibilityApp()
            }
        }
    }
}


private fun supportsLiveBackdropEffects(): Boolean {
    // CMP Backdrop live blur/lens/vibrancy is enabled for Android 12+ on both physical and emulated devices, including Waydroid Android 13.
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

@Composable
fun LiquidGlassCompatibilityApp() {
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF6D5DFB))) {
        var selectedTab by remember { mutableIntStateOf(0) }
        Box(Modifier.fillMaxSize().background(Color(0xFFF8FAFF))) {
            DemoBackdropArt()
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp, 24.dp, 20.dp, 112.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Header()
                ActiveTabSummary(selectedTab)
                FeatureCard(
                    "Liquid glass compatibility mode",
                    "This Android 11 device uses a stable glass-styled fallback: translucent gradients, bright rims, tint, and soft depth. Android 12+ devices, including Waydroid Android 13, show live CMP Backdrop blur, lens, and vibrancy rendering."
                )
                CompatGlassCard("Glass Bottom Bar", "Tinted surface over the same artwork, without runtime backdrop capture.")
                CompatGlassCard("Interactive Glass Bottom Bar", "Simple pills preserve layout and readability on older devices.")
                CompatGlassCard("Glass Bottom Sheet", "Rounded translucent sheet with a visible blue tint.")
                CompatGlassCard("Glass Slider", "Compose-only track preview for Android 11.")
                CompatGlassCard("Tinted glass icon button", "Hue-style pink tint represented with a safe translucent surface.")
            }
            CompatBottomBar(
                selected = selectedTab,
                onSelect = { selectedTab = it },
                modifier = Modifier.align(Alignment.BottomCenter).padding(20.dp)
            )
        }
    }
}


private fun Modifier.liquidGlassFallback(
    shape: RoundedCornerShape,
    tint: Color = Color.White
): Modifier = this
    .shadow(
        elevation = 18.dp,
        shape = shape,
        ambientColor = Color(0xFF7C3AED).copy(.18f),
        spotColor = Color(0xFF06B6D4).copy(.14f)
    )
    .clip(shape)
    .background(
        Brush.linearGradient(
            listOf(
                tint.copy(.64f),
                Color.White.copy(.34f),
                Color(0xFFD7E8FF).copy(.28f),
                Color(0xFFFFD6E7).copy(.18f)
            )
        )
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            listOf(
                Color.White.copy(.92f),
                Color.White.copy(.30f),
                Color(0xFF8B5CF6).copy(.22f)
            )
        ),
        shape = shape
    )

@Composable
private fun CompatGlassCard(title: String, body: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .liquidGlassFallback(RoundedCornerShape(28.dp), Color.White)
            .padding(22.dp)
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF101828))
        Spacer(Modifier.height(8.dp))
        Text(body, color = Color(0xFF475467))
    }
}

@Composable
private fun CompatBottomBar(selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(72.dp)
            .liquidGlassFallback(RoundedCornerShape(28.dp), Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Home", "Glass", "Tune").forEachIndexed { index, label ->
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        if (index == selected) {
                            Brush.linearGradient(
                                listOf(
                                    Color.White.copy(.78f),
                                    Color(0xFFDDEBFF).copy(.52f),
                                    Color(0xFFC7B7FF).copy(.28f)
                                )
                            )
                        } else {
                            Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                        }
                    )
                    .border(
                        width = if (index == selected) 1.dp else 0.dp,
                        brush = Brush.linearGradient(listOf(Color.White.copy(.90f), Color.White.copy(.24f))),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(label, fontWeight = if (index == selected) FontWeight.Bold else FontWeight.Medium)
            }
        }
    }
}

@Composable
fun LiquidGlassDemoApp() {
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF6D5DFB))) {
        val hostBackdrop = rememberLayerBackdrop()
        val canvasBackdrop = rememberCanvasBackdrop { DemoBackdropArt() }
        val combinedBackdrop = rememberCombinedBackdrop(hostBackdrop, canvasBackdrop)
        var selectedTab by remember { mutableIntStateOf(0) }
        Box(Modifier.fillMaxSize().background(Color(0xFFF8FAFF))) {
            DemoBackdropArt()
            Column(
                Modifier
                    .fillMaxSize()
                    .layerBackdrop(hostBackdrop) // Records the nav host so foreground glass can redraw it.
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp, 24.dp, 20.dp, 112.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Header()
                ActiveTabSummary(selectedTab)
                FeatureCard("Glass Bottom Bar over a main nav host", "The host content is captured with rememberLayerBackdrop; the bar redraws that background with blur, lensing, vibrancy and a translucent tint.")
                InteractiveGlassBottomBar(combinedBackdrop, selectedTab) { selectedTab = it }
                GlassBottomSheet(combinedBackdrop)
                GlassSlider(combinedBackdrop)
                TintedGlassIconButton(combinedBackdrop)
                FallbackGlass(emptyBackdrop())
            }
            GlassBottomBar(combinedBackdrop, selectedTab) { selectedTab = it }
        }
    }
}


@Composable
private fun ActiveTabSummary(selectedTab: Int) {
    val tabs = listOf(
        "Home" to "A calm dashboard surface behind the glass navigation bar.",
        "Glass" to "Backdrop-heavy examples that show blur, lensing, vibrancy, and tint.",
        "Tune" to "Controls and sliders for exploring glass intensity and readability."
    )
    val (title, body) = tabs[selectedTab.coerceIn(tabs.indices)]
    Column(
        Modifier
            .fillMaxWidth()
            .liquidGlassFallback(RoundedCornerShape(28.dp), Color(0xFFE0EAFF))
            .padding(20.dp)
    ) {
        Text("Selected tab: $title", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF101828))
        Spacer(Modifier.height(6.dp))
        Text(body, color = Color(0xFF475467))
    }
}

@Composable
private fun Header() {
    Text("CMP Backdrop\nLiquid Glass", fontSize = 38.sp, lineHeight = 40.sp, fontWeight = FontWeight.Black, color = Color(0xFF101828))
    Text("Compose-style demo focused on backdrop drawing, blur, lens distortion, vibrancy, readable backgrounds, and visible glass tint.", color = Color(0xFF475467))
}

@Composable
private fun FeatureCard(title: String, body: String) {
    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(Color.White.copy(.72f)).padding(22.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(Modifier.height(8.dp))
        Text(body, color = Color(0xFF667085))
    }
}

@Composable
private fun BoxScope.GlassBottomBar(backdrop: Backdrop, selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier
            .align(Alignment.BottomCenter)
            .padding(20.dp)
            .fillMaxWidth()
            .height(72.dp)
            .drawBackdrop(backdrop, shape = { RoundedCornerShape(28.dp) }, effects = { vibrancy(); blur(18.dp.toPx()); lens(20.dp.toPx(), 32.dp.toPx(), chromaticAberration = true) }, onDrawSurface = { drawRect(Color.White.copy(.38f)); drawRect(Color(0xFF7C3AED).copy(.10f)) })
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) { listOf("Home", "Glass", "Tune").forEachIndexed { i, label -> TabPill(label, i == selected) { onSelect(i) } } }
}

@Composable
private fun RowScope.TabPill(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(22.dp)).background(if (selected) Color.White.copy(.42f) else Color.Transparent).clickable(onClick = onClick), contentAlignment = Alignment.Center) {
        Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium, color = Color(0xFF1D2939))
    }
}

@Composable
private fun InteractiveGlassBottomBar(backdrop: Backdrop, selected: Int, onSelect: (Int) -> Unit) {
    val localRecorder = rememberLayerBackdrop()
    Section("Interactive Glass Bottom Bar") {
        Row(Modifier.layerBackdrop(localRecorder).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf("Blur", "Lens", "Glow").forEachIndexed { index, label ->
                val combined = rememberCombinedBackdrop(backdrop, rememberBackdrop(localRecorder) { it() })
                Box(Modifier.weight(1f).height(54.dp).graphicsLayer { scaleX = if (selected == index) 1.05f else 1f; scaleY = scaleX }.drawBackdrop(combined, shape = { RoundedCornerShape(20.dp) }, effects = { vibrancy(); blur(10.dp.toPx()); lens(12.dp.toPx(), 18.dp.toPx()) }, onDrawSurface = { drawRect(Color.White.copy(if (selected == index) .50f else .24f)) }).clickable { onSelect(index) }, contentAlignment = Alignment.Center) { Text(label, fontWeight = FontWeight.SemiBold) }
            }
        }
    }
}

@Composable
private fun GlassBottomSheet(backdrop: Backdrop) = Section("Glass Bottom Sheet") {
    Column(Modifier.fillMaxWidth().drawBackdrop(backdrop, shape = { RoundedCornerShape(32.dp) }, effects = { vibrancy(); blur(22.dp.toPx()); lens(18.dp.toPx(), 26.dp.toPx()) }, onDrawSurface = { drawRect(Color(0xFFE0EAFF).copy(.38f)) }).padding(22.dp)) {
        Box(Modifier.align(Alignment.CenterHorizontally).size(44.dp, 5.dp).clip(CircleShape).background(Color(0xFF98A2B3)))
        Spacer(Modifier.height(16.dp)); Text("Readable glass sheet", fontWeight = FontWeight.Bold); Text("The sheet paints a translucent blue surface after the backdrop effects, keeping foreground text legible.", color = Color(0xFF475467))
    }
}

@Composable
private fun GlassSlider(backdrop: Backdrop) = Section("Glass Slider") {
    var value by remember { mutableFloatStateOf(.62f) }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Slider(value = value, onValueChange = { value = it })
        Box(Modifier.fillMaxWidth(value).height(34.dp).drawBackdrop(backdrop, shape = { RoundedCornerShape(18.dp) }, effects = { blur(12.dp.toPx()); lens(14.dp.toPx(), 20.dp.toPx()) }, onDrawSurface = { drawRect(Color(0xFF4F46E5).copy(.22f)) }))
    }
}

@Composable
private fun TintedGlassIconButton(backdrop: Backdrop) = Section("Tinted glass icon button") {
    Box(Modifier.size(68.dp).drawBackdrop(backdrop, shape = { CircleShape }, effects = { vibrancy(); blur(10.dp.toPx()); lens(16.dp.toPx(), 22.dp.toPx()) }, onDrawSurface = { drawRect(Color(0xFFFF2D55), blendMode = BlendMode.Hue); drawRect(Color.White.copy(.34f)) }), contentAlignment = Alignment.Center) { Text("✦", fontSize = 28.sp, color = Color(0xFF111827)) }
}

@Composable
private fun FallbackGlass(backdrop: Backdrop) = Section("emptyBackdrop fallback") { Text("emptyBackdrop is useful for previews, placeholders, or disabled effects while keeping the same glass component API.", Modifier.drawBackdrop(backdrop, shape = { RoundedCornerShape(18.dp) }, effects = { blur(6.dp.toPx()) }, onDrawSurface = { drawRect(Color.White.copy(.26f)) }).padding(16.dp)) }

@Composable
private fun Section(title: String, content: @Composable ColumnScope.() -> Unit) = Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { Text(title, fontWeight = FontWeight.Bold, fontSize = 22.sp); content() }

private fun DrawScope.DemoBackdropArt() {
    drawRect(Brush.linearGradient(listOf(Color(0xFFFFF7AD), Color(0xFFFFD6E7), Color(0xFFD7E8FF))))
    drawCircle(Color(0xFF7C3AED).copy(.35f), 260f, Offset(size.width * .18f, size.height * .18f))
    drawCircle(Color(0xFF06B6D4).copy(.30f), 340f, Offset(size.width * .86f, size.height * .34f))
    drawCircle(Color(0xFFFF7A00).copy(.24f), 300f, Offset(size.width * .42f, size.height * .70f))
}

@Composable
private fun DemoBackdropArt() = Canvas(Modifier.fillMaxSize()) { DemoBackdropArt() }
