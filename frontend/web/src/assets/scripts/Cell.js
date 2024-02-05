import AcGameObject from "./AcGameObject";

//画一个球
export default class Cell extends AcGameObject {
  constructor(r, c) {
    super();
    this.r = r; //算法中的坐标 起点为左上角 竖为x 横为y
    this.c = c;
    this.x = this.c + 0.5; //转为canvas坐标 起点为左上角 横为x 竖为y
    this.y = this.r + 0.5;
  }
}
