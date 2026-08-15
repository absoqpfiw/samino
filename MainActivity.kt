package com.samino.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF090B18)
private val Card = Color(0xFF15182A)
private val Purple = Color(0xFF8B3DFF)
private val TextMain = Color(0xFFF6F2FF)
private val TextDim = Color(0xFFAAA5BA)

data class Post(val user: String, val text: String, val likes: Int, val community: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SaminoApp() }
    }
}

@Composable
fun SaminoApp() {
    var tab by remember { mutableIntStateOf(0) }
    var showCreate by remember { mutableStateOf(false) }
    var posts by remember {
        mutableStateOf(listOf(
            Post("Roxy", "من هنا تبدأ صداقات جديدة 💜 ما هو مجتمعك المفضل؟", 128, "Anime World"),
            Post("Zoro", "ما رأيكم بأفضل لعبة لهذا الأسبوع؟ 🎮", 87, "Gamer Zone"),
            Post("Moonlight", "شاركوني رسوماتكم الجديدة ✨", 54, "Art & Design")
        ))
    }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Purple, background = Bg, surface = Card,
        onBackground = TextMain, onSurface = TextMain
    )) {
        Scaffold(
            containerColor = Bg,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showCreate = true },
                    containerColor = Purple, contentColor = Color.White
                ) { Text("+", fontSize = 28.sp) }
            },
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF101225)) {
                    val labels = listOf("الرئيسية", "المجتمعات", "الدردشة", "ملفي")
                    labels.forEachIndexed { i, label ->
                        NavigationBarItem(
                            selected = tab == i,
                            onClick = { tab = i },
                            icon = { Text(listOf("⌂","◉","💬","●")[i], fontSize = 20.sp) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        ) { pad ->
            Box(Modifier.padding(pad).fillMaxSize()) {
                when(tab) {
                    0 -> Home(posts)
                    1 -> Communities()
                    2 -> Chats()
                    else -> Profile()
                }
            }
        }

        if (showCreate) {
            CreatePost(
                onClose = { showCreate = false },
                onPublish = { text ->
                    if (text.isNotBlank()) {
                        posts = listOf(Post("أنت", text, 0, "Samino")) + posts
                    }
                    showCreate = false
                }
            )
        }
    }
}

@Composable
fun TopBar(title: String) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Text("⌕  🔔", fontSize = 22.sp, color = TextDim)
    }
}

@Composable
fun Home(posts: List<Post>) {
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 14.dp)) {
        item { TopBar("Samino") }
        item {
            Text("مجتمعاتك", color = TextDim, modifier = Modifier.padding(6.dp))
            Row(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                listOf("Anime", "ألعاب", "فن", "أفلام").forEach {
                    Surface(
                        color = Card, shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) { Text(it, Modifier.padding(horizontal = 15.dp, vertical = 9.dp)) }
                }
            }
        }
        items(posts) { PostCard(it) }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Card),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).clip(CircleShape).background(Purple))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(post.user, fontWeight = FontWeight.Bold)
                    Text(post.community, color = TextDim, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(post.text, fontSize = 16.sp)
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(22.dp)) {
                Text("♥ ${post.likes}", color = Color(0xFFFF5B8D))
                Text("💬 23", color = TextDim)
                Text("↗", color = TextDim)
            }
        }
    }
}

@Composable
fun Communities() {
    val communities = listOf(
        "Anime World" to "128K عضو",
        "Gamer Zone" to "95K عضو",
        "Art & Design" to "72K عضو",
        "Music Vibes" to "64K عضو",
        "Space" to "48K عضو"
    )
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 14.dp)) {
        item { TopBar("المجتمعات") }
        item {
            OutlinedTextField(
                value = "", onValueChange = {}, enabled = false,
                placeholder = { Text("ابحث عن مجتمع...", color = TextDim) },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )
        }
        items(communities) { (name, members) ->
            Card(colors = CardDefaults.cardColors(containerColor = Card),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(Purple))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(name, fontWeight = FontWeight.Bold)
                        Text(members, color = TextDim, fontSize = 12.sp)
                    }
                    Button(onClick = {}) { Text("انضمام") }
                }
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun Chats() {
    val chats = listOf("Nora" to "كيف حالك؟", "Anime World" to "تم نشر منشور جديد 🔥",
        "Kaito" to "شكراً على المشاركة!", "Sara" to "أرسلت لك صورة")
    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 14.dp)) {
        item { TopBar("الدردشات") }
        items(chats) { (name, msg) ->
            Row(Modifier.fillMaxWidth().clickable {}.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(50.dp).clip(CircleShape).background(Purple))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(name, fontWeight = FontWeight.Bold)
                    Text(msg, color = TextDim)
                }
            }
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun Profile() {
    Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(20.dp))
        Box(Modifier.size(105.dp).clip(CircleShape).background(Purple),
            contentAlignment = Alignment.Center) {
            Text("S", fontSize = 54.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        Text("Samino User", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("@samino_user", color = TextDim)
        Spacer(Modifier.height(22.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Stat("89", "منشور")
            Stat("356", "متابع")
            Stat("1.2K", "متابعة")
        }
        Spacer(Modifier.height(25.dp))
        Button(onClick = {}) { Text("تعديل الملف الشخصي") }
    }
}

@Composable
fun Stat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = TextDim, fontSize = 12.sp)
    }
}

@Composable
fun CreatePost(onClose: () -> Unit, onPublish: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("منشور جديد") },
        text = {
            OutlinedTextField(
                value = text, onValueChange = { text = it },
                placeholder = { Text("شارك أفكارك مع المجتمع...") },
                minLines = 4, modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = { Button(onClick = { onPublish(text) }) { Text("نشر") } },
        dismissButton = { TextButton(onClick = onClose) { Text("إلغاء") } }
    )
}
