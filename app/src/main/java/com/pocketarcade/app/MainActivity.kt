package com.pocketarcade.app

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.content.Context
import android.widget.FrameLayout
import android.widget.TextView
import kotlin.math.abs
import kotlin.math.min
import kotlin.random.Random

class MainActivity : Activity() {

    private lateinit var root: FrameLayout
    private lateinit var prefs: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = getSharedPreferences("records", MODE_PRIVATE)
        showHome()
    }

    private fun base(): FrameLayout {
        root = FrameLayout(this)
        root.setBackgroundColor(Color.rgb(246, 247, 249))
        return root
    }

    private fun txt(
        text: String,
        size: Float,
        color: Int = Color.rgb(23, 42, 70),
        bold: Boolean = false
    ): TextView {

        return TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER

            if (bold) {
                typeface = Typeface.create("sans", Typeface.BOLD)
            }
        }
    }

    private fun button(
        label: String,
        action: () -> Unit
    ): TextView {

        val b = txt(
            label,
            16f,
            Color.WHITE,
            true
        )

        b.setPadding(24, 18, 24, 18)

        b.background = GradientDrawable().apply {
            cornerRadius = 28f
            setColor(Color.rgb(23, 42, 70))
        }

        b.setOnClickListener {
            action()
        }

        return b
    }

    private fun add(
        view: View,
        width: Int,
        height: Int,
        gravity: Int,
        topMargin: Int = 0
    ) {

        val lp = FrameLayout.LayoutParams(
            width,
            height,
            gravity
        )

        lp.setMargins(
            20,
            topMargin,
            20,
            20
        )

        root.addView(view, lp)
    }

    private fun showHome() {

        root = base()
        setContentView(root)

        val logo = LogoView(this)

        add(
            logo,
            180,
            180,
            Gravity.TOP or Gravity.CENTER_HORIZONTAL,
            48
        )

        val title = txt(
            "POCKET ARCADE",
            30f,
            Color.rgb(23, 42, 70),
            true
        )

        add(
            title,
            -1,
            70,
            Gravity.TOP or Gravity.CENTER_HORIZONTAL,
            230
        )

        val sub = txt(
            "سه بازی مینیمال، همیشه همراهت",
            15f,
            Color.DKGRAY
        )

        add(
            sub,
            -1,
            50,
            Gravity.TOP or Gravity.CENTER_HORIZONTAL,
            292
        )

        val snake = button("🐍  مار  •  Snake") {
            openGame("snake")
        }

        add(
            snake,
            -1,
            70,
            Gravity.TOP,
            365
        )

        val breakout = button("◼  بریک‌اوت  •  Breakout") {
            openGame("breakout")
        }

        add(
            breakout,
            -1,
            70,
            Gravity.TOP,
            450
        )

        val flappy = button("●  فلپی  •  Flappy") {
            openGame("flappy")
        }

        add(
            flappy,
            -1,
            70,
            Gravity.TOP,
            535
        )

        val ttt = button("✕  دوز  •  Tic-Tac-Toe") {
            openGame("ttt")
        }

        add(
            ttt,
            -1,
            70,
            Gravity.TOP,
            620
        )

        val records = txt(
            "رکوردها روی همین دستگاه ذخیره می‌شوند ✓",
            13f,
            Color.GRAY
        )

        add(
            records,
            -1,
            45,
            Gravity.BOTTOM
        )
    }

    private fun openGame(type: String) {

        if (type == "ttt") {
            showTTT()
            return
        }

        root = base()
        setContentView(root)

        val back = txt(
            "‹",
            42f,
            Color.rgb(23, 42, 70),
            true
        )

        back.setOnClickListener {
            showHome()
        }

        add(
            back,
            70,
            70,
            Gravity.TOP or Gravity.START,
            24
        )

        val titleText = when (type) {
            "snake" -> "SNAKE"
            "breakout" -> "BREAKOUT"
            else -> "FLAPPY"
        }

        val title = txt(
            titleText,
            22f,
            Color.rgb(23, 42, 70),
            true
        )

        add(
            title,
            -1,
            70,
            Gravity.TOP or Gravity.CENTER_HORIZONTAL,
            24
        )

        val board = GameView(
            this,
            type,
            prefs
        ) {
            showHome()
        }

        val lp = FrameLayout.LayoutParams(
            -1,
            0
        )

        lp.gravity = Gravity.TOP
        lp.topMargin = 100

        root.addView(
            board,
            lp
        )
    }

    private fun showTTT() {

        root = base()
        setContentView(root)

        val back = txt(
            "‹",
            42f,
            Color.rgb(23, 42, 70),
            true
        )

        back.setOnClickListener {
            showHome()
        }

        add(
            back,
            70,
            70,
            Gravity.TOP or Gravity.START,
            24
        )

        val title = txt(
            "TIC-TAC-TOE",
            22f,
            Color.rgb(23, 42, 70),
            true
        )

        add(
            title,
            -1,
            70,
            Gravity.TOP,
            24
        )

        val board = TTTView(
            this,
            prefs
        )

        val lp = FrameLayout.LayoutParams(
            -1,
            520
        )

        lp.gravity = Gravity.TOP
        lp.topMargin = 115

        lp.setMargins(
            25,
            115,
            25,
            0
        )

        root.addView(
            board,
            lp
        )
    }
}

