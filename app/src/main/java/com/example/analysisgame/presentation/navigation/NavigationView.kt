package com.example.analysisgame.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.analysisgame.R
import com.example.analysisgame.common.Constants.Companion.INTER_FONT_FAMILY

@Composable
fun NavigationView(
    modifier: Modifier,
    onHomeClick: () -> Unit,
    onBookingClick: () -> Unit,
    onFavouritesClick: () -> Unit,
    onProfileClick: () -> Unit,
    btnId: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        //home
        /*Column(
            modifier = Modifier
                .width(80.dp)
                .clickable {
                    onHomeClick()
                }
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.nav_home),
                contentDescription = "nav_icon",
                modifier = Modifier,
                colorFilter = ColorFilter.tint(if (btnId == 1) GreenMain else Color.Gray)
            )
            Text(
                text = stringResource(id = R.string.home),
                color = if (btnId == 1) GreenMain else Color.Gray,
                fontFamily = INTER_FONT_FAMILY,
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        //bookings
        Column(
            modifier = Modifier
                .width(80.dp)
                .clickable {
                    onBookingClick()
                }
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.nav_bookings),
                contentDescription = "nav_icon",
                modifier = Modifier,
                colorFilter = ColorFilter.tint(if (btnId == 2) GreenMain else Color.Gray)
            )
            Text(
                text = stringResource(id = R.string.bookings),
                color = if (btnId == 2) GreenMain else Color.Gray,
                fontFamily = INTER_FONT_FAMILY,
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        //favourites
        Column(
            modifier = Modifier
                .width(80.dp)
                .clickable {
                    onFavouritesClick()
                }
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.nav_favourites),
                contentDescription = "nav_icon",
                modifier = Modifier,
                colorFilter = ColorFilter.tint(if (btnId == 3) GreenMain else Color.Gray)
            )
            Text(
                text = stringResource(id = R.string.favourites),
                color = if (btnId == 3) GreenMain else Color.Gray,
                fontFamily = INTER_FONT_FAMILY,
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        //profile
        Column(
            modifier = Modifier
                .width(80.dp)
                .clickable {
                    onProfileClick()
                }
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.nav_profile),
                contentDescription = "nav_icon",
                modifier = Modifier,
                colorFilter = ColorFilter.tint(if (btnId == 4) GreenMain else Color.Gray)
            )
            Text(
                text = stringResource(id = R.string.profile),
                color = if (btnId == 4) GreenMain else Color.Gray,
                fontFamily = INTER_FONT_FAMILY,
                fontSize = 12.sp,
                maxLines = 1
            )
        }*/


    }
}