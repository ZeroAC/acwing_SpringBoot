import AcGameObject from "./AcGameObject";

//画一片墙
export default class Wall extends AcGameObject {
  constructor(r, c, gameMap) {
    super();
    this.r = r;
    this.c = c;
    //这里gameMap中还是空的 不能直接赋值
    // this.L = gameMap.L;
    // this.ctx = gameMap.ctx;
    this.gameMap = gameMap;
  }
  render() {
    this.L = this.gameMap.L;
    this.ctx = this.gameMap.ctx;
    this.ctx.fillStyle = "#B47226";
    this.ctx.fillRect(this.c * this.L, this.r * this.L, this.L, this.L);
  }
  update() {
    this.render();
  }
}
