import AcGameObject from "./AcGameObject";
import Cell from "./Cell";
//画一条蛇 有Cell序列组成
export default class Snake extends AcGameObject {
  constructor(info, gameMap) {
    super();
    this.gameMap = gameMap;
    this.color = info.color;
    this.id = info.id;
    //存放蛇体 蛇头在前 尾部在后
    this.cells = [new Cell(info.r, info.c)];
    this.speed = info.speed; //蛇的每秒钟移动多少格
    this.direction = -1; //蛇的移动方向 -1表示无 0上 1右 2下 3左
    this.status = "idle"; //蛇的状态 idle表示静止 move表示移动 die表示死亡
    this.dr = [-1, 0, 1, 0];
    this.dc = [0, 1, 0, -1];
    this.step = 0; //走了几步 即第几回合
    this.next_cell = null; //蛇头的下一步位置
    this.eps = 1e-2; //允许的误差
    //蛇的眼睛相关参数
    this.eye_direction = 0;
    if (info.id == "1") this.eye_direction = 2; //眼睛初始方向 蛇0向上 蛇1向下
    this.eye_dx = [
      // 蛇眼不同方向x偏移量 (从圆心向左向右的偏移量)
      [-1, 1], //向上,右,左,下
      [1, 1],
      [-1, 1],
      [-1, -1],
    ];
    this.eye_dy = [
      // 蛇眼不同方向y偏移量
      [-1, -1],
      [-1, 1],
      [1, 1],
      [-1, 1],
    ];
  }
  //更新蛇的位置状态,进入下一步
  next_step() {
    const d = this.direction;
    this.next_cell = new Cell(
      this.cells[0].r + this.dr[d],
      this.cells[0].c + this.dc[d]
    );
    this.direction = -1;
    this.status = "move";
    this.step++;
    //删除蛇尾
    for (let i = this.cells.length; i > 0; i--) {
      // 初始元素不变 每一个元素往后移动一位,为新蛇头腾位置
      this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
    }
    console.log(
      "蛇：",
      this.id,
      "的移动方向为",
      d,
      "状态为: ",
      this.status,
      "新蛇头位置: ",
      this.next_cell
    );
    //下一位置非法时死亡
    if (!this.gameMap.check_valid(this.next_cell)) {
      this.status = "die";
    }
  }
  //让蛇进行移动
  update_move() {
    const a = this.next_cell.x - this.cells[0].x; //蛇头x方向上移动的距离
    const b = this.next_cell.y - this.cells[0].y; //蛇头y方向上移动的距离
    const distance = Math.sqrt(a * a + b * b); //蛇头移动的距离
    if (distance < this.eps) {
      //走到目标点 添加新蛇头
      this.cells[0] = this.next_cell;
      this.next_cell = null;
      this.status = "idle";
      if (!this.check_tail_increasing()) {
        //无需增长时彻底删除蛇尾
        this.cells.pop();
      }
    } else {
      //蛇头前移
      // 当前帧移动的距离，根据速度(speed)和时间差(deltaTime)计算得出
      // 一秒划分为了60帧 每帧的时间间隔为this.timedelta
      const move_distance = (this.speed * this.timedelta) / 1000;
      // 根据计算出的移动距离和蛇头移动的方向，更新蛇头的x坐标 move_distance*sin
      this.cells[0].x += (move_distance * a) / distance;
      // 根据计算出的移动距离和蛇头移动的方向，更新蛇头的y坐标 move_distance*cos
      this.cells[0].y += (move_distance * b) / distance;

      if (!this.check_tail_increasing()) {
        //无需增长时蛇尾也要相应的前移
        const k = this.cells.length;
        const tail = this.cells[k - 1],
          tail_target = this.cells[k - 2];
        const tail_dx = tail_target.x - tail.x;
        const tail_dy = tail_target.y - tail.y;
        //因为都是直线行走，蛇头蛇尾的距离是一样的
        tail.x += (move_distance * 3 * tail_dx) / distance;
        tail.y += (move_distance * 3 * tail_dy) / distance;
      }
    }
  }
  set_direction(d) {
    this.direction = d;
  }
  //判断蛇尾是否需要增长
  check_tail_increasing() {
    if (this.step <= 10) return true;
    if (this.step % 3 === 1) return true;
    return false;
  }
  render() {
    //画蛇
    const L = this.gameMap.L;
    const radius = 0.35 * L; //每个cell的半径
    const ctx = this.gameMap.ctx;
    ctx.fillStyle = this.color;
    if (this.status === "die") {
      ctx.fillStyle = "white";
    }
    for (const cell of this.cells) {
      ctx.beginPath();
      ctx.arc(cell.x * L, cell.y * L, radius, 0, Math.PI * 2);
      ctx.fill();
    }
    //美化蛇体 将相邻两个块用正方形覆盖
    for (let i = 0; i < this.cells.length - 1; i++) {
      let a = this.cells[i],
        b = this.cells[i + 1];
      //两个球重合时 跳过
      if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) {
        continue;
      }
      //两球垂直排列时
      if (Math.abs(a.x - b.x) < this.eps) {
        // 参数: x坐标, y坐标, 宽度, 高度
        ctx.fillRect(
          a.x * L - radius,
          Math.min(a.y, b.y) * L,
          radius * 2,
          Math.abs(a.y - b.y) * L
        );
      }
      //两球水平排列时
      if (Math.abs(a.y - b.y) < this.eps) {
        ctx.fillRect(
          Math.min(a.x, b.x) * L,
          a.y * L - radius,
          Math.abs(a.x - b.x) * L,
          radius * 2
        );
      }
    }

    //绘制蛇的眼睛
    ctx.fillStyle = "black";
    for (let i = 0; i < 2; i++) {
      const eye_x =
        (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
      const eye_y =
        (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;

      ctx.beginPath();
      ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
      ctx.fill();
    }
  }
  update() {
    this.render();
    if (this.status === "move") {
      this.update_move();
    }
  }
}
