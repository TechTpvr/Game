package com.pocketarcade.app

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.content.*
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
        root.setBackgroundColor(Color.rgb(246,247,249))
        return root
    }

    private fun txt(s:String, size:Float, color:Int=Color.rgb(23,42,70), bold:Boolean=false): android.widget.TextView {
        return android.widget.TextView(this).apply {
            text=s; textSize=size; setTextColor(color)
            gravity=Gravity.CENTER
            if (bold) typeface=Typeface.create("sans",Typeface.BOLD)
        }
    }

    private fun button(label:String, action:()->Unit): android.widget.TextView {
        val b=txt(label,16f,Color.WHITE,true)
        b.setPadding(24,18,24,18)
        b.background=GradientDrawable().apply {
            cornerRadius=28f; setColor(Color.rgb(23,42,70))
        }
        b.setOnClickListener { action() }
        return b
    }

    private fun add(v:View, w:Int, h:Int, gravity:Int, mt:Int=0) {
        val lp=FrameLayout.LayoutParams(w,h,gravity); lp.setMargins(20,mt,20,20); root.addView(v,lp)
    }

    private fun showHome() {
        root=base(); setContentView(root)
        val logo=LogoView(this)
        add(logo,180,180,Gravity.TOP or Gravity.CENTER_HORIZONTAL,48)

        val title=txt("POCKET ARCADE",30f,Color.rgb(23,42,70),true)
        add(title, -1,70,Gravity.TOP or Gravity.CENTER_HORIZONTAL,230)
        val sub=txt("سه بازی مینیمال، همیشه همراهت",15f,Color.DKGRAY)
        add(sub,-1,50,Gravity.TOP or Gravity.CENTER_HORIZONTAL,292)

        val snake=button("🐍  مار  •  Snake") { openGame("snake") }
        add(snake,-1,70,Gravity.TOP,365)
        val breakout=button("◼  بریک‌اوت  •  Breakout") { openGame("breakout") }
        add(breakout,-1,70,Gravity.TOP,450)
        val flappy=button("●  فلپی  •  Flappy") { openGame("flappy") }
        add(flappy,-1,70,Gravity.TOP,535)
        val t=button("✕  دوز  •  Tic-Tac-Toe") { openGame("ttt") }
        add(t,-1,70,Gravity.TOP,620)

        val records=txt("رکوردها روی همین دستگاه ذخیره می‌شوند ✓",13f,Color.GRAY)
        add(records,-1,45,Gravity.BOTTOM)
    }

    private fun openGame(type:String) {
        if(type=="ttt") { showTTT(); return }
        root=base(); setContentView(root)
        val back=txt("‹",42f,Color.rgb(23,42,70),true)
        back.setOnClickListener { showHome() }
        add(back,70,70,Gravity.TOP or Gravity.START,24)
        val title=txt(when(type){"snake"->"SNAKE";"breakout"->"BREAKOUT";else->"FLAPPY"},22f,Color.rgb(23,42,70),true)
        add(title, -1,70,Gravity.TOP or Gravity.CENTER_HORIZONTAL,24)
        val board=GameView(this,type,prefs) { showHome() }
        val lp=FrameLayout.LayoutParams(-1,0); lp.gravity=Gravity.TOP; lp.topMargin=100; lp.bottomMargin=0
        root.addView(board,lp)
    }

    private fun showTTT() {
        root=base(); setContentView(root)
        val back=txt("‹",42f,Color.rgb(23,42,70),true); back.setOnClickListener{showHome()}
        add(back,70,70,Gravity.TOP or Gravity.START,24)
        add(txt("TIC-TAC-TOE",22f,Color.rgb(23,42,70),true),-1,70,Gravity.TOP,24)
        val board=TTTView(this,prefs)
        val lp=FrameLayout.LayoutParams(-1,520); lp.gravity=Gravity.TOP; lp.topMargin=115; lp.setMargins(25,115,25,0)
        root.addView(board,lp)
    }
}

class LogoView(c:Context): View(c) {
    private val p=Paint(1)
    override fun onDraw(canvas:Canvas) {
        super.onDraw(canvas)
        val cx=width/2f; val cy=height/2f
        p.color=Color.rgb(23,42,70); p.style=Paint.Style.FILL
        canvas.drawCircle(cx,cy,72f,p)
        p.color=Color.WHITE
        canvas.drawRoundRect(cx-42,cy-42,cx+42,cy+42,18f,18f,p)
        p.color=Color.rgb(23,42,70)
        canvas.drawCircle(cx-16,cy-10,8f,p); canvas.drawCircle(cx+16,cy-10,8f,p)
        p.style=Paint.Style.STROKE; p.strokeWidth=8f
        canvas.drawArc(cx-30,cy-4,cx+30,cy+42,20f,140f,false,p)
    }
}

