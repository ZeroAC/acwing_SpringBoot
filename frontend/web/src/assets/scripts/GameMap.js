import AcGameObject from "./AcGameObject";

export default class GameMap extends AcGameObject {
  constructor(ctx, parent) {
    //画布ctx,游戏板
    super();
    this.ctx = ctx;
    this.parent = parent;
    this.L = 0; //地图每个格子的边长
    this.rows = 13; //地图行数
    this.cols = 13; //地图列数
  }
  start() {}

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
  update() {
    this.update_size();
    this.render();
  }

  render() {
    this.ctx.fillStyle = "green";
    this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
    //画地图
  }
}
