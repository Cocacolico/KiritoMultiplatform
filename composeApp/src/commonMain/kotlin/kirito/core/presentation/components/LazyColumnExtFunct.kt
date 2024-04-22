package kirito.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


/***
 * Se usa de la siguiente manera:
 * LazyColumn {
 *     item {
 *         Text(text = "My LazyColumn Title")
 *     }
 *     // with count
 *     gridItems(10, nColumns = 4) { index ->
 *         Box(
 *             modifier = Modifier
 *                 .size(50.dp)
 *                 .padding(5.dp)
 *                 .background(Color.Gray)
 *         )
 *     }
 *     // or with list of items
 *     gridItems(listOf(1,2,3), nColumns = 4) { item ->
 *         Box(
 *             modifier = Modifier
 *                 .size(50.dp)
 *                 .padding(5.dp)
 *                 .background(Color.Gray)
 *         )
 *     }
 * }
 * */
fun LazyListScope.gridItems(
    count: Int,
    nColumns: Int,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(Int) -> Unit,
) {
    gridItems(
        data = List(count) { it },
        nColumns = nColumns,
        horizontalArrangement = horizontalArrangement,
        itemContent = itemContent,
    )
}

fun <T> LazyListScope.gridItems(
    data: List<T>,
    nColumns: Int,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val rows = if (data.isEmpty()) 0 else 1 + (data.count() - 1) / nColumns
    items(rows) { rowIndex ->
        Row(horizontalArrangement = horizontalArrangement) {
            for (columnIndex in 0 until nColumns) {
                val itemIndex = rowIndex * nColumns + columnIndex
                if (itemIndex < data.count()) {
                    val item = data[itemIndex]
                    androidx.compose.runtime.key(key?.invoke(item)) {
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            itemContent.invoke(this, item)
                        }
                    }
                } else {
                    Spacer(Modifier.weight(1f, fill = true))
                }
            }
        }
    }
}