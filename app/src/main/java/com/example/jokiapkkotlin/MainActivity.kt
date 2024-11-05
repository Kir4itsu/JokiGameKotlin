package com.example.jokiapkkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp


// Data Classes
data class Game(
    val name: String,
    val description: String,
    val iconResId: Int  // Menggunakan resource ID untuk icon
)

// Main Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainContent()
            }
        }
    }
}

// Utility Functions
fun getGames(): List<Game> {
    return listOf(
        Game(
            "Genshin Impact",
            "Open World RPG Game",
            R.drawable.ic_genshin  // Pastikan Anda memiliki icon ini di res/drawable
        ),
        Game(
            "Mobile Legends",
            "5v5 MOBA Game",
            R.drawable.ic_mlbb  // Pastikan Anda memiliki icon ini di res/drawable
        ),
        Game(
            "PUBG Mobile",
            "Battle Royale Game",
            R.drawable.ic_pubg  // Pastikan Anda memiliki icon ini di res/drawable
        ),
        Game(
            "Free Fire",
            "Battle Royale Game",
            R.drawable.ic_ff  // Pastikan Anda memiliki icon ini di res/drawable
        )
    )
}

// Reusable Components
@Composable
fun GameIcon(game: Game, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = game.iconResId),
        contentDescription = "${game.name} Icon",
        modifier = modifier
    )
}

@Composable
fun ServiceCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun RankSelector(
    ranks: List<String>,
    selectedRank: String,
    onRankSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ranks.forEach { rank ->
            Button(
                onClick = { onRankSelected(rank) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedRank == rank)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(rank)
            }
        }
    }
}

// Main Content
@Composable
fun MainContent() {
    var isLoggedIn by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedGame by remember { mutableStateOf<Game?>(null) }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            !isLoggedIn -> LoginScreen(
                username = username,
                password = password,
                onUsernameChange = { username = it },
                onPasswordChange = { password = it },
                onLoginClick = {
                    if (username == "admin" && password == "admin") {
                        isLoggedIn = true
                        Toast.makeText(context, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Username atau password salah!", Toast.LENGTH_SHORT).show()
                    }
                }
            )
            selectedGame == null -> GameSelectionScreen(
                onGameSelected = { game -> selectedGame = game },
                onLogout = {
                    isLoggedIn = false
                    username = ""
                    password = ""
                    Toast.makeText(context, "Berhasil logout!", Toast.LENGTH_SHORT).show()
                }
            )
            else -> GameDetailScreen(
                game = selectedGame!!,
                onBackClick = { selectedGame = null }
            )
        }
    }
}

// Login Screen
@Composable
fun LoginScreen(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_joystick),
                    contentDescription = "Login Icon",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Login Game Service",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("Username") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Person Icon"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Lock Icon"
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Login Icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Login")
                }
            }
        }
    }
}

@Composable
fun GameSelectionScreen(
    onGameSelected: (Game) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Game Service Menu",
                style = MaterialTheme.typography.headlineMedium
            )

            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout"
                )
            }
        }

        LazyColumn {
            items(getGames()) { game ->
                GameCard(game = game, onClick = { onGameSelected(game) })
            }
        }
    }
}

