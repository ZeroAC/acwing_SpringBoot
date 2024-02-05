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
    } else {
      const move_distance = (this.speed * this.timedelta) / 1000; //当前帧移动的距离
      this.cells[0].x += (move_distance * a) / distance; //蛇头x方向上移动
      this.cells[0].y += (move_distance * b) / distance; //蛇头y方向上移动
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
    const ctx = this.gameMap.ctx;
    ctx.fillStyle = this.color;
    for (const cell of this.cells) {
      ctx.beginPath();
      ctx.arc(cell.x * L, cell.y * L, L / 2, 0, Math.PI * 2);
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
