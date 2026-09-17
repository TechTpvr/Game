package com.pocketarcade.app

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.*
import android.content.Context
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class MainActivity : Activity() {

    private val navy = Color.rgb(24, 48, 78)
    private val green = Color.rgb(42, 181, 132)
    private val bg = Color.rgb(247, 248, 250)
    private val dark = Color.rgb(42, 48, 56)
    private lateinit var root: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        showHome()
    }

    private fun dp(v: Int): Int =
        (v * resources.displayMetrics.density).toInt()

    // =========================================================
    // HOME
    // =========================================================

    private fun showHome() {

        root = FrameLayout(this)
        root.setBackgroundColor(bg)

        val box = LinearLayout(this)
        box.orientation = LinearLayout.VERTICAL
        box.gravity = Gravity.CENTER_HORIZONTAL

        val rp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        rp.setMargins(dp(14), dp(18), dp(14), dp(8))
        root.addView(box, rp)

        // LOGO
        val logo = ArcadeLogoView(this)

        val lpLogo = LinearLayout.LayoutParams(
            dp(104),
            dp(104)
        )
        lpLogo.gravity = Gravity.CENTER_HORIZONTAL
        box.addView(logo, lpLogo)

        logo.alpha = 0f
        logo.scaleX = .55f
        logo.scaleY = .55f
        logo.rotation = -12f

        logo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .rotation(0f)
            .setDuration(850)
            .start()

        // WORDMARK
        val wordmark = ArcadeWordmarkView(this)

        val lpWord = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dp(54)
        )
        lpWord.topMargin = dp(2)
        box.addView(wordmark, lpWord)

        wordmark.alpha = 0f
        wordmark.translationY = dp(16).toFloat()

        wordmark.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(650)
            .setStartDelay(250)
            .start()

        // SUBTITLE
        val sub = TextView(this)
        sub.text = "سه بازی مینیمال، همیشه همراهت"
        sub.textSize = 16f
        sub.setTextColor(Color.rgb(80, 85, 92))
        sub.gravity = Gravity.CENTER
        sub.typeface = Typeface.DEFAULT

        box.addView(
            sub,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(38)
            )
        )

        sub.alpha = 0f

        sub.animate()
            .alpha(1f)
            .setDuration(600)
            .setStartDelay(450)
            .start()

        val gap = Space(this)

        box.addView(
            gap,
            LinearLayout.LayoutParams(
                1,
                dp(14)
            )
        )

        addButton(
            box,
            "SNAKE",
            "مار",
            "●",
            0
        ) {
            showSnake()
        }

        addButton(
            box,
            "BREAKOUT",
            "آجرشکن",
            "■",
            1
        ) {
            showBreakout()
        }

        addButton(
            box,
            "FLAPPY",
            "پرواز",
            "◆",
            2
        ) {
            showFlappy()
        }

        addButton(
            box,
            "TIC-TAC-TOE",
            "دوز",
            "×",
            3
        ) {
            showTicTacToe()
        }

        val bottom = Space(this)

        val bp = LinearLayout.LayoutParams(
            1,
            0
        )
        bp.weight = 1f
        box.addView(bottom, bp)

        val footer = TextView(this)
        footer.text = "✓  رکوردها روی همین دستگاه ذخیره می‌شوند"
        footer.textSize = 15f
        footer.setTextColor(Color.rgb(145, 149, 154))
        footer.gravity = Gravity.CENTER

        box.addView(
            footer,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(42)
            )
        )

        setContentView(root)
    }

    private fun addButton(
        parent: LinearLayout,
        title: String,
        subtitle: String,
        symbol: String,
        index: Int,
        click: () -> Unit
    ) {

        val button = LinearLayout(this)
        button.orientation = LinearLayout.HORIZONTAL
        button.gravity = Gravity.CENTER_VERTICAL
        button.setPadding(dp(14), 0, dp(12), 0)

        val drawable = GradientDrawable()
        drawable.setColor(navy)
        drawable.cornerRadius = dp(18).toFloat()
        button.background = drawable

        val icon = TextView(this)
        icon.text = symbol
        icon.textSize = 22f
        icon.setTextColor(Color.WHITE)
        icon.gravity = Gravity.CENTER

        button.addView(
            icon,
            LinearLayout.LayoutParams(
                dp(38),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )

        val texts = LinearLayout(this)
        texts.orientation = LinearLayout.VERTICAL
        texts.gravity = Gravity.CENTER

        val t1 = TextView(this)
        t1.text = title
        t1.textSize = 17f
        t1.setTextColor(Color.WHITE)
        t1.typeface = Typeface.DEFAULT_BOLD
        t1.gravity = Gravity.CENTER

        val t2 = TextView(this)
        t2.text = subtitle
        t2.textSize = 11f
        t2.setTextColor(Color.rgb(205, 216, 228))
        t2.gravity = Gravity.CENTER

        texts.addView(
            t1,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(25)
            )
        )

        texts.addView(
            t2,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(18)
            )
        )

        button.addView(
            texts,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
            )
        )

        val arrow = TextView(this)
        arrow.text = "›"
        arrow.textSize = 29f
        arrow.setTextColor(Color.WHITE)
        arrow.gravity = Gravity.CENTER

        button.addView(
            arrow,
            LinearLayout.LayoutParams(
                dp(30),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )

        val p = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dp(57)
        )

        p.topMargin =
            if (index == 0) dp(4) else dp(9)

        parent.addView(button, p)

        button.alpha = 0f
        button.translationY = dp(20).toFloat()

        button.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(450)
            .setStartDelay(
                (650 + index * 90).toLong()
            )
            .start()

        button.setOnTouchListener { v, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .scaleX(.97f)
                        .scaleY(.97f)
                        .setDuration(70)
                        .start()
                }

                MotionEvent.ACTION_UP -> {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()

                    v.performClick()
                }

                MotionEvent.ACTION_CANCEL -> {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
            }

            true
        }

        button.setOnClickListener {
            click()
        }
    }

    // =========================================================
    // GAME CONTAINER
    // =========================================================

    private fun gameContainer(
        title: String,
        game: View
    ): FrameLayout {

        val frame = FrameLayout(this)
        frame.setBackgroundColor(bg)

        val header = LinearLayout(this)
        header.orientation = LinearLayout.HORIZONTAL
        header.gravity = Gravity.CENTER_VERTICAL

        val back = TextView(this)
        back.text = "‹"
        back.textSize = 42f
        back.setTextColor(navy)
        back.gravity = Gravity.CENTER
        back.setOnClickListener {
            showHome()
        }

        header.addView(
            back,
            LinearLayout.LayoutParams(
                dp(60),
                dp(62)
            )
        )

        val titleView = TextView(this)
        titleView.text = title
        titleView.textSize = 24f
        titleView.setTextColor(navy)
        titleView.typeface = Typeface.DEFAULT_BOLD
        titleView.gravity = Gravity.CENTER

        header.addView(
            titleView,
            LinearLayout.LayoutParams(
                0,
                dp(62),
                1f
            )
        )

        header.addView(
            Space(this),
            LinearLayout.LayoutParams(
                dp(60),
                dp(62)
            )
        )

        frame.addView(
            header,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                dp(62)
            )
        )

        val gp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        gp.topMargin = dp(62)
        gp.leftMargin = dp(8)
        gp.rightMargin = dp(8)
        gp.bottomMargin = dp(8)

        frame.addView(game, gp)

        return frame
    }

    // =========================================================
    // SNAKE
    // =========================================================

    private fun showSnake() {

        val game = SnakeView(this)

        setContentView(
            gameContainer("SNAKE", game)
        )

        game.alpha = 0f
        game.scaleX = .96f
        game.scaleY = .96f

        game.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(350)
            .start()

        game.start()
    }

    private class SnakeView(
        context: Context
    ) : View(context) {

        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        private val cols = 18
        private val rows = 25

        private val snake =
            mutableListOf<Pair<Int, Int>>()

        private var food =
            Pair(10, 10)

        private var dx = 1
        private var dy = 0

        private var ndx = 1
        private var ndy = 0

        private var score = 0
        private var running = false
        private var over = false

        private var downX = 0f
        private var downY = 0f

        private val prefs =
            context.getSharedPreferences(
                "records",
                Context.MODE_PRIVATE
            )

        init {
            reset()
        }

        fun start() {

            if (running) return

            running = true

            post(object : Runnable {

                override fun run() {

                    if (!running || over) return

                    update()
                    invalidate()

                    postDelayed(this, 145)
                }
            })
        }

        private fun reset() {

            snake.clear()

            snake.add(Pair(8, 12))
            snake.add(Pair(7, 12))
            snake.add(Pair(6, 12))

            dx = 1
            dy = 0

            ndx = 1
            ndy = 0

            score = 0
            over = false

            food = randomFood()

            invalidate()
        }

        private fun randomFood(): Pair<Int, Int> {

            var p: Pair<Int, Int>

            do {
                p = Pair(
                    Random.nextInt(cols),
                    Random.nextInt(rows)
                )
            } while (snake.contains(p))

            return p
        }

        private fun update() {

            dx = ndx
            dy = ndy

            val head = snake.first()

            val newHead = Pair(
                head.first + dx,
                head.second + dy
            )

            if (
                newHead.first < 0 ||
                newHead.first >= cols ||
                newHead.second < 0 ||
                newHead.second >= rows ||
                snake.contains(newHead)
            ) {
                endGame()
                return
            }

            snake.add(0, newHead)

            if (newHead == food) {

                score++
                food = randomFood()

            } else {

                snake.removeAt(
                    snake.lastIndex
                )
            }
        }

        private fun endGame() {

            running = false
            over = true

            val old =
                prefs.getInt("snake_best", 0)

            if (score > old) {

                prefs.edit()
                    .putInt("snake_best", score)
                    .apply()
            }

            invalidate()
        }

        override fun onDraw(canvas: Canvas) {

            super.onDraw(canvas)

            val w = width.toFloat()
            val h = height.toFloat()

            val cell = min(
                (w - 20f) / cols,
                (h - 105f) / rows
            )

            val boardW = cell * cols
            val boardH = cell * rows

            val left =
                (w - boardW) / 2f

            val top = 62f

            paint.color = Color.rgb(
                65, 72, 80
            )

            paint.textSize = 17f
            paint.typeface =
                Typeface.DEFAULT_BOLD

            canvas.drawText(
                "SCORE  $score",
                left,
                30f,
                paint
            )

            val best =
                prefs.getInt("snake_best", 0)

            paint.color = Color.rgb(
                135, 140, 146
            )

            paint.textSize = 14f

            canvas.drawText(
                "BEST  $best",
                left + boardW - 72f,
                30f,
                paint
            )

            paint.color = Color.WHITE

            canvas.drawRoundRect(
                left - 5f,
                top - 5f,
                left + boardW + 5f,
                top + boardH + 5f,
                18f,
                18f,
                paint
            )

            // GRID
            paint.color = Color.rgb(
                238, 240, 243
            )
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f

            for (i in 0..cols) {

                val x =
                    left + i * cell

                canvas.drawLine(
                    x,
                    top,
                    x,
                    top + boardH,
                    paint
                )
            }

            for (i in 0..rows) {

                val y =
                    top + i * cell

                canvas.drawLine(
                    left,
                    y,
                    left + boardW,
                    y,
                    paint
                )
            }

            paint.style = Paint.Style.FILL

            // FOOD
            paint.color = green

            canvas.drawCircle(
                left +
                    food.first * cell +
                    cell / 2f,
                top +
                    food.second * cell +
                    cell / 2f,
                cell * .32f,
                paint
            )

            // SNAKE
            snake.forEachIndexed { index, p ->

                paint.color =
                    if (index == 0)
                        navy
                    else
                        Color.rgb(54, 82, 111)

                val x =
                    left + p.first * cell

                val y =
                    top + p.second * cell

                canvas.drawRoundRect(
                    x + 2f,
                    y + 2f,
                    x + cell - 2f,
                    y + cell - 2f,
                    cell * .22f,
                    cell * .22f,
                    paint
                )
            }

            if (over) {

                paint.color = Color.argb(
                    230,
                    247,
                    248,
                    250
                )

                canvas.drawRoundRect(
                    left,
                    top,
                    left + boardW,
                    top + boardH,
                    18f,
                    18f,
                    paint
                )

                paint.color = navy
                paint.textAlign =
                    Paint.Align.CENTER

                paint.textSize = 27f
                paint.typeface =
                    Typeface.DEFAULT_BOLD

                canvas.drawText(
                    "GAME OVER",
                    w / 2f,
                    top + boardH / 2f - 12f,
                    paint
                )

                paint.textSize = 16f
                paint.typeface =
                    Typeface.DEFAULT

                canvas.drawText(
                    "برای شروع دوباره لمس کن",
                    w / 2f,
                    top + boardH / 2f + 23f,
                    paint
                )

                paint.textAlign =
                    Paint.Align.LEFT
            }
        }

        override fun onTouchEvent(
            event: MotionEvent
        ): Boolean {

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    downX = event.x
                    downY = event.y

                    if (over) {

                        reset()
                        start()

                        return true
                    }

                    return true
                }

                MotionEvent.ACTION_UP -> {

                    val dxTouch =
                        event.x - downX

                    val dyTouch =
                        event.y - downY

                    if (
                        abs(dxTouch) < 25f &&
                        abs(dyTouch) < 25f
                    ) {
                        return true
                    }

                    if (
                        abs(dxTouch) >
                        abs(dyTouch)
                    ) {

                        if (
                            dxTouch > 0 &&
                            dx != -1
                        ) {
                            ndx = 1
                            ndy = 0
                        }

                        if (
                            dxTouch < 0 &&
                            dx != 1
                        ) {
                            ndx = -1
                            ndy = 0
                        }

                    } else {

                        if (
                            dyTouch > 0 &&
                            dy != -1
                        ) {
                            ndx = 0
                            ndy = 1
                        }

                        if (
                            dyTouch < 0 &&
                            dy != 1
                        ) {
                            ndx = 0
                            ndy = -1
                        }
                    }

                    return true
                }
            }

            return true
        }
    }

    // =========================================================
    // BREAKOUT
    // =========================================================

    private fun showBreakout() {

        val game = BreakoutView(this)

        setContentView(
            gameContainer("BREAKOUT", game)
        )

        game.start()
    }

    private class BreakoutView(
        context: Context
    ) : View(context) {

        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        private var paddleX = 0f

        private var ballX = 0f
        private var ballY = 0f

        private var vx = 5.5f
        private var vy = -7.5f

        private var score = 0
        private var over = false
        private var running = false

        private val bricks =
            ArrayList<RectF>()

        private val prefs =
            context.getSharedPreferences(
                "records",
                Context.MODE_PRIVATE
            )

        fun start() {

            post {

                reset()

                running = true

                post(object : Runnable {

                    override fun run() {

                        if (!running || over)
                            return

                        update()
                        invalidate()

                        postDelayed(this, 16)
                    }
                })
            }
        }

        private fun reset() {

            score = 0
            over = false

            paddleX = width / 2f

            ballX = width / 2f
            ballY = height * .70f

            vx = 5.5f
            vy = -7.5f

            bricks.clear()

            val gap = 7f
            val bw =
                (width - 40f - gap * 4f) / 5f

            for (r in 0 until 5) {

                for (c in 0 until 5) {

                    val x =
                        20f +
                            c * (bw + gap)

                    val y =
                        72f +
                            r * 34f

                    bricks.add(
                        RectF(
                            x,
                            y,
                            x + bw,
                            y + 26f
                        )
                    )
                }
            }
        }

        private fun update() {

            ballX += vx
            ballY += vy

            if (ballX < 9f) {
                ballX = 9f
                vx = abs(vx)
            }

            if (ballX > width - 9f) {
                ballX = width - 9f
                vx = -abs(vx)
            }

            if (ballY < 60f) {
                ballY = 60f
                vy = abs(vy)
            }

            val paddleY =
                height - 72f

            if (
                ballY + 10f >= paddleY &&
                ballY <= paddleY + 25f &&
                ballX >= paddleX - 58f &&
                ballX <= paddleX + 58f &&
                vy > 0
            ) {

                ballY = paddleY - 10f
                vy = -abs(vy)

                val hit =
                    (ballX - paddleX) / 58f

                vx =
                    (hit * 7f)
                        .coerceIn(-7f, 7f)
            }

            val iterator =
                bricks.iterator()

            while (iterator.hasNext()) {

                val b = iterator.next()

                if (
                    ballX >= b.left &&
                    ballX <= b.right &&
                    ballY >= b.top &&
                    ballY <= b.bottom
                ) {

                    iterator.remove()

                    vy *= -1f
                    score++

                    break
                }
            }

            if (bricks.isEmpty()) {
                finish()
            }

            if (ballY > height + 30f) {
                finish()
            }
        }

        private fun finish() {

            running = false
            over = true

            val old =
                prefs.getInt(
                    "breakout_best",
                    0
                )

            if (score > old) {

                prefs.edit()
                    .putInt(
                        "breakout_best",
                        score
                    )
                    .apply()
            }

            invalidate()
        }

        override fun onDraw(canvas: Canvas) {

            super.onDraw(canvas)

            paint.color = dark
            paint.textSize = 17f
            paint.typeface =
                Typeface.DEFAULT_BOLD

            canvas.drawText(
                "SCORE  $score",
                20f,
                32f,
                paint
            )

            bricks.forEachIndexed { i, b ->

                paint.color =
                    if (i % 2 == 0)
                        navy
                    else
                        Color.rgb(
                            52,
                            80,
                            109
                        )

                canvas.drawRoundRect(
                    b,
                    7f,
                    7f,
                    paint
                )
            }

            paint.color = navy

            canvas.drawRoundRect(
                paddleX - 58f,
                height - 72f,
                paddleX + 58f,
                height - 50f,
                10f,
                10f,
                paint
            )

            paint.color = green

            canvas.drawCircle(
                ballX,
                ballY,
                10f,
                paint
            )

            if (over) {

                paint.color = Color.argb(
                    230,
                    247,
                    248,
                    250
                )

                canvas.drawRect(
                    0f,
                    0f,
                    width.toFloat(),
                    height.toFloat(),
                    paint
                )

                paint.color = navy
                paint.textAlign =
                    Paint.Align.CENTER

                paint.textSize = 28f
                paint.typeface =
                    Typeface.DEFAULT_BOLD

                canvas.drawText(
                    if (bricks.isEmpty())
                        "YOU WIN"
                    else
                        "GAME OVER",
                    width / 2f,
                    height / 2f,
                    paint
                )

                paint.textSize = 16f
                paint.typeface =
                    Typeface.DEFAULT

                canvas.drawText(
                    "برای شروع دوباره لمس کن",
                    width / 2f,
                    height / 2f + 35f,
                    paint
                )

                paint.textAlign =
                    Paint.Align.LEFT
            }
        }

        override fun onTouchEvent(
            event: MotionEvent
        ): Boolean {

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    if (over) {

                        reset()
                        start()

                    } else {

                        paddleX =
                            event.x.coerceIn(
                                58f,
                                width - 58f
                            )
                    }

                    return true
                }

                MotionEvent.ACTION_MOVE -> {

                    if (!over) {

                        paddleX =
                            event.x.coerceIn(
                                58f,
                                width - 58f
                            )

                        invalidate()
                    }

                    return true
                }
            }

            return true
        }
    }

    // =========================================================
    // FLAPPY
    // =========================================================

    private fun showFlappy() {

        val game = FlappyView(this)

        setContentView(
            gameContainer("FLAPPY", game)
        )

        game.start()
    }

    private class FlappyView(
        context: Context
    ) : View(context) {

        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        private var birdX = 0f
        private var birdY = 0f
        private var velocity = 0f

        private var pipeX = 0f
        private var gapY = 0f

        private var score = 0

        private var over = false
        private var running = false

        private val prefs =
            context.getSharedPreferences(
                "records",
                Context.MODE_PRIVATE
            )

        fun start() {

            post {

                reset()

                running = true

                post(object : Runnable {

                    override fun run() {

                        if (!running || over)
                            return

                        update()
                        invalidate()

                        postDelayed(this, 16)
                    }
                })
            }
        }

        private fun reset() {

            birdX = width * .28f
            birdY = height * .43f

            velocity = 0f

            pipeX = width + 80f

            gapY =
                (height * .45f)
                    .coerceIn(
                        150f,
                        max(
                            151f,
                            height - 150f
                        )
                    )

            score = 0
            over = false
        }

        private fun flap() {
            velocity = -9.5f
        }

        private fun update() {

            velocity += .42f
            birdY += velocity

            pipeX -= 4.6f

            if (pipeX < -75f) {

                pipeX =
                    width + 40f

                val minGap =
                    140

                val maxGap =
                    max(
                        minGap + 1,
                        height - 170
                    )

                gapY =
                    Random.nextInt(
                        minGap,
                        maxGap
                    ).toFloat()

                score++
            }

            val radius = 17f

            if (
                birdY - radius < 62f ||
                birdY + radius > height
            ) {
                endGame()
                return
            }

            val gap = 145f
            val pipeWidth = 62f

            val hitX =
                birdX + radius > pipeX &&
                birdX - radius <
                pipeX + pipeWidth

            val hitY =
                birdY - radius <
                gapY - gap / 2f ||
                birdY + radius >
                gapY + gap / 2f

            if (hitX && hitY) {
                endGame()
            }
        }

        private fun endGame() {

            running = false
            over = true

            val old =
                prefs.getInt(
                    "flappy_best",
                    0
                )

            if (score > old) {

                prefs.edit()
                    .putInt(
                        "flappy_best",
                        score
                    )
                    .apply()
            }

            invalidate()
        }

        override fun onDraw(canvas: Canvas) {

            super.onDraw(canvas)

            paint.color =
                Color.rgb(225, 229, 234)

            canvas.drawRect(
                0f,
                56f,
                width.toFloat(),
                60f,
                paint
            )

            paint.color = dark
            paint.textAlign =
                Paint.Align.CENTER

            paint.textSize = 23f
            paint.typeface =
                Typeface.DEFAULT_BOLD

            canvas.drawText(
                "$score",
                width / 2f,
                40f,
                paint
            )

            val gap = 145f

            paint.color = navy

            canvas.drawRoundRect(
                pipeX,
                60f,
                pipeX + 62f,
                gapY - gap / 2f,
                9f,
                9f,
                paint
            )

            canvas.drawRoundRect(
                pipeX,
                gapY + gap / 2f,
                pipeX + 62f,
                height.toFloat(),
                9f,
                9f,
                paint
            )

            // BIRD
            paint.color = green

            canvas.drawCircle(
                birdX,
                birdY,
                17f,
                paint
            )

            paint.color = Color.WHITE

            canvas.drawCircle(
                birdX + 6f,
                birdY - 5f,
                4f,
                paint
            )

            paint.color =
                Color.rgb(30, 35, 40)

            canvas.drawCircle(
                birdX + 7f,
                birdY - 5f,
                2f,
                paint
            )

            if (over) {

                paint.color = Color.argb(
                    230,
                    247,
                    248,
                    250
                )

                canvas.drawRect(
                    0f,
                    60f,
                    width.toFloat(),
                    height.toFloat(),
                    paint
                )

                paint.color = navy
                paint.textSize = 28f
                paint.typeface =
                    Typeface.DEFAULT_BOLD

                canvas.drawText(
                    "GAME OVER",
                    width / 2f,
                    height / 2f,
                    paint
                )

                paint.textSize = 16f
                paint.typeface =
                    Typeface.DEFAULT

                canvas.drawText(
                    "برای شروع دوباره لمس کن",
                    width / 2f,
                    height / 2f + 35f,
                    paint
                )
            }

            paint.textAlign =
                Paint.Align.LEFT
        }

        override fun onTouchEvent(
            event: MotionEvent
        ): Boolean {

            if (
                event.action ==
                MotionEvent.ACTION_DOWN
            ) {

                if (over) {

                    reset()
                    start()

                } else {

                    flap()
                }

                return true
            }

            return true
        }
    }

    // =========================================================
    // TIC TAC TOE
    // =========================================================

    private fun showTicTacToe() {

        val game = TicTacToeView(this)

        setContentView(
            gameContainer(
                "TIC-TAC-TOE",
                game
            )
        )

        game.alpha = 0f

        game.animate()
            .alpha(1f)
            .setDuration(350)
            .start()
    }

    private class TicTacToeView(
        context: Context
    ) : View(context) {

        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        private val board =
            Array(3) {
                Array(3) { "" }
            }

        private var player = "X"
        private var finished = false
        private var result = ""

        override fun onDraw(canvas: Canvas) {

            super.onDraw(canvas)

            val size = min(
                width * .84f,
                height * .62f
            )

            val left =
                (width - size) / 2f

            val top = 105f
            val cell = size / 3f

            paint.color = navy
            paint.style =
                Paint.Style.STROKE
            paint.strokeWidth = 5f
            paint.strokeCap =
                Paint.Cap.ROUND

            for (i in 1..2) {

                canvas.drawLine(
                    left + cell * i,
                    top + 8f,
                    left + cell * i,
                    top + size - 8f,
                    paint
                )

                canvas.drawLine(
                    left + 8f,
                    top + cell * i,
                    left + size - 8f,
                    top + cell * i,
                    paint
                )
            }

            paint.style =
                Paint.Style.FILL

            for (r in 0..2) {

                for (c in 0..2) {

                    val v =
                        board[r][c]

                    if (v.isNotEmpty()) {

                        paint.color =
                            if (v == "X")
                                navy
                            else
                                green

                        paint.textAlign =
                            Paint.Align.CENTER

                        paint.textSize =
                            cell * .55f

                        paint.typeface =
                            Typeface.DEFAULT_BOLD

                        canvas.drawText(
                            v,
                            left +
                                c * cell +
                                cell / 2f,
                            top +
                                r * cell +
                                cell * .68f,
                            paint
                        )
                    }
                }
            }

            paint.color =
                Color.rgb(70, 77, 85)

            paint.textAlign =
                Paint.Align.CENTER

            paint.textSize = 18f
            paint.typeface =
                Typeface.DEFAULT_BOLD

            canvas.drawText(
                if (finished)
                    result
                else
                    "نوبت بازیکن $player",
                width / 2f,
                65f,
                paint
            )

            if (finished) {

                paint.color =
                    Color.rgb(130, 135, 141)

                paint.textSize = 15f
                paint.typeface =
                    Typeface.DEFAULT

                canvas.drawText(
                    "برای شروع دوباره لمس کن",
                    width / 2f,
                    top + size + 48f,
                    paint
                )
            }

            paint.textAlign =
                Paint.Align.LEFT
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

            if (finished) {

                clearBoard()

                invalidate()

                return true
            }

            val size = min(
                width * .84f,
                height * .62f
            )

            val left =
                (width - size) / 2f

            val top = 105f
            val cell = size / 3f

            val col =
                ((event.x - left) / cell)
                    .toInt()

            val row =
                ((event.y - top) / cell)
                    .toInt()

            if (
                row !in 0..2 ||
                col !in 0..2
            ) {
                return true
            }

            if (
                board[row][col].isNotEmpty()
            ) {
                return true
            }

            board[row][col] = player

            if (win(player)) {

                finished = true
                result =
                    "بازیکن $player برنده شد"

            } else if (full()) {

                finished = true
                result = "مساوی شد"

            } else {

                player =
                    if (player == "X")
                        "O"
                    else
                        "X"
            }

            invalidate()

            return true
        }

        private fun clearBoard() {

            for (r in 0..2) {
                for (c in 0..2) {
                    board[r][c] = ""
                }
            }

            player = "X"
            finished = false
            result = ""
        }

        private fun full(): Boolean {

            for (r in 0..2) {
                for (c in 0..2) {

                    if (
                        board[r][c].isEmpty()
                    ) {
                        return false
                    }
                }
            }

            return true
        }

        private fun win(
            p: String
        ): Boolean {

            for (i in 0..2) {

                if (
                    board[i][0] == p &&
                    board[i][1] == p &&
                    board[i][2] == p
                ) {
                    return true
                }

                if (
                    board[0][i] == p &&
                    board[1][i] == p &&
                    board[2][i] == p
                ) {
                    return true
                }
            }

            if (
                board[0][0] == p &&
                board[1][1] == p &&
                board[2][2] == p
            ) {
                return true
            }

            if (
                board[0][2] == p &&
                board[1][1] == p &&
                board[2][0] == p
            ) {
                return true
            }

            return false
        }
    }

    // =========================================================
    // CUSTOM LOGO
    // =========================================================

    private class ArcadeLogoView(
        context: Context
    ) : View(context) {

        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        override fun onDraw(
            canvas: Canvas
        ) {

            super.onDraw(canvas)

            val cx = width / 2f
            val cy = height / 2f

            val r =
                min(width, height) * .43f

            // Outer geometric circle
            paint.color =
                Color.rgb(24, 48, 78)

            canvas.drawCircle(
                cx,
                cy,
                r,
                paint
            )

            // Inner display
            paint.color =
                Color.rgb(247, 248, 250)

            canvas.drawRoundRect(
                cx - r * .59f,
                cy - r * .44f,
                cx + r * .59f,
                cy + r * .45f,
                r * .24f,
                r * .24f,
                paint
            )

            // Eyes
            paint.color =
                Color.rgb(24, 48, 78)

            canvas.drawCircle(
                cx - r * .23f,
                cy - r * .10f,
                r * .075f,
                paint
            )

            canvas.drawCircle(
                cx + r * .23f,
                cy - r * .10f,
                r * .075f,
                paint
            )

            // Smile
            paint.style =
                Paint.Style.STROKE

            paint.strokeWidth =
                r * .07f

            paint.strokeCap =
                Paint.Cap.ROUND

            val smile = RectF(
                cx - r * .34f,
                cy - r * .03f,
                cx + r * .34f,
                cy + r * .39f
            )

            canvas.drawArc(
                smile,
                15f,
                150f,
                false,
                paint
            )

            paint.style =
                Paint.Style.FILL
        }
    }

    // =========================================================
    // CUSTOM WORDMARK
    // =========================================================

    private class ArcadeWordmarkView(
        context: Context
    ) : View(context) {

        private val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        override fun onDraw(
            canvas: Canvas
        ) {

            super.onDraw(canvas)

            paint.color =
                Color.rgb(24, 48, 78)

            paint.textAlign =
                Paint.Align.CENTER

            paint.typeface =
                Typeface.create(
                    Typeface.DEFAULT,
                    Typeface.BOLD
                )

            paint.textSize =
                min(width, height) * .40f

            canvas.drawText(
                "POCKET ARCADE",
                width / 2f,
                height * .65f,
                paint
            )

            paint.textAlign =
                Paint.Align.LEFT
        }
    }
}