@Composable
fun GameCard(game: Game, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameIcon(
                game = game,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = game.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// Game Detail Screen
@Composable
fun GameDetailScreen(
    game: Game,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = game.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        when (game.name) {
            "Genshin Impact" -> GenshinImpactContent()
            "Mobile Legends" -> MobileLegendContent()
            "PUBG Mobile" -> PUBGMobileContent()
            "Free Fire" -> FreeFireContent()
        }
    }
}

// Game-Specific Content
//Genshin Impact Start
@Composable
fun GenshinImpactContent() {
    var uid by remember { mutableStateOf("") }
    var adventureRank by remember { mutableStateOf("") }
    var serverRegion by remember { mutableStateOf("Asia") }
    var selectedService by remember { mutableStateOf("Spiral Abyss") }

    // Spiral Abyss State
    var selectedFloor by remember { mutableStateOf(9) }
    var targetStars by remember { mutableStateOf(6) }

    // Exploration State
    var selectedRegion by remember { mutableStateOf("Mondstadt") }
    var explorationTarget by remember { mutableStateOf(80) }
    var includeChests by remember { mutableStateOf(true) }
    var includeOculus by remember { mutableStateOf(true) }

    // Character Building State
    var selectedCharacter by remember { mutableStateOf("") }
    var targetLevel by remember { mutableStateOf(90) }
    var includeTalents by remember { mutableStateOf(true) }
    var targetTalentLevel by remember { mutableStateOf(8) }
    var includeArtifacts by remember { mutableStateOf(true) }

    // Daily Commission State
    var commissionDays by remember { mutableStateOf(1) }
    var includeResin by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Account Information Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FormSection(title = "Account Information") {
                    OutlinedTextField(
                        value = uid,
                        onValueChange = { uid = it },
                        label = { Text("UID") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = adventureRank,
                        onValueChange = { adventureRank = it },
                        label = { Text("Adventure Rank") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = serverRegion,
                        onValueChange = { serverRegion = it },
                        label = { Text("Server Region") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Service Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FormSection(title = "Select Service") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("Spiral Abyss", "Exploration", "Character Building", "Daily Commission").forEach { service ->
                            FilterChip(
                                selected = selectedService == service,
                                onClick = { selectedService = service },
                                label = { Text(service) }
                            )
                        }
                    }
                }
            }
        }

        // Dynamic Service Content
        when (selectedService) {
            "Spiral Abyss" -> SpiralAbyssForm(
                selectedFloor = selectedFloor,
                onFloorChange = { selectedFloor = it },
                targetStars = targetStars,
                onTargetStarsChange = { targetStars = it }
            )

            "Exploration" -> ExplorationForm(
                selectedRegion = selectedRegion,
                onRegionChange = { selectedRegion = it },
                explorationTarget = explorationTarget,
                onExplorationTargetChange = { explorationTarget = it },
                includeChests = includeChests,
                onIncludeChestsChange = { includeChests = it },
                includeOculus = includeOculus,
                onIncludeOculusChange = { includeOculus = it }
            )

            "Character Building" -> CharacterBuildingForm(
                selectedCharacter = selectedCharacter,
                onCharacterChange = { selectedCharacter = it },
                targetLevel = targetLevel,
                onTargetLevelChange = { targetLevel = it },
                includeTalents = includeTalents,
                onIncludeTalentsChange = { includeTalents = it },
                targetTalentLevel = targetTalentLevel,
                onTargetTalentLevelChange = { targetTalentLevel = it },
                includeArtifacts = includeArtifacts,
                onIncludeArtifactsChange = { includeArtifacts = it }
            )

            "Daily Commission" -> DailyCommissionForm(
                commissionDays = commissionDays,
                onCommissionDaysChange = { commissionDays = it },
                includeResin = includeResin,
                onIncludeResinChange = { includeResin = it }
            )
        }

        // Price Summary Section
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                FormSection(title = "Price Summary") {
                    val basePrice = when (selectedService) {
                        "Spiral Abyss" -> calculateSpiralAbyssPrice(selectedFloor, targetStars)
                        "Exploration" -> calculateExplorationPrice(explorationTarget, includeChests, includeOculus)
                        "Character Building" -> calculateCharacterBuildingPrice(targetLevel, includeTalents, targetTalentLevel, includeArtifacts)
                        "Daily Commission" -> calculateDailyCommissionPrice(commissionDays, includeResin)
                        else -> 0
                    }

                    Text(
                        text = "Selected Service: $selectedService",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    when (selectedService) {
                        "Spiral Abyss" -> {
                            Text("• Floor $selectedFloor")
                            Text("• Target: $targetStars Stars")
                        }
                        "Exploration" -> {
                            Text("• Region: $selectedRegion")
                            Text("• Target: $explorationTarget%")
                            if (includeChests) Text("• Include Chest Hunting")
                            if (includeOculus) Text("• Include Oculus Collection")
                        }
                        "Character Building" -> {
                            Text("• Character: $selectedCharacter")
                            Text("• Target Level: $targetLevel")
                            if (includeTalents) Text("• Talents to Level $targetTalentLevel")
                            if (includeArtifacts) Text("• Include Artifact Farming")
                        }
                        "Daily Commission" -> {
                            Text("• Duration: $commissionDays days")
                            if (includeResin) Text("• Include Resin Management")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Total Price: Rp ${basePrice.toString().chunked(3).joinToString(".")}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = { /* Handle order submission */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Order Service")
                    }
                }
            }
        }
    }
}

@Composable
fun SpiralAbyssForm(
    selectedFloor: Int,
    onFloorChange: (Int) -> Unit,
    targetStars: Int,
    onTargetStarsChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FormSection(title = "Spiral Abyss Details") {
                Text("Select Floor", modifier = Modifier.padding(bottom = 4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (9..12).forEach { floor ->
                        FilterChip(
                            selected = selectedFloor == floor,
                            onClick = { onFloorChange(floor) },
                            label = { Text("Floor $floor") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Target Stars", modifier = Modifier.padding(bottom = 4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(6, 7, 8, 9).forEach { stars ->
                        FilterChip(
                            selected = targetStars == stars,
                            onClick = { onTargetStarsChange(stars) },
                            label = { Text("$stars ★") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExplorationForm(
    selectedRegion: String,
    onRegionChange: (String) -> Unit,
    explorationTarget: Int,
    onExplorationTargetChange: (Int) -> Unit,
    includeChests: Boolean,
    onIncludeChestsChange: (Boolean) -> Unit,
    includeOculus: Boolean,
    onIncludeOculusChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FormSection(title = "Exploration Details") {
                Text("Select Region", modifier = Modifier.padding(bottom = 4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Mondstadt", "Liyue", "Inazuma", "Sumeru", "Fontaine").forEach { region ->
                        FilterChip(
                            selected = selectedRegion == region,
                            onClick = { onRegionChange(region) },
                            label = { Text(region) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Exploration Target", modifier = Modifier.padding(bottom = 4.dp))
                Slider(
                    value = explorationTarget.toFloat(),
                    onValueChange = { onExplorationTargetChange(it.toInt()) },
                    valueRange = 0f..100f,
                    steps = 20
                )
                Text("Target: $explorationTarget%")

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Checkbox(
                            checked = includeChests,
                            onCheckedChange = onIncludeChestsChange
                        )
                        Text("Include Chests")
                    }
                    Column {
                        Checkbox(
                            checked = includeOculus,
                            onCheckedChange = onIncludeOculusChange
                        )
                        Text("Include Oculus")
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterBuildingForm(
    selectedCharacter: String,
    onCharacterChange: (String) -> Unit,
    targetLevel: Int,
    onTargetLevelChange: (Int) -> Unit,
    includeTalents: Boolean,
    onIncludeTalentsChange: (Boolean) -> Unit,
    targetTalentLevel: Int,
    onTargetTalentLevelChange: (Int) -> Unit,
    includeArtifacts: Boolean,
    onIncludeArtifactsChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FormSection(title = "Character Building Details") {
                OutlinedTextField(
                    value = selectedCharacter,
                    onValueChange = onCharacterChange,
                    label = { Text("Character Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Text("Target Level", modifier = Modifier.padding(bottom = 4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(70, 80, 90).forEach { level ->
                        FilterChip(
                            selected = targetLevel == level,
                            onClick = { onTargetLevelChange(level) },
                            label = { Text("Lv. $level") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Include Talents")
                    Switch(
                        checked = includeTalents,
                        onCheckedChange = onIncludeTalentsChange
                    )
                }

                if (includeTalents) {
                    Text("Target Talent Level", modifier = Modifier.padding(vertical = 4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(6, 8, 10).forEach { level ->
                            FilterChip(
                                selected = targetTalentLevel == level,
                                onClick = { onTargetTalentLevelChange(level) },
                                label = { Text("Lv. $level") }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Include Artifact Farming")
                    Switch(
                        checked = includeArtifacts,
                        onCheckedChange = onIncludeArtifactsChange
                    )
                }
            }
        }
    }
}

@Composable
fun DailyCommissionForm(
    commissionDays: Int,
    onCommissionDaysChange: (Int) -> Unit,
    includeResin: Boolean,
    onIncludeResinChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            FormSection(title = "Daily Commission Details") {
                Text("Number of Days", modifier = Modifier.padding(bottom = 4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(1, 3, 7, 14, 30).forEach { days ->
                        FilterChip(
                            selected = commissionDays == days,
                            onClick = { onCommissionDaysChange(days) },
                            label = { Text("$days days") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Include Resin Management")
                    Switch(
                        checked = includeResin,
                        onCheckedChange = onIncludeResinChange
                    )
                }
            }
        }
    }
}

@Composable
fun FormSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        content()
    }
}

// Price calculation functions
private fun calculateSpiralAbyssPrice(floor: Int, targetStars: Int): Int {
    val basePrice = when (floor) {
        9 -> 50000
        10 -> 75000
        11 -> 100000
        12 -> 150000
        else -> 50000
    }

    val starMultiplier = when (targetStars) {
        6 -> 1.0
        7 -> 1.2
        8 -> 1.5
        9 -> 2.0
        else -> 1.0
    }

    return (basePrice * starMultiplier).toInt()
}

private fun calculateExplorationPrice(
    explorationTarget: Int,
    includeChests: Boolean,
    includeOculus: Boolean
): Int {
    var price = (explorationTarget * 1000)

    if (includeChests) price += 50000
    if (includeOculus) price += 75000

    return price
}

private fun calculateCharacterBuildingPrice(
    targetLevel: Int,
    includeTalents: Boolean,
    targetTalentLevel: Int,
    includeArtifacts: Boolean
): Int {
    var price = when (targetLevel) {
        70 -> 100000
        80 -> 150000
        90 -> 250000
        else -> 100000
    }

    if (includeTalents) {
        price += when (targetTalentLevel) {
            6 -> 50000
            8 -> 100000
            10 -> 200000
            else -> 50000
        }
    }

    if (includeArtifacts) {
        price += 150000
    }

    return price
}

private fun calculateDailyCommissionPrice(days: Int, includeResin: Boolean): Int {
    var price = days * 25000

    if (includeResin) {
        price += days * 15000
    }

    return price
}
//Genshin Impact End


@Composable
fun MobileLegendContent() {
    var selectedRank by remember { mutableStateOf("Epic") }
    var matches by remember { mutableStateOf("10") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ServiceCard(title = "Rank Boost") {
            RankSelector(
                ranks = listOf("Epic", "Legend", "Mythic"),
                selectedRank = selectedRank,
                onRankSelected = { selectedRank = it }
            )
        }

        ServiceCard(title = "Win Rate Boost") {
            Column {
                OutlinedTextField(
                    value = matches,
                    onValueChange = { matches = it },
                    label = { Text("Number of Matches") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Calculate Price")
                }
            }
        }
    }
}

@Composable
fun PUBGMobileContent() {
    var selectedRank by remember { mutableStateOf("Platinum") }
    var targetKD by remember { mutableStateOf("") }
    var matches by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ServiceCard(title = "Rank Push") {
            RankSelector(
                ranks = listOf("Platinum", "Diamond", "Crown", "Ace"),
                selectedRank = selectedRank,
                onRankSelected = { selectedRank = it }
            )
        }

        ServiceCard(title = "K/D Ratio Boost") {
            Column {
                OutlinedTextField(
                    value = targetKD,
                    onValueChange = { targetKD = it },
                    label = { Text("Target K/D Ratio") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Calculate Price")
                }
            }
        }

        ServiceCard(title = "Win Rate Boost") {
            Column {
                OutlinedTextField(
                    value = matches,
                    onValueChange = { matches = it },
                    label = { Text("Number of Matches") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Order Service")
                }
            }
        }
    }
}

@Composable
fun FreeFireContent() {
    var selectedRank by remember { mutableStateOf("Gold") }
    var selectedStat by remember { mutableStateOf("Headshot") }
    var roomSize by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ServiceCard(title = "Rank Push") {
            RankSelector(
                ranks = listOf("Gold", "Diamond", "Heroic", "Grandmaster"),
                selectedRank = selectedRank,
                onRankSelected = { selectedRank = it }
            )
        }

        ServiceCard(title = "Character Stats Boost") {
            RankSelector(
                ranks = listOf("Headshot", "K/D", "Accuracy"),
                selectedRank = selectedStat,
                onRankSelected = { selectedStat = it }
            )
        }

        ServiceCard(title = "Custom Room Service") {
            Column {
                OutlinedTextField(
                    value = roomSize,
                    onValueChange = { roomSize = it },
                    label = { Text("Number of Slots") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Order Custom Room")
                }
            }
        }
    }
}