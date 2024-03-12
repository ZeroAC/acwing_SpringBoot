import AcGameObject from "./AcGameObject";
import Wall from "./Wall";
import Snake from "./Snake";
export default class GameMapObject extends AcGameObject {
  constructor(ctx, parent, store) {
    //画布ctx,游戏板
    super();
    this.ctx = ctx;
    this.parent = parent;
    this.rows = 13;
    this.cols = 14;
    this.L = 0; //地图每个格子的边长
    this.walls = [];
    this.store = store;
  }
  start() {
    // 创建墙障碍物 墙本身有update方法 可以自动刷新
    // 为什么墙会覆盖地图？
    // 因为AcGame对象是先创建先push,然后从前往后依次刷新一遍
    // 而墙是在地图创建后才被创建的，所以墙会覆盖背景
    const g = this.store.state.pk.gameMap;
    for (let i = 0; i < this.rows; i++) {
      for (let j = 0; j < this.cols; j++) {
        if (g[i][j]) {
          this.walls.push(new Wall(i, j, this));
        }
      }
    }
    this.snakes = [
      //生成蛇0
      new Snake(
        {
          id: this.store.state.pk.id,
          r: this.store.state.pk.sx,
          c: this.store.state.pk.sy,
          color: "#4876ec",
          speed: 5,
        },
        this
      ),
      //生成蛇1
      new Snake(
        {
          id: this.store.state.pk.opponentId,
          r: this.store.state.pk.opponentSx,
          c: this.store.state.pk.opponentSy,
          color: "#f94848",
          speed: 5,
        },
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
    this.ctx.canvas.addEventListener("keydown", (e) => {
      let d = -1;
      if (e.key === "w") d = 0;
      else if (e.key === "d") d = 1;
      else if (e.key === "s") d = 2;
      else if (e.key === "a") d = 3;
      if (d >= 0) {
        //此时蛇动了
        this.store.state.pk.socket.send(
          JSON.stringify({
            event: "move",
            direction: d,
          })
        );
      }
    });
  }
  //检测蛇头是否撞墙或者碰到两条蛇的身体
  check_valid(cell) {
    for (let wall of this.walls) {
      if (cell.r == wall.r && cell.c == wall.c) {
        return false;
      }
    }
    for (let snake of this.snakes) {
      let k = snake.cells.length;
      if (!snake.check_tail_increasing()) k--; //若尾部不再增加，则尾部是可以走的,不做检查
      for (let i = 1; i < k; i++) {
        if (cell.r == snake.cells[i].r && cell.c == snake.cells[i].c) {
          return false;
        }
      }
    }
    return true;
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