class LogoView(context: Context) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        val cx = width / 2f
        val cy = height / 2f

        paint.color = Color.rgb(23, 42, 70)
        paint.style = Paint.Style.FILL

        canvas.drawCircle(
            cx,
            cy,
            72f,
            paint
        )

        paint.color = Color.WHITE

        canvas.drawRoundRect(
            cx - 42f,
            cy - 42f,
            cx + 42f,
            cy + 42f,
            18f,
            18f,
            paint
        )

        paint.color = Color.rgb(23, 42, 70)

        canvas.drawCircle(
            cx - 16f,
            cy - 10f,
            8f,
            paint
        )

        canvas.drawCircle(
            cx + 16f,
            cy - 10f,
            8f,
            paint
        )

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f

        canvas.drawArc(
            cx - 30f,
            cy - 4f,
            cx + 30f,
            cy + 42f,
            20f,
            140f,
            false,
            paint
        )
    }
}

class GameView(
    context: Context,
    private val type: String,
    private val prefs: android.content.SharedPreferences,
    private val home: () -> Unit
) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val navy = Color.rgb(23, 42, 70)

    private var score = 0

    private var best =
        prefs.getInt("best_$type", 0)

    private var running = false

    private var over = false

    private var last =
        System.currentTimeMillis()

    // Snake

    private val snake =
        ArrayList<Point>()

    private var dir =
        Point(1, 0)

    private var food =
        Point(10, 10)

    // Flappy

    private var birdY = 0f
    private var vel = 0f
    private var pipeX = 0f
    private var gapY = 0f

    // Breakout

    private var bx = 0f
    private var by = 0f
    private var bvx = 5f
    private var bvy = -7f
    private var paddleX = 0f

    private val bricks =
        ArrayList<RectF>()

    private var downX = 0f
    private var downY = 0f

    init {

        isFocusable = true

        if (type == "snake") {
            resetSnake()
        }

        if (type == "flappy") {
            resetFlappy()
        }

        if (type == "breakout") {
            post {
                resetBreakout()
            }
        }
    }

    private fun resetSnake() {

        snake.clear()

        snake.add(Point(8, 12))
        snake.add(Point(7, 12))
        snake.add(Point(6, 12))

        dir = Point(1, 0)

        food = Point(14, 8)
    }

    private fun resetFlappy() {

        birdY =
            if (height > 0) {
                height / 2f
            } else {
                500f
            }

        vel = 0f

        pipeX =
            if (width > 0) {
                width.toFloat()
            } else {
                1000f
            }

        gapY =
            if (height > 0) {
                height / 2f
            } else {
                500f
            }
    }

    private fun resetBreakout() {

        bricks.clear()

        if (width <= 0) return

        val cols = 6
        val cellWidth =
            width.toFloat() / cols.toFloat()

        for (r in 0..4) {

            for (col in 0 until cols) {

                bricks.add(
                    RectF(
                        col * cellWidth + 8f,
                        r * 34f + 30f,
                        col * cellWidth + cellWidth - 8f,
                        r * 34f + 58f
                    )
                )
            }
        }

        bx = width / 2f
        by = height - 180f
        paddleX = width / 2f
    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        canvas.drawColor(
            Color.rgb(246, 247, 249)
        )

        paint.color = navy
        paint.style = Paint.Style.FILL
        paint.textSize = 18f

        canvas.drawText(
            "Score  $score",
            25f,
            35f,
            paint
        )

        canvas.drawText(
            "Best  $best",
            width - 120f,
            35f,
            paint
        )

        when (type) {

            "snake" ->
                drawSnake(canvas)

            "flappy" ->
                drawFlappy(canvas)

            "breakout" ->
                drawBreakout(canvas)
        }

        if (!running) {

            paint.color = navy
            paint.textSize = 24f

            val message =
                if (over) {
                    "GAME OVER"
                } else {
                    "TAP TO START"
                }

            canvas.drawText(
                message,
                width / 2f - 75f,
                height - 90f,
                paint
            )

            paint.textSize = 14f

            canvas.drawText(
                "لمس کن / سوایپ کن",
                width / 2f - 55f,
                height - 55f,
                paint
            )
        }
    }

    private fun drawSnake(canvas: Canvas) {

        val cols = 18
        val rows = 28

        val availableHeight =
            (height - 70).coerceAtLeast(1)

        val cell =
            min(
                width / cols.toFloat(),
                availableHeight / rows.toFloat()
            )

        val ox =
            (width - cols * cell) / 2f

        val oy = 55f

        paint.color =
            Color.rgb(229, 232, 236)

        for (i in 1 until cols) {

            canvas.drawLine(
                ox + i * cell,
                oy,
                ox + i * cell,
                oy + rows * cell,
                paint
            )
        }

        for (i in 1 until rows) {

            canvas.drawLine(
                ox,
                oy + i * cell,
                ox + cols * cell,
                oy + i * cell,
                paint
            )
        }

        paint.color = navy

        snake.forEach { point ->

            canvas.drawRoundRect(
                ox + point.x * cell + 2f,
                oy + point.y * cell + 2f,
                ox + (point.x + 1) * cell - 2f,
                oy + (point.y + 1) * cell - 2f,
                7f,
                7f,
                paint
            )
        }

        paint.color =
            Color.rgb(65, 92, 125)

        canvas.drawCircle(
            ox + (food.x + 0.5f) * cell,
            oy + (food.y + 0.5f) * cell,
            cell * 0.28f,
            paint
        )
    }

    private fun drawFlappy(canvas: Canvas) {

        paint.color = navy

        canvas.drawCircle(
            width * 0.3f,
            birdY,
            22f,
            paint
        )

        paint.color =
            Color.rgb(65, 92, 125)

        canvas.drawRect(
            pipeX,
            0f,
            pipeX + 58f,
            gapY - 85f,
            paint
        )

        canvas.drawRect(
            pipeX,
            gapY + 85f,
            pipeX + 58f,
            height.toFloat(),
            paint
        )

        paint.color = navy
        paint.textSize = 13f

        canvas.drawText(
            "برای پرواز ضربه بزن",
            width / 2f - 75f,
            height - 50f,
            paint
        )
    }

    private fun drawBreakout(canvas: Canvas) {

        paint.color =
            Color.rgb(65, 92, 125)

        bricks.forEach {
            canvas.drawRoundRect(
                it,
                8f,
                8f,
                paint
            )
        }

        paint.color = navy

        canvas.drawCircle(
            bx,
            by,
            10f,
            paint
        )

        canvas.drawRoundRect(
            paddleX - 55f,
            height - 55f,
            paddleX + 55f,
            height - 35f,
            12f,
            12f,
            paint
        )

        paint.textSize = 13f

        canvas.drawText(
            "پد را با انگشت جابه‌جا کن",
            width / 2f - 70f,
            height - 10f,
            paint
        )
    }

    private fun gameOver() {

        running = false
        over = true

        if (score > best) {

            best = score

            prefs.edit()
                .putInt("best_$type", best)
                .apply()
        }

        invalidate()
    }

    private fun loop() {

        if (!running) return

        val now =
            System.currentTimeMillis()

        val interval =
            if (type == "snake") {
                120L
            } else {
                16L
            }

        if (now - last > interval) {

            last = now

            update()

            invalidate()
        }

        postDelayed(
            {
                loop()
            },
            16L
        )
    }

    private fun update() {

        when (type) {

            "snake" -> updateSnake()

            "flappy" -> updateFlappy()

            "breakout" -> updateBreakout()
        }
    }

    private fun updateSnake() {

        val head = snake[0]

        val next =
            Point(
                head.x + dir.x,
                head.y + dir.y
            )

        if (
            next.x < 0 ||
            next.y < 0 ||
            next.x >= 18 ||
            next.y >= 28 ||
            snake.contains(next)
        ) {

            gameOver()
            return
        }

        snake.add(
            0,
            next
        )

        if (next == food) {

            score++

            var newFood: Point

            do {

                newFood =
                    Point(
                        Random.nextInt(18),
                        Random.nextInt(28)
                    )

            } while (snake.contains(newFood))

            food = newFood

        } else {

            snake.removeAt(
                snake.size - 1
            )
        }
    }

    private fun updateFlappy() {

        vel += 0.42f
        birdY += vel

        pipeX -= 5f

        if (pipeX < -70f) {

            pipeX =
                width.toFloat()

            gapY =
                100f +
                    Random.nextFloat() *
                    (height - 250f).coerceAtLeast(100f)

            score++
        }

        val birdX =
            width * 0.3f

        val collision =
            birdX + 22f > pipeX &&
            birdX - 22f < pipeX + 58f &&
            (
                birdY < gapY - 85f ||
                birdY > gapY + 85f
            )

        if (
            birdY < 0f ||
            birdY > height ||
            collision
        ) {

            gameOver()
        }
    }

    private fun updateBreakout() {

        bx += bvx
        by += bvy

        if (
            bx < 10f ||
            bx > width - 10f
        ) {

            bvx = -bvx
        }

        if (by < 10f) {

            bvy = -bvy
        }

        if (
            by > height - 80f &&
            bx >= paddleX - 70f &&
            bx <= paddleX + 70f
        ) {

            bvy =
                -abs(bvy)
        }

        val hit =
            bricks.indexOfFirst {
                it.contains(
                    bx,
                    by
                )
            }

        if (hit >= 0) {

            bricks.removeAt(hit)

            bvy = -bvy

            score++

            if (bricks.isEmpty()) {

                gameOver()
                return
            }
        }

        if (by > height) {

            gameOver()
        }
    }

    override fun onTouchEvent(
        event: MotionEvent
    ): Boolean {

        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {

                downX = event.x
                downY = event.y

                if (!running) {

                    if (over) {

                        score = 0
                        over = false

                        when (type) {

                            "snake" ->
                                resetSnake()

                            "flappy" ->
                                resetFlappy()

                            "breakout" ->
                                resetBreakout()
                        }
                    }

                    running = true

                    last =
                        System.currentTimeMillis()

                    loop()

                    if (type == "flappy") {
                        vel = -8f
                    }
                }

                return true
            }

            MotionEvent.ACTION_MOVE -> {

                if (type == "breakout") {

                    paddleX =
                        event.x.coerceIn(
                            55f,
                            width - 55f
                        )
                }

                return true
            }

            MotionEvent.ACTION_UP -> {

                if (type == "snake") {

                    val dx =
                        event.x - downX

                    val dy =
                        event.y - downY

                    if (
                        abs(dx) >
                        abs(dy)
                    ) {

                        if (dx > 0) {
                            dir = Point(1, 0)
                        } else {
                            dir = Point(-1, 0)
                        }

                    } else {

                        if (dy > 0) {
                            dir = Point(0, 1)
                        } else {
                            dir = Point(0, -1)
                        }
                    }

                }

                return true
            }
        }

        return true
    }
}

