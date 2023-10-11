package core.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Bolt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.theme.Green
import domain.model.Character
import feature.paginator.LoadingType

@Composable
fun CharacterContent(
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit = {},
    characters: List<Character> = emptyList(),
    loadingType: LoadingType,
    loadMore: () -> Unit = {},
    refresh: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        color = Color.White
    ) {

        when {
            (loadingType is LoadingType.FirstPage && characters.isEmpty()) -> {
                PaginationLoading(
                    loadingType = loadingType
                )
            }

            loadingType is LoadingType.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Green
                        ),
                        onClick = { refresh() },
                    ) {
                        Text("Retry")
                    }
                }
            }

            else -> ChracterList(
                characters = characters,
                onItemClick = onItemClick,
                loadingType = loadingType,
                loadMore = loadMore,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChracterList(
    characters: List<Character>,
    onItemClick: (Int) -> Unit = {},
    loadingType: LoadingType,
    loadMore: () -> Unit = {},
    modifier: Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(190.dp),
        modifier = modifier
    ) {

        item(
            span = StaggeredGridItemSpan.FullLine
        ) {
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "Rick and Morty",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))
        }

        items(characters.size) {
            val character = characters[it]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        onItemClick(character.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                CharacterItem(
                    character = character,
                    onItemClick = onItemClick
                )
            }
        }

        item(
            span = StaggeredGridItemSpan.FullLine

        ) {
            PaginationLoading(
                loadingType = loadingType
            )

            Spacer(modifier = Modifier.size(16.dp))

            if (loadingType is LoadingType.FirstPage || loadingType is LoadingType.NextPage) {
                LaunchedEffect(Unit) {
                    loadMore()
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: Character,
    onItemClick: (Int) -> Unit = {}
) {
    Card(
        backgroundColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.padding(8.dp)
            .clickable {
                onItemClick(character.id)
            },
        elevation = 8.dp
    ) {
        Column(
            modifier = modifier.padding(12.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            ImageRequest(
                url = character.image,
                loading = Icons.TwoTone.Bolt,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White, CircleShape),
            )

            Text(
                text = character.name,
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = character.species, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val color = when (character.status) {
                    "Alive" -> Color.Green
                    "Dead" -> Color.Red
                    else -> Color.Gray
                }

                Text(text = "Status", fontSize = 12.sp)

                Spacer(modifier = Modifier.width(2.dp))

                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color = color, shape = CircleShape)
                )

                Text(text = character.status.lowercase(), fontSize = 12.sp)
            }
        }
    }
}