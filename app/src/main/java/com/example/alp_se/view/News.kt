//package com.example.alp_se.view
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Divider
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.alp_se.R
//
//// Data class for News
//data class NewsItem(
//    val imageRes: Int,
//    val title: String,
//    val description: String
//)
//
//@Composable
//fun NewsScreen(newsList: List<NewsItem>, modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color(0xFF222222))
//    ) {
//        // Header Section
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF222222)),
//            contentAlignment = Alignment.Center
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(R.drawable.community_removebg_preview),
//                    contentDescription = "Logo",
//                    modifier = Modifier.size(80.dp)
//                )
//                Spacer(modifier = Modifier.weight(1f))
//            }
//
//            Text(
//                text = "News",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                textAlign = TextAlign.Center
//            )
//        }
//        Divider(
//            color = Color.Gray,
//            thickness = 1.dp,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // News List Section
//        LazyColumn(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 16.dp)
//        ) {
//            items(newsList) { news ->
//                NewsItemView(news)
//                Divider(color = Color.Gray, thickness = 1.dp)
//            }
//        }
//
//        // Bottom Navigation
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF222222))
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.baseline_home_filled_24),
//                    contentDescription = "Home",
//                    modifier = Modifier.size(40.dp)
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.baseline_search_24),
//                    contentDescription = "Search",
//                    modifier = Modifier.size(40.dp)
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.champion),
//                    contentDescription = "Champion",
//                    modifier = Modifier.size(40.dp)
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.baseline_groups_24),
//                    contentDescription = "Team",
//                    modifier = Modifier.size(40.dp)
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.baseline_person_24),
//                    contentDescription = "Profile",
//                    modifier = Modifier.size(40.dp)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun NewsItemView(newsItem: NewsItem) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Image(
//            painter = painterResource(newsItem.imageRes),
//            contentDescription = newsItem.title,
//            modifier = Modifier
//                .size(120.dp)
//                .padding(end = 20.dp)
//        )
//        Column(modifier = Modifier.weight(1f).padding(10.dp)) {
//            Text(
//                text = newsItem.title,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White
//            )
//            Spacer(modifier = Modifier.height(2.dp))
//            Text(
//                text = newsItem.description,
//                fontSize = 14.sp,
//                color = Color.Gray
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun NewsScreenPreview() {
//    // Generate a list of NewsItem dynamically
//    val previewNewsList = List(10) { index ->
//        NewsItem(
//            imageRes = R.drawable.shortnsweet,
//            title = "M6 Bracket MLBB $index",
//            description = "Preview Detail for item $index"
//        )
//    }
//    NewsScreen(newsList = previewNewsList)
//}
//=======
////package com.example.alp_se.view
////
////import androidx.compose.foundation.Image
////import androidx.compose.foundation.background
////import androidx.compose.foundation.layout.*
////import androidx.compose.foundation.lazy.LazyColumn
////import androidx.compose.foundation.lazy.items
////import androidx.compose.material3.Divider
////import androidx.compose.material3.Text
////import androidx.compose.runtime.Composable
////import androidx.compose.ui.Alignment
////import androidx.compose.ui.Modifier
////import androidx.compose.ui.graphics.Color
////import androidx.compose.ui.res.painterResource
////import androidx.compose.ui.text.font.FontWeight
////import androidx.compose.ui.text.style.TextAlign
////import androidx.compose.ui.tooling.preview.Preview
////import androidx.compose.ui.unit.dp
////import androidx.compose.ui.unit.sp
////
////// Data class for News
////data class NewsItem(
////    val imageRes: Int,
////    val title: String,
////    val description: String
////)
////
////@Composable
////fun NewsScreen(newsList: List<NewsItem>, modifier: Modifier = Modifier) {
////    Column(
////        modifier = modifier
////            .fillMaxSize()
////            .background(Color(0xFF222222))
////    ) {
////        // Header Section
////        Box(
////            modifier = Modifier
////                .fillMaxWidth()
////                .background(Color(0xFF222222)),
////            contentAlignment = Alignment.Center
////        ) {
////            Row(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(horizontal = 16.dp),
////                verticalAlignment = Alignment.CenterVertically
////            ) {
////                Image(
////                    painter = painterResource(R.drawable.community_removebg_preview),
////                    contentDescription = "Logo",
////                    modifier = Modifier.size(80.dp)
////                )
////                Spacer(modifier = Modifier.weight(1f))
////            }
////
////            Text(
////                text = "News",
////                fontSize = 20.sp,
////                fontWeight = FontWeight.Bold,
////                color = Color.White,
////                textAlign = TextAlign.Center
////            )
////        }
////        Divider(
////            color = Color.Gray,
////            thickness = 1.dp,
////            modifier = Modifier.fillMaxWidth()
////        )
////
////        // News List Section
////        LazyColumn(
////            modifier = Modifier
////                .weight(1f)
////                .padding(horizontal = 16.dp)
////        ) {
////            items(newsList) { news ->
////                NewsItemView(news)
////                Divider(color = Color.Gray, thickness = 1.dp)
////            }
////        }
////
////        // Bottom Navigation
////        Box(
////            modifier = Modifier
////                .fillMaxWidth()
////                .background(Color(0xFF222222))
////        ) {
////            Row(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(10.dp),
////                verticalAlignment = Alignment.CenterVertically,
////                horizontalArrangement = Arrangement.SpaceAround
////            ) {
////                Image(
////                    painter = painterResource(id = R.drawable.baseline_home_filled_24),
////                    contentDescription = "Home",
////                    modifier = Modifier.size(40.dp)
////                )
////                Image(
////                    painter = painterResource(id = R.drawable.baseline_search_24),
////                    contentDescription = "Search",
////                    modifier = Modifier.size(40.dp)
////                )
////                Image(
////                    painter = painterResource(id = R.drawable.champion),
////                    contentDescription = "Champion",
////                    modifier = Modifier.size(40.dp)
////                )
////                Image(
////                    painter = painterResource(id = R.drawable.baseline_groups_24),
////                    contentDescription = "Team",
////                    modifier = Modifier.size(40.dp)
////                )
////                Image(
////                    painter = painterResource(id = R.drawable.baseline_person_24),
////                    contentDescription = "Profile",
////                    modifier = Modifier.size(40.dp)
////                )
////            }
////        }
////    }
////}
////
////@Composable
////fun NewsItemView(newsItem: NewsItem) {
////    Row(
////        modifier = Modifier
////            .fillMaxWidth()
////            .padding(vertical = 8.dp),
////        verticalAlignment = Alignment.CenterVertically
////    ) {
////        Image(
////            painter = painterResource(newsItem.imageRes),
////            contentDescription = newsItem.title,
////            modifier = Modifier
////                .size(120.dp)
////                .padding(end = 20.dp)
////        )
////        Column(modifier = Modifier.weight(1f).padding(10.dp)) {
////            Text(
////                text = newsItem.title,
////                fontSize = 16.sp,
////                fontWeight = FontWeight.Bold,
////                color = Color.White
////            )
////            Spacer(modifier = Modifier.height(2.dp))
////            Text(
////                text = newsItem.description,
////                fontSize = 14.sp,
////                color = Color.Gray
////            )
////        }
////    }
////}
////
////@Preview(showBackground = true)
////@Composable
////fun NewsScreenPreview() {
////    // Generate a list of NewsItem dynamically
////    val previewNewsList = List(10) { index ->
////        NewsItem(
////            imageRes = R.drawable.shortnsweet,
////            title = "M6 Bracket MLBB $index",
////            description = "Preview Detail for item $index"
////        )
////    }
////    NewsScreen(newsList = previewNewsList)
////}
//>>>>>>> Stashed changes