class GameView(
    c:Context, private val type:String,
    private val prefs:android.content.SharedPreferences,
    private val home:()->Unit
): View(c) {
    private val paint=Paint(1)
    private val navy=Color.rgb(23,42,70)
    private var score=0
    private var best=prefs.getInt("best_$type",0)
    private var running=false
    private var over=false
    private var last=System.currentTimeMillis()

    // Snake
    private val snake=ArrayList<Point>()
    private var dir=Point(1,0)
    private var food=Point(10,10)

    // Flappy
    private var birdY=0f; private var vel=0f; private var pipeX=0f; private var gapY=0f
    // Breakout
    private var bx=0f; private var by=0f; private var bvx=5f; private var bvy=-7f; private var paddleX=0f
    private val bricks=ArrayList<RectF>()

    init {
        isFocusable=true
        if(type=="snake") resetSnake()
        if(type=="flappy") resetFlappy()
        if(type=="breakout") resetBreakout()
    }

    private fun resetSnake(){
        snake.clear(); snake.add(Point(8,12));snake.add(Point(7,12));snake.add(Point(6,12))
        dir=Point(1,0); food=Point(14,8)
    }
    private fun resetFlappy(){birdY=height/2f;vel=0f;pipeX=width.toFloat();gapY=height/2f}
    private fun resetBreakout(){
        bricks.clear()
        val cols=6
        for(r in 0..4) for(col in 0 until cols) bricks.add(RectF(col*(width/cols.toFloat())+8,r*34f+30,col*(width/cols.toFloat())+width/cols.toFloat()-8,r*34f+58))
        bx=width/2f; by=height-180f; paddleX=width/2f
    }

    override fun onDraw(c:Canvas){
        super.onDraw(c)
        c.drawColor(Color.rgb(246,247,249))
        paint.color=navy; paint.style=Paint.Style.FILL
        paint.textSize=18f
        c.drawText("Score  $score",25f,35f,paint)
        c.drawText("Best  $best",width-120f,35f,paint)
        if(type=="snake") drawSnake(c)
        if(type=="flappy") drawFlappy(c)
        if(type=="breakout") drawBreakout(c)
        if(!running){
            paint.color=navy; paint.textSize=24f
            c.drawText(if(over)"GAME OVER" else "TAP TO START",width/2f-75f,height-90f,paint)
            paint.textSize=14f; c.drawText("لمس کن / سوایپ کن",width/2f-55f,height-55f,paint)
        }
    }

    private fun drawSnake(c:Canvas){
        val cols=18; val rows=28; val cell=min(width/cols.toFloat(),(height-70)/rows.toFloat())
        val ox=(width-cols*cell)/2; val oy=55f
        paint.color=Color.rgb(229,232,236)
        for(i in 1 until cols) c.drawLine(ox+i*cell,oy,ox+i*cell,oy+rows*cell,paint)
        for(i in 1 until rows) c.drawLine(ox,oy+i*cell,ox+cols*cell,oy+i*cell,paint)
        paint.color=navy
        snake.forEachIndexed{idx,p-> c.drawRoundRect(ox+p.x*cell+2,oy+p.y*cell+2,ox+(p.x+1)*cell-2,oy+(p.y+1)*cell-2,7f,7f,paint)}
        paint.color=Color.rgb(65,92,125)
        c.drawCircle(ox+(food.x+.5f)*cell,oy+(food.y+.5f)*cell,cell*.28f,paint)
    }

    private fun drawFlappy(c:Canvas){
        paint.color=navy
        c.drawCircle(width*.3f,birdY,22f,paint)
        paint.color=Color.rgb(65,92,125)
        c.drawRect(pipeX,0f,pipeX+58f,gapY-85f,paint)
        c.drawRect(pipeX,gapY+85f,pipeX+58f,height.toFloat(),paint)
        paint.color=navy; c.drawText("برای پرواز ضربه بزن",width/2f-75f,height-50f,paint)
    }

    private fun drawBreakout(c:Canvas){
        paint.color=Color.rgb(65,92,125)
        bricks.forEach{c.drawRoundRect(it,8f,8f,paint)}
        paint.color=navy
        c.drawCircle(bx,by,10f,paint)
        c.drawRoundRect(paddleX-55,height-55,paddleX+55,height-35,12f,12f,paint)
        paint.color=navy; paint.textSize=13f
        c.drawText("پد را با انگشت جابه‌جا کن",width/2f-70,height-10f,paint)
    }

    private fun gameOver(){
        running=false; over=true
        if(score>best){best=score;prefs.edit().putInt("best_$type",best).apply()}
        invalidate()
    }

    private fun loop(){
        if(!running)return
        val now=System.currentTimeMillis()
        if(now-last>if(type=="snake")120L else 16L){
            last=now; update()
            invalidate()
        }
        postDelayed({loop()},16)
    }

    private fun update(){
        if(type=="snake"){
            val head=snake[0]; val n=Point(head.x+dir.x,head.y+dir.y)
            if(n.x<0||n.y<0||n.x>=18||n.y>=28||snake.contains(n)){gameOver();return}
            snake.add(0,n)
            if(n==food){score++; var f:Point; do{f=Point(Random.nextInt(18),Random.nextInt(28))}while(snake.contains(f));food=f}
            else snake.removeAt(snake.size-1)
        } else if(type=="flappy"){
            vel+=0.42f; birdY+=vel; pipeX-=5f
            if(pipeX<-70){pipeX=width.toFloat();gapY=100+Random.nextFloat()*(height-250);score++}
            val bx0=width*.3f
            if(birdY<0||birdY>height|| (bx0+22>pipeX&&bx0-22<pipeX+58&&(birdY<gapY-85||birdY>gapY+85))){gameOver()}
        } else {
            bx+=bvx;by+=bvy
            if(bx<10||bx>width-10)bvx=-bvx
            if(by<10)bvy=-bvy
            if(by>height-80 && bx in (paddleX-70)..(paddleX+70)) bvy=-abs(bvy)
            val hit=bricks.indexOfFirst{it.contains(bx,by)}
            if(hit>=0){bricks.removeAt(hit);bvy=-bvy;score++;if(bricks.isEmpty()){gameOver();return}}
            if(by>height){gameOver()}
        }
    }

    override fun onTouchEvent(e:android.view.MotionEvent):Boolean{
        when(e.actionMasked){
            MotionEvent.ACTION_DOWN->{
                if(!running){
                    if(over){score=0;over=false;if(type=="snake")resetSnake();if(type=="flappy")resetFlappy();if(type=="breakout")resetBreakout()}
                    running=true;last=System.currentTimeMillis();loop()
                    if(type=="flappy")vel=-8f
                } else if(type=="flappy") vel=-8f
                return true
            }
            MotionEvent.ACTION_MOVE->{
                if(type=="breakout") paddleX=e.x
                else if(type=="snake"){
                    // handled on release by comparing gesture
                }
                return true
            }
            MotionEvent.ACTION_UP->{
                if(type=="snake"){
                    val dx=e.x-downX; val dy=e.y-downY
                    if(abs(dx)>abs(dy)) dir=if(dx>0)Point(1,0) else Point(-1,0)
                    else dir=if(dy>0)Point(0,1) else Point(0,-1)
                }
                return true
            }
        }
        return true
    }
    private var downX=0f; private var downY=0f
    override fun dispatchTouchEvent(e:MotionEvent):Boolean{
        if(e.actionMasked==MotionEvent.ACTION_DOWN){downX=e.x;downY=e.y}
        return super.dispatchTouchEvent(e)
    }
}

