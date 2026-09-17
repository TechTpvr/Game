package com.pocketarcade.app

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.*
import android.content.Context
import android.content.res.ColorStateList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class MainActivity : Activity() {

    private lateinit var root: FrameLayout

    private val navy = Color.rgb(25, 49, 79)
    private val background = Color.rgb(247, 248, 250)
    private val textDark = Color.rgb(30, 39, 50)
    private val gray = Color.rgb(120, 128, 138)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = background
        window.navigationBarColor = background
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        showHome()
    }

    // ---------------------------------------------------------
    // HOME
    // ---------------------------------------------------------

    private fun showHome() {

        root = FrameLayout(this)
        root.setBackgroundColor(background)

        val content = LinearLayout(this)
        content.orientation = LinearLayout.VERTICAL
        content.gravity = Gravity.CENTER_HORIZONTAL

        val contentParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        contentParams.setMargins(dp(14), dp(22), dp(14), dp(8))
        root.addView(content, contentParams)

        // لوگوی اختصاصی هندسی
        val logo = ArcadeLogoView(this)

        val logoParams = LinearLayout.LayoutParams(dp(105), dp(105))
        logoParams.gravity = Gravity.CENTER_HORIZONTAL
        content.addView(logo, logoParams)

        // انیمیشن ورود لوگو
        logo.alpha = 0f
        logo.scaleX = 0.65f
        logo.scaleY = 0.65f

        logo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(850)
            .start()

        // نام اختصاصی
        val title = ArcadeWordmarkView(this)

        val titleParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dp(58)
        )
        titleParams.topMargin = dp(2)
        content.addView(title, titleParams)

        title.alpha = 0f
        title.translationY = dp(15).toFloat()

        title.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(700)
            .setStartDelay(300)
            .start()

        // زیرعنوان
        val subtitle = TextView(this)
        subtitle.text = "سه بازی مینیمال، همیشه همراهت"
        subtitle.textSize = 17f
        subtitle.setTextColor(Color.rgb(70, 75, 82))
        subtitle.gravity = Gravity.CENTER
        subtitle.typeface = Typeface.create("sans", Typeface.NORMAL)

        val subtitleParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dp(40)
        )
        content.addView(subtitle, subtitleParams)

        subtitle.alpha = 0f
        subtitle.animate()
            .alpha(1f)
            .setDuration(600)
            .setStartDelay(500)
            .start()

        // فاصله
        val spacer = Space(this)
        content.addView(
            spacer,
            LinearLayout.LayoutParams(
                1,
                dp(12)
            )
        )

        // دکمه‌ها
        addGameButton(
            content,
            "SNAKE",
            "مار",
            "●",
            0
        ) {
            showSnake()
        }

        addGameButton(
            content,
            "BREAKOUT",
            "آجرشکن",
            "■",
            1
        ) {
            showBreakout()
        }

        addGameButton(
            content,
            "FLAPPY",
            "پرواز",
            "◆",
            2
        ) {
            showFlappy()
        }

        addGameButton(
            content,
            "TIC-TAC-TOE",
            "دوز",
            "×",
            3
        ) {
            showTicTacToe()
        }

        // فضای خالی
        val bottomSpace = Space(this)

        val bottomParams = LinearLayout.LayoutParams(
            1,
            0
        )
        bottomParams.weight = 1f
        content.addView(bottomSpace, bottomParams)

        val footer = TextView(this)
        footer.text = "✓  رکوردها روی همین دستگاه ذخیره می‌شوند"
        footer.textSize = 16f
        footer.setTextColor(Color.rgb(145, 148, 153))
        footer.gravity = Gravity.CENTER
        footer.typeface = Typeface.DEFAULT

        content.addView(
            footer,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(45)
            )
        )

        setContentView(root)
    }

    private fun addGameButton(
        parent: LinearLayout,
        title: String,
        persian: String,
        symbol: String,
        index: Int,
        action: () -> Unit
    ) {

        val button = LinearLayout(this)
        button.orientation = LinearLayout.HORIZONTAL
        button.gravity = Gravity.CENTER_VERTICAL
        button.setPadding(dp(18), 0, dp(18), 0)

        val bg = GradientDrawable()
        bg.setColor(navy)
        bg.cornerRadius = dp(19).toFloat()
        button.background = bg

        button.isClickable = true
        button.isFocusable = true

        val icon = TextView(this)
        icon.text = symbol
        icon.textSize = 22f
        icon.setTextColor(Color.WHITE)
        icon.gravity = Gravity.CENTER

        button.addView(
            icon,
            LinearLayout.LayoutParams(
                dp(35),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )

        val textContainer = LinearLayout(this)
        textContainer.orientation = LinearLayout.VERTICAL
        textContainer.gravity = Gravity.CENTER

        val mainText = TextView(this)
        mainText.text = title
        mainText.textSize = 18f
        mainText.setTextColor(Color.WHITE)
        mainText.typeface = Typeface.create(
            Typeface.DEFAULT,
            Typeface.BOLD
        )
        mainText.gravity = Gravity.CENTER

        val subText = TextView(this)
        subText.text = persian
        subText.textSize = 11f
        subText.setTextColor(Color.rgb(205, 215, 225))
        subText.gravity = Gravity.CENTER

        textContainer.addView(
            mainText,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(25)
            )
        )

        textContainer.addView(
            subText,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(20)
            )
        )

        button.addView(
            textContainer,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
            )
        )

        val arrow = TextView(this)
        arrow.text = "›"
        arrow.textSize = 30f
        arrow.setTextColor(Color.WHITE)
        arrow.gravity = Gravity.CENTER

        button.addView(
            arrow,
            LinearLayout.LayoutParams(
                dp(30),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dp(58)
        )

        params.topMargin = if (index == 0) dp(5) else dp(10)

        parent.addView(button, params)

        button.alpha = 0f
        button.translationY = dp(20).toFloat()

        button.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(450)
            .setStartDelay((650 + index * 100).toLong())
            .start()

        button.setOnTouchListener { v, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    v.animate()
                        .scaleX(0.97f)
                        .scaleY(0.97f)
                        .setDuration(80)
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
            action()
        }
    }

    // ---------------------------------------------------------
    // GAME SCREEN
    // ---------------------------------------------------------

    private fun gameContainer(
        title: String,
        gameView: View
    ): FrameLayout {

        val frame = FrameLayout(this)
        frame.setBackgroundColor(background)

        val top = LinearLayout(this)
        top.orientation = LinearLayout.HORIZONTAL
        top.gravity = Gravity.CENTER_VERTICAL

        val back = TextView(this)
        back.text = "‹"
        back.textSize = 42f
        back.setTextColor(navy)
        back.gravity = Gravity.CENTER
        back.setPadding(0, 0, 0, dp(4))

        back.setOnClickListener {
            showHome()
        }

        top.addView(
            back,
            LinearLayout.LayoutParams(
                dp(58),
                dp(60)
            )
        )

        val titleView = TextView(this)
        titleView.text = title
        titleView.textSize = 25f
        titleView.setTextColor(navy)
        titleView.typeface = Typeface.create(
            Typeface.DEFAULT,
            Typeface.BOLD
        )
        titleView.gravity = Gravity.CENTER

        top.addView(
            titleView,
            LinearLayout.LayoutParams(
                0,
                dp(60),
                1f
            )
        )

        val empty = Space(this)

        top.addView(
            empty,
            LinearLayout.LayoutParams(
                dp(58),
                dp(60)
            )
        )

        val topParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            dp(65)
        )

        frame.addView(top, topParams)

        val gameParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        gameParams.topMargin = dp(65)
        gameParams.bottomMargin = dp(8)
        gameParams.leftMargin = dp(8)
        gameParams.rightMargin = dp(8)

        frame.addView(gameView, gameParams)

        return frame
    }

    // ---------------------------------------------------------
    // SNAKE
    // ---------------------------------------------------------

    private fun showSnake() {

        val game = SnakeView(this)

        setContentView(
            gameContainer(
                "SNAKE",
                game
            )
        )

        game.start()

        game.alpha = 0f
        game.scaleX = 0.96f
        game.scaleY = 0.96f

        game.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(350)
            .start()
    }

    private class SnakeView(context: Context) : View(context) {

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        private val cols = 18
        private val rows = 25

        private val snake = mutableListOf<Pair<Int, Int>>()

        private var food = Pair(10, 10)

        private var dx = 1
        private var dy = 0

        private var nextDx = 1
        private var nextDy = 0

        private var score = 0
        private var running = false
        private var gameOver = false

        private var lastX = 0f
        private var lastY = 0f

        private val prefs =
            context.getSharedPreferences("records", Context.MODE_PRIVATE)

        init {
            setBackgroundColor(Color.rgb(247, 248, 250))
            reset()
        }

        fun start() {
            running = true

            post(object : Runnable {
                override fun run() {

                    if (running && !gameOver) {
                        update()
                        invalidate()
                        postDelayed(this, 145)
                    }
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

            nextDx = 1
            nextDy = 0

            score = 0
            gameOver = false

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

            dx = nextDx
            dy = nextDy

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

                snake.removeAt(snake.lastIndex)
            }
        }

        private fun endGame() {

            gameOver = true
            running = false

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

            val cell =
                min(
                    (w - 20f) / cols,
                    (h - 105f) / rows
                )

            val boardW = cell * cols
            val boardH = cell * rows

            val left = (w - boardW) / 2f
            val top = 65f

            // امتیاز
            paint.color = Color.rgb(60, 68, 77)
            paint.textSize = 18f
            paint.typeface = Typeface.DEFAULT_BOLD

            canvas.drawText(
                "SCORE  $score",
                left,
                32f,
                paint
            )

            val best =
                prefs.getInt("snake_best", 0)

            paint.color = Color.rgb(125, 130, 138)
            paint.textSize = 14f
            paint.typeface = Typeface.DEFAULT

            canvas.drawText(
                "BEST  $best",
                left + boardW - 75f,
                32f,
                paint
            )

            // صفحه
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

            // خطوط خیلی ظریف
            paint.color = Color.rgb(238, 240, 243)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f

            for (i in 0..cols) {

                val x = left + i * cell

                canvas.drawLine(
                    x,
                    top,
                    x,
                    top + boardH,
                    paint
                )
            }

            for (i in 0..rows) {

                val y = top + i * cell

                canvas.drawLine(
                    left,
                    y,
                    left + boardW,
                    y,
                    paint
                )
            }

            paint.style = Paint.Style.FILL

            // غذا
            paint.color = Color.rgb(40, 180, 130)

            canvas.drawCircle(
                left + food.first * cell + cell / 2f,
                top + food.second * cell + cell / 2f,
                cell * .34f,
                paint
            )

            // مار
            snake.forEachIndexed { index, p ->

                paint.color =
                    if (index == 0)
                        Color.rgb(25, 49, 79)
                    else
                        Color.rgb(50, 79, 110)

                val x = left + p.first * cell
                val y = top + p.second * cell

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

            if (gameOver) {

                paint.color = Color.argb(
                    225,
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

                paint.color = Color.rgb(25, 49, 79)
                paint.textAlign = Paint.Align.CENTER
                paint.textSize = 27f
                paint.typeface = Typeface.DEFAULT_BOLD

                canvas.drawText(
                    "GAME OVER",
                    w / 2f,
                    top + boardH / 2f - 15f,
                    paint
                )

                paint.textSize = 17f
                paint.typeface = Typeface.DEFAULT

                canvas.drawText(
                    "Tap to play again",
                    w / 2f,
                    top + boardH / 2f + 22f,
                    paint
                )

                paint.textAlign = Paint.Align.LEFT
            }
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    lastX = event.x
                    lastY = event.y

                    if (gameOver) {
                        reset()
                        start()
                    }

                    return true
                }

                MotionEvent.ACTION_UP -> {

                    val diffX = event.x - lastX
                    val diffY = event.y - lastY

                    if (abs(diffX) > abs(diffY)) {

                        if (diffX > 30 && dx != -1) {
                            nextDx = 1
                            nextDy = 0
                        }

                        if (diffX < -30 && dx != 1) {
                            nextDx = -1
                            nextDy = 0
                        }

                    } else {

                        if (diffY > 30 && dy != -1) {
                            nextDx = 0
                            nextDy = 1
                        }

                        if (diffY < -30 && dy != 1) {
                            nextDx = 0
                            nextDy = -1
                        }
                    }

                    return true
                }
            }

            return true
        }
    }

    // ---------------------------------------------------------
    // BREAKOUT
    // ---------------------------------------------------------

    private fun showBreakout() {

        val game = BreakoutView(this)

        setContentView(
            gameContainer(
                "BREAKOUT",
                game
            )
        )

        game.start()
    }

    private class BreakoutView(context: Context) : View(context) {

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        private var paddleX = 0f

        private var ballX = 0f
        private var ballY = 0f

        private var vx = 6f
        private var vy = -8f

        private var score = 0
        private var running = false
        private var over = false

        private val bricks = ArrayList<RectF>()

        private val prefs =
            context.getSharedPreferences("records", Context.MODE_PRIVATE)

        init {
            setBackgroundColor(Color.rgb(247, 248, 250))
        }

        fun start() {

            post {

                reset()

                running = true

                post(object : Runnable {
                    override fun run() {

                        if (running && !over) {
                            update()
                            invalidate()
                            postDelayed(this, 16)
                        }
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

            vx = 6f
            vy = -8f

            bricks.clear()

            val gap = 8f
            val bw = (width - 40f - gap * 4) / 5f

            for (r in 0 until 5) {

                for (c in 0 until 5) {

                    val x =
                        20f + c * (bw + gap)

                    val y =
                        75f + r * 35f

                    bricks.add(
                        RectF(
                            x,
                            y,
                            x + bw,
                            y + 27f
                        )
                    )
                }
            }
        }

        private fun update() {

            ballX += vx
            ballY += vy

            if (ballX < 8f) {
                ballX = 8f
                vx = abs(vx)
            }

            if (ballX > width - 8f) {
                ballX = width - 8f
                vx = -abs(vx)
            }

            if (ballY < 60f) {
                ballY = 60f
                vy = abs(vy)
            }

            val paddleY = height - 75f

            if (
                ballY + 10f >= paddleY &&
                ballY + 10f <= paddleY + 25f &&
                ballX >= paddleX - 55f &&
                ballX <= paddleX + 55f &&
                vy > 0
            ) {
                vy = -abs(vy)
            }

            val iterator = bricks.iterator()

            while (iterator.hasNext()) {

                val brick = iterator.next()

                if (
                    ballX >= brick.left &&
                    ballX <= brick.right &&
                    ballY >= brick.top &&
                    ballY <= brick.bottom
                ) {

                    iterator.remove()

                    vy *= -1f
                    score++

                    break
                }
            }

            if (bricks.isEmpty()) {
                over = true
                running = false
            }

            if (ballY > height + 30f) {

                over = true
                running = false

                val old =
                    prefs.getInt("breakout_best", 0)

                if (score > old) {
                    prefs.edit()
                        .putInt("breakout_best", score)
                        .apply()
                }
            }
        }

        override fun onDraw(canvas: Canvas) {

            super.onDraw(canvas)

            paint.color = Color.rgb(55, 63, 72)
            paint.textSize = 18f
            paint.typeface = Typeface.DEFAULT_BOLD

            canvas.drawText(
                "SCORE  $score",
                20f,
                35f,
                paint
            )

            // bricks
            bricks.forEachIndexed { i, rect ->

                paint.color =
                    if (i % 2 == 0)
                        Color.rgb(25, 49, 79)
                    else
                        Color.rgb(47, 76, 108)

                canvas.drawRoundRect(
                    rect,
                    7f,
                    7f,
                    paint
                )
            }

            // paddle
            paint.color = Color.rgb(25, 49, 79)

            canvas.drawRoundRect(
                paddleX - 55f,
                height - 75f,
                paddleX + 55f,
                height - 55f,
                10f,
                10f,
                paint
            )

            // ball
            paint.color = Color.rgb(40, 180, 130)

            canvas.drawCircle(
                ballX,
                ballY,
                10f,
                paint
            )

            if (over) {

                paint.color = Color.argb(
                    225,
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

                paint.color = Color.rgb(25, 49, 79)
                paint.textAlign = Paint.Align.CENTER
                paint.textSize = 28f
                paint.typeface = Typeface.DEFAULT_BOLD

                canvas.drawText(
                    if (bricks.isEmpty()) "YOU WIN" else "GAME OVER",
                    width / 2f,
                    height / 2f,
                    paint
                )

                paint.textSize = 16f
                paint.typeface = Typeface.DEFAULT

                canvas.drawText(
                    "Tap to restart",
                    width / 2f,
                    height / 2f + 35f,
                    paint
                )

                paint.textAlign = Paint.Align.LEFT
            }
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    if (over) {
                        reset()
                        start()
                        return true
                    }

                    paddleX = event.x
                    invalidate()
                    return true
                }

                MotionEvent.ACTION_MOVE -> {

                    paddleX = event.x.coerceIn(
                        60f,
                        width - 60f
                    )

                    invalidate()
                    return true
                }
            }

            return true
        }
    }

    // ---------------------------------------------------------
    // FLAPPY
    // ---------------------------------------------------------

    private fun showFlappy() {

        val game = FlappyView(this)

        setContentView(
            gameContainer(
                "FLAPPY",
                game
            )
        )

        game.start()
    }

    private class FlappyView(context: Context) : View(context) {

        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        private var birdX = 0f
        private var birdY = 0f

        private var velocity = 0f

        private var pipeX = 0f
        private var gapY = 0f

        private var score = 0

        private var running = false
        private var over = false

        private var lastPipe = 0f

        private val prefs =
            context.getSharedPreferences("records", Context.MODE_PRIVATE)

        fun start() {

            post {

                reset()

                running = true

                post(object : Runnable {
                    override fun run() {

                        if (running && !over) {

                            update()
                            invalidate()

                            postDelayed(this, 16)
                        }
                    }
                })
            }
        }

        private fun reset() {

            birdX = width * .28f
            birdY = height * .45f

            velocity = 0f

            pipeX = width + 100f
            gapY = height * .45f

            score = 0

            over = false
            lastPipe = pipeX
        }

        private fun flap() {
            velocity = -9.5f
        }

        private fun update() {

            velocity += .42f
            birdY += velocity

            pipeX -= 4.8f

            if (pipeX < -70f) {

                pipeX = width + 50f

                gapY = Random.nextInt(
                    max(130, height / 4),
                    max(
                        131,
                        height * 3 / 4
                    )
                ).toFloat()

                score++
            }

            val birdRadius = 17f

            if (
                birdY - birdRadius < 60f ||
                birdY + birdRadius > height
            ) {
                endGame()
                return
            }

            val gap = 145f
            val pipeWidth = 60f

            val hitX =
                birdX + birdRadius > pipeX &&
                birdX - birdRadius < pipeX + pipeWidth

            val hitY =
                birdY - birdRadius < gapY - gap / 2f ||
                birdY + birdRadius > gapY + gap / 2f

            if (hitX && hitY) {
                endGame()
            }
        }

        private fun endGame() {

            running = false
            over = true

            val old =
                prefs.getInt("flappy_best", 0)

            if (score > old) {

                prefs.edit()
                    .putInt("flappy_best", score)
                    .apply()
            }
        }

        override fun onDraw(canvas: Canvas) {

            super.onDraw(canvas)

            // سقف
            paint.color = Color.rgb(225, 229, 234)

            canvas.drawRect(
                0f,
                55f,
                width.toFloat(),
                60f,
                paint
            )

            // امتیاز
            paint.color = Color.rgb(55, 63, 72)
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = 24f
            paint.typeface = Typeface.DEFAULT_BOLD

            canvas.drawText(
                "$score",
                width / 2f,
                38f,
                paint
            )

            // pipe
            val gap = 145f

            paint.color = Color.rgb(25, 49, 79)

            canvas.drawRoundRect(
                pipeX,
                60f,
                pipeX + 60f,
                gapY - gap / 2f,
                10f,
                10f,
                paint
            )

            canvas.drawRoundRect(
                pipeX,
                gapY + gap / 2f,
                pipeX + 60f,
                height.toFloat(),
                10f,
                10f,
                paint
            )

            // bird
            paint.color = Color.rgb(40, 180, 130)

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

            paint.color = Color.rgb(30, 35, 40)

            canvas.drawCircle(
                birdX + 7f,
                birdY - 5f,
                2f,
                paint
            )

            if (over) {

                paint.color = Color.argb(
                    225,
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

                paint.color = Color.rgb(25, 49, 79)
                paint.textSize = 28f
                paint.typeface = Typeface.DEFAULT_BOLD

                canvas.drawText(
                    "GAME OVER",
                    width / 2f,
                    height / 2f,
                    paint
                )

                paint.textSize = 16f
               
