package dev.seh.basicscodelab2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.seh.basicscodelab2.ui.theme.BasicsCodeLab2Theme
import dev.seh.basicscodelab2.ui.theme.myColor1
import dev.seh.basicscodelab2.ui.theme.myColor2
import dev.seh.basicscodelab2.ui.theme.myColor3

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodeLab2Theme {
                myApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun myApp() {
    var shouldShowOnboarding by rememberSaveable  { mutableStateOf(true) }
    if (shouldShowOnboarding) {
        OnboardingScreen({ shouldShowOnboarding = false })
    } else {
        Surface(
            color = MaterialTheme.colors.background
        )
        {
            // A surface container using the 'background' color from the theme
            // Column 은 위아래로 쌓임
            // 즉 뷰가 column 전체를 먹음
            // 반대로 Row 는 옆으로 쌓임
            // 즉 뷰가 row 전체를 먹음
            Column {
/*
                Greeting("Android")
                Greeting("Compose")
                textListView(orientation = true)
                textListView(orientation = false)
                buttonBorderLayout()
*/
                lazyColumnText()
            }
        }

    }

}

@Composable
fun textListView(
    list: List<String> = listOf("java", "kotlin"),
    colors: List<Color> = listOf(myColor1, myColor2),
    orientation: Boolean
) {
    if (orientation) {
        Column() {
            for (i in 0..1) {
                textBorder(name = list[i], borderWidth = 2, color = colors[i])
            }
        }
    } else {
        Row(
            modifier = Modifier.horizontalScroll(
                enabled = true,
                reverseScrolling = true,
                state = ScrollState(0),
                flingBehavior = null
            )
        ) {
            for (page in 0..2) {
                for (i in 0..1) {
                    textBorder(name = list[i], borderWidth = 2, color = colors[i])
                }
            }
        }

    }

}

@Composable
fun OnboardingScreen(onClickEvent: () -> Unit) {
    // TODO: This state should be hoisted
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onClickEvent
            ) {
                Text("Continue")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodeLab2Theme() {
        OnboardingScreen(onClickEvent = {})
    }
}

@Composable
fun lazyColumnText(names:List<String> = List(1000){"$it"}) {
    // 그냥 Column 을 쓸경우 포퍼먼스 매우 떨어짐
/*
    Column {
        for(name in names){
            buttonBorderLayout()
        }
    }
*/
    LazyColumn() {
        items(items = names) { name ->
            buttonBorderLayout(name)
        }
    }
}
@Composable
fun buttonBorderLayout(name:String) {
    val context = LocalContext.current
    // rememberSaveable 을 통해 상태 기억
    // remember 를 쓰면 포커스 뷰를 벗어나거나 화면 전환( onStop ) 으로 갈시 초기화됨

    val isExpand = rememberSaveable { mutableStateOf(false) }
    val expandPadding = if (isExpand.value) 28.dp else 0.dp

    val extraPadding by animateDpAsState(
        if (isExpand.value) 28.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessHigh
        )
    )
    var surfaceColor = rememberSaveable{
        mutableStateOf(true)
    }

    Surface(
        color =  if (surfaceColor.value)  myColor1 else myColor2 ,
    ) {
        Row(
            modifier = Modifier
                .padding(25.dp)
                .padding(bottom = extraPadding.coerceAtLeast(0.dp))
        ) {
            Text(
                text=name,
                style = MaterialTheme.typography.h4.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                modifier = Modifier.padding(end = 14.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text("myCustom")
                Text("layout")
                if(isExpand.value){
                    Text(text = ("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(4),)
                }
            }
            IconButton(onClick = {
                isExpand.value = !isExpand.value
                surfaceColor.value = !surfaceColor.value
            }) {
                Icon(imageVector = if(isExpand.value) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if(isExpand.value) "close" else "open" )
            }
/*
            OutlinedButton(onClick = {
                isExpand.value = !isExpand.value
                Toast.makeText(context, "button click", Toast.LENGTH_SHORT).show()
            }) {
                if (isExpand.value) {
                    Text(
                        text = "more!!!!",
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    surfaceColor.value = false

                } else {
                    Text(
                        text = "click me",
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    surfaceColor.value = true
                }
            }
*/

        }

    }

}

@Composable
fun textBorder(name: String, borderWidth: Int, color: Color) {
    Surface(
        color = color,
        modifier = Modifier.padding(vertical = 13.dp, horizontal = 13.dp),

        ) {
        Text(
            text = name,
            modifier = Modifier
                .fillMaxWidth()
                .border(width = borderWidth.dp, color = Color.Black)
                .padding(23.dp),
            fontSize = 34.sp,
            color = Color.White

        )
    }
}

@Composable
fun Greeting(name: String) {
    Surface(color = MaterialTheme.colors.primary) {
        Text(
            text = name,
            modifier = Modifier.padding(40.dp), fontSize = 35.sp
        )
    }
}

@Composable
fun DefaultPreview() {
    BasicsCodeLab2Theme {
        Greeting("Android")
    }
}