class TTTView(
    context: Context,
    private val prefs: android.content.SharedPreferences
) : View(context) {

    private val paint =
        Paint(Paint.ANTI_ALIAS_FLAG)

    private val board =
        IntArray(9)

    private var turn = 1

    private var done = false

    private val navy =
        Color.rgb(23, 42, 70)

    override fun onDraw(canvas: Canvas) {

        canvas.drawColor(
            Color.rgb(246, 247, 249)
        )

        paint.color = navy
        paint.strokeWidth = 5f

        val cell =
            width / 3f

        canvas.drawLine(
            cell,
            30f,
            cell,
            height - 30f,
            paint
        )

        canvas.drawLine(
            cell * 2f,
            30f,
            cell * 2f,
            height - 30f,
            paint
        )

        canvas.drawLine(
            30f,
            cell,
            width - 30f,
            cell,
            paint
        )

        canvas.drawLine(
            30f,
            cell * 2f,
            width - 30f,
            cell * 2f,
            paint
        )

        paint.textSize = 70f
        paint.textAlign = Paint.Align.CENTER

        for (i in 0..8) {

            if (board[i] != 0) {

                val symbol =
                    if (board[i] == 1) {
                        "X"
                    } else {
                        "O"
                    }

                canvas.drawText(
                    symbol,
                    (i % 3 + 0.5f) * cell,
                    (i / 3 + 0.72f) * cell,
                    paint
                )
            }
        }

        paint.textSize = 18f

        canvas.drawText(
            if (done) {
                "دوباره لمس کن"
            } else {
                "نوبت: ${
                    if (turn == 1) "X"
                    else "O"
                }"
            },
            width / 2f,
            height - 8f,
            paint
        )
    }

    override fun onTouchEvent(
        event: MotionEvent
    ): Boolean {

        if (
            event.action !=
            MotionEvent.ACTION_DOWN
        ) {
            return true
        }

        if (done) {

            java.util.Arrays.fill(
                board,
                0
            )

            turn = 1
            done = false

            invalidate()

            return true
        }

        val cell =
            width / 3f

        val col =
            (event.x / cell)
                .toInt()
                .coerceIn(0, 2)

        val row =
            (event.y / cell)
                .toInt()
                .coerceIn(0, 2)

        val index =
            row * 3 + col

        if (board[index] != 0) {
            return true
        }

        board[index] = turn

        if (
            win(turn) ||
            board.all { it != 0 }
        ) {

            done = true

        } else {

            turn = 3 - turn
        }

        invalidate()

        return true
    }

    private fun win(player: Int): Boolean {

        val lines =
            arrayOf(
                intArrayOf(0, 1, 2),
                intArrayOf(3, 4, 5),
                intArrayOf(6, 7, 8),
                intArrayOf(0, 3, 6),
                intArrayOf(1, 4, 7),
                intArrayOf(2, 5, 8),
                intArrayOf(0, 4, 8),
                intArrayOf(2, 4, 6)
            )

        return lines.any {
            board[it[0]] == player &&
            board[it[1]] == player &&
            board[it[2]] == player
        }
    }
}
