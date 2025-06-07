package com.example.alp_se.view

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.alp_se.R
import com.example.alp_se.uiStates.TournamentDataStatusUIState
import com.example.alp_se.viewModels.TournamentViewModel

@Composable
fun CreateTournament(
    navController: NavController,
    tournamentViewModel: TournamentViewModel,
    context: Context,
    token: String,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { newValue ->
                tournamentViewModel.imageInput = newValue.toString()
                tournamentViewModel.updateImageInput(tournamentViewModel.imageInput)
            }
        }
    )

    LaunchedEffect(Unit) {
        if (tournamentViewModel.currentTournament == null) {
            tournamentViewModel.initializeForEdit(null)
        }
    }

    LaunchedEffect(tournamentViewModel.submissionStatus) {
        val dataStatus = tournamentViewModel.submissionStatus
        if (dataStatus is TournamentDataStatusUIState.Success) {
            Toast.makeText(context, "Fetched ${dataStatus.data.size} tournaments", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0F23),
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Enhanced Header with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF6C63FF),
                                Color(0xFF9C88FF),
                                Color(0xFF6C63FF)
                            )
                        )
                    )
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        painter = painterResource(R.drawable.community_removebg_preview),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                CircleShape
                            )
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = if (tournamentViewModel.currentTournament == null) "Create Tournament" else "Update Tournament",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item {
                    Text(
                        text = "Tournament Details",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Fill in the information below to create your tournament",
                        fontSize = 16.sp,
                        color = Color(0xFFB0B3B8),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                // Enhanced Image Upload Section
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E2E)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Tournament Banner",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .size(220.dp)
                                    .background(
                                        color = Color(0xFF2D2D3D),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .border(
                                        2.dp,
                                        Color(0xFF6C63FF).copy(alpha = 0.3f),
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                val imageUrl = tournamentViewModel.imageInput
                                if (imageUrl.isNotEmpty()) {
                                    val modelToLoad = if (imageUrl.startsWith("content://")) {
                                        Uri.parse(imageUrl)
                                    } else {
                                        "http://192.168.88.32:3000${imageUrl}"
                                    }

                                    Image(
                                        painter = rememberAsyncImagePainter(model = modelToLoad),
                                        contentDescription = "Tournament Image",
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(64.dp)
                                                .background(
                                                    Color(0xFF6C63FF).copy(alpha = 0.2f),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CloudUpload,
                                                contentDescription = "Upload",
                                                tint = Color(0xFF6C63FF),
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = "Upload Tournament Banner",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "JPG, PNG up to 10MB",
                                            color = Color(0xFFB0B3B8),
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = { launcher.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                border = BorderStroke(2.dp, Color(0xFF6C63FF)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = "Choose Image",
                                        tint = Color(0xFF6C63FF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Choose Image",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF6C63FF)
                                    )
                                }
                            }
                        }
                    }
                }

                // Enhanced Form Fields
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E2E)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            CustomTextField(
                                value = tournamentViewModel.nameTournamentInput,
                                onValueChange = {
                                    tournamentViewModel.nameTournamentInput = it
                                    tournamentViewModel.updateNameTournamentInput(it)
                                },
                                label = "Tournament Name",
                                icon = Icons.Default.EmojiEvents
                            )

                            CustomTextField(
                                value = tournamentViewModel.descriptionInput,
                                onValueChange = {
                                    tournamentViewModel.descriptionInput = it
                                    tournamentViewModel.updateDescriptionInput(it)
                                },
                                label = "Description",
                                maxLines = 3,
                                icon = Icons.Default.Description
                            )

                            DoubleTextField(
                                value = tournamentViewModel.costInput,
                                onValueChange = { newInt ->
                                    tournamentViewModel.costInput = newInt
                                },
                                label = "Entry Cost",
                                icon = Icons.Default.AttachMoney
                            )

                            CustomTextField(
                                value = tournamentViewModel.lokasiInput,
                                onValueChange = {
                                    tournamentViewModel.lokasiInput = it
                                    tournamentViewModel.updateLokasiInput(it)
                                },
                                label = "Location",
                                maxLines = 2,
                                icon = Icons.Default.LocationOn
                            )

                            BestOfDropdown(
                                selectedOption = tournamentViewModel.typeInput,
                                onOptionSelected = { tournamentViewModel.typeInput = it }
                            )
                        }
                    }
                }

                // Enhanced Submit Button
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                if (tournamentViewModel.currentTournament != null) {
                                    tournamentViewModel.updateTournament(
                                        navController = navController,
                                        tournamentId = tournamentViewModel.currentTournament!!.TournamentID,
                                        token = token,
                                        context = context
                                    )
                                } else {
                                    if (tournamentViewModel.nameTournamentInput.isNullOrEmpty() ||
                                        tournamentViewModel.descriptionInput.isNullOrEmpty() ||
                                        tournamentViewModel.costInput == 0 ||
                                        tournamentViewModel.lokasiInput.isNullOrEmpty() ||
                                        tournamentViewModel.typeInput.isNullOrEmpty()
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "Error, Please Fill all Inputs",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        tournamentViewModel.createTournament(
                                            navController = navController,
                                            token = token,
                                            context = context
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF6C63FF),
                                                Color(0xFF9C88FF)
                                            )
                                        ),
                                        shape = RoundedCornerShape(28.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (tournamentViewModel.currentTournament != null)
                                            Icons.Default.Refresh else Icons.Default.Add,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = if (tournamentViewModel.currentTournament != null)
                                            "Update Tournament" else "Create Tournament",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        if (tournamentViewModel.currentTournament != null) {
                            Button(
                                onClick = {
                                    tournamentViewModel.deleteTournament(
                                        navController = navController,
                                        tournamentId = tournamentViewModel.currentTournament!!.TournamentID,
                                        token = token
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                border = BorderStroke(2.dp, Color(0xFFFF6B6B)),
                                shape = RoundedCornerShape(28.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color(0xFFFF6B6B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Delete Tournament",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFF6B6B)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLines: Int = 1,
    icon: ImageVector
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6C63FF),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF2D2D3D),
                    RoundedCornerShape(12.dp)
                ),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6C63FF),
                unfocusedBorderColor = Color(0xFF404040),
                cursorColor = Color(0xFF6C63FF)
            )
        )
    }
}

