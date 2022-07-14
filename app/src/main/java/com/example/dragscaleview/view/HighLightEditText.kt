class HighLightEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {

    private val highLightPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            style = Paint.Style.FILL
        }
    }

    var maxLength = Int.MAX_VALUE

    private val highLightPath by lazy { Path() }

    //设置最大输入数
    fun setMaxCount(size: Int){
        maxLength = size
    }

    private fun getUpdatedHighlightPath(): Path? {
        var highlight: Path? = null
        val selStart = maxLength
        val selEnd = text?.length ?: 0
        if (selEnd <= selStart) return null
        if (movementMethod != null && (isFocused || isPressed) && selStart >= 0) {
            highLightPath.reset()
            layout.getSelectionPath(selStart, selEnd, highLightPath)
            highlight = highLightPath
        }
        return highlight
    }

    override fun onDraw(canvas: Canvas?) {
        val path = getUpdatedHighlightPath()
        if (path == null) {
            super.onDraw(canvas)
            return
        }
        canvas?.save()
        val vspace = bottom - top - compoundPaddingBottom - compoundPaddingTop
        val maxScrollY = layout.height - vspace
        val clipLeft = compoundPaddingLeft + scrollX
        val clipTop = if (scrollY == 0) 0 else extendedPaddingTop + scrollY
        val clipRight = right - left - compoundPaddingRight + scrollX
        val clipBottom = bottom - top + scrollY -
                (if (scrollY == maxScrollY) 0 else extendedPaddingBottom)
        canvas?.clipRect(clipLeft, clipTop, clipRight, clipBottom)
        canvas?.translate(compoundPaddingLeft.toFloat(), compoundPaddingTop.toFloat())
        canvas?.drawPath(path, highLightPaint)
        canvas?.restore()
        super.onDraw(canvas)
    }


}
