import AcGameObject from "./AcGameObject";

//画一个球
export default class Cell extends AcGameObject {
  constructor(r, c) {
    super();
    this.r = r;
    this.c = c;
    this.x = this.c + 0.5; //转为canvas坐标
    this.y = this.r + 0.5;
  }
}