@Composable
fun DoubleTextField(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    maxLines: Int = 1,
    icon: ImageVector
) {
    var text by remember(value) {
        mutableStateOf(if (value == 0) "" else value.toString())
    }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF6C63FF),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                if (newText.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    text = newText
                    newText.toIntOrNull()?.let { onValueChange(it) }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFF2D2D3D),
                    RoundedCornerShape(12.dp)
                ),
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            shape = RoundedCornerShape(12.dp),
            maxLines = maxLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6C63FF),
                unfocusedBorderColor = Color(0xFF404040),
                cursorColor = Color(0xFF6C63FF)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestOfDropdown(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf("Best of 1", "Best of 3")
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Match Format",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { /* readOnly */ },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded,
//                        modifier = Modifier.rotate(if (expanded) 180f else 0f)
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .background(
                        Color(0xFF2D2D3D),
                        RoundedCornerShape(12.dp)
                    ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C63FF),
                    unfocusedBorderColor = Color(0xFF404040),
                    cursorColor = Color(0xFF6C63FF)
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .exposedDropdownSize(matchTextFieldWidth = true)
                    .background(
                        Color(0xFF2D2D3D),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                color = Color.White
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTournamentPreview() {
    CreateTournament(
        navController = rememberNavController(),
        tournamentViewModel = viewModel<TournamentViewModel>(factory = TournamentViewModel.Factory),
        context = LocalContext.current,
        token = ""
    )
}