class TTTView(c:Context, private val prefs:android.content.SharedPreferences):View(c){
    private val p=Paint(1); private val b=IntArray(9); private var turn=1; private var done=false
    private val navy=Color.rgb(23,42,70)
    override fun onDraw(c:Canvas){
        c.drawColor(Color.rgb(246,247,249)); p.color=navy;p.strokeWidth=5f
        val s=width/3f
        c.drawLine(s,30f,s,height-30f,p);c.drawLine(2*s,30f,2*s,height-30f,p)
        c.drawLine(30f,s,width-30f,s,p);c.drawLine(30f,2*s,width-30f,2*s,p)
        p.textSize=70f;p.textAlign=Paint.Align.CENTER
        for(i in 0..8) if(b[i]!=0)c.drawText(if(b[i]==1)"X" else "O",(i%3+.5f)*s,(i/3+0.72f)*s,p)
        p.textSize=18f
        c.drawText(if(done)"دوباره لمس کن" else "نوبت: ${if(turn==1)"X" else "O"}",width/2f,height-8f,p)
    }
    override fun onTouchEvent(e:MotionEvent):Boolean{
        if(e.action!=MotionEvent.ACTION_DOWN)return true
        if(done){java.util.Arrays.fill(b,0);turn=1;done=false;invalidate();return true}
        val s=width/3f;val col=(e.x/s).toInt().coerceIn(0,2);val row=(e.y/s).toInt().coerceIn(0,2);val i=row*3+col
        if(b[i]!=0)return true
        b[i]=turn
        if(win(turn)||b.all{it!=0})done=true else turn=3-turn
        invalidate();return true
    }
    private fun win(x:Int)=listOf(intArrayOf(0,1,2),intArrayOf(3,4,5),intArrayOf(6,7,8),intArrayOf(0,3,6),intArrayOf(1,4,7),intArrayOf(2,5,8),intArrayOf(0,4,8),intArrayOf(2,4,6)).any{b[it[0]]==x&&b[it[1]]==x&&b[it[2]]==x}
}
