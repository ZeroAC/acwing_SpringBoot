import AcGameObject from "./AcGameObject";
import Wall from "./Wall";
import Snake from "./Snake";
export default class GameMapObject extends AcGameObject {
  constructor(ctx, parent) {
    //画布ctx,游戏板
    super();
    this.ctx = ctx;
    this.parent = parent;
    this.L = 0; //地图每个格子的边长
    this.rows = 13; //地图行数
    this.cols = 14; //地图列数
    this.wallNum = 40; //障碍物数量
  }
  //判断是否连通
  is_connected(g, sx, sy, ex, ey) {
    if (sx == ex && sy == ey) return true;
    g[sx][sy] = true;
    const dx = [-1, 0, 1, 0];
    const dy = [0, 1, 0, -1];
    for (let i = 0; i < 4; i++) {
      let nx = sx + dx[i];
      let ny = sy + dy[i];
      if (!g[nx][ny] && this.is_connected(g, nx, ny, ex, ey)) return true;
    }
    g[sx][sy] = false;
    return false;
  }
  //创建障碍物墙
  generate_walls() {
    //创建一圈障碍物
    const g = [];
    for (let i = 0; i < this.rows; i++) {
      g[i] = [];
      for (let j = 0; j < this.cols; j++) {
        if (i == 0 || i == this.rows - 1 || j == 0 || j == this.cols - 1) {
          g[i][j] = true;
        }
      }
    }
    //创建内部障碍物(公平起见，中心对称)，但不能覆盖起点
    for (let i = 0; i < this.wallNum / 2; i++) {
      for (let j = 0; j < 1000; j++) {
        let x = Math.floor(Math.random() * this.rows);
        let y = Math.floor(Math.random() * this.cols);
        if ((x == this.rows - 2 && y == 1) || (x == 1 && y == this.cols - 2))
          continue;
        // 中心对称 让(x,y)先关于地图中心x轴对称,再关于中心点y轴对称->(x’,y’)
        if (x != y && !g[x][y] && !g[this.rows - 1 - x][this.cols - 1 - y]) {
          g[x][y] = g[this.rows - 1 - x][this.cols - 1 - y] = true;
          break;
        }
      }
    }
    const copy_g = JSON.parse(JSON.stringify(g));

    //判断从左下角到右上角能否连通
    if (!this.is_connected(copy_g, this.rows - 2, 1, 1, this.cols - 2))
      return false;
    for (let i = 0; i < this.rows; i++) {
      for (let j = 0; j < this.cols; j++) {
        if (g[i][j]) {
          new Wall(i, j, this);
        }
      }
    }
    return true;
  }
  start() {
    // 创建墙障碍物 墙本身有update方法 可以自动刷新
    // 为什么墙会覆盖地图？
    // 因为AcGame对象是先创建先push,然后从前往后依次刷新一遍
    // 而墙是在地图创建后才被创建的，所以墙会覆盖背景
    for (let i = 0; i < 10000; i++) {
      if (this.generate_walls()) break;
    }
    this.snakes = [
      //生成蛇0
      new Snake(
        { id: 0, r: this.rows - 2, c: 1, color: "#4876ec", speed: 5 },
        this
      ),
      //生成蛇1
      new Snake(
        { id: 1, r: 1, c: this.cols - 2, color: "#f94848", speed: 5 },
        this
      ),
    ];
    this.add_listener(); //监听蛇的移动事件
  }
  //检查两条蛇是否均处于静止且都已输入移动指令
  check_ready() {
    for (let i = 0; i < this.snakes.length; i++) {
      if (this.snakes[i].direction == -1) return false;
      if (this.snakes[i].status != "idle") return false;
    }
    return true;
  }
  //若蛇已准备好，则让二者一起移动，进入下一回合
  next_step() {
    for (const snake of this.snakes) {
      snake.next_step();
    }
  }
  add_listener() {
    this.ctx.canvas.focus();
    const [snake0, snake1] = this.snakes;
    this.ctx.canvas.addEventListener("keydown", (e) => {
      if (e.key === "w") snake0.set_direction(0);
      else if (e.key === "d") snake0.set_direction(1);
      else if (e.key === "s") snake0.set_direction(2);
      else if (e.key === "a") snake0.set_direction(3);
      else if (e.key === "ArrowUp") snake1.set_direction(0);
      else if (e.key === "ArrowRight") snake1.set_direction(1);
      else if (e.key === "ArrowDown") snake1.set_direction(2);
      else if (e.key === "ArrowLeft") snake1.set_direction(3);
    });
  }
  update_size() {
    //动态更新地图大小
    //地图边长应该为父元素能包含的最大矩形
    this.L = Math.min(
      this.parent.clientWidth / this.cols,
      this.parent.clientHeight / this.rows
    );
    this.ctx.canvas.width = this.L * this.cols;
    this.ctx.canvas.height = this.L * this.rows;
  }

  render_background() {
    //画颜色交替的格子
    for (let i = 0; i < this.rows; i++) {
      for (let j = 0; j < this.cols; j++) {
        if ((i + j) % 2 == 0) {
          this.ctx.fillStyle = "#AAD751";
        } else {
          this.ctx.fillStyle = "#A2D149";
        }
        //坐标系起点在左上角 横为x 竖为y
        this.ctx.fillRect(j * this.L, i * this.L, this.L, this.L);
      }
    }
  }

  render() {
    this.render_background();
    if (this.check_ready()) {
      this.next_step();
    }
  }

  update() {
    this.update_size();
    this.render();
  }
}
