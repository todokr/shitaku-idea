import com.intellij.util.containers.headTail

val selectionActionId = "FormatDSL"

ide.registerAction(selectionActionId, "Format CSV as Table") { e ->
    val editor = e.getRequiredData(CommonDataKeys.EDITOR)

    val project  = e.getRequiredData(CommonDataKeys.PROJECT)
    val primaryCaret = editor.caretModel.primaryCaret
    val start = primaryCaret.selectionStart
    val end = primaryCaret.selectionEnd

    val document = editor.document
    val range = TextRange(start, end)
    WriteCommandAction.runWriteCommandAction(project) {
        val formatted = formatDSL(document.getText(range))
        document.replaceString(start, end, formatted)
    }
}

"""
 Col1                             | Col2                             | Col3                             
----------------------------------|----------------------------------|----------------------------------
 Value1                           | Value2                           | Value3                           
 Separate                         | cols                             | with a colon in DSL              
 This is a row with only one cell |                                  |                                  
                                  | This is a row with only 2nd cell |                                  
                                  |                                  | This is a row with only 3nd cell 
"""

private val delimiter: String = "|"

/**
 *               Col1               |  Col23  |          Col3          | Numeric Column
 *----------------------------------|---------|------------------------|----------------
 * Value 1                          | Value 2 | 123                    |           10.0
 * Separate                         | cols    | with a tab or 4 spaces |       -2,027.1
 * This is a row with only one cell |         |                        |
 */
fun formatDSL(raw: String): String {

    val matrix = raw.lines().map { line ->
        line.split(",").map { it.trim() }
    }
    val transposed = List(matrix.first().size) { i ->
        matrix.map { it[i] }
    }
    val colWidths = transposed.map { col -> col.maxOf { it.length } }
    val aligned = matrix.map { cols ->
        cols.zip(colWidths).joinToString(delimiter) { (col, max) ->
            val wsSize = max - col.length
            " ${col}${" ".repeat(wsSize)} "
        }
    }
    val (header, body) = aligned.headTail()
    val headerLine = colWidths.joinToString("|") { "-".repeat(it  + 2) }
    return (listOf(header, headerLine) + body).joinToString("\n")
}

fun <T> Sequence<T>.cycle(): Sequence<T> =
    generateSequence(this){ this }.flatten()
