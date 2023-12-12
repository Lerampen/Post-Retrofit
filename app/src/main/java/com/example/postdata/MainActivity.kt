@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.postdata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.postdata.model.Users
import com.example.postdata.network.PostApi
import com.example.postdata.ui.theme.PostDataTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PostDataTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
MyApp()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    var name by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }


    val coroutinescope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange ={name = it},
            label = { Text(text = "Name")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )
        //Textfield for job
        OutlinedTextField(
            value = job,
            onValueChange ={job = it},
            label = { Text(text = "Job")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
            
        )
        
        Button(onClick = {
            if (!isLoading){
                
                isLoading = true

                coroutinescope.launch(Dispatchers.IO) {
                    try {
                        val response: Response<Users> =
                        PostApi.retrofitService.postData(Users(name,job))

                        withContext(Dispatchers.Main){
                            if (response.isSuccessful){
                                val user : Users? = response.body()
                                responseMessage = "Data Saved: Name ${user?.name}, Job - ${user?.job}"
                            }else{
                                responseMessage = "Error: ${response.message()}"
                            }
                        }
                    }catch (e: IOException){
                        withContext(Dispatchers.Main){
                            responseMessage = "Network Error: ${e.message}"
                        }
                    }finally {
                        isLoading = false
                    }
                }

            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
        ) {
            Text(text = "Send Data to API")
        }
        if (isLoading){
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        responseMessage?.let {
            Text(text = it)
        }
        
    }



}

@Preview(showBackground = true)
@Composable
fun PreviewMyApp() {
    MyApp()
}
