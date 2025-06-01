    package com.example.alp_se.view

    import android.content.Context
    import android.net.Uri
    import android.widget.Toast
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
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
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.text.KeyboardActions
    import androidx.compose.foundation.text.KeyboardOptions
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.DropdownMenu
    import androidx.compose.material3.DropdownMenuItem
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.ExposedDropdownMenuBox
    import androidx.compose.material3.ExposedDropdownMenuDefaults
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextField
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.TextStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavController
    import androidx.navigation.compose.rememberNavController
    import coil.compose.rememberAsyncImagePainter
    import com.example.alp_se.R
    import com.example.alp_se.uiStates.StringDataStatusUIState
    import com.example.alp_se.viewModels.TournamentViewModel
    import androidx.compose.material3.MenuDefaults
    import androidx.compose.material3.TextFieldDefaults
    import androidx.compose.ui.platform.LocalFocusManager
    import androidx.compose.ui.text.input.ImeAction
    import androidx.compose.ui.text.input.KeyboardType
    import androidx.compose.ui.text.style.TextAlign


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

    LaunchedEffect(Unit) { // Use Unit as key to run once when the composable enters the composition
        // Check if we are in "create" mode (i.e., no tournament being updated)
        // This is crucial for when you navigate back to a "create" state
        if (tournamentViewModel.currentTournament == null) {
            tournamentViewModel.initializeForEdit(null)
        }
    }

    LaunchedEffect(tournamentViewModel.submissionStatus) {
        val dataStatus = tournamentViewModel.submissionStatus
        if (dataStatus is StringDataStatusUIState.Success) {
            Toast.makeText(context, dataStatus.data, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
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

        LaunchedEffect(tournamentViewModel.submissionStatus) {
            val dataStatus = tournamentViewModel.submissionStatus
            if (dataStatus is StringDataStatusUIState.Success) {
                Toast.makeText(context, dataStatus.data, Toast.LENGTH_SHORT).show()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
        ) {
            // Enhanced Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2D2D2D))
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.community_removebg_preview),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(end = 16.dp)
                    )
                    Text(
                        text = if (tournamentViewModel.currentTournament == null) "Create Tournament" else "Update Tournament", // ⭐ Dynamic text ⭐
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

//             Image Upload Section
            item {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            color = Color(0xFF2D2D2D),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val imageUrl = tournamentViewModel.imageInput
                    if (imageUrl.isNotEmpty()) {
                        val modelToLoad = if (imageUrl.startsWith("content://")) {
                            Uri.parse(imageUrl)
                        } else {
                            "http://10.0.2.2:3000${imageUrl}"
                        }

                            Image(
                                painter = rememberAsyncImagePainter(model = modelToLoad), // ⭐ Use the determined modelToLoad ⭐
                                contentDescription = "Tournament Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        } else {
                            // Placeholder when no image is selected
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.community_removebg_preview),
                                    contentDescription = "Upload Icon",
                                    modifier = Modifier.size(48.dp)
                                )
                                Text(
                                    text = "Upload Tournament Banner",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { launcher.launch("image/*") }, // Launches image picker
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2)),
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "Choose Image",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Form Fields
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        CustomTextField(
                            value = tournamentViewModel.nameTournamentInput,
                            onValueChange = {
                                tournamentViewModel.nameTournamentInput = it
                                tournamentViewModel.updateNameTournamentInput(it)
                            },
                            label = "Tournament Name"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        CustomTextField(
                            value = tournamentViewModel.descriptionInput,
                            onValueChange = {
                                tournamentViewModel.descriptionInput = it
                                tournamentViewModel.updateDescriptionInput(it)
                            },
                            label = "Description",
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        DoubleTextField(
                            value = tournamentViewModel.costInput,
                            onValueChange = { newInt ->
                                tournamentViewModel.costInput = newInt
                            },
                            label = "Entry Cost"
                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        CustomTextField(
                            value = tournamentViewModel.lokasiInput,
                            onValueChange = {
                                tournamentViewModel.lokasiInput = it
                                tournamentViewModel.updateLokasiInput(it)
                            },
                            label = "Location",
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        BestOfDropdown(
                            selectedOption = tournamentViewModel.typeInput,
                            onOptionSelected = { tournamentViewModel.typeInput = it }
                        )
                    }
                }

                // Submit Button
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            if (tournamentViewModel.currentTournament != null) {
                                tournamentViewModel.updateTournament(
                                    navController = navController,
                                    tournamentId = tournamentViewModel.currentTournament!!.TournamentID,
                                    nameTournamentInput = tournamentViewModel.nameTournamentInput,
                                    descriptionInput = tournamentViewModel.descriptionInput,
                                    costInput = tournamentViewModel.costInput,
                                    imageInput = tournamentViewModel.imageInput,
                                    typeInput = tournamentViewModel.typeInput,
                                    lokasi = tournamentViewModel.lokasiInput,
                                    token = token,
                                    context = context
                                )
                            } else {
                                tournamentViewModel.createTournament(
                                    navController = navController,
                                    token = token,
                                    context = context
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "Create Tournament",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // Submit Button
            item {
                Spacer(modifier = Modifier.height(24.dp))
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
                            if(tournamentViewModel.nameTournamentInput.isNullOrEmpty() || tournamentViewModel.descriptionInput.isNullOrEmpty() || tournamentViewModel.costInput == 0 || tournamentViewModel.lokasiInput.isNullOrEmpty() || tournamentViewModel.typeInput.isNullOrEmpty()){
                                Toast.makeText(context, "Error, Please Fill all Inputs", Toast.LENGTH_SHORT).show()
                            }else{
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
                        containerColor = Color(0xFF4A90E2)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    if(tournamentViewModel.currentTournament != null){
                        Text(
                            text = "Update Tournament",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }else{
                        Text(
                            text = "Create Tournament",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if(tournamentViewModel.currentTournament != null){
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
                            .height(56.dp)
                            .padding(top = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = "Delete Tournament",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    @Composable
    private fun CustomTextField(
        value: String,
        onValueChange: (String) -> Unit,
        label: String,
        maxLines: Int = 1
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2D2D2D), RoundedCornerShape(12.dp)),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                maxLines = maxLines,
                shape = RoundedCornerShape(12.dp),
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DoubleTextField(
        value: Int,
        onValueChange: (Int) -> Unit,
        label: String,
        maxLines: Int = 1
    ) {
        // keep the textual representation in sync with `value`
        var text by remember(value) {
            mutableStateOf(if (value == 0) "" else value.toString())
        }
        val focusManager = LocalFocusManager.current

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = text,
                onValueChange = { newText ->
                    // allow only digits and one decimal point
                    if (newText.matches(Regex("^\\d*\\.?\\d*\$"))) {
                        text = newText
                        // update the Double if parsable
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
                    .background(Color(0xFF2D2D2D), RoundedCornerShape(12.dp)),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = maxLines
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

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = modifier
        ) {
            // your existing OutlinedTextField styling...
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { /* readOnly */ },
                readOnly = true,
                label = { Text("Match Format", color = Color.White) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .background(Color(0xFF2D2D2D), RoundedCornerShape(12.dp)),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                shape = RoundedCornerShape(12.dp),
            )

            // instead of ExposedDropdownMenu, use DropdownMenu so you can set containerColor
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .exposedDropdownSize(matchTextFieldWidth = true),         // :contentReference[oaicite:0]{index=0}
                          // :contentReference[oaicite:1]{index=1}
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },

                    )
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

